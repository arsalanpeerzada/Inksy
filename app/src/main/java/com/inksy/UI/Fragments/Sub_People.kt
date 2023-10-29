package com.inksy.UI.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.inksy.Interfaces.OnChangeStateClickListener
import com.inksy.Model.UserModel
import com.inksy.Remote.Status
import com.inksy.UI.Adapter.PeopleDashboardAdapter
import com.inksy.UI.ViewModel.DashboardView
import com.inksy.UI.ViewModel.PeopleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentSubPeopleBinding

class Sub_People : Fragment(), OnChangeStateClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: FragmentSubPeopleBinding
    lateinit var peopleView: PeopleView
    lateinit var tinyDB: TinyDB
    lateinit var list: ArrayList<UserModel>
    var token = ""
    lateinit var dashboardView: DashboardView
    lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var adapter: PeopleDashboardAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubPeopleBinding.inflate(layoutInflater)
        refreshLayout = binding.refreshListener
        peopleView = ViewModelProvider(requireActivity())[PeopleView::class.java]
        peopleView.init()
        dashboardView = ViewModelProvider(requireActivity())[DashboardView::class.java]
        tinyDB = TinyDB(requireContext())
        token = tinyDB.getString("token").toString()
        list = ArrayList()


        refreshLayout = binding.refreshListener

        refreshLayout.setOnRefreshListener(this)
        refreshLayout.post(Runnable {
            refreshLayout.setRefreshing(true)

            // Fetching data from server
            getData()
        })

        return binding.root
    }

    override fun onResume() {
        refresh()
        super.onResume()
    }

    override fun onStateChange(position: Int, like: Boolean, type: String) {
        unfollowUser(list[position].id!!, position)
    }

    private fun unfollowUser(id: Int, position: Int) {

        var mytoken = "Bearer $token"
        peopleView.userUnfollow(id, mytoken)?.observe(requireActivity()) { it ->
            when (it?.status) {
                Status.SUCCESS -> {
                    Toast.makeText(
                        requireActivity(),
                        it.data?.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    adapter.notifyItemRemoved(position)
                    list.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                } else -> {}
            }
        }
    }

    private fun getData() {
        val mytoken = "Bearer $token"
        dashboardView.getData(mytoken)?.observe(requireActivity()) { it ->

            when (it.status) {
                Status.SUCCESS -> {

                    // people = it.data?.data?.people
                    list = it.data?.data?.people?.followers!!
                    if (list.size > 0) {
                        adapter = PeopleDashboardAdapter(requireContext(), list, false, this)
                        binding.rvChat.adapter = adapter

                        binding.rvChat.visibility = View.VISIBLE
                        binding.layoutemptyPeople.visibility = View.GONE

                    } else {
                        binding.rvChat.visibility = View.GONE
                        binding.layoutemptyPeople.visibility = View.VISIBLE
                    }

                    refreshLayout.isRefreshing = false;
                }

                Status.ERROR -> {
                    refreshLayout.isRefreshing = false;
                }
                Status.LOADING -> {}

            }
        }
    }


    override fun onRefresh() {
        refresh()
    }

    fun refresh() {
        getData()

    }


}