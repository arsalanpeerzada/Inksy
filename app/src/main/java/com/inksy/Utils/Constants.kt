package com.inksy.UI

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Environment
import android.text.format.DateFormat
import android.view.View
import com.inksy.Model.Model
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.*


class Constants {

    companion object {

        private const val REQUEST_EXTERNAL_STORAGe = 1
        private val permissionstorage = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )


        const val IDCHECK = "IDCHECK"
        const val googlePlayLicenseKey =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoJM6733YjgNVOUO5/B53qi78JkHNsl4bssmOGMrPgkLn7bzC1YTNO3F38kmPvbsbBDYFXnT7ugUIawLphlGT3o3ZJL91EMpzk0l9T99ba6l7mhhX5ynkOV+LPkapbZqE0ng9mr/Wo169WNiim3kUEfs7XyUjZnKlfHW0F45dhnGKgHdFG6IXryoGouyOxVYY1l3iffdTQOJjcSAXfRpmXqT9eGOWsMkKnt00ALrv0iYE+z/9rIscSppI9yqKvj9GJpz6KuD2o7Pfo4pncNu5D5SDWrnUGSLqNT54qFxgYxvOz6lIPur/GFJneQv1tzEEc+y161OSTpdxj6q6+rgZPwIDAQAB"
        const val googlePlayDoodlePackPrice1 = "com.inksy.doodlepackprice1"
        const val googlePlayDoodlePackPrice2 = "com.inksy.doodlepackprice2"
        const val googlePlayDoodlePackPrice3 = "com.inksy.doodlepackprice3"
        const val googlePlayDoodlePackPrice4 = "com.inksy.doodlepackprice4"
        const val googlePlayDoodlePackPrice5 = "com.inksy.doodlepackprice5_"
        const val googlePlayTestingProductID = "android.test.purchased"

        const val BASE_URL = "https://admin.inksyapp.com/api/v1/"
        const val BASE_IMAGE = "https://admin.inksyapp.com/public/assets/uploads/images/"
        const val BASE_THUMBNAIL = "https://admin.inksyapp.com/public/assets/uploads/thumbnails/"


//        const val BASE_URL = "https://www.inksy.appshah.us/api/v1/"
//        const val BASE_IMAGE = "https://inksy.appshah.us/assets/uploads/images/"
//        const val BASE_THUMBNAIL = "https://inksy.appshah.us/assets/uploads/thumbnails/"


        const val LINKFORBULLET = 100001
        const val LINKFORHEADER = 100000
        const val APP_NAME = "INSKSY"
        const val CREATEJOURNALINDEX = "CreateJournalIndex"
        const val CREATEJOURNALENTRY = "CreateJournalEntry"
        var sub_journalViewAll = "SubJournalViewAll"
        var doodleViewAll = "DoodleViewAll"

        var BULLETITEMCHECK = "bulletItemCheck"

        var sub_journalSearch = "SubJournalSearch"
        var doodleSearch = "DoodleSearch"
        var peopleSearch = "PeopleSearch"
        var peopleViewAll = "PeopleViewAll"
        var fragment_approved = "fragment_approved"
        var fragment_pending = "fragment_pending"

        var packActivity = "PackActivity"
        var people = "People"
        var activity = "activity"
        var amountPaid = "Amount Paid"
        var journalType = "JournalType"
        var fromAdapter = "fromAdapter"
        var private_data = "private_data"
        var isLogin = "isLogin"
        var person = "Person"

        public fun readFromAsset(context: Context): MutableList<Model> {
            val modeList = mutableListOf<Model>()
            val bufferReader = context.assets.open("android_version.json").bufferedReader()
            val json_string = bufferReader.use {
                it.readText()
            }
            val jsonArray = JSONArray(json_string);

            for (i in 0 until jsonArray.length()) {
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)

                val model = Model(jsonObject.getString("name"), jsonObject.getString("version"))
                modeList.add(model)
            }

            return modeList
        }

        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return if (activeNetwork != null && (activeNetwork.type == ConnectivityManager.TYPE_WIFI || activeNetwork.type == ConnectivityManager.TYPE_MOBILE)) {
                true
            } else {
                false
            }
        }


        public fun takeScreenshot(view: View, applicationContext: Context): File {
            val now = Date()
            DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)
            val mPath =
                getfilepath(applicationContext)

            // create bitmap screen capture
            val v1: View = view
            v1.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(v1.drawingCache)
            v1.isDrawingCacheEnabled = false
            val imageFile = File(mPath)
            val outputStream = FileOutputStream(imageFile)
            val quality = 100
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()

            return imageFile
            //  openScreenshot(imageFile)

        }


        fun getfilepath(applicationContext: Context): String {
            val now = Date()
            var time = DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)

            var contextWrapper = ContextWrapper(applicationContext)
            var inksypictures = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            var name = "$time.jpg"
            var file = File(inksypictures, name)
            return file.path
        }

    }


}

