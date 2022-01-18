package com.example.testfirebase.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testfirebase.databinding.ActivityFirestoreCrudBinding

class FirestoreCrudActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirestoreCrudBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirestoreCrudBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}