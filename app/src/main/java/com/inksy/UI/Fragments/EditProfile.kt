package com.inksy.UI.Fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.inksy.R
import com.inksy.UI.ViewModel.EditProfileView
import com.inksy.Utils.FileUtil
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentEditProfileBinding
import com.inksy.databinding.TablayoutBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody


class EditProfile : Fragment() {
    lateinit var editProfileView: EditProfileView
    lateinit var binding: FragmentEditProfileBinding
    lateinit var tinyDB: TinyDB
    var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(layoutInflater)


        tinyDB = TinyDB(requireContext())


        editProfileView = ViewModelProvider(requireActivity())[EditProfileView::class.java]
        editProfileView.init()
        binding.name1.setText(tinyDB.getString("fullname"))
        binding.summary.setText(tinyDB.getString("bio"))
        token = tinyDB.getString("token").toString()

        var isprivate = tinyDB.getString("isprivate")

        if (isprivate == "1") {
            binding.privacyChange.isChecked = true
        }

        binding.email.text = tinyDB.getString("email")
        binding.phone.text = tinyDB.getString("phonecode") + tinyDB.getString("phone")


        var v: TablayoutBinding = binding.include

        //   var tab1: TextView = v.findViewById(R.id.tab1)

        v.tab1.text = getString(R.string.privacy)


        var v1: TablayoutBinding = binding.include2

//        var tab2: TextView = v1.findViewById(R.id.tab1)

        v1.tab1.text = getString(R.string.change_password)


        binding.privacyChange.setOnCheckedChangeListener { buttonView, isChecked ->

            privacyChange()
        }

        binding.back.setOnClickListener {
            val action = EditProfileDirections.actionEditProfileToProfile()
            findNavController().navigate(action)
        }
        binding.saveprofile.setOnClickListener() {
            if (binding.name1.text.isNullOrEmpty()) {
                binding.nameError.visibility = View.VISIBLE
            }
            if (binding.summary.text.isNullOrEmpty()) {
                binding.summary.error = getString(R.string.bioError)
            }

            if (!binding.name1.text.isNullOrEmpty() && !binding.summary.text.isNullOrEmpty()) {

//                val action = EditProfileDirections.actionEditProfileToProfile()
//                findNavController().navigate(action)
                binding.spinKit.visibility = View.VISIBLE


                editProfileView.profile(
                    binding.name1.text.toString(),
                    binding.summary.text.toString(),
                    token
                )?.observe(requireActivity()) {
                    binding.spinKit.visibility = View.GONE

                    if (it?.status == 1) {
                        tinyDB.putString("fullname", it.data?.fullName)
                        tinyDB.putString("bio", it.data?.bio)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
        binding.savepassword.setOnClickListener() {

            if (binding.password1.text.isNullOrEmpty()) {
                binding.emailError.visibility = View.VISIBLE
            } else {
                binding.emailError.visibility = View.GONE
            }
            if (binding.confirmpassword1.text.isNullOrEmpty()) {
                binding.passwordError.visibility = View.VISIBLE
                binding.emailError.text = getString(R.string.password_cannot_be_empty)
            } else {
                binding.passwordError.visibility = View.GONE
            }

            if (!binding.confirmpassword1.text.isNullOrEmpty() && !binding.password1.text.isNullOrEmpty()) {

                if (binding.confirmpassword1.text!!.contentEquals(binding.password1.text)) {
                    binding.spinKit.visibility = View.VISIBLE

                    val password = tinyDB.getString("password")
                    val token = tinyDB.getString("token")
                    val email = tinyDB.getString("email")



                    editProfileView.changepassword(
                        binding.confirmpassword1.text.toString(),
                        binding.password1.text.toString(),
                        token,
                        password,
                        email,
                    )?.observe(requireActivity()) {
                        binding.spinKit.visibility = View.GONE
                        if (it?.status == 1) {
                            val action = EditProfileDirections.actionEditProfileToProfile()
                            findNavController().navigate(action)

                            tinyDB.putString("password", password)
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    binding.passwordError.visibility = View.VISIBLE
                    binding.passwordError.text = getString(R.string.confimpassworderror)

                }
            }
        }

        return binding.root
    }

    private fun privacyChange() {
        binding.spinKit.visibility = View.VISIBLE
        editProfileView.changePrivacy(
            token
        )?.observe(requireActivity()) {
            binding.spinKit.visibility = View.GONE
            if (it?.status == 1) {

                tinyDB.putString("isprivate", it.data?.isPrivateProfile!!.toString())
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}