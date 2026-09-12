package com.example.modernclock

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class AppSettingsActivity : AppCompatActivity() {

    companion object {

        private const val PREFS_NAME =
            "modern_clock_app_preferences"

        private const val KEY_THEME =
            "app_theme"

        private const val KEY_TRUE_BLACK =
            "true_black"

        private const val THEME_SYSTEM =
            "system"

        private const val THEME_LIGHT =
            "light"

        private const val THEME_DARK =
            "dark"

        fun applyTheme(context: android.content.Context) {

            val preferences =
                context.getSharedPreferences(
                    PREFS_NAME,
                    android.content.Context.MODE_PRIVATE
                )

            when (
                preferences.getString(
                    KEY_THEME,
                    THEME_SYSTEM
                )
            ) {

                THEME_LIGHT -> {
                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO
                    )
                }

                THEME_DARK -> {
                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES
                    )
                }

                else -> {
                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    )
                }
            }
        }

        fun isTrueBlackEnabled(
            context: android.content.Context
        ): Boolean {

            return context
                .getSharedPreferences(
                    PREFS_NAME,
                    android.content.Context.MODE_PRIVATE
                )
                .getBoolean(
                    KEY_TRUE_BLACK,
                    false
                )
        }
    }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        applyTheme(this)

        super.onCreate(savedInstanceState)

        /*
         * =====================================================
         * ROOT
         * =====================================================
         */

        val root =
            LinearLayout(this).apply {

                orientation =
                    LinearLayout.VERTICAL

                setPadding(
                    32,
                    48,
                    32,
                    32
                )

                if (
                    isTrueBlackEnabled(this@AppSettingsActivity)
                ) {

                    setBackgroundColor(
                        android.graphics.Color.BLACK
                    )
                }
            }

        /*
         * =====================================================
         * TITLE
         * =====================================================
         */

        val title =
            TextView(this).apply {

                text =
                    "App Settings"

                textSize =
                    28f

                gravity =
                    Gravity.CENTER

                setPadding(
                    0,
                    0,
                    0,
                    32
                )
            }

        root.addView(
            title,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        /*
         * =====================================================
         * APPEARANCE SECTION
         * =====================================================
         */

        val appearanceTitle =
            TextView(this).apply {

                text =
                    "Appearance"

                textSize =
                    18f

                setTypeface(
                    null,
                    android.graphics.Typeface.BOLD
                )
            }

        root.addView(
            appearanceTitle,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        /*
         * =====================================================
         * THEME RADIO GROUP
         * =====================================================
         */

        val radioGroup =
            RadioGroup(this).apply {

                orientation =
                    RadioGroup.VERTICAL

                setPadding(
                    0,
                    12,
                    0,
                    0
                )
            }

        val systemRadio =
            RadioButton(this).apply {

                text =
                    "Same as device"

                textSize =
                    16f

                tag =
                    THEME_SYSTEM
            }

        val lightRadio =
            RadioButton(this).apply {

                text =
                    "Light"

                textSize =
                    16f

                tag =
                    THEME_LIGHT
            }

        val darkRadio =
            RadioButton(this).apply {

                text =
                    "Dark"

                textSize =
                    16f

                tag =
                    THEME_DARK
            }

        radioGroup.addView(
            systemRadio
        )

        radioGroup.addView(
            lightRadio
        )

        radioGroup.addView(
            darkRadio
        )

        root.addView(
            radioGroup,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        /*
         * =====================================================
         * OLED SECTION
         * =====================================================
         */

        val oledTitle =
            TextView(this).apply {

                text =
                    "OLED"

                textSize =
                    18f

                setTypeface(
                    null,
                    android.graphics.Typeface.BOLD
                )
            }

        root.addView(
            oledTitle,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {

                topMargin =
                    32
            }
        )

        val trueBlackSwitch =
            Switch(this).apply {

                text =
                    "True black background"

                textSize =
                    16f

                isChecked =
                    isTrueBlackEnabled(
                        this@AppSettingsActivity
                    )

                setPadding(
                    0,
                    8,
                    0,
                    0
                )
            }

        root.addView(
            trueBlackSwitch,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        val trueBlackDescription =
            TextView(this).apply {

                text =
                    "Use #000000 as the app background in Dark mode for OLED displays."

                textSize =
                    13f

                setPadding(
                    0,
                    4,
                    0,
                    0
                )
            }

        root.addView(
            trueBlackDescription,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        /*
         * =====================================================
         * LOAD CURRENT THEME
         * =====================================================
         */

        val preferences =
            getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            )

        when (
            preferences.getString(
                KEY_THEME,
                THEME_SYSTEM
            )
        ) {

            THEME_LIGHT -> {
                lightRadio.isChecked = true
            }

            THEME_DARK -> {
                darkRadio.isChecked = true
            }

            else -> {
                systemRadio.isChecked = true
            }
        }

        /*
         * =====================================================
         * THEME CHANGE
         * =====================================================
         */

        radioGroup.setOnCheckedChangeListener {
                _,
                checkedId ->

            val selectedRadio =
                findViewById<RadioButton>(
                    checkedId
                )

            val selectedTheme =
                selectedRadio.tag
                    ?.toString()
                    ?: THEME_SYSTEM

            preferences.edit {

                putString(
                    KEY_THEME,
                    selectedTheme
                )
            }

            applyTheme(this)

            recreate()
        }

        /*
         * =====================================================
         * TRUE BLACK CHANGE
         * =====================================================
         */

        trueBlackSwitch.setOnCheckedChangeListener {
                _,
                checked ->

            preferences.edit {

                putBoolean(
                    KEY_TRUE_BLACK,
                    checked
                )
            }

            applyTrueBlackBackground(
                root
            )
        }

        /*
         * =====================================================
         * SET CONTENT
         * =====================================================
         */

        setContentView(root)

        applyTrueBlackBackground(
            root
        )
    }

    private fun applyTrueBlackBackground(
        root: View
    ) {

        val preferences =
            getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            )

        val theme =
            preferences.getString(
                KEY_THEME,
                THEME_SYSTEM
            )

        val trueBlack =
            preferences.getBoolean(
                KEY_TRUE_BLACK,
                false
            )

        val isDarkMode =
            when (theme) {

                THEME_DARK -> true

                THEME_LIGHT -> false

                else ->
                    resources.configuration.uiMode and
                            android.content.res.Configuration.UI_MODE_NIGHT_MASK ==
                            android.content.res.Configuration.UI_MODE_NIGHT_YES
            }

        if (
            isDarkMode &&
            trueBlack
        ) {

            root.setBackgroundColor(
                android.graphics.Color.BLACK
            )

        } else {

            root.setBackgroundColor(
                android.graphics.Color.TRANSPARENT
            )
        }
    }
}