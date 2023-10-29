package com.inksy.UI.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inksy.R
import com.inksy.UI.Activities.AmountReceived
import com.inksy.UI.Constants

class DashboardAdapter(
    var context: Context,
    var list_logo: Array<Int>,
    var list_name: Array<String>,
    var value: Array<String>
) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var name: TextView
        lateinit var value: TextView
        lateinit var logo: ImageView
        fun bind() {
            name = itemView.findViewById(R.id.name)
            logo = itemView.findViewById(R.id.logo)
            value = itemView.findViewById(R.id.value)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dashboard, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()
            Glide.with(context).load(list_logo[position]).into(holder.logo)
            holder.name.text = list_name[position]
            holder.value.text = value[position].toString()


        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }


        holder.itemView.setOnClickListener {

            if (list_name[position].contains(Constants.amountPaid))
                context.startActivity(
                    Intent(context, AmountReceived::class.java)
                )


        }
    }

    override fun getItemCount(): Int {
        return list_name.size
    }

//    fun setFilter(countryModels: List<Get_Doctors_Model.myList>, _searchText: String) {
//        doc_list = ArrayList<Get_Doctors_Model.myList>()
//        doc_list.addAll(countryModels)
//        this.searchText = _searchText
//        notifyDataSetChanged()
//    }


}