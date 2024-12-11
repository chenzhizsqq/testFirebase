package com.chenzhizs.checkfirebase11.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chenzhizs.checkfirebase11.R
import com.chenzhizs.checkfirebase11.databinding.ActivityAuthFirebaseUiactivityBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//https://firebase.google.com/docs/auth/android/firebaseui?authuser=0
class AuthFirebaseUIActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthFirebaseUiactivityBinding

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    // [START auth_fui_create_launcher]
    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }
    // [END auth_fui_create_launcher]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthFirebaseUiactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        createSignInIntent()

        binding.signOut.setOnClickListener {
            signOut()
        }

        binding.refresh.setOnClickListener {
            refresh()
        }

        binding.delete.setOnClickListener {
            delete()
        }
    }


    //刷新
    private fun refresh() {
        val user = Firebase.auth.currentUser
        if (user == null) {
            Toast.makeText(baseContext, "user == null", Toast.LENGTH_SHORT)
                .show()
            binding.email.text = ""
            binding.displayName.text = ""
            return
        }
        binding.email.text = user.email
        binding.displayName.text = user.displayName

    }


    private fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),     //邮箱登录
            AuthUI.IdpConfig.GoogleBuilder().build()    //google方式。要在后台设置
            /*,AuthUI.IdpConfig.PhoneBuilder().build()
            AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.TwitterBuilder().build()*/
        )
        //上面的选项。要超过两个，登录时候才能够见到登录的logo

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.mipmap.ic_launcher) // Set logo drawable。设置logo的显示图案
            .setTheme(R.style.ThemeOverlay_AppCompat_Dark) // Set theme。设置模板
            .build()

        signInLauncher.launch(signInIntent)
        // [END auth_fui_create_intent]
    }


    //登录成功后
    // [START auth_fui_result]
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            binding.email.text = response?.email
            binding.displayName.text = user?.displayName
            // ...
        } else {
            binding.email.text = "不成功"
            binding.displayName.text = "不成功"
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
    // [END auth_fui_result]

    private fun signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "signOut ok")
                    Toast.makeText(this, "signOut ok", Toast.LENGTH_SHORT).show()
                    refresh()
                } else {
                    Log.e(TAG, "signOut no")
                    Toast.makeText(this, "signOut no", Toast.LENGTH_SHORT).show()
                    refresh()
                }
            }
        // [END auth_fui_signout]
    }

    private fun delete() {
        val user = Firebase.auth.currentUser
        if (user == null) {
            Toast.makeText(baseContext, "user == null", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // [START auth_fui_delete]
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "delete yes", Toast.LENGTH_SHORT)
                        .show()
                    refresh()
                } else {
                    Toast.makeText(baseContext, "delete no", Toast.LENGTH_SHORT)
                        .show()
                    refresh()
                }
            }
        // [END auth_fui_delete]

    }

    companion object {
        private const val TAG = "AuthFirebaseUIActivity"
    }
}