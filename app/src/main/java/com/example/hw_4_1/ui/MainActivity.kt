package com.example.hw_4_1.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw_4_1.R
import com.example.hw_4_1.data.model.Account
import com.example.hw_4_1.databinding.ActivityMainBinding
import com.example.hw_4_1.databinding.DialogAddBinding
import com.example.hw_4_1.domain.presenter.AccountContracts
import com.example.hw_4_1.domain.presenter.AccountPresenter
import com.example.hw_4_1.ui.adapter.AccountsAdapter

class MainActivity : AppCompatActivity(), AccountContracts.View {

    private lateinit var adapter: AccountsAdapter
    private lateinit var presenter: AccountPresenter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initAdapter()
        presenter = AccountPresenter(this)
        binding.btnCreate.setOnClickListener {
            showAddDialog()
        }
    }
    private fun showAddDialog(){
    val binding = DialogAddBinding.inflate(LayoutInflater.from(this))
    with(binding){
        AlertDialog.Builder(this@MainActivity)
            .setTitle("Добавление счета")
            .setView(binding.root)
            .setPositiveButton("Добавить") {_,_ ->
                val account = Account(
                    name = etName.text.toString(),
                    balance = etBalance.text.toString().toInt(),
                    currency = etCurrency.text.toString()
                )
                presenter.addAccount(account)
            }.show()
    }
    }

    override fun onResume() {
        super.onResume()
        presenter.loadAcoounts()
    }

    private fun initAdapter() = with(binding) {
        adapter = AccountsAdapter(
            onEdit = {
                showEditDialog(it)
            },
            onSwitchToogle = {id, isCheked ->
                presenter.updateAccountPartially(id, isCheked)
            },
            onDelete = {
                showDeleteDialog(it)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter
    }
    private fun showEditDialog(account: Account){
        val binding = DialogAddBinding.inflate(LayoutInflater.from(this))
        with(binding){

            account.run {

                etName.setText(name)
                etBalance.setText(balance.toString())
                etCurrency.setText(currency)

                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Изменение счета")
                    .setView(binding.root)
                    .setPositiveButton("изменить"){_,_ ->
                        val updatedAccount = account.copy(
                            name = etName.text.toString(),
                            balance = etBalance.text.toString().toInt(),
                            currency = etCurrency.text.toString()
                        )
                            presenter.updateAccountFully(updatedAccount)
                    }.show()
            }

        }
    }
    private fun showDeleteDialog(id: String){
        AlertDialog.Builder(this)
            .setTitle("Вы уверены?")
            .setMessage("Вы уверены что хотите удалить счет с индефикатором - ${id}")
            .setPositiveButton("Удалить"){_,_, ->
                presenter.deleteAccount(id)
            }
            .setNegativeButton("отмена"){_,_ ->

            }.show()
    }

    override fun showAccounts(accountsList: List<Account>) {
        adapter.submitList(accountsList)
    }
}
