package com.inksy.UI.Fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.appcheck.FirebaseAppCheck
//import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.inksy.Interfaces.OnKeyboardVisibilityListener
import com.inksy.Model.UserDataModelFirebase
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Activities.MainActivity
import com.inksy.UI.ViewModel.LoginView
import com.inksy.UI.ViewModel.PeopleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentOtpBinding
import java.util.concurrent.TimeUnit
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory


class FragmentOtp : Fragment(), OnKeyboardVisibilityListener {

    var userEnteredCode = ""
    var systemGeneratedCode = ""
    private var mDatabase: DatabaseReference? = null

    var phoneNumer = ""
    var phoneDigits = ""

    var phoneNumerWithoutCountryCode = ""
    var countryCode = ""

    private lateinit var auth: FirebaseAuth

    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    lateinit var peopleView: PeopleView
    lateinit var tinyDB: TinyDB
    var token = ""
    lateinit var binding: FragmentOtpBinding
    var forgetPasswordData: Boolean = false
    var registerData: Boolean = false
    var numberVerify: Boolean = false
    lateinit var loginView: LoginView
    // private val args: FragmentOtpArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOtpBinding.inflate(layoutInflater)
        peopleView = ViewModelProvider(this)[PeopleView::class.java]
        peopleView.init()


        mDatabase = FirebaseDatabase.getInstance().getReference()

        tinyDB = TinyDB(requireContext())
        token = tinyDB.getString("token").toString()
        val code = arguments?.getString("code").toString()

        var mobileNumber = arguments?.getString("number")
        var register = arguments?.getBoolean("register")
        var email = arguments?.getString("email")

