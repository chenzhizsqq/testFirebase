package com.chenzhizs.checkfirebase11

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chenzhizs.checkfirebase11.databinding.ActivityMainBinding
import com.chenzhizs.checkfirebase11.ui.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.writeAndRead.setOnClickListener(this)
        binding.FirestoreCrudActivity.setOnClickListener(this)
        binding.authentication.setOnClickListener(this)
        binding.authenticationMailCheck.setOnClickListener(this)
        binding.AuthFirebaseUIActivity.setOnClickListener(this)
        binding.GetTokenActivity.setOnClickListener(this)
        binding.AnalyticsActivity.setOnClickListener(this)
        binding.CloudStorageActivity.setOnClickListener(this)
        binding.RemoteConfigActivity.setOnClickListener(this)
        binding.AdmobActivity.setOnClickListener(this)
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
            R.id.GetTokenActivity -> {
                val intent =
                    Intent(this@MainActivity, GetTokenActivity::class.java)
                startActivity(intent)
            }
            R.id.AnalyticsActivity -> {
                val intent =
                    Intent(this@MainActivity, AnalyticsActivity::class.java)
                startActivity(intent)
            }
            R.id.CloudStorageActivity -> {
                val intent =
                    Intent(this@MainActivity, CloudStorageActivity::class.java)
                startActivity(intent)
            }
            R.id.RemoteConfigActivity -> {
                val intent =
                    Intent(this@MainActivity, RemoteConfigActivity::class.java)
                startActivity(intent)
            }
            R.id.AdmobActivity -> {
                val intent =
                    Intent(this@MainActivity, AdmobActivity::class.java)
                startActivity(intent)
            }

        }
    }
}