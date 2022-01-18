package com.example.testfirebase.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.databinding.ActivityFirestoreCrudBinding
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreCrudActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirestoreCrudBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirestoreCrudBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()

        //写入
        binding.write.setOnClickListener {
            val text: String = binding.etWrite.text.toString()
            val collect = db.collection("testId")
            collect.add(
                mapOf(
                    "textTest" to text,
                )
            ).addOnCompleteListener {
                Toast.makeText(this, "记录成功", Toast.LENGTH_SHORT).show()
            }
        }

        //读取
        binding.read.setOnClickListener {
            val collect = db.collection("testId")
            collect.get().addOnSuccessListener {
                //Toast.makeText(this, "成功读取： ${it.documents[0]}", Toast.LENGTH_SHORT).show()
                if (it.size() == 0) {
                    Toast.makeText(this, "数据为空", Toast.LENGTH_SHORT).show()
                } else {
                    it.forEach { each ->
                        Log.e("TAG", "onCreate: forEach:" + each.data.toString())

                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "读取失败", Toast.LENGTH_SHORT).show()
            }
        }

        //删除
        binding.delete.setOnClickListener {
            val collect = db.collection("testId")
            collect.get().addOnSuccessListener {
                it.forEach { forEach ->
                    forEach.reference.delete().addOnCompleteListener {
                        Toast.makeText(this, "成功删除", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show()
            }
        }
    }
}