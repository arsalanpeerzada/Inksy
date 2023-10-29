package com.inksy.Interfaces

interface iSendCommentReply {

    fun sendcommentReply(
        action: String,
        journalId: String,
        comment: String,
        comment_Id: String,
    )
}