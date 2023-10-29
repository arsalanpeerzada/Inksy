package com.inksy.UI.Activities

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import com.google.gson.JsonElement
import com.inksy.Database.Entities.SelectedAudience
import com.inksy.Interfaces.OnChangeStateClickListener
import com.inksy.Model.ChatMessageModel
import com.inksy.Model.UserModel
import com.inksy.Remote.APIInterface.ApiResponse
import com.inksy.Remote.Resource
import com.inksy.Remote.Status
import com.inksy.UI.Adapter.AudienceAdapter
import com.inksy.UI.Constants
import com.inksy.UI.ViewModel.NotificationView
import com.inksy.UI.ViewModel.PeopleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivityShareJournalBinding
import kotlin.collections.List


class ShareJournalActivity : AppCompatActivity(), OnChangeStateClickListener {

    var token: String = ""
    lateinit var tinyDB: TinyDB
    lateinit var peopleView: PeopleView
    lateinit var binding: ActivityShareJournalBinding
    var list: ArrayList<SelectedAudience> = ArrayList()
    var chatRoomList: ArrayList<String> = ArrayList()
    var userIDList: ArrayList<Int> = ArrayList()
    var dummyList: ArrayList<UserModel> = ArrayList()
    var audienceList: ArrayList<UserModel>? = ArrayList()
    var journalID = 0
    var sendMessageCount = 0
    var isfinish = false
    var coverImage = "0"
    var userID = "0"
    var chatRoomGlobal: String = ""

    private var mDatabase: DatabaseReference? = null
    var chatMessageModelArrayList: ArrayList<ChatMessageModel>? = null
    var isFirstMessageToSend = false

    lateinit var notificationView: NotificationView
    lateinit var observerCreateChatNode: Observer<Resource<ApiResponse<JsonElement>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatMessageModelArrayList = ArrayList()

        notificationView = ViewModelProvider(this).get(NotificationView::class.java)
        notificationView!!.init()

        var idcheck = intent.getBooleanExtra(Constants.IDCHECK, false)
        if (idcheck) {
            journalID = intent.getIntExtra("JournalID", 0)
            coverImage = intent.getStringExtra("CoverImage")!!
        }

        peopleView = ViewModelProvider(this)[PeopleView::class.java]
        peopleView.init()
        tinyDB = TinyDB(this)
        token = tinyDB.getString("token").toString()

        binding.back.setOnClickListener {
            this.finish()
        }

        binding.button.setOnClickListener {

            if(list.size > 0){
                for (i in 0 until list.size) {
                    var anotherUserID: Int = list.get(i).userID?.toInt()!!
                    var myUserID: Int = tinyDB.getString("id")!!.toInt()
                    var chatRoom: String = ""

                    if(anotherUserID < myUserID){
                        chatRoom = "u-" + anotherUserID + "-" + myUserID
                    } else {
                        chatRoom = "u-" + myUserID + "-" + anotherUserID
                    }

                    userIDList.add(anotherUserID)
                    chatRoomList.add(chatRoom)
//                    Log.d("isFirstMessageToSend", "Clicked")
//                    getIsChildExist(chatRoom)


//                    getChatAndStartListener(chatRoom)

                }
            }

            if(chatRoomList.size > 0){
                for (i in 0 until chatRoomList.size) {
                    getIsChildExist(chatRoomList.get(i), userIDList.get(i))
                }
            }

        }

        observerCreateChatNode =
            Observer<Resource<ApiResponse<JsonElement>>> { (status) ->
                if (status === Status.LOADING) {
                } else if (status === Status.ERROR) {
                } else if (status === Status.SUCCESS) {
//                    getUsersData2(userID, "UploadImage", firebaseTempImage)
                    sendMessage(
                        chatRoomGlobal,
                        coverImage,
                        tinyDB.getString("id"),
                        tinyDB.getString("fullname"),
                        2,
                        tinyDB.getString("avatar"),
                        /*isOnChat*/ false,
                        journalID
                    )
                }
            }

