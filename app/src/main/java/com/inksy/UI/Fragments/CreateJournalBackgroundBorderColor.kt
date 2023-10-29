package com.inksy.UI.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.inksy.Database.JournalDatabase
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.R
import com.inksy.UI.Adapter.BorderColorAdapter
import com.inksy.databinding.FragmentCreateJournalBackgroundBoderColorBinding
import kotlinx.coroutines.launch

class CreateJournalBackgroundBorderColor : Fragment(), iOnClickListerner {


    lateinit var binding: FragmentCreateJournalBackgroundBoderColorBinding
    lateinit var journalDatabase: JournalDatabase

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var edit = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentCreateJournalBackgroundBoderColorBinding.inflate(layoutInflater)
        journalDatabase = JournalDatabase.getInstance(requireContext())!!
        val array = arrayOf(
            resources.getColor(R.color.journalBlue), resources.getColor(R.color.journalPurple),
            resources.getColor(R.color.journalGreen), resources.getColor(R.color.journalRed)
        )
        binding.linearLayout.adapter = BorderColorAdapter(requireContext(), array, this)

        binding.ivBack.setOnClickListener {
            val action =
                CreateJournalBackgroundBorderColorDirections.actionCreateJournalBackgroundBorderColorToCreatejournal()
            findNavController().navigate(action)
        }


        val tabledata = journalDatabase.getJournalData().getAllNotes()
        if (tabledata.isNotEmpty()) {
            edit = true
        }

        binding.ivnext.setOnClickListener() {

            savecolor("blue");
        }

        if (tabledata[0].indexBackground?.isNotEmpty() == true) {
            binding.ivnext.visibility = View.VISIBLE
            binding.create.text = getString(R.string.updatejournal)
            binding.selectText.visibility = View.GONE
            binding.selectedText.visibility = View.VISIBLE
            binding.selectedColor.visibility = View.VISIBLE

            val context = requireContext()

            when (tabledata[0].coverColor) {
                "blue" -> {
                    binding.selectedColor.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.journalBlue);
                }
                "green" -> {
                    binding.selectedColor.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.journalGreen);
                }
                "red" -> {
                    binding.selectedColor.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.journalRed);
                }
                "purple" -> {
                    binding.selectedColor.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.journalPurple);
                }
            }

        }

        return binding.root
    }

    override fun onclick(position: Int) {

        var color = " "
        when (position) {
            0 -> {
                color = "blue"
            }
            1 -> {
                color = "purple"
            }
            2 ->
                color = "green"
            3 -> {
                color = "red"
            }
            4 -> {
                color = "black"
            }
            5 -> {
                color = "purple"
            }
        }
        savecolor(color)

    }

    fun savecolor(color: String) {
        val journalDatabase: JournalDatabase = JournalDatabase.getInstance(requireContext())!!
        lifecycleScope.launch {
            journalDatabase.getJournalData().insertCoverColor(color, "1")
        }

        findNavController().navigate(
            R.id.action_createJournalBackgroundBorderColor_to_CreateJournalCoverInfo,
        )
    }
}