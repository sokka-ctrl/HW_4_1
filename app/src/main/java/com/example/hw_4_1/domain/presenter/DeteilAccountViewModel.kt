package com.example.hw_4_1.domain.presenter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hw_4_1.data.model.Account
import com.example.hw_4_1.data.network.DeteilAccountsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel

class DeteilAccountViewModel @Inject constructor(
    private val DeteilAccountsApi: DeteilAccountsApi
): ViewModel() {
    private val _account = MutableLiveData<Account?>()
    val account: LiveData<Account?> = _account
    fun loadAccount(id: String) {
        DeteilAccountsApi.getAccount().enqueue(object : Callback<List<Account>> {
            override fun onResponse(call: Call<List<Account>>, response: Response<List<Account>>) {
                if (response.isSuccessful && response.body() != null) {
                    // Сначала логируем, что пришло
                    val list = response.body()!!
                    Log.d("ViewModel", "Accounts list = $list")

                    // Фильтруем по id
                    val acc = list.find { it.id == id }
                    if (acc != null) {
                        _account.postValue(acc) // используем postValue для асинхронного обновления
                    } else {
                        Log.e("ViewModel", "Account с id=$id не найден!")
                    }
                }
            }

            override fun onFailure(call: Call<List<Account>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


        fun updateAccountFully(updatedAccount: Account) {
        updatedAccount.id?.let {
            DeteilAccountsApi.updateAccountFully(it, updatedAccount)
                .handleAccountResponse()
        }
    }

         fun deleteAccount(id: String) {
             DeteilAccountsApi.deleteAccount(id)
            .handleAccountResponse()
    }
    private fun <T>Call<T>?.handleAccountResponse(
        onSuccess: (T) -> Unit = { loadAccount(it.toString())},
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
