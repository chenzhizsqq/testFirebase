package com.example.testfirebase.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.databinding.ActivityCloudStorageBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
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


    // 使用多个 Cloud Storage 存储桶：这个不是默认的云空间，是其他的云空间地址
    fun nonDefaultBucket() {
        // [START storage_non_default_bucket]
        // Get a non-default Storage bucket
        val storage = Firebase.storage("gs://my-custom-bucket")
        // [END storage_non_default_bucket]
    }

    //使用自定义 Firebase 应用 : 如果您要使用自定义 FirebaseApp 构建更复杂的应用，可以创建使用该应用初始化的
    fun customApp() {
        val customApp = Firebase.initialize(this)

        // [START storage_custom_app]
        // Get the default bucket from a custom FirebaseApp
        val storage = Firebase.storage(customApp!!)

        // Get a non-default bucket from a custom FirebaseApp
        val customStorage = Firebase.storage(customApp, "gs://my-custom-bucket")
        // [END storage_custom_app]
    }
}