package com.example.testfirebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.databinding.ActivityMainBinding
import com.example.testfirebase.ui.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.writeAndRead.setOnClickListener(this)
        binding.CloudMessagingActivity.setOnClickListener(this)
        binding.FirestoreCrudActivity.setOnClickListener(this)
        binding.authentication.setOnClickListener(this)
        binding.authenticationMailCheck.setOnClickListener(this)
        binding.AuthFirebaseUIActivity.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.authentication -> {
                val intent =
                    Intent(this@MainActivity, AuthenticationActivity::class.java)
                startActivity(intent)
            }
            R.id.authenticationMailCheck -> {
                val intent =
                    Intent(this@MainActivity, AuthenticationMailCheckActivity::class.java)
                startActivity(intent)
            }
            R.id.AuthFirebaseUIActivity -> {
                val intent =
                    Intent(this@MainActivity, AuthFirebaseUIActivity::class.java)
                startActivity(intent)
            }
            R.id.write_and_read -> {
                val intent =
                    Intent(this@MainActivity, WriteReadDelActivity::class.java)
                startActivity(intent)
            }
            R.id.FirestoreCrudActivity -> {
                val intent =
                    Intent(this@MainActivity, FirestoreCrudActivity::class.java)
                startActivity(intent)
            }
            R.id.CloudMessagingActivity -> {
                val intent =
                    Intent(this@MainActivity, CloudMessagingActivity::class.java)
                startActivity(intent)
            }
        }
    }
}