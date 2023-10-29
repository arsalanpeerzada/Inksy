package com.inksy.UI.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.example.DoodlePack
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Remote.Status
import com.inksy.UI.Activities.Doodle_Drawing
import com.inksy.UI.Adapter.DashboardApprovedAdapter
import com.inksy.UI.Constants
import com.inksy.UI.ViewModel.DoodleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentDashboardApprovedBinding

class DashboardApproved : Fragment(), iOnClickListerner {
    lateinit var doodleView: DoodleView
    var doodleApprovedlist: ArrayList<DoodlePack> = ArrayList()
    lateinit var tinydb: TinyDB
    var token = ""
    lateinit var _adapter: DashboardApprovedAdapter
    lateinit var binding: FragmentDashboardApprovedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardApprovedBinding.inflate(layoutInflater)
        tinydb = TinyDB(requireContext())
        token = tinydb.getString("token").toString()
        doodleView = ViewModelProvider(this)[DoodleView::class.java]
        doodleView.init()
        getData()
        _adapter = DashboardApprovedAdapter(requireContext(), doodleApprovedlist, this)
        binding.rvDashboardApproved.adapter = _adapter


        return binding.root
    }

    private fun getData() {
        doodleView.doodleApproved(token)?.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    doodleApprovedlist = it?.data?.data as ArrayList<DoodlePack>
                    if (doodleApprovedlist.size > 0) {
                        _adapter =
                            DashboardApprovedAdapter(requireContext(), doodleApprovedlist, this)
                        binding.rvDashboardApproved.visibility = View.VISIBLE
                        binding.rvDashboardApproved.adapter = _adapter
                    } else {
                        binding.layoutemptydoodle.visibility = View.VISIBLE
                    }

                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it?.data?.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun onclick(Id: Int) {
        var ids = Id.toString()
        requireContext().startActivity(
            Intent(
                requireContext(),
                Doodle_Drawing::class.java
            ).putExtra("fragment", Constants.fragment_approved).putExtra("Id", ids)
        )
    }


}