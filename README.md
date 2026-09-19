<h1 align="center"> Modern Clock </h1>

> A minimalist, customizable Android home-screen clock widget inspired by KDE Modern Clock.

<div align="center">
  <a href="https://github.com/Dark-Witcher/modern-clock-android/releases">
    <img src="https://img.shields.io/github/v/release/Dark-Witcher/modern-clock-android?style=for-the-badge&label=Latest%20Release" alt="Latest Release">
  </a>
  <a href="https://github.com/Dark-Witcher/modern-clock-android/blob/main/LICENSE">
    <img src="https://img.shields.io/github/license/Dark-Witcher/modern-clock-android?style=for-the-badge" alt="License">
  </a>
  <a href="https://github.com/Dark-Witcher/modern-clock-android">
    <img src="https://img.shields.io/github/stars/Dark-Witcher/modern-clock-android?style=for-the-badge" alt="GitHub Stars">
  </a>
</div>

<div align="center">
  <img src="docs/images/hero.png" alt="Modern Clock running on a Samsung Galaxy S23 Ultra" width="900">
</div>

Modern Clock brings the distinctive KDE Modern Clock aesthetic to Android as a native home-screen widget.

It is deliberately simple: a stylized day, date, and time — with enough control to make it fit your wallpaper and your setup without turning the clock into a customization suite.

## ✨ Features

- 🕐 Minimalist Android home-screen clock widget
- 🔤 Stylized day display using the Anurati typeface
- 📅 Customizable date format
- ⏱️ Customizable time format
- 🎨 Independent colors for day, date, and time
- ↔️ Adjustable day letter spacing
- 🔠 Adjustable font sizing
- 👁️ Option to show or hide the day
- 🌑 Subtle text shadows for readability over wallpapers
- 🖤 True-black OLED mode
- 🌗 Independent Light, Dark, and Follow system widget appearance
- 📐 Resizable Android widget
- ⚡ Automatic clock updates
- 📱 No Google Play Services required


## 📱 Screenshots

### Home Screen

Modern Clock is designed to work naturally with your existing home screen and wallpaper.

<div align="center">
  <img src="docs/images/homescreen_showcase.png" alt="Modern Clock on phone and tablet home screens">
</div>

### Customization

Configure the widget directly from the app, with a live preview of your changes.

<div align="center">
  <img src="docs/images/settings_showcase.png" alt="Modern Clock widget settings on phone and tablet">
</div>

---

The settings screen gives you control over typography, spacing, formats, colors, shadows, and visibility.

## 🎨 Design

Modern Clock is inspired by the KDE Modern Clock Plasma widget, which itself takes inspiration from the Rainmeter Mond skin.

The Android version preserves the distinctive minimalist appearance while adapting it to Android's home-screen widget system.

The day uses **Anurati**, while the date and time use **Poppins**, following the visual approach of the original project.

## 📦 Installation

