package com.example.hw_4_1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw_4_1.data.model.Account
import com.example.hw_4_1.databinding.ActivityDeteilBinding
import com.example.hw_4_1.databinding.ActivityMainBinding
import com.example.hw_4_1.databinding.DialogAddBinding
import com.example.hw_4_1.domain.presenter.AccountViewModel
import com.example.hw_4_1.domain.presenter.DeteilAccountViewModel
import com.example.hw_4_1.ui.adapter.AccountsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class Deteil_Activity : AppCompatActivity() {
    private val viewModel: DeteilAccountViewModel by viewModels()
    private lateinit var idAccount: String
    private lateinit var binding: ActivityDeteilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDeteilBinding.inflate(layoutInflater)
        setContentView(binding.root)
            subscribeLiveData()
        idAccount = intent.getStringExtra("accountid") ?: ""
        Log.d("Deteil_Activity", "idAccount = $idAccount")
        Log.d("Deteil_Activity", "idAccount = $idAccount")
        if (idAccount.isNotEmpty()) {
            viewModel.loadAccount(idAccount)
        }
        idAccount = intent.getStringExtra("editTextID") ?: ""


        binding.tvNameDetail.text = viewModel.account.value?.name.toString()
        binding.tvBalanceDetail.text = viewModel.account.value?.balance.toString()


        binding.btnEditDetail.setOnClickListener {
                viewModel.account.value?.let { showEditDialog(it)}
                }
        binding.btnDeleteDetail.setOnClickListener {
            showDeleteDialog(idAccount)
        }
        }

    override fun onResume() {
        super.onResume()
        if (idAccount.isNotEmpty()) {
            viewModel.loadAccount(idAccount)
        }
    }


    private fun subscribeLiveData() {
        viewModel.account.observe(this) { account ->
            if (account != null) {
                binding.tvNameDetail.text = account.name
                binding.tvBalanceDetail.text = account.balance.toString()
            }
        }
    }
        private fun showEditDialog(account: Account){
            val binding = DialogAddBinding.inflate(LayoutInflater.from(this))
            with(binding){

                account.run {

                    etName.setText(name)
                    etBalance.setText(balance.toString())

                        AlertDialog.Builder(this@Deteil_Activity)
                            .setTitle("Изменение счета")
                            .setView(binding.root)
                            .setPositiveButton("изменить"){_,_ ->
                                val updatedAccount = account.copy(
                                    name = etName.text.toString(),
                                    balance = etBalance.text.toString().toInt(),
                                )
                                viewModel.updateAccountFully(updatedAccount)
                            }.show()
                         }

        }
    }
    private fun showDeleteDialog(id: String){
        AlertDialog.Builder(this)
            .setTitle("Вы уверены?")
            .setMessage("Вы уверены что хотите удалить счет с индефикатором - ${id}")
            .setPositiveButton("Удалить"){_,_, ->
                viewModel.deleteAccount(id)
            }
            .setNegativeButton("отмена"){_,_ ->

            }.show()
    }
}


