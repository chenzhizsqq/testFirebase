package com.example.testfirebase.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.databinding.ActivityCloudStorageBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class CloudStorageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCloudStorageBinding

    // [START storage_field_declaration]
    lateinit var storage: FirebaseStorage
    // [END storage_field_declaration]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCloudStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // [START storage_field_initialization]
        storage = Firebase.storage
        // [END storage_field_initialization]

    }
}