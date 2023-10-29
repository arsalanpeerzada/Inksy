package com.inksy.UI.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Model.UserModel
import com.inksy.R
import com.inksy.UI.Constants

class BlockedUsersAdapter(
    var context: Context,
    var list: ArrayList<UserModel>,
    var iOnClickListerner: iOnClickListerner
) : RecyclerView.Adapter<BlockedUsersAdapter.ViewHolder>() {

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var tv: TextView
        lateinit var bio: TextView
        lateinit var avatar: ImageView
        lateinit var block: ImageView
        fun bind() {

            tv = itemView.findViewById(R.id.name)
            bio = itemView.findViewById(R.id.bio)
            avatar = itemView.findViewById(R.id.avatar)
            block = itemView.findViewById(R.id.unblock)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()
            holder.tv.text = list[position].fullName
            holder.bio.text = list[position].bio

            Glide.with(context).load(Constants.BASE_IMAGE + list[position].avatar)
                .placeholder(ContextCompat.getDrawable(context, R.drawable.avatar_placeholder))
                .into(holder.avatar)
        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

        holder.block.setOnClickListener {
            iOnClickListerner.onclick(position)
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


}