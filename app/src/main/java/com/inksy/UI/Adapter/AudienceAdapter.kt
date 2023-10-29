package com.inksy.UI.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inksy.Database.Entities.SelectedAudience
import com.inksy.Interfaces.OnChangeStateClickListener
import com.inksy.Model.UserModel
import com.inksy.R
import com.inksy.UI.Activities.ShareJournalActivity
import com.inksy.UI.Constants

class AudienceAdapter(
    var context: Context,
    var list: ArrayList<UserModel>,
    var selectedAudience: List<SelectedAudience>,
    var onChangeStateClickListener: OnChangeStateClickListener
) : RecyclerView.Adapter<AudienceAdapter.ViewHolder>() {

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var tv: TextView
        lateinit var iv: ImageView
        lateinit var cb: CheckBox
        fun bind() {
            tv = itemView.findViewById(R.id.name)
            iv = itemView.findViewById(R.id.audience_Image)
            cb = itemView.findViewById(R.id.audience_cb)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_audience, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.bind()
            holder.tv.text = list[position].fullName
            Glide.with(context).load(Constants.BASE_IMAGE + list[position].avatar).into(holder.iv)

            for (i in 0 until selectedAudience.size) {
                if (list[position].id.toString() == selectedAudience[i].userID) {
                    holder.cb.isChecked = true
                }
            }

        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

/*
        holder.cb.setOnCheckedChangeListener { buttonView, isChecked ->

            if (holder.cb.isChecked) {
                onChangeStateClickListener.onStateChange(position, true, "")
            } else {
                onChangeStateClickListener.onStateChange(position, false, "")
            }

        }
*/

        holder.cb.setOnClickListener {
            if(context is ShareJournalActivity){
                if(ShareJournalActivity().getListSize() == 5 || ShareJournalActivity().getListSize() > 5){
                    Toast.makeText(context.applicationContext,
                        "You can not select more than 5 users", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            if (holder.cb.isChecked) {
                holder.cb.isChecked = true
                onChangeStateClickListener.onStateChange(position, true, "")
            } else {
                holder.cb.isChecked = false
                onChangeStateClickListener.onStateChange(position, false, "")
            }

        }

/*
        holder.iv.setOnClickListener {
            if (holder.cb.isChecked == false) {
                holder.cb.isChecked = true
                onChangeStateClickListener.onStateChange(position, true, "")
            } else {
                holder.cb.isChecked = false
                onChangeStateClickListener.onStateChange(position, false, "")
            }


        }

        holder.tv.setOnClickListener {
            if (holder.cb.isChecked == false) {
                holder.cb.isChecked = true
                onChangeStateClickListener.onStateChange(position, true, "")
            } else {
                holder.cb.isChecked = false
                onChangeStateClickListener.onStateChange(position, false, "")
            }

        }
*/

    }

    override fun getItemCount(): Int {
        return list.size
    }


}