package e.iantm.recommendationapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

/************************************************************
 Author - Ian McManus
 Version - 1.0.0
 Date - 30/04/2019
 Description - Fragment for the main recommendations navigation page

 ************************************************************/

public class HomeFragment extends Fragment {

    String reviews;
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    SharedPreferences instructionsPref;

    View view;
    String userName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_home, null);

        instructionsPref = (getContext().getSharedPreferences("instructions", Context.MODE_PRIVATE));
        SharedPreferences.Editor editor = instructionsPref.edit();

        editor.putString("home", "false");
        editor.commit();
        Bundle bundle = getArguments();
        if(bundle != null) {
            userName = String.valueOf(bundle.get("userName"));
        }

        loadFragment(new MapFragment(), userName);


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

