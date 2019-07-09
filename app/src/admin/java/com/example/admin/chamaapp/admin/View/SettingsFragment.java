package com.example.admin.chamaapp.admin.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;


import com.example.admin.chamaapp.R;

import androidx.preference.PreferenceFragmentCompat;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
      addPreferencesFromResource(R.xml.settings_pref);
    }
}
