package com.chenzhizs.checkfirebase11.ui

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
import com.chenzhizs.checkfirebase11.databinding.ActivityCloudStorageBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
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

        binding.listAllFiles.setOnClickListener {
            listAllFiles()
        }

        binding.includesForFileMetadata.setOnClickListener {
            includesForFileMetadata()
        }

        binding.editFileMetadata.setOnClickListener {
            editFileMetadata()
        }

        binding.getParentRoot.setOnClickListener {
            getParentRoot()
        }

        //本程序需要您同意允许访问所有文件权限
        fileScopedStorageCheck()
    }

    //https://firebase.google.com/docs/storage/android/create-reference#navigate_with_references
    private fun getParentRoot() {
        // parent allows us to move our reference to a parent node
        val parentRef = storageRef.parent

        // root allows us to move all the way back to the top of our bucket
        // rootRef now points to the root
        val rootRef = storageRef.root

        Log.e(
            TAG, "getParentRoot: parentRef:" + parentRef.toString()
                    + " rootRef:" + rootRef.toString()
        )
        Toast.makeText(
            baseContext, "getParentRoot: parentRef:" + parentRef.toString()
                    + " rootRef:" + rootRef.toString(), Toast.LENGTH_SHORT
        ).show()
    }

    //错误的Listener   https://firebase.google.com/docs/storage/android/handle-errors
    internal inner class MyFailureListener : OnFailureListener {
        override fun onFailure(exception: Exception) {
            val errorCode = (exception as StorageException).errorCode
            val errorMessage = exception.message
            // test the errorCode and errorMessage, and handle accordingly

            Log.e(TAG, "onFailure: $errorCode:$errorMessage")
            Toast.makeText(baseContext, "onFailure: $errorCode:$errorMessage", Toast.LENGTH_SHORT)
                .show()
        }
    }


    //https://firebase.google.com/docs/storage/android/file-metadata#file_metadata_properties
    private fun includesForFileMetadata() {

        // Get reference to the file
        val forestRef = storageRef.child("test.txt")
        // [END metadata_get_storage_reference]

        // [START get_file_metadata]
        forestRef.metadata.addOnSuccessListener { metadata ->
            // Metadata now contains the metadata for 'images/forest.jpg'
            Log.e(TAG, "includesForFileMetadata: ${metadata.name}")
            Toast.makeText(this, "Success ${metadata.name}", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
            Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
        }
    }

    //https://firebase.google.com/docs/storage/android/file-metadata#file_metadata_properties
    private fun editFileMetadata() {

        // Get reference to the file
        val forestRef = storageRef.child("test.txt")
        // [END metadata_get_storage_reference]

        // Create file metadata including the content type
        val metadata = storageMetadata {
            setCustomMetadata("name", "test-ok.txt")
        }


        // Update metadata properties
        forestRef.updateMetadata(metadata).addOnSuccessListener { updatedMetadata ->
            // Updated metadata is in updatedMetadata

            Log.e(TAG, "editFileMetadata: ${updatedMetadata.getCustomMetadata("name")}")
            Toast.makeText(
                this,
                "Success ${updatedMetadata.getCustomMetadata("name")}",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
            Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
        }

    }


    //https://github.com/firebase/snippets-android/blob/8184cba2c40842a180f91dcfb4a216e721cc6ae6/storage/app/src/main/java/com/google/firebase/referencecode/storage/kotlin/StorageActivity.kt#L436-L454
    private fun listAllFiles() {

        //如果要获取根目录，就是直接用storageRef，就可以了
        //val listRef = storageRef

        // [START storage_list_all]
        val listRef = storageRef.child("images")

        // You'll need to import com.google.firebase.storage.ktx.component1 and
        // com.google.firebase.storage.ktx.component2
        listRef.listAll()
            .addOnSuccessListener {
                val size = it.items.size
                for (i in 0 until size) {
                    Log.e(TAG, "listAllFiles $i: " + it.items[i].toString())
                }
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                // Uh-oh, an error occurred!
                Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
            }
        // [END storage_list_all]
    }

    //测试文件的路径
    private fun testFilePath() {
        val getFilesDir = filesDir
        Log.e(TAG, "getFilesDir: " + getFilesDir)
        ///data/user/0/com.chenzhizs.checkfirebase11/files

        val getCacheDir = cacheDir
        Log.e(TAG, "getCacheDir: " + getCacheDir)
        ///data/user/0/com.chenzhizs.checkfirebase11/cache

        val getExternalStoragePublicDirectory = getExternalStoragePublicDirectory("DCIM")
        Log.e(TAG, "getExternalStoragePublicDirectory: " + getExternalStoragePublicDirectory)
        ///storage/emulated/0/DCIM

        val mDIRECTORY_DCIM = applicationContext.getExternalFilesDir(Environment.DIRECTORY_DCIM)
        Log.e(TAG, "mDIRECTORY_DCIM: " + mDIRECTORY_DCIM)
        ////storage/emulated/0/Android/data/com.chenzhizs.checkfirebase11/files/DCIM


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

        val mMyFailureListener = MyFailureListener()

        // Delete the file
        desertRef.delete().addOnSuccessListener {
            // File deleted successfully
            Toast.makeText(this, "delete().addOnSuccessListener true", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener(mMyFailureListener)
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