        getData(token, list)
    }

    fun getData(token: String, savedAudienceList: List<SelectedAudience>) {
        peopleView.getfollowersFollowList(token)?.observe(this) {
            when (it?.status) {
                Status.ERROR -> {}
                Status.SUCCESS -> {
                    audienceList = it.data?.data as ArrayList<UserModel>?
                    binding.rvAudience.adapter = AudienceAdapter(
                        this,
                        audienceList!!, savedAudienceList, this
                    )
                }
                Status.LOADING -> {}
                else -> {}
            }
        }
    }

    override fun onStateChange(id: Int, like: Boolean, type: String) {
        super.onStateChange(id, like, type)

        if (like) {
            var audience = audienceList?.get(id)!!
            var data = SelectedAudience(
                id,
                audience.id.toString(),
                audience.avatar,
                audience.bio,
                audience.is_artist.toString()
            )

            list.add(data)

        } else {

            if(list.size > 0){
                list.indices.forEach {
                    val tempString = list[it].audienceId
                    if (tempString == id) {
                        list.removeAt(it)
                    }
                }
            }

        }
    }

    public fun getListSize(): Int{
        return list.size
    }

    private fun getFirebaseDBInstance(): DatabaseReference? {
        mDatabase = FirebaseDatabase.getInstance().reference
        return mDatabase
    }

    fun getIsChildExist(chatRoom: String, userID: Int): String{
        val query: Query? = getFirebaseDBInstance()
            ?.child("chats")
            ?.child(chatRoom)

        query?.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    if(!isfinish){
                        Log.d("isFirstMessageToSend", "SendMessage")
                        sendMessage(
                            chatRoom,
                            coverImage,
                            tinyDB.getString("id"),
                            tinyDB.getString("fullname"),
                            2,
                            tinyDB.getString("avatar"),
                            /*isOnChat*/ false,
                            journalID
                        )
                    }

                } else {
                    if(!isfinish){
                        isFirstMessageToSend = true
                        Log.d("isFirstMessageToSend", "True")
                        createChatNode(userID)
                        chatRoomGlobal = chatRoom
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        return ""
    }

/*
    fun getChatAndStartListener(chatRoom: String) {
        val query: Query? = getFirebaseDBInstance()
            ?.child("chats")
            ?.child(chatRoom)

        query?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                chatMessageModelArrayList?.clear()
                for (data in dataSnapshot.children) {
                    val chatMessageModel = data.getValue(ChatMessageModel::class.java)

                    if (chatMessageModel != null) {
                        chatMessageModelArrayList?.add(chatMessageModel)
                    }
                }
                if (chatMessageModelArrayList?.size == 0) {
                    isFirstMessageToSend = true
                    if(!isfinish){
                        Log.d("isFirstMessageToSend", "True")
                        createChatNode(userID)
                    }

                } else {
                    if(!isfinish){
                        Log.d("isFirstMessageToSend", "SendMessage")
                        sendMessage(
                            coverImage,
                            tinyDB.getString("id"),
                            tinyDB.getString("fullname"),
                            2,
                            tinyDB.getString("avatar"),
                            */
/*isOnChat*//*
 false,
                            journalID
                        )
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext, "" + databaseError.message, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
*/

    private fun createChatNode(userId: Int) {
        notificationView.createChatNode(userId, tinyDB.getString("token").toString())
            ?.observe(this, observerCreateChatNode)
    }

    private fun sendMessage(
        chatRoom: String,
        message: String?,
        senderID: String?,
        senderName: String?,
        type: Int?,
        image: String?,
        isUnRead: Boolean?,
        journalID: Int
    ) {
        val chatMessageModel = ChatMessageModel()
        chatMessageModel.message = message
        chatMessageModel.senderId = senderID?.toLong()
        chatMessageModel.senderName = senderName
        chatMessageModel.setTimestamp(ServerValue.TIMESTAMP)
        chatMessageModel.type = type.toString().toLong()
        chatMessageModel.userImage = image
        chatMessageModel.journalID = journalID

        val uniqueID = mDatabase!!.push().key
        mDatabase!!.child("chats").child(chatRoom).child(uniqueID!!).setValue(chatMessageModel)
            .addOnSuccessListener {

                Log.d("isFirstMessageToSend", "Success")
                sendMessageCount++
                if(list.size == sendMessageCount){
                    Log.d("isFirstMessageToSend", "Finish")
                    isfinish = true
                    finish()
                }

            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@ShareJournalActivity,
                    "" + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    class AsyncTaskClass() : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String {

            return ""
        }

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

        }
    }

}