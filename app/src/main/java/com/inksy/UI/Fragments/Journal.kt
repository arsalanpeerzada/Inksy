package com.inksy.UI.Fragments

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.inksy.Model.Categories
import com.inksy.Model.People
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Activities.ProfileActivity
import com.inksy.UI.Activities.ViewAll
import com.inksy.UI.Constants
import com.inksy.UI.ViewModel.DashboardView
import com.inksy.UI.ViewModel.JournalView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentJournalBinding
import com.inksy.databinding.Tablayout1Binding
import java.io.Serializable


class Journal : Fragment() {
    lateinit var vpPager: ViewPager
    lateinit var binding: FragmentJournalBinding
    private lateinit var adapterViewPager: MyPagerAdapter
    lateinit var v: Tablayout1Binding
    lateinit var dashboardView: DashboardView
    lateinit var people: People
    lateinit var tinyDB: TinyDB
    private lateinit var tvTitle: TextView
    private lateinit var tvContinue: TextView
    var numberPicker: NumberPicker? = null
    private lateinit var bottomSheetDialog: RoundedBottomSheetDialog
    private lateinit var bottomSheetDialogCategory: RoundedBottomSheetDialog
    var catList: List<Categories> = ArrayList()
    lateinit var jouralView: JournalView
    lateinit var categorydata: Array<String?>
    var token: String = " "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJournalBinding.inflate(layoutInflater)
        vpPager = binding.vpPager
        dashboardView = ViewModelProvider(requireActivity())[DashboardView::class.java]
        dashboardView.init()
        tinyDB = TinyDB(requireContext())

        jouralView = ViewModelProvider(requireActivity())[JournalView::class.java]
        jouralView.init()


        Handler().postDelayed({
            binding.loader.visibility = View.GONE
        }, 3000)

        token = tinyDB.getString("token").toString()
        v = binding.include3

//        var tab1: TextView = v.tab1
//        var tab2: TextView = v.findViewById(R.id.tab2)

        v.tab1.text = getString(R.string.journal)
        v.tab2.text = getString(R.string.people)
        // getData()

        v.tab1.setOnClickListener {
            selectTab(0)

            vpPager.currentItem = 0
            binding.searchview.visibility = View.VISIBLE

        }
        v.tab2.setOnClickListener {

            selectTab(1)
            vpPager.currentItem = 1
            binding.searchview.visibility = View.GONE

        }

        binding.profile.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }
        binding.search.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val text = binding.search.text.toString()
                performSearch()
                openactivity(text)
                return@OnEditorActionListener true
            }
            false
        })

        binding.searchview.setOnClickListener() {

            if (vpPager.currentItem == 0) {
                openRoundBottomSheet()
            }
        }

        selectTab(0)
        setupViewPager()


