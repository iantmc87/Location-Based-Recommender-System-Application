package e.iantm.recommendationapplication;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    String places, reviews;
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    View view;
    String userName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_home, null);
        Bundle bundle = getArguments();
        if(bundle != null) {
            userName = String.valueOf(bundle.get("userName"));
        }
        //Toast.makeText(getContext(), userName, Toast.LENGTH_SHORT).show();

        loadFragment(new MapFragment(), userName );

        loadFragment1(new WaitingScreenFragment(), userName);

        final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadFragment1(new RecommendedListView(), userName);
                                }
                            }, 10000);


        return view;
    }

    private boolean loadFragment(Fragment fragment, String userName) {
        //switching fragment
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        fragment.setArguments(bundle);
        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.child_fragment_container_map, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private boolean loadFragment1(Fragment fragment, String userName) {
        //switching fragment
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        fragment.setArguments(bundle);
        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.child_fragment_container_list, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}

