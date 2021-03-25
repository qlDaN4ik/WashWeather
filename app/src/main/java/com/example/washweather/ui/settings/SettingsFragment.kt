package com.example.washweather.ui.settings

import android.os.Bundle
import android.content.SharedPreferences
import androidx.preference.*
import com.example.washweather.R


class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    private lateinit var preferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_limit_key)))
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_min_temp_key)))
    }

    private fun bindPreferenceSummaryToValue(preference: Preference?) {
        if (preference != null) {
            preference.onPreferenceChangeListener = this
        }

        if (preference is ListPreference) {
            onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.context)
                            .getString(preference.key, ""))
        }
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        val stringValue = newValue.toString()

        if(preference != null){
            if (preference is ListPreference) {
                val prefIndex = preference.findIndexOfValue(stringValue)
                if (prefIndex >= 0) {
                    preference.setSummary(preference.entries[prefIndex])
                }
            }
        }
        return true
    }
}
