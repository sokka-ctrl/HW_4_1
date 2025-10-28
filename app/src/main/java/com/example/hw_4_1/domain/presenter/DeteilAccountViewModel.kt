package com.example.hw_4_1.domain.presenter

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
    private val _account = MutableLiveData<Account>()
    val account: LiveData<Account> = _account
    fun loadAcoount(id: String) {
        DeteilAccountsApi.getAccount(id).
        handleAccountResponse(
            onSuccess = { _account.value = it}
        )
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
        onSuccess: (T) -> Unit = { loadAcoount(it.toString())},
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
