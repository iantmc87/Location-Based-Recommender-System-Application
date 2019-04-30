package e.iantm.recommendationapplication;

import android.content.Context;
import android.content.SharedPreferences;

/************************************************************
 Author - Ian McManus
 Version - 1.0.0
 Date - 30/04/2019
 Description - Preference Manager for welcome screen pages

 ************************************************************/

public class WelcomeScreenPrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "welcomeFirstTimeLaunch";

    public WelcomeScreenPrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}//end class