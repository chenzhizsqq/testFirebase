package com.example.testfirebase.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.MainActivity
import com.example.testfirebase.databinding.ActivityAuthenticationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//https://firebase.google.com/docs/auth/android/password-auth?authuser=0
class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        binding.SignUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passEditText.text.toString()
            createAccount(email = email, password = password)
        }

        binding.LoginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passEditText.text.toString()
            signIn(email = email, password = password)
        }

        binding.CheckButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            checkIsNewUser(email = email)
        }

        binding.updatePassword.setOnClickListener {
            val password = binding.passEditText.text.toString()
            updatePassword(password)
        }

        binding.signOut.setOnClickListener {
            auth.signOut()
            Log.e(TAG, "signOut")
            val intent =
                Intent(this@AuthenticationActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.sendPasswordReset.setOnClickListener {
            val emailLabel = binding.emailLabel.text.toString()
            sendPasswordReset(emailLabel)
        }

        binding.sendEmailVerification.setOnClickListener {
            sendEmailVerification()
        }

        binding.isEmailVerified.setOnClickListener {
            isEmailVerified()
        }

        binding.deleteUser.setOnClickListener {
            deleteUser()
        }

    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun createAccount(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()){
            Toast.makeText(
                baseContext, "email.isEmpty() or password.isEmpty()",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed." + task.exception!!.message!!,
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun signIn(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()){
            Toast.makeText(
                baseContext, "email.isEmpty() or password.isEmpty()",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        binding.message.text = ""
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed." + task.exception!!.localizedMessage!!,
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }

        // [END sign_in_with_email]
    }

    private fun reload() {

    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            binding.message.text = user.email + " success"
        } else {
            binding.message.text = " wrong"
        }
    }

    private fun updateMessage(message: String) {
        binding.message.text = message
    }


    private fun checkIsNewUser(email: String) {

        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val isNewUser = task.result.signInMethods?.isEmpty()
                    if (isNewUser == true) {
                        Toast.makeText(baseContext, "checkIsNewUser isNewUser", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            baseContext,
                            "checkIsNewUser isNewUser no no no",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e(TAG, "Error signing in with email link", task.exception)
                }
            }
    }

    //在登录的情况下。重设密码
    private fun updatePassword(newPassword: String) {
        // [START update_password]
        val user = Firebase.auth.currentUser

        user?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "User password updated.")
                    Toast.makeText(baseContext, "User password updated.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(baseContext, "User password updated. error", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    //密码忘记，在没有登录的情况下，要求重设密码。(等一下会发送到注册的邮件中)
    private fun sendPasswordReset(emailAddress: String) {
        // [START send_password_reset]
        //val emailAddress = "user@example.com"

        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "Email sent.")
                    Toast.makeText(baseContext, "Email sent.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Log.e(TAG, "sendPasswordReset error")
                    Toast.makeText(baseContext, "sendPasswordReset error", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        // [END send_password_reset]
    }

    //对当前用户，发送邮件验证
    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = Firebase.auth.currentUser
        if (user == null) {
            Toast.makeText(baseContext, "user == null", Toast.LENGTH_SHORT)
                .show()
            return
        }

        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "Email sent.")
                    Toast.makeText(baseContext, "Email sent.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Log.e(TAG, "sendEmailVerification error")
                    Toast.makeText(baseContext, "sendEmailVerification error", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        // [END send_email_verification]
    }

    //对当前用户，确定邮件验证是否成功
    private fun isEmailVerified() {
        val user = Firebase.auth.currentUser
        if (user == null) {
            Toast.makeText(baseContext, "user == null", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (user.isEmailVerified) {
            Toast.makeText(baseContext, "isEmailVerified yes", Toast.LENGTH_SHORT)
                .show()
            updateMessage("isEmailVerified yes")
        } else {
            Toast.makeText(baseContext, "isEmailVerified no", Toast.LENGTH_SHORT)
                .show()
            updateMessage("isEmailVerified no")
        }

    }

    //
    private fun deleteUser() {
        // [START delete_user]
        val user = Firebase.auth.currentUser
        if (user == null) {
            Toast.makeText(baseContext, "user == null", Toast.LENGTH_SHORT)
                .show()
            return
        }

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "delete yes", Toast.LENGTH_SHORT)
                        .show()
                    updateMessage("delete yes")
                } else {
                    Toast.makeText(baseContext, "delete no", Toast.LENGTH_SHORT)
                        .show()
                    updateMessage("delete no")
                }
            }
        // [END delete_user]
    }


    companion object {
        private const val TAG = "AuthenticationActivity"
    }
}