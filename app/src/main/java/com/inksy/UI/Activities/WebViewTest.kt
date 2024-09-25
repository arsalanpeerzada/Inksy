package com.inksy.UI.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.inksy.R
import com.inksy.databinding.ActivityWebViewTestBinding

class WebViewTest : AppCompatActivity() {

    lateinit var binding: ActivityWebViewTestBinding

    private var journalDetail: JournalDetail? = null // Your data model class
    private var page_num: Int = 0
    private var journal_id: Int = 0
    private var user_doodles: ArrayList<UserDoodle> = ArrayList<UserDoodle>() // Replace with your actual model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_test)

        binding = ActivityWebViewTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWebView()

        // Load your URL like the iframe
        binding.webview .loadUrl("https://canva-editor-self.vercel.app/")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webview .settings.javaScriptEnabled = true
        binding.webview .webChromeClient = WebChromeClient()

        // Handle messages from the WebView
        binding.webview .addJavascriptInterface(WebAppInterface(this), "Android")

        binding.webview .webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                handleIframeLoad()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }
    }

    // Function to send messages to WebView (similar to postMessage)
    private fun sendMessageToWebView(name: String, msg: String) {
        binding.webview .evaluateJavascript("window.postMessage({ '$name': '$msg' }, '*');", null)
    }

    // Handle iframe load similar to the React version
    private fun handleIframeLoad() {
        journalDetail?.let { detail ->
            if (detail.html_content != null) {
                sendMessageToWebView("html_content", detail.html_content ?: "")
            } else {
                val image = if (isUploadedFile(detail.page_image)) {
                    detail.page_image ?: ""
                } else {
                    getImage(detail.page_image ?: "") ?: ""
                }
                sendMessageToWebView("bg_image", image)
            }
        }

        if (user_doodles.isNotEmpty()) {
            // Convert user doodles to JSON-like format and send to WebView
            val doodleImages = user_doodles.joinToString(separator = ",") { "{img: \"${getImage(
                user_doodles.toString()
            )}\"}" }
            sendMessageToWebView("doodle_images", "[$doodleImages]")
        }


        // Send page_num and journal_id
        if (page_num != 0 && journal_id != 0) {
            sendMessageToWebView("default_page_index", page_num.toString())
        }
    }

    // Mocking the helper methods
    private fun isUploadedFile(image: String?): Boolean {
        // Implement your logic
        return true
    }

    private fun getImage(image: String?): String? {
        // Implement your logic to get image
        return image
    }

    // Interface to handle messages from the WebView (JavaScript calling Kotlin)
    class WebAppInterface(private val activity: WebViewTest) {

        @JavascriptInterface
        fun handle_back() {
            activity.onBackPressed()
        }

        @JavascriptInterface
        fun handle_save(data: String) {
            // Handle save logic with received data
            // data could be a JSON string containing the html_content and other details
        }
    }
}

data class JournalDetail(
    val html_content: String?, // HTML content for the journal
    val page_image: String? // Background image or any other relevant field
)

data class UserDoodle(
    val img: String // URL or path to the doodle image
)