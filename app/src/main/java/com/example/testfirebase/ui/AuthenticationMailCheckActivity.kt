package com.example.testfirebase.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.databinding.ActivityAuthenticationMailCheckBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthenticationMailCheckActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationMailCheckBinding

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationMailCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]
    }
}