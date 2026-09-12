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
import kotlin.math.sqrt

class ModernClockWidget : AppWidgetProvider() {

    companion object {

        const val ACTION_UPDATE =
            "com.example.modernclock.UPDATE"

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
         * LIVE SECOND UPDATES
         * =====================================================
         *
         * Do NOT keep a strong Context reference in this
         * singleton companion object.
         *
         * A WeakReference prevents the static object from
         * retaining the Context.
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

                    /*
                     * Retrieve the actual Context from the
                     * WeakReference.
                     *
                     * If it has disappeared, simply stop.
                     */

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
                                now / 1_000L + 1L
                                ) * 1_000L

                    val delay =
                        max(
                            1L,
                            nextSecond - now
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

            for (
            appWidgetId in appWidgetIds
            ) {

                updateWidget(
                    context,
                    appWidgetManager,
                    appWidgetId
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
            appWidgetId: Int
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

            val bitmap =
                createClockBitmap(
                    context = context,
                    widthDp = widthDp,
                    heightDp = heightDp,
                    timeFormat =
                        MainActivity.getGlobalTimeFormat(
                            context
                        ),
                    dateFormat =
                        MainActivity.getGlobalDateFormat(
                            context
                        ),
                    showDay =
                        MainActivity.getGlobalShowDay(
                            context
                        ),
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
                        )
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

            /*
             * Tapping the widget opens the application.
             */

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
                context
                    .resources
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
                    )
            )
        }

        /*
         * =====================================================
         * CLOCK BITMAP
         * =====================================================
         */

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
            dayLetterSpacing: Float
        ): Bitmap {

            val density =
                context
                    .resources
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

            /*
             * =================================================
             * DATE / TIME TEXT
             * =================================================
             */

            val now =
                Date()

            val dayText =
                SimpleDateFormat(
                    "EEEE",
                    Locale.getDefault()
                )
                    .format(now)
                    .uppercase(
                        Locale.getDefault()
                    )

            val dateText =
                SimpleDateFormat(
                    dateFormat,
                    Locale.getDefault()
                )
                    .format(now)
                    .uppercase(
                        Locale.getDefault()
                    )

            val timeText =
                SimpleDateFormat(
                    timeFormat,
                    Locale.getDefault()
                )
                    .format(now)

            /*
             * =================================================
             * FONTS
             * =================================================
             */

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

            /*
             * =================================================
             * HEIGHT SCALE
             * =================================================
             *
             * DATE/TIME and vertical spacing scale with height.
             *
             * DAY does NOT.
             */

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

            /*
             * =================================================
             * DAY PAINT
             * =================================================
             */

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

            /*
             * =================================================
             * DAY BASE FONT SIZE
             * =================================================
             *
             * Calculate the original base DAY size at the
             * reference width.
             *
             * The original KDE spacing of 17 is used only for
             * establishing that base size.
             */

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

            /*
             * =================================================
             * SCALE DAY WITH WIDTH
             * =================================================
             *
             * This is independent from widget height.
             */

            val widthScale =
                widthDp /
                        REFERENCE_WIDTH_DP

            dayPaint.textSize =
                lowSize *
                        widthScale

            /*
             * =================================================
             * APPLY USER LETTER SPACING
             * =================================================
             *
             * IMPORTANT:
             *
             * Changing this value does NOT change textSize.
             */

            val safeLetterSpacing =
                dayLetterSpacing.coerceIn(
                    0f,
                    100f
                )

            dayPaint.letterSpacing =
                safeLetterSpacing /
                        dayPaint.textSize

            /*
             * No width-fitting/shrinking happens here.
             */

            /*
             * =================================================
             * DATE / TIME
             * =================================================
             */

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

            /*
             * =================================================
             * TEXT BOUNDS
             * =================================================
             */

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

            /*
             * =================================================
             * VERTICAL SPACING
             * =================================================
             */

            var visualSpacing =
                BASE_SPACING_DP *
                        heightScale *
                        density

            val availableHeight =
                heightPx *
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

            var totalHeight =
                dayHeight +
                        dateBounds.height() +
                        timeBounds.height() +
                        visualSpacing *
                        spacingCount

            /*
             * Reduce spacing first.
             */

            if (
                totalHeight >
                availableHeight
            ) {

                val availableForSpacing =
                    availableHeight -
                            dayHeight -
                            dateBounds.height() -
                            timeBounds.height()

                visualSpacing =
                    max(
                        1f * density,
                        availableForSpacing /
                                spacingCount
                    )

                totalHeight =
                    dayHeight +
                            dateBounds.height() +
                            timeBounds.height() +
                            visualSpacing *
                            spacingCount
            }

            /*
             * If the secondary elements still do not fit,
             * scale DATE/TIME only.
             *
             * DAY remains untouched.
             */

            if (
                totalHeight >
                availableHeight
            ) {

                val secondaryContentHeight =
                    dateBounds.height() +
                            timeBounds.height() +
                            visualSpacing *
                            spacingCount

                val availableSecondaryHeight =
                    availableHeight -
                            dayHeight

                val fitScale =
                    max(
                        0.5f,
                        availableSecondaryHeight /
                                secondaryContentHeight
                    )

                datePaint.textSize *=
                    fitScale

                timePaint.textSize *=
                    fitScale

                visualSpacing *=
                    fitScale

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

                totalHeight =
                    dayHeight +
                            dateBounds.height() +
                            timeBounds.height() +
                            visualSpacing *
                            spacingCount
            }

            /*
             * =================================================
             * BITMAP
             * =================================================
             */

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

            /*
             * =================================================
             * DAY
             * =================================================
             */

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

            /*
             * =================================================
             * DATE
             * =================================================
             */

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

            /*
             * =================================================
             * TIME
             * =================================================
             */

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

            /*
             * Store only a weak reference to the application
             * context.
             */

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
                        ) * 60_000L

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