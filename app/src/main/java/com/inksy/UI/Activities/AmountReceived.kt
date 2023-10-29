package com.inksy.UI.Activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.inksy.Model.TransactionModel
import com.inksy.Remote.Status
import com.inksy.UI.Adapter.AmountReceivedAdapter
import com.inksy.UI.ViewModel.DoodleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivityAmountReceivedBinding

class AmountReceived : AppCompatActivity() {
    lateinit var doodleView: DoodleView
    lateinit var tinydb: TinyDB
    var token = ""
    lateinit var binding: ActivityAmountReceivedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAmountReceivedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tinydb = TinyDB(this)
        token = tinydb.getString("token").toString()
        doodleView = ViewModelProvider(this)[DoodleView::class.java]
        doodleView.init()

        getData(token)

        binding.back.setOnClickListener {
            onBackPressed()
            this.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        getData(token)
    }

    fun getData(token: String) {
        doodleView.getTransaction(token)?.observe(this) {
            when (it?.status) {
                Status.LOADING -> {}
                Status.ERROR -> {}
                Status.SUCCESS -> {

                    var list = it?.data?.data as ArrayList<TransactionModel>
                    if (list.size > 0) {
                        binding.rvFriends.adapter = AmountReceivedAdapter(this, list)
                        binding.layoutempty.visibility = View.GONE
                    } else {
                        binding.layoutempty.visibility = View.VISIBLE
                    }
                }
                else -> {}
            }
        }
    }
}