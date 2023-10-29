package com.inksy.UI.Fragments

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.inksy.Interfaces.OnDialogClickListener
import com.inksy.Model.NotificationDataModel
import com.inksy.Model.UserModel
import com.inksy.Remote.Status
import com.inksy.UI.Adapter.NotificationAdapter
import com.inksy.databinding.FragmentNotificationsBinding
import com.inksy.UI.Activities.List
import com.inksy.UI.Adapter.UsersListAdapter
import com.inksy.UI.Dialogs.TwoButtonDialog
import com.inksy.UI.ViewModel.NotificationView
import com.inksy.UI.ViewModel.PeopleView
import com.inksy.Utils.TinyDB


class Notifications_Fragment : Fragment() {

    lateinit var binding: FragmentNotificationsBinding
    lateinit var notificationView: NotificationView
    var token = " "
    var list: ArrayList<NotificationDataModel> = ArrayList()
    lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(layoutInflater)

        token = TinyDB(requireContext()).getString("token").toString()
        Log.d("Token", token)

        notificationView = ViewModelProvider(this)[NotificationView::class.java]
        notificationView.init()

        binding.followRequests.setOnClickListener {
            startActivity(
                Intent(requireContext(), List::class.java).putExtra(
                    "followRequests",
                    true
                )
            )
        }

        binding.refreshListener.setOnRefreshListener {
            binding.refreshListener.isRefreshing = true
            getNotificationsList(token)
        }

        binding.imgClearNotifications.setOnClickListener {
            openDialog()
        }

        getNotificationsList(token)

        return binding.root
    }

    private fun getNotificationsList(_token: String) {
        notificationView.notificationsList(_token)?.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {}
                Status.ERROR -> {
                    binding.refreshListener.isRefreshing = false
                    binding.rvNotifications.visibility = View.GONE
                    binding.layoutemptyNotifications.visibility = View.VISIBLE

                }
                Status.SUCCESS -> {
                    list.clear()
                    list = it?.data?.data as ArrayList<NotificationDataModel>
                    if (list.size > 0) {
                        adapter = NotificationAdapter(requireContext(), list)
                        binding.rvNotifications.adapter = adapter

                        binding.imgClearNotifications.visibility = View.VISIBLE
                        binding.rvNotifications.visibility = View.VISIBLE
                        binding.layoutemptyNotifications.visibility = View.GONE

                    } else {
                        binding.imgClearNotifications.visibility = View.GONE
                        binding.rvNotifications.visibility = View.GONE
                        binding.layoutemptyNotifications.visibility = View.VISIBLE
                    }

                    binding.refreshListener.isRefreshing = false

                }
            }
        }
    }

    private fun clearNotifications(_token: String) {
        notificationView.clearNotifications(_token)?.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {}
                Status.ERROR -> {
                    binding.refreshListener.isRefreshing = false
                    binding.rvNotifications.visibility = View.GONE
                    binding.layoutemptyNotifications.visibility = View.VISIBLE

                }
                Status.SUCCESS -> {
                    list.clear()

                    binding.rvNotifications.visibility = View.GONE
                    binding.layoutemptyNotifications.visibility = View.VISIBLE
                    binding.refreshListener.isRefreshing = false

                }
            }
        }
    }

    private fun openDialog() {
        val twoButtonDialog: TwoButtonDialog = TwoButtonDialog(
            requireActivity(), "Delete Notifications",
            "Are you sure, You want to delete all your notifications?",
            getString(R.string.yes),
            getString(R.string.no),
            object : OnDialogClickListener {
                override fun onDialogClick(callBack: String?) {
                    if (callBack == "Yes") {
                        clearNotifications(token)
                    } else {
                    }
                }
            })
        twoButtonDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        twoButtonDialog.show()
    }

}