package com.example.testfirebase.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.testfirebase.databinding.ActivityCloudStorageBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class CloudStorageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCloudStorageBinding

    // [START storage_field_declaration]
    lateinit var storage: FirebaseStorage
    // [END storage_field_declaration]

    // Creating a storage reference from our app
    lateinit var storageRef: StorageReference


    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCloudStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // [START storage_field_initialization]
        storage = Firebase.storage
        // [END storage_field_initialization]

        storageRef = storage.reference
        imageView = binding.image

        binding.clear.setOnClickListener {
            imageView.setImageBitmap(null)
        }

        //httpUrlLoad()
        binding.httpUrl.setOnClickListener {
            imageView.setImageBitmap(null)
            httpUrlLoad()
        }

        binding.fullLoad.setOnClickListener {
            imageView.setImageBitmap(null)
            fullLoad()
        }

        binding.deletePic.setOnClickListener {
            imageView.setImageBitmap(null)
            includesForDeleteFiles()
        }
    }

    //https://firebase.google.com/docs/storage/android/download-files#full_example
    private fun fullLoad() {
        storageRef.child("test.png").getBytes(Long.MAX_VALUE).addOnSuccessListener {
            // Use the bytes to display the image
            Glide.with(this /* context */)
                .load(it)
                .into(imageView)
            Toast.makeText(this, "addOnSuccessListener true", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            // Handle any errors
            Toast.makeText(this, "addOnSuccessListener false", Toast.LENGTH_SHORT).show()
        }
    }

    //在访问后台中，查看“存储位置"，而获取的图片位置
    private fun httpUrlLoad() {
        Glide.with(this /* context */)
            .load("https://firebasestorage.googleapis.com/v0/b/childtestcz.appspot.com/o/test.png?alt=media&token=d10e80ef-9c6a-4ead-86ee-1bf67734a6ef")
            .into(imageView)
    }


    fun includesForDeleteFiles() {

        // Create a reference to the file to delete
        val desertRef = storageRef.child("images/test2.png")

        // Delete the file
        desertRef.delete().addOnSuccessListener {
            // File deleted successfully
            Toast.makeText(this, "delete().addOnSuccessListener true", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
            Toast.makeText(this, "delete().addOnSuccessListener false", Toast.LENGTH_SHORT).show()
        }
        // [END delete_file]
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
        val customStorage = Firebase.storage(customApp, "gs://childtestcz.appspot.com/")
        // [END storage_custom_app]
    }

}