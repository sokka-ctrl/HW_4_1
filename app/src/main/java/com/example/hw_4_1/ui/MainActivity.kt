package com.example.hw_4_1.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw_4_1.Deteil_Activity
import com.example.hw_4_1.data.model.Account
import com.example.hw_4_1.databinding.ActivityMainBinding
import com.example.hw_4_1.databinding.DialogAddBinding
import com.example.hw_4_1.domain.presenter.AccountViewModel
import com.example.hw_4_1.ui.adapter.AccountsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: AccountsAdapter
    private lateinit var binding: ActivityMainBinding
    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeLiveData()
        initAdapter()
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
                viewModel.addAccount(account)
            }.show()
    }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAcoounts()
    }

    private fun initAdapter() = with(binding) {
        adapter = AccountsAdapter(
            onClickCard = {
                DeteilScreenAction(it
                )
            },
            onSwitchToogle = {id, isCheked ->
                viewModel.updateAccountPartially(id, isCheked)
            }


        )
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter
    }

    private fun subscribeLiveData() {
        viewModel.accounts.observe(this) {
            adapter.submitList(it)
        }
    }


    private fun DeteilScreenAction(id: String?){
        val intent = Intent(this, Deteil_Activity:: class.java)
        intent.putExtra("accountid", id)
        startActivity(intent)
    }
}

