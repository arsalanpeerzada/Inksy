package com.inksy.UI.Adapter

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.R


class FontAdapter(
    var context: Context,
    var iclick: iOnClickListerner
) : RecyclerView.Adapter<FontAdapter.ViewHolder>() {

    private var row_index = 100

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var tv: TextView
        lateinit var layout: RelativeLayout

        fun bind() {
            tv = itemView.findViewById(R.id.font_picker_view)
            layout = itemView.findViewById(R.id.cardView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_select_font, parent, false)
        return ViewHolder(v)

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind()

        when (holder.absoluteAdapterPosition) {
            0 -> {
                var typeface: Typeface = ResourcesCompat.getFont(context, R.font.sfmedium)!!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    typeface = ResourcesCompat.getFont(context, R.font.fuzzybubbles_regular)!!
                }
                holder.tv.typeface = typeface
                holder.tv.setTextColor(ContextCompat.getColor(context,R.color.black))
            }
            1 -> {
                val typeface = ResourcesCompat.getFont(context, R.font.bakbak)
                holder.tv.typeface = typeface
                holder.tv.setTextColor(ContextCompat.getColor(context,R.color.black))
            }
            2 -> {
                val typeface = ResourcesCompat.getFont(context, R.font.inconsolata_regular)
                holder.tv.typeface = typeface
                holder.tv.setTextColor(ContextCompat.getColor(context,R.color.black))
            }
            3 -> {
                val typeface =
                    ResourcesCompat.getFont(context, R.font.montserrat_regular)
                holder.tv.typeface = typeface
                holder.tv.setTextColor(ContextCompat.getColor(context,R.color.black))
            }
            4 -> {
                val typeface = ResourcesCompat.getFont(context, R.font.sfmedium)
                holder.tv.typeface = typeface
                holder.tv.setTextColor(ContextCompat.getColor(context,R.color.black))
            }
            5 -> {
                val typeface = ResourcesCompat.getFont(context, R.font.ls_regular)
                holder.tv.typeface = typeface
                holder.tv.setTextColor(ContextCompat.getColor(context,R.color.black))
            }
            6 ->{
                val typeface = ResourcesCompat.getFont(context, R.font.homemadeapple)
                holder.tv.typeface = typeface
                holder.tv.setTextColor(ContextCompat.getColor(context,R.color.black))
            }
            7 ->{
                val typeface = ResourcesCompat.getFont(context, R.font.playfair)
                holder.tv.typeface = typeface
                holder.tv.setTextColor(ContextCompat.getColor(context,R.color.black))
            }
            8 ->{
                val typeface = ResourcesCompat.getFont(context, R.font.justanotherhand)
                holder.tv.typeface = typeface
                holder.tv.setTextColor(ContextCompat.getColor(context,R.color.black))
            }
            9 ->{
                val typeface = ResourcesCompat.getFont(context, R.font.milkshake)
                holder.tv.typeface = typeface
                holder.tv.setTextColor(ContextCompat.getColor(context,R.color.black))
            }
            10 ->{
                val typeface = ResourcesCompat.getFont(context, R.font.alexbrush)
                holder.tv.typeface = typeface
                holder.tv.setTextColor(ContextCompat.getColor(context,R.color.black))
            }
        }


        if (row_index == holder.absoluteAdapterPosition) {
            holder.layout.backgroundTintList =
                ContextCompat.getColorStateList(context,R.color.black)
            holder.tv.setTextColor( ContextCompat.getColor(context,R.color.white))
        } else {
            holder.layout.backgroundTintList =
                ContextCompat.getColorStateList(context,R.color.line_grey_trans)
            holder.tv.setTextColor( ContextCompat.getColor(context,R.color.black))
        }

        holder.itemView.setOnClickListener {

            var typeface = 0

            if (row_index == position) {
                typeface = R.font.sfmedium
                row_index = 100
            } else {
                when (position) {
                    0 -> {
                        typeface = R.font.fuzzybubbles_regular
                        row_index = holder.absoluteAdapterPosition

                    }
                    1 -> {
                        typeface = R.font.bakbak
                        row_index = holder.absoluteAdapterPosition

                    }
                    2 -> {
                        typeface = R.font.inconsolata_regular
                        row_index = holder.absoluteAdapterPosition

                    }
                    3 -> {
                        typeface = R.font.montserrat_regular
                        row_index = holder.absoluteAdapterPosition

                    }
                    4 -> {
                        typeface = R.font.sfmedium
                        row_index = holder.absoluteAdapterPosition

                    }
                    5 -> {
                        typeface = R.font.ls_regular
                        row_index = holder.absoluteAdapterPosition

                    }
                    6 -> {
                        typeface = R.font.homemadeapple
                        row_index = holder.absoluteAdapterPosition

                    }
                    7 -> {
                        typeface = R.font.playfair
                        row_index = holder.absoluteAdapterPosition

                    }
                    8 -> {
                        typeface = R.font.justanotherhand
                        row_index = holder.absoluteAdapterPosition

                    }
                    9 -> {
                        typeface = R.font.milkshake
                        row_index = holder.absoluteAdapterPosition

                    }
                    10 -> {
                        typeface = R.font.alexbrush
                        row_index = holder.absoluteAdapterPosition

                    }
                }
            }
            iclick.onclick(typeface)
        }
    }


    fun delete(){
        Toast.makeText(context, "clear selection", Toast.LENGTH_SHORT).show()
    }


    override fun getItemCount(): Int {
        return 11
    }

    fun reset() {

    }


}