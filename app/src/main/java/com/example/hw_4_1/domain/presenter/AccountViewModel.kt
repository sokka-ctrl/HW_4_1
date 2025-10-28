package com.example.hw_4_1.domain.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hw_4_1.data.model.Account
import com.example.hw_4_1.data.model.AccountState
import com.example.hw_4_1.data.network.AccountsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountsApi: AccountsApi
) : ViewModel() {

    private val _account = MutableLiveData<List<Account>>()
    val accounts: LiveData<List<Account>> = _account

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage


    fun loadAcoounts() {
        accountsApi.getAccount()
            .handleAccountResponse(
                onSuccess = { _account.value = it }
            )
    }

    fun addAccount(account: Account) {
        accountsApi.addAccount(account)
            .handleAccountResponse()
    }

    fun updateAccountPartially(id: String, isChecked: Boolean) {
        accountsApi.updateAccountPartially(id, AccountState(isChecked))
            .handleAccountResponse()
    }

    fun updateAccountFully(id: String, updatedAccount: Account) {
        accountsApi.updateAccountFully(id, updatedAccount)
            .handleAccountResponse()
    }

    fun deleteAccount(id: String) {
        accountsApi.deleteAccount(id)
            .handleAccountResponse()
    }

    private fun <T> Call<T>?.handleAccountResponse(
        onSuccess: (T) -> Unit = { loadAcoounts() },
        onError: (String) -> Unit = { _errorMessage.postValue(it) }
    ) {
        if (this == null) {
            _errorMessage.postValue("Ошибка: запрос не инициализирован")
            return
        }
        try {
            this.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            onSuccess(result)
                        } else {
                            _errorMessage.postValue("Пустой ответ от сервера")
                        }
                    } else {
                        _errorMessage.postValue("Ошибка сервера: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    _errorMessage.postValue("Нет соединения: ${t.localizedMessage}")
                }
            })
        } catch (e: Exception) {
            _errorMessage.postValue("Сетевая ошибка: ${e.localizedMessage}")
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
