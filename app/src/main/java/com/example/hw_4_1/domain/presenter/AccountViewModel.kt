package com.example.hw_4_1.domain.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hw_4_1.data.model.Account
import com.example.hw_4_1.data.model.AccountState
import com.example.hw_4_1.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountViewModel(): ViewModel(){
    private val _account = MutableLiveData<List<Account>>()
    val accounts: LiveData<List<Account>> = _account


     fun loadAcoounts() {
        ApiClient.accountsApi.getAccount().
        handleAccountResponse(
            onSuccess = { _account.value = it}
        )

    }

     fun addAccount(account: Account) {
        ApiClient.accountsApi.addAccount(account)
            .handleAccountResponse()
    }

    fun updateAccountFully(updatedAccount: Account) {
        updatedAccount.id?.let {
            ApiClient.accountsApi.updateAccountFully(it, updatedAccount)
                .handleAccountResponse()
        }
    }

     fun updateAccountPartially(id: String, isCheked: Boolean) {
        ApiClient.accountsApi.updateAccountPartially(id, AccountState(isCheked))
            .handleAccountResponse()
    }

     fun deleteAccount(id: String) {
        ApiClient.accountsApi.deleteAccount(id)
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