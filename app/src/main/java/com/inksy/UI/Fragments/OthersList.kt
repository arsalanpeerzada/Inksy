package com.inksy.UI.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.inksy.databinding.FragmentOthersListBinding

class OthersList : Fragment() {


    lateinit var binding: FragmentOthersListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOthersListBinding.inflate(layoutInflater)

        binding.imgAboutUs.setOnClickListener { openAboutUs() }
        binding.tvAboutus.setOnClickListener { openAboutUs() }
        binding.tvAboutDesc.setOnClickListener { openAboutUs() }

        binding.imgTerms.setOnClickListener { openterms() }
        binding.tvTerms.setOnClickListener { openterms() }
        binding.tvTermsDesc.setOnClickListener { openterms() }

        binding.imgPrivacy.setOnClickListener { openprivacy() }
        binding.tvPrivacy.setOnClickListener { openprivacy() }
        binding.tvPrivacyDesc.setOnClickListener { openprivacy() }


        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
            requireActivity().finish()
        }

        return binding.root
    }

    fun openAboutUs() {
        val action = OthersListDirections.actionOthersListToAboutUs()

        findNavController().navigate(action)
    }

    fun openterms() {
        val action = OthersListDirections.actionOthersListToTerms()

        findNavController().navigate(action)
    }

    fun openprivacy() {
        val action = OthersListDirections.actionOthersListToPrivacyPolicy()

        findNavController().navigate(action)
    }


}