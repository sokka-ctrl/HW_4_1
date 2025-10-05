package com.example.hw_4_1.domain.presenter

import com.example.hw_4_1.data.model.Account

class AccountPresenter(private val view: AccountContracts.View): AccountContracts.Presenter{
    override fun loadAcoounts() {
        val testMockList = arrayListOf<Account>()
        testMockList.add(Account(
            name = "O! Bank",
            balance =  1000,
            currency = "KGS"
        ))
        testMockList.add(Account(
            name = "M Bank",
            balance =  100,
            currency = "USD"
        ))
        testMockList.add(Account(
            name = "Optima Bank",
            balance =  10,
            currency = "EUR"
        ))
        view.showAccounts(testMockList)
    }
}