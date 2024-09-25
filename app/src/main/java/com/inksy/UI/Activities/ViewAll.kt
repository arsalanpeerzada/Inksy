package com.inksy.UI.Activities

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.example.DoodlePack
import com.inksy.Interfaces.OnChangeStateClickListener
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Model.Journals
import com.inksy.Model.UserModel
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Adapter.DoodleAdapter
import com.inksy.UI.Adapter.JournalAdapter
import com.inksy.UI.Adapter.PeopleAdapter
import com.inksy.UI.Constants
import com.inksy.UI.Dialogs.Comment_BottomSheet
import com.inksy.UI.ViewModel.JournalView
import com.inksy.UI.ViewModel.PeopleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivityViewAllBinding


class ViewAll : AppCompatActivity(), iOnClickListerner, OnChangeStateClickListener  {


    lateinit var tinyDB: TinyDB
    lateinit var peopleView: PeopleView
    lateinit var binding: ActivityViewAllBinding
    var activity: String = ""
    var peoplelist = ArrayList<UserModel>()
    var journallist = ArrayList<Journals>()
    var doodleList = ArrayList<DoodlePack>()
    var token = ""
    lateinit var journalAdapter: JournalAdapter
    lateinit var peopleAdapter: PeopleAdapter
    lateinit var doodleAdapter: DoodleAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvAll.visibility = View.VISIBLE
        binding.layoutempty.visibility = View.GONE

        peopleView = ViewModelProvider(this)[PeopleView::class.java]
        peopleView.init()

        tinyDB = TinyDB(this)
        token = tinyDB.getString("token").toString()

