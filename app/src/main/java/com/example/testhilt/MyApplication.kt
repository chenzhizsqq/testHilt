package com.example.testhilt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 必须在app的Application中加入@HiltAndroidApp
 * 这样才能够让APP知道现在是调用Hilt
 */
@HiltAndroidApp
class MyApplication : Application()