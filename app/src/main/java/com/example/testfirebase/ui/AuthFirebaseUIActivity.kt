package com.example.testfirebase.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.R
import com.example.testfirebase.databinding.ActivityAuthFirebaseUiactivityBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class AuthFirebaseUIActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthFirebaseUiactivityBinding

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

        createSignInIntent()
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

    companion object {
        private const val TAG = "AuthFirebaseUIActivity"
    }
}