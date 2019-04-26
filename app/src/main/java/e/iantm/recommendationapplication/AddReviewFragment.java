package e.iantm.recommendationapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/************************************************************
    Author - Ian McManus
    Version - 1.0.0
    Date - 30/04/2019
    Description - Fragment for adding a review to the database

 ************************************************************/
public class AddReviewFragment extends Fragment {

    View view;
    EditText reviewText, name;
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
        name = (EditText)view.findViewById(R.id.editText2);
        userName = pref.getString("user", null);
        reviewText = (EditText)view.findViewById(R.id.editText);
        ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
        save = (Button)view.findViewById(R.id.button);
        requestQueue = Volley.newRequestQueue(getContext());
        res = getResources();
        getReviews = String.format(res.getString(R.string.newReview), res.getString(R.string.url));
        Bundle bundle1 = getArguments();
        if(bundle1 != null) {
            title = String.valueOf(bundle1.get("title"));
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String getText = reviewText.getText().toString();
                final String getName = name.getText().toString();

                final double stars = ratingBar.getRating();
                final String starsText = String.valueOf(stars);
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                final String todayDate = df.format(date);
                Toast.makeText(getContext(), getText, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), starsText, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), userName, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), getName, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), title, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("text", getText);
                        parameters.put("rating", starsText);
                        parameters.put("user_name", userName);
                        parameters.put("name", getName);
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

}