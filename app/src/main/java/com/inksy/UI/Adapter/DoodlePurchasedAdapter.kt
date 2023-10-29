package com.inksy.UI.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inksy.Database.Entities.PurchasedDoodles
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.R
import com.inksy.UI.Constants

class DoodlePurchasedAdapter(
    var context: Context,
    var list: ArrayList<PurchasedDoodles>,
    var iOnClickListerner: iOnClickListerner
) : RecyclerView.Adapter<DoodlePurchasedAdapter.ViewHolder>() {

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
            Glide.with(context).load(Constants.BASE_IMAGE + list[position].doodle_image)
                .into(holder.imageView)

            holder.info.visibility = View.GONE
        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

        holder.lock.visibility = View.GONE

//        if (list[position].notes.size > 0)
//            holder.info.visibility = View.VISIBLE
//        else{
//            holder.info.visibility = View.GONE
//        }

//        holder.info.setOnClickListener {
//
//            iOnClickListerner.onclick(position)
//        }

        holder.itemView.setOnClickListener {
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