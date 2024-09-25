package com.inksy.UI.Activities

import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.example.DoodlePack
import com.inksy.Interfaces.OnChangeStateClickListener
import com.inksy.Interfaces.OnDialogClickListener
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Model.Journals
import com.inksy.Model.UserModel
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Adapter.ArtworkAdapter
import com.inksy.UI.Adapter.BookAdapter
import com.inksy.UI.Constants
import com.inksy.UI.Dialogs.Comment_BottomSheet
import com.inksy.UI.Dialogs.ReportDialog
import com.inksy.UI.Dialogs.TwoButtonDialog
import com.inksy.UI.ViewModel.JournalView
import com.inksy.UI.ViewModel.PeopleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivityPeopleBinding
import com.inksy.databinding.TablayoutBinding
import java.io.Serializable


class People : AppCompatActivity(), iOnClickListerner, OnChangeStateClickListener {
    lateinit var peopleView: PeopleView
    lateinit var binding: ActivityPeopleBinding
    var data: UserModel = UserModel()
    var userId: Int? = 0
    var accountUser: Int? = 0
    var followersList: ArrayList<UserModel> = ArrayList()
    var list: ArrayList<Journals>? = ArrayList()
    var doodle_list: ArrayList<DoodlePack> = ArrayList()
    var token: String = " "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPeopleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loader.visibility = View.VISIBLE

        if (intent.hasExtra("Data")) {
            data = intent.getSerializableExtra("Data") as UserModel
        } else {
            userId = intent.getIntExtra("UserId", 0)
        }

        peopleView = ViewModelProvider(this)[PeopleView::class.java]
        peopleView.init()
        var tinyDB = TinyDB(this)

        token = tinyDB.getString("token")!!
        accountUser = tinyDB.getString("id")!!.toInt()


        if (data != null && intent.hasExtra("Data")) {
            getdetails(data.id!!, token)

        } else {
            getdetails(userId!!, token)
        }

        binding.follow.setOnClickListener {
            if (data.isFollowed?.status == 1) {
                data.isFollowed?.status = 0
                Glide.with(this).load(R.drawable.unfollow).into(binding.follow)
                unfollowUser(data.id!!)
            } else if (data.isFollowed?.status == null) {
                followUser(data.id!!)

            } else if (data.isFollowed?.status == 0) {
                unfollowUser(data.id!!)
                data.isFollowed?.status = null
            }

        }

