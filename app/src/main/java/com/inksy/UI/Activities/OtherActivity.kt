package com.inksy.UI.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.inksy.R
import com.inksy.UI.Fragments.AboutUsDirections
import com.inksy.UI.Fragments.NumberVerify
import com.inksy.UI.Fragments.PrivacyPolicyDirections
import com.inksy.UI.Fragments.TermsDirections
import com.inksy.databinding.ActivityOtherBinding

class OtherActivity : AppCompatActivity() {

    lateinit var binding: ActivityOtherBinding
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportFragmentManager.findFragmentById(binding.fragmentContainerView.id)?.let { it ->
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

        when (Navigation.findNavController(this, R.id.fragmentContainerView)
            .currentDestination?.id) {
            R.id.aboutUs -> {
                // handle back button the way you want here

                val action = AboutUsDirections.actionAboutUsToOthersList()
                navController.navigate(action)
                return;
            }

            R.id.privacyPolicy -> {
                // handle back button the way you want here

                val action = PrivacyPolicyDirections.actionPrivacyPolicyToOthersList()
                navController.navigate(action)
                return;
            }

            R.id.terms -> {
                // handle back button the way you want here

                val action = TermsDirections.actionTermsToOthersList()
                navController.navigate(action)
                return;
            }
            else -> {
                this.finish()
            }
        }

    }

}