//        getData()
        return binding.root
    }


    private fun openactivity(text: String) {
        binding.loader.visibility = View.VISIBLE
        Log.d("searchText", text)

        if (vpPager.currentItem == 0) {

            search(text, 0)
            binding.searchview.visibility = View.VISIBLE

        } else if (vpPager.currentItem == 1) {
            search(text, 1)

        }
    }

    private fun performSearch() {
        binding.search.clearFocus()
        binding.search.text.clear()

        val input: InputMethodManager? =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input?.hideSoftInputFromWindow(binding.search.windowToken, 0)
        //...perform search
    }

    class MyPagerAdapter(
        fragmentManager: FragmentManager?,
    ) : FragmentPagerAdapter(fragmentManager!!) {
        // Returns total number of pages
        override fun getCount(): Int {
            return NUM_ITEMS
        }

        // Returns the fragment to display for that page
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> Sub_Journal()
                1 -> Sub_People()

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

    private fun selectTab(position: Int) {
        when (position) {
            0 -> {
                v.tab1.setTextColor(resources.getColor(R.color.appBlue))
                v.tab2.setTextColor(resources.getColor(R.color.realwhite))
                v.tab1.setBackgroundResource(R.drawable.round_border_edittext_blue)
                v.tab2.setBackgroundResource(R.drawable.round_border_edittext_gradient)
                binding.searchview.visibility = View.VISIBLE

            }

            1 -> {
                v.tab2.setTextColor(resources.getColor(R.color.appBlue))
                v.tab1.setTextColor(resources.getColor(R.color.realwhite))
                v.tab2.setBackgroundResource(R.drawable.round_border_edittext_blue)
                v.tab1.setBackgroundResource(R.drawable.round_border_edittext_gradient)
                binding.searchview.visibility = View.GONE
                binding.search.clearFocus()
            }
        }
    }

//    private fun getData() {
//
//
//        val mytoken = "Bearer $token"
//        val image = tinyDB.getString("avatar")
//        if (!image.isNullOrEmpty())
//            Glide.with(requireContext()).load(Constants.BASE_IMAGE + image).into(binding.profile)
//
//        dashboardView.getData(mytoken)?.observe(requireActivity()) { it ->
//
//            when (it.status) {
//                Status.SUCCESS -> {
//
//                    categories = it?.data?.data?.categories!!
//
//                    myjournals = it.data.data?.journals!!
//
//                    otherJournals = it.data.data?.followedJournals!!
//
//                    people = it.data.data?.people!!
//
//
//
//                }
//
//                Status.ERROR -> {
//
//                    tinyDB.clear()
//
//                    requireContext().startActivity(
//                        Intent(
//                            requireContext(),
//                            StartingActivity::class.java
//                        )
//                    )
//                    Toast.makeText(requireContext(), "Token Expired", Toast.LENGTH_SHORT).show()
//                    refreshLayout.isRefreshing = false;
//                }
//                Status.LOADING -> {}
//
//            }
//        }
//    }

    fun search(text: String, i: Int) {
        binding.search.text.clear()
        binding.search.clearFocus()
        val mytoken = "Bearer $token"

        if (i == 0) {
            dashboardView.searchJournal(text, mytoken)?.observe(requireActivity()) { it ->
                binding.loader.visibility = View.GONE
                when (it.status) {
                    Status.SUCCESS -> {

                        var data = it?.data?.data

                        requireContext().startActivity(
                            Intent(requireContext(), ViewAll::class.java)
                                .putExtra(Constants.activity, Constants.sub_journalSearch)
                                .putExtra("List", data as Serializable)
                                .putExtra("Data", true)
                        )
                    }

                    Status.ERROR -> {
                        Toast.makeText(requireContext(), "No Journal Found", Toast.LENGTH_SHORT)
                            .show()
                    }

                    Status.LOADING -> {}

                }
            }
        } else if (i == 1) {
            dashboardView.searchUser(text, mytoken)?.observe(requireActivity()) { it ->
                binding.loader.visibility = View.GONE
                when (it.status) {
                    Status.SUCCESS -> {

                        val data = it?.data?.data

                        requireContext().startActivity(
                            Intent(requireContext(), ViewAll::class.java)
                                .putExtra(Constants.activity, Constants.peopleSearch)
                                .putExtra("List", data as Serializable)
                                .putExtra("Data", true)
                        )
                    }

                    Status.ERROR -> {
                        Toast.makeText(requireContext(), "No People Found", Toast.LENGTH_SHORT)
                            .show()
                    }

                    Status.LOADING -> {}

                }
            }
        } else if (i == 2) {
            dashboardView.searchCategory(text, mytoken)?.observe(requireActivity()) { it ->
                binding.loader.visibility = View.GONE
                when (it.status) {
                    Status.SUCCESS -> {

                        var data = it?.data?.data

                        requireContext().startActivity(
                            Intent(requireContext(), ViewAll::class.java)
                                .putExtra(Constants.activity, Constants.sub_journalSearch)
                                .putExtra("List", data as Serializable)
                                .putExtra("Data", true)
                        )
                    }

                    Status.ERROR -> {
                        Toast.makeText(requireContext(), "No Journal Found", Toast.LENGTH_SHORT)
                            .show()
                    }

                    Status.LOADING -> {}

                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        if (!tinyDB.getString("avatar").isNullOrEmpty()) {
            Glide.with(requireContext()).load(Constants.BASE_IMAGE + tinyDB.getString("avatar"))
                .placeholder(R.drawable.ic_empty_user)
                .into(binding.profile)
        }
        binding.search.clearFocus()
        if (vpPager.currentItem == 0) {
            binding.searchview.visibility = View.VISIBLE
        } else {
            binding.searchview.visibility = View.GONE
        }
    }

    fun setupViewPager(

    ) {

        adapterViewPager = MyPagerAdapter(childFragmentManager)
        vpPager.adapter = adapterViewPager


        vpPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                selectTab(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }


    private fun openRoundBottomSheet() {
        getData(token)
        bottomSheetDialog = RoundedBottomSheetDialog(requireContext())
        val bottomDialogView: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.bottom_dialog_wheel, null)
        bottomSheetDialog.setContentView(bottomDialogView)

        numberPicker = bottomDialogView.findViewById<NumberPicker>(R.id.numberPicker)
        tvContinue = bottomDialogView.findViewById<TextView>(R.id.tvContinue)
        tvTitle = bottomDialogView.findViewById<TextView>(R.id.tvTitle)
        var bottomsheet_search = bottomSheetDialog.findViewById<EditText>(R.id.bottomsheet_search)

        tvTitle.text = "Select Search Item"

        var data = arrayOf("Journal Name", "Category")
        numberPicker?.minValue = 0 //from array first value
        numberPicker?.maxValue = data.size - 1 //to array last value


        numberPicker?.displayedValues = data
        numberPicker?.wrapSelectorWheel = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            numberPicker?.textColor = resources.getColor(R.color.black)
        }


        tvContinue.setOnClickListener(View.OnClickListener { view1: View? ->


            if (data[numberPicker?.value!!] == "Category") {
                bottomSheetDialog.dismiss()
                if (categorydata.size != 0) {
                    openRoundBottomSheetCategory()
                } else {
                    binding.loader.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Categories are not ready please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                binding.searchview.visibility = View.GONE
                tvTitle.setText("Enter Journal Name")

                numberPicker?.visibility = View.GONE
                bottomsheet_search?.visibility = View.VISIBLE
                tvContinue.visibility = View.GONE


                bottomsheet_search?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        val text = bottomsheet_search?.text.toString()
                        performSearch()
                        openactivity(text)
                        bottomSheetDialog.dismiss()
                        return@OnEditorActionListener true
                    }
                    false
                })

                val imm: InputMethodManager =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.search, InputMethodManager.SHOW_IMPLICIT)

            }

        })
        bottomSheetDialog.show()
    }

    fun getData(token: String) {
        jouralView.getCategoriesList(token)?.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {

                    for (i in 0 until it?.data?.data?.size!!) {

                        catList = it.data.data!!
                        categorydata = arrayOfNulls<String>(catList.size)

                        val namelist = ArrayList<String>()
                        for (i in 0 until catList.size) {
                            val data = catList[i].categoryName.toString()
                            namelist.add(data)
                        }
                        for (i in 0 until catList.size) {
                            categorydata[i] = namelist[i]
                        }
                    }
                }

                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }
    }

    private fun openRoundBottomSheetCategory() {
        bottomSheetDialogCategory = RoundedBottomSheetDialog(requireContext())
        val bottomDialogView: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.bottom_dialog_wheel, null)
        bottomSheetDialogCategory.setContentView(bottomDialogView)

        numberPicker = bottomDialogView.findViewById<NumberPicker>(R.id.numberPicker)
        tvContinue = bottomDialogView.findViewById<TextView>(R.id.tvContinue)
        tvTitle = bottomDialogView.findViewById<TextView>(R.id.tvTitle)

        tvTitle.text = getString(R.string.select_category)

        numberPicker?.minValue = 0 //from array first value
        numberPicker?.maxValue = catList.size - 1 //to array last value

        numberPicker?.displayedValues = categorydata
        numberPicker?.wrapSelectorWheel = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            numberPicker?.textColor = resources.getColor(R.color.black)
        }


        tvContinue.setOnClickListener(View.OnClickListener { view1: View? ->
            binding.search.setText(catList[numberPicker?.value!!].categoryName)
            search(catList[numberPicker?.value!!].categoryName.toString(), 2)
            bottomSheetDialogCategory.dismiss()
        })
        bottomSheetDialogCategory.show()
    }


}