![license](https://img.shields.io/github/license/mathisdt/trackworktime.svg?style=flat)
[![Travis-CI Build](https://img.shields.io/travis/mathisdt/trackworktime.svg?label=Travis-CI%20Build&style=flat)](https://travis-ci.org/mathisdt/trackworktime/)
[![last released](https://img.shields.io/github/release-date/mathisdt/trackworktime.svg?label=last%20released&style=flat)](https://github.com/mathisdt/trackworktime/releases)

# Track Work Time
  
This app can track your work time easily! You can automate time tracking using geo-fencing functions (see below).
You may also **categorize each recorded interval** by a predefined client/task and a free text.
Of course, the list of clients/tasks can be edited to suit your needs.
  
Additionally, if you wish, your **flexible time account is taken care of**: you always see how much you worked.
You can also keep an eye on how much work time is left for today or for the current week (by a **notification**
which you can enable).
  
The app enables you to modify the planned working time effortlessly - just tap on the date you want to edit in the
main table.

You may provide the **geo-coordinates** or the **Wi-Fi network name** of your work place and the app can
**automatically clock you in** while you are at work. This is done **without using GPS**, so your battery won't
be emptied by this app. (You don't have to be connected to the WiFi network at work, it just has to be visible.)

If you prefer to use other apps like Llama or Tasker for tracking your movements, that's fine - **TWT can be
triggered from other apps** and just do the book-keeping of your work time. In this case, you have to create
broadcast intents called *org.zephyrsoft.trackworktime.ClockIn* or *org.zephyrsoft.trackworktime.ClockOut*.
When using ClockIn, you can also set the parameters *task=...* and *text=...* in the "extra" section of
the intent so your events are more meaningful. Here are some screenshots to point out how it can be done:
Llama: [Overview](https://zephyrsoft.org/images/llama-1.png),
[Detail 1](https://zephyrsoft.org/images/llama-2-detail-1.png),
[Detail 2](https://zephyrsoft.org/images/llama-3-detail-2.png) /
Tasker: [Detail 1](https://zephyrsoft.org/images/tasker-1.png),
[Detail 2](https://zephyrsoft.org/images/tasker-2.png).

If you have a **Pebble** smart watch, the app will notify you on clock-in and clock-out events which is especially
useful if you want to be in the know about automatic time tracking via location and/or WiFi.
  
Finally, the app can generate **reports** for you. The raw events report is the right thing if you want to
import your data somewhere else, while year/month/week reports are fine if you want to keep track of your
task progress.

Important note: **This app definitely won't use your personal data for anything you don't want!**
It uses the INTERNET permission only to offer you to send some information about crashes to the developer
(and does that only if you agree, you will be asked every time). The app does NOT include tracked times or
places in the bug report, but the general log file is appended and might potentially include personal data -
if so, it will be kept strictly confidential and only used to identify the problem.
  
[<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png"
     alt="Get it on Google Play"
     height="80">](https://play.google.com/store/apps/details?id=org.zephyrsoft.trackworktime)
[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/org.zephyrsoft.trackworktime/)

You can track the past development by looking at the [version history](https://zephyrsoft.org/trackworktime/history).  
  
**This is an open source project**, so if there's something you don't like, you are very welcome to
[file an issue](https://github.com/mathisdt/trackworktime/issues) or even fix things yourself and create a pull request.
Please don't try to communicate with me via reviews, that doesn't work in both directions.
You can always [write me an email](https://zephyrsoft.org/contact-about-me) and I'll see what I can do.
