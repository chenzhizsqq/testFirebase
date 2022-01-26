package com.example.testfirebase.ui

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.testfirebase.databinding.ActivityCloudStorageBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


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
        binding.testFilePath.setOnClickListener {
            testFilePath()
        }

        binding.upload.setOnClickListener {
            imageView.setImageBitmap(null)
            UploadTask()
        }

        //本程序需要您同意允许访问所有文件权限
        fileScopedStorageCheck()
    }

    //测试文件的路径
    private fun testFilePath() {
        val getFilesDir = filesDir
        Log.e(TAG, "getFilesDir: " + getFilesDir)
        ///data/user/0/com.example.testfirebase/files

        val getCacheDir = cacheDir
        Log.e(TAG, "getCacheDir: " + getCacheDir)
        ///data/user/0/com.example.testfirebase/cache

        val getExternalStoragePublicDirectory = getExternalStoragePublicDirectory("DCIM")
        Log.e(TAG, "getExternalStoragePublicDirectory: " + getExternalStoragePublicDirectory)
        ///storage/emulated/0/DCIM

        val mDIRECTORY_DCIM = applicationContext.getExternalFilesDir(Environment.DIRECTORY_DCIM)
        Log.e(TAG, "mDIRECTORY_DCIM: " + mDIRECTORY_DCIM)
        ////storage/emulated/0/Android/data/com.example.testfirebase/files/DCIM


        val testFilePath: String =
            getExternalStoragePublicDirectory.toString() + "/" + "upload_test.png"
        Log.e(TAG, "testFilePath: " + testFilePath)
        //testFilePath: /storage/emulated/0/DCIM/upload_test.png

        val file = File(testFilePath)

        //最后验证是否有这个文件
        val isFile = file.isFile
        Log.e(TAG, "isFile: " + isFile)
        //isFile: true
    }

    //允许访问所有文件权限的检测
    private fun fileScopedStorageCheck() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R ||
            Environment.isExternalStorageManager()
        ) {
            Toast.makeText(this, "已获得访问所有文件权限", Toast.LENGTH_SHORT).show()
        } else {
            val builder = AlertDialog.Builder(this)
                .setMessage("本程序需要您同意允许访问所有文件权限")
                .setPositiveButton("确定") { _, _ ->
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivity(intent)
                }
            builder.show()
        }
    }

    //https://logicalerror.seesaa.net/article/443110283.html
    private fun UploadTask() {
// View から Bitmap 取得
        val view: View = binding.root // 画面全体
        view.isDrawingCacheEnabled = true
        val cache: Bitmap = view.drawingCache
        val rootViewCapture = Bitmap.createBitmap(cache)
        view.isDrawingCacheEnabled = false

// 画像アップロード用パス決定
        val cal: Calendar = Calendar.getInstance()
        val sf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val uploadImagePath =
            java.lang.String.format("images/%s.png", sf.format(cal.time))
        val imageRef = storageRef.child(uploadImagePath)

// byte[] に変換
        val baos = ByteArrayOutputStream()
        rootViewCapture.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask: UploadTask = imageRef.putBytes(data)

        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(this, "上传不成功", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show()
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

    companion object {
        private const val TAG = "CloudStorageActivity"
    }
}