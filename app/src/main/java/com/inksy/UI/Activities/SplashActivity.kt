package com.inksy.UI.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.inksy.Utils.Permissions
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding
    var isLaunchedFromNotification = "False"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        checkNotificationData()

        Handler().postDelayed({
            var tinyDB = TinyDB(this)
            var isLogin = tinyDB.getString("token")
            var fullname = tinyDB.getString("fullname")
            if (isLogin != "" && fullname != "") {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java)
                    .putExtra("NotificationCheck", isLaunchedFromNotification))
                this.finish()
            } else {
                startActivity(Intent(this@SplashActivity, StartingActivity::class.java))
                this.finish()
            }

        }, 2000)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("NotificationLog", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            Log.d("FCMToken", token)
        })

    }

    override fun onBackPressed() {

    }

    private fun checkNotificationData(){

        val bundle = intent.extras

        if(bundle != null){
            if(bundle.getString("ComesFromNotification") != null &&
                bundle.getString("ComesFromNotification").equals("True")){
                isLaunchedFromNotification = "True"

            }
        }
    }
}