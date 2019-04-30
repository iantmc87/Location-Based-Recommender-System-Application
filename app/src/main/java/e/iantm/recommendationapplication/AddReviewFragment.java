package e.iantm.recommendationapplication;

//import packages
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
    String getReviews, userName, title;
    RatingBar ratingBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_add_review, null);

        //gets username from shared preferences
        SharedPreferences pref = getContext().getSharedPreferences("account", MODE_PRIVATE);
        userName = pref.getString("user", null);

        name = (EditText)view.findViewById(R.id.editText2);
        reviewText = (EditText)view.findViewById(R.id.editText);
        ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
        save = (Button)view.findViewById(R.id.button);

        //creates volley request
        requestQueue = Volley.newRequestQueue(getContext());
        res = getResources();

        getReviews = String.format(res.getString(R.string.newReview), res.getString(R.string.url));

        //gets business name from previous fragment
        Bundle bundle1 = getArguments();
        if(bundle1 != null) {
            title = String.valueOf(bundle1.get("title"));
        }//end if statement for getting business name

        //on click listener for save button
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

                //request to send review data to server
                stringRequest = new StringRequest(Request.Method.POST, getReviews, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //sends user back to business info page
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
                        parameters.put("name", getName);
                        parameters.put("title", title);
                        parameters.put("date", todayDate);
                        return parameters;
                    }//end sending parameters to PHP method

                };//end method for sending review to the PHP script
                requestQueue.add(stringRequest);
            }
        });//end on click listener for save button

        return view;
    }//end onCreateView

}//end class