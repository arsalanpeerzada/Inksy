package com.inksy.UI.Fragments

import android.content.Context
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.inksy.UI.Constants
import com.inksy.UI.ViewModel.OthersView
import com.inksy.databinding.FragmentPrivacyPolicyBinding

class PrivacyPolicy : Fragment() {

    lateinit var binding: com.inksy.databinding.FragmentPrivacyPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPrivacyPolicyBinding.inflate(layoutInflater)

        binding.back.setOnClickListener {
            val action = PrivacyPolicyDirections.actionPrivacyPolicyToOthersList()
            findNavController().navigate(action)
        }

        getData(binding)
        return binding.root
    }

    fun getData(binding: FragmentPrivacyPolicyBinding) {
        val sharedPreferences = requireContext().getSharedPreferences(
            Constants.APP_NAME,
            Context.MODE_PRIVATE
        )
        val token = sharedPreferences.getString("token", "")
        val othersView: OthersView =
            ViewModelProvider(requireActivity())[OthersView::class.java]
        othersView.init()
        othersView.privacy(
            token
        )?.observe(requireActivity()) {
            if (it?.status == 1) {
                binding.imageView13.text = it.data?.title
                binding.description.text = Html.fromHtml(it.data?.description).toString()
            } else {
                Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}