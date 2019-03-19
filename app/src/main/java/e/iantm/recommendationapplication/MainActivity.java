package e.iantm.recommendationapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.omadahealth.lollipin.lib.managers.AppLock;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final int REQUEST_CODE_ENABLE = 11;
    String userName = null;
    String viewInfo = null;
    String firstLoad = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = getMailAddress();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        viewInfo = intent.getStringExtra("viewInfo");
        firstLoad = intent.getStringExtra("settings");

        if (firstLoad != null) {
            navigation.setSelectedItemId(R.id.navigation_settings);
            loadFragment1(new SettingsFragment(), userName, null);
        } else {

            if (viewInfo != null) {
                navigation.setSelectedItemId(R.id.navigation_reviews);
                loadFragment1(new ReviewFragment(), userName, viewInfo);
                setTitle("Reviews");

            } else {
                loadFragment1(new HomeFragment(), userName, null);
                setTitle("Recommendations");
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                setTitle("Recommendations");
                break;

            case R.id.navigation_reviews:
                fragment = new ReviewFragment();
                setTitle("Reviews");
                break;

            case R.id.navigation_settings:
                fragment = new SettingsFragment();
                setTitle("Settings");
                break;

            case R.id.navigation_help:
                fragment = new HelpFragment();
                setTitle("Help");
                break;
        }
        return loadFragment1(fragment, userName,null);
    }

    private boolean loadFragment(Fragment fragment, String userName) {
        //switching fragment
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        fragment.setArguments(bundle);
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

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
    }

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

    public String getMailAddress(){


        AccountManager am = AccountManager.get(this); // "this" references the current Context
        String acName = null;
        int end = 0;
        Account[] accounts = am.getAccounts();
        for (Account ac : accounts) {
            end = ac.name.indexOf("@");
            if(end != -1){
                acName = ac.name.substring(0, end);
            }  else {
                acName = ac.name;
            }
        }
        userName = acName;
        return userName;
    }
}
