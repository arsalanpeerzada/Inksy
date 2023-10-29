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
import com.inksy.Interfaces.OnChangeStateClickListener
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Model.UserModel
import com.inksy.R
import com.inksy.UI.Constants

class UsersListAdapter(
    var context: Context,
    var list: ArrayList<UserModel>,
    var iOnClickListerner: iOnClickListerner,
    var followRequest: Boolean,
    var onChangeStateClickListener: OnChangeStateClickListener
) : RecyclerView.Adapter<UsersListAdapter.ViewHolder>() {

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var tv: TextView
        lateinit var bio: TextView
        lateinit var avatar: ImageView
        lateinit var block: ImageView
        lateinit var accept: ImageView
        lateinit var reject: ImageView
        fun bind() {

            tv = itemView.findViewById(R.id.name)
            bio = itemView.findViewById(R.id.bio)
            avatar = itemView.findViewById(R.id.avatar)
            block = itemView.findViewById(R.id.unblock)
            accept = itemView.findViewById(R.id.accept)
            reject = itemView.findViewById(R.id.reject)
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
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_empty_user))
                .into(holder.avatar)

            if (followRequest) {
                holder.block.visibility = View.GONE

                holder.accept.visibility = View.VISIBLE
                holder.reject.visibility = View.VISIBLE
            } else {
                holder.block.visibility = View.VISIBLE
                holder.accept.visibility = View.GONE
                holder.reject.visibility = View.GONE
            }

        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

        holder.block.setOnClickListener {

            if (!followRequest) {
                iOnClickListerner.onclick(position)
            }

        }

        holder.accept.setOnClickListener {
            onChangeStateClickListener.onStateChange(list[position].id!!, true, "")
        }
        holder.reject.setOnClickListener {
            onChangeStateClickListener.onStateChange(list[position].id!!, false, "")
        }

        holder.itemView.setOnClickListener() {
            if (followRequest) {
                iOnClickListerner.onclick(position)
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


}