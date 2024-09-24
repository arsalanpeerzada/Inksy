package com.inksy.UI.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.inksy.Model.ChatDataModel
import com.inksy.Model.LastMessageModel
import com.inksy.Model.MyChatsModel2
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Activities.ProfileActivity
import com.inksy.UI.Adapter.ChatAdapter
import com.inksy.UI.Constants
import com.inksy.UI.ViewModel.ChatView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentChatBinding
import java.util.*


class Chat : Fragment() {

    lateinit var binding: FragmentChatBinding
    lateinit var chatView: ChatView
    var token = " "
    var list: List<ChatDataModel> = ArrayList()
    lateinit var adapter: ChatAdapter
    var stringArrayList: ArrayList<String>? = null
    private var mDatabase: DatabaseReference? = null
    private var imageModelArrayList: ArrayList<MyChatsModel2>? = null
    var tinydb: TinyDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater)

        stringArrayList = ArrayList()
        imageModelArrayList = ArrayList()

        tinydb = TinyDB(requireContext())

        token = TinyDB(requireContext()).getString("token").toString()
        Log.d("Token", token)

        chatView = ViewModelProvider(this)[ChatView::class.java]
        chatView.init()

        if (!tinydb?.getString("avatar").isNullOrEmpty()) {
            Glide.with(requireContext()).load(Constants.BASE_IMAGE + tinydb?.getString("avatar"))
                .placeholder(R.drawable.ic_empty_user)
                .into(binding.profile)
        }

        binding.profile.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }

        binding.refreshListener.setOnRefreshListener {
            binding.refreshListener.isRefreshing = true
            getChatList(token)
        }

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(query: CharSequence, start: Int, before: Int, count: Int) {

                adapter.filter!!.filter(query.toString())

            }
        })

