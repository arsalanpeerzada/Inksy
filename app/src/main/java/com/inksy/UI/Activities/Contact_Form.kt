package com.inksy.UI.Activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.inksy.R
import com.inksy.UI.ViewModel.OthersView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivityContactFormBinding
import com.inksy.databinding.Tablayout1Binding

class Contact_Form : AppCompatActivity() {
    lateinit var binding: ActivityContactFormBinding
    lateinit var otherView: OthersView
    var tabselected = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var tinydb = TinyDB(this)
        var token = tinydb.getString("token").toString()
        otherView = ViewModelProvider(this)[OthersView::class.java]
        otherView.init()


        var v: Tablayout1Binding = binding.include5

//        var tab1: TextView = v.findViewById(R.id.tab1)
//        var tab2: TextView = v.findViewById(R.id.tab2)

        v.tab1.text = getString(R.string.contactus)
        v.tab2.text = getString(R.string.suggestion)



        v.tab1.setOnClickListener {

            v.tab1.setTextColor(resources.getColor(R.color.appBlue))
            v.tab2.setTextColor(resources.getColor(R.color.realwhite))
            v.tab1.setBackgroundResource(R.drawable.round_border_edittext_blue)
            v.tab2.setBackgroundResource(R.drawable.round_border_edittext_gradient)
            binding.name.hint = "Subject"
            binding.summary.hint = "Your message"
            tabselected = 0

        }
        v.tab2.setOnClickListener {

            v.tab2.setTextColor(resources.getColor(R.color.appBlue))
            v.tab1.setTextColor(resources.getColor(R.color.realwhite))
            v.tab2.setBackgroundResource(R.drawable.round_border_edittext_blue)
            v.tab1.setBackgroundResource(R.drawable.round_border_edittext_gradient)
            binding.name.hint = "Category Name"
            binding.summary.hint = "Description"
            tabselected = 1
        }

        binding.back.setOnClickListener() {
            onBackPressed()
            this.finish()
        }
        binding.button.setOnClickListener() {
            if (binding.name1.text.isNullOrEmpty()) {
                binding.nameError.visibility = View.VISIBLE
            }
            if (binding.summary.text.isNullOrEmpty()) {
                binding.summary.error = getString(R.string.messageError)
            }

            if (!binding.name1.text.isNullOrEmpty() && !binding.summary.text.isNullOrEmpty()) {

                if (tabselected == 0)
                    uploadData(
                        binding.name1.text.toString(),
                        binding.summary.text.toString(),
                        token
                    )
                else
                    uploadSuggestion(
                        binding.name1.text.toString(),
                        binding.summary.text.toString(),
                        token
                    )

            }
        }

    }

    fun uploadData(subject: String, message: String, token: String) {
        otherView.contactUS(subject, message, token)?.observe(this) {
            if (it?.status == 1) {
                Toast.makeText(this, it?.message, Toast.LENGTH_SHORT).show()
                onBackPressed()
                this.finish()
            }
        }
    }

    fun uploadSuggestion(subject: String, message: String, token: String) {
        otherView.suggestion(subject, message, token)?.observe(this) {
            if (it?.status == 1) {
                Toast.makeText(this, it?.message, Toast.LENGTH_SHORT).show()
                onBackPressed()
                this.finish()
            }
        }
    }
}