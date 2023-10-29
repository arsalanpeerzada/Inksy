package com.inksy.UI.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.example.Doodles
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.R
import com.inksy.UI.Constants
import com.inksy.UI.Dialogs.ImageViewDialog

class DoodleViewAdapter(
    var context: Context,
    var list: ArrayList<Doodles>,
    var iOnClickListerner: iOnClickListerner,
    var lockcheck: Boolean
) : RecyclerView.Adapter<DoodleViewAdapter.ViewHolder>() {

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var imageView: ImageView
        lateinit var info: ImageView
        lateinit var lock: ImageView
        fun bind() {
            imageView = itemView.findViewById(R.id.image)
            info = itemView.findViewById(R.id.info)
            lock = itemView.findViewById(R.id.lock)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doodleview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()
            Glide.with(context).load(Constants.BASE_IMAGE + list[position].doodleImage)
                .into(holder.imageView)


        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

        if (list[position].notes.size > 0) {

            if (list[position].isActive == 0) {
                holder.info.visibility = View.VISIBLE
            } else {
                holder.info.visibility = View.GONE
            }
        } else {
            holder.info.visibility = View.GONE
        }

        holder.info.setOnClickListener {

            iOnClickListerner.onclick(position)
        }

        if (lockcheck) {

        } else {
            holder.lock.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {

            if (lockcheck) {
                Toast.makeText(
                    context,
                    "Please purchase!! And avail the opportunity to use this pack in journal",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                ImageViewDialog(
                    context,
                    list[position].doodleImage!!,
                    lockcheck
                ).show()
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