# Bake It - Baking recipes browser

<img src="https://user-images.githubusercontent.com/15446842/35539542-f7dbb142-0549-11e8-96c6-4de7a89f1801.png"/>

Bake It is baking recipe browser for Android tablets and smartphones running Android Lollipop (5.0) or newer. It allows the user to select a recipe and see video-guided steps for how to complete it. The app is built using [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/guide.html) such as the Lifecycle library, which includes LiveData and ViewModel, as well as Room persistence library.

**App Features:**
* Displays recipes from provided network resource
* Periodically updates recipe data in the background
* Stores recipe data locally to allow offline browsing
* Plays recipe videos using ExoPlayer including fullscreen mode
* Utilises RecyclerViews to show recipe, ingredient and step lists
* Displays ingredient list for desired recipe in homescreen widget
* Supports diffrent screen sizes by utilising master detail flow
* Includes isolated user interface test suite

## Try it out
To install the app on a connected device or running emulator, run:

```gradle
git clone https://github.com/mattwiduch/BakeIt.git
cd BakeIt
./gradlew installDebug
```

## Dependencies
Bake It uses following libraries:
- [Android Support Library](https://developer.android.com/topic/libraries/support-library/index.html)
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html)
- [Butter Knife](http://jakewharton.github.io/butterknife/) for View binding
- [Circle ImageView](https://github.com/hdodenhof/CircleImageView/) for circular Views
- [Dagger2](https://github.com/google/dagger/) for dependency injection
- [Espresso](https://developer.android.com/training/testing/espresso/index.html) for user interface tests
- [ExoPlayer](https://github.com/google/ExoPlayer/) for playing media
- [Expandable Layout](https://github.com/cachapa/ExpandableLayout/) for expansion and collapse of layouts
- [Firebase Job Dispatcher](https://github.com/firebase/firebase-jobdispatcher-android) for scheduling background jobs
- [Glide](https://github.com/bumptech/glide/) for image loading
- [GSON](https://github.com/google/gson/) for JSON conversion
- [Mockito](https://github.com/mockito/mockito/) for mocking in tests
- [Retrofit](https://github.com/square/retrofit/) for REST api communication

## License
```
Copyright (C) 2018 Mateusz Widuch

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
