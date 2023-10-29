package com.inksy.UI.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Model.JournalTemplateModel
import com.inksy.R
import com.inksy.UI.Constants

class TemplateAdapter(
    var context: Context,
    var list: ArrayList<JournalTemplateModel>,
    var onClickListerner: iOnClickListerner
) : RecyclerView.Adapter<TemplateAdapter.ViewHolder>() {

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var templateImage: ImageView
        fun bind() {

            templateImage = itemView.findViewById(R.id.templateImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_template, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()

            Glide.with(context).load(Constants.BASE_IMAGE + list[position].templateImage)
                .into(holder.templateImage)


        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

        holder.itemView.setOnClickListener {
            onClickListerner.onclick(position)
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