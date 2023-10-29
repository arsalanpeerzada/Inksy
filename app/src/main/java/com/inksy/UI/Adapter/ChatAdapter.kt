package com.inksy.UI.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inksy.Model.MyChatsModel2
import com.inksy.R
import com.inksy.UI.Activities.ChatActivityNew
import com.inksy.UI.Constants
import com.inksy.Utils.TinyDB
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ChatAdapter(
    var context: Context,
    var list: ArrayList<MyChatsModel2> = ArrayList(),
    var arraylist: ArrayList<MyChatsModel2> = ArrayList()
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>(), Filterable {

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var tv: TextView
        lateinit var text: TextView
        lateinit var text2: TextView
        lateinit var userImage: ImageView

        fun bind() {

            tv = itemView.findViewById(R.id.name)
            text = itemView.findViewById(R.id.text)
            text2 = itemView.findViewById(R.id.text2)
            userImage = itemView.findViewById(R.id.userImage)

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {

            holder.bind()

            holder.tv.text = list[position].user_2_name
            holder.text.text = list[position].user_2_country

            holder.text2.text = getLongToAgo(list[position].date!!)

            Glide.with(context).load(Constants.BASE_IMAGE + list[position].user_2_image)
                .placeholder(R.drawable.ic_empty_user)
                .into(holder.userImage)

        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

        holder.itemView.setOnClickListener {

            val tinyDB = TinyDB(context)

            val loggedInUserID: String = tinyDB.getString("id")!!
            val anotherUserUserID: String = list[position].user_2!!
            val anotherUserUserName: String = list[position].user_2_name!!
            val anotherUserUserImage: String = Constants.BASE_IMAGE + list[position].user_2_image
            val anotherUserCountry: String = list[position].user_2_country!!
//            var chatRoom = ""

//            chatRoom = if (loggedInUserID.toInt() < anotherUserUserID.toInt()) {
//                "$loggedInUserID-$anotherUserUserID"
//            } else {
//                "$anotherUserUserID-$loggedInUserID"
//            }

            val intent = Intent(context, ChatActivityNew::class.java)
            intent.putExtra("ChatRoom", list[position].chat_id)
            intent.putExtra("UserID", anotherUserUserID)
            intent.putExtra("UserName", anotherUserUserName)
            intent.putExtra("UserImage", anotherUserUserImage)
            intent.putExtra("UserCountry", anotherUserCountry)
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

//    fun setFilter(countryModels: List<Get_Doctors_Model.myList>, _searchText: String) {
//        doc_list = ArrayList<Get_Doctors_Model.myList>()
//        doc_list.addAll(countryModels)
//        this.searchText = _searchText
//        notifyDataSetChanged()
//    }

    fun getLongToAgo(createdAt: Long): String? {
        val userDateFormat: DateFormat = SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy")
        val dateFormatNeeded: DateFormat = SimpleDateFormat("MM/dd/yyyy HH:MM:SS")
        var date: Date? = null
        date = Date(createdAt)
        var crdate1: String = dateFormatNeeded.format(date)

        // Date Calculation
        val dateFormat: DateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
        crdate1 = SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date)

        // get current date time with Calendar()
        val cal: Calendar = Calendar.getInstance()
        val currenttime: String = dateFormat.format(cal.getTime())
        var CreatedAt: Date? = null
        var current: Date? = null
        try {
            CreatedAt = dateFormat.parse(crdate1)
            current = dateFormat.parse(currenttime)
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        // Get msec from each, and subtract.
        val diff: Long = current?.getTime()!! - CreatedAt?.getTime()!!
        val diffSeconds = diff / 1000
        val diffMinutes = diff / (60 * 1000) % 60
        val diffHours = diff / (60 * 60 * 1000) % 24
        val diffDays = diff / (24 * 60 * 60 * 1000)

        var time: String? = null
        if (diffDays > 0) {
            time = if (diffDays == 1L) {
                diffDays.toString() + " day ago "
            } else {
                diffDays.toString() + " days ago "
            }
        } else {
            if (diffHours > 0) {
                time = if (diffHours == 1L) {
                    diffHours.toString() + " hour ago"
                } else {
                    diffHours.toString() + " hours ago"
                }
            } else {
                if (diffMinutes > 0) {
                    time = if (diffMinutes == 1L) {
                        diffMinutes.toString() + " minute ago"
                    } else {
                        diffMinutes.toString() + " minutes ago"
                    }
                } else {
                    if (diffSeconds > 0) {
                        time = if (diffSeconds == 1L) {
                            diffSeconds.toString() + " second ago"
                        } else {
                            diffSeconds.toString() + " seconds ago"
                        }
                    }
                }
            }
        }
        return time
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    list = arraylist
                } else {
                    val filteredList: ArrayList<MyChatsModel2> = ArrayList()
                    for (row in arraylist) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.user_2_name?.toLowerCase()
                                ?.contains(charString.lowercase(Locale.getDefault())) == true) {
                            filteredList.add(row)
                        }
                    }
                    list = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = list
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                list = filterResults.values as ArrayList<MyChatsModel2>
                notifyDataSetChanged()
            }
        }
    }

}