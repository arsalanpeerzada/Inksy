package com.inksy.UI.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.inksy.Database.JournalDatabase
import com.inksy.R
import com.inksy.databinding.FragmentSelectAudienceBinding
import com.inksy.databinding.TablayoutBinding

class Select_Audience : Fragment() {

    lateinit var binding: FragmentSelectAudienceBinding
    lateinit var journalDatabase: JournalDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelectAudienceBinding.inflate(layoutInflater)
        journalDatabase = JournalDatabase.getInstance(requireContext())!!
        var v: TablayoutBinding = binding.include6

//        var tab1: TextView = v.findViewById(R.id.tab1)

        v.tab1.text = resources.getString(R.string.privacy)

        var data = arguments?.getBoolean("selected")

        if (data != null) {
            if (data == true) {
                binding.optionSpecificPeople.isChecked = true
            } else {
                binding.optionSpecificPeople.isChecked = false
            }
        }


        binding.button.setOnClickListener {
            findNavController().navigate(
                R.id.action_select_Audience_to_CreateJournalCoverInfo
            )
        }
        binding.back.setOnClickListener {
            findNavController().navigate(
                R.id.action_select_Audience_to_CreateJournalCoverInfo
            )
        }

        binding.optionSpecificPeople.setOnClickListener {
            findNavController().navigate(
                R.id.action_select_Audience_to_selectedAudience2
            )
            journalDatabase.getJournalData().updateprivacy("5", "1")
        }
        binding.people.setOnClickListener {
            findNavController().navigate(
                R.id.action_select_Audience_to_selectedAudience2
            )

            journalDatabase.getJournalData().updateprivacy("5", "1")
        }

        binding.peopleIfollow.setOnClickListener {
            if (binding.cbpeopleIfollow.isChecked == false) {
                binding.cbpeopleIfollow.isChecked = true
                binding.cbeveryone.isChecked = false
                binding.cbPeopleFollowMe.isChecked = false

                journalDatabase.getJournalData().updateprivacy("4", "1")

            } else {
                binding.cbpeopleIfollow.isChecked = false
                journalDatabase.getJournalData().updateprivacy("2", "1")
            }
        }
        binding.peoplewhofollowme.setOnClickListener {
            if (binding.cbPeopleFollowMe.isChecked == false) {
                binding.cbPeopleFollowMe.isChecked = true
                binding.cbeveryone.isChecked = false
                binding.cbpeopleIfollow.isChecked = false
                journalDatabase.getJournalData().updateprivacy("3", "1")
            } else {
                binding.cbPeopleFollowMe.isChecked = false
                journalDatabase.getJournalData().updateprivacy("2", "1")
            }
        }

        binding.everyone.setOnClickListener {
            if (binding.cbeveryone.isChecked == false) {
                binding.cbeveryone.isChecked = true
                binding.cbPeopleFollowMe.isChecked = false
                binding.cbpeopleIfollow.isChecked = false
                journalDatabase.getJournalData().updateprivacy("2", "1")
            } else {
                binding.cbeveryone.isChecked = false
            }
        }





        return binding.root
    }


}