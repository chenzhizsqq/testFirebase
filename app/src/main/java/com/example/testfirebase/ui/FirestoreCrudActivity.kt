package com.example.testfirebase.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.databinding.ActivityFirestoreCrudBinding
import com.google.firebase.firestore.FirebaseFirestore

//https://blog.csdn.net/cunjie3951/article/details/106918461
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
                    "name" to text,
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

        //运行查询
        binding.select.setOnClickListener {
            val selectEdit: String = binding.selectEdit.text.toString()
            val collect = db.collection("testId")
            collect.whereEqualTo("name", selectEdit)
                .get().addOnSuccessListener {
                    if (it.isEmpty) {
                        Toast.makeText(this, "运行查询:没有找到", Toast.LENGTH_SHORT).show()
                    } else {
                        it.forEach {
                            Toast.makeText(
                                this,
                                "找到了\"name\"的值是： ${it.get("name")}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "运行查询:错误", Toast.LENGTH_SHORT).show()
                }

        }

        //特定地方写入或修改
        binding.writeByName.setOnClickListener {
            val text: String = binding.etWrite.text.toString()
            val collect = db.collection("testId")
            collect
                .document("PLANET_EARTH")
                .set(
                    mapOf(
                        "name" to text,
                    )
                ).addOnCompleteListener {
                    Toast.makeText(this, "特定地方写入或修改:记录成功", Toast.LENGTH_SHORT).show()
                }
        }

        //特定地方读取
        binding.readByName.setOnClickListener {
            val text: String = binding.etWrite.text.toString()
            val collect = db.collection("testId")
            collect
                .document("PLANET_EARTH")
                .get().addOnSuccessListener {
                    Toast.makeText(this, "读取成功:${it.data?.values}", Toast.LENGTH_SHORT).show()
                    Log.e("TAG", "onCreate:readByName: 读取成功:${it.data?.values}")
                }.addOnFailureListener {
                    Toast.makeText(this, "读取失败", Toast.LENGTH_SHORT).show()
                }
        }

        //特定地方删除
        binding.deleteByName.setOnClickListener {
            db.collection("testId")
                .document("PLANET_EARTH")
                .delete()
                .addOnCompleteListener {
                    Toast.makeText(this, "特定地方删除：成功", Toast.LENGTH_SHORT).show()
                }
        }

        //创建子集合
        binding.createChild.setOnClickListener {
            val collect = db.collection("testId")

            //创建子集合
            val satellitesOfEarth = collect.document("PLANET_EARTH")
                .collection("satellites")

            //子集合中添加数据
            satellitesOfEarth.add(
                mapOf(
                    "name" to "The Moon",
                    "gravity" to 1.62,
                    "radius" to 1738
                )
            ).addOnCompleteListener {
                Toast.makeText(this, "子集合中添加数据：成功", Toast.LENGTH_SHORT).show()
            }
        }


    }
}