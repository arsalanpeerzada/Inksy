package com.inksy.UI.Fragments

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.inksy.Interfaces.OnDialogClickListener
import com.inksy.UI.Activities.*
import com.inksy.UI.Activities.List
import com.inksy.UI.Dialogs.TwoButtonDialog
import com.inksy.UI.ViewModel.LogoutView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentMoreInfoBinding


class MoreInfo : Fragment() {


    lateinit var binding: FragmentMoreInfoBinding
    lateinit var logoutView: LogoutView
    lateinit var tinyDB: TinyDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        tinyDB = TinyDB(requireContext())
        binding = FragmentMoreInfoBinding.inflate(layoutInflater)


        binding.imageDooble11.setOnClickListener() {

            //  Toast.makeText(requireContext(), "Feature coming soon", Toast.LENGTH_SHORT).show()
            openNewActivity(WebViewTest::class.java)
        }
        binding.tvDoodle22.setOnClickListener() {

            //  Toast.makeText(requireContext(), "Feature coming soon", Toast.LENGTH_SHORT).show()
            openNewActivity(WebViewTest::class.java)
        }
        binding.tvDescDoodle33.setOnClickListener() {

            //  Toast.makeText(requireContext(), "Feature coming soon", Toast.LENGTH_SHORT).show()
            openNewActivity(WebViewTest::class.java)
        }


        binding.tvDoodle.setOnClickListener() {

            //  Toast.makeText(requireContext(), "Feature coming soon", Toast.LENGTH_SHORT).show()
            openNewActivity(DoodleStore::class.java)
        }
        binding.imageDooble.setOnClickListener {
            //Toast.makeText(requireContext(), "Feature coming soon", Toast.LENGTH_SHORT).show()
            openNewActivity(DoodleStore::class.java)
        }
        binding.tvDescDoodle.setOnClickListener {
            //  Toast.makeText(requireContext(), "Feature coming soon", Toast.LENGTH_SHORT).show()
            openNewActivity(DoodleStore::class.java)
        }

        binding.tvimgBlockList.setOnClickListener { openNewActivity(List::class.java) }
        binding.imgBlockList.setOnClickListener { openNewActivity(List::class.java) }
        binding.subtvimgBlockList.setOnClickListener { openNewActivity(List::class.java) }

        binding.tvArtist.setOnClickListener {
            openNewActivity(ArtisePanel::class.java)
        }

        binding.imgArtist.setOnClickListener {
            openNewActivity(ArtisePanel::class.java)
        }
        binding.subtvArtist.setOnClickListener {
            openNewActivity(ArtisePanel::class.java)
        }


        binding.tvMore.setOnClickListener { openNewActivity(OtherActivity::class.java) }
        binding.tvMoreDesc.setOnClickListener { openNewActivity(OtherActivity::class.java) }
        binding.imgMore.setOnClickListener { openNewActivity(OtherActivity::class.java) }


        binding.tvContactUs.setOnClickListener { openNewActivity(Contact_Form::class.java) }
        binding.tvContactUsDesc.setOnClickListener { openNewActivity(Contact_Form::class.java) }
        binding.imgtvContactUs.setOnClickListener { openNewActivity(Contact_Form::class.java) }


        binding.tvCommunity.setOnClickListener { openNewActivity(CommunityGuide::class.java) }
        binding.subtvCommunity.setOnClickListener { openNewActivity(CommunityGuide::class.java) }
        binding.imgCommunity.setOnClickListener { openNewActivity(CommunityGuide::class.java) }

        binding.shareImg.setOnClickListener { share() }
        binding.sharedesc.setOnClickListener { share() }
        binding.sharetv.setOnClickListener { share() }

        binding.LogoutTitle.setOnClickListener { openDialog() }
        binding.logoutImg.setOnClickListener { openDialog() }
        binding.logoutSub.setOnClickListener { openDialog() }


        binding.support.setOnClickListener {
            binding.supportNumbe.performClick()
        }
        binding.supportNumbe.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:") // only email apps should handle this

            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("Support@inksyapp.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "App feedback")
            startActivity(intent)

        }





        return binding.root

    }

    private fun openNewActivity(clazz: Class<*>) {
        requireContext().startActivity(Intent(requireContext(), clazz))
    }

    fun share() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
//                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.appshah.Helloletsgo");
        //                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.appshah.Helloletsgo");
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "https://play.google.com/store/apps/details?id=" + requireContext().applicationContext
                .packageName
        )
        sendIntent.type = "text/plain"
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }


    private fun openDialog() {
        val twoButtonDialog: TwoButtonDialog = TwoButtonDialog(
            requireActivity(), "Log Out",
            getString(com.inksy.R.string.logout),
            getString(R.string.yes),
            getString(R.string.no),
            object : OnDialogClickListener {
                override fun onDialogClick(callBack: String?) {
                    if (callBack == "Yes") {

                        logout()
                    } else {

                    }
                }
            })
        twoButtonDialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        twoButtonDialog.show()
    }


    fun logout() {
        binding.spinKit.visibility = View.VISIBLE

        var token = tinyDB.getString("token")

        logoutView = ViewModelProvider(requireActivity())[LogoutView::class.java]
        logoutView.init()
        logoutView.logout(token)?.observe(requireActivity()) {

            binding.spinKit.visibility = View.GONE
            if (it?.status == 1) {

                tinyDB.clear()

                requireContext().startActivity(
                    Intent(
                        requireContext(),
                        StartingActivity::class.java
                    )
                )

            } else {
                Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}