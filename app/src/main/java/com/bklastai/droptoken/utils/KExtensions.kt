package com.bklastai.droptoken.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.bklastai.droptoken.APP_LEVEL_SHARED_PREFS

fun Context.safeGetString(intKey: Int): String {
    return try { resources.getString(intKey) } catch (err: Resources.NotFoundException) { "" }
}

fun Context.getPrefs(): SharedPreferences {
    return getSharedPreferences(APP_LEVEL_SHARED_PREFS, Context.MODE_PRIVATE)
}

fun Context.getPrefsEditor(): SharedPreferences.Editor {
    return getPrefs().edit()
}


fun Context.containsPref(intKey: Int): Boolean { return getPrefs().contains(safeGetString(intKey)) }
fun Context.containsPref(key: String): Boolean { return getPrefs().contains(key) }

// remove pref
fun Context.removePref(intKey: Int) { return getPrefsEditor().remove(safeGetString(intKey)).apply() }
fun Context.removePref(key: String) { return getPrefsEditor().remove(key).apply() }

// get and put String
fun Context.getStringPref(intKey: Int): String? { return getStringPref(safeGetString(intKey)) }
fun Context.getStringPref(strKey: String): String? { return getPrefs().getString(strKey, null) }
fun Context.putStringPref(intKey: Int, value: String) { putStringPref(safeGetString(intKey), value) }
fun Context.putStringPref(strKey: String, value: String) { getPrefsEditor().putString(strKey, value).apply() }

// get and put Boolean
fun Context.getBooleanPref(intKey: Int): Boolean? { return getBooleanPref(safeGetString(intKey)) }
fun Context.getBooleanPref(strKey: String): Boolean? {
    return if (getPrefs().contains(strKey)) getPrefs().getBoolean(strKey, false) else null
}
fun Context.putBooleanPref(intKey: Int, value: Boolean) { putBooleanPref(safeGetString(intKey), value) }
fun Context.putBooleanPref(strKey: String, value: Boolean) { getPrefsEditor().putBoolean(strKey, value).apply() }