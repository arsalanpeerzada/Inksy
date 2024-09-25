package com.inksy.UI.Activities

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.inksy.Interfaces.OnChangeStateClickListener
import com.inksy.Interfaces.OnDialogClickListener
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Model.UserModel
import com.inksy.Remote.Status
import com.inksy.UI.Adapter.UsersListAdapter
import com.inksy.UI.Dialogs.TwoButtonDialog
import com.inksy.UI.ViewModel.PeopleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivityListBinding
import java.io.Serializable

class List<T> : AppCompatActivity(), iOnClickListerner, SwipeRefreshLayout.OnRefreshListener,
    OnChangeStateClickListener {

    var followRequest = false
    lateinit var peopleView: PeopleView
    lateinit var binding: ActivityListBinding
    var token = " "
    var list: ArrayList<UserModel> = ArrayList()
    lateinit var adapter: UsersListAdapter
    lateinit var refreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        followRequest = intent.getBooleanExtra("followRequests", false)

        if (followRequest) {
            binding.title.text = "Follow Requests"

        } else {
            binding.title.text = "Blocked Users"
        }

        peopleView = ViewModelProvider(this)[PeopleView::class.java]
        peopleView.init()

        token = TinyDB(this).getString("token").toString()

        refreshLayout = binding.swipe

        refreshLayout.setOnRefreshListener(this)
        refreshLayout.post(Runnable {
            refreshLayout.setRefreshing(true)

            // Fetching data from server
            if (followRequest) {
                getDataFollow(token)
            } else {
                getDataBlock(token)
            }

        })
    }

    private fun getDataBlock(_token: String) {
        peopleView.blockList(_token)?.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.ERROR -> {
                    refreshLayout.isRefreshing = false;
                    binding.layoutblockeduser.visibility = View.VISIBLE
                    binding.rvItemList.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    refreshLayout.isRefreshing = false;
                    list = it?.data?.data as ArrayList<UserModel>
                    if (list.size > 0) {
                        adapter = UsersListAdapter(this, list, this, followRequest, this)
                        binding.rvItemList.adapter = adapter

                        binding.layoutblockeduser.visibility = View.GONE
                        binding.rvItemList.visibility = View.VISIBLE
                    } else {
                        binding.layoutblockeduser.visibility = View.VISIBLE
                        binding.rvItemList.visibility = View.GONE

                    }
                }
            }
        }
    }

    private fun getDataFollow(_token: String) {
        peopleView.followRequests(_token)?.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.ERROR -> {
                    binding.layoutblockeduser.visibility = View.VISIBLE
                    binding.rvItemList.visibility = View.GONE
                    binding.emptyText.text = "No Request Found"
                    refreshLayout.isRefreshing = false;
                }
                Status.SUCCESS -> {
                    refreshLayout.isRefreshing = false;
                    list = it?.data?.data as ArrayList<UserModel>
                    if (list.size > 0) {
                        adapter = UsersListAdapter(this, list, this, followRequest, this)
                        binding.rvItemList.adapter = adapter

                        binding.layoutblockeduser.visibility = View.GONE
                        binding.rvItemList.visibility = View.VISIBLE
                    } else {
                        binding.layoutblockeduser.visibility = View.VISIBLE
                        binding.rvItemList.visibility = View.GONE

                        binding.emptyText.text = "No Request Found"
                    }
                }
            }
        }
    }


    private fun acceptRequest(
        userId: Int?,
        acceptChoice: Int?,
        token: String?
    ) {
        peopleView.userRequest(userId!!, acceptChoice!!, token)?.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.ERROR -> {
                    refreshLayout.isRefreshing = false;
                }
                Status.SUCCESS -> {
                    refreshLayout.isRefreshing = false;

                    Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT).show()
                    refresh()
                }
            }
        }
    }

    override fun onStateChange(_id: Int, accept: Boolean, type: String) {
        super.onStateChange(_id, accept, type)

        if (accept) {
            acceptRequest(_id, 1, token)
        } else {
            acceptRequest(_id, 0, token)
        }

    }

    override fun onclick(position: Int) {
        super.onclick(position)
        if (followRequest) {
            var data = list[position]
            startActivity(
                Intent(this, People::class.java).putExtra(
                    "Data",
                    data as Serializable
                )
            )
        } else {
            openDialog(position)
        }
    }

    private fun openDialog(position: Int) {
        val twoButtonDialog: TwoButtonDialog = TwoButtonDialog(
            this, "UnBlock User",
            "Are you sure, You want to unblock this user?",
            getString(R.string.yes),
            getString(R.string.no),
            object : OnDialogClickListener {
                override fun onDialogClick(callBack: String?) {
                    if (callBack == "Yes") {
                        unblockUser(list[position].id!!, token, position)
                    } else {

                    }
                }
            })
        twoButtonDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        twoButtonDialog.show()
    }

    fun unblockUser(id: Int, token: String, position: Int) {
        peopleView.userUnblock(id, token)?.observe(this) { it ->
            when (it.status) {
                Status.ERROR -> {
                }
                Status.SUCCESS -> {
                    Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT).show()
                    refresh()
                }
                Status.LOADING -> {
                }
            }
        }
    }

    override fun onRefresh() {
        refresh()
    }

    fun refresh() {
        if (followRequest) {
            getDataFollow(token)
        } else {
            getDataBlock(token)
        }

    }
}