package com.inksy.UI.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.inksy.R
import com.inksy.UI.Fragments.EditProfileDirections
import com.inksy.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.findFragmentById(binding.fragmentContainerView.id)?.let {
            navController = NavHostFragment.findNavController(it)
        }
    }

    override fun onBackPressed() {
        if (Navigation.findNavController(this, R.id.fragmentContainerView)
                .currentDestination?.id == R.id.editProfile
        ) {
            // handle back button the way you want here

            val action = EditProfileDirections.actionEditProfileToProfile()
            navController.navigate(action)
            return;
        } else {
            this.finish()
        }
    }

// 2131296547 : id for editprofile
    // 2131296805 : id for profile
}