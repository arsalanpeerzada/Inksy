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
import com.example.example.DoodlePack
import com.inksy.R
import com.inksy.UI.Activities.Doodle_Drawing
import com.inksy.UI.Constants

class ArtworkAdapter(
    var context: Context,
    var list: ArrayList<DoodlePack>,
    var type: String
) : RecyclerView.Adapter<ArtworkAdapter.ViewHolder>() {

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var buynow: TextView
        lateinit var image: ImageView
        lateinit var title: TextView
        lateinit var price_Tag: TextView
        fun bind() {
            image = itemView.findViewById(R.id.image)
            title = itemView.findViewById(R.id.title)
            buynow = itemView.findViewById(R.id.buynow)
            price_Tag = itemView.findViewById(R.id.myprice)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artwork, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()

            holder.title.text = list[position].packTitle
            val number2digits: String = String.format("%.2f", list[position].price)
            holder.price_Tag.text = number2digits
            Glide.with(context).load(Constants.BASE_IMAGE + list[position].coverImage)
                .into(holder.image)

//            if (type == "Feature") {
//                var mylist = list as ArrayList<DoodlePack>
//
//                setup1(holder, mylist, position)
//            } else {
//                var mylist = list as ArrayList<DoodlePack>
//                setup(holder, mylist, position)
//            }

            if (list[position].isPurchased == 1) {
                holder.buynow.text = "Purchased"
            }
        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }
        holder.itemView.setOnClickListener {
            context.startActivity(
                Intent(context, Doodle_Drawing::class.java)
                    .putExtra("fromAdapter", true).putExtra("Id", list[position].id.toString())
            )
        }
    }
    private fun setup(holder: ViewHolder, mylist: ArrayList<DoodlePack>, position: Int) {
        holder.title.text = mylist[position].packTitle
        val number2digits: String = String.format("%.2f", mylist[position].price)
        holder.price_Tag.text = number2digits
        Glide.with(context).load(Constants.BASE_IMAGE + mylist[position].coverImage)
            .into(holder.image)
    }

    private fun setup1(holder: ViewHolder, mylist: ArrayList<DoodlePack>, position: Int) {
        holder.title.text = mylist[position].packTitle
        val number2digits: String = String.format("%.2f", mylist[position].price)
        holder.price_Tag.text = number2digits
        Glide.with(context).load(Constants.BASE_IMAGE + mylist[position].coverImage)
            .into(holder.image)
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