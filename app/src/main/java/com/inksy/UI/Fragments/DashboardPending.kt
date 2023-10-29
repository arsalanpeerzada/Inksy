package com.inksy.UI.Fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.example.DoodlePack
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Activities.Doodle_Drawing
import com.inksy.UI.Adapter.DashboardPendingAdapter
import com.inksy.UI.Constants
import com.inksy.UI.ViewModel.DoodleView
import com.inksy.Utils.SwipeHelper
import com.inksy.Utils.SwipeHelper.UnderlayButtonClickListener
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentDashboardPendingBinding

class DashboardPending : Fragment(), iOnClickListerner {
    lateinit var doodleView: DoodleView
    private var toast: Toast? = null
    lateinit var binding: FragmentDashboardPendingBinding
    lateinit var _adapter: DashboardPendingAdapter
    var doodlePendinglist: ArrayList<DoodlePack> = ArrayList()
    lateinit var tinydb: TinyDB
    var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardPendingBinding.inflate(layoutInflater)
        tinydb = TinyDB(requireContext())
        token = tinydb.getString("token").toString()
        doodleView = ViewModelProvider(this)[DoodleView::class.java]
        doodleView.init()


        getData()
        object : SwipeHelper(requireContext(), binding.rvDashboardApproved, false) {

            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>?
            ) {

                // More Button
                underlayButtons?.add(SwipeHelper.UnderlayButton(
                    "Delete",
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_delete_24
                    ),
                    Color.parseColor("#FFFFFFFF"), Color.parseColor("#F81A00"),
                    UnderlayButtonClickListener { pos: Int ->

                        Toast.makeText(
                            requireContext(),
                            "Item Deleted CLicked at Position: $pos",
                            Toast.LENGTH_SHORT
                        ).show()
                        _adapter.notifyItemChanged(pos)
                    }

                ))
            }


        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData() {
        doodleView.doodlePending(token)?.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    doodlePendinglist = it?.data?.data as ArrayList<DoodlePack>
                    if (doodlePendinglist.size > 0) {
                        _adapter =
                            DashboardPendingAdapter(requireContext(), doodlePendinglist, this)

                        binding.rvDashboardApproved.visibility = View.VISIBLE
                        binding.rvDashboardApproved.adapter = _adapter
                        binding.layoutemptydoodle.visibility = View.GONE
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

    override fun onclick(Id: Int) {
        var ids = Id.toString()
        requireContext().startActivity(
            Intent(
                requireContext(),
                Doodle_Drawing::class.java
            ).putExtra("fragment", Constants.fragment_pending).putExtra("Id", ids)
        )
    }


    private fun toast(text: String) {
        toast?.cancel()
        toast = Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT)
        toast?.show()
    }


}


