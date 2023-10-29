package com.inksy.UI.Adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.example.DoodlePack
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.R
import com.inksy.UI.Constants
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DashboardPendingAdapter(
    var context: Context,
    var doodlePendinglist: ArrayList<DoodlePack>,
    private var idashboardApproved: iOnClickListerner
) : RecyclerView.Adapter<DashboardPendingAdapter.ViewHolder>() {

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var image: ImageView
        lateinit var title: TextView
        lateinit var size: TextView
        lateinit var date: TextView


        fun bind() {
            image = itemView.findViewById(R.id.Image)
            title = itemView.findViewById(R.id.packtitle)
            size = itemView.findViewById(R.id.packsize)
            date = itemView.findViewById(R.id.date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dashboardpending, parent, false)
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind()

            holder.title.text = doodlePendinglist[position].packTitle
            holder.size.text = "Pack of Doodle"

            Glide.with(context).load(Constants.BASE_IMAGE + doodlePendinglist[position].coverImage)
                .into(holder.image)

            var stf = doodlePendinglist[position].updatedAt
            var date = OffsetDateTime.parse(stf).format(
                DateTimeFormatter.ofPattern(
                    "MMM d uuuu",
                    Locale.ENGLISH
                )
            );

            holder.date.text = date


            holder.itemView.setOnClickListener() {
                idashboardApproved.onclick(doodlePendinglist[position]?.id!!)
            }
        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return doodlePendinglist.size
    }


}