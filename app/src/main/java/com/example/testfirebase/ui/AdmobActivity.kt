package com.example.testfirebase.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirebase.databinding.ActivityAdmobBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.util.*


class AdmobActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdmobBinding

    //插入式广告
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdmobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.simpleCreate.setOnClickListener { simpleCreate() }
        binding.setTestCreate.setOnClickListener { setTestCreate() }
        binding.setInterstitialAd.setOnClickListener { setInterstitialAd() }

    }

    //https://developers.google.com/admob/android/interstitial
    //测试账号的插页式广告
    private fun setInterstitialAd() {

        val adRequest = AdRequest.Builder().build()

        //加载广告
        InterstitialAd.load(this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    logToast(adError.message)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    logToast("广告开始读取")
                    mInterstitialAd = interstitialAd

                    //FullScreenContentCallback 处理与展示 InterstitialAd 相关的事件。
                    // 在展示 InterstitialAd 之前，请务必按如下方式设置回调：
                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                logToast("广告已经关闭了")
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                logToast("Ad failed to show.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                logToast("广告正在全屏展示")
                                mInterstitialAd = null
                            }
                        }

                    //展示广告
                    if (mInterstitialAd != null) {
                        mInterstitialAd?.show(this@AdmobActivity)
                    } else {
                        logToast("The interstitial ad wasn't ready yet.")
                    }
                }
            })
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