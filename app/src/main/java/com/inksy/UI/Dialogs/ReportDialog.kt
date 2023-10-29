package com.inksy.UI.Dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.ViewModel.JournalView
import com.inksy.UI.ViewModel.PeopleView
import com.inksy.Utils.AppUtils
import com.inksy.Utils.TinyDB


class ReportDialog(
    var vm: ViewModelStoreOwner,
    var lco: LifecycleOwner,
    context: Context, var _activity: Activity, var id: String,
    var type: String
) : Dialog(context), View.OnClickListener {

    lateinit var tvCancel: TextView
    lateinit var tvOk: TextView
    var _event_id: String? = null
    lateinit var Okprogress: ProgressBar
    private lateinit var edtdesc: EditText
    private lateinit var edtTitle: Spinner
    lateinit var journalView: JournalView
    lateinit var peopleView: PeopleView
    lateinit var dialogTitle: TextView
    lateinit var tiny: TinyDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawableResource(R.drawable.transparent)
        setContentView(R.layout.dialoge_report_event)
        setCanceledOnTouchOutside(false)
        val window = window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        dialogTitle = findViewById(R.id.tvAddAccount)
        Okprogress = findViewById(R.id.Okprogress)
        tvCancel = findViewById(R.id.tvCancel)
        tvOk = findViewById(R.id.tvOk)
        edtTitle = findViewById(R.id.edtTitle)
        edtdesc = findViewById(R.id.edtdesc)
        tvCancel.setOnClickListener(this)
        tvOk.setOnClickListener(this)
        edtdesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (edtdesc.getText().toString().length > 0) {
                    val letter: String = edtdesc.getText().toString().get(0).toString()
                    val letterUpperCaee = letter.toUpperCase()
                    if (letter != letterUpperCaee) {
                        edtdesc.setText(capitaliseOnlyFirstLetter(edtdesc.getText().toString()))
                        edtdesc.setSelection(edtdesc.getText().toString().length)
                    }
                }
            }
        })


        val country = arrayOf(
            "Abusive or Threatening",
            "Inappropriate Content, Behaviour or Picture",
            "Spam, advertising or a scam",
            "Made me uncomfortable",
            "Photo or Copyrighted Material"
        )

        val aa: ArrayAdapter<*> =
            ArrayAdapter<Any?>(context, android.R.layout.simple_spinner_item, country)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        //Setting the ArrayAdapter data on the Spinner
        edtTitle.adapter = aa
        if (type.contains("journal")) {
            initUiJournal()
        } else {
            initUiJoUser()
        }

    }

    private fun initUiJournal() {
        journalView = ViewModelProvider(vm)[JournalView::class.java]
        journalView.init()
        tiny = TinyDB(context)


    }

    private fun initUiJoUser() {
        peopleView = ViewModelProvider(vm)[PeopleView::class.java]
        peopleView.init()
        tiny = TinyDB(context)
        dialogTitle.text = "Report User"
    }

    fun capitaliseOnlyFirstLetter(data: String): String? {
        return data.substring(0, 1).toUpperCase() + data.substring(1)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvCancel -> dismiss()
            R.id.tvOk -> {
                if (edtdesc.text.toString().equals("", ignoreCase = true)) {
                    AppUtils.setErrorOnEditText(edtdesc, "Please enter description...")
                    return
                }
                edtTitle.isEnabled = false
                edtdesc.isEnabled = false
                Okprogress.visibility = View.VISIBLE
                tvOk.visibility = View.GONE
                if (type.contains("journal"))
                    if (type.contains("Comment")) {
                        reportComments(
                            id,
                            edtTitle.selectedItem.toString(),
                            edtdesc.text.toString()
                        )
                    } else {
                        reportJournal(id, edtTitle.selectedItem.toString(), edtdesc.text.toString())
                    }
                else {
                    reportUser(id, edtTitle.selectedItem.toString(), edtdesc.text.toString())
                }
            }
        }
    }

    private fun reportComments(id: String, title: String, desc: String) {
        var token = tiny.getString("token")
        journalView.commentReport(id.toInt(), token!!, title, desc)?.observe(lco) {
            when (it.status) {
                Status.ERROR -> {

                    Okprogress.visibility = View.GONE

                    Toast.makeText(context, it?.data?.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    this.dismiss()
                    Okprogress.visibility = View.GONE
                    tvOk.visibility = View.VISIBLE
                    Toast.makeText(context, it?.data?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun reportJournal(journal_id: String, title: String, desc: String) {
        var token = tiny.getString("token")
        journalView.journalReport(journal_id.toInt(), token!!, title, desc)?.observe(lco) {
            when (it.status) {
                Status.ERROR -> {

                    Okprogress.visibility = View.GONE

                    Toast.makeText(context, it?.data?.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    _activity.finish()
                    Okprogress.visibility = View.GONE

                    Toast.makeText(context, it?.data?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun reportUser(userId: String, title: String, desc: String) {
        var token = tiny.getString("token")
        peopleView.userReport(userId.toInt(), title, desc, token!!)?.observe(lco) {
            when (it.status) {
                Status.ERROR -> {

                    Okprogress.visibility = View.GONE

                    Toast.makeText(context, it?.data?.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    _activity.finish()
                    Okprogress.visibility = View.GONE

                    Toast.makeText(context, it?.data?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}