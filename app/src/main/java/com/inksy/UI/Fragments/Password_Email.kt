package com.inksy.UI.Fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.ViewModel.PeopleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentPasswordEmailBinding


class Password_Email : Fragment() {
    var mobileNumber: String = ""

    lateinit var peopleView: PeopleView
    lateinit var tinyDB: TinyDB
    var token = ""

    var code : String = ""
    lateinit var binding: FragmentPasswordEmailBinding
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
        binding = FragmentPasswordEmailBinding.inflate(layoutInflater)
        peopleView = ViewModelProvider(this)[PeopleView::class.java]
        peopleView.init()

        mobileNumber = arguments?.getString("number")!!

        code = arguments?.getString("code")!!//args.mobileNumber
        var dataExist = arguments?.getInt("dataExist")

        if (dataExist == 1) {
            var email = arguments?.getString("email")
            if (email.isValidEmail()) {

                binding.email.setText(email!!.toString())
                binding.edtName.isEnabled = false
                requestVerify()
            }
        }

        tinyDB = TinyDB(requireContext())
        token = tinyDB.getString("token").toString()

        binding.ivBack.visibility = View.GONE
        binding.ivBack.setOnClickListener {
            var bundle = Bundle()
            var email = arguments?.getString("email")
            bundle.putString("number", mobileNumber)
            bundle.putString("code", code)
            bundle.putString("email", email)
            bundle.putInt("dataExist", dataExist!!)
            findNavController().navigate(R.id.action_password_Email_to_login, bundle)
        }
        binding.button.setOnClickListener {
            requestVerify()
        }
        return binding.root
    }

    private fun requestVerify() {
        if (binding.email.text.isNullOrEmpty()) {
            binding.email.error = "Email cannot be empty"
        } else if (!binding.email.text.isValidEmail()) {
            binding.email.error = "Email Address is not valid"
        } else {
            binding.loader.visibility = View.VISIBLE
            peopleView.forgotPassword(binding.email.text.toString(), token)
                ?.observe(requireActivity()) {
                    binding.loader.visibility = View.VISIBLE
                    when (it?.status) {
                        Status.SUCCESS -> {
                            Toast.makeText(
                                requireContext(),
                                it?.data?.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            var bundle = Bundle()
                            bundle.putBoolean("forgetPassword", true)
                            bundle.putString("number", mobileNumber)
                            bundle.putString("code", code)
                            bundle.putString("email", binding.email.text.toString())
                            bundle.putInt("dataExist", 1)
                            findNavController().navigate(
                                R.id.action_password_Email_to_fragmentOtp,
                                bundle
                            )
                        }
                        Status.ERROR -> {

                            Toast.makeText(
                                requireContext(),
                                "Email not valid",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        Status.LOADING -> {}
                        else -> {}
                    }
                }

        }
    }

    private fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()


}