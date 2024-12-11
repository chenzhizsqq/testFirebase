package com.chenzhizs.checkfirebase11.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chenzhizs.checkfirebase11.R
import com.chenzhizs.checkfirebase11.databinding.ActivityGetTokenBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

//https://firebase.google.com/docs/cloud-messaging/android/first-message?authuser=0#kotlin+ktx_1
class GetTokenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetTokenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetTokenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //获取当前测试机的token
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.e("TAG", msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

            binding.tvToken.text = token
        })
    }
}