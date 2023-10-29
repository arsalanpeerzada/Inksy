package com.inksy.UI.Adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.inksy.Interfaces.PopUpOnClickListerner
import com.inksy.Model.Styles
import com.inksy.R

class BulletAdapter(
    var context: Context,
    var list: ArrayList<Styles>,
    var type: Int,
    var fragment: String,
    var popUpOnClickListerner: PopUpOnClickListerner
) : RecyclerView.Adapter<BulletAdapter.ViewHolder>() {

    companion object {
        private const val TYPE_BULLETS = 0
        private const val TYPE_CHECKBOX = 1
        private const val TYPE_NUMBERLIST = 2
        private const val TYPE_ALPHALIST = 3
    }

    lateinit var v: View

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var tv: TextView
        lateinit var et: TextView
        lateinit var checkBox: CheckBox
        lateinit var iv: ImageView

        fun bind_bullets() {

            iv = itemView.findViewById(R.id.imageView3)
            et = itemView.findViewById(R.id.item_bullet)
        }

        fun bind_checkBox() {

            checkBox = itemView.findViewById(R.id.checkbox)
            et = itemView.findViewById(R.id.item_checkbox)
        }

        private fun bind_number_List() {

            tv = itemView.findViewById(R.id.imageView3)
            et = itemView.findViewById(R.id.item_number)
        }

        fun bind_alpha_list() {

            tv = itemView.findViewById(R.id.imageView3)
            et = itemView.findViewById(R.id.item_alphalist)
        }

        fun bind(type: Int) {
            when (type) {
                0 -> bind_bullets()
                1 -> bind_checkBox()
                2 -> bind_number_List()
                3 -> bind_alpha_list()
                else -> bind_bullets()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = when (viewType) {
            TYPE_BULLETS -> R.layout.item_bullettext
            TYPE_CHECKBOX -> R.layout.item_checkboxtext
            TYPE_NUMBERLIST -> R.layout.item_number_list
            TYPE_ALPHALIST -> R.layout.item_alpha_list
            else -> throw IllegalArgumentException("Invalid type")
        }

        v = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
        return ViewHolder(v)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            holder.bind(type)
            when (type) {
                TYPE_BULLETS -> {

                }
                TYPE_CHECKBOX -> {
                }
                TYPE_NUMBERLIST -> {
                    var index = position+1
                    holder.tv.text = "$index)"
                }
                TYPE_ALPHALIST -> {
                    var char: Char = 'A'
                    char += (position)
                    holder.tv.text = "$char)"
                }
            }
            var finaldata = checkTextStyle(list[position])
            holder.et.text = Html.fromHtml(finaldata)
            holder.et.setTextColor((list[position].textColor!!))
            holder.et.textSize = list[position].fontsize?.toFloat()!!
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                holder.et.typeface = context.resources.getFont(list[position].typeface!!)
            }

        } catch (e: NullPointerException) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

        holder.itemView.setOnLongClickListener {
            popUpOnClickListerner.popuponclick(type.toString(), holder.itemView,list,position)
            true
        }


    }

    fun checkTextStyle(styles: Styles): String {
        var data = styles.data

        if (styles.isBold!!) {
            // holder.et.setTextAppearance(R.style.boldText)
            data = "<b>$data</b>"
        }
        if (styles.isItalic!!) {
            // holder.et.setTextAppearance(R.style.italicText)
            data = "<i>$data</i>"
        }
        if (styles.isunderline!!) {
            data = "<u>$data</u>"
        }
        if (styles.isstrike!!) {
            data = "<s>$data</s>"
        }

        return data!!
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (type) {
            0 -> TYPE_BULLETS
            1 -> TYPE_CHECKBOX
            2 -> TYPE_NUMBERLIST
            3 -> TYPE_ALPHALIST
            else -> TYPE_BULLETS
        }
    }

//    fun setFilter(countryModels: List<Get_Doctors_Model.myList>, _searchText: String) {
//        doc_list = ArrayList<Get_Doctors_Model.myList>()
//        doc_list.addAll(countryModels)
//        this.searchText = _searchText
//        notifyDataSetChanged()
//    }


}