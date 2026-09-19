package com.example.modernclock

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class ModernClockApplication : Application() {

    private val configurationChangeReceiver =
        object : BroadcastReceiver() {

            override fun onReceive(
                context: Context,
                intent: Intent
            ) {

                if (
                    intent.action ==
                    Intent.ACTION_CONFIGURATION_CHANGED &&
                    MainActivity.getGlobalWidgetAppearance(
                        context
                    ) ==
                    "follow_system"
                ) {

                    ModernClockWidget.updateAllWidgets(
                        context.applicationContext
                    )
                }
            }
        }

    override fun onCreate() {
        super.onCreate()

        ContextCompat.registerReceiver(
            this,
            configurationChangeReceiver,
            android.content.IntentFilter(
                Intent.ACTION_CONFIGURATION_CHANGED
            ),
            ContextCompat.RECEIVER_EXPORTED
        )
    }
}