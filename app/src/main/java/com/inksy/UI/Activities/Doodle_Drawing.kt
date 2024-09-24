package com.inksy.UI.Activities


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.SkuType
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
import com.anjlab.android.iab.v3.TransactionDetails
import com.example.example.DoodlePack
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Adapter.DoodleViewAdapter
import com.inksy.UI.Constants
import com.inksy.UI.ViewModel.DoodleView
import com.inksy.Utils.Security
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivityDoodleDrawingBinding
import java.io.IOException
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.List


class Doodle_Drawing : AppCompatActivity(), iOnClickListerner, IBillingHandler,
    PurchasesUpdatedListener {

    lateinit var doodleView: DoodleView
    var token: String = ""
    lateinit var tinyDB: TinyDB
    lateinit var binding: ActivityDoodleDrawingBinding
    var fragment: String = ""
    var fromAdapter: Boolean = false
    var wholedata: DoodlePack? = DoodlePack()
    var packId: String? = ""

    var productID: String = ""

    var billingProcessor: BillingProcessor? = null
    private var billingClient: BillingClient? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDoodleDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeBilling()

        fragment = intent.getStringExtra("fragment").toString()
        fromAdapter = intent.getBooleanExtra("fromAdapter", false)
        var doodlePackId = intent.getStringExtra("Id")

        packId = doodlePackId

        doodleView = ViewModelProvider(this)[DoodleView::class.java]
        doodleView.init()

        tinyDB = TinyDB(this)
        token = tinyDB.getString("token").toString()

        binding.buy.setOnClickListener {

            val number2digits: Double = String.format("%.2f", wholedata?.price).toDouble()

            if (number2digits == 0.00) {
                createOrder(wholedata?.id!!, number2digits)

            } else if (productID.equals("") || productID.equals("Invalid")) {
                Toast.makeText(this, "Invalid Doodle Pack Price", Toast.LENGTH_SHORT).show()

            } else {
                purchase()
            }
//            val number2digits: Double = String.format("%.2f", wholedata?.price).toDouble()
//            createOrder(wholedata?.id!!, number2digits)
        }



        binding.edit.setOnClickListener {
            startActivity(
                Intent(
                    this@Doodle_Drawing,
                    EditPackActivity::class.java
                ).putExtra("PackId", wholedata?.id.toString())
            )
        }

        if (fragment == Constants.fragment_pending) {
            binding.subtext.visibility = View.GONE
            binding.edit.visibility = View.VISIBLE
            binding.buy.visibility = View.GONE
            binding.line1Title.text = "Pack of"
            binding.line2Title.text = "Last updated on"
            binding.line3Title.text = "Response"

            getData(doodlePackId!!, token)

        } else if (fragment == Constants.fragment_approved) {
            binding.subtext.visibility = View.VISIBLE
            binding.edit.visibility = View.GONE
            binding.buy.visibility = View.GONE
            binding.line1Title.text = "Your Earnings"
            binding.line2Title.text = "Sale"
            binding.line3Title.text = "Upload Date"

            getData(doodlePackId!!, token)
        } else {
            binding.buy.visibility = View.VISIBLE
            binding.line1Title.text = "Pack of"
            binding.line2Title.text = "Total Sales"
            binding.line3Title.text = "Amount"

            getData(doodlePackId!!, token)
        }

        binding.back.setOnClickListener {
            onBackPressed()
            this.finish()
        }

        binding.line3value.setOnClickListener() {

//            if (fragment == Constants.fragment_pending) {
//            onButtonShowPopupWindowClick(it)
//            }
        }
        binding.line3Title.setOnClickListener {
//            if (fragment == Constants.fragment_pending) {
//                onButtonShowPopupWindowClick(it)
//            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        refresh()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refresh() {
        getData(packId!!, token)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getData(doodlePackId: String, token: String) {
        doodleView.doodleDetails(doodlePackId, token)?.observe(this) {
            binding.loader.visibility = View.GONE
            when (it.status) {
                Status.SUCCESS -> {
                    var stf = it?.data?.data?.updatedAt
                    var date = OffsetDateTime.parse(stf).format(
                        DateTimeFormatter.ofPattern(
                            "MMM d uuuu",
                            Locale.ENGLISH
                        )
                    );
                    wholedata = it?.data?.data
                    binding.text.text = it?.data?.data?.packTitle

                    val price: Double = String.format("%.2f", wholedata?.price).toDouble()

                    if (price == 0.00) {
                        binding.buy.text = "Get it Now"
                    }

//                    productID = Constants.googlePlayTestingProductID

                    if (price == 0.99) {
                        productID = Constants.googlePlayDoodlePackPrice1
                    } else if (price == 1.99) {
                        productID = Constants.googlePlayDoodlePackPrice2
                    } else if (price == 2.99) {
                        productID = Constants.googlePlayDoodlePackPrice3
                    } else if (price == 3.99) {
                        productID = Constants.googlePlayDoodlePackPrice4
                    } else if (price == 4.99) {
                        productID = Constants.googlePlayDoodlePackPrice5
                    } else {
                        productID = "Invalid"
                    }


                    if (fragment == Constants.fragment_pending) {


                        binding.buy.visibility = View.GONE
                        binding.edit.visibility = View.VISIBLE
                        binding.line1value.text = it.data?.data?.doodles?.size.toString()
                        binding.line2value.text = date
                        binding.line3value.text = "Admin"
                        binding.subtext.text = it?.data?.data?.user?.fullName.toString()

                    } else if (fragment == Constants.fragment_approved) {


                        binding.buy.visibility = View.GONE
                        binding.edit.visibility = View.GONE

                        val earning = it?.data?.data?.price?.times(it.data.data?.salesCount!!)

                        val number2digits: Double = String.format("%.2f", earning).toDouble()
                        binding.line1value.text = "$ ${number2digits.toString()}"
                        binding.line2value.text = it.data?.data?.salesCount.toString()
                        binding.line3value.text = date
                        binding.subtext.text = it?.data?.data?.user?.fullName.toString()
                    } else {
                        binding.line1value.text = it?.data?.data?.doodles?.size.toString()
                        binding.line2value.text = it.data?.data?.salesCount.toString()
                        val number2digits = String.format("%.2f", it?.data?.data?.price)
                        binding.line3value.text = "$ $number2digits"
                        binding.subtext.text = it?.data?.data?.user?.fullName.toString()

                        binding.buy.visibility = View.VISIBLE
                        binding.edit.visibility = View.GONE

                    }

                    if (it?.data?.data?.isPurchased == 1) {
                        binding.buy.visibility = View.GONE
                        fromAdapter = false
                    }

                    if (it?.data?.data?.doodles?.size!! > 0) {

                        var adapter: DoodleViewAdapter =
                            DoodleViewAdapter(this, it.data.data?.doodles!!, this, fromAdapter)

                        binding.rvDoodle.layoutManager = GridLayoutManager(this, 2)
                        binding.rvDoodle.adapter = adapter
                    } else {
                        Toast.makeText(this, "Doodles are empty", Toast.LENGTH_SHORT).show()
                    }

                }

                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }
    }


    private fun onButtonShowPopupWindowClick(view: View?, note: String) {

        // inflate the layout of the popup window
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.dialog_artistpanel, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        var textview: TextView = popupView.findViewById(R.id.note)
        textview.text = note.toString()
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        // dismiss the popup window when touched
        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
    }

    private fun createOrder(id: Int, price: Double) {
        doodleView.createOrder(id, price, token)?.observe(this) {

            when (it.status) {
                Status.SUCCESS -> {

                    Toast.makeText(
                        applicationContext,
                        "Order Created Successful",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    startActivity(
                        Intent(this@Doodle_Drawing, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    finish()

                }

                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(
                        this,
                        it?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }

    override fun onclick(position: Int) {
        super.onclick(position)

        var size = wholedata?.doodles?.get(position)?.notes?.size?.minus(1)
        if (size != null) {
            var msg = wholedata?.doodles?.get(position)?.notes?.get(size)?.note

            onButtonShowPopupWindowClick(binding.root, msg!!)

        }
    }

    // Payment work start

    private fun initializeBilling() {

        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases().setListener(this).build()

        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

                    billingClient!!.queryPurchasesAsync(
                        SkuType.INAPP
                    ) { billingResult, list ->
//                        doSomethingWithPurchaseList(list)
// old way                 // val pr = billingClient?.queryPurchases(BillingClient.SkuType.INAPP)
                        val pList = list
                        for (iitem in pList!!) {
                            val consumeParams = ConsumeParams.newBuilder()
                                .setPurchaseToken(iitem.purchaseToken)
                                .build()
                            billingClient?.consumeAsync(consumeParams,
                                ConsumeResponseListener { billingResult, s -> })
                        }
                    }

                }
            }

            override fun onBillingServiceDisconnected() {}
        })

        billingProcessor = BillingProcessor(this, Constants.googlePlayLicenseKey, this)
        billingProcessor!!.initialize()

    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {}

    override fun onPurchaseHistoryRestored() {}

    override fun onBillingError(errorCode: Int, error: Throwable?) {}

    override fun onBillingInitialized() {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (!billingProcessor!!.handleActivityResult(requestCode, resultCode, data)) {
            Toast.makeText(this, "Paid Successfully!", Toast.LENGTH_SHORT).show()
            return
        }
    }

    private fun initiatePurchase() {
        val skuList: MutableList<String> = ArrayList()
        skuList.add(productID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(SkuType.INAPP)
        billingProcessor!!.consumePurchase(productID)
        billingClient!!.querySkuDetailsAsync(
            params.build()
        ) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (skuDetailsList != null && skuDetailsList.size > 0) {
                    val flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList[0])
                        .build()
                    billingClient!!.launchBillingFlow(this@Doodle_Drawing, flowParams)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Purchase Item not Found",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Something went wrong! " + billingResult.debugMessage, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases)
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            initiatePurchase()
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(applicationContext, "Cancelled", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                applicationContext,
                "Something went wrong! " + billingResult.debugMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

//    fun handlePurchases(purchases: List<Purchase>) {
//        for (purchase in purchases) {
//            if (productID == purchase.sku && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
////                if (!verifyValidSignature(purchase.originalJson, purchase.signature)) {
////                    Toast.makeText(
////                        applicationContext,
////                        "Error : Invalid Purchase",
////                        Toast.LENGTH_SHORT
////                    ).show()
////                    return
////                } else {
//                    val number2digits: Double = String.format("%.2f", wholedata?.price).toDouble()
//                    createOrder(wholedata?.id!!, number2digits)
////                }
//            } else if (productID == purchase.sku && purchase.purchaseState == Purchase.PurchaseState.PENDING) {
//                Toast.makeText(
//                    applicationContext,
//                    "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT
//                ).show()
//            } else if (productID == purchase.sku && purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
//                Toast.makeText(applicationContext, "Purchase Status Unknown", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }


    fun handlePurchases(purchases: List<Purchase>) {
        for (purchase in purchases) {
            for (sku in 0 until purchase.skus.size) {
                if (productID == purchase.skus[sku].toString() && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    val number2digits: Double = String.format("%.2f", wholedata?.price).toDouble()
                    createOrder(wholedata?.id!!, number2digits)

                } else if (productID == sku.toString() && purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                    Toast.makeText(
                        applicationContext,
                        "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT
                    ).show()

                } else if (productID == sku.toString() && purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                    Toast.makeText(
                        applicationContext,
                        "Purchase Status Unknown",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun verifyValidSignature(signedData: String, signature: String): Boolean {
        return try {
            val base64Key = Constants.googlePlayLicenseKey
            Security.verifyPurchase(base64Key, signedData, signature)
        } catch (e: IOException) {
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (billingClient != null) {
            billingClient!!.endConnection()
        }
    }

    fun purchase() {
        if (billingClient!!.isReady) {
            initiatePurchase()
        } else {
            billingClient =
                BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build()
            billingClient!!.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Something went wrong! " + billingResult.debugMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onBillingServiceDisconnected() {}
            })
        }
    }

    // Payment work end

}