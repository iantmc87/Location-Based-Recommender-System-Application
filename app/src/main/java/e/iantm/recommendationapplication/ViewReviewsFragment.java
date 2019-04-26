package e.iantm.recommendationapplication;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import org.json.JSONObject;;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/************************************************************
 Author - Ian McManus
 Version - 1.0.0
 Date - 30/04/2019
 Description - Fragment for the viewing all users own reviews

 ************************************************************/

public class ViewReviewsFragment extends Fragment {

    String reviews, userName, deleteReview;
    RequestQueue requestQueue;
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;
    View view;
    Resources res;
    ListView listView;
    FloatingActionButton addReview;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_reviews, null);
        listView = (ListView) view.findViewById(R.id.list_view);
        requestQueue = Volley.newRequestQueue(getContext());
        SharedPreferences prefs = getActivity().getSharedPreferences("account", MODE_PRIVATE);
        userName = prefs.getString("user", null);
        Toast.makeText(getContext(), userName, Toast.LENGTH_SHORT).show();

        res = getResources();

        reviews = String.format(res.getString(R.string.viewReviews), res.getString(R.string.url));
        deleteReview = String.format(res.getString(R.string.deleteReviews), res.getString(R.string.url));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, reviews, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray recommendations = jsonObject.getJSONArray("reviews");
                    int length = recommendations.length();
                    HashMap<String, String> item;
                    for(int i = 0; i < length; i++) {
                        JSONObject obj = recommendations.getJSONObject(i);
                        item = new HashMap<String, String>();
                        //item.put("id", obj.getString("id"));
                        item.put("name", obj.getString("name"));
                        item.put("date", obj.getString("date"));
                        item.put("rating", obj.getString("stars"));
                        item.put("review", obj.getString("text"));
                        list.add(item);
                    }
                    adapter = new SimpleAdapter(getContext(), list, R.layout.viewreviewslistview,
                            new String[] {"name", "date", "rating", "review"}, new int []{R.id.name, R.id.date, R.id.ratingBar, R.id.usertext});
                    adapter.setViewBinder(new MyBinder());
                    listView.setAdapter(adapter);
                    Toast.makeText(getContext(), "entered", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("user_name", userName);

                return parameters;
            }
        };
        requestQueue.add(stringRequest);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView business = (TextView)view.findViewById(R.id.name);
                final String businessText = business.getText().toString();
                Toast.makeText(getContext(), businessText, Toast.LENGTH_SHORT).show();

                StringRequest request = new StringRequest(Request.Method.POST, deleteReview, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();

                        parameters.put("user_name", userName);
                        parameters.put("business", businessText);
                        return parameters;
                    }
                };

                requestQueue.add(request);
            }
        });

        return view;
    }

    class MyBinder implements SimpleAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            if(view.getId() == R.id.ratingBar){
                String stringval = (String) data;
                float ratingValue = Float.parseFloat(stringval);
                RatingBar ratingBar = (RatingBar) view;
                ratingBar.setRating(ratingValue);
                return true;
            }
            return false;
        }
    }
}