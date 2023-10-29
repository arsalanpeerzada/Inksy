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
import com.inksy.Model.TransactionModel
import com.inksy.R
import com.inksy.UI.Constants

class AmountReceivedAdapter(
    var context: Context,
    var list: ArrayList<TransactionModel>,
) : RecyclerView.Adapter<AmountReceivedAdapter.ViewHolder>() {

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var image: ImageView
        lateinit var name: TextView
        lateinit var text: TextView

        fun bind() {
            image = itemView.findViewById(R.id.image)
            name = itemView.findViewById(R.id.name)
            text = itemView.findViewById(R.id.text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_amoun_received, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()

            holder.name.text = list[position].title
            val number2digits: String = String.format("%.2f", list[position].amount)
            holder.text.text = number2digits

            Glide.with(context).load(Constants.BASE_IMAGE + list[position].transactionImage)
                .placeholder(R.drawable.ic_empty_user).into(holder.image)


        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }
        holder.itemView.setOnClickListener {

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