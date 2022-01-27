package com.example.testfirebase.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.databinding.ActivityAdmobBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds


class AdmobActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdmobBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdmobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the Mobile Ads SDK with an AdMob App ID.
        MobileAds.initialize(this) {}

        // Create an ad request.
        val adRequest = AdRequest.Builder().build()

        // Start loading the ad in the background.
        binding.adView.loadAd(adRequest)


        // 自己测试用的
        val adRequest2 = AdRequest.Builder().build()
        binding.adView2.loadAd(adRequest2)
    }
}