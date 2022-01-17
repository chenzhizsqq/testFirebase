package com.example.testfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.testfirebase.databinding.ActivityMainBinding
import com.example.testfirebase.helloworld.HelloworldActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.testHelloworld.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.testHelloworld -> {
                val intent =
                    Intent(this@MainActivity, HelloworldActivity::class.java)
                startActivity(intent)
            }
        }
    }
}