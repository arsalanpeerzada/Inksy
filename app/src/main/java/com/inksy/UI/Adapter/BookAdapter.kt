package com.inksy.UI.Adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inksy.Interfaces.OnChangeStateClickListener
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Model.Journals
import com.inksy.R
import com.inksy.UI.Activities.ViewOnlyJournal
import com.inksy.UI.Constants
import com.inksy.Utils.TinyDB
import com.varunest.sparkbutton.SparkButton
import java.io.Serializable

class BookAdapter(
    var context: Context,
    var list: ArrayList<Journals>,
    var type: String,
    var ionclick: iOnClickListerner,
    var onLikeJournalClickListener: OnChangeStateClickListener
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {


    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var view: ImageView
        lateinit var like: SparkButton
        lateinit var comment: ImageView
        lateinit var imgrecent: ImageView
        lateinit var journalImage: ImageView
        lateinit var journalTitle: TextView
        lateinit var journalDesc: TextView

        lateinit var tvrecent: TextView
        lateinit var like_counter: TextView
        lateinit var comment_counter: TextView
        lateinit var imagelayout: RelativeLayout


        var like_count: Int = 0
        var comment_count: Int = 0
        fun bind() {
            tvrecent = itemView.findViewById(R.id.tvrecent)
            imgrecent = itemView.findViewById(R.id.imgrecent)
            view = itemView.findViewById(R.id.view)
            like = itemView.findViewById(R.id.like)
            comment = itemView.findViewById(R.id.comment)
            like_counter = itemView.findViewById(R.id.like_count)
            comment_counter = itemView.findViewById(R.id.comment_count)

            journalImage = itemView.findViewById(R.id.journalImage)
            journalDesc = itemView.findViewById(R.id.journalDesc)
            journalTitle = itemView.findViewById(R.id.journalText)
            imagelayout = itemView.findViewById(R.id.imagelayout)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()
            Glide.with(context).load(Constants.BASE_IMAGE + list[position].coverImage)
                .into(holder.journalImage)

            holder.journalTitle.text = list[position].title
            holder.journalDesc.text = list[position].description

            holder.like_counter.text = list[position].likesCount.toString()
            holder.comment_counter.text = list[position].commentsCount.toString()

            when (list[position].coverBc) {
                "blue" -> {
                    holder.view.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.journalBlue);
                    holder.journalImage.setBackgroundResource(R.color.journalBlue)
                    holder.imagelayout.setBackgroundResource(R.color.journalBlue)
                }

                "green" -> {
                    holder.view.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.journalGreen);
                    holder.journalImage.setBackgroundResource(R.color.journalGreen)
                    holder.imagelayout.setBackgroundResource(R.color.journalGreen)
                }

                "red" -> {
                    holder.view.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.journalRed);
                    holder.journalImage.setBackgroundResource(R.color.journalRed)
                    holder.imagelayout.setBackgroundResource(R.color.journalRed)
                }

                "purple" -> {
                    holder.view.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.journalPurple);
                    holder.journalImage.setBackgroundResource(R.color.journalPurple)
                    holder.imagelayout.setBackgroundResource(R.color.journalPurple)
                }
            }

        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

        if (position == 0) {
            holder.imgrecent.visibility = View.VISIBLE
            holder.tvrecent.visibility = View.VISIBLE
        }


        holder.like.setOnClickListener {

            if (list[position].isJournalLike == 0) {
                // holder.like_count++
                holder.like_counter.text = (list[position].likesCount?.plus(1)).toString()
                list[position].likesCount = (list[position].likesCount?.plus(1))
                list[position].isJournalLike = 1
                onLikeJournalClickListener.onStateChange(list[position]?.id!!, true, "Journal")
            } else if (list[position].isJournalLike == 1) {
                // holder.like_count--
                holder.like_counter.text = (list[position].likesCount?.minus(1)).toString()
                list[position].likesCount = (list[position].likesCount?.minus(1))
                list[position].isJournalLike = 0
                onLikeJournalClickListener.onStateChange(list[position].id!!, false, "Journal")
            }

            Log.d("-----", holder.like_count.toString())
        }
        holder.comment.setOnClickListener {
            ionclick.onclick(list[position].id!!)
        }

        holder.itemView.setOnClickListener {

            val tinyDB = TinyDB(context)
            val createdId = tinyDB.getString("id")

            if (list[position].createdBy.toString() == createdId) {
                type = Constants.person
            }


            context.startActivity(
                Intent(context, ViewOnlyJournal::class.java)
                    .putExtra(Constants.journalType, type)
                    .putExtra("data", list[position] as Serializable)
            )
        }
    }

    override fun getItemCount(): Int {

        if (list.size > 10) {
            return 10
        } else {
            return list.size
        }


    }

//    fun setFilter(countryModels: List<Get_Doctors_Model.myList>, _searchText: String) {
//        doc_list = ArrayList<Get_Doctors_Model.myList>()
//        doc_list.addAll(countryModels)
//        this.searchText = _searchText
//        notifyDataSetChanged()
//    }


}