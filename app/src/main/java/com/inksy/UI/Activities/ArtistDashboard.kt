package com.inksy.UI.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.inksy.Interfaces.OnDialogClickListener
import com.inksy.R
import com.inksy.UI.Constants
import com.inksy.UI.Dialogs.TwoButtonDialog
import com.inksy.UI.Fragments.DashboardApproved
import com.inksy.UI.Fragments.DashboardPending
import com.inksy.UI.Fragments.Dashboard_Analytics
import com.inksy.databinding.ActivityArtistDashboardBinding
import com.inksy.databinding.Tablayout2Binding


class ArtistDashboard : AppCompatActivity() {

    private lateinit var adapterViewPager: MyPagerAdapter
    lateinit var binding: ActivityArtistDashboardBinding
    lateinit var vpPager: ViewPager

    lateinit var tab1: TextView
    lateinit var tab2: TextView
    lateinit var tab3: TextView
    lateinit var v: Tablayout2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vpPager = binding.vpPager
        adapterViewPager = MyPagerAdapter(supportFragmentManager)
        vpPager.adapter = adapterViewPager


        v = binding.include7

        val activity = intent.getStringExtra("activity")
        if (activity == Constants.packActivity) {
            selectTab(2)
        }
        binding.ivBack.setOnClickListener {
            exitCheck()
        }

        vpPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.Maintext1.text = getString(R.string.welcome)
                        binding.MainText2.text = getString(R.string.ArtistPanel)
                        v.tab1.setTextColor(resources.getColor(R.color.appBlue))
                        v.tab2.setTextColor(resources.getColor(R.color.realwhite))
                        v.tab3.setTextColor(resources.getColor(R.color.realwhite))

                        v.tab1.setBackgroundResource(R.drawable.round_border_edittext_blue)
                        v.tab2.setBackgroundResource(R.drawable.round_border_edittext_gradient)
                        v.tab3.setBackgroundResource(R.drawable.round_border_edittext_gradient)


                    }
                    1 -> {
                        binding.Maintext1.text = getString(R.string.welcome)
                        binding.MainText2.text = getString(R.string.ArtSection)
                        v.tab2.setTextColor(resources.getColor(R.color.appBlue))
                        v.tab1.setTextColor(resources.getColor(R.color.realwhite))
                        v.tab3.setTextColor(resources.getColor(R.color.realwhite))
                        v.tab2.setBackgroundResource(R.drawable.round_border_edittext_blue)
                        v.tab1.setBackgroundResource(R.drawable.round_border_edittext_gradient)
                        v.tab3.setBackgroundResource(R.drawable.round_border_edittext_gradient)
                    }
                    2 -> {
                        binding.Maintext1.text = getString(R.string.welcome)
                        binding.MainText2.text = getString(R.string.statusofyoursubmittedart)
                        v.tab3.setTextColor(resources.getColor(R.color.appBlue))
                        v.tab1.setTextColor(resources.getColor(R.color.realwhite))
                        v.tab2.setTextColor(resources.getColor(R.color.realwhite))
                        v.tab3.setBackgroundResource(R.drawable.round_border_edittext_blue)
                        v.tab1.setBackgroundResource(R.drawable.round_border_edittext_gradient)
                        v.tab2.setBackgroundResource(R.drawable.round_border_edittext_gradient)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        binding.pack.setOnClickListener {
            startActivity(Intent(this, PackActivity::class.java))

        }


        v.tab1.setOnClickListener {
            selectTab(0)
        }
        v.tab2.setOnClickListener {
            selectTab(1)
        }

        v.tab3.setOnClickListener {
            selectTab(2)
        }
    }

    private fun selectTab(i: Int) {
        when (i) {
            0 -> {
                v.tab1.setTextColor(resources.getColor(R.color.appBlue))
                v.tab2.setTextColor(resources.getColor(R.color.realwhite))
                v.tab3.setTextColor(resources.getColor(R.color.realwhite))
                v.tab1.setBackgroundResource(R.drawable.round_border_edittext_blue)
                v.tab2.setBackgroundResource(R.drawable.round_border_edittext_gradient)
                v.tab3.setBackgroundResource(R.drawable.round_border_edittext_gradient)
                vpPager.currentItem = 0
            }
            1 -> {
                v.tab2.setTextColor(resources.getColor(R.color.appBlue))
                v.tab1.setTextColor(resources.getColor(R.color.realwhite))
                v.tab1.setTextColor(resources.getColor(R.color.realwhite))
                v.tab2.setBackgroundResource(R.drawable.round_border_edittext_blue)
                v.tab1.setBackgroundResource(R.drawable.round_border_edittext_gradient)
                v.tab3.setBackgroundResource(R.drawable.round_border_edittext_gradient)

                vpPager.currentItem = 1
            }
            2 -> {
                v.tab3.setTextColor(resources.getColor(R.color.appBlue))
                v.tab1.setTextColor(resources.getColor(R.color.realwhite))
                v.tab2.setTextColor(resources.getColor(R.color.realwhite))
                v.tab3.setBackgroundResource(R.drawable.round_border_edittext_blue)
                v.tab1.setBackgroundResource(R.drawable.round_border_edittext_gradient)
                v.tab2.setBackgroundResource(R.drawable.round_border_edittext_gradient)
                vpPager.currentItem = 2
            }
        }
    }


    class MyPagerAdapter(fragmentManager: FragmentManager?) :
        FragmentPagerAdapter(fragmentManager!!) {
        // Returns total number of pages
        override fun getCount(): Int {
            return NUM_ITEMS
        }

        // Returns the fragment to display for that page
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> Dashboard_Analytics()
                1 -> DashboardApproved()
                2 -> DashboardPending()
                else -> null!!
            }
        }

        // Returns the page title for the top indicator
        override fun getPageTitle(position: Int): CharSequence {
            return "$position"
        }

        companion object {
            private const val NUM_ITEMS = 3
        }
    }

    override fun onBackPressed() {
        exitCheck()
    }

    private fun exitCheck() {
        val twoButtonDialog: TwoButtonDialog = TwoButtonDialog(
            this, getString(R.string.app_name),
            getString(R.string.artiststring),
            getString(R.string.iamsure),
            "No",
            object : OnDialogClickListener {
                override fun onDialogClick(callBack: String?) {
                    if (callBack == "Yes") {
                        this@ArtistDashboard.finish()
                    } else {

                    }
                }
            })
        twoButtonDialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        twoButtonDialog.show()

    }
}