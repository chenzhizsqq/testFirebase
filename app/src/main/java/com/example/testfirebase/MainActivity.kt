package com.example.testfirebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.databinding.ActivityMainBinding
import com.example.testfirebase.ui.CloudMessagingActivity
import com.example.testfirebase.ui.FirestoreCrudActivity
import com.example.testfirebase.ui.WriteReadDelActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.writeAndRead.setOnClickListener(this)
        binding.CloudMessagingActivity.setOnClickListener(this)
        binding.FirestoreCrudActivity.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
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