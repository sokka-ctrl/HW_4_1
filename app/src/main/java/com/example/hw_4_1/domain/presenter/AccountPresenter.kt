package com.example.hw_4_1.domain.presenter

import com.example.hw_4_1.data.model.Account
import com.example.hw_4_1.data.model.AccountState
import com.example.hw_4_1.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountPresenter(private val view: AccountContracts.View): AccountContracts.Presenter{


    override fun loadAcoounts() {
        ApiClient.accountsApi.getAccount().enqueue(object : Callback<List<Account>>{
            override fun onResponse(
                call: Call<List<Account>?>,
                response: Response<List<Account>?>
            ) {
                if (response.isSuccessful) view.showAccounts(response.body() ?: emptyList())
            }

            override fun onFailure(
                call: Call<List<Account>?>,
                t: Throwable
            ) {

            }

        })

    }

    override fun addAccount(account: Account) {
        ApiClient.accountsApi.addAccount(account).enqueue(object : Callback<Unit>{
            override fun onResponse(
                call: Call<Unit?>,
                response: Response<Unit?>
            ) {
                if (response.isSuccessful) loadAcoounts()
            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {

            }

        })
    }

    override fun updateAccountFully(updatedAccount: Account) {
        updatedAccount.id?.let {
            ApiClient.accountsApi.updateAccountFully(it, updatedAccount).enqueue(object : Callback<Unit>{

                override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {
                    if (response.isSuccessful) loadAcoounts()
                }

                override fun onFailure(call: Call<Unit?>, t: Throwable) {
                }
            })
        }
    }

    override fun updateAccountPartially(id: String, isCheked: Boolean) {
        ApiClient.accountsApi.updateAccountPartially(id, AccountState(isCheked))
        .enqueue(object : Callback<Unit>{

            override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {
                if (response.isSuccessful) loadAcoounts()
            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
            }
        })
    }

    override fun deleteAccount(id: String) {
        ApiClient.accountsApi.deleteAccount(id).enqueue(object : Callback<Unit>{

            override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {
                if (response.isSuccessful) loadAcoounts()
            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
            }
        })
    }
}