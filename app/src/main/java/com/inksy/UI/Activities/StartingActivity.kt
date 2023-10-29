package com.inksy.UI.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.inksy.UI.Fragments.NumberVerify
import com.inksy.databinding.ActivityNumberVerificationBinding

class StartingActivity : AppCompatActivity() {


    lateinit var binding: ActivityNumberVerificationBinding
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumberVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.findFragmentById(binding.fragmentContainerView.id)?.let {
            navController = NavHostFragment.findNavController(it)

            it.childFragmentManager.primaryNavigationFragment?.let {
                if (it is NumberVerify) {
//                    finish()
                } else {
//                    super.onBackPressed()
                }
            }
        }

    }

    override fun onBackPressed() {

    }


}