        forgetPasswordData = arguments?.getBoolean("forgetPassword", false)!!
        registerData = arguments?.getBoolean("RegisterData", false)!!
        numberVerify = arguments?.getBoolean("numberverify")!!
        if (numberVerify!!) {

            binding.loader.visibility = View.VISIBLE
            binding.fragmentotptitle.text =
                " ${getString(R.string.digit_code)} ${code + mobileNumber}"
        } else if (registerData) {
            binding.fragmentotptitle.text = "Enter 6 digit code we have email you at $email"
            binding.otpView.itemCount = 6
        } else if (forgetPasswordData) {
            binding.fragmentotptitle.text = "Enter 6 digit code we have email you at $email"
            binding.otpView.itemCount = 6

        }

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timer.text = getString(R.string.resend_code) + millisUntilFinished / 1000
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                binding.timer.text = getString(R.string.resend_code_)
            }
        }.start()

        binding.layMain.viewTreeObserver.addOnGlobalLayoutListener {
            val rec = Rect()
            binding.layMain.getWindowVisibleDisplayFrame(rec)
            val screenHeight = binding.layMain.rootView.height
            val keypadHeight = screenHeight - rec.bottom
            if (keypadHeight > screenHeight * 0.15) {
            } else {
                Handler().postDelayed(object : Runnable {
                    override fun run() {
                        val imm: InputMethodManager =
                            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(binding.otpView, InputMethodManager.SHOW_IMPLICIT)
                    }

                }, 100)
            }
        }

        binding.timer.setOnClickListener {

            if (binding.timer.text.toString() == "Resend Code"){
                var check = forgetPasswordData

                if (forgetPasswordData){
                    peopleView.forgotPassword(email!!, token)
                        ?.observe(requireActivity()) {
                            binding.loader.visibility = View.VISIBLE
                            when (it?.status) {
                                Status.SUCCESS -> {
                                    Toast.makeText(
                                        requireContext(),
                                        it?.data?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.loader.visibility = View.GONE
                                }
                                Status.ERROR -> {

                                    Toast.makeText(
                                        requireContext(),
                                        "Email not valid",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.loader.visibility = View.GONE
                                }
                                Status.LOADING -> {}
                                else -> {}
                            }
                        }
                }else {
                    auth = Firebase.auth
                    FirebaseApp.initializeApp(requireContext())
                    val firebaseAppCheck = FirebaseAppCheck.getInstance()
                    // check
//                    firebaseAppCheck.installAppCheckProviderFactory(
//                        SafetyNetAppCheckProviderFactory.getInstance()
//                    )
                    firebaseAppCheck.installAppCheckProviderFactory(
                        PlayIntegrityAppCheckProviderFactory.getInstance()
                    )

                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(code + mobileNumber)
                        .setTimeout(30L, TimeUnit.SECONDS)
                        .setActivity(requireActivity())
                        .setCallbacks(callbacks)
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                }

            }

            object : CountDownTimer(30000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.timer.text = getString(R.string.resend_code) + millisUntilFinished / 1000
                    //here you can have your logic to set text to edittext
                }

                override fun onFinish() {
                    binding.timer.text = getString(R.string.resend_code_)
                }
            }.start()
        }


        binding.back.setOnClickListener() {

            if (forgetPasswordData) {
                val number = arguments?.getString("number").toString()
                val code = arguments?.getString("code").toString()
                var email = arguments?.getString("email")
                var dataExist = arguments?.getInt("dataExist")

                val bundle = Bundle()
                bundle.putString("number", number)
                bundle.putString("code", code)
                bundle.putString("email", email)
                bundle.putInt("dataExist", dataExist!!)

                findNavController().navigate(R.id.action_fragmentOtp_to_login, bundle)
            } else if (registerData) {
                val number = arguments?.getString("number").toString()
                val code = arguments?.getString("code").toString()
                val bundle = Bundle()
                bundle.putString("number", number)
                bundle.putString("code", code)
                findNavController().navigate(R.id.action_fragmentOtp_to_login, bundle)
            } else {
                val number = arguments?.getString("number").toString()
                val code = arguments?.getString("code").toString()
                val bundle = Bundle()
                bundle.putString("number", number)
                bundle.putString("code", code)
                findNavController().navigate(R.id.action_fragmentOtp_to_numberVerify, bundle)
            }

        }


        binding.otpView.requestFocus()

        binding.otpView.setOtpCompletionListener() { otp ->
            binding.loader.visibility = View.VISIBLE
            if (forgetPasswordData) {
                binding.loader.visibility = View.VISIBLE
                var email = arguments?.getString("email")
                verifyEmail(email, token, binding.otpView.text.toString())


            } else if (registerData) {
                binding.loader.visibility = View.VISIBLE
                var email = arguments?.getString("email")
                var password = arguments?.getString("password")
                var phone = arguments?.getString("phone")
                var phoneCode = arguments?.getString("phoneCode")


                login(email!!, password!!, phone!!, phoneCode!!, binding.otpView.text.toString())

            } else if (numberVerify) {
                verifyPhoneNumberWithCode(systemGeneratedCode, otp)

            }
        }
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

        if (numberVerify){
            auth = Firebase.auth
            FirebaseApp.initializeApp(requireContext())
            val firebaseAppCheck = FirebaseAppCheck.getInstance()
            // check
//            firebaseAppCheck.installAppCheckProviderFactory(
//                SafetyNetAppCheckProviderFactory.getInstance()
//            )
            firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance()
            )

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(code + mobileNumber)
                .setTimeout(30L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(callbacks)
                .build()


            PhoneAuthProvider.verifyPhoneNumber(options)

        }

        return binding.root
    }

    private fun login(
        email: String,
        password: String,
        phone: String,
        phonecode: String,
        code: String
    ) {
        loginView = ViewModelProvider(requireActivity())[LoginView::class.java]
        loginView.init()
        loginView.loginRegister(
            email.lowercase(),
            password,
            phone,
            phonecode,
            code,
            tinyDB.getString("fbToken").toString()
        )?.observe(requireActivity()) {
            binding.loader.visibility = View.GONE
            if (it?.status == 1) {

                if (it?.data?.is_email_verification == 0) {
                    var email = it.data?.email

                    var tinydb = TinyDB(requireContext())

                    tinydb.putString("password", password)
                    tinydb.putString("email", email)
                    tinydb.putString("id", it.data?.id.toString())
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
                    tinydb.putInt("isprofilecompleted", it.data?.isProfileCompleted!!)
                    tinydb.putInt("isArtist", it?.data?.is_artist!!)

                    if (it.data?.isProfileCompleted == 0) {

                        findNavController().navigate(R.id.action_fragmentOtp_to_bio)
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

                    }

                    updateUserDataInFirebase(it.data?.id.toString())

                } else {

                    findNavController().navigate(R.id.action_fragmentOtp_to_bio)
                }
            } else {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun verifyEmail(email: String?, token: String, mycode: String) {
        peopleView.verifyCode(mycode, email!!, token)
            ?.observe(requireActivity()) {
                binding.loader.visibility = View.GONE
                when (it?.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        var bundle = Bundle()
                        bundle.putBoolean("forgetPassword", true)
                        bundle.putString("email", email)
                        bundle.putString("code", mycode)
                        findNavController().navigate(
                            R.id.action_fragmentOtp_to_forgetPassword2,
                            bundle
                        )
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {}
                }
            }


    }

    private fun verifyPhone(phoneCode: String, Phone: String) {
        peopleView.phoneVerify(phoneCode!!, Phone)
            ?.observe(requireActivity()) {
                binding.loader.visibility = View.GONE
                when (it?.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        var json = it?.data?.data

                        var dataExist = json?.isDataExist
                        var bundle = Bundle()
                        if (dataExist == 1) {
                            var email = json?.email
                            bundle.putString("number", Phone)
                            bundle.putString("code", phoneCode)
                            bundle.putString("email", email)
                            bundle.putInt("dataExist", dataExist)
                        } else {

                            var email = json?.email
                            bundle.putString("number", Phone)
                            bundle.putString("code", phoneCode)
                            bundle.putInt("dataExist", dataExist!!)
                        }

                        findNavController().navigate(R.id.action_fragmentOtp_to_login, bundle)
                    }
                    Status.ERROR -> {
                        binding.loader.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {}
                }
            }


    }


    private fun setKeyboardVisibilityListener(onKeyboardVisibilityListener: OnKeyboardVisibilityListener) {
        val parentView: View =
            (requireActivity().findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        parentView.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                private var alreadyOpen = false
                private val defaultKeyboardHeightDP = 100
                private val EstimatedKeyboardDP =
                    defaultKeyboardHeightDP + if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 48 else 0
                private val rect: Rect = Rect()
                override fun onGlobalLayout() {
                    val estimatedKeyboardHeight = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        EstimatedKeyboardDP.toFloat(),
                        parentView.getResources().getDisplayMetrics()
                    )
                        .toInt()
                    parentView.getWindowVisibleDisplayFrame(rect)
                    val heightDiff: Int =
                        parentView.getRootView().getHeight() - (rect.bottom - rect.top)
                    val isShown = heightDiff >= estimatedKeyboardHeight
                    if (isShown == alreadyOpen) {
                        Log.d("Keyboard state", "Ignoring global layout change...")
                        return
                    }
                    alreadyOpen = isShown
                    onKeyboardVisibilityListener.onVisibilityChanged(isShown)
                }
            })
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden === Configuration.HARDKEYBOARDHIDDEN_NO) {

        } else if (newConfig.hardKeyboardHidden === Configuration.HARDKEYBOARDHIDDEN_YES) {
        }
    }

    override fun onVisibilityChanged(visible: Boolean) {
        if (!visible) {
            val imm: InputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.otpView, InputMethodManager.SHOW_IMPLICIT)
        }

    }

    override fun onResume() {
        super.onResume()
        binding.otpView.requestFocus()


        val imm: InputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.otpView, InputMethodManager.SHOW_IMPLICIT)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)


    }

    private var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                binding.loader.visibility = View.GONE
                Log.d(TAG, "onVerificationCompleted: $credential")
                val code = credential.smsCode

                if (code != null) {
                    binding.otpView.setText(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                binding.loader.visibility = View.GONE
                Log.d(TAG, "onVerificationFailed $e")
                if (e is FirebaseAuthInvalidCredentialsException) {

                } else if (e is FirebaseTooManyRequestsException) {
                    Log.i("++--++", "++--++")
                    e.printStackTrace()

                } else {
//
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                binding.loader.visibility = View.GONE
                try {
                    Log.d(TAG, "onCodeSent: $verificationId")
                    systemGeneratedCode = verificationId
                    resendToken = token

                    // countdownTimer()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        try {
            val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
            signInWithPhoneAuthCredential(credential);

        } catch (e: Exception) {
            binding.otpView.setText("")
            binding.loader.visibility = View.GONE

            e.printStackTrace()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {

                Log.d(TAG, "signInWithCredential:success")

                val user = task.result?.user


                val number = arguments?.getString("number").toString()
                val code = arguments?.getString("code").toString()

//                val bundle = Bundle()
//                bundle.putString("number", number)
//                bundle.putString("code", code)
                verifyPhone(code, number)

                // findNavController().navigate(R.id.action_fragmentOtp_to_login, bundle)

            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    binding.loader.visibility = View.GONE
                }
            }
        }.addOnFailureListener(requireActivity()) { task ->
            binding.otpView.text!!.clear()
            binding.loader.visibility = View.GONE
        }
    }

    private fun updateUserDataInFirebase(userId: String){
        val chatMessageModel = UserDataModelFirebase()
//        chatMessageModel.isActive = true
        chatMessageModel.isOnChat = false

        mDatabase?.child("users")?.child(userId)?.setValue(chatMessageModel)
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