        activity = intent.getStringExtra(Constants.activity).toString()
        var dataCheck = intent.getBooleanExtra("Data", false)
        if (activity.contains("Search")) {
            binding.text.text = getString(R.string.search_result)
            binding.search.visibility = View.GONE
        } else if (activity.contains("View")) {
            binding.text.text = getString(R.string.all)
            binding.search.visibility = View.VISIBLE
        }




        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (activity.contains("Journal")) {
                    val filteredModelList: ArrayList<Journals>? =
                        filterJournal(journallist, "" + p0)
                    if (filteredModelList!!.size > 0) {
                        journalAdapter.setFilter(filteredModelList, p0.toString())

                    } else {
                        Toast.makeText(this@ViewAll, "Not Found", Toast.LENGTH_SHORT).show()

                    }
                } else if (activity.contains("People")) {
                    val filteredModelList: ArrayList<UserModel>? =
                        filterPeople(peoplelist, "" + p0)
                    if (filteredModelList!!.size > 0) {
                        peopleAdapter.setFilter(filteredModelList, p0.toString())

                    } else {
                        Toast.makeText(this@ViewAll, "Not Found", Toast.LENGTH_SHORT).show()

                    }
                } else if (activity.contains("Doodle")) {
                    val filteredModelList: ArrayList<DoodlePack>? =
                        filterDoodle(doodleList, "" + p0)
                    if (filteredModelList!!.size > 0) {
                        doodleAdapter.setFilter(filteredModelList, p0.toString())

                    } else {
                        Toast.makeText(this@ViewAll, "Not Found", Toast.LENGTH_SHORT).show()

                    }
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                if (activity.contains("Journal")) {
                    val filteredModelList: ArrayList<Journals>? =
                        filterJournal(journallist, "" + p0)
                    if (filteredModelList!!.size > 0) {
                        journalAdapter.setFilter(filteredModelList, p0.toString())

                    } else {
                        Toast.makeText(this@ViewAll, "Not Found", Toast.LENGTH_SHORT).show()

                    }
                } else if (activity.contains("People")) {
                    val filteredModelList: ArrayList<UserModel>? =
                        filterPeople(peoplelist, "" + p0)
                    if (filteredModelList!!.size > 0) {
                        peopleAdapter.setFilter(filteredModelList, p0.toString())

                    } else {
                        Toast.makeText(this@ViewAll, "Not Found", Toast.LENGTH_SHORT).show()

                    }
                } else if (activity.contains("Doodle")) {
                    val filteredModelList: ArrayList<DoodlePack>? =
                        filterDoodle(doodleList, "" + p0)
                    if (filteredModelList!!.size > 0) {
                        doodleAdapter.setFilter(filteredModelList, p0.toString())

                    } else {
                        Toast.makeText(this@ViewAll, "Not Found", Toast.LENGTH_SHORT).show()

                    }
                }

            }
        })


        if (dataCheck) {

            if (activity.contains("Journal")) {
                journallist = intent.getSerializableExtra("List") as ArrayList<Journals>
                if (journallist.size == 0) {
                    binding.rvAll.visibility = View.GONE
                    binding.layoutempty.visibility = View.VISIBLE

                    Glide.with(this)
                        .load(ContextCompat.getDrawable(this, R.drawable.ic_empty_journal)).into(
                            binding.emptyuser
                        )
                    binding.emptytv.text = "No Journal Found"

                }
            } else if (activity.contains("People")) {
                peoplelist = intent.getSerializableExtra("List") as ArrayList<UserModel>
                if (peoplelist.size == 0) {
                    binding.rvAll.visibility = View.GONE
                    binding.layoutempty.visibility = View.VISIBLE
                }
            } else {
                doodleList = intent.getSerializableExtra("List") as ArrayList<DoodlePack>
                if (doodleList.size == 0) {
                    binding.rvAll.visibility = View.GONE
                    binding.layoutempty.visibility = View.VISIBLE

                    Glide.with(this)
                        .load(ContextCompat.getDrawable(this, R.drawable.ic_empty_doodles)).into(
                            binding.emptyuser
                        )
                    binding.emptytv.text = "No Doodle Found"
                }
            }

        } else {
        }

        if (activity.contains(Constants.sub_journalViewAll)) {

            binding.rvAll.adapter =
                JournalAdapter(this@ViewAll, journallist, Constants.people, this,this)

        } else if (activity.contains(Constants.sub_journalSearch)) {

            binding.search.visibility = View.GONE
            binding.rvAll.adapter =
                JournalAdapter(this@ViewAll, journallist, Constants.people, this,this)

        } else if (activity.contains(Constants.peopleSearch)) {

            binding.search.visibility = View.GONE
            binding.rvAll.adapter =
                PeopleAdapter(this@ViewAll, peoplelist as ArrayList<UserModel>, true, this)

        } else if (activity.contains(Constants.peopleViewAll)) {

            binding.rvAll.adapter = PeopleAdapter(this@ViewAll, peoplelist, false, this)

        } else if (activity.contains(Constants.doodleSearch)) {

            binding.search.visibility = View.GONE
            binding.rvAll.layoutManager = GridLayoutManager(this, 2)
            binding.rvAll.adapter = DoodleAdapter(this@ViewAll, doodleList, "Feature", this)

        } else if (activity.contains(Constants.doodleViewAll)) {

            binding.rvAll.layoutManager = GridLayoutManager(this, 2)
            binding.rvAll.adapter = DoodleAdapter(this@ViewAll, doodleList, "Feature", this)
        }

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@OnEditorActionListener true
            }
            false
        })


    }

    override fun onBackPressed() {
        super.onBackPressed()
        //  startActivity(Intent(this@ViewAll, MainActivity::class.java))
    }

    private fun performSearch() {
        binding.search.clearFocus()
        binding.search.text.clear()
        val input: InputMethodManager? =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input?.hideSoftInputFromWindow(binding.search.windowToken, 0)
        //...perform search
    }

    override fun onStateChange(_id: Int, choice: Boolean,  type: String) {
        super.onStateChange(_id, choice, type)

        if (type == "People") {
            if (choice) {
                var followList = peoplelist as ArrayList<UserModel>
                followUser(_id)
            } else {
                var followList = peoplelist as ArrayList<UserModel>
                unfollowUser(_id)
            }
        } else {
            if (choice) {
                likeJournal(_id, choice)
            } else {
                likeJournal(_id, choice)
            }
        }
    }

    private fun followUser(id: Int) {
        peopleView.userFollow(id, token)?.observe(this@ViewAll) { it ->
            when (it?.status) {
                Status.SUCCESS -> {
                    if (it?.data?.message.toString() == "Follow request sent successfully") {

                    }

                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(this@ViewAll, it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    private fun unfollowUser(id: Int) {
        peopleView.userUnfollow(id, token)?.observe(this@ViewAll) { it ->
            when (it?.status) {
                Status.SUCCESS -> {
                    Toast.makeText(this@ViewAll, it.data?.message.toString(), Toast.LENGTH_SHORT)
                        .show()

                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(this@ViewAll, it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    override fun onclick(position: Int) {
        super.onclick(position)
        Comment_BottomSheet(journallist[position].id!!).show(supportFragmentManager, " ");
    }

    private fun filterJournal(models: ArrayList<Journals>, query: String): ArrayList<Journals>? {
        var query = query
        query = query.lowercase()
        val filteredModelList: ArrayList<Journals> = ArrayList()
        for (model in models) {
            val text: String = model.title?.lowercase().toString()
            if (text.contains(query)) {
                filteredModelList.add(model)
            }
        }
        journalAdapter = JournalAdapter(this@ViewAll, filteredModelList, Constants.people, this,this)
        binding.rvAll.layoutManager = LinearLayoutManager(this@ViewAll)
        binding.rvAll.adapter = journalAdapter
        journalAdapter.notifyDataSetChanged()
        return filteredModelList
    }

    private fun filterPeople(models: ArrayList<UserModel>, query: String): ArrayList<UserModel>? {
        var query = query
        query = query.lowercase()
        val filteredModelList: ArrayList<UserModel> = ArrayList()
        for (model in models) {
            val text: String = model.fullName?.lowercase().toString()
            if (text.contains(query)) {
                filteredModelList.add(model)
            }
        }
        peopleAdapter = PeopleAdapter(this@ViewAll, filteredModelList, true, this)
        binding.rvAll.layoutManager = LinearLayoutManager(this@ViewAll)
        binding.rvAll.adapter = peopleAdapter
        peopleAdapter.notifyDataSetChanged()
        return filteredModelList
    }

    private fun filterDoodle(models: ArrayList<DoodlePack>, query: String): ArrayList<DoodlePack>? {
        var query = query
        query = query.lowercase()
        val filteredModelList: ArrayList<DoodlePack> = ArrayList()
        for (model in models) {
            val text: String = model.packTitle?.lowercase().toString()
            if (text.contains(query)) {
                filteredModelList.add(model)
            }
        }
        doodleAdapter = DoodleAdapter(this@ViewAll, filteredModelList, Constants.people, this)
        binding.rvAll.layoutManager = GridLayoutManager(this, 2)
        binding.rvAll.adapter = doodleAdapter
        doodleAdapter.notifyDataSetChanged()
        return filteredModelList
    }

    private fun likeJournal(id: Int?, like: Boolean) {
        val journalView: JournalView =
            ViewModelProvider(this)[JournalView::class.java]
        journalView.init()
        journalView.journalLike(
            id.toString(),
            token
        )?.observe(this) {

            if (it?.data?.status == 1) {
                Toast.makeText(this, it?.data.message, Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, it?.data?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}