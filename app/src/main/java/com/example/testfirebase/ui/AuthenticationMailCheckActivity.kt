package com.chenzhizs.checkfirebase11.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chenzhizs.checkfirebase11.databinding.ActivityAuthenticationMailCheckBinding
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.ktx.Firebase

//不用密码的邮件注册，暂时还没有完成
class AuthenticationMailCheckActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationMailCheckBinding

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationMailCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        binding.sendMail.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            sendSignInLink(email = email, actionCodeSettings = buildActionCodeSettings())
        }

        binding.txSignOut.setOnClickListener {
            signOut()
        }
    }

    private fun buildActionCodeSettings(): ActionCodeSettings {
        // [START auth_build_action_code_settings]
        val actionCodeSettings = actionCodeSettings {
            // URL you want to redirect back to. The domain (www.example.com) for this
            // URL must be whitelisted in the Firebase Console.
            url = "https://chenzhitestweb.page.link/NLtk"
            // This must be true
            handleCodeInApp = true
            setIOSBundleId("com.example.ios")
            setAndroidPackageName(
                "com.chenzhizs.checkfirebase11",
                true, /* installIfNotAvailable */
                "12" /* minimumVersion */
            )
        }
        // [END auth_build_action_code_settings]
        return actionCodeSettings
    }


    fun createDynamicLink_Basic() {
        // [START create_link_basic]
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://www.example.com/")
            domainUriPrefix = "https://chenzhitestweb.page.link"
            // Open links with this app on Android
            androidParameters { }
            // Open links with com.example.ios on iOS
            iosParameters("com.example.ios") { }
        }

        val dynamicLinkUri = dynamicLink.uri
        // [END create_link_basic]
    }

    private fun sendSignInLink(email: String, actionCodeSettings: ActionCodeSettings) {
        // [START auth_send_sign_in_link]
        Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "Email sent.")
                    Toast.makeText(baseContext, "Email sent.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendSignInLink:failure", task.exception)
                }
            }
        // [END auth_send_sign_in_link]
    }

    private fun signOut() {
        // [START auth_sign_out]
        Firebase.auth.signOut()
        // [END auth_sign_out]
    }

    companion object {
        private const val TAG = "AuthenticationMailCheckActivity"
    }
}