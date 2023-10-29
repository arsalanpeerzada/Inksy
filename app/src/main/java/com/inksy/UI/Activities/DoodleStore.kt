package com.inksy.UI.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.example.example.DoodlePack
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Constants
import com.inksy.UI.Fragments.DoodleSubStore
import com.inksy.UI.ViewModel.DoodleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivityDoodleStoreBinding
import com.inksy.databinding.Tablayout1Binding
import java.io.Serializable

class DoodleStore : AppCompatActivity(){
    private lateinit var adapterViewPager: MyPagerAdapter
    lateinit var binding: ActivityDoodleStoreBinding
    lateinit var doodleView: DoodleView
    var token: String = ""
    lateinit var tinyDB: TinyDB
    lateinit var vpPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoodleStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        doodleView = ViewModelProvider(this)[DoodleView::class.java]
        doodleView.init()

        binding.loader.visibility = View.VISIBLE
        Handler().postDelayed(object : Runnable {
            override fun run() {
                binding.loader.visibility = View.GONE
            }

        }, 300)

        tinyDB = TinyDB(this)
        token = tinyDB.getString("token").toString()

        vpPager = binding.vpPager
        adapterViewPager = MyPagerAdapter(supportFragmentManager)
        vpPager.adapter = adapterViewPager

        var v: Tablayout1Binding = binding.include5

//        var tab1: TextView = v.findViewById(R.id.tab1)
//        var tab2: TextView = v.findViewById(R.id.tab2)

        v.tab1.text = getString(R.string.store)
        v.tab2.text = getString(R.string.purchase)

        vpPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    v.tab1.setTextColor(resources.getColor(R.color.appBlue))
                    v.tab2.setTextColor(resources.getColor(R.color.realwhite))
                    v.tab1.setBackgroundResource(R.drawable.round_border_edittext_blue)
                    v.tab2.setBackgroundResource(R.drawable.round_border_edittext_gradient)

                } else if (position == 1) {
                    v.tab2.setTextColor(resources.getColor(R.color.appBlue))
                    v.tab1.setTextColor(resources.getColor(R.color.realwhite))
                    v.tab2.setBackgroundResource(R.drawable.round_border_edittext_blue)
                    v.tab1.setBackgroundResource(R.drawable.round_border_edittext_gradient)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        v.tab1.setOnClickListener {

            v.tab1.setTextColor(resources.getColor(R.color.appBlue))
            v.tab2.setTextColor(resources.getColor(R.color.realwhite))
            v.tab1.setBackgroundResource(R.drawable.round_border_edittext_blue)
            v.tab2.setBackgroundResource(R.drawable.round_border_edittext_gradient)
            val viewPagerCurrentItem: Int = vpPager.currentItem

            vpPager.currentItem = 0

        }
        v.tab2.setOnClickListener {
            v.tab2.setTextColor(resources.getColor(R.color.appBlue))
            v.tab1.setTextColor(resources.getColor(R.color.realwhite))
            v.tab2.setBackgroundResource(R.drawable.round_border_edittext_blue)
            v.tab1.setBackgroundResource(R.drawable.round_border_edittext_gradient)
            val viewPagerCurrentItem: Int = vpPager.currentItem

            vpPager.currentItem = 1

        }

        binding.back.setOnClickListener() {
            onBackPressed()
            this.finish()
        }

        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                var text = binding.search.text.toString()
                performSearch()
                openactivity(text)
                return@OnEditorActionListener true
            }
            false
        })

    }

    private fun openactivity(text: String) {

        binding.loader.visibility = View.VISIBLE
        Log.d("searchText", text)
        if (vpPager.currentItem == 0) {

            searchUser(text, 0)

        } else if (vpPager.currentItem == 1) {
            searchUser(text, 1)

        }



    }

    private fun performSearch() {
        binding.search.clearFocus()
        binding.search.text.clear()
        val input: InputMethodManager? =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input?.hideSoftInputFromWindow(binding.search.getWindowToken(), 0)
        //...perform search
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
                0 -> DoodleSubStore("Store")
                1 -> DoodleSubStore("Purchased")

                else -> null!!
            }
        }

        // Returns the page title for the top indicator
        override fun getPageTitle(position: Int): CharSequence? {
            return "$position"
        }

        companion object {
            private const val NUM_ITEMS = 2
        }
    }

    fun searchUser(text: String, i: Int) {

        val mytoken = "Bearer $token"

        if (i == 0) {
            doodleView.searchDoodle(text, mytoken)?.observe(this) { it ->
                binding.loader.visibility = View.GONE
                when (it.status) {
                    Status.SUCCESS -> {


                        var data = it?.data?.data
                        var search = (it?.data?.data as ArrayList<DoodlePack>?)!!
                        startActivity(
                            Intent(this, ViewAll::class.java)
                                .putExtra(Constants.activity, Constants.doodleSearch)
                                .putExtra("List", search as Serializable)
                                .putExtra("Data", true)
                        )
                    }

                    Status.ERROR -> {
                    }
                    Status.LOADING -> {}

                }
            }
        } else {
            doodleView.doodlePurchased(mytoken)?.observe(this) { it ->
                binding.loader.visibility = View.GONE
                when (it.status) {
                    Status.SUCCESS -> {

                        var data = it?.data?.data
                        var search = (it?.data?.data as ArrayList<DoodlePack>?)!!
                        startActivity(
                            Intent(this, ViewAll::class.java)
                                .putExtra(Constants.activity, Constants.doodleSearch)
                                .putExtra("List", search as Serializable)
                                .putExtra("Data", true)
                        )
                    }

                    Status.ERROR -> {

                    }
                    Status.LOADING -> {}

                }
            }
        }


    }


}