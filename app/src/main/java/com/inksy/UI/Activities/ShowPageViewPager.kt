package com.inksy.UI.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.inksy.Database.JournalDatabase
import com.inksy.R
import com.inksy.UI.Fragments.ShowPageFragment
import com.inksy.Utils.TinyDB
import com.inksy.Utils.ViewPagerAdapter
import com.inksy.databinding.ActivityShowPageViewPagerBinding

class ShowPageViewPager : AppCompatActivity() {

    lateinit var binding: ActivityShowPageViewPagerBinding
    private var viewPager: ViewPager? = null
    private var adapter: ViewPagerAdapter? = null
    lateinit var journalDatabase: JournalDatabase
    var createdby = 0
    lateinit var tinyDb: TinyDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowPageViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIDs()
        setEvents()
        journalDatabase = JournalDatabase.getInstance(this)!!

        tinyDb = TinyDB(this)
        var pages = journalDatabase.getJournalData().getAllPages()

        var pagerows = pages.size

        if (intent.extras != null) {
            createdby = intent.getIntExtra("createdby", 0)

        }

        binding.back.performClick()

        for (i in 0 until pagerows) {
            addPage(pages[i].pageId.toString())
        }

        binding.userprofileicon.setOnClickListener {
            var userid = createdby
            var ownprofile = tinyDb.getString("ownprofileid")?.toInt()
            if (userid == ownprofile)
                startActivity(Intent(this, ProfileActivity::class.java))
            else {
                startActivity(Intent(this, People::class.java).putExtra("UserId", userid))
            }
        }

        binding.index.setOnClickListener {
            this.finish()
        }

        binding.back.setOnClickListener {
            var item = viewPager?.currentItem
            if (viewPager?.currentItem!! > 0) {
                viewPager?.currentItem = viewPager?.currentItem?.minus(1)!!
            }

            if (viewPager?.currentItem!! == 0) {
                binding.back.isEnabled = false
            } else {
                binding.back.isEnabled = true
            }

            binding.next.isEnabled = true
        }

        binding.next.setOnClickListener {
            if (viewPager?.currentItem!! < adapter?.count!!) {
                viewPager?.currentItem = viewPager?.currentItem?.plus(1)!!
            }

            if (viewPager?.currentItem!! == adapter?.count!! - 1) {
                binding.next.isEnabled = false
            } else {
                binding.next.isEnabled = true
            }

            binding.back.isEnabled = true
        }
    }

    private fun getIDs() {
        viewPager = findViewById<View>(R.id.my_viewpager) as ViewPager
        adapter = ViewPagerAdapter(supportFragmentManager, this)
        viewPager!!.adapter = adapter
    }

    private fun setEvents() {
        viewPager!!.currentItem = 0
    }

    fun addPage(pagename: String?) {
        val bundle = Bundle()
        bundle.putString("data", pagename)
        bundle.putInt("createdby", createdby)
        val fragmentChild = ShowPageFragment()
        fragmentChild.arguments = bundle
        adapter?.addFrag(fragmentChild, pagename!!)
        adapter?.notifyDataSetChanged()
//        viewPager!!.currentItem = adapter?.count!! - 1
    }
}