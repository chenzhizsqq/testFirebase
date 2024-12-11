package com.chenzhizs.checkfirebase11.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.chenzhizs.checkfirebase11.R
import com.chenzhizs.checkfirebase11.databinding.ActivityAnalyticsBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

//https://firebase.google.com/docs/analytics/get-started?platform=android&authuser=0
class AnalyticsActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "AnalyticsActivity"
        private const val KEY_FAVORITE_FOOD = "favorite_food"
    }

    private lateinit var binding: ActivityAnalyticsBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //初始化数据分析
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics

        // On first app open, ask the user his/her favorite food. Then set this as a user property
        // on all subsequent opens.

        val userFavoriteFood = getUserFavoriteFood()

        binding.tvTest.text = userFavoriteFood
        askFavoriteFood()
        if (userFavoriteFood != null) {
            setUserFavoriteFood(userFavoriteFood)
        }

        // 记录设置点击后的记录
        recordImageView()

        //测试SELECT_ITEM
        test_SELECT_ITEM()
    }

    /**
     * Get the user's favorite food from shared preferences.
     * @return favorite food, as a string.
     */
    private fun getUserFavoriteFood(): String? {
        return PreferenceManager.getDefaultSharedPreferences(this)
            .getString(KEY_FAVORITE_FOOD, null)
    }


    /**
     * Display a dialog prompting the user to pick a favorite food from a list, then record
     * the answer.
     */
    private fun askFavoriteFood() {
        val choices = resources.getStringArray(R.array.food_items)
        val ad = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(R.string.food_dialog_title)
            .setItems(choices) { _, which ->
                val food = choices[which]
                setUserFavoriteFood(food)
            }.create()

        ad.show()
    }

    /**
     * Set the user's favorite food as an app measurement user property and in shared preferences.
     * @param food the user's favorite food.
     */
    private fun setUserFavoriteFood(food: String) {

        //这是安卓本机的默认记录
        PreferenceManager.getDefaultSharedPreferences(this).edit()
            .putString(KEY_FAVORITE_FOOD, food)
            .apply()

        //这是放送到firebase的记录
        // [START user_property]
        firebaseAnalytics.setUserProperty(KEY_FAVORITE_FOOD, food)
        // [END user_property]

    }

    private fun recordImageView() {

        // [START image_view_event]
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            Log.e(TAG, "recordImageView: firebaseAnalytics.logEvent start")
            param(FirebaseAnalytics.Param.ITEM_ID, "test_Analytics_id")
            param(FirebaseAnalytics.Param.ITEM_NAME, "test_Analytics_name")
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "test_CONTENT_TYPE")
        }
        // [END image_view_event]
    }

    private fun test_SELECT_ITEM() {


        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_LIST_ID, "itemListId")
        bundle.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, "itemListName")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle)
    }
}