package com.inksy.UI.Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.inksy.Database.JournalDatabase
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Model.JournalTemplateModel
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Adapter.TemplateAdapter
import com.inksy.UI.Constants
import com.inksy.UI.ViewModel.JournalView
import com.inksy.Utils.FileUtil
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentCreatejournalBinding
import kotlinx.coroutines.launch
import java.io.File

class createjournal : Fragment(), iOnClickListerner {
    var token: String? = ""
    lateinit var cameraUri: Uri
    private val PICK_REQUEST = 53
    lateinit var journalView: JournalView
    lateinit var tinyDB: TinyDB
    lateinit var binding: FragmentCreatejournalBinding
    lateinit var journalDatabase: JournalDatabase
    var templateList: ArrayList<JournalTemplateModel> = ArrayList()
    var edit = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreatejournalBinding.inflate(layoutInflater)

        journalDatabase = JournalDatabase.getInstance(requireContext())!!

        journalView = ViewModelProvider(requireActivity())[JournalView::class.java]
        journalView.init()

        tinyDB = TinyDB(requireContext())

        token = tinyDB.getString("token")

        getData(token!!)
        val tabledata = journalDatabase.getJournalData().getAllNotes()
        if (tabledata.isNotEmpty()) {
            edit = true
        }


        binding.ivBack.setOnClickListener {
            requireActivity().finish()
        }
        binding.uploadown.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), PICK_REQUEST
            )
        }

        binding.emptyjournal.setOnClickListener {

            lifecycleScope.launch {
                if (edit) {
                    journalDatabase.getJournalData().UpdateJournalWithIndexBackGround("white", "1")
                    journalDatabase.getJournalData().updateprivacy("2", "1")
                } else {
                    journalDatabase.getJournalData().createJournalWithIndexBackGround("white", "1")
                    journalDatabase.getJournalData().updateprivacy("2", "1")
                }
            }
            val action =
                createjournalDirections.actionCreatejournalToCreateJournalBackgroundBorderColor()
            findNavController().navigate(action)
        }

        binding.ivnext.setOnClickListener {
            val action =
                createjournalDirections.actionCreatejournalToCreateJournalBackgroundBorderColor()
            findNavController().navigate(action)
        }

        if (tabledata.isNotEmpty()) {
            binding.ivnext.visibility = View.VISIBLE
            binding.create.text = getString(R.string.updatejournal)
            binding.selectText.visibility = View.GONE
            binding.selectedText.visibility = View.VISIBLE
            binding.selectedImage.visibility = View.VISIBLE
            Glide.with(requireContext())
                .load(Constants.BASE_IMAGE + tabledata[0].indexTemplate)
                .into(binding.selectedImage)

        }
        return binding.root
    }

    fun getData(token: String) {
        journalView.getTemplate(token)?.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {

                    templateList = it?.data?.data as ArrayList<JournalTemplateModel>
                    binding.rvTemplate.adapter =
                        TemplateAdapter(requireContext(), templateList, this)
                }
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }
    }

    override fun onclick(position: Int) {
        super.onclick(position)

        lifecycleScope.launch {

            if (edit) {
                journalDatabase.getJournalData()
                    .UpdateJournalWithIndexBackGround(templateList[position].templateImage!!, "1")

                journalDatabase.getJournalData().updateprivacy("2", "1")
            } else {
                journalDatabase.getJournalData()
                    .createJournalWithIndexBackGround(templateList[position].templateImage!!, "1")

                journalDatabase.getJournalData().updateprivacy("2", "1")
            }


        }
        val action =
            createjournalDirections.actionCreatejournalToCreateJournalBackgroundBorderColor()
        findNavController().navigate(action)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_REQUEST) {
                cameraUri = data!!.data!!

                val uri = data.data

                val bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().applicationContext.contentResolver,
                    uri
                )
                var file: File = FileUtil.from(requireContext(), cameraUri)
                UploadImage(file)

                if (uri != null) {
                    cameraUri = uri
                }
            }
        }

    }

    fun UploadImage(file: File) {
        journalView.imageUpload(token!!, file)?.observe(requireActivity()) {
            when (it?.status) {
                Status.SUCCESS -> {

                    lifecycleScope.launch {

                        if (edit) {
                            journalDatabase.getJournalData()
                                .UpdateJournalWithIndexBackGround(
                                    it.data?.data?.avatar!!,
                                    "1"
                                )
                            journalDatabase.getJournalData().updateprivacy("2", "1")
                        } else {
                            journalDatabase.getJournalData()
                                .createJournalWithIndexBackGround(
                                    it.data?.data?.avatar!!,
                                    "1"
                                )
                            journalDatabase.getJournalData().updateprivacy("2", "1")
                        }
                    }

                    val action =
                        createjournalDirections.actionCreatejournalToCreateJournalBackgroundBorderColor()
                    findNavController().navigate(action)

                }

                Status.ERROR -> {}
                Status.LOADING -> {}
                else -> {}
            }
        }
    }


}