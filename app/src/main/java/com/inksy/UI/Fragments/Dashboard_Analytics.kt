package com.inksy.UI.Fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.inksy.Model.AnalyticsOrders
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Activities.StartingActivity
import com.inksy.UI.Adapter.DashboardAdapter
import com.inksy.UI.ViewModel.DoodleView
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentDashboardAnalyticsBinding
import java.util.*
import kotlin.collections.ArrayList


class Dashboard_Analytics : Fragment() {

    lateinit var doodleView: DoodleView
    private lateinit var lineChart: LineChart
    lateinit var binding: FragmentDashboardAnalyticsBinding
    lateinit var tinydb: TinyDB
    var token = ""

    var dataSets: ArrayList<ILineDataSet?> = ArrayList()
    var analyticsOrders: ArrayList<AnalyticsOrders>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardAnalyticsBinding.inflate(layoutInflater)
        //lineChart = binding.lineChart
        tinydb = TinyDB(requireContext())
        token = tinydb.getString("token").toString()
        doodleView = ViewModelProvider(this)[DoodleView::class.java]
        doodleView.init()

        getData("daily")

        binding.btnDaily.setOnClickListener {
            getData("daily")
        }

        binding.btnMonthly.setOnClickListener {
            getData("monthly")
        }

        binding.btnYearly.setOnClickListener {
            getData("yearly")
        }

        return binding.root

    }

    override fun onResume() {
        super.onResume()
        getData("daily")
    }

    private fun getData(type: String) {

        doodleView.artistDashboard(token, type)?.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    var artistAnalytics = it?.data?.data?.analytics
                    tinydb.putString("pricerange", it?.data?.data?.priceRange)

                    var list_logo = arrayOf(
                        R.drawable.royalties_this_month,
                        R.drawable.approved_art,
                        R.drawable.pending_art,
                        R.drawable.royalties_this_month,
                        R.drawable.royalties_this_month,
                        R.drawable.royalties_this_month,
                        R.drawable.royalties_this_month,
                        R.drawable.royalties_this_month,
                        R.drawable.royalties_this_month
                    )


                    var total = artistAnalytics?.totalPack!!.toInt()
                    var approved = artistAnalytics.approvedPack!!.toInt()
                    var pending = artistAnalytics.pendingPack!!.toInt()

                    val totalSales: String = String.format("%.2f", artistAnalytics?.totalSales!!)
                    val todaySalesd: String = String.format("%.2f", artistAnalytics?.todaySales!!)
                    val mSales: String = String.format("%.2f", artistAnalytics?.monthlySales!!)
                    val ysales: String = String.format("%.2f", artistAnalytics?.yealrySales!!)
                    val res: String = String.format("%.2f", artistAnalytics?.totalReceived!!)
                    val totalearned: String = String.format("%.2f", artistAnalytics?.totalEarned!!)


                    var value = arrayOf(
                        total.toString(),
                        approved.toString(),
                        pending.toString(),
                        mSales,
                        todaySalesd,
                        res,
                        totalearned,

                        )
                    var list_name = arrayOf(
                        "Total Packs",
                        "Approved Art",
                        "Pending Art",
                        "Royalties this Month",
                        "Total Royalties to Date ",
                        "Amount Paid",
                        "Amount Owed",
                    )

                    binding.rvDashboard.adapter = DashboardAdapter(
                        requireContext(),
                        list_logo,
                        list_name,
                        value
                    )

                    analyticsOrders?.clear()
                    analyticsOrders = it.data?.data?.analyticsOrders

                    if(analyticsOrders?.size!! > 0){
                        setGraphData()
                    }

                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    requireContext().startActivity(
                        Intent(
                            requireContext(),
                            StartingActivity::class.java
                        )
                    )
                    Toast.makeText(requireContext(), "Token Expired", Toast.LENGTH_SHORT).show()
                    Toast.makeText(requireContext(), it?.data?.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun setGraphData() {

        val xAxisValues: ArrayList<String> = ArrayList()
//        val incomeEntries = getIncomeEntries()
        val incomeEntries: ArrayList<Entry> = ArrayList()

        for (i in 0 until analyticsOrders?.size!!) {
            xAxisValues.add(analyticsOrders?.get(i)?.formated_date.toString())
            incomeEntries.add(Entry((i+1).toFloat(), analyticsOrders?.get(i)?.sales_count?.toFloat()!!))
        }

        dataSets = ArrayList()
        val set1: LineDataSet

        set1 = LineDataSet(incomeEntries, "Income")
        set1.color = Color.BLUE
        set1.valueTextColor = Color.rgb(55, 70, 73)
        set1.valueTextSize = 10f
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.setDrawFilled(true)
        set1.fillColor = ContextCompat.getColor(requireContext(), R.color.appBlue)
        set1.setDrawFilled(true)
//        lineDataSet.cubicIntensity = 10f
        dataSets.add(set1)

        val mLineGraph: LineChart = binding.lineChart
        mLineGraph.setTouchEnabled(true)
        mLineGraph.isDragEnabled = true
        mLineGraph.setScaleEnabled(false)
        mLineGraph.setPinchZoom(false)
        mLineGraph.setDrawGridBackground(false)
        mLineGraph.xAxis.setDrawGridLines(false)
        mLineGraph.axisLeft.setDrawGridLines(false)
        mLineGraph.axisRight.setDrawGridLines(false)

        val rightYAxis = mLineGraph.axisRight
        rightYAxis.isEnabled = false
        val leftYAxis = mLineGraph.axisLeft
        leftYAxis.isEnabled = true
        val topXAxis = mLineGraph.xAxis
        topXAxis.isEnabled = false

        val xAxis = mLineGraph.xAxis
        xAxis.granularity = 2f
        xAxis.setCenterAxisLabels(true)
        xAxis.isEnabled = true
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        set1.lineWidth = 3f
        set1.circleRadius = 4f
        set1.setDrawValues(true)
        set1.circleHoleColor = Color.BLUE
        set1.setCircleColor(Color.BLUE)

        mLineGraph.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        val data = LineData(dataSets)
        mLineGraph.data = data
        //   mLineGraph.animateX(2000)
        mLineGraph.invalidate()
        mLineGraph.legend.isEnabled = false
        mLineGraph.description.isEnabled = false

    }

    private fun getIncomeEntries(): List<Entry>? {
        val incomeEntries: ArrayList<Entry> = ArrayList()
        incomeEntries.add(Entry(1f, 11f))
        incomeEntries.add(Entry(2f, 13f))
        incomeEntries.add(Entry(3f, 11f))
        incomeEntries.add(Entry(4f, 72f))
        incomeEntries.add(Entry(5f, 47f))
        incomeEntries.add(Entry(6f, 45f))
        incomeEntries.add(Entry(7f, 80f))
        incomeEntries.add(Entry(8f, 70f))
        incomeEntries.add(Entry(9f, 43f))
        incomeEntries.add(Entry(10f, 87f))
        incomeEntries.add(Entry(11f, 43f))
        incomeEntries.add(Entry(12f, 60f))
        return incomeEntries.subList(0, 12)
    }

}