/*
        binding.search.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                openactivity()
                return@OnEditorActionListener true
            }
            false
        })
*/

        getChatList(token)

        val tempEle: ArrayList<MyChatsModel2> = ArrayList()
        adapter = ChatAdapter(requireContext(), tempEle, tempEle)

        return binding.root
    }

    private fun openactivity() {

//        requireContext().startActivity(
//            Intent(requireContext(), ViewAll::class.java).putExtra(
//                "activity",
//                Constants.peopleSearch
//            )
//        )

    }

    private fun performSearch() {
        binding.search.clearFocus()
        binding.search.text.clear()
        val input: InputMethodManager? =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input?.hideSoftInputFromWindow(binding.search.windowToken, 0)
        //...perform search
    }

    private fun getChatList(_token: String) {
        chatView.chatList(_token)?.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {}
                Status.ERROR -> {
                    binding.rvChat.visibility = View.GONE
                    binding.layoutemptyChat.visibility = View.VISIBLE
                    binding.refreshListener.isRefreshing = false

                }
                Status.SUCCESS -> {
                    (list as ArrayList<ChatDataModel>).clear()
                    stringArrayList?.clear()
                    imageModelArrayList?.clear()

                    list = it.data!!.data!!
                    if (list.size > 0) {

                        for (i in 0 until list.size) {

                            val myChatsModel2 = MyChatsModel2()

                            myChatsModel2.id = list.get(i).id.toString()
                            myChatsModel2.user_2 = list.get(i).user?.id.toString()
                            myChatsModel2.chat_id = list.get(i).chatId.toString()
                            myChatsModel2.user_2_name = list.get(i).user?.fullName
                            myChatsModel2.user_2_image = list.get(i).user?.avatar

/*
                            val userID1: String = list.get(i).senderId.toString()
                            val userName1: String = jsonObject1.getString("user_1_name")
                            val userImage1: String = jsonObject1.getString("user_1_image")
                            val userCountry1: String = jsonObject1.getString("user_1_country")

                            val userID2: String = list.get(i).receiverId.toString()
                            val userName2: String = jsonObject1.getString("user_2_name")
                            val userImage2: String = jsonObject1.getString("user_2_image")
                            val userCountry2: String = jsonObject1.getString("user_2_country")

                            if (tinydb?.getString("id").equals(userID1)
                            ) {
                                myChatsModel2.user_2 = userID2
                                myChatsModel2.user_2_name = userName2
                                myChatsModel2.user_2_image = userImage2
                                myChatsModel2.user_2_country = userCountry2
                            } else {
                                myChatsModel2.user_2 = userID1
                                myChatsModel2.user_2_name = userName1
                                myChatsModel2.user_2_image = userImage1
                                myChatsModel2.user_2_country = userCountry1
                            }

                            myChatsModel2.date = jsonObject1.getString("date")
*/

                            stringArrayList!!.add(list.get(i).chatId.toString())
                            if(myChatsModel2.user_2 != "0" && myChatsModel2.user_2 != "null"){
                                imageModelArrayList!!.add(myChatsModel2)
                            }

                        }

                        if (stringArrayList?.size!! > 0) {
                            for (i in 0 until stringArrayList?.size!!) {
                                var splitValue = stringArrayList!!.get(i).split("-")
                                if(splitValue[1] != "0" && splitValue[2] != "0"){
                                    getUsersLastMsg(stringArrayList!!.get(i))
                                }
                            }
                        }

                    } else {
                        binding.rvChat.visibility = View.GONE
                        binding.layoutemptyChat.visibility = View.VISIBLE
                    }

                    binding.refreshListener.isRefreshing = false

                }
            }
        }
    }

    private fun getFirebaseDBInstance(): DatabaseReference? {
        mDatabase = FirebaseDatabase.getInstance().reference
        return mDatabase
    }

    fun getUsersLastMsg(chatRoom: String) {
        Log.d("ChatRoom", chatRoom)
        val query: Query = getFirebaseDBInstance()
            ?.child("chats")
            ?.child(chatRoom)
            ?.limitToLast(1)!!

        query.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("", "")
                var lastMessageModel = LastMessageModel()
                val key: String = dataSnapshot?.getKey()!!
//                val value: Any = dataSnapshot?.getValue()!!

                for (postSnapshot in dataSnapshot.getChildren()) {
                    lastMessageModel = postSnapshot?.getValue(LastMessageModel::class.java)!!
                    println(lastMessageModel.message + " - " + lastMessageModel.type)

                    for (j in 0 until imageModelArrayList?.size!!) {
                        if (key == imageModelArrayList?.get(j)?.chat_id) {
                            val model2: MyChatsModel2 = imageModelArrayList?.get(j)!!
                            if (lastMessageModel.message.toString().startsWith("http")) {
                                if (!tinydb?.getString("id")!!
                                        .equals(lastMessageModel.senderId)
                                ) {
                                    if (lastMessageModel.isUnRead) {
                                        model2.isUnRead = true
                                    } else {
                                        model2.isUnRead = false
                                    }
                                }
                                val isMine = if (lastMessageModel.senderId.toString().equals(
                                        tinydb?.getString("id")!!
                                    )
                                ) "You: " else ""
                                model2.user_2_country = isMine + "Image"
                                model2.date = lastMessageModel.timestamp
                            } else {
                                if (!tinydb?.getString("id")!!
                                        .equals(lastMessageModel.senderId)
                                ) {
                                    if (lastMessageModel.isUnRead) {
                                        model2.isUnRead = true
                                    } else {
                                        model2.isUnRead = false
                                    }
                                }
                                val isMine = if (lastMessageModel.senderId.toString().equals(
                                        tinydb?.getString("id")!!
                                    )
                                ) "You: " else ""
                                model2.user_2_country = isMine + "" + lastMessageModel.message
                                model2.date = lastMessageModel.timestamp
                            }
                            imageModelArrayList?.removeAt(j)
                            imageModelArrayList?.add(model2)

                            break
                        }
                    }

                }

                Handler().postDelayed(Runnable {
                    val tempElements: ArrayList<MyChatsModel2> =
                        ArrayList<MyChatsModel2>(imageModelArrayList)
                    Collections.sort(tempElements, Comparator<MyChatsModel2?> { obj1, obj2 ->
                        obj2.date!!.compareTo(obj1.date!!) // To compare string values
                    })

//                    adap = MyChatsAdapter2(activity, tempElements)
//                    recyclerRecantchats.setAdapter(adap)
//                    adap.notifyDataSetChanged()

                    adapter = ChatAdapter(requireContext(), tempElements, tempElements)
                    binding.rvChat.adapter = adapter

                    binding.rvChat.visibility = View.VISIBLE
                    binding.layoutemptyChat.visibility = View.GONE

                }, 1000)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show()
            }
        })
    }

}