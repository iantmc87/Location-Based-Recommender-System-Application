package e.iantm.recommendationapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragmentCompat {

    ListPreference systemPreference;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);

        systemPreference = (ListPreference)findPreference("system");
        systemPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                String textValue = o.toString();
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(textValue);

                CharSequence currText = systemPreference.getEntry();
                preference.setSummary(currText);
                /*if(preference instanceof ListPreference) {
                    ListPreference listPref = (ListPreference) systemPreference;
                    listPref.setSummary(listPref.getEntry());
                }*/
                /*String stringValue = o.toString();
                /*String test = String.valueOf(systemPreference.getSharedPreferences());
                Toast.makeText(getContext(), stringValue, Toast.LENGTH_SHORT).show();
                preference.setSummary(stringValue);*/
                return true;
            }
        });
    }






    /*@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.xml.settings, null);

        Preference preference = (Preference)findPreference(R.id.)
        return view;
    }*/
}
