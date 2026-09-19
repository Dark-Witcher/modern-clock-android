package com.example.modernclock

import android.app.AlertDialog
import android.app.WallpaperManager
import android.appwidget.AppWidgetManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Environment
import android.os.PowerManager
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColorInt
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.max
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    companion object {

        private const val PREFS_NAME =
            "modern_clock_app_preferences"

        private const val KEY_TIME_FORMAT =
            "time_format"

        private const val KEY_OLD_USE_24_HOUR =
            "use_24_hour"

        private const val KEY_DATE_FORMAT =
            "date_format"

        private const val KEY_SHOW_DAY =
            "show_day"

        private const val KEY_TRUE_BLACK =
            "true_black"

        private const val KEY_DAY_COLOR =
            "day_color"

        private const val KEY_DATE_COLOR =
            "date_color"

        private const val KEY_TIME_COLOR =
            "time_color"

        private const val KEY_WIDGET_APPEARANCE =
            "widget_appearance"

        private const val KEY_WIDGET_LIGHT_DAY_COLOR =
            "widget_light_day_color"

        private const val KEY_WIDGET_LIGHT_DATE_COLOR =
            "widget_light_date_color"

        private const val KEY_WIDGET_LIGHT_TIME_COLOR =
            "widget_light_time_color"

        private const val KEY_WIDGET_DARK_DAY_COLOR =
            "widget_dark_day_color"

        private const val KEY_WIDGET_DARK_DATE_COLOR =
            "widget_dark_date_color"

        private const val KEY_WIDGET_DARK_TIME_COLOR =
            "widget_dark_time_color"

        private const val KEY_DAY_LETTER_SPACING =
            "day_letter_spacing"

        private const val KEY_DAY_FONT_SIZE =
            "day_font_size"

        private const val KEY_DATE_FONT_SIZE =
            "date_font_size"

        private const val KEY_TIME_FONT_SIZE =
            "time_font_size"

        private const val KEY_DAY_SHADOW_ENABLED =
            "day_shadow_enabled"

        private const val KEY_DAY_SHADOW_COLOR =
            "day_shadow_color"

        private const val KEY_DATE_SHADOW_ENABLED =
            "date_shadow_enabled"

        private const val KEY_DATE_SHADOW_COLOR =
            "date_shadow_color"

        private const val KEY_TIME_SHADOW_ENABLED =
            "time_shadow_enabled"

        private const val KEY_TIME_SHADOW_COLOR =
            "time_shadow_color"

        private const val KEY_WIDGET_DARK_DAY_SHADOW_COLOR =
            "widget_dark_day_shadow_color"

        private const val KEY_WIDGET_DARK_DATE_SHADOW_COLOR =
            "widget_dark_date_shadow_color"

        private const val KEY_WIDGET_DARK_TIME_SHADOW_COLOR =
            "widget_dark_time_shadow_color"

        private const val KEY_DAY_SECTION_EXPANDED =
            "day_section_expanded"

        private const val KEY_DATE_SECTION_EXPANDED =
            "date_section_expanded"

        private const val KEY_TIME_SECTION_EXPANDED =
            "time_section_expanded"

        private const val DEFAULT_TIME_FORMAT =
            "HH:mm"

        private const val DEFAULT_DATE_FORMAT =
            "dd MMM yyyy"

        private const val DEFAULT_COLOR =
            "#FFFFFF"

        private const val DEFAULT_SHADOW_COLOR =
            "#000000"

        private const val DEFAULT_DAY_LETTER_SPACING =
            17f

        private const val DEFAULT_FONT_SIZE_PERCENT =
            100

        private const val MIN_FONT_SIZE_PERCENT =
            50

        private const val MAX_FONT_SIZE_PERCENT =
            200

        fun saveGlobalTimeFormat(
            context: Context,
            timeFormat: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_TIME_FORMAT,
                    timeFormat
                )
            }
        }

        fun getGlobalTimeFormat(
            context: Context
        ): String {

            val preferences =
                context.getSharedPreferences(
                    PREFS_NAME,
                    MODE_PRIVATE
                )

            if (
                preferences.contains(
                    KEY_TIME_FORMAT
                )
            ) {
                return preferences.getString(
                    KEY_TIME_FORMAT,
                    DEFAULT_TIME_FORMAT
                ) ?: DEFAULT_TIME_FORMAT
            }

            if (
                preferences.contains(
                    KEY_OLD_USE_24_HOUR
                )
            ) {
                val oldUse24Hour =
                    preferences.getBoolean(
                        KEY_OLD_USE_24_HOUR,
                        true
                    )

                return if (oldUse24Hour) {
                    "HH:mm"
                } else {
                    "hh:mm a"
                }
            }

            return DEFAULT_TIME_FORMAT
        }

        fun saveGlobalDateFormat(
            context: Context,
            dateFormat: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_DATE_FORMAT,
                    dateFormat
                )
            }
        }

        fun getGlobalDateFormat(
            context: Context
        ): String {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getString(
                KEY_DATE_FORMAT,
                DEFAULT_DATE_FORMAT
            ) ?: DEFAULT_DATE_FORMAT
        }

        fun saveGlobalShowDay(
            context: Context,
            showDay: Boolean
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putBoolean(
                    KEY_SHOW_DAY,
                    showDay
                )
            }
        }

        fun getGlobalShowDay(
            context: Context
        ): Boolean {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getBoolean(
                KEY_SHOW_DAY,
                true
            )
        }

        fun saveGlobalDayLetterSpacing(
            context: Context,
            spacing: Float
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putFloat(
                    KEY_DAY_LETTER_SPACING,
                    spacing
                )
            }
        }

        fun getGlobalDayLetterSpacing(
            context: Context
        ): Float {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getFloat(
                KEY_DAY_LETTER_SPACING,
                DEFAULT_DAY_LETTER_SPACING
            )
        }

        fun saveGlobalDayFontSize(
            context: Context,
            sizePercent: Int
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putInt(
                    KEY_DAY_FONT_SIZE,
                    sizePercent.coerceIn(
                        MIN_FONT_SIZE_PERCENT,
                        MAX_FONT_SIZE_PERCENT
                    )
                )
            }
        }

        fun getGlobalDayFontSize(
            context: Context
        ): Int {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getInt(
                KEY_DAY_FONT_SIZE,
                DEFAULT_FONT_SIZE_PERCENT
            ).coerceIn(
                MIN_FONT_SIZE_PERCENT,
                MAX_FONT_SIZE_PERCENT
            )
        }

        fun saveGlobalDateFontSize(
            context: Context,
            sizePercent: Int
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putInt(
                    KEY_DATE_FONT_SIZE,
                    sizePercent.coerceIn(
                        MIN_FONT_SIZE_PERCENT,
                        MAX_FONT_SIZE_PERCENT
                    )
                )
            }
        }

        fun getGlobalDateFontSize(
            context: Context
        ): Int {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getInt(
                KEY_DATE_FONT_SIZE,
                DEFAULT_FONT_SIZE_PERCENT
            ).coerceIn(
                MIN_FONT_SIZE_PERCENT,
                MAX_FONT_SIZE_PERCENT
            )
        }

        fun saveGlobalTimeFontSize(
            context: Context,
            sizePercent: Int
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putInt(
                    KEY_TIME_FONT_SIZE,
                    sizePercent.coerceIn(
                        MIN_FONT_SIZE_PERCENT,
                        MAX_FONT_SIZE_PERCENT
                    )
                )
            }
        }

        fun getGlobalTimeFontSize(
            context: Context
        ): Int {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getInt(
                KEY_TIME_FONT_SIZE,
                DEFAULT_FONT_SIZE_PERCENT
            ).coerceIn(
                MIN_FONT_SIZE_PERCENT,
                MAX_FONT_SIZE_PERCENT
            )
        }

        fun saveGlobalDayShadowEnabled(
            context: Context,
            enabled: Boolean
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putBoolean(
                    KEY_DAY_SHADOW_ENABLED,
                    enabled
                )
            }
        }

        fun getGlobalDayShadowEnabled(
            context: Context
        ): Boolean {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getBoolean(
                KEY_DAY_SHADOW_ENABLED,
                true
            )
        }

        fun saveGlobalDayShadowColor(
            context: Context,
            color: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_DAY_SHADOW_COLOR,
                    color
                )
            }
        }

        fun getGlobalDayShadowColor(
            context: Context
        ): String {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getString(
                KEY_DAY_SHADOW_COLOR,
                DEFAULT_SHADOW_COLOR
            ) ?: DEFAULT_SHADOW_COLOR
        }

        fun saveGlobalDateShadowEnabled(
            context: Context,
            enabled: Boolean
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putBoolean(
                    KEY_DATE_SHADOW_ENABLED,
                    enabled
                )
            }
        }

        fun getGlobalDateShadowEnabled(
            context: Context
        ): Boolean {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getBoolean(
                KEY_DATE_SHADOW_ENABLED,
                true
            )
        }

        fun saveGlobalDateShadowColor(
            context: Context,
            color: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_DATE_SHADOW_COLOR,
                    color
                )
            }
        }

        fun getGlobalDateShadowColor(
            context: Context
        ): String {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getString(
                KEY_DATE_SHADOW_COLOR,
                DEFAULT_SHADOW_COLOR
            ) ?: DEFAULT_SHADOW_COLOR
        }

        fun saveGlobalTimeShadowEnabled(
            context: Context,
            enabled: Boolean
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putBoolean(
                    KEY_TIME_SHADOW_ENABLED,
                    enabled
                )
            }
        }

        fun getGlobalTimeShadowEnabled(
            context: Context
        ): Boolean {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getBoolean(
                KEY_TIME_SHADOW_ENABLED,
                true
            )
        }

        fun saveGlobalTimeShadowColor(
            context: Context,
            color: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_TIME_SHADOW_COLOR,
                    color
                )
            }
        }

        fun getGlobalTimeShadowColor(
            context: Context
        ): String {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getString(
                KEY_TIME_SHADOW_COLOR,
                DEFAULT_SHADOW_COLOR
            ) ?: DEFAULT_SHADOW_COLOR
        }

        fun saveGlobalDaySectionExpanded(
            context: Context,
            expanded: Boolean
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putBoolean(
                    KEY_DAY_SECTION_EXPANDED,
                    expanded
                )
            }
        }

        fun getGlobalDaySectionExpanded(
            context: Context
        ): Boolean {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getBoolean(
                KEY_DAY_SECTION_EXPANDED,
                false
            )
        }

        fun saveGlobalDateSectionExpanded(
            context: Context,
            expanded: Boolean
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putBoolean(
                    KEY_DATE_SECTION_EXPANDED,
                    expanded
                )
            }
        }

        fun getGlobalDateSectionExpanded(
            context: Context
        ): Boolean {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getBoolean(
                KEY_DATE_SECTION_EXPANDED,
                false
            )
        }

        fun saveGlobalTimeSectionExpanded(
            context: Context,
            expanded: Boolean
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putBoolean(
                    KEY_TIME_SECTION_EXPANDED,
                    expanded
                )
            }
        }

        fun getGlobalTimeSectionExpanded(
            context: Context
        ): Boolean {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getBoolean(
                KEY_TIME_SECTION_EXPANDED,
                false
            )
        }

        fun isTrueBlackEnabled(
            context: Context
        ): Boolean {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getBoolean(
                KEY_TRUE_BLACK,
                false
            )
        }

        fun saveGlobalWidgetAppearance(
            context: Context,
            appearance: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_WIDGET_APPEARANCE,
                    appearance
                )
            }
        }

        fun getGlobalWidgetAppearance(
            context: Context
        ): String {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getString(
                KEY_WIDGET_APPEARANCE,
                "follow_system"
            ) ?: "follow_system"
        }

        fun saveGlobalWidgetLightDayColor(
            context: Context,
            color: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_WIDGET_LIGHT_DAY_COLOR,
                    color
                )
                putString(
                    KEY_DAY_COLOR,
                    color
                )
            }
        }

        fun getGlobalWidgetLightDayColor(
            context: Context
        ): String {
            val preferences =
                context.getSharedPreferences(
                    PREFS_NAME,
                    MODE_PRIVATE
                )

            return preferences.getString(
                KEY_WIDGET_LIGHT_DAY_COLOR,
                null
            ) ?: preferences.getString(
                KEY_DAY_COLOR,
                DEFAULT_COLOR
            ) ?: DEFAULT_COLOR
        }

        fun saveGlobalWidgetLightDateColor(
            context: Context,
            color: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_WIDGET_LIGHT_DATE_COLOR,
                    color
                )
                putString(
                    KEY_DATE_COLOR,
                    color
                )
            }
        }

        fun getGlobalWidgetLightDateColor(
            context: Context
        ): String {
            val preferences =
                context.getSharedPreferences(
                    PREFS_NAME,
                    MODE_PRIVATE
                )

            return preferences.getString(
                KEY_WIDGET_LIGHT_DATE_COLOR,
                null
            ) ?: preferences.getString(
                KEY_DATE_COLOR,
                DEFAULT_COLOR
            ) ?: DEFAULT_COLOR
        }

        fun saveGlobalWidgetLightTimeColor(
            context: Context,
            color: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_WIDGET_LIGHT_TIME_COLOR,
                    color
                )
                putString(
                    KEY_TIME_COLOR,
                    color
                )
            }
        }

        fun getGlobalWidgetLightTimeColor(
            context: Context
        ): String {
            val preferences =
                context.getSharedPreferences(
                    PREFS_NAME,
                    MODE_PRIVATE
                )

            return preferences.getString(
                KEY_WIDGET_LIGHT_TIME_COLOR,
                null
            ) ?: preferences.getString(
                KEY_TIME_COLOR,
                DEFAULT_COLOR
            ) ?: DEFAULT_COLOR
        }

        fun saveGlobalWidgetDarkDayColor(
            context: Context,
            color: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_WIDGET_DARK_DAY_COLOR,
                    color
                )
            }
        }

        fun getGlobalWidgetDarkDayColor(
            context: Context
        ): String {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getString(
                KEY_WIDGET_DARK_DAY_COLOR,
                DEFAULT_COLOR
            ) ?: DEFAULT_COLOR
        }

        fun saveGlobalWidgetDarkDateColor(
            context: Context,
            color: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_WIDGET_DARK_DATE_COLOR,
                    color
                )
            }
        }

        fun getGlobalWidgetDarkDateColor(
            context: Context
        ): String {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getString(
                KEY_WIDGET_DARK_DATE_COLOR,
                DEFAULT_COLOR
            ) ?: DEFAULT_COLOR
        }

        fun saveGlobalWidgetDarkTimeColor(
            context: Context,
            color: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_WIDGET_DARK_TIME_COLOR,
                    color
                )
            }
        }

        fun getGlobalWidgetDarkTimeColor(
            context: Context
        ): String {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getString(
                KEY_WIDGET_DARK_TIME_COLOR,
                DEFAULT_COLOR
            ) ?: DEFAULT_COLOR
        }

        fun saveGlobalWidgetDarkDayShadowColor(
            context: Context,
            color: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_WIDGET_DARK_DAY_SHADOW_COLOR,
                    color
                )
            }
        }

        fun getGlobalWidgetDarkDayShadowColor(
            context: Context
        ): String {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getString(
                KEY_WIDGET_DARK_DAY_SHADOW_COLOR,
                DEFAULT_SHADOW_COLOR
            ) ?: DEFAULT_SHADOW_COLOR
        }

        fun saveGlobalWidgetDarkDateShadowColor(
            context: Context,
            color: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_WIDGET_DARK_DATE_SHADOW_COLOR,
                    color
                )
            }
        }

        fun getGlobalWidgetDarkDateShadowColor(
            context: Context
        ): String {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getString(
                KEY_WIDGET_DARK_DATE_SHADOW_COLOR,
                DEFAULT_SHADOW_COLOR
            ) ?: DEFAULT_SHADOW_COLOR
        }

        fun saveGlobalWidgetDarkTimeShadowColor(
            context: Context,
            color: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_WIDGET_DARK_TIME_SHADOW_COLOR,
                    color
                )
            }
        }

        fun getGlobalWidgetDarkTimeShadowColor(
            context: Context
        ): String {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getString(
                KEY_WIDGET_DARK_TIME_SHADOW_COLOR,
                DEFAULT_SHADOW_COLOR
            ) ?: DEFAULT_SHADOW_COLOR
        }

        fun saveGlobalDayColor(
            context: Context,
            color: String
        ) {
            saveGlobalWidgetLightDayColor(
                context,
                color
            )
        }

        fun getGlobalDayColor(
            context: Context
        ): String {
            return getGlobalWidgetLightDayColor(
                context
            )
        }

        fun saveGlobalDateColor(
            context: Context,
            color: String
        ) {
            saveGlobalWidgetLightDateColor(
                context,
                color
            )
        }

        fun getGlobalDateColor(
            context: Context
        ): String {
            return getGlobalWidgetLightDateColor(
                context
            )
        }

        fun saveGlobalTimeColor(
            context: Context,
            color: String
        ) {
            saveGlobalWidgetLightTimeColor(
                context,
                color
            )
        }

        fun getGlobalTimeColor(
            context: Context
        ): String {
            return getGlobalWidgetLightTimeColor(
                context
            )
        }
    }

    private lateinit var root: LinearLayout
    private lateinit var preview: ImageView
    private lateinit var dateFormatInput: EditText
    private lateinit var timeFormatInput: EditText
    private lateinit var dayLetterSpacingInput: EditText
    private lateinit var showDaySwitch: SwitchCompat

    private lateinit var dayColorValue: TextView
    private lateinit var dateColorValue: TextView
    private lateinit var timeColorValue: TextView

    private lateinit var darkDayColorValue: TextView
    private lateinit var darkDateColorValue: TextView
    private lateinit var darkTimeColorValue: TextView

    private lateinit var dayColorSwatch: View
    private lateinit var dateColorSwatch: View
    private lateinit var timeColorSwatch: View

    private lateinit var darkDayColorSwatch: View
    private lateinit var darkDateColorSwatch: View
    private lateinit var darkTimeColorSwatch: View

    private lateinit var darkDayShadowColorValue: TextView
    private lateinit var darkDateShadowColorValue: TextView
    private lateinit var darkTimeShadowColorValue: TextView

    private lateinit var darkDayShadowColorSwatch: View
    private lateinit var darkDateShadowColorSwatch: View
    private lateinit var darkTimeShadowColorSwatch: View

    private lateinit var widgetAppearanceButton: Button

    private lateinit var dayShadowSwitch: SwitchCompat
    private lateinit var dateShadowSwitch: SwitchCompat
    private lateinit var timeShadowSwitch: SwitchCompat

    private lateinit var dayShadowColorValue: TextView
    private lateinit var dateShadowColorValue: TextView
    private lateinit var timeShadowColorValue: TextView

    private lateinit var dayShadowColorSwatch: View
    private lateinit var dateShadowColorSwatch: View
    private lateinit var timeShadowColorSwatch: View

    private lateinit var dayFontSizeControl: FontSizeControl
    private lateinit var dateFontSizeControl: FontSizeControl
    private lateinit var timeFontSizeControl: FontSizeControl

    private lateinit var batteryUsageButton: Button

    private var updatingFontSizeControls =
        false

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )

        if (
            !intent.getBooleanExtra(
                "opened_from_widget",
                false
            )
        ) {
            updateAllWidgets()
        }

        createInterface()
    }

    override fun onResume() {
        super.onResume()

        if (::root.isInitialized) {
            applyTrueBlackBackground()
            applyPreviewWallpaper()
            updateBatteryUsageButton()
        }
    }

    @Suppress("SetTextI18n")
    private fun createInterface() {

        val scrollView =
            ScrollView(this).apply {
                isFillViewport = true
                clipToPadding = false
            }

        root =
            LinearLayout(this).apply {
                orientation =
                    LinearLayout.VERTICAL

                gravity =
                    Gravity.CENTER_HORIZONTAL

                setPadding(
                    32,
                    48,
                    32,
                    32
                )
            }

        ViewCompat.setOnApplyWindowInsetsListener(
            scrollView
        ) { _, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            val ime =
                insets.getInsets(
                    WindowInsetsCompat.Type.ime()
                )

            val bottomInset =
                max(
                    systemBars.bottom,
                    ime.bottom
                )

            root.setPadding(
                32,
                48 + systemBars.top,
                32,
                32 + bottomInset
            )

            if (
                ime.bottom > 0
            ) {
                scrollView.post {
                    scrollFocusedFieldIntoView(
                        scrollView
                    )
                }
            }

            insets
        }

        scrollView.addView(
            root,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        val topBar =
            FrameLayout(this).apply {
                layoutParams =
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
            }

        root.addView(
            topBar
        )

        val title =
            TextView(this).apply {
                text =
                    getString(
                        R.string.app_name
                    )

                textSize =
                    28f

                gravity =
                    Gravity.CENTER

                layoutParams =
                    FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                    )
            }

        topBar.addView(
            title
        )

        val menuButton =
            TextView(this).apply {

                text =
                    getString(
                        R.string.menu_icon
                    )

                textSize =
                    28f

                gravity =
                    Gravity.CENTER

                isClickable =
                    true

                isFocusable =
                    true

                setPadding(
                    16,
                    8,
                    8,
                    0
                )

                setOnClickListener { view ->

                    val popupMenu =
                        PopupMenu(
                            this@MainActivity,
                            view
                        )

                    popupMenu.menu.add(
                        R.string.app_settings
                    )

                    popupMenu.menu.add(
                        R.string.about
                    )

                    popupMenu.setOnMenuItemClickListener { item ->

                        when (
                            item.title.toString()
                        ) {

                            getString(
                                R.string.app_settings
                            ) -> {

                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        AppSettingsActivity::class.java
                                    )
                                )

                                true
                            }

                            getString(
                                R.string.about
                            ) -> {

                                showAboutDialog()

                                true
                            }

                            else -> false
                        }
                    }

                    popupMenu.show()
                }

                layoutParams =
                    FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.END or
                                Gravity.CENTER_VERTICAL
                    )
            }

        topBar.addView(
            menuButton
        )

        val subtitle =
            TextView(this).apply {

                text =
                    getString(
                        R.string.widget_settings
                    )

                textSize =
                    16f

                gravity =
                    Gravity.CENTER
            }

        root.addView(
            subtitle,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin =
                    32
            }
        )

        val previewTitle =
            TextView(this).apply {

                text =
                    getString(
                        R.string.preview
                    )

                textSize =
                    18f
            }

        root.addView(
            previewTitle,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        preview =
            ImageView(this).apply {

                scaleType =
                    ImageView.ScaleType.FIT_CENTER
            }

        root.addView(
            preview,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                280
            ).apply {
                topMargin =
                    12
            }
        )

        applyPreviewWallpaper()

        val permissionButtonRow =
            LinearLayout(this).apply {
                orientation =
                    LinearLayout.HORIZONTAL

                gravity =
                    Gravity.CENTER

                layoutParams =
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topMargin =
                            8
                    }
            }

        val wallpaperButton =
            Button(this).apply {

                text =
                    if (
                        Environment.isExternalStorageManager()
                    ) {
                        "Use current wallpaper"
                    } else {
                        "Use wallpaper for preview"
                    }

                isAllCaps =
                    false

                setOnClickListener {

                    if (
                        Environment.isExternalStorageManager()
                    ) {

                        applyPreviewWallpaper()

                    } else {

                        val intent =
                            Intent(
                                Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                            ).apply {
                                data =
                                    "package:$packageName"
                                        .toUri()
                            }

                        startActivity(
                            intent
                        )
                    }
                }
            }

        permissionButtonRow.addView(
            wallpaperButton,
            LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                rightMargin =
                    4
            }
        )

        batteryUsageButton =
            Button(this).apply {

                isAllCaps =
                    false

                setOnClickListener {
                    requestBatteryUnrestricted()
                }
            }

        permissionButtonRow.addView(
            batteryUsageButton,
            LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                leftMargin =
                    4
            }
        )

        root.addView(
            permissionButtonRow
        )

        updateBatteryUsageButton()

        val settingsTitle =
            TextView(this).apply {

                text =
                    getString(
                        R.string.settings
                    )

                textSize =
                    18f
            }

        root.addView(
            settingsTitle,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    32
            }
        )

        val widgetAppearanceLabel =
            TextView(this).apply {
                text =
                    "Widget appearance"

                textSize =
                    16f

                setTypeface(
                    null,
                    Typeface.BOLD
                )
            }

        root.addView(
            widgetAppearanceLabel,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    16
            }
        )

        widgetAppearanceButton =
            Button(this).apply {
                isAllCaps =
                    false

                updateWidgetAppearanceButtonText(this)

                setOnClickListener {
                    showWidgetAppearanceMenu(this)
                }
            }

        root.addView(
            widgetAppearanceButton,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        val colourTabRow =
            LinearLayout(this).apply {
                orientation =
                    LinearLayout.HORIZONTAL

                setPadding(
                    4,
                    4,
                    4,
                    4
                )

                background =
                    GradientDrawable().apply {
                        shape =
                            GradientDrawable.RECTANGLE

                        cornerRadius =
                            16f

                        setColor(
                            Color.TRANSPARENT
                        )

                        setStroke(
                            1,
                            Color.GRAY
                        )
                    }
            }

        val lightColourTabButton =
            Button(this).apply {
                text =
                    "☀ Light"
                isAllCaps =
                    false

                setPadding(
                    8,
                    4,
                    8,
                    4
                )
            }

        val darkColourTabButton =
            Button(this).apply {
                text =
                    "☾ Dark"
                isAllCaps =
                    false

                setPadding(
                    8,
                    4,
                    8,
                    4
                )
            }

        colourTabRow.addView(
            lightColourTabButton,
            LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
        )

        colourTabRow.addView(
            darkColourTabButton,
            LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                leftMargin =
                    4
            }
        )

        root.addView(
            colourTabRow,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    12
            }
        )

        val daySection =
            createExpandableSection(
                title =
                    getString(
                        R.string.day_settings
                    ),
                initiallyExpanded =
                    getGlobalDaySectionExpanded(
                        this
                    ),
                onExpandedChanged = { expanded ->
                    saveGlobalDaySectionExpanded(
                        this,
                        expanded
                    )
                }
            )

        root.addView(
            daySection.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    12
            }
        )

        val dayContent =
            daySection.second

        showDaySwitch =
            SwitchCompat(this).apply {
                text =
                    getString(
                        R.string.show_day
                    )

                textSize =
                    16f

                isChecked =
                    getGlobalShowDay(
                        this@MainActivity
                    )
            }

        dayContent.addView(
            showDaySwitch,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        val daySpacingLabel =
            TextView(this).apply {
                text =
                    getString(
                        R.string.day_letter_spacing
                    )

                textSize =
                    16f

                setTypeface(
                    null,
                    Typeface.BOLD
                )
            }

        dayContent.addView(
            daySpacingLabel,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    12
            }
        )

        dayLetterSpacingInput =
            EditText(this).apply {
                setSingleLine(true)
                textSize = 16f

                inputType =
                    android.text.InputType.TYPE_CLASS_NUMBER or
                            android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL or
                            android.text.InputType.TYPE_NUMBER_FLAG_SIGNED

                imeOptions =
                    EditorInfo.IME_ACTION_DONE

                setText(
                    formatSpacing(
                        getGlobalDayLetterSpacing(
                            this@MainActivity
                        )
                    )
                )

                setSelection(
                    text.length
                )
            }

        dayContent.addView(
            dayLetterSpacingInput,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        val daySpacingHelp =
            TextView(this).apply {
                text =
                    getString(
                        R.string.day_letter_spacing_help
                    )

                textSize =
                    13f
            }

        dayContent.addView(
            daySpacingHelp,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )


        dayFontSizeControl =
            createFontSizeControl(
                label =
                    getString(
                        R.string.day_font_size
                    ),
                initialValue =
                    getGlobalDayFontSize(
                        this
                    ),
                element =
                    ModernClockWidget.FontSizeElement.DAY
            )

        dayContent.addView(
            dayFontSizeControl.container,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    12
            }
        )

        dayColorSwatch =
            createColorSwatch(
                getGlobalDayColor(this)
            )

        val dayColorRow =
            createColorRow(
                label =
                    getString(
                        R.string.day_colour
                    ),
                color =
                    getGlobalDayColor(this),
                swatch =
                    dayColorSwatch,
                onClick = {
                    showColorPicker(
                        elementName =
                            getString(
                                R.string.day_settings
                            ),
                        currentColor =
                            getGlobalDayColor(
                                this
                            ),
                        saveColor = { color ->
                            saveGlobalDayColor(
                                this,
                                color
                            )
                        },
                        updateValue = { color ->
                            dayColorValue.text =
                                color

                            dayColorSwatch.background =
                                createColorDrawable(
                                    color
                                )

                            updatePreview()
                        }
                    )
                }
            )

        dayColorValue =
            dayColorRow.second

        dayContent.addView(
            dayColorRow.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    8
            }
        )


        darkDayColorSwatch =
            createColorSwatch(
                getGlobalWidgetDarkDayColor(this)
            )

        val darkDayColorRow =
            createColorRow(
                label =
                    "DAY Colour",
                color =
                    getGlobalWidgetDarkDayColor(this),
                swatch =
                    darkDayColorSwatch,
                onClick = {
                    showColorPicker(
                        elementName =
                            "Dark DAY colour",
                        currentColor =
                            getGlobalWidgetDarkDayColor(this),
                        saveColor = { color ->
                            saveGlobalWidgetDarkDayColor(
                                this,
                                color
                            )
                        },
                        updateValue = { color ->
                            darkDayColorValue.text =
                                color

                            darkDayColorSwatch.background =
                                createColorDrawable(
                                    color
                                )

                            updatePreview()
                        }
                    )
                }
            )

        darkDayColorValue =
            darkDayColorRow.second

        dayContent.addView(
            darkDayColorRow.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        dayShadowSwitch =
            SwitchCompat(this).apply {
                text =
                    getString(
                        R.string.shadow
                    )

                textSize =
                    16f

                isChecked =
                    getGlobalDayShadowEnabled(
                        this@MainActivity
                    )
            }

        dayContent.addView(
            dayShadowSwitch,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        dayShadowColorSwatch =
            createColorSwatch(
                getGlobalDayShadowColor(this)
            )

        val dayShadowColorRow =
            createColorRow(
                label =
                    getString(
                        R.string.shadow_colour
                    ),
                color =
                    getGlobalDayShadowColor(this),
                swatch =
                    dayShadowColorSwatch,
                onClick = {
                    showColorPicker(
                        elementName =
                            getString(
                                R.string.day_shadow
                            ),
                        currentColor =
                            getGlobalDayShadowColor(
                                this
                            ),
                        saveColor = { color ->
                            saveGlobalDayShadowColor(
                                this,
                                color
                            )
                        },
                        updateValue = { color ->
                            dayShadowColorValue.text =
                                color

                            dayShadowColorSwatch.background =
                                createColorDrawable(
                                    color
                                )

                            updatePreview()
                        }
                    )
                }
            )

        dayShadowColorValue =
            dayShadowColorRow.second

        dayContent.addView(
            dayShadowColorRow.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )


        darkDayShadowColorSwatch =
            createColorSwatch(
                getGlobalWidgetDarkDayShadowColor(this)
            )

        val darkDayShadowColorRow =
            createColorRow(
                label =
                    "DAY Shadow Colour",
                color =
                    getGlobalWidgetDarkDayShadowColor(this),
                swatch =
                    darkDayShadowColorSwatch,
                onClick = {
                    showColorPicker(
                        elementName =
                            "Dark DAY shadow",
                        currentColor =
                            getGlobalWidgetDarkDayShadowColor(this),
                        saveColor = { color ->
                            saveGlobalWidgetDarkDayShadowColor(
                                this,
                                color
                            )
                        },
                        updateValue = { color ->
                            darkDayShadowColorValue.text =
                                color

                            darkDayShadowColorSwatch.background =
                                createColorDrawable(
                                    color
                                )

                            updatePreview()
                        }
                    )
                }
            )

        darkDayShadowColorValue =
            darkDayShadowColorRow.second

        dayContent.addView(
            darkDayShadowColorRow.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        val dateSection =
            createExpandableSection(
                title =
                    getString(
                        R.string.date_settings
                    ),
                initiallyExpanded =
                    getGlobalDateSectionExpanded(
                        this
                    ),
                onExpandedChanged = { expanded ->
                    saveGlobalDateSectionExpanded(
                        this,
                        expanded
                    )
                }
            )

        root.addView(
            dateSection.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    8
            }
        )

        val dateContent =
            dateSection.second

        dateFormatInput =
            EditText(this).apply {
                setSingleLine(true)
                textSize = 16f
                hint = DEFAULT_DATE_FORMAT
                imeOptions = EditorInfo.IME_ACTION_DONE

                setText(
                    getGlobalDateFormat(
                        this@MainActivity
                    )
                )

                setSelection(
                    text.length
                )
            }

        dateContent.addView(
            dateFormatInput,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        val dateFormatHelp =
            TextView(this).apply {
                text =
                    getString(
                        R.string.date_format_help
                    )

                textSize =
                    13f
            }

        dateContent.addView(
            dateFormatHelp,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        dateFontSizeControl =
            createFontSizeControl(
                label =
                    getString(
                        R.string.date_font_size
                    ),
                initialValue =
                    getGlobalDateFontSize(
                        this
                    ),
                element =
                    ModernClockWidget.FontSizeElement.DATE
            )

        dateContent.addView(
            dateFontSizeControl.container,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    12
            }
        )

        dateColorSwatch =
            createColorSwatch(
                getGlobalDateColor(this)
            )

        val dateColorRow =
            createColorRow(
                label =
                    getString(
                        R.string.date_colour
                    ),
                color =
                    getGlobalDateColor(this),
                swatch =
                    dateColorSwatch,
                onClick = {
                    showColorPicker(
                        elementName =
                            getString(
                                R.string.date_settings
                            ),
                        currentColor =
                            getGlobalDateColor(
                                this
                            ),
                        saveColor = { color ->
                            saveGlobalDateColor(
                                this,
                                color
                            )
                        },
                        updateValue = { color ->
                            dateColorValue.text =
                                color

                            dateColorSwatch.background =
                                createColorDrawable(
                                    color
                                )

                            updatePreview()
                        }
                    )
                }
            )

        dateColorValue =
            dateColorRow.second

        dateContent.addView(
            dateColorRow.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    8
            }
        )


        darkDateColorSwatch =
            createColorSwatch(
                getGlobalWidgetDarkDateColor(this)
            )

        val darkDateColorRow =
            createColorRow(
                label =
                    "DATE Colour",
                color =
                    getGlobalWidgetDarkDateColor(this),
                swatch =
                    darkDateColorSwatch,
                onClick = {
                    showColorPicker(
                        elementName =
                            "Dark DATE colour",
                        currentColor =
                            getGlobalWidgetDarkDateColor(this),
                        saveColor = { color ->
                            saveGlobalWidgetDarkDateColor(
                                this,
                                color
                            )
                        },
                        updateValue = { color ->
                            darkDateColorValue.text =
                                color

                            darkDateColorSwatch.background =
                                createColorDrawable(
                                    color
                                )

                            updatePreview()
                        }
                    )
                }
            )

        darkDateColorValue =
            darkDateColorRow.second

        dateContent.addView(
            darkDateColorRow.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        dateShadowSwitch =
            SwitchCompat(this).apply {
                text =
                    getString(
                        R.string.shadow
                    )

                textSize =
                    16f

                isChecked =
                    getGlobalDateShadowEnabled(
                        this@MainActivity
                    )
            }

        dateContent.addView(
            dateShadowSwitch,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        dateShadowColorSwatch =
            createColorSwatch(
                getGlobalDateShadowColor(this)
            )

        val dateShadowColorRow =
            createColorRow(
                label =
                    getString(
                        R.string.shadow_colour
                    ),
                color =
                    getGlobalDateShadowColor(this),
                swatch =
                    dateShadowColorSwatch,
                onClick = {
                    showColorPicker(
                        elementName =
                            getString(
                                R.string.date_shadow
                            ),
                        currentColor =
                            getGlobalDateShadowColor(
                                this
                            ),
                        saveColor = { color ->
                            saveGlobalDateShadowColor(
                                this,
                                color
                            )
                        },
                        updateValue = { color ->
                            dateShadowColorValue.text =
                                color

                            dateShadowColorSwatch.background =
                                createColorDrawable(
                                    color
                                )

                            updatePreview()
                        }
                    )
                }
            )

        dateShadowColorValue =
            dateShadowColorRow.second

        dateContent.addView(
            dateShadowColorRow.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )


        darkDateShadowColorSwatch =
            createColorSwatch(
                getGlobalWidgetDarkDateShadowColor(this)
            )

        val darkDateShadowColorRow =
            createColorRow(
                label =
                    "DATE Shadow Colour",
                color =
                    getGlobalWidgetDarkDateShadowColor(this),
                swatch =
                    darkDateShadowColorSwatch,
                onClick = {
                    showColorPicker(
                        elementName =
                            "Dark DATE shadow",
                        currentColor =
                            getGlobalWidgetDarkDateShadowColor(this),
                        saveColor = { color ->
                            saveGlobalWidgetDarkDateShadowColor(
                                this,
                                color
                            )
                        },
                        updateValue = { color ->
                            darkDateShadowColorValue.text =
                                color

                            darkDateShadowColorSwatch.background =
                                createColorDrawable(
                                    color
                                )

                            updatePreview()
                        }
                    )
                }
            )

        darkDateShadowColorValue =
            darkDateShadowColorRow.second

        dateContent.addView(
            darkDateShadowColorRow.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        val timeSection =
            createExpandableSection(
                title =
                    getString(
                        R.string.time_settings
                    ),
                initiallyExpanded =
                    getGlobalTimeSectionExpanded(
                        this
                    ),
                onExpandedChanged = { expanded ->
                    saveGlobalTimeSectionExpanded(
                        this,
                        expanded
                    )
                }
            )

        root.addView(
            timeSection.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    8
            }
        )

        val timeContent =
            timeSection.second

        timeFormatInput =
            EditText(this).apply {
                setSingleLine(true)
                textSize = 16f
                hint = DEFAULT_TIME_FORMAT
                imeOptions = EditorInfo.IME_ACTION_DONE

                setText(
                    getGlobalTimeFormat(
                        this@MainActivity
                    )
                )

                setSelection(
                    text.length
                )
            }

        timeContent.addView(
            timeFormatInput,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        val timeFormatHelp =
            TextView(this).apply {
                text =
                    getString(
                        R.string.time_format_help
                    )

                textSize =
                    13f
            }

        timeContent.addView(
            timeFormatHelp,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        timeFontSizeControl =
            createFontSizeControl(
                label =
                    getString(
                        R.string.time_font_size
                    ),
                initialValue =
                    getGlobalTimeFontSize(
                        this
                    ),
                element =
                    ModernClockWidget.FontSizeElement.TIME
            )

        timeContent.addView(
            timeFontSizeControl.container,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    12
            }
        )

        timeColorSwatch =
            createColorSwatch(
                getGlobalTimeColor(this)
            )

        val timeColorRow =
            createColorRow(
                label =
                    getString(
                        R.string.time_colour
                    ),
                color =
                    getGlobalTimeColor(this),
                swatch =
                    timeColorSwatch,
                onClick = {
                    showColorPicker(
                        elementName =
                            getString(
                                R.string.time_settings
                            ),
                        currentColor =
                            getGlobalTimeColor(
                                this
                            ),
                        saveColor = { color ->
                            saveGlobalTimeColor(
                                this,
                                color
                            )
                        },
                        updateValue = { color ->
                            timeColorValue.text =
                                color

                            timeColorSwatch.background =
                                createColorDrawable(
                                    color
                                )

                            updatePreview()
                        }
                    )
                }
            )

        timeColorValue =
            timeColorRow.second

        timeContent.addView(
            timeColorRow.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    8
            }
        )


        darkTimeColorSwatch =
            createColorSwatch(
                getGlobalWidgetDarkTimeColor(this)
            )

        val darkTimeColorRow =
            createColorRow(
                label =
                    "TIME Colour",
                color =
                    getGlobalWidgetDarkTimeColor(this),
                swatch =
                    darkTimeColorSwatch,
                onClick = {
                    showColorPicker(
                        elementName =
                            "Dark TIME colour",
                        currentColor =
                            getGlobalWidgetDarkTimeColor(this),
                        saveColor = { color ->
                            saveGlobalWidgetDarkTimeColor(
                                this,
                                color
                            )
                        },
                        updateValue = { color ->
                            darkTimeColorValue.text =
                                color

                            darkTimeColorSwatch.background =
                                createColorDrawable(
                                    color
                                )

                            updatePreview()
                        }
                    )
                }
            )

        darkTimeColorValue =
            darkTimeColorRow.second

        timeContent.addView(
            darkTimeColorRow.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        timeShadowSwitch =
            SwitchCompat(this).apply {
                text =
                    getString(
                        R.string.shadow
                    )

                textSize =
                    16f

                isChecked =
                    getGlobalTimeShadowEnabled(
                        this@MainActivity
                    )
            }

        timeContent.addView(
            timeShadowSwitch,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        timeShadowColorSwatch =
            createColorSwatch(
                getGlobalTimeShadowColor(this)
            )

        val timeShadowColorRow =
            createColorRow(
                label =
                    getString(
                        R.string.shadow_colour
                    ),
                color =
                    getGlobalTimeShadowColor(this),
                swatch =
                    timeShadowColorSwatch,
                onClick = {
                    showColorPicker(
                        elementName =
                            getString(
                                R.string.time_shadow
                            ),
                        currentColor =
                            getGlobalTimeShadowColor(
                                this
                            ),
                        saveColor = { color ->
                            saveGlobalTimeShadowColor(
                                this,
                                color
                            )
                        },
                        updateValue = { color ->
                            timeShadowColorValue.text =
                                color

                            timeShadowColorSwatch.background =
                                createColorDrawable(
                                    color
                                )

                            updatePreview()
                        }
                    )
                }
            )

        timeShadowColorValue =
            timeShadowColorRow.second

        timeContent.addView(
            timeShadowColorRow.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        darkTimeShadowColorSwatch =
            createColorSwatch(
                getGlobalWidgetDarkTimeShadowColor(this)
            )

        val darkTimeShadowColorRow =
            createColorRow(
                label =
                    "TIME Shadow Colour",
                color =
                    getGlobalWidgetDarkTimeShadowColor(this),
                swatch =
                    darkTimeShadowColorSwatch,
                onClick = {
                    showColorPicker(
                        elementName =
                            "Dark TIME shadow",
                        currentColor =
                            getGlobalWidgetDarkTimeShadowColor(this),
                        saveColor = { color ->
                            saveGlobalWidgetDarkTimeShadowColor(
                                this,
                                color
                            )
                        },
                        updateValue = { color ->
                            darkTimeShadowColorValue.text =
                                color

                            darkTimeShadowColorSwatch.background =
                                createColorDrawable(
                                    color
                                )

                            updatePreview()
                        }
                    )
                }
            )

        darkTimeShadowColorValue =
            darkTimeShadowColorRow.second

        timeContent.addView(
            darkTimeShadowColorRow.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        fun applyWidgetColourTab(
            dark: Boolean
        ) {
            dayColorValue.parent.let {
                (it as View).isVisible = !dark
            }
            darkDayColorValue.parent.let {
                (it as View).isVisible = dark
            }
            dayShadowColorValue.parent.let {
                (it as View).isVisible = !dark
            }
            darkDayShadowColorValue.parent.let {
                (it as View).isVisible = dark
            }

            dateColorValue.parent.let {
                (it as View).isVisible = !dark
            }
            darkDateColorValue.parent.let {
                (it as View).isVisible = dark
            }
            dateShadowColorValue.parent.let {
                (it as View).isVisible = !dark
            }
            darkDateShadowColorValue.parent.let {
                (it as View).isVisible = dark
            }

            timeColorValue.parent.let {
                (it as View).isVisible = !dark
            }
            darkTimeColorValue.parent.let {
                (it as View).isVisible = dark
            }
            timeShadowColorValue.parent.let {
                (it as View).isVisible = !dark
            }
            darkTimeShadowColorValue.parent.let {
                (it as View).isVisible = dark
            }

            val selectedBackground =
                GradientDrawable().apply {
                    shape =
                        GradientDrawable.RECTANGLE

                    cornerRadius =
                        12f

                    setColor(
                        if (dark) {
                            Color.DKGRAY
                        } else {
                            Color.LTGRAY
                        }
                    )
                }

            val unselectedBackground =
                GradientDrawable().apply {
                    shape =
                        GradientDrawable.RECTANGLE

                    cornerRadius =
                        12f

                    setColor(
                        Color.TRANSPARENT
                    )
                }

            lightColourTabButton.background =
                if (dark) {
                    unselectedBackground
                } else {
                    selectedBackground
                }

            darkColourTabButton.background =
                if (dark) {
                    selectedBackground
                } else {
                    unselectedBackground
                }

            lightColourTabButton.setTextColor(
                if (dark) {
                    Color.GRAY
                } else {
                    Color.BLACK
                }
            )

            darkColourTabButton.setTextColor(
                if (dark) {
                    Color.WHITE
                } else {
                    Color.GRAY
                }
            )

            lightColourTabButton.isEnabled =
                dark
            darkColourTabButton.isEnabled =
                !dark
        }

        lightColourTabButton.setOnClickListener {
            applyWidgetColourTab(false)
        }

        darkColourTabButton.setOnClickListener {
            applyWidgetColourTab(true)
        }

        applyWidgetColourTab(false)

        showDaySwitch.setOnCheckedChangeListener {
                _,
                checked ->

            saveGlobalShowDay(
                this,
                checked
            )

            updateAllWidgets()
            updatePreview()
        }

        dayShadowSwitch.setOnCheckedChangeListener {
                _,
                checked ->

            saveGlobalDayShadowEnabled(
                this,
                checked
            )

            updateAllWidgets()
            updatePreview()
        }

        dateShadowSwitch.setOnCheckedChangeListener {
                _,
                checked ->

            saveGlobalDateShadowEnabled(
                this,
                checked
            )

            updateAllWidgets()
            updatePreview()
        }

        timeShadowSwitch.setOnCheckedChangeListener {
                _,
                checked ->

            saveGlobalTimeShadowEnabled(
                this,
                checked
            )

            updateAllWidgets()
            updatePreview()
        }

        dayLetterSpacingInput.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    val value =
                        s?.toString()
                            ?.trim()
                            ?.toFloatOrNull()
                            ?: return

                    if (
                        value !in 0f..100f
                    ) {
                        return
                    }

                    saveGlobalDayLetterSpacing(
                        this@MainActivity,
                        value
                    )

                    updateAllWidgets()
                    updatePreview()
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        dayLetterSpacingInput.setOnEditorActionListener {
                _,
                actionId,
                _ ->

            if (
                actionId ==
                EditorInfo.IME_ACTION_DONE
            ) {

                saveDayLetterSpacing()
                updateAllWidgets()
                updatePreview()

                true
            } else {
                false
            }
        }

        dayLetterSpacingInput.setOnFocusChangeListener {
                _,
                hasFocus ->

            if (!hasFocus) {

                saveDayLetterSpacing()
                updateAllWidgets()
                updatePreview()

            } else {

                requestFieldVisibility(
                    scrollView,
                    dayLetterSpacingInput
                )
            }
        }

        dateFormatInput.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    val format =
                        s?.toString()
                            ?.trim()
                            ?: return

                    if (format.isBlank()) {
                        return
                    }

                    if (
                        isValidDateFormat(
                            format
                        )
                    ) {

                        saveGlobalDateFormat(
                            this@MainActivity,
                            format
                        )

                        updateAllWidgets()
                        updatePreview()
                    }
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        dateFormatInput.setOnEditorActionListener {
                _,
                actionId,
                _ ->

            if (
                actionId ==
                EditorInfo.IME_ACTION_DONE
            ) {

                saveCustomDateFormat()
                updateAllWidgets()
                updatePreview()

                true
            } else {
                false
            }
        }

        dateFormatInput.setOnFocusChangeListener {
                _,
                hasFocus ->

            if (!hasFocus) {

                saveCustomDateFormat()
                updateAllWidgets()
                updatePreview()

            } else {

                requestFieldVisibility(
                    scrollView,
                    dateFormatInput
                )
            }
        }

        timeFormatInput.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    val format =
                        s?.toString()
                            ?.trim()
                            ?: return

                    if (format.isBlank()) {
                        return
                    }

                    if (
                        isValidDateFormat(
                            format
                        )
                    ) {

                        saveGlobalTimeFormat(
                            this@MainActivity,
                            format
                        )

                        updateAllWidgets()
                        updatePreview()
                    }
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        timeFormatInput.setOnEditorActionListener {
                _,
                actionId,
                _ ->

            if (
                actionId ==
                EditorInfo.IME_ACTION_DONE
            ) {

                saveCustomTimeFormat()
                updateAllWidgets()
                updatePreview()

                true
            } else {
                false
            }
        }

        timeFormatInput.setOnFocusChangeListener {
                _,
                hasFocus ->

            if (!hasFocus) {

                saveCustomTimeFormat()
                updateAllWidgets()
                updatePreview()

            } else {

                requestFieldVisibility(
                    scrollView,
                    timeFormatInput
                )
            }
        }

        setContentView(
            scrollView
        )

        ViewCompat.requestApplyInsets(
            scrollView
        )

        updatePreview()
        applyTrueBlackBackground()
    }

    private fun updateBatteryUsageButton() {

        if (
            !::batteryUsageButton.isInitialized
        ) {
            return
        }

        val powerManager =
            getSystemService(
                PowerManager::class.java
            )

        val unrestricted =
            powerManager.isIgnoringBatteryOptimizations(
                packageName
            )

        batteryUsageButton.text =
            if (unrestricted) {
                "Battery: Unrestricted"
            } else {
                "Set battery to unrestricted"
            }
    }

    private fun requestBatteryUnrestricted() {

        startActivity(
            Intent(
                Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
            )
        )
    }

    private fun updateWidgetAppearanceButtonText(
        button: Button
    ) {

        button.text =
            when (
                getGlobalWidgetAppearance(this)
            ) {
                "light" ->
                    "Widget appearance: Light"

                "dark" ->
                    "Widget appearance: Dark"

                else ->
                    "Widget appearance: Follow system"
            }
    }

    private fun showWidgetAppearanceMenu(
        anchor: View
    ) {

        val popupMenu =
            PopupMenu(
                this,
                anchor
            )

        popupMenu.menu.add(
            "Follow system"
        )

        popupMenu.menu.add(
            "Light"
        )

        popupMenu.menu.add(
            "Dark"
        )

        popupMenu.setOnMenuItemClickListener { item ->

            val appearance =
                when (
                    item.title.toString()
                ) {
                    "Light" ->
                        "light"

                    "Dark" ->
                        "dark"

                    else ->
                        "follow_system"
                }

            saveGlobalWidgetAppearance(
                this,
                appearance
            )

            updateWidgetAppearanceButtonText(
                widgetAppearanceButton
            )

            updateAllWidgets()
            updatePreview()

            true
        }

        popupMenu.show()
    }

    private fun createExpandableSection(
        title: String,
        initiallyExpanded: Boolean,
        onExpandedChanged: (Boolean) -> Unit
    ): Pair<LinearLayout, LinearLayout> {

        val section =
            LinearLayout(this).apply {
                orientation =
                    LinearLayout.VERTICAL
            }

        val header =
            TextView(this).apply {

                text =
                    getString(
                        if (initiallyExpanded) {
                            R.string.section_expanded_format
                        } else {
                            R.string.section_collapsed_format
                        },
                        title
                    )

                textSize =
                    17f

                setTypeface(
                    null,
                    Typeface.BOLD
                )

                gravity =
                    Gravity.CENTER_VERTICAL

                setPadding(
                    8,
                    10,
                    8,
                    10
                )

                isClickable =
                    true

                isFocusable =
                    true
            }

        val content =
            LinearLayout(this).apply {
                orientation =
                    LinearLayout.VERTICAL

                isVisible =
                    initiallyExpanded

                setPadding(
                    8,
                    0,
                    8,
                    4
                )
            }

        header.setOnClickListener {

            val expanded =
                content.isVisible

            content.isVisible =
                !expanded

            header.text =
                getString(
                    if (expanded) {
                        R.string.section_collapsed_format
                    } else {
                        R.string.section_expanded_format
                    },
                    title
                )

            onExpandedChanged(
                !expanded
            )
        }

        section.addView(
            header,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        section.addView(
            content,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        return Pair(
            section,
            content
        )
    }

    private inner class FontSizeControl(
        val container: LinearLayout,
        private val seekBar: SeekBar
    ) {

        fun setValue(
            value: Int
        ) {

            val safeValue =
                value.coerceIn(
                    MIN_FONT_SIZE_PERCENT,
                    MAX_FONT_SIZE_PERCENT
                )

            updatingFontSizeControls =
                true

            seekBar.progress =
                safeValue -
                        MIN_FONT_SIZE_PERCENT

            updatingFontSizeControls =
                false
        }
    }

    private fun createFontSizeControl(
        label: String,
        initialValue: Int,
        element: ModernClockWidget.FontSizeElement
    ): FontSizeControl {

        val container =
            LinearLayout(this).apply {
                orientation =
                    LinearLayout.VERTICAL
            }

        val labelView =
            TextView(this).apply {
                text =
                    label

                textSize =
                    16f

                setTypeface(
                    null,
                    Typeface.BOLD
                )
            }

        container.addView(
            labelView
        )

        val seekBar =
            SeekBar(this).apply {

                max =
                    MAX_FONT_SIZE_PERCENT -
                            MIN_FONT_SIZE_PERCENT

                progress =
                    (
                            initialValue -
                                    MIN_FONT_SIZE_PERCENT
                            ).coerceIn(
                            0,
                            max
                        )

                setOnSeekBarChangeListener(
                    object :
                        SeekBar.OnSeekBarChangeListener {

                        override fun onProgressChanged(
                            seekBar: SeekBar,
                            progress: Int,
                            fromUser: Boolean
                        ) {

                            val value =
                                progress +
                                        MIN_FONT_SIZE_PERCENT

                            if (
                                fromUser &&
                                !updatingFontSizeControls
                            ) {

                                handleFontSizeChange(
                                    element,
                                    value
                                )
                            }
                        }

                        override fun onStartTrackingTouch(
                            seekBar: SeekBar
                        ) {
                        }

                        override fun onStopTrackingTouch(
                            seekBar: SeekBar
                        ) {
                        }
                    }
                )
            }

        container.addView(
            seekBar,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        val rangeLabels =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.HORIZONTAL

                gravity =
                    Gravity.CENTER_VERTICAL
            }

        val smallerLabel =
            TextView(this).apply {

                text =
                    getString(
                        R.string.smaller
                    )

                textSize =
                    13f

                layoutParams =
                    LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1f
                    )
            }

        val largerLabel =
            TextView(this).apply {

                text =
                    getString(
                        R.string.larger
                    )

                textSize =
                    13f

                gravity =
                    Gravity.END

                layoutParams =
                    LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1f
                    )
            }

        rangeLabels.addView(
            smallerLabel
        )

        rangeLabels.addView(
            largerLabel
        )

        container.addView(
            rangeLabels
        )

        val resetButton =
            Button(this).apply {

                text =
                    getString(
                        R.string.reset_to_default
                    )

                textSize =
                    14f

                isAllCaps =
                    false

                setOnClickListener {
                    handleFontSizeChange(
                        element,
                        DEFAULT_FONT_SIZE_PERCENT
                    )
                }
            }

        val resetButtonContainer =
            FrameLayout(this)

        resetButtonContainer.addView(
            resetButton,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        )

        container.addView(
            resetButtonContainer,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    2
            }
        )

        return FontSizeControl(
            container = container,
            seekBar = seekBar
        )
    }

    private fun handleFontSizeChange(
        changedElement: ModernClockWidget.FontSizeElement,
        requestedValue: Int
    ) {

        var requestedDay =
            getGlobalDayFontSize(this)

        var requestedDate =
            getGlobalDateFontSize(this)

        var requestedTime =
            getGlobalTimeFontSize(this)

        when (changedElement) {
            ModernClockWidget.FontSizeElement.DAY ->
                requestedDay = requestedValue

            ModernClockWidget.FontSizeElement.DATE ->
                requestedDate = requestedValue

            ModernClockWidget.FontSizeElement.TIME ->
                requestedTime = requestedValue
        }

        val appWidgetManager =
            AppWidgetManager.getInstance(this)

        val componentName =
            ComponentName(
                this,
                ModernClockWidget::class.java
            )

        val widgetIds =
            appWidgetManager.getAppWidgetIds(
                componentName
            )

        var fittedSizes =
            ModernClockWidget.FontSizeValues(
                day = requestedDay,
                date = requestedDate,
                time = requestedTime
            )

        if (widgetIds.isNotEmpty()) {

            for (widgetId in widgetIds) {

                val options =
                    appWidgetManager.getAppWidgetOptions(
                        widgetId
                    )

                val widthDp =
                    options.getInt(
                        AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH,
                        180
                    )

                val heightDp =
                    options.getInt(
                        AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT,
                        40
                    )

                val candidate =
                    ModernClockWidget.calculateFittedFontSizes(
                        context = this,
                        widthDp = widthDp,
                        heightDp = heightDp,
                        timeFormat =
                            getGlobalTimeFormat(this),
                        dateFormat =
                            getGlobalDateFormat(this),
                        showDay =
                            getGlobalShowDay(this),
                        dayLetterSpacing =
                            getGlobalDayLetterSpacing(this),
                        requestedDaySizePercent =
                            requestedDay,
                        requestedDateSizePercent =
                            requestedDate,
                        requestedTimeSizePercent =
                            requestedTime,
                        changedElement =
                            changedElement
                    )

                fittedSizes =
                    ModernClockWidget.FontSizeValues(
                        day =
                            minOf(
                                fittedSizes.day,
                                candidate.day
                            ),
                        date =
                            minOf(
                                fittedSizes.date,
                                candidate.date
                            ),
                        time =
                            minOf(
                                fittedSizes.time,
                                candidate.time
                            )
                    )
            }

        } else {

            val density =
                resources.displayMetrics.density

            val previewWidthDp =
                max(
                    1,
                    (preview.width / density).toInt()
                )

            val previewHeightDp =
                max(
                    1,
                    (preview.height / density).toInt()
                )

            fittedSizes =
                ModernClockWidget.calculateFittedFontSizes(
                    context = this,
                    widthDp = previewWidthDp,
                    heightDp = previewHeightDp,
                    timeFormat =
                        getGlobalTimeFormat(this),
                    dateFormat =
                        getGlobalDateFormat(this),
                    showDay =
                        getGlobalShowDay(this),
                    dayLetterSpacing =
                        getGlobalDayLetterSpacing(this),
                    requestedDaySizePercent =
                        requestedDay,
                    requestedDateSizePercent =
                        requestedDate,
                    requestedTimeSizePercent =
                        requestedTime,
                    changedElement =
                        changedElement
                )
        }

        saveFittedFontSizes(
            fittedSizes
        )

        updateFontSizeControls(
            fittedSizes
        )

        updateAllWidgets()
        updatePreview()
    }

    private fun saveFittedFontSizes(
        values: ModernClockWidget.FontSizeValues
    ) {

        saveGlobalDayFontSize(
            this,
            values.day
        )

        saveGlobalDateFontSize(
            this,
            values.date
        )

        saveGlobalTimeFontSize(
            this,
            values.time
        )
    }

    private fun updateFontSizeControls(
        values: ModernClockWidget.FontSizeValues
    ) {

        if (updatingFontSizeControls) {
            return
        }

        dayFontSizeControl.setValue(values.day)
        dateFontSizeControl.setValue(values.date)
        timeFontSizeControl.setValue(values.time)
    }

    private fun getFittedPreviewFontSizes():
            ModernClockWidget.FontSizeValues {

        val density =
            resources.displayMetrics.density

        val previewWidthDp =
            max(
                1,
                (preview.width / density).toInt()
            )

        val previewHeightDp =
            max(
                1,
                (preview.height / density).toInt()
            )

        return ModernClockWidget.calculateFittedFontSizes(
            context = this,
            widthDp = previewWidthDp,
            heightDp = previewHeightDp,
            timeFormat =
                timeFormatInput.text
                    .toString()
                    .trim()
                    .ifBlank {
                        DEFAULT_TIME_FORMAT
                    },
            dateFormat =
                dateFormatInput.text
                    .toString()
                    .trim()
                    .ifBlank {
                        DEFAULT_DATE_FORMAT
                    },
            showDay =
                showDaySwitch.isChecked,
            dayLetterSpacing =
                getGlobalDayLetterSpacing(this),
            requestedDaySizePercent =
                getGlobalDayFontSize(this),
            requestedDateSizePercent =
                getGlobalDateFontSize(this),
            requestedTimeSizePercent =
                getGlobalTimeFontSize(this),
            changedElement =
                null
        )
    }

    private fun requestFieldVisibility(
        scrollView: ScrollView,
        field: EditText
    ) {

        scrollView.post {

            scrollView.requestChildRectangleOnScreen(
                field,
                android.graphics.Rect(
                    0,
                    -32,
                    field.width,
                    field.height + 32
                ),
                true
            )

            scrollFocusedFieldIntoView(
                scrollView
            )
        }

        field.postDelayed(
            {
                scrollFocusedFieldIntoView(
                    scrollView
                )
            },
            300
        )
    }

    private fun scrollFocusedFieldIntoView(
        scrollView: ScrollView
    ) {

        val field =
            currentFocus as? EditText
                ?: return

        if (field.parent == null) {
            return
        }

        val fieldLocation =
            IntArray(2)

        val scrollLocation =
            IntArray(2)

        field.getLocationOnScreen(
            fieldLocation
        )

        scrollView.getLocationOnScreen(
            scrollLocation
        )

        val fieldTop =
            fieldLocation[1]

        val fieldBottom =
            fieldLocation[1] + field.height

        val scrollTop =
            scrollLocation[1] + 16

        val visibleBottom =
            window.decorView.height -
                    getBottomObscuredInset(
                        scrollView
                    ) -
                    32

        when {

            fieldBottom > visibleBottom -> {
                scrollView.scrollBy(
                    0,
                    fieldBottom - visibleBottom
                )
            }

            fieldTop < scrollTop -> {
                scrollView.scrollBy(
                    0,
                    fieldTop - scrollTop
                )
            }
        }
    }

    private fun getBottomObscuredInset(
        scrollView: ScrollView
    ): Int {

        val rootWindowInsets =
            ViewCompat.getRootWindowInsets(
                scrollView
            ) ?: return 0

        val ime =
            rootWindowInsets.getInsets(
                WindowInsetsCompat.Type.ime()
            )

        val systemBars =
            rootWindowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

        return max(
            ime.bottom,
            systemBars.bottom
        )
    }

    private fun formatSpacing(
        value: Float
    ): String {

        return if (
            value == value.toInt().toFloat()
        ) {
            value.toInt().toString()
        } else {
            value.toString()
        }
    }

    private fun saveDayLetterSpacing() {

        val value =
            dayLetterSpacingInput.text
                .toString()
                .trim()
                .toFloatOrNull()

        if (
            value == null ||
            value !in 0f..100f
        ) {

            dayLetterSpacingInput.error =
                getString(
                    R.string.day_letter_spacing_error
                )

            return
        }

        saveGlobalDayLetterSpacing(
            this,
            value
        )
    }

    private fun createColorSwatch(
        color: String
    ): View {

        return View(this).apply {
            background =
                createColorDrawable(color)
        }
    }

    private fun createColorRow(
        label: String,
        color: String,
        swatch: View,
        onClick: () -> Unit
    ): Pair<LinearLayout, TextView> {

        val row =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.HORIZONTAL

                gravity =
                    Gravity.CENTER_VERTICAL

                isClickable =
                    true

                isFocusable =
                    true

                setPadding(
                    8,
                    12,
                    8,
                    12
                )

                setOnClickListener {
                    onClick()
                }
            }

        row.addView(
            swatch,
            LinearLayout.LayoutParams(
                42,
                42
            ).apply {
                rightMargin =
                    16
            }
        )

        val labelView =
            TextView(this).apply {

                text =
                    label

                textSize =
                    16f

                layoutParams =
                    LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1f
                    )
            }

        row.addView(labelView)

        val valueView =
            TextView(this).apply {

                text =
                    color

                textSize =
                    15f

                gravity =
                    Gravity.CENTER_VERTICAL
            }

        row.addView(valueView)

        return Pair(
            row,
            valueView
        )
    }

    private fun createColorDrawable(
        color: String
    ): GradientDrawable {

        val drawable =
            GradientDrawable()

        drawable.shape =
            GradientDrawable.RECTANGLE

        drawable.cornerRadius =
            8f

        drawable.setColor(
            parseColor(color)
        )

        drawable.setStroke(
            1,
            Color.GRAY
        )

        return drawable
    }

    private fun parseColor(
        color: String
    ): Int {

        return try {
            color.toColorInt()
        } catch (
            _: IllegalArgumentException
        ) {
            DEFAULT_COLOR.toColorInt()
        }
    }

    private fun showColorPicker(
        elementName: String,
        currentColor: String,
        saveColor: (String) -> Unit,
        updateValue: (String) -> Unit
    ) {

        val initialColor =
            parseColor(currentColor)

        val picker =
            ColorPickerView(
                context = this,
                initialColor = initialColor
            )

        val container =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL

                setPadding(
                    32,
                    8,
                    32,
                    0
                )
            }

        container.addView(
            picker,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                300
            )
        )

        val selectedColor =
            View(this).apply {
                background =
                    createColorDrawable(currentColor)
            }

        container.addView(
            selectedColor,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                48
            ).apply {
                topMargin =
                    16
            }
        )

        val hexRow =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.HORIZONTAL

                gravity =
                    Gravity.CENTER_VERTICAL

                layoutParams =
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topMargin =
                            16
                    }
            }

        val hexInput =
            EditText(this).apply {

                setSingleLine(true)
                textSize = 16f
                hint = "#FFFFFF"

                setText(
                    colorToHex(initialColor)
                )

                setSelection(text.length)

                imeOptions =
                    EditorInfo.IME_ACTION_DONE

                layoutParams =
                    LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1f
                    )
            }

        hexRow.addView(hexInput)

        val copyButton =
            TextView(this).apply {

                text =
                    getString(
                        R.string.copy
                    )

                textSize =
                    14f

                gravity =
                    Gravity.CENTER

                setPadding(
                    20,
                    12,
                    20,
                    12
                )

                isClickable =
                    true

                isFocusable =
                    true
            }

        hexRow.addView(
            copyButton,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                leftMargin =
                    8
            }
        )

        container.addView(hexRow)

        val rgbValue =
            TextView(this).apply {
                textSize = 13f
            }

        val hslValue =
            TextView(this).apply {
                textSize = 13f
            }

        container.addView(
            rgbValue,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    12
            }
        )

        container.addView(
            hslValue,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        val swatchesTitle =
            TextView(this).apply {

                text =
                    getString(
                        R.string.quick_colours
                    )

                textSize =
                    12f

                setTypeface(
                    null,
                    Typeface.BOLD
                )
            }

        container.addView(
            swatchesTitle,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    18
            }
        )

        val swatches =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.HORIZONTAL

                gravity =
                    Gravity.CENTER_VERTICAL
            }

        val quickColors =
            listOf(
                "#FFFFFF",
                "#FF0000",
                "#FF9500",
                "#FFFF00",
                "#00FF00",
                "#00FFFF",
                "#008CFF",
                "#0000FF",
                "#A855F7",
                "#FF00FF"
            )

        for (quickColor in quickColors) {

            val swatch =
                View(this).apply {

                    background =
                        createColorDrawable(
                            quickColor
                        )

                    isClickable =
                        true

                    isFocusable =
                        true

                    setOnClickListener {
                        picker.setColor(
                            parseColor(
                                quickColor
                            )
                        )
                    }
                }

            swatches.addView(
                swatch,
                LinearLayout.LayoutParams(
                    30,
                    30
                ).apply {
                    rightMargin =
                        8
                }
            )
        }

        container.addView(
            swatches,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    8
            }
        )

        fun refreshInformation(
            color: Int
        ) {

            val hex =
                colorToHex(color)

            val red =
                Color.red(color)

            val green =
                Color.green(color)

            val blue =
                Color.blue(color)

            val hsl =
                rgbToHsl(
                    red,
                    green,
                    blue
                )

            selectedColor.background =
                createColorDrawable(hex)

            rgbValue.text =
                getString(
                    R.string.rgb_value,
                    red,
                    green,
                    blue
                )

            hslValue.text =
                getString(
                    R.string.hsl_value,
                    hsl[0].roundToInt(),
                    hsl[1].roundToInt(),
                    hsl[2].roundToInt()
                )
        }

        var updatingHex =
            false

        picker.onColorChanged =
            { color ->

                val hex =
                    colorToHex(color)

                if (!updatingHex) {

                    updatingHex = true

                    hexInput.setText(hex)

                    hexInput.setSelection(
                        hexInput.text.length
                    )

                    updatingHex = false
                }

                refreshInformation(color)
            }

        hexInput.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    if (updatingHex) {
                        return
                    }

                    val normalized =
                        normalizeHexColor(
                            s?.toString()
                        ) ?: return

                    updatingHex = true

                    picker.setColor(
                        parseColor(normalized)
                    )

                    updatingHex = false
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        copyButton.setOnClickListener {

            val clipboard =
                getSystemService(
                    ClipboardManager::class.java
                )

            val value =
                hexInput.text
                    .toString()
                    .trim()

            clipboard.setPrimaryClip(
                ClipData.newPlainText(
                    getString(
                        R.string.hex_colour_clipboard_label
                    ),
                    value
                )
            )
        }

        val dialog =
            AlertDialog.Builder(this)
                .setTitle(
                    getString(
                        R.string.colour_dialog_title,
                        elementName
                    )
                )
                .setView(container)
                .setNegativeButton(
                    R.string.cancel,
                    null
                )
                .setPositiveButton(
                    R.string.apply,
                    null
                )
                .create()

        dialog.setOnShowListener {

            refreshInformation(
                picker.getColor()
            )

            dialog
                .getButton(
                    AlertDialog.BUTTON_POSITIVE
                )
                .setOnClickListener {

                    val color =
                        normalizeHexColor(
                            hexInput.text.toString()
                        )

                    if (color == null) {

                        hexInput.error =
                            getString(
                                R.string.invalid_hex_colour
                            )

                        return@setOnClickListener
                    }

                    saveColor(color)
                    updateValue(color)

                    updateAllWidgets()

                    dialog.dismiss()
                }
        }

        dialog.show()

        if (
            isTrueBlackEnabled(this) &&
            (
                    resources.configuration.uiMode and
                            Configuration.UI_MODE_NIGHT_MASK
                    ) == Configuration.UI_MODE_NIGHT_YES
        ) {
            dialog.window?.setBackgroundDrawable(
                Color.BLACK.toDrawable()
            )
        }
    }

    private fun colorToHex(
        color: Int
    ): String {

        return String.format(
            Locale.US,
            "#%02X%02X%02X",
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )
    }

    private fun normalizeHexColor(
        input: String?
    ): String? {

        var value =
            input
                ?.trim()
                ?.uppercase(Locale.US)
                ?: return null

        if (value.isEmpty()) {
            return null
        }

        if (!value.startsWith("#")) {
            value = "#$value"
        }

        if (
            !Regex(
                "^#[0-9A-F]{6}$"
            ).matches(value)
        ) {
            return null
        }

        return value
    }

    private fun rgbToHsl(
        red: Int,
        green: Int,
        blue: Int
    ): FloatArray {

        val r = red / 255f
        val g = green / 255f
        val b = blue / 255f

        val maxValue =
            max(
                r,
                max(g, b)
            )

        val minValue =
            minOf(
                r,
                minOf(g, b)
            )

        val delta =
            maxValue - minValue

        var hue = 0f

        val lightness =
            (
                    maxValue +
                            minValue
                    ) / 2f

        var saturation = 0f

        if (delta != 0f) {

            saturation =
                delta /
                        (
                                1f -
                                        kotlin.math.abs(
                                            2f *
                                                    lightness -
                                                    1f
                                        )
                                )

            hue =
                when (maxValue) {

                    r -> {
                        60f *
                                (
                                        (
                                                (g - b) /
                                                        delta
                                                ) % 6f
                                        )
                    }

                    g -> {
                        60f *
                                (
                                        (
                                                (b - r) /
                                                        delta +
                                                        2f
                                                )
                                        )
                    }

                    else -> {
                        60f *
                                (
                                        (
                                                (r - g) /
                                                        delta +
                                                        4f
                                                )
                                        )
                    }
                }

            if (hue < 0f) {
                hue += 360f
            }
        }

        return floatArrayOf(
            hue,
            saturation * 100f,
            lightness * 100f
        )
    }

    private fun applyPreviewWallpaper() {

        preview.setBackgroundColor(
            "#D0D0D0".toColorInt()
        )

        if (!Environment.isExternalStorageManager()) {
            return
        }

        val wallpaperManager =
            getSystemService(
                WallpaperManager::class.java
            )

        val wallpaperFile =
            try {
                wallpaperManager.getWallpaperFile(
                    WallpaperManager.FLAG_SYSTEM
                )
            } catch (
                _: Exception
            ) {
                null
            }

        if (wallpaperFile == null) {
            return
        }

        wallpaperFile.use { file ->

            val bitmap =
                BitmapFactory.decodeFileDescriptor(
                    file.fileDescriptor
                )

            if (bitmap != null) {

                preview.background =
                    bitmap.toDrawable(resources)
            }
        }
    }

    private fun applyTrueBlackBackground() {

        val trueBlack =
            isTrueBlackEnabled(this)

        val isDarkMode =
            (
                    resources.configuration.uiMode and
                            Configuration.UI_MODE_NIGHT_MASK
                    ) ==
                    Configuration.UI_MODE_NIGHT_YES

        if (
            trueBlack &&
            isDarkMode
        ) {

            root.setBackgroundColor(
                Color.BLACK
            )

        } else {

            root.setBackgroundColor(
                Color.TRANSPARENT
            )
        }
    }

    private fun showAboutDialog() {

        val container =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL

                setPadding(
                    32,
                    8,
                    32,
                    8
                )
            }

        val description =
            TextView(this).apply {

                text =
                    getString(
                        R.string.about_description
                    )

                textSize =
                    16f
            }

        container.addView(
            description,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        val createdByLabel =
            TextView(this).apply {

                text =
                    getString(
                        R.string.created_by
                    )

                textSize =
                    13f

                setTypeface(
                    null,
                    Typeface.BOLD
                )

                layoutParams =
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topMargin =
                            24
                    }
            }

        container.addView(createdByLabel)

        val creatorLink =
            TextView(this).apply {

                text =
                    getString(
                        R.string.created_by_name
                    )

                textSize =
                    16f

                isClickable =
                    true

                isFocusable =
                    true

                setOnClickListener {

                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            "https://github.com/Dark-Witcher"
                                .toUri()
                        )
                    )
                }
            }

        container.addView(
            creatorLink,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    4
            }
        )

        val attributionText =
            getString(
                R.string.about_attribution
            )

        val attributionLinkText =
            getString(
                R.string.about_attribution_link_text
            )

        val attributionStart =
            attributionText.indexOf(
                attributionLinkText
            )

        val attributionEnd =
            attributionStart +
                    attributionLinkText.length

        val attribution =
            TextView(this).apply {

                textSize =
                    13f

                movementMethod =
                    android.text.method.LinkMovementMethod.getInstance()

                highlightColor =
                    Color.TRANSPARENT

                layoutParams =
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topMargin =
                            24
                    }
            }

        val attributionColor =
            attribution.currentTextColor

        val attributionSpannable =
            android.text.SpannableString(
                attributionText
            )

        if (attributionStart >= 0) {

            attributionSpannable.setSpan(
                object :
                    android.text.style.ClickableSpan() {

                    override fun onClick(
                        widget: View
                    ) {

                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                "https://github.com/Prayag2/kde_modernclock"
                                    .toUri()
                            )
                        )
                    }

                    override fun updateDrawState(
                        drawState:
                        android.text.TextPaint
                    ) {

                        drawState.color =
                            attributionColor

                        drawState.isUnderlineText =
                            false
                    }
                },
                attributionStart,
                attributionEnd,
                android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        attribution.text =
            attributionSpannable

        container.addView(attribution)

        val dialog =
            AlertDialog.Builder(this)
                .setTitle(
                    getString(
                        R.string.about_title,
                        getString(
                            R.string.app_version
                        )
                    )
                )
                .setView(container)
                .setNeutralButton(
                    R.string.github,
                    null
                )
                .setPositiveButton(
                    R.string.close,
                    null
                )
                .create()

        dialog.setOnShowListener {

            dialog
                .getButton(
                    AlertDialog.BUTTON_NEUTRAL
                )
                .setOnClickListener {

                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            "https://github.com/Dark-Witcher/modern-clock-android"
                                .toUri()
                        )
                    )
                }
        }

        dialog.show()

        if (
            isTrueBlackEnabled(this) &&
            (
                    resources.configuration.uiMode and
                            Configuration.UI_MODE_NIGHT_MASK
                    ) == Configuration.UI_MODE_NIGHT_YES
        ) {
            dialog.window?.setBackgroundDrawable(
                Color.BLACK.toDrawable()
            )
        }
    }

    private fun isValidDateFormat(
        format: String
    ): Boolean {

        return try {

            SimpleDateFormat(
                format,
                Locale.getDefault()
            )

            true

        } catch (
            _: IllegalArgumentException
        ) {

            false
        }
    }

    private fun saveCustomDateFormat() {

        val format =
            dateFormatInput
                .text
                .toString()
                .trim()

        if (format.isBlank()) {
            return
        }

        if (!isValidDateFormat(format)) {
            return
        }

        saveGlobalDateFormat(
            this,
            format
        )
    }

    private fun saveCustomTimeFormat() {

        val format =
            timeFormatInput
                .text
                .toString()
                .trim()

        if (format.isBlank()) {
            return
        }

        if (!isValidDateFormat(format)) {
            return
        }

        saveGlobalTimeFormat(
            this,
            format
        )
    }

    private fun updateAllWidgets() {

        val appWidgetManager =
            AppWidgetManager.getInstance(this)

        val componentName =
            ComponentName(
                this,
                ModernClockWidget::class.java
            )

        val widgetIds =
            appWidgetManager.getAppWidgetIds(
                componentName
            )

        if (widgetIds.isEmpty()) {
            return
        }

        val updateIntent =
            Intent(
                this,
                ModernClockWidget::class.java
            ).apply {

                action =
                    AppWidgetManager.ACTION_APPWIDGET_UPDATE

                putExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_IDS,
                    widgetIds
                )
            }

        sendBroadcast(updateIntent)
    }

    private fun updatePreview() {

        val width =
            preview.width

        val height =
            preview.height

        if (
            width <= 0 ||
            height <= 0
        ) {

            preview.post {
                updatePreview()
            }

            return
        }

        val timeFormat =
            timeFormatInput.text
                .toString()
                .trim()
                .ifBlank {
                    DEFAULT_TIME_FORMAT
                }

        val dateFormat =
            dateFormatInput.text
                .toString()
                .trim()
                .ifBlank {
                    DEFAULT_DATE_FORMAT
                }

        val showDay =
            showDaySwitch.isChecked

        if (!isValidDateFormat(timeFormat)) {
            return
        }

        if (!isValidDateFormat(dateFormat)) {
            return
        }

        val fittedSizes =
            getFittedPreviewFontSizes()

        val currentDay =
            getGlobalDayFontSize(this)

        val currentDate =
            getGlobalDateFontSize(this)

        val currentTime =
            getGlobalTimeFontSize(this)

        if (
            fittedSizes.day != currentDay ||
            fittedSizes.date != currentDate ||
            fittedSizes.time != currentTime
        ) {

            saveFittedFontSizes(fittedSizes)
            updateFontSizeControls(fittedSizes)
        }

        preview.setImageBitmap(
            ModernClockWidget.createPreviewBitmap(
                context = this,
                width = width,
                height = height,
                timeFormat = timeFormat,
                dateFormat = dateFormat,
                showDay = showDay
            )
        )
    }

    private class ColorPickerView(
        context: Context,
        initialColor: Int
    ) : View(context) {

        private val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)

        private val selectorPaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style =
                    Paint.Style.STROKE

                strokeWidth =
                    3f

                color =
                    Color.WHITE
            }

        private val shadowPaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style =
                    Paint.Style.STROKE

                strokeWidth =
                    5f

                color =
                    Color.BLACK
            }

        private var hue = 0f
        private var saturation = 0f
        private var value = 1f

        private var selectorX = 0f
        private var selectorY = 0f
        private var hueX = 0f

        private var hueGradient:
                LinearGradient? = null

        private var whiteGradient:
                LinearGradient? = null

        private var blackGradient:
                LinearGradient? = null

        var onColorChanged:
                ((Int) -> Unit)? = null

        init {

            val hsv =
                FloatArray(3)

            Color.colorToHSV(
                initialColor,
                hsv
            )

            hue = hsv[0]
            saturation = hsv[1]
            value = hsv[2]

            setLayerType(
                LAYER_TYPE_SOFTWARE,
                null
            )
        }

        fun getColor(): Int {

            return Color.HSVToColor(
                floatArrayOf(
                    hue,
                    saturation,
                    value
                )
            )
        }

        fun setColor(
            color: Int
        ) {

            val hsv =
                FloatArray(3)

            Color.colorToHSV(
                color,
                hsv
            )

            hue = hsv[0]
            saturation = hsv[1]
            value = hsv[2]

            updatePositions()
            invalidate()

            onColorChanged?.invoke(
                getColor()
            )
        }

        override fun onSizeChanged(
            width: Int,
            height: Int,
            oldWidth: Int,
            oldHeight: Int
        ) {

            super.onSizeChanged(
                width,
                height,
                oldWidth,
                oldHeight
            )

            hueGradient =
                LinearGradient(
                    0f,
                    0f,
                    width.toFloat(),
                    0f,
                    intArrayOf(
                        Color.RED,
                        Color.MAGENTA,
                        Color.BLUE,
                        Color.CYAN,
                        Color.GREEN,
                        Color.YELLOW,
                        Color.RED
                    ),
                    null,
                    Shader.TileMode.CLAMP
                )

            whiteGradient =
                LinearGradient(
                    0f,
                    0f,
                    width.toFloat(),
                    0f,
                    Color.WHITE,
                    Color.TRANSPARENT,
                    Shader.TileMode.CLAMP
                )

            blackGradient =
                LinearGradient(
                    0f,
                    0f,
                    0f,
                    height * 0.78f,
                    Color.TRANSPARENT,
                    Color.BLACK,
                    Shader.TileMode.CLAMP
                )

            updatePositions()
        }

        private fun updatePositions() {

            if (
                width <= 0 ||
                height <= 0
            ) {
                return
            }

            val padding =
                8f

            val fieldHeight =
                height * 0.78f

            selectorX =
                padding +
                        saturation *
                        (
                                width -
                                        padding * 2f
                                )

            selectorY =
                padding +
                        (1f - value) *
                        (
                                fieldHeight -
                                        padding * 2f
                                )

            hueX =
                padding +
                        (
                                hue / 360f
                                ) *
                        (
                                width -
                                        padding * 2f
                                )
        }

        override fun onDraw(
            canvas: Canvas
        ) {

            super.onDraw(canvas)

            val padding =
                8f

            val fieldHeight =
                height * 0.78f

            val hueColor =
                Color.HSVToColor(
                    floatArrayOf(
                        hue,
                        1f,
                        1f
                    )
                )

            paint.shader = null
            paint.color = hueColor

            canvas.drawRoundRect(
                padding,
                padding,
                width - padding,
                fieldHeight,
                16f,
                16f,
                paint
            )

            paint.shader = whiteGradient

            canvas.drawRoundRect(
                padding,
                padding,
                width - padding,
                fieldHeight,
                16f,
                16f,
                paint
            )

            paint.shader = blackGradient

            canvas.drawRoundRect(
                padding,
                padding,
                width - padding,
                fieldHeight,
                16f,
                16f,
                paint
            )

            paint.shader = null

            canvas.drawCircle(
                selectorX,
                selectorY,
                7f,
                shadowPaint
            )

            canvas.drawCircle(
                selectorX,
                selectorY,
                6f,
                selectorPaint
            )

            val hueTop =
                fieldHeight + 14f

            val hueBottom =
                hueTop + 24f

            paint.shader =
                hueGradient

            canvas.drawRoundRect(
                padding,
                hueTop,
                width - padding,
                hueBottom,
                12f,
                12f,
                paint
            )

            paint.shader = null

            val hueSelectorY =
                (
                        hueTop +
                                hueBottom
                        ) / 2f

            canvas.drawCircle(
                hueX,
                hueSelectorY,
                7f,
                shadowPaint
            )

            canvas.drawCircle(
                hueX,
                hueSelectorY,
                6f,
                selectorPaint
            )
        }

        override fun onTouchEvent(
            event: MotionEvent
        ): Boolean {

            if (
                event.action !in
                MotionEvent.ACTION_DOWN..
                MotionEvent.ACTION_UP
            ) {
                return false
            }

            val padding =
                8f

            val fieldHeight =
                height * 0.78f

            val hueTop =
                fieldHeight + 14f

            val hueBottom =
                hueTop + 24f

            when {

                event.y <= fieldHeight -> {

                    saturation =
                        (
                                (
                                        event.x -
                                                padding
                                        ) /
                                        (
                                                width -
                                                        padding * 2f
                                                )
                                )
                            .coerceIn(
                                0f,
                                1f
                            )

                    value =
                        (
                                1f -
                                        (
                                                (
                                                        event.y -
                                                                padding
                                                        ) /
                                                        (
                                                                fieldHeight -
                                                                        padding * 2f
                                                                )
                                                )
                                )
                            .coerceIn(
                                0f,
                                1f
                            )

                    selectorX =
                        padding +
                                saturation *
                                (
                                        width -
                                                padding * 2f
                                        )

                    selectorY =
                        padding +
                                (1f - value) *
                                (
                                        fieldHeight -
                                                padding * 2f
                                        )
                }

                event.y in hueTop..hueBottom -> {

                    hue =
                        (
                                (
                                        event.x -
                                                padding
                                        ) /
                                        (
                                                width -
                                                        padding * 2f
                                                )
                                )
                            .coerceIn(
                                0f,
                                1f
                            ) *
                                360f

                    hueX =
                        padding +
                                (
                                        hue / 360f
                                        ) *
                                (
                                        width -
                                                padding * 2f
                                        )
                }

                else -> {
                    return true
                }
            }

            invalidate()

            onColorChanged?.invoke(
                getColor()
            )

            if (
                event.action ==
                MotionEvent.ACTION_UP
            ) {
                performClick()
            }

            return true
        }

        override fun performClick(): Boolean {

            super.performClick()

            return true
        }
    }
}
