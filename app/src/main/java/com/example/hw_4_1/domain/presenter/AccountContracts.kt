package com.example.hw_4_1.domain.presenter

import com.example.hw_4_1.data.model.Account

interface AccountContracts {

    interface View {
        fun showAccounts(accountList: List<Account>)
    }
    interface Presenter{
        fun loadAcoounts()
        fun addAccount(account: Account)
        fun updateAccountFully(updatedAccount: Account)
        fun updateAccountPartially(id: String, isCheked: Boolean)
        fun deleteAccount(id: String)
    }

}