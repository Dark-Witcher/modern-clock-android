package com.example.modernclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.RemoteViews
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toColorInt
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sqrt

class ModernClockWidget : AppWidgetProvider() {

    /*
     * =========================================================
     * FONT SIZE TYPES
     * =========================================================
     *
     * These are nested directly inside ModernClockWidget rather
     * than inside the companion object so they can be referenced
     * cleanly from MainActivity as:
     *
     * ModernClockWidget.FontSizeValues
     * ModernClockWidget.FontSizeElement
     */

    data class FontSizeValues(
        val day: Int,
        val date: Int,
        val time: Int
    )

    enum class FontSizeElement {
        DAY,
        DATE,
        TIME
    }

    companion object {

        const val ACTION_UPDATE =
            "com.example.modernclock.UPDATE"

        /*
         * =====================================================
         * WIDGET LOCALE
         * =====================================================
         *
         * The widget currently uses English for DAY and DATE.
         *
         * Keep locale selection behind this function so that
         * user-selectable widget locales can be introduced in
         * a future version without changing the rendering code.
         */

        private fun getWidgetLocale(): Locale {
            return Locale.ENGLISH
        }

        /*
         * =====================================================
         * REFERENCE DIMENSIONS
         * =====================================================
         */

        private const val REFERENCE_WIDTH_DP =
            180f

        private const val REFERENCE_HEIGHT_DP =
            40f

        /*
         * =====================================================
         * DAY
         * =====================================================
         */

        private const val DAY_WIDTH_RATIO =
            0.94f

        private const val DEFAULT_DAY_LETTER_SPACING =
            17f

        /*
         * =====================================================
         * FONT SIZE RANGE
         * =====================================================
         */

        private const val MIN_SIZE_PERCENT =
            50

        private const val MAX_SIZE_PERCENT =
            200

        /*
         * =====================================================
         * DATE / TIME
         * =====================================================
         */

        private const val BASE_DATE_TIME_DP =
            9f

        private const val BASE_SPACING_DP =
            7f

        private const val DEFAULT_COLOR =
            "#FFFFFF"

        /*
         * =====================================================
         * TEXT SHADOW
         * =====================================================
         */

        private const val SHADOW_RADIUS_DP =
            2.5f

        private const val SHADOW_OFFSET_X_DP =
            0f

        private const val SHADOW_OFFSET_Y_DP =
            1.5f

        private const val SHADOW_ALPHA =
            180

        /*
         * =====================================================
         * FONT FITTING
         * =====================================================
         */

        private data class LayoutMeasurement(
            val totalHeight: Float,
            val availableHeight: Float
        )

        /*
         * =====================================================
         * SHADOW CONFIGURATION
         * =====================================================
         *
         * Shadow settings are kept as a small independent
         * configuration object so DAY, DATE and TIME can each
         * have their own enabled state and colour without
         * duplicating shadow-rendering logic.
         */

        private data class ShadowConfig(
            val enabled: Boolean,
            val color: Int
        )

        /*
         * =====================================================
         * SHADOW CONFIG HELPERS
         * =====================================================
         */

        private fun getDayShadowConfig(
            context: Context
        ): ShadowConfig {

            return ShadowConfig(
                enabled =
                    MainActivity.getGlobalDayShadowEnabled(
                        context
                    ),
                color =
                    parseShadowColor(
                        MainActivity.getGlobalDayShadowColor(
                            context
                        )
                    )
            )
        }

        private fun getDateShadowConfig(
            context: Context
        ): ShadowConfig {

            return ShadowConfig(
                enabled =
                    MainActivity.getGlobalDateShadowEnabled(
                        context
                    ),
                color =
                    parseShadowColor(
                        MainActivity.getGlobalDateShadowColor(
                            context
                        )
                    )
            )
        }

        private fun getTimeShadowConfig(
            context: Context
        ): ShadowConfig {

            return ShadowConfig(
                enabled =
                    MainActivity.getGlobalTimeShadowEnabled(
                        context
                    ),
                color =
                    parseShadowColor(
                        MainActivity.getGlobalTimeShadowColor(
                            context
                        )
                    )
            )
        }

        private fun parseShadowColor(
            color: String
        ): Int {

            return try {

                color.toColorInt()

            } catch (
                _: IllegalArgumentException
            ) {

                Color.BLACK
            }
        }

        /*
         * =====================================================
         * APPLY SHADOW
         * =====================================================
         *
         * Geometry remains fixed for v1.1:
         *
         * Radius: 2.5dp
         * X:      0dp
         * Y:      1.5dp
         * Alpha:  180
         *
         * The configured colour supplies RGB values while the
         * fixed alpha is applied here.
         */

        private fun applyShadow(
            paint: Paint,
            config: ShadowConfig,
            density: Float
        ) {

            if (!config.enabled) {

                paint.clearShadowLayer()

                return
            }

            val shadowRadius =
                SHADOW_RADIUS_DP *
                        density

            val shadowOffsetX =
                SHADOW_OFFSET_X_DP *
                        density

            val shadowOffsetY =
                SHADOW_OFFSET_Y_DP *
                        density

            val shadowColor =
                Color.argb(
                    SHADOW_ALPHA,
                    Color.red(config.color),
                    Color.green(config.color),
                    Color.blue(config.color)
                )

            paint.setShadowLayer(
                shadowRadius,
                shadowOffsetX,
                shadowOffsetY,
                shadowColor
            )
        }

        /*
         * =====================================================
         * SHARED FONT FITTING CALCULATION
         * =====================================================
         */

        fun calculateFittedFontSizes(
            context: Context,
            widthDp: Int,
            heightDp: Int,
            timeFormat: String,
            dateFormat: String,
            showDay: Boolean,
            dayLetterSpacing: Float,
            requestedDaySizePercent: Int,
            requestedDateSizePercent: Int,
            requestedTimeSizePercent: Int,
            changedElement: FontSizeElement? = null
        ): FontSizeValues {

            val safeWidthDp =
                max(
                    1,
                    widthDp
                )

            val safeHeightDp =
                max(
                    1,
                    heightDp
                )

            var dayResult =
                requestedDaySizePercent.coerceIn(
                    MIN_SIZE_PERCENT,
                    MAX_SIZE_PERCENT
                )

            var dateResult =
                requestedDateSizePercent.coerceIn(
                    MIN_SIZE_PERCENT,
                    MAX_SIZE_PERCENT
                )

            var timeResult =
                requestedTimeSizePercent.coerceIn(
                    MIN_SIZE_PERCENT,
                    MAX_SIZE_PERCENT
                )

            val now =
                Date()

            val widgetLocale =
                getWidgetLocale()

            val dayText =
                SimpleDateFormat(
                    "EEEE",
                    widgetLocale
                )
                    .format(now)
                    .uppercase(
                        widgetLocale
                    )

            val dateText =
                SimpleDateFormat(
                    dateFormat,
                    widgetLocale
                )
                    .format(now)
                    .uppercase(
                        widgetLocale
                    )

            val timeText =
                SimpleDateFormat(
                    timeFormat,
                    Locale.getDefault()
                )
                    .format(now)

            val dayTypeface =
                ResourcesCompat.getFont(
                    context,
                    R.font.anurati
                ) ?: Typeface.DEFAULT

            val secondaryTypeface =
                ResourcesCompat.getFont(
                    context,
                    R.font.poppins
                ) ?: Typeface.DEFAULT

            val density =
                context.resources
                    .displayMetrics
                    .density

            /*
             * =================================================
             * BASE SIZES
             * =================================================
             */

            val heightRatio =
                max(
                    1f,
                    safeHeightDp /
                            REFERENCE_HEIGHT_DP
                )

            val heightScale =
                sqrt(
                    heightRatio
                )

            val widthScale =
                safeWidthDp /
                        REFERENCE_WIDTH_DP

            /*
             * =================================================
             * DAY BASE SIZE
             * =================================================
             */

            val dayPaint =
                Paint(
                    Paint.ANTI_ALIAS_FLAG or
                            Paint.SUBPIXEL_TEXT_FLAG
                ).apply {

                    typeface =
                        dayTypeface

                    textAlign =
                        Paint.Align.CENTER
                }

            val referenceWidthPx =
                REFERENCE_WIDTH_DP *
                        density

            val referenceTargetWidth =
                referenceWidthPx *
                        DAY_WIDTH_RATIO

            var lowSize =
                1f

            var highSize =
                300f *
                        density

            repeat(24) {

                val testSize =
                    (
                            lowSize +
                                    highSize
                            ) / 2f

                dayPaint.textSize =
                    testSize

                dayPaint.letterSpacing =
                    DEFAULT_DAY_LETTER_SPACING /
                            testSize

                val measuredWidth =
                    dayPaint.measureText(
                        dayText
                    )

                if (
                    measuredWidth <
                    referenceTargetWidth
                ) {

                    lowSize =
                        testSize

                } else {

                    highSize =
                        testSize
                }
            }

            val baseDaySize =
                lowSize *
                        widthScale

            /*
             * =================================================
             * DATE / TIME BASE SIZE
             * =================================================
             */

            val baseSecondarySize =
                BASE_DATE_TIME_DP *
                        heightScale *
                        density

            /*
             * =================================================
             * MEASUREMENT FUNCTION
             * =================================================
             */

            fun calculateLayout(
                dayPercent: Int,
                datePercent: Int,
                timePercent: Int
            ): LayoutMeasurement {

                val measuredDayPaint =
                    Paint(
                        Paint.ANTI_ALIAS_FLAG or
                                Paint.SUBPIXEL_TEXT_FLAG
                    ).apply {

                        typeface =
                            dayTypeface

                        textAlign =
                            Paint.Align.CENTER

                        textSize =
                            baseDaySize *
                                    dayPercent /
                                    100f

                        val safeSpacing =
                            dayLetterSpacing.coerceIn(
                                0f,
                                100f
                            )

                        letterSpacing =
                            safeSpacing /
                                    textSize
                    }

                val measuredDatePaint =
                    Paint(
                        Paint.ANTI_ALIAS_FLAG or
                                Paint.SUBPIXEL_TEXT_FLAG
                    ).apply {

                        typeface =
                            secondaryTypeface

                        textAlign =
                            Paint.Align.CENTER

                        textSize =
                            baseSecondarySize *
                                    datePercent /
                                    100f

                        letterSpacing =
                            3f /
                                    19f
                    }

                val measuredTimePaint =
                    Paint(
                        Paint.ANTI_ALIAS_FLAG or
                                Paint.SUBPIXEL_TEXT_FLAG
                    ).apply {

                        typeface =
                            secondaryTypeface

                        textAlign =
                            Paint.Align.CENTER

                        textSize =
                            baseSecondarySize *
                                    timePercent /
                                    100f

                        letterSpacing =
                            3f /
                                    19f
                    }

                val dayBounds =
                    android.graphics.Rect()

                val dateBounds =
                    android.graphics.Rect()

                val timeBounds =
                    android.graphics.Rect()

                if (showDay) {

                    measuredDayPaint.getTextBounds(
                        dayText,
                        0,
                        dayText.length,
                        dayBounds
                    )
                }

                measuredDatePaint.getTextBounds(
                    dateText,
                    0,
                    dateText.length,
                    dateBounds
                )

                measuredTimePaint.getTextBounds(
                    timeText,
                    0,
                    timeText.length,
                    timeBounds
                )

                val visualSpacing =
                    BASE_SPACING_DP *
                            heightScale *
                            density

                val availableHeight =
                    safeHeightDp *
                            density *
                            0.96f

                val dayHeight =
                    if (showDay) {
                        dayBounds.height()
                    } else {
                        0
                    }

                val spacingCount =
                    if (showDay) {
                        2f
                    } else {
                        1f
                    }

                val totalHeight =
                    dayHeight +
                            dateBounds.height() +
                            timeBounds.height() +
                            visualSpacing *
                            spacingCount

                return LayoutMeasurement(
                    totalHeight =
                        totalHeight,
                    availableHeight =
                        availableHeight
                )
            }

            /*
             * =================================================
             * FIT CHECK
             * =================================================
             */

            fun fits(
                dayPercent: Int,
                datePercent: Int,
                timePercent: Int
            ): Boolean {

                val measurement =
                    calculateLayout(
                        dayPercent,
                        datePercent,
                        timePercent
                    )

                return measurement.totalHeight <=
                        measurement.availableHeight
            }

            /*
             * =================================================
             * ALREADY FITS
             * =================================================
             */

            if (
                fits(
                    dayResult,
                    dateResult,
                    timeResult
                )
            ) {

                return FontSizeValues(
                    day = dayResult,
                    date = dateResult,
                    time = timeResult
                )
            }

            /*
             * =================================================
             * PRIORITIZED FITTING
             * =================================================
             */

            if (
                changedElement != null
            ) {

                fun findMaximumFittingValue(
                    element: FontSizeElement
                ): Int {

                    var low =
                        MIN_SIZE_PERCENT

                    var high =
                        when (element) {

                            FontSizeElement.DAY ->
                                dayResult

                            FontSizeElement.DATE ->
                                dateResult

                            FontSizeElement.TIME ->
                                timeResult
                        }

                    var best =
                        MIN_SIZE_PERCENT

                    while (
                        low <= high
                    ) {

                        val middle =
                            (
                                    low +
                                            high
                                    ) / 2

                        val testDay =
                            when (element) {

                                FontSizeElement.DAY ->
                                    middle

                                else ->
                                    dayResult
                            }

                        val testDate =
                            when (element) {

                                FontSizeElement.DATE ->
                                    middle

                                else ->
                                    dateResult
                            }

                        val testTime =
                            when (element) {

                                FontSizeElement.TIME ->
                                    middle

                                else ->
                                    timeResult
                            }

                        if (
                            fits(
                                testDay,
                                testDate,
                                testTime
                            )
                        ) {

                            best =
                                middle

                            low =
                                middle + 1

                        } else {

                            high =
                                middle - 1
                        }
                    }

                    return best
                }

                when (
                    changedElement
                ) {

                    FontSizeElement.DAY -> {

                        dayResult =
                            findMaximumFittingValue(
                                FontSizeElement.DAY
                            )
                    }

                    FontSizeElement.DATE -> {

                        dateResult =
                            findMaximumFittingValue(
                                FontSizeElement.DATE
                            )
                    }

                    FontSizeElement.TIME -> {

                        timeResult =
                            findMaximumFittingValue(
                                FontSizeElement.TIME
                            )
                    }
                }

                if (
                    fits(
                        dayResult,
                        dateResult,
                        timeResult
                    )
                ) {

                    return FontSizeValues(
                        day = dayResult,
                        date = dateResult,
                        time = timeResult
                    )
                }
            }

            /*
             * =================================================
             * FALLBACK NORMALIZATION
             * =================================================
             */

            var scaleLow =
                0.5f

            var scaleHigh =
                1f

            var bestScale =
                0.5f

            repeat(20) {

                val scale =
                    (
                            scaleLow +
                                    scaleHigh
                            ) / 2f

                val testDay =
                    max(
                        MIN_SIZE_PERCENT,
                        (
                                dayResult *
                                        scale
                                ).roundToInt()
                    )

                val testDate =
                    max(
                        MIN_SIZE_PERCENT,
                        (
                                dateResult *
                                        scale
                                ).roundToInt()
                    )

                val testTime =
                    max(
                        MIN_SIZE_PERCENT,
                        (
                                timeResult *
                                        scale
                                ).roundToInt()
                    )

                if (
                    fits(
                        testDay,
                        testDate,
                        testTime
                    )
                ) {

                    bestScale =
                        scale

                    scaleLow =
                        scale

                } else {

                    scaleHigh =
                        scale
                }
            }

            dayResult =
                max(
                    MIN_SIZE_PERCENT,
                    (
                            dayResult *
                                    bestScale
                            ).roundToInt()
                )

            dateResult =
                max(
                    MIN_SIZE_PERCENT,
                    (
                            dateResult *
                                    bestScale
                            ).roundToInt()
                )

            timeResult =
                max(
                    MIN_SIZE_PERCENT,
                    (
                            timeResult *
                                    bestScale
                            ).roundToInt()
                )

            return FontSizeValues(
                day =
                    dayResult.coerceIn(
                        MIN_SIZE_PERCENT,
                        MAX_SIZE_PERCENT
                    ),
                date =
                    dateResult.coerceIn(
                        MIN_SIZE_PERCENT,
                        MAX_SIZE_PERCENT
                    ),
                time =
                    timeResult.coerceIn(
                        MIN_SIZE_PERCENT,
                        MAX_SIZE_PERCENT
                    )
            )
        }

        /*
         * =====================================================
         * LIVE SECOND UPDATES
         * =====================================================
         */

        private val handler =
            Handler(
                Looper.getMainLooper()
            )

        private var liveUpdateContext:
                WeakReference<Context>? =
            null

        private val liveUpdateRunnable =
            object : Runnable {

                override fun run() {

                    val context =
                        liveUpdateContext
                            ?.get()
                            ?: return

                    val timeFormat =
                        MainActivity.getGlobalTimeFormat(
                            context
                        )

                    if (
                        !timeFormat.contains(
                            "s"
                        )
                    ) {

                        return
                    }

                    updateAllWidgets(
                        context
                    )

                    val now =
                        System.currentTimeMillis()

                    val nextSecond =
                        (
                                now / 1_000L +
                                        1L
                                ) *
                                1_000L

                    val delay =
                        max(
                            1L,
                            nextSecond -
                                    now
                        )

                    handler.postDelayed(
                        this,
                        delay
                    )
                }
            }

        /*
         * =====================================================
         * COLOR HELPERS
         * =====================================================
         */

        private fun getDayColor(
            context: Context
        ): Int {

            return parseColor(
                MainActivity.getGlobalDayColor(
                    context
                )
            )
        }

        private fun getDateColor(
            context: Context
        ): Int {

            return parseColor(
                MainActivity.getGlobalDateColor(
                    context
                )
            )
        }

        private fun getTimeColor(
            context: Context
        ): Int {

            return parseColor(
                MainActivity.getGlobalTimeColor(
                    context
                )
            )
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

        /*
         * =====================================================
         * UPDATE ALL WIDGETS
         * =====================================================
         */

        fun updateAllWidgets(
            context: Context,
            changedElement: FontSizeElement? = null
        ) {

            val appWidgetManager =
                AppWidgetManager.getInstance(
                    context
                )

            val componentName =
                ComponentName(
                    context,
                    ModernClockWidget::class.java
                )

            val appWidgetIds =
                appWidgetManager
                    .getAppWidgetIds(
                        componentName
                    )

            for (
            appWidgetId in appWidgetIds
            ) {

                updateWidget(
                    context,
                    appWidgetManager,
                    appWidgetId,
                    changedElement
                )
            }
        }

        /*
         * =====================================================
         * UPDATE SINGLE WIDGET
         * =====================================================
         */

        private fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            changedElement: FontSizeElement? = null
        ) {

            val options =
                appWidgetManager
                    .getAppWidgetOptions(
                        appWidgetId
                    )

            val widthDp =
                options.getInt(
                    AppWidgetManager
                        .OPTION_APPWIDGET_MIN_WIDTH,
                    REFERENCE_WIDTH_DP.toInt()
                )

            val heightDp =
                options.getInt(
                    AppWidgetManager
                        .OPTION_APPWIDGET_MIN_HEIGHT,
                    REFERENCE_HEIGHT_DP.toInt()
                )

            val timeFormat =
                MainActivity.getGlobalTimeFormat(
                    context
                )

            val dateFormat =
                MainActivity.getGlobalDateFormat(
                    context
                )

            val showDay =
                MainActivity.getGlobalShowDay(
                    context
                )

            val dayLetterSpacing =
                MainActivity.getGlobalDayLetterSpacing(
                    context
                )

            val requestedDaySize =
                MainActivity.getGlobalDayFontSize(
                    context
                )

            val requestedDateSize =
                MainActivity.getGlobalDateFontSize(
                    context
                )

            val requestedTimeSize =
                MainActivity.getGlobalTimeFontSize(
                    context
                )

            val fittedSizes =
                calculateFittedFontSizes(
                    context = context,
                    widthDp = widthDp,
                    heightDp = heightDp,
                    timeFormat = timeFormat,
                    dateFormat = dateFormat,
                    showDay = showDay,
                    dayLetterSpacing = dayLetterSpacing,
                    requestedDaySizePercent = requestedDaySize,
                    requestedDateSizePercent = requestedDateSize,
                    requestedTimeSizePercent = requestedTimeSize,
                    changedElement = changedElement
                )

            val bitmap =
                createClockBitmap(
                    context = context,
                    widthDp = widthDp,
                    heightDp = heightDp,
                    timeFormat = timeFormat,
                    dateFormat = dateFormat,
                    showDay = showDay,
                    dayColor =
                        getDayColor(
                            context
                        ),
                    dateColor =
                        getDateColor(
                            context
                        ),
                    timeColor =
                        getTimeColor(
                            context
                        ),
                    dayLetterSpacing =
                        dayLetterSpacing,
                    daySizePercent =
                        fittedSizes.day,
                    dateSizePercent =
                        fittedSizes.date,
                    timeSizePercent =
                        fittedSizes.time
                )

            val views =
                RemoteViews(
                    context.packageName,
                    R.layout.widget_modern_clock
                )

            views.setImageViewBitmap(
                R.id.widget_clock,
                bitmap
            )

            val launchIntent =
                Intent(
                    context,
                    MainActivity::class.java
                )

            val launchPendingIntent =
                PendingIntent.getActivity(
                    context,
                    appWidgetId,
                    launchIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or
                            PendingIntent.FLAG_IMMUTABLE
                )

            views.setOnClickPendingIntent(
                R.id.widget_clock,
                launchPendingIntent
            )

            appWidgetManager.updateAppWidget(
                appWidgetId,
                views
            )
        }

        /*
         * =====================================================
         * PREVIEW BITMAP
         * =====================================================
         */

        fun createPreviewBitmap(
            context: Context,
            width: Int,
            height: Int,
            timeFormat: String,
            dateFormat: String,
            showDay: Boolean
        ): Bitmap {

            val density =
                context.resources
                    .displayMetrics
                    .density

            val widthDp =
                max(
                    1,
                    (width / density).toInt()
                )

            val heightDp =
                max(
                    1,
                    (height / density).toInt()
                )

            return createClockBitmap(
                context = context,
                widthDp = widthDp,
                heightDp = heightDp,
                timeFormat = timeFormat,
                dateFormat = dateFormat,
                showDay = showDay,
                dayColor =
                    getDayColor(
                        context
                    ),
                dateColor =
                    getDateColor(
                        context
                    ),
                timeColor =
                    getTimeColor(
                        context
                    ),
                dayLetterSpacing =
                    MainActivity.getGlobalDayLetterSpacing(
                        context
                    ),
                daySizePercent =
                    MainActivity.getGlobalDayFontSize(
                        context
                    ),
                dateSizePercent =
                    MainActivity.getGlobalDateFontSize(
                        context
                    ),
                timeSizePercent =
                    MainActivity.getGlobalTimeFontSize(
                        context
                    )
            )
        }

        /*
         * =====================================================
         * CLOCK BITMAP
         * ===================================================== */

        private fun createClockBitmap(
            context: Context,
            widthDp: Int,
            heightDp: Int,
            timeFormat: String,
            dateFormat: String,
            showDay: Boolean,
            dayColor: Int,
            dateColor: Int,
            timeColor: Int,
            dayLetterSpacing: Float,
            daySizePercent: Int,
            dateSizePercent: Int,
            timeSizePercent: Int
        ): Bitmap {

            val density =
                context.resources
                    .displayMetrics
                    .density

            val widthPx =
                max(
                    1,
                    (widthDp * density).toInt()
                )

            val heightPx =
                max(
                    1,
                    (heightDp * density).toInt()
                )

            val now =
                Date()

            val widgetLocale =
                getWidgetLocale()

            val dayText =
                SimpleDateFormat(
                    "EEEE",
                    widgetLocale
                )
                    .format(now)
                    .uppercase(
                        widgetLocale
                    )

            val dateText =
                SimpleDateFormat(
                    dateFormat,
                    widgetLocale
                )
                    .format(now)
                    .uppercase(
                        widgetLocale
                    )

            val timeText =
                SimpleDateFormat(
                    timeFormat,
                    Locale.getDefault()
                )
                    .format(now)

            val dayTypeface =
                ResourcesCompat.getFont(
                    context,
                    R.font.anurati
                ) ?: Typeface.DEFAULT

            val secondaryTypeface =
                ResourcesCompat.getFont(
                    context,
                    R.font.poppins
                ) ?: Typeface.DEFAULT

            val heightRatio =
                max(
                    1f,
                    heightDp /
                            REFERENCE_HEIGHT_DP
                )

            val heightScale =
                sqrt(
                    heightRatio
                )

            val dayPaint =
                Paint(
                    Paint.ANTI_ALIAS_FLAG or
                            Paint.SUBPIXEL_TEXT_FLAG
                ).apply {

                    typeface =
                        dayTypeface

                    color =
                        dayColor

                    textAlign =
                        Paint.Align.CENTER
                }

            val referenceWidthPx =
                REFERENCE_WIDTH_DP *
                        density

            val referenceTargetWidth =
                referenceWidthPx *
                        DAY_WIDTH_RATIO

            var lowSize =
                1f

            var highSize =
                300f *
                        density

            repeat(24) {

                val testSize =
                    (
                            lowSize +
                                    highSize
                            ) / 2f

                dayPaint.textSize =
                    testSize

                dayPaint.letterSpacing =
                    DEFAULT_DAY_LETTER_SPACING /
                            testSize

                val measuredWidth =
                    dayPaint.measureText(
                        dayText
                    )

                if (
                    measuredWidth <
                    referenceTargetWidth
                ) {

                    lowSize =
                        testSize

                } else {

                    highSize =
                        testSize
                }
            }

            val widthScale =
                widthDp /
                        REFERENCE_WIDTH_DP

            dayPaint.textSize =
                lowSize *
                        widthScale

            val safeDaySizePercent =
                daySizePercent.coerceIn(
                    MIN_SIZE_PERCENT,
                    MAX_SIZE_PERCENT
                )

            dayPaint.textSize *=
                safeDaySizePercent /
                        100f

            val safeLetterSpacing =
                dayLetterSpacing.coerceIn(
                    0f,
                    100f
                )

            dayPaint.letterSpacing =
                safeLetterSpacing /
                        dayPaint.textSize

            val secondarySize =
                BASE_DATE_TIME_DP *
                        heightScale *
                        density

            val datePaint =
                Paint(
                    Paint.ANTI_ALIAS_FLAG or
                            Paint.SUBPIXEL_TEXT_FLAG
                ).apply {

                    typeface =
                        secondaryTypeface

                    textSize =
                        secondarySize

                    color =
                        dateColor

                    textAlign =
                        Paint.Align.CENTER

                    letterSpacing =
                        3f /
                                19f
                }

            val timePaint =
                Paint(
                    Paint.ANTI_ALIAS_FLAG or
                            Paint.SUBPIXEL_TEXT_FLAG
                ).apply {

                    typeface =
                        secondaryTypeface

                    textSize =
                        secondarySize

                    color =
                        timeColor

                    textAlign =
                        Paint.Align.CENTER

                    letterSpacing =
                        3f /
                                19f
                }

            val safeDateSizePercent =
                dateSizePercent.coerceIn(
                    MIN_SIZE_PERCENT,
                    MAX_SIZE_PERCENT
                )

            val safeTimeSizePercent =
                timeSizePercent.coerceIn(
                    MIN_SIZE_PERCENT,
                    MAX_SIZE_PERCENT
                )

            datePaint.textSize *=
                safeDateSizePercent /
                        100f

            timePaint.textSize *=
                safeTimeSizePercent /
                        100f

            /*
             * =================================================
             * SHADOW CONFIGURATION
             * =================================================
             */

            val dayShadow =
                getDayShadowConfig(
                    context
                )

            val dateShadow =
                getDateShadowConfig(
                    context
                )

            val timeShadow =
                getTimeShadowConfig(
                    context
                )

            applyShadow(
                paint = dayPaint,
                config = dayShadow,
                density = density
            )

            applyShadow(
                paint = datePaint,
                config = dateShadow,
                density = density
            )

            applyShadow(
                paint = timePaint,
                config = timeShadow,
                density = density
            )

            val dayBounds =
                android.graphics.Rect()

            val dateBounds =
                android.graphics.Rect()

            val timeBounds =
                android.graphics.Rect()

            if (showDay) {

                dayPaint.getTextBounds(
                    dayText,
                    0,
                    dayText.length,
                    dayBounds
                )
            }

            datePaint.getTextBounds(
                dateText,
                0,
                dateText.length,
                dateBounds
            )

            timePaint.getTextBounds(
                timeText,
                0,
                timeText.length,
                timeBounds
            )

            val visualSpacing =
                BASE_SPACING_DP *
                        heightScale *
                        density

            val totalHeight =
                (
                        if (showDay) {
                            dayBounds.height()
                        } else {
                            0
                        }
                        ) +
                        dateBounds.height() +
                        timeBounds.height() +
                        visualSpacing *
                        (
                                if (showDay) {
                                    2f
                                } else {
                                    1f
                                }
                                )

            val bitmap =
                createBitmap(
                    widthPx,
                    heightPx,
                    Bitmap.Config.ARGB_8888
                )

            val canvas =
                Canvas(bitmap)

            canvas.drawColor(
                Color.TRANSPARENT,
                android.graphics.PorterDuff.Mode.CLEAR
            )

            val centerX =
                widthPx / 2f

            var currentTop =
                (
                        heightPx -
                                totalHeight
                        ) / 2f

            if (showDay) {

                val baseline =
                    currentTop -
                            dayBounds.top

                canvas.drawText(
                    dayText,
                    centerX,
                    baseline,
                    dayPaint
                )

                currentTop +=
                    dayBounds.height() +
                            visualSpacing
            }

            val dateBaseline =
                currentTop -
                        dateBounds.top

            canvas.drawText(
                dateText,
                centerX,
                dateBaseline,
                datePaint
            )

            currentTop +=
                dateBounds.height() +
                        visualSpacing

            val timeBaseline =
                currentTop -
                        timeBounds.top

            canvas.drawText(
                timeText,
                centerX,
                timeBaseline,
                timePaint
            )

            return bitmap
        }

        /*
         * =====================================================
         * LIVE SECOND UPDATES
         * =====================================================
         */

        fun startLiveUpdates(
            context: Context
        ) {

            val appWidgetManager =
                AppWidgetManager.getInstance(
                    context
                )

            val componentName =
                ComponentName(
                    context,
                    ModernClockWidget::class.java
                )

            val appWidgetIds =
                appWidgetManager
                    .getAppWidgetIds(
                        componentName
                    )

            if (
                appWidgetIds.isEmpty()
            ) {

                stopLiveUpdates()
                return
            }

            val timeFormat =
                MainActivity.getGlobalTimeFormat(
                    context
                )

            if (
                !timeFormat.contains(
                    "s"
                )
            ) {

                stopLiveUpdates()
                return
            }

            liveUpdateContext =
                WeakReference(
                    context.applicationContext
                )

            handler.removeCallbacks(
                liveUpdateRunnable
            )

            handler.post(
                liveUpdateRunnable
            )
        }

        fun stopLiveUpdates() {

            handler.removeCallbacks(
                liveUpdateRunnable
            )

            liveUpdateContext =
                null
        }

        /*
         * =====================================================
         * NEXT MINUTE UPDATE
         * =====================================================
         */

        fun scheduleNextUpdate(
            context: Context
        ) {

            val timeFormat =
                MainActivity.getGlobalTimeFormat(
                    context
                )

            if (
                timeFormat.contains(
                    "s"
                )
            ) {

                startLiveUpdates(
                    context
                )

                return
            }

            val alarmManager =
                context.getSystemService(
                    Context.ALARM_SERVICE
                ) as AlarmManager

            val intent =
                Intent(
                    context,
                    ClockTickReceiver::class.java
                ).apply {

                    action =
                        ACTION_UPDATE
                }

            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    1001,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or
                            PendingIntent.FLAG_IMMUTABLE
                )

            val currentTime =
                System.currentTimeMillis()

            val nextMinute =
                (
                        currentTime / 60_000L +
                                1
                        ) *
                        60_000L

            val elapsedRealtime =
                SystemClock.elapsedRealtime()

            val delay =
                nextMinute -
                        currentTime

            val triggerAt =
                elapsedRealtime +
                        delay

            if (
                alarmManager
                    .canScheduleExactAlarms()
            ) {

                alarmManager
                    .setExactAndAllowWhileIdle(
                        AlarmManager
                            .ELAPSED_REALTIME_WAKEUP,
                        triggerAt,
                        pendingIntent
                    )

            } else {

                alarmManager
                    .setAndAllowWhileIdle(
                        AlarmManager
                            .ELAPSED_REALTIME_WAKEUP,
                        triggerAt,
                        pendingIntent
                    )
            }
        }
    }

    /*
     * =========================================================
     * WIDGET LIFECYCLE
     * =========================================================
     */

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        for (
        appWidgetId in appWidgetIds
        ) {

            updateWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }

        scheduleNextUpdate(
            context
        )
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: android.os.Bundle
    ) {

        updateWidget(
            context,
            appWidgetManager,
            appWidgetId
        )
    }

    override fun onEnabled(
        context: Context
    ) {

        super.onEnabled(
            context
        )

        updateAllWidgets(
            context
        )

        scheduleNextUpdate(
            context
        )
    }

    override fun onDisabled(
        context: Context
    ) {

        super.onDisabled(
            context
        )

        stopLiveUpdates()

        val alarmManager =
            context.getSystemService(
                Context.ALARM_SERVICE
            ) as AlarmManager

        val intent =
            Intent(
                context,
                ClockTickReceiver::class.java
            ).apply {

                action =
                    ACTION_UPDATE
            }

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                1001,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        alarmManager.cancel(
            pendingIntent
        )
    }
}