package com.inksy.UI.Activities

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.inksy.UI.Constants
import com.inksy.UI.ViewModel.OthersView
import com.inksy.databinding.FragmentCommunityGuideBinding


class CommunityGuide : AppCompatActivity() {

    lateinit var binding: FragmentCommunityGuideBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCommunityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener {
            this.finish()
        }

        getData(binding)

    }


    fun getData(binding: FragmentCommunityGuideBinding) {
        val sharedPreferences = getSharedPreferences(
            Constants.APP_NAME,
            Context.MODE_PRIVATE
        )

        val token = sharedPreferences.getString("token", "")


        val othersView: OthersView =
            ViewModelProvider(this@CommunityGuide)[OthersView::class.java]
        othersView.init()
        othersView.community(
            token
        )?.observe(this@CommunityGuide) {

            if (it?.status == 1) {


                binding.textView16.text = it.data?.title
                binding.description.text = Html.fromHtml(it.data?.description).toString()

            } else {
                Toast.makeText(this@CommunityGuide, it?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}