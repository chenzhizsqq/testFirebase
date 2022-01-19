package com.example.testfirebase.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.databinding.ActivityWriteReadDelBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//Firebase Realtime Database 是一种托管在云端的数据库。
// 数据以 JSON 格式存储并实时同步到每个连接的客户端。
// 当您使用我们的 Apple 平台、Android 和 JavaScript SDK 构建跨平台应用时，
// 所有的客户端共享一个 Realtime Database 实例，并自动接收包含最新数据的更新。
//https://qiita.com/YoshitakaOkada/items/ebbb83c199750742d1d2
class WriteReadDelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteReadDelBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteReadDelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //写入
        binding.write.setOnClickListener {
            val database = Firebase.database
            val myRef = database.getReference("message")
            myRef.setValue(binding.etWrite.text.toString())
            Toast.makeText(this, "成功写入：${binding.etWrite.text}", Toast.LENGTH_SHORT)
                .show()
        }

        //读取
        binding.read.setOnClickListener {
            val database = Firebase.database
            database.getReference("message").get().addOnSuccessListener {
                Toast.makeText(this, "成功读取： ${it.value}", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "addOnFailureListener", Toast.LENGTH_SHORT).show()
            }
        }

        //删除
        binding.delete.setOnClickListener {
            val database = Firebase.database
            database.getReference("message").removeValue().addOnSuccessListener {
                Toast.makeText(this, "成功删除!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}