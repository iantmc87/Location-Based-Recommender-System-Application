package e.iantm.recommendationapplication;

//imports packages
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

/************************************************************
 Author - Ian McManus
 Version - 1.0.0
 Date - 30/04/2019
 Description - Main Activity for the application

 ************************************************************/

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final int REQUEST_CODE_ENABLE = 11;
    String userName = null, viewInfo = null, firstLoad = null;
    SharedPreferences instructionsPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //gets username from sharedPreferences
        SharedPreferences preferences = getSharedPreferences("account", 0);
        userName = preferences.getString("user", null);

        //creates bottom navigation bar
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        viewInfo = intent.getStringExtra("viewInfo");
        firstLoad = intent.getStringExtra("settings");

        //if statement for checking if first load or not
        if (firstLoad != null) {
            instructionsPref = (this.getSharedPreferences("instructions", Context.MODE_PRIVATE));
            SharedPreferences.Editor editor = instructionsPref.edit();

            editor.putString("home", "true");
            editor.putString("review", "true");
            editor.putString("settings", "true");
            editor.commit();
            navigation.setSelectedItemId(R.id.navigation_settings);
            loadFragmentFirst(new SettingsFragment(), new SettingsInstructionsFragment(), userName, null);
        } else {

            if (viewInfo != null) {
                if(viewInfo.equals("viewReviews")) {
                    navigation.setSelectedItemId(R.id.navigation_settings);
                    loadFragment1(new ViewReviewsFragment(), userName, viewInfo);
                    setTitle("Settings");
                } else {
                    navigation.setSelectedItemId(R.id.navigation_reviews);
                    loadFragment1(new ReviewFragment(), userName, viewInfo);
                    setTitle("Reviews");
                }
            } else {
                loadFragment1(new HomeFragment(), userName, null);
                   setTitle("Recommendations");
            }
        }
    }//end onCreate


    /**
     *Method for function of nav bar clicked
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        Fragment fragment2 = null;
        SharedPreferences preferences = getSharedPreferences("instructions", 0);

        switch (item.getItemId()) {
            case R.id.navigation_home:
                String home = preferences.getString("home", null);
                if (home.equals("true")) {
                    fragment = new HomeFragment();
                    fragment2 = new HomeInstructionsFragment();
                    return loadFragmentFirst(fragment, fragment2, userName, null);
                } else {
                    fragment = new HomeFragment();
                }
                setTitle("Recommendations");
                break;

            case R.id.navigation_reviews:
                String review = preferences.getString("review", null);
                if (review.equals("true")) {
                    fragment = new ReviewFragment();
                    fragment2 = new ReviewsInstructionsFragment();
                    return loadFragmentFirst(fragment, fragment2, userName, null);
                } else {
                    fragment = new ReviewFragment();
                }
                setTitle("Reviews");
                break;

            case R.id.navigation_settings:
                String settings = preferences.getString("settings", null);
                if(settings.equals("true")) {
                    fragment = new SettingsFragment();
                    fragment2 = new SettingsInstructionsFragment();
                    return loadFragmentFirst(fragment, fragment2, userName, null);
                } else {
                    fragment = new SettingsFragment();
                }
                setTitle("Settings");
                break;

            case R.id.navigation_help:
                fragment = new HelpFragment();
                setTitle("Help");
                break;
        }
        return loadFragment1(fragment, userName,null);
    }//end navigation select method

    /**
     *loads fragments into container
     */
    private boolean loadFragment1(Fragment fragment, String userName, String viewInfo) {
        //switching fragment
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        bundle.putString("title", viewInfo);
        fragment.setArguments(bundle);
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }//end load fragment method for page containers

    /**
     * load fragment into container if first install
     */
    private boolean loadFragmentFirst(Fragment fragment, Fragment fragment1, String userName, String viewInfo) {
        //switching fragment
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        bundle.putString("title", viewInfo);
        fragment.setArguments(bundle);
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .replace(R.id.help_overview_container, fragment1)
                    .commit();
            return true;
        }
        return false;
    }//end load fragment method for initial install

    private void checkFirstRun() {
        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {

            // TODO This is a new install (or the user cleared the shared preferences)

        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();

    }
}//end class
