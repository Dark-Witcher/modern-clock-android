package com.example.modernclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ClockTickReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        when (intent.action) {

            ModernClockWidget.ACTION_UPDATE,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_DATE_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED -> {

                ModernClockWidget.updateAllWidgets(
                    context
                )

                ModernClockWidget.scheduleNextUpdate(
                    context
                )
            }
        }
    }
}