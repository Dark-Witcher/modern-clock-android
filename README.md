# Modern Clock

```{=html}
<p align="center">
```
`<strong>`{=html}A minimalist Android clock widget inspired by KDE
Modern Clock.`</strong>`{=html}
```{=html}
</p>
```
```{=html}
<p align="center">
```
`<a href="https://github.com/Dark-Witcher/modern-clock-android/releases">`{=html}
`<img src="https://img.shields.io/github/v/release/Dark-Witcher/modern-clock-android?style=for-the-badge&label=Latest%20Release" alt="Latest Release">`{=html}
`</a>`{=html}
`<a href="https://github.com/Dark-Witcher/modern-clock-android/blob/main/LICENSE">`{=html}
`<img src="https://img.shields.io/github/license/Dark-Witcher/modern-clock-android?style=for-the-badge" alt="License">`{=html}
`</a>`{=html}
`<a href="https://github.com/Dark-Witcher/modern-clock-android">`{=html}
`<img src="https://img.shields.io/github/stars/Dark-Witcher/modern-clock-android?style=for-the-badge" alt="GitHub Stars">`{=html}
`</a>`{=html}
```{=html}
</p>
```
Modern Clock is a clean, highly customizable Android home-screen clock
widget based on the visual style of the **KDE Modern Clock** Plasma
widget by Prayag2.

It is designed to stay simple: a large stylized day, a date, and the
current time --- with no unnecessary clutter.

------------------------------------------------------------------------

## ✨ Features

-   🕐 **Minimalist home-screen clock widget**
-   📅 Customizable **date format**
-   ⏱️ Customizable **time format**
-   🔤 Stylized day display using the original Anurati typeface
-   🎨 Independent colors for:
    -   Day
    -   Date
    -   Time
-   ↔️ Adjustable **day letter spacing**
-   👁️ Option to **show or hide the day**
-   🖤 **True-black OLED mode**
-   🌗 Light, dark, and system appearance settings
-   📐 Resizable Android widget
-   🌑 Subtle text shadows for readability over wallpapers
-   ⚡ Updates automatically at the appropriate time boundary
-   📱 Works without Google Play Services

### Tested on

Modern Clock v1.0 has been tested successfully on:

-   Samsung Galaxy S9+ --- Android 14 / One UI 6
-   Samsung Galaxy S23 Ultra
-   Huawei MatePad 11 --- HarmonyOS 3

------------------------------------------------------------------------

## 🎨 Design

The Android version is inspired by the original **KDE Modern Clock**
widget, which itself takes inspiration from the Rainmeter *Mond* skin.

The goal is to preserve the distinctive minimalist appearance while
adapting the experience to Android's widget system.

The day uses **Anurati**, while the date and time use **Poppins**,
following the original project's visual approach.

------------------------------------------------------------------------

## 📲 Installation

### From GitHub Releases

1.  Download the latest `Modern-Clock-vX.X.apk` from the
    [Releases](https://github.com/Dark-Witcher/modern-clock-android/releases)
    page.
2.  Install the APK on your Android device.
3.  Open **Modern Clock** once after installation.
4.  Add **Modern Clock** from your home-screen widget picker.
5.  Configure the widget to your liking.

Android may ask you to allow installation from the source you used to
download the APK.

### Compatibility

-   **Minimum Android version:** Android 12 (API 31)
-   No Google Play Services required.
-   The widget is intended for Android-compatible launchers that support
    third-party home-screen widgets.

------------------------------------------------------------------------

## ⚙️ Configuration

Modern Clock provides settings for the main elements of the widget.

### Day

-   Show/hide the day
-   Adjust letter spacing
-   Choose the day color

### Date

-   Set a custom date format
-   Choose the date color

Examples:

``` text
dd MMM yyyy
dd MMMM yyyy
MMM dd yyyy
yyyy-MM-dd
```

### Time

-   Set a custom time format
-   Choose the time color

Examples:

``` text
HH:mm
hh:mm a
HH:mm:ss
HH mm
```

### Colors

Each element can be configured independently using the built-in color
picker.

You can enter colors directly as HEX values and view their RGB and HSL
representations.

------------------------------------------------------------------------

## 🛠️ Building from Source

### Requirements

-   Android Studio
-   Android SDK with API 31 or newer
-   JDK compatible with the Android Gradle Plugin used by the project

Clone the repository:

``` bash
git clone https://github.com/Dark-Witcher/modern-clock-android.git
cd modern-clock-android
```

Open the project in Android Studio and allow Gradle to synchronize.

To build a debug APK:

``` bash
./gradlew assembleDebug
```

The APK will be generated under:

``` text
app/build/outputs/apk/debug/
```

To create a release build, use:

**Android Studio → Build → Generate Signed App Bundle / APK**

A release build must be signed with your own keystore.

> **Never commit your signing keystore or its passwords to Git.**

------------------------------------------------------------------------

## 📦 Releases

Stable releases are published through GitHub Releases.

The first stable release is:

### v1.0

The v1.0 release focuses on the core Modern Clock experience:

-   Native Android home-screen widget
-   KDE-inspired visual design
-   Custom fonts
-   Date and time formatting
-   Per-element colors
-   Day visibility and letter spacing controls
-   Appearance settings
-   OLED mode
-   Widget resizing
-   Automatic clock updates

Future releases may expand typography controls and improve font
rendering and scaling.

------------------------------------------------------------------------

## 🗺️ Roadmap

### v1.1 and beyond

Planned improvements include:

-   More precise font width controls
-   More precise font height/scale controls
-   Further investigation into rendering the day text directly rather
    than as a bitmap
-   Additional typography customization
-   Continued compatibility testing across Android launchers and
    manufacturers

The project intentionally keeps the interface focused rather than
turning the widget into an overly complicated customization tool.

------------------------------------------------------------------------

## 🙏 Attribution

Modern Clock is based on the visual concept and design of **KDE Modern
Clock by Prayag2**:

https://github.com/Prayag2/kde_modernclock

The original KDE Modern Clock project is licensed under **GPL-3.0**.

This Android port is also released under **GPL-3.0**.

Created by **Dark Witcher**:

https://github.com/Dark-Witcher

------------------------------------------------------------------------

## 📄 License

Modern Clock is free and open-source software licensed under the **GNU
General Public License v3.0**.

See [`LICENSE`](LICENSE) for the complete license text.

------------------------------------------------------------------------

```{=html}
<p align="center">
```
`<strong>`{=html}Simple. Minimal. Always visible.`</strong>`{=html}
```{=html}
</p>
```
