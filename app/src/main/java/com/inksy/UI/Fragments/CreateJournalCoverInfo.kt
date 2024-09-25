package com.inksy.UI.Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.inksy.Database.Entities.CategoryTable
import com.inksy.Database.JournalDatabase
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Constants
import com.inksy.UI.ViewModel.JournalView
import com.inksy.Utils.FileUtil
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentCreatejournalcoverBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class CreateJournalCoverInfo : Fragment() {

    lateinit var cameraUri: Uri
    var cameraUploadedImage: String? = ""
    var photoSelect = false
    private lateinit var tvTitle: TextView
    private lateinit var tvContinue: TextView
    var numberPicker: NumberPicker? = null
    private lateinit var bottomSheetDialog: RoundedBottomSheetDialog
    lateinit var binding: FragmentCreatejournalcoverBinding
    private val PICK_REQUEST = 53
    lateinit var tinydb: TinyDB
    var selectedCategoyId: Int = 0
    var catList: ArrayList<CategoryTable> = ArrayList()
    lateinit var data: Array<String?>
    lateinit var journalDatabase: JournalDatabase
    lateinit var jouralView: JournalView
    var edit = false
    var token: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreatejournalcoverBinding.inflate(layoutInflater)

        tinydb = TinyDB(requireContext())

        journalDatabase = JournalDatabase.getInstance(requireContext())!!
        jouralView = ViewModelProvider(requireActivity())[JournalView::class.java]
        jouralView.init()

        token = tinydb.getString("token")
        getData(token!!)
        cameraUri = Uri.parse("asdasdasd")

        binding.category.setOnClickListener {
            openRoundBottomSheet()
        }

        val tinyDB = TinyDB(requireContext())
        tinyDB.remove("jsondata")

        val tabledata = journalDatabase.getJournalData().getAllNotes()
        if (tabledata.isNotEmpty()) {
            edit = true
        }
        if (tabledata[0].categoryId?.isNotEmpty() == true) {
            binding.title.setText(tabledata[0].journalTitle.toString())
            binding.description.setText(tabledata[0].coverDescription.toString())
            binding.category.setText(tabledata[0].categoryName.toString())
            selectedCategoyId = tabledata[0].categoryId?.toInt()!!
            cameraUri = Uri.parse(tabledata[0].coverImage)

            if (tabledata[0].coverImageString.equals("Empty")){

                Glide.with(requireContext()).load(Constants.BASE_IMAGE + tabledata[0].coverImage)
                    .into(binding.coverImage)
                photoSelect = true
            }else {
                Glide.with(requireContext()).load(Constants.BASE_IMAGE + tabledata[0].coverImageString)
                    .into(binding.coverImage)
                photoSelect = true
            }


        }

        binding.ivBack.setOnClickListener {
            val action =
                CreateJournalCoverInfoDirections.actionCreateJournalCoverInfoToCreateJournalBackgroundBorderColor()
            findNavController().navigate(action)
        }

        binding.checked.setOnClickListener {

            if (checkdata())
                findNavController().navigate(
                    R.id.action_CreateJournalCoverInfo_to_createJournalIndex
                )
        }

        binding.audienceSetting.setOnClickListener {

            if (checkdata())
                findNavController().navigate(
                    R.id.action_CreateJournalCoverInfo_to_select_Audience
                )
        }
        binding.audienceSettingText.setOnClickListener {

            if (checkdata())
                findNavController().navigate(
                    R.id.action_CreateJournalCoverInfo_to_select_Audience
                )
        }

        binding.image.setOnClickListener {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), PICK_REQUEST
            )
        }
        return binding.root
    }

    fun checkdata(): Boolean {
        if (selectedCategoyId == 0) {
            Toast.makeText(requireContext(), "Please select category", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (binding.title.text.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please enter title", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.description.text.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please enter description", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (!photoSelect) {
            Toast.makeText(requireContext(), "Please add a photo for your journal cover", Toast.LENGTH_SHORT)
                .show()
            return false
        } else {
            val bundle = Bundle()
            bundle.putInt("categoryId", selectedCategoyId)
            bundle.putString("title", binding.title.text.toString())
            bundle.putString("description", binding.description.text.toString())
            bundle.putString("categoryName", binding.category.text.toString())
            bundle.putString("uri", cameraUri.toString())
            val journalDatabase: JournalDatabase =
                JournalDatabase.getInstance(requireContext())!!
            lifecycleScope.launch {
                journalDatabase.getJournalData().insertCover(
                    "1",
                    binding.title.text.toString(),
                    binding.description.text.toString(),
                    cameraUri.toString(),
                    selectedCategoyId.toString(),
                    binding.category.text.toString()
                )
            }
            return true
        }
    }

    fun getData(token: String) {
        jouralView.getCategoriesList(token)?.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {

                    for (i in 0 until it?.data?.data?.size!!) {

                        val categoryTable = CategoryTable(
                            it.data.data!![i].id.toString(),
                            it.data.data!![i].categoryName,
                            it.data.data!![i].isActive,
                            it.data.data!![i].createdAt,
                            it.data.data!![i].updatedAt
                        )

                            journalDatabase.getJournalData().insertCategory(categoryTable)
                            catList = journalDatabase.getJournalData()
                                .getAllCategories() as ArrayList<CategoryTable>
                            data = arrayOfNulls<String>(catList.size)

                            val namelist = ArrayList<String>()
                            for (i in 0 until catList.size) {
                                val data = catList[i].categoryName.toString()
                                namelist.add(data)
                            }
                            for (i in 0 until catList.size) {
                                data[i] = namelist[i]
                            }

                    }
                }
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }
    }

    private fun openRoundBottomSheet() {
        bottomSheetDialog = RoundedBottomSheetDialog(requireContext())
        val bottomDialogView: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.bottom_dialog_wheel, null)
        bottomSheetDialog.setContentView(bottomDialogView)

        numberPicker = bottomDialogView.findViewById<NumberPicker>(R.id.numberPicker)
        tvContinue = bottomDialogView.findViewById<TextView>(R.id.tvContinue)
        tvTitle = bottomDialogView.findViewById<TextView>(R.id.tvTitle)

        tvTitle.text = getString(R.string.select_category)

        numberPicker?.minValue = 0 //from array first value
        numberPicker?.maxValue = catList.size - 1 //to array last value

        numberPicker?.displayedValues = data
        numberPicker?.wrapSelectorWheel = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            numberPicker?.textColor = resources.getColor(R.color.black)
        }


        tvContinue.setOnClickListener(View.OnClickListener { view1: View? ->
            binding.category.setText(catList[numberPicker?.value!!].categoryName)
            selectedCategoyId = catList[numberPicker?.value!!].categoryId.toInt()
            bottomSheetDialog.dismiss()
        })
        bottomSheetDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_REQUEST) {
                cameraUri = data!!.data!!

                var file: File = FileUtil.from(requireContext(), cameraUri)
                jouralView.imageUpload(token!!, file)?.observe(requireActivity()) {
                    when (it?.status) {
                        Status.SUCCESS -> {

                            photoSelect = true
                            var backgroundImage = it?.data?.data?.avatar.toString()
                            cameraUploadedImage = backgroundImage
                            Glide.with(requireContext())
                                .load(Constants.BASE_IMAGE + backgroundImage)
                                .into(binding.coverImage)
                            binding.coverImage.scaleType = ImageView.ScaleType.CENTER_CROP

                            journalDatabase.getJournalData()
                                .UpdateCoverImage(cameraUploadedImage!!, "1")
                        }
                        Status.ERROR -> {}
                        Status.LOADING -> {}
                        else -> {}
                    }
                }

            }
        }

    }


}