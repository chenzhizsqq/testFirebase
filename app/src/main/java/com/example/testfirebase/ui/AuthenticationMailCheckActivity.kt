package com.example.testfirebase.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.databinding.ActivityAuthenticationMailCheckBinding
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
            sendSignInLink(email = email,actionCodeSettings =buildActionCodeSettings() )
        }

        binding.checkMail.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            verifySignInLink(email = email)
        }

        binding.txSignOut.setOnClickListener {
            signOut()
        }
    }

    private fun buildActionCodeSettings() : ActionCodeSettings {
        // [START auth_build_action_code_settings]
        val actionCodeSettings = actionCodeSettings {
            // URL you want to redirect back to. The domain (www.example.com) for this
            // URL must be whitelisted in the Firebase Console.
            url = "https://chenzhitestweb.page.link/NLtk"
            // This must be true
            handleCodeInApp = true
            setIOSBundleId("com.example.ios")
            setAndroidPackageName(
                "com.example.testfirebase",
                true, /* installIfNotAvailable */
                "12" /* minimumVersion */)
        }
        // [END auth_build_action_code_settings]
        return actionCodeSettings
    }

    private fun sendSignInLink(email: String, actionCodeSettings: ActionCodeSettings) {
        // [START auth_send_sign_in_link]
        Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "Email sent.")
                    Toast.makeText(baseContext, "Email sent.", Toast.LENGTH_SHORT).show()
                }else{
                    Log.e(TAG, "sendSignInLink:failure", task.exception)
                }
            }
        // [END auth_send_sign_in_link]
    }

    private fun verifySignInLink(email: String) {
        // [START auth_verify_sign_in_link]
        val auth = Firebase.auth
        val intent = intent
        val emailLink = intent.data.toString()

        // Confirm the link is a sign-in with email link.
        if (auth.isSignInWithEmailLink(emailLink)) {
            // Retrieve this from wherever you stored it
            //val email = "someemail@domain.com"
            val email = email

            // The client SDK will parse the code from the link for you.
            auth.signInWithEmailLink(email, emailLink)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.e(TAG, "Successfully signed in with email link!")
                        Toast.makeText(baseContext, "Successfully signed in with email link!", Toast.LENGTH_SHORT).show()
                        val result = task.result
                        // You can access the new user via result.getUser()
                        // Additional user info profile *not* available via:
                        // result.getAdditionalUserInfo().getProfile() == null
                        // You can check if the user is new or existing:
                        // result.getAdditionalUserInfo().isNewUser()
                    } else {
                        Log.e(TAG, "Error signing in with email link", task.exception)
                    }
                }
        }else
        {
            Toast.makeText(baseContext, "isSignInWithEmailLink Error ", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "isSignInWithEmailLink Error")

        }
        // [END auth_verify_sign_in_link]
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