        binding.seeall1.setOnClickListener {
            startActivity(
                Intent(this, ViewAll::class.java).putExtra(
                    Constants.activity,
                    Constants.sub_journalViewAll
                ).putExtra("Data", true)
                    .putExtra("List", list as Serializable)
            )
        }
        binding.seeall2.setOnClickListener {
            startActivity(
                Intent(this, ViewAll::class.java).putExtra(
                    Constants.activity,
                    Constants.doodleViewAll
                ).putExtra("Data", true).putExtra("List", doodle_list as Serializable)
            )
        }


        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.more.setOnClickListener {
            val contextWrapper = ContextThemeWrapper(this, R.style.popupMenuStyle)
            val popupMenu = PopupMenu(
                contextWrapper, binding.more
            )
            popupMenu.setForceShowIcon(true)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->

                when (item.itemId) {
                    R.id.Home->{
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        this.finish()
                        return@OnMenuItemClickListener true
                    }
                    R.id.Delete -> {

                        return@OnMenuItemClickListener true
                    }
                    R.id.edit -> {

                        return@OnMenuItemClickListener true
                    }
                    R.id.Report -> {

                        if (data != null) {
                            ReportDialog(
                                this,
                                this,
                                this,
                                this,
                                data?.id!!.toString(),
                                "user",
                            ).show()
                        }

                        return@OnMenuItemClickListener true
                    }
                    R.id.block -> {
                        openDialog()
                        return@OnMenuItemClickListener true
                    }

                    else -> false
                }


            })
            popupMenu.inflate(R.menu.view_journal_popup)
            popupMenu.show()

            popupMenu.menu.findItem(R.id.edit).isVisible = false
            popupMenu.menu.findItem(R.id.Delete).isVisible = false
            popupMenu.menu.findItem(R.id.View).isVisible = false
            popupMenu.menu.findItem(R.id.gallery).isVisible = false
            popupMenu.menu.findItem(R.id.doodle).isVisible = false

        }

        binding.followedPeople.setOnClickListener() {
            var intent = Intent(this, ViewAll::class.java).putExtra(
                "activity",
                Constants.peopleViewAll
            ).putExtra("List", followersList as Serializable).putExtra("Data", true)
            startActivity(intent)
        }

        binding.chat.setOnClickListener {
            if (data != null) {
                var anotherUserID: Int = data.id.toString().toInt()
                var myUserID: Int = tinyDB.getString("id")!!.toInt()
                var chatRoom: String = ""

                if (anotherUserID < myUserID) {
                    chatRoom = "u-" + anotherUserID + "-" + myUserID
                } else {
                    chatRoom = "u-" + myUserID + "-" + anotherUserID
                }

                val intent = Intent(this, ChatActivityNew::class.java)
                intent.putExtra("ChatRoom", chatRoom)
                intent.putExtra("UserID", data.id.toString())
                intent.putExtra("UserName", data.fullName)
                intent.putExtra("UserImage", Constants.BASE_IMAGE + data.avatar)
                intent.putExtra("UserCountry", "NotUsedInChatActivityClass")
                startActivity(intent)

            }
        }

    }

    fun getdetails(id: Int, token: String) {
        peopleView.userDetail(id, token)?.observe(this) { it ->
            binding.loader.visibility = View.GONE
            when (it.status) {
                Status.ERROR -> {

                }
                Status.SUCCESS -> {
//                    Toast.makeText(this, it.data?.data?.fullName, Toast.LENGTH_SHORT).show()

                    data = it?.data?.data!!

                    binding.title.text = data.fullName.toString()
                    binding.bio.text = data.bio.toString()

                    if (accountUser == data.id) {
                        binding.follow.visibility = View.GONE
                        binding.more.visibility = View.INVISIBLE
                        binding.more.isEnabled = false
                        binding.chat.visibility = View.GONE
                    }

                    if (data.avatar != null)
                        Glide.with(this).load(Constants.BASE_IMAGE + data.avatar)
                            .placeholder(R.drawable.ic_empty_user)
                            .into(binding.circleImageView)

                    var v: TablayoutBinding = binding.include1
                    v.tab1.text = "${data.fullName}'s Journals"

                    var v1: TablayoutBinding = binding.include6
                    v1.tab1.text = "${data.fullName}'s Arts"

                    if (it.data?.data?.isFollowed?.status == 1) {
                        Glide.with(this).load(R.drawable.follow).into(binding.follow)
                    } else if (it?.data?.data?.isFollowed?.status == null) {
                        Glide.with(this).load(R.drawable.unfollow).into(binding.follow)
                    } else if (it?.data?.data?.isFollowed?.status == 0) {
                        Glide.with(this).load(R.drawable.pending).into(binding.follow)
                    }

                    binding.followedPeople.text =
                        "Followed by ${it.data?.data?.followerCount} People"

                    binding.points.text = it.data?.data?.points.toString()


                    list = it.data.data?.journals
                    followersList = it.data.data?.followers!!


                    if (it.data.data?.doodles != null) {
                        doodle_list = it.data.data?.doodles!!
                    }


                    if (doodle_list.size == 0) {
                        binding.doodleTitle.visibility = View.GONE
                        binding.seeall2.visibility = View.GONE
                        binding.textView130.visibility = View.GONE
                        binding.layoutempty.visibility = View.VISIBLE
                    } else {
                        binding.doodleTitle.visibility = View.VISIBLE
                        binding.seeall2.visibility = View.VISIBLE
                        binding.textView130.visibility = View.VISIBLE
                        binding.layoutempty.visibility = View.GONE
                    }

                    if (list?.size == 0) {
                        binding.textView3.visibility = View.GONE
                        binding.textView4.visibility = View.GONE
                        binding.rvFriends.visibility = View.GONE
                        binding.seeall1.visibility = View.GONE
                    } else {
                        binding.textView3.visibility = View.VISIBLE
                        binding.textView4.visibility = View.VISIBLE
                        binding.rvFriends.visibility = View.VISIBLE
                        binding.seeall1.visibility = View.VISIBLE

                        binding.rvFriends.adapter =
                            BookAdapter(this, list!!, Constants.person, this, this)


                        binding.rvDoodle.adapter = ArtworkAdapter(this, doodle_list, "Pack")

                    }
                }
                Status.LOADING -> {

                }
            }

            //it.data?.data?.fullName
        }
    }

    override fun onStateChange(_id: Int, like: Boolean, type: String) {
        super.onStateChange(_id, like, type)
        if (like) {
            likeJournal(_id, like)
        } else {
            likeJournal(_id, like)
        }
    }

    override fun onclick(position: Int) {
        super.onclick(position)
        Comment_BottomSheet(position).show(supportFragmentManager, " ");
    }

    private fun likeJournal(id: Int?, like: Boolean) {
        val journalView: JournalView =
            ViewModelProvider(this)[JournalView::class.java]
        journalView.init()
        journalView.journalLike(
            id.toString(),
            token
        )?.observe(this) {

            if (it?.data?.status == 1) {
                Toast.makeText(this, it?.data.message, Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, it?.data?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openDialog() {
        val twoButtonDialog: TwoButtonDialog = TwoButtonDialog(
            this, "Block User",
            "Are you sure, You want to block this user?",
            getString(android.R.string.yes),
            getString(android.R.string.no),
            object : OnDialogClickListener {
                override fun onDialogClick(callBack: String?) {
                    if (callBack == "Yes") {
                        blockUser(data.id!!, token)
                    } else {

                    }
                }
            })
        twoButtonDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        twoButtonDialog.show()
    }

    fun blockUser(id: Int, token: String) {
        peopleView.userBlock(id, token)?.observe(this) {
            when (it.status) {
                Status.ERROR -> {

                }
                Status.SUCCESS -> {

                    Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                Status.LOADING -> {

                }
            }
        }
    }

    private fun followUser(id: Int) {
        peopleView.userFollow(id, token)?.observe(this@People) { it ->
            when (it?.status) {
                Status.SUCCESS -> {

                    if (it.data?.message.toString()
                            .contains(getString(R.string.followrequestsuccess))
                    ) {
                        data.isFollowed?.status = 0
                        Glide.with(this).load(R.drawable.pending).into(binding.follow)
                    } else {
                        data.isFollowed?.status = 1
                        Glide.with(this).load(R.drawable.follow).into(binding.follow)
                    }

                    Toast.makeText(this@People, it.data?.message.toString(), Toast.LENGTH_SHORT)
                        .show()

                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(this@People, it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    private fun unfollowUser(id: Int) {
        peopleView.userUnfollow(id, token)?.observe(this@People) { it ->
            when (it?.status) {
                Status.SUCCESS -> {
                    Toast.makeText(this@People, it.data?.message.toString(), Toast.LENGTH_SHORT)
                        .show()

                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(this@People, it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

}