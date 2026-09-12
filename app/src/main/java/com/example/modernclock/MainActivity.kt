package com.example.modernclock

import android.app.AlertDialog
import android.appwidget.AppWidgetManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import androidx.core.graphics.toColorInt
import androidx.core.net.toUri
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

        private const val KEY_DAY_LETTER_SPACING =
            "day_letter_spacing"

        private const val DEFAULT_TIME_FORMAT =
            "HH:mm"

        private const val DEFAULT_DATE_FORMAT =
            "dd MMM yyyy"

        private const val DEFAULT_COLOR =
            "#FFFFFF"

        private const val DEFAULT_DAY_LETTER_SPACING =
            17f

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

        fun saveGlobalDayColor(
            context: Context,
            color: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_DAY_COLOR,
                    color
                )
            }
        }

        fun getGlobalDayColor(
            context: Context
        ): String {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getString(
                KEY_DAY_COLOR,
                DEFAULT_COLOR
            ) ?: DEFAULT_COLOR
        }

        fun saveGlobalDateColor(
            context: Context,
            color: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_DATE_COLOR,
                    color
                )
            }
        }

        fun getGlobalDateColor(
            context: Context
        ): String {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getString(
                KEY_DATE_COLOR,
                DEFAULT_COLOR
            ) ?: DEFAULT_COLOR
        }

        fun saveGlobalTimeColor(
            context: Context,
            color: String
        ) {
            context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).edit {
                putString(
                    KEY_TIME_COLOR,
                    color
                )
            }
        }

        fun getGlobalTimeColor(
            context: Context
        ): String {
            return context.getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            ).getString(
                KEY_TIME_COLOR,
                DEFAULT_COLOR
            ) ?: DEFAULT_COLOR
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

    private lateinit var dayColorSwatch: View
    private lateinit var dateColorSwatch: View
    private lateinit var timeColorSwatch: View

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        createInterface()
    }

    override fun onResume() {
        super.onResume()

        if (::root.isInitialized) {
            applyTrueBlackBackground()
        }
    }

    private fun createInterface() {

        val scrollView =
            ScrollView(this).apply {
                isFillViewport = true
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
                    "☰"

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
                    8
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

                setBackgroundColor(
                    Color.BLACK
                )
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

        val dayTitle =
            TextView(this).apply {

                text =
                    getString(
                        R.string.day_settings
                    )

                textSize =
                    16f

                setTypeface(
                    null,
                    Typeface.BOLD
                )
            }

        root.addView(
            dayTitle,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    16
            }
        )

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

        root.addView(
            showDaySwitch,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    8
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

        root.addView(
            daySpacingLabel,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    16
            }
        )

        dayLetterSpacingInput =
            EditText(this).apply {

                setSingleLine(
                    true
                )

                textSize =
                    16f

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

        root.addView(
            dayLetterSpacingInput,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    8
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

        root.addView(
            daySpacingHelp,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    6
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

        root.addView(
            dayColorRow.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    8
            }
        )

        val dateTitle =
            TextView(this).apply {

                text =
                    getString(
                        R.string.date_settings
                    )

                textSize =
                    16f

                setTypeface(
                    null,
                    Typeface.BOLD
                )
            }

        root.addView(
            dateTitle,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    24
            }
        )

        dateFormatInput =
            EditText(this).apply {

                setSingleLine(
                    true
                )

                textSize =
                    16f

                hint =
                    DEFAULT_DATE_FORMAT

                imeOptions =
                    EditorInfo.IME_ACTION_DONE

                setText(
                    getGlobalDateFormat(
                        this@MainActivity
                    )
                )

                setSelection(
                    text.length
                )
            }

        root.addView(
            dateFormatInput,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    8
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

        root.addView(
            dateFormatHelp,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    6
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

        root.addView(
            dateColorRow.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    16
            }
        )

        val timeTitle =
            TextView(this).apply {

                text =
                    getString(
                        R.string.time_settings
                    )

                textSize =
                    16f

                setTypeface(
                    null,
                    Typeface.BOLD
                )
            }

        root.addView(
            timeTitle,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    24
            }
        )

        timeFormatInput =
            EditText(this).apply {

                setSingleLine(
                    true
                )

                textSize =
                    16f

                hint =
                    DEFAULT_TIME_FORMAT

                imeOptions =
                    EditorInfo.IME_ACTION_DONE

                setText(
                    getGlobalTimeFormat(
                        this@MainActivity
                    )
                )

                setSelection(
                    text.length
                )
            }

        root.addView(
            timeFormatInput,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    8
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

        root.addView(
            timeFormatHelp,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    6
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

        root.addView(
            timeColorRow.first,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin =
                    16
            }
        )

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

        dayLetterSpacingInput.addTextChangedListener(
            object : android.text.TextWatcher {

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
                    s: android.text.Editable?
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
            }
        }

        dateFormatInput.addTextChangedListener(
            object : android.text.TextWatcher {

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
                    s: android.text.Editable?
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
            }
        }

        timeFormatInput.addTextChangedListener(
            object : android.text.TextWatcher {

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
                    s: android.text.Editable?
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
            }
        }

        setContentView(
            scrollView
        )

        updatePreview()
        applyTrueBlackBackground()
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
            dayLetterSpacingInput
                .text
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
                createColorDrawable(
                    color
                )
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

        row.addView(
            labelView
        )

        val valueView =
            TextView(this).apply {

                text =
                    color

                textSize =
                    15f

                gravity =
                    Gravity.CENTER_VERTICAL
            }

        row.addView(
            valueView
        )

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
            parseColor(
                color
            )
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
            parseColor(
                currentColor
            )

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
                    createColorDrawable(
                        currentColor
                    )
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

                setSingleLine(
                    true
                )

                textSize =
                    16f

                hint =
                    "#FFFFFF"

                setText(
                    colorToHex(
                        initialColor
                    )
                )

                setSelection(
                    text.length
                )

                imeOptions =
                    EditorInfo.IME_ACTION_DONE

                layoutParams =
                    LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1f
                    )
            }

        hexRow.addView(
            hexInput
        )

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

        container.addView(
            hexRow
        )

        val rgbValue =
            TextView(this).apply {
                textSize =
                    13f
            }

        val hslValue =
            TextView(this).apply {
                textSize =
                    13f
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

        for (
        quickColor in quickColors
        ) {

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
                colorToHex(
                    color
                )

            val red =
                Color.red(
                    color
                )

            val green =
                Color.green(
                    color
                )

            val blue =
                Color.blue(
                    color
                )

            val hsl =
                rgbToHsl(
                    red,
                    green,
                    blue
                )

            selectedColor.background =
                createColorDrawable(
                    hex
                )

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
                    colorToHex(
                        color
                    )

                if (!updatingHex) {

                    updatingHex =
                        true

                    hexInput.setText(
                        hex
                    )

                    hexInput.setSelection(
                        hexInput.text.length
                    )

                    updatingHex =
                        false
                }

                refreshInformation(
                    color
                )
            }

        hexInput.addTextChangedListener(
            object : android.text.TextWatcher {

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

                    updatingHex =
                        true

                    picker.setColor(
                        parseColor(
                            normalized
                        )
                    )

                    updatingHex =
                        false
                }

                override fun afterTextChanged(
                    s: android.text.Editable?
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
                .setView(
                    container
                )
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

                    saveColor(
                        color
                    )

                    updateValue(
                        color
                    )

                    updateAllWidgets()

                    dialog.dismiss()
                }
        }

        dialog.show()
    }

    private fun colorToHex(
        color: Int
    ): String {

        return String.format(
            Locale.US,
            "#%02X%02X%02X",
            Color.red(
                color
            ),
            Color.green(
                color
            ),
            Color.blue(
                color
            )
        )
    }

    private fun normalizeHexColor(
        input: String?
    ): String? {

        var value =
            input
                ?.trim()
                ?.uppercase(
                    Locale.US
                )
                ?: return null

        if (value.isEmpty()) {
            return null
        }

        if (!value.startsWith("#")) {
            value =
                "#$value"
        }

        if (
            !Regex(
                "^#[0-9A-F]{6}$"
            ).matches(
                value
            )
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

        val r =
            red / 255f

        val g =
            green / 255f

        val b =
            blue / 255f

        val maxValue =
            max(
                r,
                max(
                    g,
                    b
                )
            )

        val minValue =
            minOf(
                r,
                minOf(
                    g,
                    b
                )
            )

        val delta =
            maxValue -
                    minValue

        var hue =
            0f

        val lightness =
            (
                    maxValue +
                            minValue
                    ) / 2f

        var saturation =
            0f

        if (
            delta != 0f
        ) {

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

            if (
                hue < 0f
            ) {
                hue +=
                    360f
            }
        }

        return floatArrayOf(
            hue,
            saturation * 100f,
            lightness * 100f
        )
    }

    private fun applyTrueBlackBackground() {

        val trueBlack =
            isTrueBlackEnabled(
                this
            )

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

        val message =
            getString(
                R.string.about_message
            )

        val dialog =
            AlertDialog.Builder(this)
                .setTitle(
                    getString(
                        R.string.app_name
                    )
                )
                .setMessage(
                    message
                )
                .setPositiveButton(
                    R.string.github
                ) { _, _ ->

                    val intent =
                        Intent(
                            Intent.ACTION_VIEW,
                            "https://github.com/Prayag2/kde_modernclock"
                                .toUri()
                        )

                    startActivity(
                        intent
                    )
                }
                .setNegativeButton(
                    R.string.close,
                    null
                )
                .create()

        dialog.show()
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

        if (
            !isValidDateFormat(
                format
            )
        ) {
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

        if (
            !isValidDateFormat(
                format
            )
        ) {
            return
        }

        saveGlobalTimeFormat(
            this,
            format
        )
    }

    private fun updateAllWidgets() {

        val appWidgetManager =
            AppWidgetManager.getInstance(
                this
            )

        val componentName =
            ComponentName(
                this,
                ModernClockWidget::class.java
            )

        val widgetIds =
            appWidgetManager.getAppWidgetIds(
                componentName
            )

        if (
            widgetIds.isEmpty()
        ) {
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

        sendBroadcast(
            updateIntent
        )
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
            timeFormatInput
                .text
                .toString()
                .trim()
                .ifBlank {
                    DEFAULT_TIME_FORMAT
                }

        val dateFormat =
            dateFormatInput
                .text
                .toString()
                .trim()
                .ifBlank {
                    DEFAULT_DATE_FORMAT
                }

        val showDay =
            showDaySwitch.isChecked

        if (
            !isValidDateFormat(
                timeFormat
            )
        ) {
            return
        }

        if (
            !isValidDateFormat(
                dateFormat
            )
        ) {
            return
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
            Paint(
                Paint.ANTI_ALIAS_FLAG
            )

        private val selectorPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {

                style =
                    Paint.Style.STROKE

                strokeWidth =
                    3f

                color =
                    Color.WHITE
            }

        private val shadowPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {

                style =
                    Paint.Style.STROKE

                strokeWidth =
                    5f

                color =
                    Color.BLACK
            }

        private var hue =
            0f

        private var saturation =
            0f

        private var value =
            1f

        private var selectorX =
            0f

        private var selectorY =
            0f

        private var hueX =
            0f

        private var hueGradient:
                LinearGradient? =
            null

        private var whiteGradient:
                LinearGradient? =
            null

        private var blackGradient:
                LinearGradient? =
            null

        var onColorChanged:
                ((Int) -> Unit)? =
            null

        init {

            val hsv =
                FloatArray(3)

            Color.colorToHSV(
                initialColor,
                hsv
            )

            hue =
                hsv[0]

            saturation =
                hsv[1]

            value =
                hsv[2]

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

            hue =
                hsv[0]

            saturation =
                hsv[1]

            value =
                hsv[2]

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

            super.onDraw(
                canvas
            )

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

            paint.shader =
                null

            paint.color =
                hueColor

            canvas.drawRoundRect(
                padding,
                padding,
                width - padding,
                fieldHeight,
                16f,
                16f,
                paint
            )

            paint.shader =
                whiteGradient

            canvas.drawRoundRect(
                padding,
                padding,
                width - padding,
                fieldHeight,
                16f,
                16f,
                paint
            )

            paint.shader =
                blackGradient

            canvas.drawRoundRect(
                padding,
                padding,
                width - padding,
                fieldHeight,
                16f,
                16f,
                paint
            )

            paint.shader =
                null

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

            paint.shader =
                null

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