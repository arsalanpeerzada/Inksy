package com.inksy.UI.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.inksy.Database.Entities.JournalIndexTable
import com.inksy.Database.Entities.PageTable
import com.inksy.Database.JournalDatabase
import com.inksy.Interfaces.OnDialogClickListener
import com.inksy.Model.Journals
import com.inksy.Model.UserModel
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Constants
import com.inksy.UI.Dialogs.Comment_BottomSheet
import com.inksy.UI.Dialogs.ReportDialog
import com.inksy.UI.Dialogs.TwoButtonDialog
import com.inksy.UI.ViewModel.JournalView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivityViewOnlyJournalBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class ViewOnlyJournal() : AppCompatActivity() {
    var journalData: Journals? = null
    private var mInterstitialAd: InterstitialAd? = null
    lateinit var binding: ActivityViewOnlyJournalBinding
    lateinit var like: ImageView
    lateinit var comment: ImageView
    lateinit var journalView: JournalView
    lateinit var like_counter: TextView
    lateinit var comment_counter: TextView
    lateinit var tinyDb: TinyDB
    var journalId: Int = 0
    var like_count: Int = 0
    var comment_count: Int = 0
    var followcounter: Int = 0
    var userModel = UserModel()
    var loading = true
    lateinit var journalDatabase: JournalDatabase
    lateinit var pTable: PageTable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewOnlyJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadIntersititialAd()
        tinyDb = TinyDB(this)
        journalView = ViewModelProvider(this)[JournalView::class.java]
        journalView.init()
        var token = tinyDb.getString("token")
        Handler().postDelayed({
            binding.loader.visibility = View.GONE
        }, 3000)

        like = findViewById(R.id.like)
        comment = findViewById(R.id.comment)
        like_counter = findViewById(R.id.like_count)
        comment_counter = findViewById(R.id.comment_count)
        followcounter = 0
        binding.followCount.text = "0"

        journalData = null

        if (intent.hasExtra("data")) {
            journalData = intent.getSerializableExtra("data") as? Journals
            journalId = journalData?.id!!

        } else {
            journalId = intent.getIntExtra("JournalId", 0)
        }

        if (journalData != null)
            getData(journalData!!.id!!, token!!)
        else
            getData(journalId, token!!)

        var string = intent.getStringExtra(Constants.journalType)

        binding.like.setOnClickListener {

            if (journalData != null) {
                if (journalData?.isJournalLike == 0) {
                    like_count++
                    like_counter.text = like_count.toString()
                    journalData?.isJournalLike = 1
                    likeJournal(journalData?.id, true, token!!)
                } else if (like_count == 1) {
                    journalData?.isJournalLike = 0
                    like_count--
                    like_counter.text = like_count.toString()
                    likeJournal(journalData?.id, true, token!!)
                }
            } else {
                Toast.makeText(this, "Journal data is empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.share.setOnClickListener {
            startActivity(
                Intent(this@ViewOnlyJournal, ShareJournalActivity::class.java)
                    .putExtra("JournalID", journalId)
                    .putExtra("CoverImage", Constants.BASE_IMAGE + journalData?.coverImage)
                    .putExtra(Constants.IDCHECK, true)
            )
        }

        binding.comment.setOnClickListener {


            Comment_BottomSheet(journalData?.id!!).show(supportFragmentManager, "")

            Log.d("-----", comment_count.toString())
        }

        binding.follow.setOnClickListener()
        {
            if (journalData?.isJournalFollow == 0) {
                Glide.with(this).load(ContextCompat.getDrawable(this, R.drawable.follow))
                    .into(binding.follow)
                journalData?.isJournalFollow = 1
                followcounter++
                binding.followCount.text = followcounter.toString()
                followJournal(journalData?.id, token!!)
            } else {
                journalData?.isJournalFollow = 0
                followcounter--
                binding.followCount.text = followcounter.toString()
                Glide.with(this).load(getDrawable(R.drawable.unfollow)).into(binding.follow)
                followJournal(journalData?.id, token!!)

            }
        }

        binding.profileImage.setOnClickListener {
            startActivity(
                Intent(this, People::class.java).putExtra(
                    "Data",
                    userModel
                )
            )
        }
        binding.nextpage.setOnClickListener() {

            if (loading) {
                Toast.makeText(this, "loading", Toast.LENGTH_SHORT).show()
            } else {
                val data = journalData?.pages
                if (data == null) {
                    Toast.makeText(this@ViewOnlyJournal, "Data is null", Toast.LENGTH_SHORT).show()
                } else {
                    loadTODatabase()

                    if (journalData?.pages?.size!! > 0) {
                        if (journalData?.pages?.get(0)?.pageImage != "") {
                            startActivity(
                                Intent(this, ShowJournal::class.java)
                                    .putExtra("Edit", "Edit")
                                    .putExtra("JSON", journalData?.htmlContent)
                                    .putExtra("background", journalData?.pages?.get(0)?.pageImage)
                                    .putExtra("createdby", journalData?.createdBy)
                            )
                        }
                    } else {
                        startActivity(
                            Intent(this, ShowJournal::class.java)
                                .putExtra("Edit", "Edit")
                                .putExtra("JSON", journalData?.htmlContent)
                        )
                    }
                }
            }
        }

        binding.userName.setOnClickListener {
            startActivity(
                Intent(this, People::class.java).putExtra(
                    "Data",
                    userModel
                )
            )
        }

        binding.more.setOnClickListener {

            if (loading){
                Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            }
            else {
                val contextWrapper = ContextThemeWrapper(this, R.style.popupMenuStyle)
                val popupMenu = PopupMenu(
                    contextWrapper, binding.more
                )
                popupMenu.setForceShowIcon(true)
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->

                    when (item.itemId) {
                        R.id.Delete -> {

                            if (journalData != null) {
                                val twoButtonDialog: TwoButtonDialog = TwoButtonDialog(
                                    this, getString(R.string.app_name),
                                    "Are you sure you want to delete this journal? ",
                                    getString(R.string.iamsure),
                                    "No",
                                    object : OnDialogClickListener {
                                        override fun onDialogClick(callBack: String?) {
                                            if (callBack == "Yes") {
                                                deleteJournal(journalData!!.id.toString())

                                                binding.loader.visibility = View.VISIBLE

                                            }
                                        }
                                    })
                                twoButtonDialog.window!!.setBackgroundDrawableResource(R.color.transparent)
                                twoButtonDialog.show()
                            } else {
                                Toast.makeText(this, "Journal data is empty", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            return@OnMenuItemClickListener true
                        }
                        R.id.edit -> {

                            val data = journalData?.htmlContent
                            if (data == null) {
                                Toast.makeText(
                                    this@ViewOnlyJournal,
                                    "Data is null",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                loadTODatabase()
                                startActivity(
                                    Intent(
                                        this@ViewOnlyJournal,
                                        CreateActivity::class.java
                                    )
                                )
                            }


                            return@OnMenuItemClickListener true
                        }
                        R.id.Report -> {

                            if (journalData != null) {
                                ReportDialog(
                                    this,
                                    this@ViewOnlyJournal,
                                    this@ViewOnlyJournal,
                                    this@ViewOnlyJournal,
                                    journalData?.id!!.toString(),
                                    "journal",
                                ).show()
                            } else {
                                Toast.makeText(this, "Journal data is empty", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            return@OnMenuItemClickListener true
                        }
                        R.id.block -> {
                            return@OnMenuItemClickListener true
                        }
                        R.id.View -> {
                            return@OnMenuItemClickListener true
                        }
                        R.id.Deactivate -> {
                            journalView.askforActivation(journalData?.id, token)
                                ?.observe(this) {
                                    when (it.status) {
                                        Status.SUCCESS -> {
                                            Toast.makeText(
                                                this,
                                                "Ask For Activation Pending",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            return@OnMenuItemClickListener true
                        }
                        else -> false
                    }


                })
                popupMenu.inflate(R.menu.view_journal_popup)
                popupMenu.show()

                if (journalData?.createdBy.toString() == tinyDb.getString("id")) {
                    popupMenu.menu.findItem(R.id.Home).isVisible = false
                    popupMenu.menu.findItem(R.id.Report).isVisible = false
                    popupMenu.menu.findItem(R.id.block).isVisible = false
                    popupMenu.menu.findItem(R.id.gallery).isVisible = false
                    popupMenu.menu.findItem(R.id.doodle).isVisible = false
                    popupMenu.menu.findItem(R.id.View).isVisible = false
                    popupMenu.menu.findItem(R.id.Deactivate).isVisible = false
                } else {
                    popupMenu.menu.findItem(R.id.Home).isVisible = false
                    popupMenu.menu.findItem(R.id.Delete).isVisible = false
                    popupMenu.menu.findItem(R.id.edit).isVisible = false
                    popupMenu.menu.findItem(R.id.block).isVisible = false
                    popupMenu.menu.findItem(R.id.gallery).isVisible = false
                    popupMenu.menu.findItem(R.id.doodle).isVisible = false
                    popupMenu.menu.findItem(R.id.View).isVisible = false
                    popupMenu.menu.findItem(R.id.Deactivate).isVisible = false

                }
                if (journalData?.isActive == 0) {
                    popupMenu.menu.findItem(R.id.Deactivate).isVisible = true
                }
            }
        }


        binding.ivBack.setOnClickListener {
            onBackPressed()
        }


    }

    private fun loadTODatabase() {


        var list = ArrayList<Journals>()
        journalDatabase = JournalDatabase.getInstance(this)!!
        journalDatabase.getJournalData().DeleteData("1")
        journalDatabase.getJournalData().deleteTable()
        journalDatabase.getJournalData().deleteSelectedAudience()
        journalDatabase.getJournalData().deletepageforlinkTable()
        var pages = journalData?.pages
       // val data = journalData?.htmlContent

        // val myd = JSONObject(data);


        for (i in 1 until pages?.size!!) {

            pTable = PageTable(
                Integer.valueOf(pages[i].pageNo!!),
                pages[i].id.toString(),
                pages[i].pageImage,
                pages[i].pageNo.toString(),
                "",
                "",
                "",
                pages[i].journalId.toString()
            )

            journalDatabase.getJournalData().insertPage(pTable)
        }


//        val arrayofBullets = myd.get("ArrayofBullets").toString()
//        var arrayofText = myd.get("ArrayofText").toString()
//        var arrayofImages = myd.get("ArrayofImage").toString()
//        var imageTemplate = myd.get("IndexBackground").toString()

        var jtable: JournalIndexTable = JournalIndexTable(
            "1",
            journalData?.id.toString(),
            journalData?.pages?.get(0)?.pageImage,
            journalData?.pages?.get(0)?.id.toString(),
            "",
            journalData?.title,
            journalData?.protection.toString(),
            journalData?.categoryId.toString(),
            journalData?.category?.categoryName.toString(),
            journalData?.coverImage.toString(),
            "Empty",
            journalData?.description.toString(),
            journalData?.coverBc.toString(),
            "",
            "",
            "",
            pages.size,
            "",
        )

        GlobalScope.launch {
            journalDatabase.getJournalData().insertJournalIndexTable(jtable)
        }
    }

    private fun deleteJournal(journal_id: String) {
        val token = tinyDb.getString("token")
        journalView.journalDelete(journal_id, token!!)?.observe(this) {
            when (it.status) {
                Status.ERROR -> {}
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    startActivity(Intent(this, MainActivity::class.java))


                    Toast.makeText(this, it?.data?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun likeJournal(id: Int?, like: Boolean, token: String) {

        journalView.journalLike(
            id.toString(),
            token
        )?.observe(this) {

            if (it?.data?.status == 1) {
                Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, it?.data?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun followJournal(id: Int?, token: String) {

        journalView.journalFollow(
            id.toString(),
            token
        )?.observe(this) {

            if (it?.data?.status == 1) {
                Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, it?.data?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getData(journalid: Int, token: String) {
        journalView.journalDetails(journalid, token)?.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    loading = false
                    val journals = it?.data?.data
                    journalData = journals
                    populatedata(journals!!)
                }
            }

        }
    }

    fun populatedata(_journalData: Journals) {


        if (_journalData != null) {

            binding.journalTitle.text = _journalData.title
            binding.subtext.text = _journalData.category?.categoryName
            binding.subtext2.text = _journalData.description
            binding.userName.text = _journalData.user?.fullName

            if (_journalData.user == null) {
                Toast.makeText(
                    applicationContext,
                    "This journal has been deleted",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
                return
            }

            userModel = _journalData.user!!

            Glide.with(this).load(Constants.BASE_IMAGE + _journalData.user?.avatar)
                .into(binding.profileImage)

            Glide.with(this).load(Constants.BASE_IMAGE + _journalData.coverImage)
                .into(binding.coverImage)

            like_count = _journalData.likesCount!!
            comment_count = _journalData.commentsCount!!

            like_counter.text = like_count.toString()
            comment_counter.text = comment_count.toString()
            binding.followCount.text = _journalData.followersCount.toString()
            followcounter = _journalData.followersCount!!



            if (journalData?.isJournalFollow == 0) {
                Glide.with(this).load(getDrawable(R.drawable.ic_unfollow)).into(binding.follow)

            } else {

                Glide.with(this).load(getDrawable(R.drawable.ic_follow)).into(binding.follow)

            }


        }
    }

    private fun loadIntersititialAd() {
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this,
            "ca-app-pub-9808753304257500/1677777040",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("MobileAD", adError?.toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d("MobileAD", "Ad was loaded.")
                    mInterstitialAd = interstitialAd

                    var showAd = 0;
                    showAd = tinyDb.getInt("showad")

                    if (showAd < 5) {
                        showAd++
                        tinyDb.putInt("showad", showAd)
                    } else if (showAd == 5) {
                        tinyDb.putInt("showad", 0)
                        startIntersititialAd()
                    }
                }
            })
    }

    private fun startIntersititialAd() {

        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d("MobileAD", "Ad was clicked.")
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    Log.d("MobileAD", "Ad dismissed fullscreen content.")
                    mInterstitialAd = null


                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when ad fails to show.
                    Log.e("MobileAD", "Ad failed to show fullscreen content.")
                    mInterstitialAd = null
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d("MobileAD", "Ad recorded an impression.")
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d("MobileAD", "Ad showed fullscreen content.")
                }
            }
            mInterstitialAd?.show(this)
        } else {
            Log.d("MobileAD", "The interstitial ad wasn't ready yet.")
        }

    }
}