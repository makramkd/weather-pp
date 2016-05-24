package me.makram.weatherpp.app.activities;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;

import me.makram.weatherpp.app.PreferenceConstants;
import me.makram.weatherpp.app.R;

/**
 * Created by admin on 5/24/16.
 */
public class SinglePageSettingsActivity extends AppCompatPreferenceActivity {

    private static final String TAG = SinglePageSettingsActivity.class.getName();

    private static Preference.OnPreferenceChangeListener listener =
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (newValue == null) {
                        return false;
                    }

                    String stringValue = newValue.toString();

                    if (preference instanceof ListPreference) {
                        ListPreference listPreference = (ListPreference) preference;
                        int index = listPreference.findIndexOfValue(stringValue);

                        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] :
                        null);

                        // TODO : notify the adapter that the setting has changed so that
                        // we can update the UI
                    } else if (preference instanceof EditTextPreference) {
                        EditTextPreference editTextPreference = (EditTextPreference) preference;
                        // get the number that the user gave, and make sure
                        // its between 7 and 16
                        Integer integer = Integer.parseInt(editTextPreference.getText());
                        if (integer < 7 || integer > 16) {
                            // show error to user and keep preference like it is
                            Log.e(TAG, "Forecast day should be a number between 7 and 16");
                        } else {
                            preference.setSummary(editTextPreference.getText());
                        }
                    }

                    return true;
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up the preferences
        addPreferencesFromResource(R.xml.preferences_all);

        // set up the action bar
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        bindPreferenceSummaryToValue(findPreference(PreferenceConstants.PREF_TEMPERATURE_DISPLAY_TYPE));
        bindPreferenceSummaryToValue(findPreference(PreferenceConstants.PREF_DAYS_IN_FORECAST));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(listener);

        // Trigger the listener immediately with the preference's
        // current value.
        listener.onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext())
        .getString(preference.getKey(), null));
    }
}
