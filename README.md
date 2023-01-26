# FunFlix
FunFlix is a Kotlin-based movie app that consumes the TMDB API to display the current upcoming, trending, top-rated, and popular movies and TV shows. The app is built using the MVVM architecture and is powered by several popular libraries and frameworks.

## Getting Started
   To get started with the app, you'll need to obtain an API key from [TMDB](https://www.themoviedb.org/documentation/api) 
   1. Add the API key in the gradle.properties file within the root directory:
      ```
      API_KEY="YOUR_API_KEY"
      ```
   2. Open your .gitignore file and add the gradle.properties file so that it will not be included in the git repository, therefore not visible to others.
      ```
      gradle.properties
      ```
   3. In your app-level build.gradle file, add the following line in the buidConfig block
      ```
      buildConfig{
      ...
      buildConfigField("String", "API_KEY", API_KEY)
      }
      ```
   4. Finaly, you can access the key from anywhere in your app
      ```
      BuildConfig.API_KEY
      ```

## Tech Stack/Libraries
   * [Navigation Component](https://developer.android.com/guide/navigation) A Jetpack library that helps to navigate between fragments and activities.
   * [Retrofit](https://square.github.io/retrofit/) A type-safe HTTP client for Android and Java.
   * [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) A Kotlin library that helps to write asynchronous, non-blocking code.
   * [Hilt](https://dagger.dev/hilt/) A Dependency Injection library for Android.
   * [Glide](https://bumptech.github.io/glide/)  An image loading and caching library for Android.
   * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) A class that is responsible for preparing and managing the data for the UI.
   * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)  A data holder class that can be observed within a given lifecycle.
   * [Gson](https://github.com/google/gson) A Java library that can be used to convert Java Objects into their JSON representation.
   * [Okhttp](https://square.github.io/okhttp/) An HTTP client for Android and Java. Used to Log HTTP request and response data.
   * [Facebook Shimmer](https://github.com/facebook/shimmer-android) A library that provides a shimmering effect for loading placeholder.
