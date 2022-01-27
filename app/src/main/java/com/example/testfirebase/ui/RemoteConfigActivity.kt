package com.example.testfirebase.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.R
import com.example.testfirebase.databinding.ActivityRemoteConfigBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class RemoteConfigActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRemoteConfigBinding
    private lateinit var remoteConfig: FirebaseRemoteConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemoteConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Get Remote Config instance.
        // [START get_remote_config_instance]
        remoteConfig = Firebase.remoteConfig
        // [END get_remote_config_instance]


        // 设置刷新后台属性的频率
        // [START enable_dev_mode]
        val configSettings = remoteConfigSettings {
            fetchTimeoutInSeconds = 60            //读取数据的时间超过指定的超时时间
            minimumFetchIntervalInSeconds = 60    //设置最小提取间隔以实现频繁刷新：
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        // [END enable_dev_mode]

        // 设置后台刷新的默认模式
        // [START set_default_values]
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        // [END set_default_values]

        binding.fetchButton.setOnClickListener { fetchWelcome() }

        binding.testServerConfigCtr.setOnClickListener { testServerConfigCtr() }

        binding.testServerConfigDef.setOnClickListener { testServerConfigDef() }

        binding.testRandom.setOnClickListener { testRandom() }

    }

    //搭配服务器的条件值，就会获取对应的值。是按照服务器的条件，而不是按照当前APP的条件。
    //例如：随机百分之50，一旦当前用户是false，就会一直都会false。再取值，也是一样的结果。
    private fun testRandom() {

        // [START fetch_config_with_callback]
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result               //是否后台有最新的更新
                    Log.e(TAG, "Config params updated: $updated")

                    //在后台获取最新的数值
                    val testRandom = remoteConfig.getDouble("testRandom")
                    logToast("testRandom:$testRandom")
                    binding.welcomeTextView.text = testRandom.toString()
                } else {
                    Toast.makeText(
                        this, "Fetch failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        // [END fetch_config_with_callback]
    }


    //测试关于后台“配置”后，获取的服务器最新数值
    private fun testServerConfigCtr() {

        // [START fetch_config_with_callback]
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result               //是否后台有最新的更新
                    Log.e(TAG, "Config params updated: $updated")

                    //在后台获取最新的数值
                    val testName = remoteConfig.getString("testName")
                    logToast("testServerConfigCtr:$testName")
                    binding.welcomeTextView.text = testName
                } else {
                    Toast.makeText(
                        this, "Fetch failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        // [END fetch_config_with_callback]
    }

    //测试没有联络后台的情况下，获取最后的值
    private fun testServerConfigDef() {

        //在后台获取最新的数值
        val testName = remoteConfig.getString("testName")
        logToast("testServerConfigCtr:$testName")
        binding.welcomeTextView.text = testName
    }


    /**
     * Fetch a welcome message from the Remote Config service, and then activate it.
     * 测试获取默认值:因为服务器还没有对应的值，而直接拿默认值
     */
    private fun fetchWelcome() {
        binding.welcomeTextView.text = remoteConfig[LOADING_PHRASE_CONFIG_KEY].asString()

        // [START fetch_config_with_callback]
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result       //是否后台有最新的更新
                    Log.e(TAG, "Config params updated: $updated")
                    Toast.makeText(
                        this, "Fetch and activate succeeded",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this, "Fetch failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                displayWelcomeMessage()
            }
        // [END fetch_config_with_callback]
    }


    /**
     * Display a welcome message in all caps if welcome_message_caps is set to true. Otherwise,
     * display a welcome message as fetched from welcome_message.
     */
    // [START display_welcome_message]
    private fun displayWelcomeMessage() {
        // [START get_config_values]
        val welcomeMessage = remoteConfig[WELCOME_MESSAGE_KEY].asString()
        // [END get_config_values]
        binding.welcomeTextView.isAllCaps = remoteConfig[WELCOME_MESSAGE_CAPS_KEY].asBoolean()
        binding.welcomeTextView.text = welcomeMessage
    }

    private fun logToast(str: String) {
        Toast.makeText(baseContext, str, Toast.LENGTH_SHORT).show()
        Log.e(TAG, str)
    }

    companion object {

        private const val TAG = "RemoteConfigActivity"

        // Remote Config keys
        private const val LOADING_PHRASE_CONFIG_KEY = "loading_phrase"
        private const val WELCOME_MESSAGE_KEY = "welcome_message"
        private const val WELCOME_MESSAGE_CAPS_KEY = "welcome_message_caps"
    }
}