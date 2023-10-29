package com.inksy.UI.Fragments

import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hbb20.CountryCodePicker
import com.inksy.Interfaces.OnKeyboardVisibilityListener
import com.inksy.R
import com.inksy.databinding.FragmentNumberVerifyBinding


class NumberVerify : Fragment(), OnKeyboardVisibilityListener {

    lateinit var binding: FragmentNumberVerifyBinding
    lateinit var ccp: CountryCodePicker

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNumberVerifyBinding.inflate(layoutInflater)
        ccp = binding.ccp

        binding.ccp.registerCarrierNumberEditText(binding.number);
        binding.ccp.setNumberAutoFormattingEnabled(true)
        binding.nextbutton.setOnClickListener {
//
            binding.loader.visibility = View.VISIBLE
            if (!binding.number.text.isNullOrEmpty()) {
                if (binding.number.length() in 9..13) {

                    val bundle = Bundle()
                    var number = binding.number.text.toString()
                    //  val re = Regex("[^A-Za-z0-9  ]")
                    number = number.filter { it.isLetterOrDigit() }
                    bundle.putBoolean("numberverify",true)
                    bundle.putString("number", number)
                    bundle.putString("code", binding.ccp.selectedCountryCodeWithPlus.toString())
                    binding.loader.visibility = View.GONE
                    findNavController().navigate(R.id.action_numberVerify_to_fragmentOtp, bundle)

                    binding.number.clearFocus()

                } else {
                    binding.loader.visibility = View.GONE
                    binding.number.text.clear()
                    binding.number.textSize = 11f
                    binding.number.setHintTextColor(resources.getColor(R.color.errorRed))
                    binding.number.hint = getString(R.string.NumberValidError)

                }
            } else {
                binding.loader.visibility = View.GONE
                binding.number.textSize = 11f
                binding.number.setHintTextColor(resources.getColor(R.color.errorRed))
                binding.number.hint = getString(R.string.EmptryNumberError)

            }
        }



        binding.layMain.viewTreeObserver.addOnGlobalLayoutListener {
            val rec = Rect()
            binding.layMain.getWindowVisibleDisplayFrame(rec)
            val screenHeight = binding.layMain.rootView.height
            val keypadHeight = screenHeight - rec.bottom
            if (keypadHeight > screenHeight * 0.15) {
            } else {
                Handler().postDelayed({
                    val imm: InputMethodManager =
                        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(binding.number, InputMethodManager.SHOW_IMPLICIT)
                }, 100)
            }
        }

        binding.number.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.number.textSize = 16f
                binding.number.hint = null
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        return binding.root
    }

    //
    override fun onVisibilityChanged(visible: Boolean) {
        if (!visible) {
            val imm: InputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.number, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.number.requestFocus()
        val imm: InputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.number, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden === Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(requireContext(), "keyboard visible", Toast.LENGTH_SHORT).show()
        } else if (newConfig.hardKeyboardHidden === Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(requireContext(), "keyboard hidden", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setKeyboardVisibilityListener(onKeyboardVisibilityListener: OnKeyboardVisibilityListener) {
        val parentView: View =
            (activity?.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
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
                        parentView.rootView.height - (rect.bottom - rect.top)
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


//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            NumberVerify().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}