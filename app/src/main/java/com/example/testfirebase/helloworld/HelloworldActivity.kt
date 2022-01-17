package com.example.testfirebase.helloworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testfirebase.databinding.ActivityHelloworldBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//https://qiita.com/YoshitakaOkada/items/ebbb83c199750742d1d2
class HelloworldActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelloworldBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelloworldBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.setValue("Hello, World!")

    }
}