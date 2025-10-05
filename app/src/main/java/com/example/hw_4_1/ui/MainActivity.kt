package com.example.hw_4_1.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw_4_1.R
import com.example.hw_4_1.data.model.Account
import com.example.hw_4_1.databinding.ActivityMainBinding
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
    }

    override fun onResume() {
        super.onResume()
        presenter.loadAcoounts()
    }

    private fun initAdapter() = with(binding) {
        adapter = AccountsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter
    }

    override fun showAccounts(accountsList: List<Account>) {
        adapter.submitList(accountsList)
    }
}
