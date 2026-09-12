# Modern Clock

<p align="center">
  <strong>A minimalist Android clock widget inspired by KDE Modern Clock.</strong>
</p>

<p align="center">
  <a href="https://github.com/Dark-Witcher/modern-clock-android/releases">
    <img src="https://img.shields.io/github/v/release/Dark-Witcher/modern-clock-android?style=for-the-badge&label=Latest%20Release" alt="Latest Release">
  </a>
  <a href="https://github.com/Dark-Witcher/modern-clock-android/blob/main/LICENSE">
    <img src="https://img.shields.io/github/license/Dark-Witcher/modern-clock-android?style=for-the-badge" alt="License">
  </a>
  <a href="https://github.com/Dark-Witcher/modern-clock-android">
    <img src="https://img.shields.io/github/stars/Dark-Witcher/modern-clock-android?style=for-the-badge" alt="GitHub Stars">
  </a>
</p>

Modern Clock is a clean, customizable Android home-screen clock widget based on the visual style of **KDE Modern Clock** by Prayag2.

It is designed to stay simple: a large stylized day, a date, and the current time — with no unnecessary clutter.

---

## ✨ Features

- 🕐 **Minimalist home-screen clock widget**
- 📅 Customizable **date format**
- ⏱️ Customizable **time format**
- 🔤 Stylized day display using the Anurati typeface
- 🎨 Independent colors for day, date, and time
- ↔️ Adjustable **day letter spacing**
- 👁️ Option to **show or hide the day**
- 🖤 **True-black OLED mode**
- 🌗 Light, dark, and system appearance settings
- 📐 Resizable Android widget
- 🌑 Subtle text shadows for readability over wallpapers
- ⚡ Automatic clock updates
- 📱 No Google Play Services required

### Tested on

Modern Clock v1.0 has been tested successfully on:

- Samsung Galaxy S9+ — Android 14 / One UI 6
- Samsung Galaxy S23 Ultra
- Huawei MatePad 11 — HarmonyOS 3

---

## 🎨 Design

Modern Clock is inspired by the **KDE Modern Clock** Plasma widget, which itself takes inspiration from the Rainmeter *Mond* skin.

The Android version aims to preserve the distinctive minimalist appearance while adapting it to Android's home-screen widget system.

The day uses **Anurati**, while the date and time use **Poppins**, following the visual approach of the original project.

---

## 📲 Installation

1. Go to the [Releases](https://github.com/Dark-Witcher/modern-clock-android/releases) page.
2. Download the latest `Modern-Clock-vX.X.apk`.
3. Install the APK on your Android device.
4. Open **Modern Clock** once after installation.
5. Add **Modern Clock** from your home-screen widget picker.
6. Configure the widget to your liking.

Android may ask you to allow installation from the source you used to download the APK.

### Compatibility

- **Minimum Android version:** Android 12 (API 31)
- No Google Play Services required.
- Requires an Android-compatible launcher with support for third-party home-screen widgets.

---

## ⚙️ Configuration

Modern Clock provides settings for the main elements of the widget.

### Day

- Show or hide the day
- Adjust letter spacing
- Choose the day color

### Date

- Set a custom date format
- Choose the date color

Examples:

```text
dd MMM yyyy
dd MMMM yyyy
MMM dd yyyy
yyyy-MM-dd
```

### Time

- Set a custom time format
- Choose the time color

Examples:

```text
HH:mm
hh:mm a
HH:mm:ss
HH mm
```

### Colors

Each element can be configured independently using the built-in color picker.

You can enter colors directly as HEX values and view their RGB and HSL representations.

---

## 🛠️ Building from Source

### Requirements

- Android Studio
- Android SDK with API 31 or newer
- JDK compatible with the Android Gradle Plugin used by the project

Clone the repository:

```bash
git clone https://github.com/Dark-Witcher/modern-clock-android.git
cd modern-clock-android
```

Open the project in Android Studio and allow Gradle to synchronize.

To build a debug APK:

```bash
./gradlew assembleDebug
```

The APK will be generated under:

```text
app/build/outputs/apk/debug/
```

To create a release APK, use:

**Android Studio → Build → Generate Signed App Bundle / APK**

A release build must be signed with your own keystore.

> **Never commit your signing keystore or its passwords to Git.**

---

## 📦 v1.0

The first stable release focuses on the core Modern Clock experience:

- Native Android home-screen widget
- KDE-inspired visual design
- Custom fonts
- Date and time formatting
- Per-element colors
- Day visibility and letter spacing controls
- Appearance settings
- OLED mode
- Widget resizing
- Automatic clock updates
- Subtle text shadows

Future releases may expand typography controls and improve font rendering and scaling.

---

## 🗺️ Roadmap

### v1.1 and beyond

Planned improvements include:

- More precise font width controls
- More precise font height/scale controls
- Further investigation into rendering the day text directly rather than as a bitmap
- Additional typography customization
- Continued compatibility testing across Android launchers and manufacturers

The project intentionally keeps the interface focused rather than turning the widget into an overly complicated customization tool.

---

## 🙏 Attribution

Modern Clock is based on the visual concept and design of **KDE Modern Clock by Prayag2**:

https://github.com/Prayag2/kde_modernclock

The original KDE Modern Clock project is licensed under **GPL-3.0**.

This Android port is also released under **GPL-3.0**.

Created by **Dark Witcher**:

https://github.com/Dark-Witcher

---

## 📄 License

Modern Clock is free and open-source software licensed under the **GNU General Public License v3.0**.

See [`LICENSE`](LICENSE) for the complete license text.

---

<p align="center">
  <strong>Simple. Minimal. Always visible.</strong>
</p>
