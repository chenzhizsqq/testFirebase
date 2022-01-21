package com.example.testfirebase.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.databinding.ActivityCloudMessagingBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class CloudMessagingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCloudMessagingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCloudMessagingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(baseContext, "FirebaseMessaging failed", Toast.LENGTH_SHORT).show()
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Toast.makeText(baseContext, "msg_token_fmt", Toast.LENGTH_SHORT).show()
        })

    }


    companion object {
        private const val TAG = "CloudMessagingActivity"
    }
}