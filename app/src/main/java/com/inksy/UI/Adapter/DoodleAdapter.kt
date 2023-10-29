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
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.R
import com.inksy.UI.Activities.Doodle_Drawing
import com.inksy.UI.Constants

class DoodleAdapter(
    var context: Context,
    var list: ArrayList<DoodlePack>,
    var type: String,
    var iOnClickListerner: iOnClickListerner
) : RecyclerView.Adapter<DoodleAdapter.ViewHolder>() {
    var searchText = ""

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var image: ImageView
        lateinit var title: TextView
        lateinit var price_Tag: TextView
        lateinit var buyButton: TextView
        fun bind() {
            image = itemView.findViewById(R.id.image)
            title = itemView.findViewById(R.id.textView5)
            price_Tag = itemView.findViewById(R.id.myprice)
            buyButton = itemView.findViewById(R.id.buyButton)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doodle, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()


            if (type == "Feature") {
                var mylist = list as ArrayList<DoodlePack>
                setup1(holder, mylist, position)

            } else if ("Purchased" == type) {
                holder.buyButton.text = "Purchased"
                var mylist = list as ArrayList<DoodlePack>
                setup(holder, mylist, position)
            } else {
                var mylist = list as ArrayList<DoodlePack>
                setup(holder, mylist, position)

            }
            if (list[position].isPurchased == 1) {
                holder.buyButton.text = "Purchased"
            }
            holder.title.text = list[position].packTitle
            val number2digits: String = String.format("%.2f", list[position].price)
            holder.price_Tag.text = "$ $number2digits"
            Glide.with(context).load(Constants.BASE_IMAGE + list[position].coverImage)
                .into(holder.image)


//            holder.buyButton.setOnClickListener() {
//                if (type != "Purchased") {
//                    iOnClickListerner.onclick(position)
//                }
//            }


        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

        holder.itemView.setOnClickListener {

            if (type != "Purchased") {
                context.startActivity(
                    Intent(context, Doodle_Drawing::class.java)
                        .putExtra("fromAdapter", true).putExtra("Id", list[position].id.toString())
                        .putExtra("fragment", "NEW")
                )
            } else {
                context.startActivity(
                    Intent(context, Doodle_Drawing::class.java)
                        .putExtra("fromAdapter", false).putExtra("Id", list[position].id.toString())
                        .putExtra("fragment", "NEW")
                )
            }


        }
    }

    private fun setup(holder: ViewHolder, mylist: ArrayList<DoodlePack>, position: Int) {

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


    fun setFilter(countryModels: ArrayList<DoodlePack>, _searchText: String) {
        list = ArrayList<DoodlePack>()
        list.addAll(countryModels)
        this.searchText = _searchText
        notifyDataSetChanged()
    }

//    fun setFilter(countryModels: List<Get_Doctors_Model.myList>, _searchText: String) {
//        doc_list = ArrayList<Get_Doctors_Model.myList>()
//        doc_list.addAll(countryModels)
//        this.searchText = _searchText
//        notifyDataSetChanged()
//    }


}