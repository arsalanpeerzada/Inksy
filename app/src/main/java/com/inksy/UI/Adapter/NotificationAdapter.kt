package com.inksy.UI.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inksy.Model.NotificationDataModel
import com.inksy.R
import com.inksy.UI.Activities.*
import com.inksy.UI.Activities.List
import com.inksy.UI.Constants
import com.inksy.UI.Dialogs.Comment_BottomSheet
import com.inksy.Utils.TinyDB
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class NotificationAdapter(
    var context: Context,
    var list: ArrayList<NotificationDataModel> = ArrayList(),
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var tv: TextView
        lateinit var ivUserProfile: ImageView
        lateinit var text2: TextView

        fun bind() {

            tv = itemView.findViewById(R.id.name)
            ivUserProfile = itemView.findViewById(R.id.ivUserProfile)
            text2 = itemView.findViewById(R.id.text2)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notifications, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()

            holder.tv.text = list[position].data?.fullName + " " + list[position].data?.message
            holder.text2.text = getLongToAgo(list[position].createdAt!!)

            Glide.with(context).load(Constants.BASE_IMAGE + list[position].data?.avatar)
                .placeholder(R.drawable.ic_empty_user)
                .into(holder.ivUserProfile)

        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

        holder.itemView.setOnClickListener {

            var notificationType = list.get(position).data?.notiType

            if(notificationType.equals("JOURNAL")){

                context.startActivity(
                    Intent(context, ViewOnlyJournal::class.java).putExtra(Constants.journalType, "")
                        .putExtra("JournalId", list[position].data?.meta?.id)
                )

            } else if(notificationType.equals("USER_PROFILE")){

                context.startActivity(
                    Intent(context, People::class.java).putExtra(
                        "UserId",
                        list[position].data?.userId
                    )
                )

            } else if(notificationType.equals("COMMENT")){

                Comment_BottomSheet(list[position].data?.meta?.id!!).show((context as AppCompatActivity).supportFragmentManager, " ")

            } else if(notificationType.equals("FOLLOW_REQUEST")){

                context.startActivity(
                    Intent(context, List::class.java).putExtra("followRequests", true))

            } else if(notificationType.equals("USER_REPORT")){

            } else if(notificationType.equals("CHAT")){

                val tinyDB = TinyDB(context)
                var anotherUserID: Int = list.get(position).data?.userId!!
                var myUserID: Int = tinyDB.getString("id")!!.toInt()
                var chatRoom: String = ""

                if(anotherUserID < myUserID){
                    chatRoom = "u-" + anotherUserID + "-" + myUserID
                } else {
                    chatRoom = "u-" + myUserID + "-" + anotherUserID
                }

                val intent = Intent(context, ChatActivityNew::class.java)
                intent.putExtra("ChatRoom", chatRoom)
                intent.putExtra("UserID", anotherUserID.toString())
                intent.putExtra("UserName", list.get(position).data?.fullName)
                intent.putExtra("UserImage", Constants.BASE_IMAGE + list.get(position).data?.avatar)
                intent.putExtra("UserCountry", "NotUsedInChatActivityClass")
                context.startActivity(intent)

            } else if(notificationType.equals("ORDER")){

                context.startActivity(Intent(context, ArtistDashboard::class.java))

            } else if(notificationType.equals("PACK")){

                context.startActivity(
                    Intent(
                        context,
                        Doodle_Drawing::class.java
                    ).putExtra("fragment", Constants.fragment_approved).putExtra("Id", list[position].data?.meta?.id.toString())
                )
            }
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

    fun getLongToAgo(serverDate: String): String? {
        val userDateFormat: DateFormat = SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy")
        val dateFormatNeeded: DateFormat = SimpleDateFormat("MM/dd/yyyy HH:MM:SS")

        val f = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val d: Date = f.parse(serverDate)
        val createdAt = d.time

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

}