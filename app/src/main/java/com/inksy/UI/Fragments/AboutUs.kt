package com.inksy.UI.Fragments

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.inksy.UI.Constants
import com.inksy.UI.ViewModel.OthersView
import com.inksy.databinding.FragmentAboutUsBinding
import com.inksy.databinding.FragmentTermsBinding


class AboutUs : Fragment() {

    lateinit var binding: FragmentAboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAboutUsBinding.inflate(layoutInflater)

        binding.back.setOnClickListener {
            val action = AboutUsDirections.actionAboutUsToOthersList()
            findNavController().navigate(action)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {
                    val action = AboutUsDirections.actionAboutUsToOthersList()
                    findNavController().navigate(action)
                }
            })
        getData(binding)

        return binding.root
    }

    fun getData(binding: FragmentAboutUsBinding) {
        val sharedPreferences = requireContext().getSharedPreferences(
            Constants.APP_NAME,
            Context.MODE_PRIVATE
        )

        val token = sharedPreferences.getString("token", "")


        val othersView: OthersView =
            ViewModelProvider(requireActivity())[OthersView::class.java]
        othersView.init()
        othersView.about(
            token
        )?.observe(requireActivity()) {

            if (it?.status == 1) {


                binding.textView16.text = it.data?.title
                binding.description.text = Html.fromHtml(it.data?.description).toString()

            } else {
                Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}