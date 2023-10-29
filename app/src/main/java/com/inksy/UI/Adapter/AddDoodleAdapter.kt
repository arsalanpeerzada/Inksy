package com.inksy.UI.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.R

class AddDoodleAdapter(
    var context: Context,
    var list: ArrayList<String>,
    var type: String,
    var iOnClickListerner: iOnClickListerner
) : RecyclerView.Adapter<AddDoodleAdapter.ViewHolder>() {
    var searchText = ""

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var image: ImageView
        lateinit var count: TextView
        fun bind() {
            image = itemView.findViewById(R.id.item)
            count = itemView.findViewById(R.id.count)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_packtitle, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()
            var pos = position
            if (position < 8) {
                pos = position + 2
                holder.count.text = "0$pos"
            } else {
                pos = position + 2
                holder.count.text = "$pos"
            }

            if (!list[position].isNullOrEmpty()) {
                Glide.with(context).load(list[position])
                    .placeholder(R.drawable.add_doodle).into(holder.image)
            }


        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

        holder.itemView.setOnClickListener() {
            iOnClickListerner.onclick(position)
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }


}