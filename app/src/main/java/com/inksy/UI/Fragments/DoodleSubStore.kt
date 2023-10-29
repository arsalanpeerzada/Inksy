package com.inksy.UI.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.example.example.DoodlePack
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Remote.Status
import com.inksy.UI.Adapter.DoodleAdapter
import com.inksy.UI.ViewModel.DoodleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentDoodleStoreBinding


class DoodleSubStore(type: String) : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    iOnClickListerner {
    lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var doodleView: DoodleView
    var token: String = ""
    lateinit var tinyDB: TinyDB
    lateinit var vpPager: ViewPager
    lateinit var binding: FragmentDoodleStoreBinding
    var doodleList: ArrayList<DoodlePack> = ArrayList()
    var doodleType = type
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDoodleStoreBinding.inflate(layoutInflater)


        doodleView = ViewModelProvider(this)[DoodleView::class.java]
        doodleView.init()


        tinyDB = TinyDB(requireContext())
        token = tinyDB.getString("token").toString()

        refreshLayout = binding.swipe

        refreshLayout.setOnRefreshListener(this)
        refreshLayout.post(Runnable {
            refreshLayout.setRefreshing(true)

            // Fetching data from server

            getData(token, doodleType)
        })

        return binding.root
    }

    fun getData(token: String, doodleType: String) {
        refreshLayout.isRefreshing = false;
        if (doodleType == "Store") {
            doodleView.getDataAll(token)?.observe(requireActivity()) {

                when (it.status) {
                    Status.SUCCESS -> {

                        doodleList = it.data?.data?.allpack!!
                        refreshLayout.isRefreshing = false;
                        if (doodleList.size == 0) {
                            binding.layoutempty.visibility = View.VISIBLE
                            binding.rvDoodle.visibility = View.GONE
                            refreshLayout.isRefreshing = false;
                        } else {
                            binding.rvDoodle.visibility = View.VISIBLE
                            refreshLayout.isRefreshing = false;
                            binding.rvDoodle.adapter =
                                DoodleAdapter(requireContext(), doodleList, "Pack", this)
                        }
                    }
                    Status.LOADING -> {}
                    Status.ERROR -> {
                        refreshLayout.isRefreshing = false;

                        binding.layoutempty.visibility = View.VISIBLE

                    }
                }
            }
        } else if (doodleType == "Purchased") {
            doodleView.getDataAll(token)?.observe(requireActivity()) {
                refreshLayout.isRefreshing = false
                when (it.status) {
                    Status.SUCCESS -> {

                        doodleList = it.data?.data?.purchased_pack as ArrayList<DoodlePack>


                        if (doodleList.size == 0) {
                            binding.layoutempty.visibility = View.VISIBLE
                        } else {
                            binding.rvDoodle.adapter =
                                DoodleAdapter(requireContext(), doodleList, "Purchased", this)
                        }
                        refreshLayout.isRefreshing = false

                    }
                    Status.LOADING -> {}
                    Status.ERROR -> {
                        refreshLayout.isRefreshing = false
                    }
                }
            }
        }

    }

    override fun onclick(id: Int) {
        super.onclick(id)
        createOrder(doodleList[id].id!!, doodleList[id].price!!.toDouble())
    }


    private fun createOrder(id: Int, price: Double) {
        doodleView.createOrder(id, price, token)?.observe(requireActivity()) {
            refreshLayout.isRefreshing = false
            when (it.status) {
                Status.SUCCESS -> {
                    refreshLayout.isRefreshing = false
                    refresh()

                    // var message = it?.data?.message.toString()
                    Toast.makeText(
                        requireContext(),
                        "Add Successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        it?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    refreshLayout.isRefreshing = false
                }
            }
        }
    }


    override fun onRefresh() {
        refresh()
    }

    private fun refresh() {
        getData(token, doodleType)
    }


}