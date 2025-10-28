package com.example.hw_4_1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hw_4_1.databinding.ActivityDeteilBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Deteil_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityDeteilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDeteilBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}


