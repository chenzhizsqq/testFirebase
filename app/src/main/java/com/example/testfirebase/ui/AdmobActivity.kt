package com.example.testfirebase.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.databinding.ActivityAdmobBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import java.util.*


class AdmobActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdmobBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdmobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.simpleCreate.setOnClickListener { simpleCreate() }
        binding.setTestCreate.setOnClickListener { setTestCreate() }

    }

    // 简单的广告显示
    private fun simpleCreate() {
        // Initialize the Mobile Ads SDK with an AdMob App ID.
        MobileAds.initialize(this) {}

        // Create an ad request.
        val adRequest = AdRequest.Builder().build()

        // Start loading the ad in the background.
        binding.adView.loadAd(adRequest)

        logToast("简单的广告显示")
    }

    // 创建测试用的广告显示
    //https://developers.google.com/admob/android/test-ads
    private fun setTestCreate() {
        val testDeviceIds = Arrays.asList("E9CCC26CCC75A7E43D70B3C4E286EBF0")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)

        val adRequest2 = AdRequest.Builder().build()
        if (adRequest2.isTestDevice(this)) {
            logToast("测试用的广告显示")
        }

        binding.adViewTest.loadAd(adRequest2)
    }

    private fun logToast(str: String) {
        Toast.makeText(baseContext, str, Toast.LENGTH_SHORT).show()
        Log.e(TAG, str)
    }

    companion object {

        private const val TAG = "AdmobActivity"
    }
}