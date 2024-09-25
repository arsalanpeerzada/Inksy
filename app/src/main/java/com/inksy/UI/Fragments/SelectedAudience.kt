package com.inksy.UI.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.inksy.Database.Entities.SelectedAudience
import com.inksy.Database.JournalDatabase
import com.inksy.Interfaces.OnChangeStateClickListener
import com.inksy.Model.UserModel
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Adapter.AudienceAdapter
import com.inksy.UI.ViewModel.PeopleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentSelectedAudienceBinding


class SelectedAudience : Fragment(), OnChangeStateClickListener {
    var token: String = ""
    lateinit var tinyDB: TinyDB
    lateinit var peopleView: PeopleView
    lateinit var binding: FragmentSelectedAudienceBinding
    var list: ArrayList<SelectedAudience> = ArrayList()
    var audienceList: ArrayList<UserModel>? = ArrayList()
    lateinit var journalDatabase: JournalDatabase
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
        binding = FragmentSelectedAudienceBinding.inflate(layoutInflater)
        journalDatabase = JournalDatabase.getInstance(requireContext())!!
        peopleView = ViewModelProvider(this)[PeopleView::class.java]
        peopleView.init()
        tinyDB = TinyDB(requireContext())
        token = tinyDB.getString("token").toString()
        binding.back.setOnClickListener() {
            findNavController().navigate(
                R.id.action_selectedAudience_to_select_Audience2
            )
        }
        var list = journalDatabase.getJournalData().getAllAudience()


        binding.button.setOnClickListener {

            var listcheck = journalDatabase.getJournalData().getAllAudience()
            var bundle = Bundle()

            if (listcheck.size > 0){
                bundle.putBoolean("selected", true)
            }else {
                bundle.putBoolean("selected", false)
            }

            findNavController().navigate(
                R.id.action_selectedAudience_to_select_Audience2, bundle
            )
        }

        getData(token, list)
        return binding.root
    }

    fun getData(token: String, savedAudienceList: List<SelectedAudience>) {
        peopleView.getfollowersFollowList(token)?.observe(requireActivity()) {
            when (it?.status) {
                Status.ERROR -> {}
                Status.SUCCESS -> {
                    audienceList = it.data?.data as ArrayList<UserModel>?
                    binding.rvAudience.adapter = AudienceAdapter(
                        requireContext(),
                        audienceList!!, savedAudienceList, this
                    )
                }
                Status.LOADING -> {}
                else -> {}
            }
        }
    }


    override fun onStateChange(id: Int, like: Boolean, type: String) {
        super.onStateChange(id, like, type)

        if (like) {
            var audience = audienceList?.get(id)!!
            var data = SelectedAudience(
                id,
                audience.id.toString(),
                audience.avatar,
                audience.bio,
                audience.is_artist.toString()
            )


            journalDatabase.getJournalData().insertAudeience(data)

        } else {
            journalDatabase.getJournalData().DeleteAudience(audienceList?.get(id).toString())
        }
    }

}