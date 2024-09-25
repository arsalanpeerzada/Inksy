package com.inksy.UI.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Activities.StartingActivity
import com.inksy.UI.ViewModel.PeopleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentForgetPasswordBinding

class ForgetPassword : Fragment() {
    lateinit var peopleView: PeopleView
    lateinit var tinyDB: TinyDB
    var token = ""
    lateinit var binding: FragmentForgetPasswordBinding
    var forgetPasswordData = false
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
        binding = FragmentForgetPasswordBinding.inflate(layoutInflater)
        peopleView = ViewModelProvider(this)[PeopleView::class.java]
        peopleView.init()

        tinyDB = TinyDB(requireContext())
        token = tinyDB.getString("token").toString()

        forgetPasswordData = arguments?.getBoolean("forgetPassword")!!

        binding.layGetStarted.setOnClickListener {
            if (binding.password1.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Password cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.cpassword1.text.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Confirm password cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.cpassword1.text.toString() != binding.password1.text.toString()) {
                Toast.makeText(requireContext(), "Passwords doesnt match", Toast.LENGTH_SHORT)
                    .show()
            } else if (binding.cpassword.size > 8) {
                Toast.makeText(
                    requireContext(),
                    "Passwords must be greater than 8 characters",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (forgetPasswordData) {
                    binding.loader.visibility = View.VISIBLE
                    var email = arguments?.getString("email")
                    var code = arguments?.getString("code")
                    forgetpassword(email, token, code!!, binding.cpassword1.text.toString())
                }
            }
        }
        return binding.root
    }

    private fun forgetpassword(email: String?, token: String, mycode: String, password: String) {
        peopleView.resetPassword(password, mycode, email!!, token)
            ?.observe(requireActivity()) {
                binding.loader.visibility = View.GONE
                when (it?.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(
                            requireContext(),
                            it?.data?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_forgetPassword2_to_numberVerify)
                    }
                    Status.ERROR -> {
                        tinyDB.clear()

                        requireContext().startActivity(
                            Intent(
                                requireContext(),
                                StartingActivity::class.java
                            )
                        )
                        Toast.makeText(
                            requireContext(),
                            "Token Expired",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Status.LOADING -> {}
                    else -> {}
                }
            }


    }


}