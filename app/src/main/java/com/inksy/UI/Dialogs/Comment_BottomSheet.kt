package com.inksy.UI.Dialogs

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inksy.Interfaces.iOnCommentClickListener
import com.inksy.Interfaces.iSendCommentReply
import com.inksy.Model.CommentDataModel
import com.inksy.Model.CommentsDataParent
import com.inksy.Remote.Status
import com.inksy.UI.Adapter.CommentAdapter
import com.inksy.UI.ViewModel.JournalView
import com.inksy.UI.ViewModel.PeopleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentCommentBottomSheetBinding

class Comment_BottomSheet(id: Int) : BottomSheetDialogFragment(), iOnCommentClickListener,
    SwipeRefreshLayout.OnRefreshListener, iSendCommentReply {

    lateinit var journalView: JournalView
    lateinit var peopleView: PeopleView
    lateinit var binding: FragmentCommentBottomSheetBinding
    var journalid = id
    lateinit var tiny: TinyDB
//    lateinit var refreshLayout: SwipeRefreshLayout
    var list: ArrayList<CommentDataModel> = ArrayList()
    var token: String = ""
    var data: CommentsDataParent = CommentsDataParent()
    var position = 0
    var commentEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommentBottomSheetBinding.inflate(layoutInflater)

        journalView = ViewModelProvider(requireActivity())[JournalView::class.java]
        peopleView = ViewModelProvider(requireActivity())[PeopleView::class.java]
        journalView.init()
        peopleView.init()
        tiny = TinyDB(context)
        token = tiny.getString("token").toString()

        getCommentsData(token)

        binding.ivBack.setOnClickListener {
            dismiss()
        }

        binding.imgSend.setOnClickListener() {
            if (binding.etMessage.text.toString().isNullOrEmpty()) {

            } else {

                if (commentEdit) {
                    val commentid = list[position].id
                    val journalId = list[position].journalId

                    updateComment(journalId, commentid, binding.etMessage.text.toString(), token)
                    position = 0
                    commentEdit = false
                    binding.etMessage.text.clear()
                } else {
                    sendComment(journalid, binding.etMessage.text.toString(), token)
                    binding.etMessage.text.clear()
                }
            }

        }

        getCommentsData(token)
//        refreshLayout = binding.swipe

//        refreshLayout.setOnRefreshListener(this)
//        refreshLayout.post(Runnable {
//            refreshLayout.setRefreshing(true)
//            refreshLayout.isRefreshing = false
//            // Fetching data from server
//
//        })

        return binding.root
    }

    private fun sendComment(_journalid: Int, message: String, token: String) {
        journalView.sendComment(_journalid, message, token)?.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        it?.data?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Status.SUCCESS -> {

                    Toast.makeText(
                        requireContext(),
                        it?.data?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    refresh()
                }
            }
        }
    }

    private fun sendCommentReply(
        _journalid: Int,
        message: String,
        commentId: String,
        token: String
    ) {
        journalView.sendComment(_journalid, message, commentId, token)?.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        it?.data?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Status.SUCCESS -> {

                    Toast.makeText(
                        requireContext(),
                        it?.data?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    refresh()
                }
            }
        }
    }

    fun getCommentsData(token: String) {
        journalView.getAllComments(journalid, token)?.observe(requireActivity()) {
            // refreshLayout.isRefreshing = false
            when (it.status) {
                Status.SUCCESS -> {

                    val _data = it?.data?.data?.get(0)!!
                    list = it.data.data?.get(0)?.comments as ArrayList<CommentDataModel>
                    if (list.size > 0)
                        binding.commentList.adapter =
                            CommentAdapter(requireContext(), list, this, _data.createdBy!!, this)
                    else Toast.makeText(requireContext(), "No Comments Found", Toast.LENGTH_SHORT)
                        .show()
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }
    }

    override fun onclick(id: Int, action: String, replyPosition: Int) {
        super.onclick(id, action, replyPosition)

        if (action == "LIKE") {
            likeComment(id)
        } else if (action == "REPORT") {
            reportComment(id)
        } else if (action == "EDIT") {
            position = id
            commentEdit = true
            var comment = list[id].comment.toString()

            binding.etMessage.setText(comment)
        } else if (action == "DELETE") {
            deleteComment(id, token)
        } else if (action == "BLOCK") {
            blockUser(id, token)
        }

    }

    private fun deleteComment(id: Int?, token: String) {
        journalView.commentDelete(id, token)?.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        it?.data?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Status.SUCCESS -> {

                    Toast.makeText(
                        requireContext(),
                        it?.data?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    refresh()
                }
            }
        }
    }

    private fun updateComment(journalId: Int?, commentid: Int?, comment: String, token: String) {

        journalView.commentUpdate(journalId!!, commentid!!, comment, token)
            ?.observe(requireActivity()) {
                when (it.status) {
                    Status.LOADING -> {}
                    Status.ERROR -> {
                        Toast.makeText(
                            requireContext(),
                            it?.data?.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Status.SUCCESS -> {

                        Toast.makeText(
                            requireContext(),
                            it?.data?.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                        refresh()
                    }
                }
            }
    }

    private fun reportComment(id: Int) {

        ReportDialog(
            this,
            this,
            requireContext(),
            requireActivity(),
            id.toString(),
            "journalComment",
        ).show()

    }

    private fun likeComment(id: Int?) {
        journalView.commentLike(id, token)?.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        it?.data?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Status.SUCCESS -> {

                    Toast.makeText(
                        requireContext(),
                        it?.data?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    Handler().postDelayed({
                        refresh()
                    }, 3000)


                }
            }
        }
    }

    fun blockUser(id: Int, token: String) {
        peopleView.userBlock(id, token)?.observe(this) {
            when (it.status) {
                Status.ERROR -> {

                }
                Status.SUCCESS -> {

                    Toast.makeText(requireContext(), "User Blocked", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {

                }
            }
        }
    }

    override fun onRefresh() {
        refresh()
    }

    private fun refresh() {
        getCommentsData(token)
    }

    override fun sendcommentReply(
        action: String,
        journalId: String,
        comment: String,
        comment_Id: String,
    ) {

        if (action == "Comment") {
            sendCommentReply(journalId.toInt(), comment, comment_Id, token)
        } else if (action == "REPLYEDIT") {

            var commed = comment_Id
            var comment = comment
            updateComment(journalId.toInt(), comment_Id.toInt(), comment!!, token)
        }
    }

}