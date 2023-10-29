package com.inksy.UI.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.inksy.R

class PackAdapter(
    var context: Context
) : RecyclerView.Adapter<PackAdapter.ViewHolder>() {


    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        //  lateinit var imageview: ImageView
        lateinit var count: TextView

        fun bind() {

            //  imageview = itemView.findViewById(R.id.image)
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


        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }


        holder.itemView.setOnClickListener {
//            context.startActivity(
//                Intent(context, OkaDoc_Detail::class.java)
//                    .putExtra("name", doc_list[position].fullName)
//                    .putExtra("designation", doc_list[position].jobTitle)
//                    .putExtra("image", doc_list[position].profilePicture)
//                    .putExtra("role", doc_list[position].jobRole)
//                    .putExtra("function", doc_list[position].jobFuction)
//
//
//            )


        }
    }

    override fun getItemCount(): Int {
        return 20
    }

//    fun setFilter(countryModels: List<Get_Doctors_Model.myList>, _searchText: String) {
//        doc_list = ArrayList<Get_Doctors_Model.myList>()
//        doc_list.addAll(countryModels)
//        this.searchText = _searchText
//        notifyDataSetChanged()
//    }


}