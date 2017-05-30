package com.app.android.upbook

import android.app.Application
import butterknife.ButterKnife

/**
 * Created by bzumpano on 29/05/17.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ButterKnife.setDebug(BuildConfig.DEBUG)
    }
}