package com.inksy.UI.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.inksy.Interfaces.OnDialogClickListener
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Dialogs.TwoButtonDialog
import com.inksy.UI.ViewModel.DoodleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivityArtisePanelBinding


class ArtisePanel : AppCompatActivity() {

    lateinit var binding: ActivityArtisePanelBinding
    lateinit var doodleView: DoodleView
    lateinit var tinydb: TinyDB
    var token = ""
   // private var mInterstitialAd: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtisePanelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tinydb = TinyDB(this)
        token = tinydb.getString("token").toString()
        doodleView = ViewModelProvider(this)[DoodleView::class.java]
        doodleView.init()
        var isArtist = tinydb.getInt("isArtist")
        loadIntersititialAd()

        if (isArtist == 1){
            binding.buttonText.text = getString(R.string.gotoartistpanel)
        }

        binding.iamin.setOnClickListener {

            if (isArtist == 1) {

               startIntersititialAd()


            } else {
                val twoButtonDialog: TwoButtonDialog = TwoButtonDialog(
                    this, getString(R.string.app_name),
                    getString(com.inksy.R.string.artist_writeup),
                    getString(com.inksy.R.string.iamsure),
                    "No",
                    object : OnDialogClickListener {
                        override fun onDialogClick(callBack: String?) {
                            if (callBack == "Yes") {
                                makeartist()

                                binding.loader.visibility = View.VISIBLE

                            } else {

                            }
                        }
                    })
                twoButtonDialog.window!!.setBackgroundDrawableResource(R.color.transparent)
                twoButtonDialog.show()
            }


        }

        binding.ManagePayment.setOnClickListener {
            startActivity(Intent(this@ArtisePanel, PaymentActivity::class.java))


        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.link.setOnClickListener(){
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse("http://www.copyright.gov")
            startActivity(browserIntent)
        }

    }

    private fun makeartist() {

        doodleView.artistMake(token)?.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.loader.visibility = View.GONE
                    tinydb.putInt("isArtist", 1)
//                    this@ArtisePanel.startActivity(
//                        Intent(
//                            this@ArtisePanel,
//                            ArtistDashboard::class.java
//                        )
//                    )
//                    this@ArtisePanel.finish()

                    startIntersititialAd()

                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(this, it?.data?.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun loadIntersititialAd(){
//        var adRequest = AdRequest.Builder().build()
//
//        InterstitialAd.load(this,"ca-app-pub-9808753304257500/8867580936", adRequest, object : InterstitialAdLoadCallback() {
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//                Log.d("MobileAD", adError?.toString())
//                mInterstitialAd = null
//            }
//
//            override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                Log.d("MobileAD", "Ad was loaded.")
//                mInterstitialAd = interstitialAd
//            }
//        })
    }

    private fun startIntersititialAd(){

//        if (mInterstitialAd != null) {
//            mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
//                override fun onAdClicked() {
//                    // Called when a click is recorded for an ad.
//                    Log.d("MobileAD", "Ad was clicked.")
//                }
//
//                override fun onAdDismissedFullScreenContent() {
//                    // Called when ad is dismissed.
//                    Log.d("MobileAD", "Ad dismissed fullscreen content.")
//                    mInterstitialAd = null
//
//                    this@ArtisePanel.startActivity(
//                        Intent(
//                            this@ArtisePanel,
//                            ArtistDashboard::class.java
//                        )
//                    )
//                    this@ArtisePanel.finish()
//                }
//
//                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
//                    // Called when ad fails to show.
//                    Log.e("MobileAD", "Ad failed to show fullscreen content.")
//                    mInterstitialAd = null
//                }
//
//                override fun onAdImpression() {
//                    // Called when an impression is recorded for an ad.
//                    Log.d("MobileAD", "Ad recorded an impression.")
//                }
//
//                override fun onAdShowedFullScreenContent() {
//                    // Called when ad is shown.
//                    Log.d("MobileAD", "Ad showed fullscreen content.")
//                }
//            }
//            mInterstitialAd?.show(this)
//        } else {
//            Log.d("MobileAD", "The interstitial ad wasn't ready yet.")
//        }

    }
}