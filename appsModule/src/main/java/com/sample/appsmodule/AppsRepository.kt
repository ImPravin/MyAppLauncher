package com.sample.appsmodule

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class AppsRepository(private val context: Context) {

    suspend fun getInstalledAppList(): List<AppInfo> {
        val pm: PackageManager = context.packageManager
        val appsList = mutableListOf<AppInfo>()
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val allApps = pm.queryIntentActivities(i, 0)
        for (ri in allApps) {
            val app = AppInfo(
                ri.loadLabel(pm).toString(),
                ri.activityInfo.packageName,
                ri.activityInfo.loadIcon(pm)
            )
            appsList.add(app)
        }
        return appsList
    }
}