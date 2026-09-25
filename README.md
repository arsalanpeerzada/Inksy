# Inksy

With Inksy you can make creative journals using text, images and drawings and share them however you want. You can add art from our Doodle store to embellish your journals or upload your own art to sell on the Doodle store yourself!

You have complete control over who can see your journal, from keeping it totally private to sharing it with a small group of select people, or making it publicly available on our network for anyone to read.

Want a daily habit tracker just for yourself? You can do that with Inksy. Want to make a scrapbook of your favorite baby milestones, but keep it limited to close family? You can do that with Inksy. Want to share your comedy, your fashion, or your dreams with the whole world? Go right ahead!

Comment on and follow your favorite users or just the journals you like, it's up to you. You'll never miss updates from your favorites and easily avoid the content you DON’T want to see. It's social media without the social anxiety.

Share your story. Be inspired. Skip the drama.

## 1. Idea
Inksy is a platform designed to provide a safe, customizable, and creative journaling experience. Users can build rich, interactive journals incorporating doodles, text, and photos. Unlike traditional social media, it emphasizes user control over privacy and audience, allowing for a personalized space that ranges from a private diary to a public blog, all while supporting a creator economy via the Doodle store.

## 2. Tech Stack
- **Language:** Kotlin
- **Architecture:** MVVM (Model-View-ViewModel)
- **Minimum SDK:** 26 (Android 8.0)
- **Target SDK:** 34 (Android 14)
- **Backend / BaaS:** Firebase (Authentication, Realtime Database, Storage, Cloud Messaging, Analytics, AppCheck)
- **Build System:** Gradle

## 3. Libraries Used
- **UI & Navigation:**
  - `androidx.navigation` (Navigation Component for UI flow)
  - `com.google.android.material` (Material Design Components)
  - `com.intuit.sdp:sdp-android` (Scalable size units for responsive layouts)
  - `de.hdodenhof:circleimageview` (Circular image views)
  - `com.github.Deishelon:RoundedBottomSheet` (Bottom sheet dialogs)
  - `com.github.hyuwah:DraggableView` & `com.github.varunest:sparkbutton`
- **Networking & API:**
  - `com.squareup.retrofit2:retrofit` (REST API communication)
  - `com.squareup.okhttp3:okhttp` (HTTP client with logging interceptor)
- **Image Processing & Loading:**
  - `com.github.bumptech.glide:glide` (Image loading and caching)
  - `com.github.yalantis:ucrop` & `com.github.chrisbanes:PhotoView` (Image cropping and zooming)
  - `id.zelory:compressor` (Image compression)
- **Data Persistence:**
  - `androidx.room:room-ktx` (Room Database for local storage)
- **Concurrency:**
  - `org.jetbrains.kotlinx:kotlinx-coroutines-android` (Coroutines for asynchronous programming)
- **Utilities & Features:**
  - `com.hbb20:ccp` (Country Code Picker)
  - `com.github.mukeshsolanki:android-otpview-pinview` (OTP verification UI)
  - `jp.wasabeef:richeditor-android` (Rich text editing for journals)
  - `com.github.PhilJay:MPAndroidChart` (Data visualization/charts)
  - `com.android.billingclient:billing` (Google Play Billing for In-App Purchases)

## 4. Things I Learned From This
- **Complex UI Building:** Building a rich text editor and canvas for drawings/doodles on Android requires careful handling of touch events, image manipulation, and UI performance.
- **Firebase Integration:** Managing a full-fledged backend with Firebase Realtime Database and Cloud Storage, while ensuring security and data integrity using AppCheck and Firebase Authentication.
- **Responsive Design:** Using scalable density-independent pixels (SDP) to ensure the application UI looks consistent across various Android screen sizes and densities.
- **Privacy Controls:** Designing a database schema and application logic that robustly handles varied privacy settings (private, friends-only, public) for user-generated content.
- **Monetization & In-App Purchases:** Integrating the Google Play Billing Library to handle a digital economy (the Doodle store) within the app, understanding product fulfillment and purchase verification.