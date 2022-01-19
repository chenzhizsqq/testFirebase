package com.example.testfirebase.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testfirebase.databinding.ActivityWriteReadDelBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


//json 处理的类
data class PostsData(val id: Int, val title: String)
class TestViewModel : ViewModel() {
    //专门对应json数据中的posts数据List
    val postsDataList = MutableLiveData<List<PostsData>>()
}
//json 处理的类

//Firebase Realtime Database 是一种托管在云端的数据库。
// 数据以 JSON 格式存储并实时同步到每个连接的客户端。
// 当您使用我们的 Apple 平台、Android 和 JavaScript SDK 构建跨平台应用时，
// 所有的客户端共享一个 Realtime Database 实例，并自动接收包含最新数据的更新。
//https://qiita.com/YoshitakaOkada/items/ebbb83c199750742d1d2
class WriteReadDelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteReadDelBinding
    private val testJsonDir = "testJsonDir"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteReadDelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //写入
        binding.write.setOnClickListener {
            val database = Firebase.database
            val myRef = database.getReference("message")
            myRef.setValue(binding.etWrite.text.toString()).addOnSuccessListener {
                Toast.makeText(this, "成功写入：${binding.etWrite.text}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        //双层的子层写入
        binding.doubleLevelWrite.setOnClickListener {
            val database = Firebase.database
            val myRef = database.getReference("level1").child("level2")
            myRef.setValue(binding.etWrite.text.toString()).addOnSuccessListener {
                Toast.makeText(this, "双层的子层写入：${binding.etWrite.text}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        //读取
        binding.read.setOnClickListener {
            val database = Firebase.database
            database.getReference("message").get().addOnSuccessListener {
                Toast.makeText(this, "成功读取： ${it.value}", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "不成功读取", Toast.LENGTH_SHORT).show()
            }
        }

        //删除
        binding.delete.setOnClickListener {
            val database = Firebase.database
            database.getReference("message").removeValue().addOnSuccessListener {
                Toast.makeText(this, "成功删除!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "不成功删除", Toast.LENGTH_SHORT).show()
            }
        }

        //json写入
        jsonWrite()

        //json读取
        jsonRead()
    }

    //json读取 begin
    private val mTestReadViewModel = TestViewModel()
    private fun jsonRead() {
        binding.jsonRead.setOnClickListener {
            val database = Firebase.database
            database.getReference(testJsonDir).get().addOnSuccessListener {
                Log.e("TAG", "jsonRead:" + it.value)
                Toast.makeText(this, "${it.value}", Toast.LENGTH_SHORT).show()

                mTestReadViewModel.postsDataList.postValue(it.value as List<PostsData>)
            }
        }
        //观察是否正在读取数据，做出不同的操作
        mTestReadViewModel.postsDataList.observe(this, {
            Log.e("TAG", "jsonRead:mTestReadViewModel.postsDataList: $it")
        })
    }
    //json读取 end


    //json写入 jsonWrite() begin
    lateinit var mJsonWriteList: ArrayList<PostsData>
    private fun jsonWrite() {
        val mTestViewModel = TestViewModel()
        binding.jsonWrite.setOnClickListener {
            mJsonWriteList = ArrayList()
            for (i in 0..5) {
                val mPostsData = PostsData(i, "${binding.etWrite.text}$i")
                mJsonWriteList.add(mPostsData)
            }
            mTestViewModel.postsDataList.postValue(mJsonWriteList)
        }
        //观察是否正在读取数据，做出不同的操作
        mTestViewModel.postsDataList.observe(this, {
            val database = Firebase.database
            database.getReference(testJsonDir).setValue(it).addOnSuccessListener {
                Toast.makeText(this, "json写入!", Toast.LENGTH_SHORT).show()
            }
        })
    }
    //json写入 jsonWrite() end
}