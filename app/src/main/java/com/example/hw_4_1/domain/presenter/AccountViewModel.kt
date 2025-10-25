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
): ViewModel(){
    private val _account = MutableLiveData<List<Account>>()
    val accounts: LiveData<List<Account>> = _account


     fun loadAcoounts() {
        accountsApi.getAccount().
        handleAccountResponse(
            onSuccess = { _account.value = it}
        )

    }

     fun addAccount(account: Account) {
        accountsApi.addAccount(account)
            .handleAccountResponse()
    }

    fun updateAccountFully(updatedAccount: Account) {
        updatedAccount.id?.let {
            accountsApi.updateAccountFully(it, updatedAccount)
                .handleAccountResponse()
        }
    }

     fun updateAccountPartially(id: String, isCheked: Boolean) {
        accountsApi.updateAccountPartially(id, AccountState(isCheked))
            .handleAccountResponse()
    }

     fun deleteAccount(id: String) {
        accountsApi.deleteAccount(id)
            .handleAccountResponse()
    }


    private fun <T>Call<T>?.handleAccountResponse(
        onSuccess: (T) -> Unit = { loadAcoounts() },
        onError: (String) -> Unit   = {}
    ) {
        this?.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val result = response.body()
                if (result != null && response.isSuccessful) {
                    onSuccess(result)
                } else {
                    onError(response.code().toString())
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onError(t.message.toString())
            }

        }

        )
    }
}