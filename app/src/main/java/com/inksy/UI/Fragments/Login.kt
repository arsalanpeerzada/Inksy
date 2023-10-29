package com.inksy.UI.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.inksy.Interfaces.OnDialogClickListener
import com.inksy.Model.UserDataModelFirebase
import com.inksy.R
import com.inksy.UI.Activities.DoodleStore
import com.inksy.UI.Activities.MainActivity
import com.inksy.UI.Activities.StartingActivity
import com.inksy.UI.Dialogs.TwoButtonDialog
import com.inksy.UI.ViewModel.LoginView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentLoginBinding


class Login : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var loginView: LoginView
    var mobile = ""
    var mycode = ""
    lateinit var tinyDB: TinyDB
    private var mDatabase: DatabaseReference? = null

    // private val args: FragmentOtpArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        val mobileNumber = arguments?.getString("number")

        tinyDB = TinyDB(requireContext())

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            try {
                if (it.isComplete) {
                    if (tinyDB.getString("fbToken").isNullOrEmpty())
                        tinyDB.putString("fbToken", it.result.toString())
                    Log.d("FirebaseToken", tinyDB.getString("fbToken").toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val code = arguments?.getString("code")//args.mobileNumber
        mobile = mobileNumber!!
        mycode = code!!
        var dataExist = arguments?.getInt("dataExist")

        if (dataExist == 1) {
            var email = arguments?.getString("email")
            if (email.isValidEmail()) {

                binding.edtName1.setText(email!!.toString())
                binding.edtName.isEnabled = false
                binding.toastShow.visibility = View.VISIBLE
            }
        } else {
            binding.forgetPassword.visibility = View.GONE
            binding.toastShow.visibility = View.GONE

            val twoButtonDialog: TwoButtonDialog = TwoButtonDialog(
                requireContext(), "Age Verification",
                "Confirm Your Age: Are you 13 or older?",
                getString(android.R.string.yes),
                getString(android.R.string.no),
                object : OnDialogClickListener {
                    override fun onDialogClick(callBack: String?) {
                        if (callBack == "Yes") {
                            Toast.makeText(
                                context,
                                "Please enter your valid email and password to continue",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            Toast.makeText(
                                context,
                                "You are not eligible to use this app",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_login_to_numberVerify)
                        }
                    }
                })
            twoButtonDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            twoButtonDialog.show()
        }

        binding.toastShow.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "This number is used for an existing profile. Please log in to your existing account.",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.button.setOnClickListener() {


            if (binding.edtName1.text.isNullOrEmpty()) {
                binding.emailError.visibility = View.VISIBLE
            }
            if (binding.password1.text.isNullOrEmpty()) {
                binding.passwordError.visibility = View.VISIBLE
                binding.emailError.text = getString(R.string.emailError)
            }

            if (!binding.edtName1.text.isNullOrEmpty() && !binding.password1.text.isNullOrEmpty()) {

                if (binding.edtName1.text.isValidEmail()) {
                    binding.button.isEnabled = false
                    login(mobileNumber, code)

                } else {
                    binding.emailError.visibility = View.VISIBLE
                    binding.emailError.text = getString(R.string.emailError2)
                }
            }
        }

        binding.forgetPassword.setOnClickListener {
            var email = arguments?.getString("email")
            var bundle = Bundle()
            bundle.putString("number", mobileNumber)
            bundle.putString("code", code)
            bundle.putString("email", email)
            bundle.putInt("dataExist", dataExist!!)
            findNavController().navigate(R.id.action_login_to_password_Email, bundle)
        }
        return binding.root
    }


    private fun login(mobileNumber: String?, mobileCode: String?) {
        binding.spinKit.visibility = View.VISIBLE
        loginView = ViewModelProvider(requireActivity())[LoginView::class.java]
        loginView.init()
        loginView.login(
            binding.edtName1.text.toString().lowercase(),
            binding.password1.text.toString(),
            mobileNumber!!,
            mobileCode!!,
            tinyDB.getString("fbToken").toString()
        )?.observe(requireActivity()) {
            binding.spinKit.visibility = View.GONE
            binding.button.isEnabled = true
            if (it?.status == 1) {

                if (it?.data?.is_email_verification == 0) {
                    var email = it.data?.email
                    var password = binding.password1.text.toString()

                    var tinydb = TinyDB(requireContext())

                    tinydb.putString("password", password)
                    tinydb.putString("email", email)
                    tinydb.putString("id", it.data?.id.toString())
                    tinydb.putString("ownprofileid", it.data?.id.toString())
                    tinydb.putString("token", it.data?.token)
                    tinydb.putString("email", it.data?.email)
                    tinydb.putString("phone", it.data?.phone)
                    tinydb.putString("followers", it.data?.followerCount.toString())
                    tinydb.putString("following", it.data?.followingCount.toString())
                    tinydb.putString("points", it.data?.points.toString())
                    tinydb.putString("phonecode", it.data?.phoneCode.toString())
                    tinydb.putString(
                        "isprivate",
                        it.data?.isPrivateProfile!!.toString()
                    )
                    tinyDB.putBoolean("isfirstTime", true)
                    tinydb.putInt("isprofilecompleted", it.data?.isProfileCompleted!!)
                    tinydb.putInt("isArtist", it?.data?.is_artist!!)

                    if (it?.data?.payment_email != null) {
                        tinydb.putString("paymentEmail", it?.data?.payment_email)
                    }
                    if (it?.data?.payment_method != null) {
                        tinydb.putString("paymentMethod", it?.data?.payment_method)
                    }

                    if (it.data?.isProfileCompleted == 0) {

                        var action: NavDirections = LoginDirections.actionLoginToBio()
                        findNavController().navigate(action)
                    } else {
                        tinydb.putString("fullname", it.data?.fullName)
                        tinydb.putString("bio", it?.data?.bio)


                        if (it?.data?.avatar.isNullOrBlank()) {

                        } else {
                            tinydb.putString("avatar", it?.data?.avatar)
                        }

                        requireContext().startActivity(
                            Intent(
                                requireContext(),
                                MainActivity::class.java
                            )
                        )
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    createUserInFirebaseDB(it.data?.id.toString())

                } else {
                    var bundle = Bundle()
                    bundle.putString("email", binding.edtName1.text.toString())
                    bundle.putString("password", binding.password1.text.toString())
                    bundle.putString("phone", mobile)
                    bundle.putString("phoneCode", mycode)
                    bundle.putBoolean("RegisterData", true)
                    findNavController().navigate(R.id.action_login_to_fragmentOtp, bundle)
                }
            } else {
                Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()

                if (it?.message.toString() == "Your account is not activated."
                    || it?.message.toString() == "Sorry! Phone number is not associated with this email address."
                ) {
                    var action: NavDirections =
                        LoginDirections.actionLoginToNumberVerify()
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun getFirebaseDBInstance(): DatabaseReference? {
        mDatabase = FirebaseDatabase.getInstance().reference
        return mDatabase
    }

    private fun createUserInFirebaseDB(userID: String) {
        val chatMessageModel = UserDataModelFirebase()
//        chatMessageModel.isActive = true
        chatMessageModel.isOnChat = false

        getFirebaseDBInstance()?.child("users")?.child(userID)?.setValue(chatMessageModel)
            ?.addOnSuccessListener(OnSuccessListener<Void?> {
                Log.d("FirebaseListener", "LoginSuccess")
            })
            ?.addOnFailureListener(OnFailureListener { e ->
                Log.d("FirebaseListener", "LoginFailure")
                Toast.makeText(
                    requireContext(),
                    "" + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            })
    }

}