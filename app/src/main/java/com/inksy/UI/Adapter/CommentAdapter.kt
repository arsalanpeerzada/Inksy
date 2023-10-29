package com.inksy.UI.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inksy.Interfaces.iOnCommentClickListener
import com.inksy.Interfaces.iSendCommentReply
import com.inksy.Model.CommentDataModel
import com.inksy.R
import com.inksy.UI.Activities.People
import com.inksy.UI.Constants
import com.inksy.Utils.TinyDB
import java.io.Serializable

class CommentAdapter(
    var context: Context,
    var list: ArrayList<CommentDataModel>,
    var iOnCommentClickListener: iOnCommentClickListener,
    var creatorID: Int,
    var iSendCommentReply: iSendCommentReply
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {


    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        lateinit var more: ImageView
        lateinit var comment: TextView
        lateinit var like: TextView
        lateinit var reply_count: TextView
        lateinit var like_count: TextView
        lateinit var newComment: EditText
        lateinit var send: ImageView
        lateinit var name: TextView
        lateinit var desc: TextView
        lateinit var avatar: ImageView
        lateinit var rv_replies: RecyclerView
        lateinit var replySend: ImageView


        fun bind() {

            more = itemView.findViewById(R.id.more)
            comment = itemView.findViewById(R.id.comment)
            like = itemView.findViewById(R.id.like)
            reply_count = itemView.findViewById(R.id.comment_count)
            like_count = itemView.findViewById(R.id.like_count)
            newComment = itemView.findViewById(R.id.newcomment)
            send = itemView.findViewById(R.id.send)
            name = itemView.findViewById(R.id.name)
            desc = itemView.findViewById(R.id.text)
            avatar = itemView.findViewById(R.id.image)
            rv_replies = itemView.findViewById(R.id.rv_replies)
            replySend = itemView.findViewById(R.id.replySend)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind()
        holder.name.text = list[position].user?.fullName
        holder.desc.text = list[position].comment

        Glide.with(context).load(Constants.BASE_IMAGE + list[position].user?.avatar)
            .placeholder(R.drawable.ic_empty_user).into(holder.avatar)

        holder.like_count.text = list[position].likesCount!!.toString()
        holder.reply_count.text = list[position].replies.size.toString()


        if (list[position].replies.isNotEmpty()) {
            holder.reply_count.setTextColor(ContextCompat.getColor(context, R.color.appBlue))
            holder.comment.setTextColor(ContextCompat.getColor(context, R.color.appBlue))
        }

        if (list[position].isCommentLike == 1) {
            holder.like.setTextColor(context.resources.getColor(R.color.appBlue))
            holder.like_count.setTextColor(context.resources.getColor(R.color.appBlue))
        }

        holder.like.setOnClickListener() {

            if (list[position].isCommentLike == 0) {
                list[position].isCommentLike = 1
                holder.like.setTextColor(context.resources.getColor(R.color.appBlue))
                holder.like_count.setTextColor(context.resources.getColor(R.color.appBlue))
                holder.like_count.text = (list[position].likesCount?.plus(1)).toString()
            } else {
                list[position].isCommentLike = 0
                holder.like.setTextColor(context.resources.getColor(R.color.defaulttextcolor))
                holder.like_count.setTextColor(context.resources.getColor(R.color.defaulttextcolor))
                holder.like_count.text = (list[position].likesCount?.minus(1)).toString()
            }

            iOnCommentClickListener.onclick(list[position].id!!, "LIKE", 0)

        }
        holder.itemView.setOnClickListener() {

            var userid = TinyDB(context).getString("id")
            if (userid.toString() != list[position].user?.id.toString()) {
                var data = list[position].user
                context.startActivity(
                    Intent(context, People::class.java).putExtra(
                        "Data",
                        data as Serializable
                    )
                )
            } else {
                Toast.makeText(context, "You cannot visit your own profile", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        holder.comment.setOnClickListener() {
            holder.newComment.visibility = View.VISIBLE
            holder.send.visibility = View.VISIBLE
            holder.rv_replies.visibility = View.VISIBLE

            val data = list[position].replies

            val adapter = ReplyAdapter(
                context,
                data as ArrayList<CommentDataModel>,
                iOnCommentClickListener,
                creatorID,
                list[position].userId.toString(),
                position,
                holder.send,
                holder.newComment,
                holder.replySend,
                iSendCommentReply
            )
            holder.rv_replies.adapter = adapter

        }

        holder.send.setOnClickListener() {
//            holder.newComment.visibility = View.GONE
//            holder.send.visibility = View.GONE

            holder.send.isEnabled = false
            val comment = holder.newComment.text.toString()

            if (comment.isNotEmpty()) {
                iSendCommentReply.sendcommentReply(
                    "Comment",
                    journalId = list[position].journalId.toString(),
                    comment,
                    list[position].id.toString(),
                )
            }


        }
        holder.more.setOnClickListener {
            val contextWrapper = ContextThemeWrapper(context, R.style.popupMenuStyle)
            val popupMenu = PopupMenu(
                contextWrapper, holder.more
            )
            popupMenu.setForceShowIcon(true)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->

                when (item.itemId) {
                    R.id.Delete -> {
                        iOnCommentClickListener.onclick(list[position].id!!, "DELETE", 0)
                        return@OnMenuItemClickListener true
                    }
                    R.id.edit -> {
                        iOnCommentClickListener.onclick(position, "EDIT", 0)

                        return@OnMenuItemClickListener true
                    }
                    R.id.Report -> {
                        iOnCommentClickListener.onclick(list[position].id!!, "REPORT", 0)
                        return@OnMenuItemClickListener true
                    }
                    R.id.Block -> {
                        iOnCommentClickListener.onclick(list[position].userId!!, "BLOCK", 0)
                        return@OnMenuItemClickListener true
                    }
                    else -> false
                }


            })
            popupMenu.inflate(R.menu.comment_pop_up)
            popupMenu.show()

            val userid = TinyDB(context).getString("id")
            //    Toast.makeText(context, creatorID.toString(), Toast.LENGTH_SHORT).show()
            if (userid == list[position].userId.toString()) {
                popupMenu.menu.findItem(R.id.Report).isVisible = false
                popupMenu.menu.findItem(R.id.edit).isVisible = true
                popupMenu.menu.findItem(R.id.Delete).isVisible = true
                popupMenu.menu.findItem(R.id.Delete).isVisible = true
                popupMenu.menu.findItem(R.id.Block).isVisible = false
            } else if (creatorID.toString() == userid && userid == list[position].userId.toString()) {
                popupMenu.menu.findItem(R.id.Delete).isVisible = true
                popupMenu.menu.findItem(R.id.Report).isVisible = true
                popupMenu.menu.findItem(R.id.edit).isVisible = false
            } else if (creatorID.toString() == userid && userid != list[position].userId.toString()) {
                popupMenu.menu.findItem(R.id.Delete).isVisible = true
                popupMenu.menu.findItem(R.id.Report).isVisible = true
                popupMenu.menu.findItem(R.id.edit).isVisible = false
            } else {
                popupMenu.menu.findItem(R.id.Delete).isVisible = false
                popupMenu.menu.findItem(R.id.Report).isVisible = true
                popupMenu.menu.findItem(R.id.edit).isVisible = false
            }


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