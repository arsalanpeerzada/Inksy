package com.inksy.UI.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inksy.Interfaces.OnChangeStateClickListener
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Model.Categories
import com.inksy.Model.Journals
import com.inksy.R
import com.inksy.UI.Activities.ViewAll
import com.inksy.UI.Constants
import java.io.Serializable

class CategoriesAdapter(
    var context: Context,
    var categorieslist: ArrayList<Categories>,
    var journallist: ArrayList<Journals>,
    var type: String,
    var iOnClickListerner: iOnClickListerner,
    var onChangeStateClickListener: OnChangeStateClickListener,


    ) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>(), iOnClickListerner,
    OnChangeStateClickListener {
    var searchText = ""
    var list = ArrayList<Journals>()

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var title: TextView
        lateinit var recyclerview: RecyclerView
        lateinit var seeAll: TextView
        lateinit var view: View

        fun bind() {
            title = itemView.findViewById(R.id.title)
            recyclerview = itemView.findViewById(R.id.rv)
            seeAll = itemView.findViewById(R.id.seeall)
            view = itemView.findViewById(R.id.view12)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categories, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind()
        holder.title.text = categorieslist[position].categoryName
        val list = ArrayList<Journals>()
        for (i in 0 until journallist.size) {
            if (categorieslist[position].id == journallist[i].categoryId) {

                list.add(journallist[i])

                holder.recyclerview.adapter =
                    BookAdapter(context, list, "", this, this)
            }
        }

        holder.seeAll.setOnClickListener {
            context.startActivity(
                Intent(context, ViewAll::class.java).putExtra(
                    Constants.activity,
                    Constants.sub_journalViewAll
                ).putExtra("Data", true)
                    .putExtra("List", list as Serializable)
            )
        }

        if (list.size == 0) {
            holder.recyclerview.visibility = View.GONE
            holder.title.visibility = View.GONE
            holder.seeAll.visibility = View.GONE
            holder.view.visibility = View.GONE
        }


    }


    override fun getItemCount(): Int {
        return categorieslist.size
    }

    override fun onclick(position: Int) {
        super.onclick(position)

        iOnClickListerner.onclick(position)
    }

    override fun onStateChange(id: Int, like: Boolean, type: String) {
        super.onStateChange(id, like, type)

        onChangeStateClickListener.onStateChange(id, like, type)
    }


}