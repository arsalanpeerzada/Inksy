package com.inksy.UI.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.example.DoodlePack
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Activities.ProfileActivity
import com.inksy.UI.Activities.ViewAll
import com.inksy.UI.Adapter.ArtworkAdapter
import com.inksy.UI.Constants
import com.inksy.UI.ViewModel.DoodleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentArtworkBinding
import java.io.Serializable

class Artwork : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: FragmentArtworkBinding
    lateinit var doodleView: DoodleView
    var token: String = ""
    lateinit var tinyDB: TinyDB
    var feature: ArrayList<DoodlePack> = ArrayList()
    var search: ArrayList<DoodlePack> = ArrayList()
    var pack: ArrayList<DoodlePack> = ArrayList()
    lateinit var featureAdapter: ArtworkAdapter
    lateinit var packAdapter: ArtworkAdapter
    lateinit var refreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArtworkBinding.inflate(layoutInflater)
        doodleView = ViewModelProvider(requireActivity())[DoodleView::class.java]
        doodleView.init()

        binding.loader.visibility = View.VISIBLE
        tinyDB = TinyDB(requireContext())
        token = tinyDB.getString("token").toString()

        if (!tinyDB.getString("avatar").isNullOrEmpty()) {
            Glide.with(requireContext()).load(Constants.BASE_IMAGE + tinyDB.getString("avatar"))
                .placeholder(R.drawable.ic_empty_user)
                .into(binding.profile)
        }

        binding.seeall2.setOnClickListener {
            requireContext().startActivity(
                Intent(requireContext(), ViewAll::class.java).putExtra(
                    Constants.activity,
                    Constants.doodleViewAll
                ).putExtra("Data", true).putExtra("List", pack as Serializable)
            )
        }

        binding.seeall1.setOnClickListener {
            requireContext().startActivity(
                Intent(requireContext(), ViewAll::class.java).putExtra(
                    Constants.activity,
                    Constants.doodleViewAll
                ).putExtra("Data", true).putExtra("List", feature as Serializable)
            )

        }

        binding.profile.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(), ProfileActivity::class.java))
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
        refreshLayout = binding.swipe

        refreshLayout.setOnRefreshListener(this)
        refreshLayout.post(Runnable {
            refreshLayout.setRefreshing(true)

            // Fetching data from server
            getData(token)
        })


        return binding.root
    }

    private fun openactivity(search: String) {

//        requireContext().startActivity(
//            Intent(requireContext(), ViewAll::class.java).putExtra(
//                Constants.activity,
//                Constants.doodleSearch
//            ).putExtra("Data", true).putExtra("List", search as Serializable)
//        )
        binding.loader.visibility = View.VISIBLE
        searchUser(search)
    }


    private fun performSearch() {
        binding.search.clearFocus()
        binding.search.text.clear()
        val input: InputMethodManager? =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input?.hideSoftInputFromWindow(binding.search.getWindowToken(), 0)
        //...perform search
    }


    fun getData(token: String) {

        doodleView.getData(token)?.observe(requireActivity()) {
            binding.loader.visibility = View.GONE
            when (it.status) {
                Status.SUCCESS -> {
                    feature = it?.data?.data?.featuredPack!!
                    pack = it.data.data?.pack!!

                    if (feature.size == 0 && pack.size == 0) {
                        binding.layoutempty.visibility = View.VISIBLE
                        binding.rvFeatured.visibility = View.GONE
                        binding.rvBestsellers.visibility = View.GONE


                        binding.textView3.visibility = View.GONE
                        binding.view12.visibility = View.GONE
                        binding.seeall1.visibility = View.GONE

                        binding.textView6.visibility = View.GONE
                        binding.view13.visibility = View.GONE
                        binding.seeall2.visibility = View.GONE
                    } else {


                        featureAdapter = ArtworkAdapter(requireContext(), feature, "Feature")
                        binding.rvFeatured.adapter = featureAdapter

                        packAdapter = ArtworkAdapter(requireContext(), pack, "Pack")
                        binding.rvBestsellers.adapter = packAdapter
                    }

                    refreshLayout.isRefreshing = false;

                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    refreshLayout.isRefreshing = false;
                }
            }
        }
    }


    override fun onRefresh() {
        getData(token)
    }

    fun searchUser(text: String) {

        val mytoken = "Bearer $token"

        doodleView.searchDoodle(text, mytoken)?.observe(requireActivity()) { it ->
            binding.loader.visibility = View.GONE
            when (it.status) {
                Status.SUCCESS -> {

                    var data = it?.data?.data
                    search = (it?.data?.data as ArrayList<DoodlePack>?)!!
                    requireContext().startActivity(
                        Intent(requireContext(), ViewAll::class.java)
                            .putExtra(Constants.activity, Constants.doodleSearch)
                            .putExtra("List", data as Serializable)
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