package e.iantm.recommendationapplication;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeInstructionsFragment extends Fragment {

    ImageView exit, business, stars, distance, map, location;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home_instructions, null);

        exit = (ImageView) view.findViewById(R.id.imageView5);
        business = (ImageView) view.findViewById(R.id.imageView6);
        stars = (ImageView) view.findViewById(R.id.imageView7);
        distance = (ImageView) view.findViewById(R.id.imageView8);
        map = (ImageView) view.findViewById(R.id.imageView9);
        location = (ImageView) view.findViewById(R.id.imageView10);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //view.setBackgroundColor(0x3E7);
                exit.setVisibility(View.VISIBLE);
                business.setVisibility(View.VISIBLE);
                stars.setVisibility(View.VISIBLE);
                distance.setVisibility(View.VISIBLE);
                map.setVisibility(View.VISIBLE);
                location.setVisibility(View.VISIBLE);
                }
        }, 10000);


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .remove(HomeInstructionsFragment.this).commit();
            }
        });

        return view;
    }


}