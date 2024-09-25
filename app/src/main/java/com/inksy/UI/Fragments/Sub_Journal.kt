package com.inksy.UI.Fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.inksy.Database.Entities.CategoryTable
import com.inksy.Database.Entities.PurchasedDoodles
import com.inksy.Database.JournalDatabase
import com.inksy.Interfaces.OnChangeStateClickListener
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Model.Journals
import com.inksy.Remote.Status
import com.inksy.UI.Activities.CreateActivity
import com.inksy.UI.Activities.StartingActivity
import com.inksy.UI.Activities.ViewAll
import com.inksy.UI.Adapter.BookAdapter
import com.inksy.UI.Adapter.CategoriesAdapter
import com.inksy.UI.Adapter.FirstBookAdapter
import com.inksy.UI.Constants
import com.inksy.UI.Dialogs.Comment_BottomSheet
import com.inksy.UI.ViewModel.DashboardView
import com.inksy.UI.ViewModel.JournalView
import com.inksy.Utils.Permissions
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentSubJournalBinding
import java.io.Serializable


class Sub_Journal :
    Fragment(), OnChangeStateClickListener, SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: FragmentSubJournalBinding
    lateinit var dashboardView: DashboardView
    var otherJournals: ArrayList<Journals> = ArrayList()
    var myjournals: ArrayList<Journals> = ArrayList()
    lateinit var refreshLayout: SwipeRefreshLayout
    var token: String = ""
    lateinit var tinyDB: TinyDB
    lateinit var journalDatabase: JournalDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubJournalBinding.inflate(layoutInflater)
        tinyDB = TinyDB(requireContext())
        token = tinyDB.getString("token")!!
        refreshLayout = binding.refreshListener
        dashboardView = ViewModelProvider(requireActivity())[DashboardView::class.java]
        journalDatabase = JournalDatabase.getInstance(requireContext())!!

        if (!Permissions.Check_CAMERA(requireActivity())) {
            Permissions.Request_CAMERA_STORAGE(requireActivity(), 11)
        }

        binding.tvContinue.setOnClickListener {

            journalDatabase.getJournalData().DeleteData("1")
            journalDatabase.getJournalData().deleteTable()
            journalDatabase.getJournalData().deleteSelectedAudience()
            journalDatabase.getJournalData().deletepageforlinkTable()
            requireContext().startActivity(Intent( requireContext(), CreateActivity::class.java))
        }

        binding.seeall.setOnClickListener {
            myjournals.removeAt(0)
            requireContext().startActivity(
                Intent(requireContext(), ViewAll::class.java).putExtra(
                    Constants.activity,
                    Constants.sub_journalViewAll
                ).putExtra("Data", true)
                    .putExtra("List", myjournals as Serializable)
            )
        }
        binding.seeall1.setOnClickListener {
            requireContext().startActivity(
                Intent(requireContext(), ViewAll::class.java).putExtra(
                    Constants.activity,
                    Constants.sub_journalViewAll
                ).putExtra("Data", true)
                    .putExtra("List", otherJournals as Serializable)
            )
        }



        refreshLayout.setOnRefreshListener(this)
        refreshLayout.post(Runnable {
            refreshLayout.setRefreshing(true)

            // Fetching data from server
            getData()
        })


        return binding.root
    }

    override fun onStateChange(id: Int, like: Boolean, type: String) {
        super.onStateChange(id, like, type)

        if (like) {
            likeJournal(id, like)
        } else {
            likeJournal(id, like)
        }
    }


    private fun likeJournal(id: Int?, like: Boolean) {
        val journalView: JournalView =
            ViewModelProvider(requireActivity())[JournalView::class.java]
        journalView.init()
        journalView.journalLike(
            id.toString(),
            token
        )?.observe(requireActivity()) {

            if (it?.data?.status == 1) {
                Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(requireContext(), it?.data?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getData() {
        val mytoken = "Bearer $token"

        dashboardView.getData(mytoken)?.observe(requireActivity()) { it ->

            when (it.status) {
                Status.SUCCESS -> {
                    // people = it.data?.data?.people
                    myjournals = it.data?.data?.journals!!
                    otherJournals = it.data.data?.followedJournals!!

                    journalDatabase.getJournalData().deletePurchasedDoodle()
                    journalDatabase.getJournalData().deleteCategoryTable()
                    var doodles = it?.data.data?.userDoodles

                    for (i in 0 until doodles?.size!!) {
                        val ddle = doodles[i]
                        val purchasedDoodles =
                            PurchasedDoodles(
                                i,
                                ddle.doodlePackId.toString(),
                                ddle.userId.toString(),
                                ddle.doodleImage,
                                ddle.status.toString(),
                                ddle.createdAt.toString(),
                                ddle.updatedAt.toString()
                            )
                        journalDatabase.getJournalData().insertDoodles(purchasedDoodles)
                    }
                    val categoriesList = it.data.data!!.categories
                    val list = ArrayList<String>()

                    binding.categories.adapter =
                        CategoriesAdapter(
                            requireContext(),
                            categoriesList!!,
                            otherJournals,
                            "",
                            object : iOnClickListerner {
                                override fun onclick(position: Int) {
                                    Comment_BottomSheet(position).show(
                                        childFragmentManager,
                                        " "
                                    );
                                }
                            },
                            this,
                        )
                    for (i in 0 until categoriesList.size) {

                        var categoryTable = CategoryTable(
                            categoriesList[i].id.toString(),
                            categoriesList[i].categoryName,
                            categoriesList[i].isActive,
                            categoriesList[i].createdAt,
                            categoriesList[i].updatedAt
                        )

                        journalDatabase.getJournalData().insertCategory(categoryTable)


                        val json = categoriesList[i]
                        val gson = Gson()
                        list.add(gson.toJson(json))


                    }
                    tinyDB.putListString("categoriesList", list)
                    populate()

                    refreshLayout.isRefreshing = false;
                }

                Status.ERROR -> {
                    refreshLayout.isRefreshing = false;
                    if (it.message == "Unauthenticated") {
                        tinyDB.clear()

                        requireContext().startActivity(
                            Intent(
                                requireContext(),
                                StartingActivity::class.java
                            )
                        )
                        Toast.makeText(requireContext(), "Token Expired", Toast.LENGTH_SHORT).show()
                        refreshLayout.isRefreshing = false;
                    } else {

                        Toast.makeText(requireContext(), "Some error occurred", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                Status.LOADING -> {}

            }
        }
    }


    fun populate() {
        if (myjournals.isNotEmpty() || otherJournals.isNotEmpty()) {
            myjournals = myjournals


            var newJournal = myjournals
            var indexJournal = Journals(title = "@@@@")
            newJournal.add(0, indexJournal)
            binding.myJournal.adapter = FirstBookAdapter(
                requireContext(),
                newJournal,
                " ",
                object : iOnClickListerner {
                    override fun onclick(position: Int) {
                        Comment_BottomSheet(position).show(
                            childFragmentManager,
                            " "
                        );
                    }
                },
                this, journalDatabase
            )

            binding.rvHealth.adapter = BookAdapter(
                requireContext(),
                otherJournals,
                " ",
                object : iOnClickListerner {
                    override fun onclick(position: Int) {
                        Comment_BottomSheet(position).show(
                            childFragmentManager,
                            " "
                        );
                    }
                },
                this
            )

            binding.layout.visibility = View.VISIBLE
            binding.layoutemptyChat.visibility = View.GONE

        } else {
            binding.layout.visibility = View.GONE
            binding.layoutemptyChat.visibility = View.VISIBLE
        }
    }

    override fun onRefresh() {
        refresh()
    }

    fun refresh() {
        getData()

    }

    override fun onResume() {
        refresh()
        super.onResume()
    }


}