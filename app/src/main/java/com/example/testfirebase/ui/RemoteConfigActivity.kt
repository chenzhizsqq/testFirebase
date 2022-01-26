package com.example.testfirebase.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.databinding.ActivityRemoteConfigBinding

class RemoteConfigActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRemoteConfigBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemoteConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}