1. Go to the [Releases](https://github.com/Dark-Witcher/modern-clock-android/releases) page.
2. Download the latest `Modern Clock-vX.X.apk`.
3. Install the APK on your Android device.
4. Open Modern Clock once after installation.
5. Add Modern Clock from your home-screen widget picker.
6. Configure the widget to your liking.

Android may ask you to allow installation from the source you used to download the APK.

### Compatibility

- **Minimum Android version:** Android 12 (API 31)
- **Google Play Services:** Not required
- Requires an Android-compatible launcher with support for third-party home-screen widgets.

### Tested devices

Modern Clock has been tested on:

- Samsung Galaxy S9+ — Android 14 / One UI 6
- Samsung Galaxy S23 Ultra — Android 16 / One UI 8.5
- Huawei MatePad 11 — HarmonyOS 3
- Google Pixel 10 Pro — Android 17

## ⚙️ Configuration

Modern Clock provides independent controls for the main elements of the widget.

### Day

- Show or hide the day
- Adjust letter spacing
- Adjust font size
- Choose the day color
- Enable or disable a text shadow
- Choose the shadow color

### Date

- Set a custom date format
- Adjust font size
- Choose the date color
- Configure shadow settings

Examples:

```text
dd MMM yyyy
dd MMMM yyyy
MMM dd yyyy
yyyy-MM-dd
```

### Time

- Set a custom time format
- Adjust font size
- Choose the time color
- Configure shadow settings

Examples:

```text
HH:mm
hh:mm a
HH:mm:ss
HH mm
```

### Colors

Each element can be configured independently using the built-in color picker.

HEX values can be entered directly, with RGB and HSL representations available in the picker.

Widget colors can be configured independently for **Light** and **Dark** appearance:

- Light DAY, DATE, and TIME colors
- Dark DAY, DATE, and TIME colors
- Separate Light and Dark shadow colors

Existing color settings are preserved when upgrading from earlier versions.

### Appearance and OLED mode

The app supports:

- Same as device
- Light
- Dark
- True-black background for OLED displays

The widget has its own independent appearance setting:

- **Follow system** — follows the device's Light/Dark mode
- **Light** — always uses Light widget colors
- **Dark** — always uses Dark widget colors

### Widget Updates

Version 1.2 improves widget update reliability across different Android devices and launchers.

The widget now handles:

- Application updates
- Time changes
- Date changes
- Timezone changes
- System Light/Dark mode changes when **Follow system** is selected
- Widget refresh when opening the application

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

Build a debug APK:

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

> Never commit your signing keystore or its passwords to Git.

## 🚧 Current release

**v1.2 — Stable**

Version 1.2 expands widget appearance and customization controls, improves responsive sizing and rendering, and significantly improves widget update reliability across different devices and launchers.

See the [Releases](https://github.com/Dark-Witcher/modern-clock-android/releases) page for the latest build.

## 🗺️ Roadmap

### v1.2 — Completed

Version 1.2 focused on widget appearance, responsive sizing, rendering reliability, and improved compatibility across different Android devices and launchers.

#### Widget appearance

- Added **Follow system**, **Light**, and **Dark** widget appearance modes.
- Added independent Light and Dark colors for DAY, DATE, and TIME.
- Added independent Light and Dark shadow colors.
- Added Light/Dark color selection tabs.

#### App appearance

- Added **Same as device**, **Light**, **Dark**, and **True black** application appearance modes.

#### Responsive sizing

- Added independent size controls for DAY, DATE, and TIME.
- Improved responsive fitting when text becomes too large for the available widget space.
- Preserved the relationship between the sizes of the individual elements.

#### Widget reliability

- Improved widget updates after application updates.
- Improved handling of time, date, and timezone changes.
- Added detection of system Light/Dark changes for **Follow system** widgets.
- Improved compatibility with different Android launchers and manufacturers.

#### Battery usage

- Added access to Android's unrestricted battery usage setting for Modern Clock.

#### Known issue

On some devices, the widget may not visually update immediately when the system switches between **Light** and **Dark** mode while **Follow system** is selected. Modern Clock detects the change and updates the widget, but some launchers may delay displaying the updated appearance for up to a minute.

### v1.3 — Planned

Version 1.3 will focus primarily on bug fixes, compatibility improvements, and additional widget interaction.

#### Bug fixes

- Continue investigating and fixing bugs discovered during v1.2 testing.
- Address newly reported bugs where possible.
- Continue improving widget update and rendering reliability.
- Continue compatibility testing across different devices and launchers.

#### Widget interaction

- Add an option to choose **which application opens when the widget is clicked**.

#### Additional improvements

Additional small improvements and fixes will be considered during v1.3 development based on testing and user feedback.

### v2.0 — Planned

Version 2 is planned as the major long-term update to Modern Clock.

The current direction for v2 includes a significant typography and rendering update, with planned work around:

- Custom font support and the replacement of the current Anurati-based DAY rendering
- Further typography and font-scaling improvements
- More advanced customization of the individual widget elements
- Continued rendering-quality and compatibility improvements
- Additional customization while keeping the interface focused and simple

The exact v2 feature set will be defined during the v1.x development cycle.

The project intentionally keeps the interface focused rather than turning the widget into an overly complicated customization tool.

## 🙏 Attribution

Modern Clock is based on the visual concept and design of **KDE Modern Clock by Prayag2**:

https://github.com/Prayag2/kde_modernclock

The original KDE Modern Clock project is licensed under GPL-3.0.

This Android port is also released under GPL-3.0.

Created by **Dark Witcher**:

https://github.com/Dark-Witcher

## 📄 License

Modern Clock is free and open-source software licensed under the **GNU General Public License v3.0**.

See [`LICENSE`](LICENSE) for the complete license text.

---

<div align="center"> **Simple. Minimal. Always visible.** </div>
