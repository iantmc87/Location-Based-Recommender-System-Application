package e.iantm.recommendationapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class AddReviewFragment extends Fragment {

    View view;
    EditText reviewText;
    Button save;
    Resources res;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    String getReviews;
    String userName, title;
    RatingBar ratingBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_add_review, null);
        SharedPreferences pref = getContext().getSharedPreferences("account", MODE_PRIVATE);
        userName = pref.getString("username", null);
        reviewText = (EditText)view.findViewById(R.id.editText);
        ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
        save = (Button)view.findViewById(R.id.button);
        requestQueue = Volley.newRequestQueue(getContext());
        res = getResources();
        getReviews = String.format(res.getString(R.string.newReview), res.getString(R.string.url));
        Bundle bundle1 = getArguments();
        if(bundle1 != null) {
            //userName = String.valueOf(bundle1.get("userName"));
            title = String.valueOf(bundle1.get("title"));
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getText = reviewText.getText().toString();

                final double stars = ratingBar.getRating();
                final String starsText = String.valueOf(stars);
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                final String todayDate = df.format(date);
                Toast.makeText(getContext(), todayDate, Toast.LENGTH_SHORT).show();

                stringRequest = new StringRequest(Request.Method.POST, getReviews, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("viewInfo", title);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("text", getText);
                        parameters.put("rating", starsText);
                        parameters.put("user_name", userName);
                        parameters.put("title", title);
                        parameters.put("date", todayDate);
                        return parameters;
                    }

                };
                requestQueue.add(stringRequest);
            }
        });

        return view;
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.child_fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}