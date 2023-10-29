package com.inksy.UI.Activities

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.inksy.Model.ChatDataModel
import com.inksy.Model.MyChatsModel2
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.ViewModel.ChatView
import com.inksy.UI.ViewModel.PaymentView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivityPaymentBinding
import java.util.ArrayList

class PaymentActivity : AppCompatActivity() {

    lateinit var binding: ActivityPaymentBinding

    lateinit var paymentView: PaymentView
    lateinit var tinyDB: TinyDB

    var paymentMethod: String? = "paypal"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentView = ViewModelProvider(this)[PaymentView::class.java]
        paymentView.init()

        tinyDB = TinyDB(this)

        if(tinyDB.getString("paymentEmail") != null &&
            !tinyDB.getString("paymentEmail").equals("")){
            binding.etEmailAddress.setText(tinyDB.getString("paymentEmail"))
        }

        if(tinyDB.getString("paymentMethod") != null &&
            !tinyDB.getString("paymentMethod").equals("")){
            paymentMethod = tinyDB.getString("paymentMethod")

            if(paymentMethod.equals("venmo")){
                binding.paypal.setImageDrawable(resources.getDrawable(R.drawable.paypal))
                binding.venmo.setImageDrawable(resources.getDrawable(R.drawable.venmo_tap))

            } else {
                binding.paypal.setImageDrawable(resources.getDrawable(R.drawable.paypal_tap))
                binding.venmo.setImageDrawable(resources.getDrawable(R.drawable.venmo))
            }
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
            this.finish()
        }

        binding.paypal.setOnClickListener {

            paymentMethod = "paypal"
            Log.d("--->", "PayPal Clicked")
            binding.paypal.setImageDrawable(resources.getDrawable(R.drawable.paypal_tap))
            binding.venmo.setImageDrawable(resources.getDrawable(R.drawable.venmo))
        }

        binding.venmo.setOnClickListener {

            paymentMethod = "venmo"
            Log.d("-->", "Vemno Clicked")
            binding.paypal.setImageDrawable(resources.getDrawable(R.drawable.paypal))
            binding.venmo.setImageDrawable(resources.getDrawable(R.drawable.venmo_tap))
        }

        binding.button.setOnClickListener() {
            if (binding.etEmailAddress.text.isNullOrEmpty()) {
                binding.etEmailAddress.error = "Email cannot be empty"

            } else if (!binding.etEmailAddress.text.toString().isValidEmail()) {
                binding.etEmailAddress.error = "Email Address is not valid"

            } else {
                savePaymentMethod(
                    binding.etEmailAddress.text.toString(),
                    paymentMethod!!,
                    TinyDB(this).getString("token").toString())
            }
        }

    }

    private fun savePaymentMethod(email: String, method: String, _token: String) {
        paymentView.paymentMethod(email, method, _token)?.observe(this) {
            when (it.status) {
                Status.LOADING -> {}
                Status.ERROR -> {}
                Status.SUCCESS -> {
                    tinyDB.putString("paymentEmail", email)
                    tinyDB.putString("paymentMethod", method)
                    Toast.makeText(applicationContext, "Email saved successfully", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                    this.finish()
                }
            }
        }
    }

    private fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

}