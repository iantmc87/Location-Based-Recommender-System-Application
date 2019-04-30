package e.iantm.recommendationapplication;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;

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
import java.util.Map;

/************************************************************
 Author - Ian McManus
 Version - 1.0.0
 Date - 30/04/2019
 Description - Fragment for showing restaurant reviews

 ************************************************************/

public class ShowReviewsFragment extends Fragment {

    String reviews;
    String title;

    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;
    ListView listView;

    View view;

    RequestQueue requestQueue;
    Resources res;

    FloatingActionButton addReview;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_reviews, null);
        listView = (ListView) view.findViewById(R.id.list_view);
        requestQueue = Volley.newRequestQueue(getContext());
        Bundle bundle = getArguments();
        if(bundle != null) {
            title = bundle.getString("title");

        } else {

        }
        res = getResources();

        reviews = String.format(res.getString(R.string.reviews), res.getString(R.string.url));

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
                        item.put("name", obj.getString("name"));
                        item.put("date", obj.getString("date"));
                        item.put("rating", obj.getString("stars"));
                        item.put("review", obj.getString("text"));
                        list.add(item);
                    }
                    adapter = new SimpleAdapter(getContext(), list, R.layout.reviewslistview,
                            new String[] {"name", "date", "rating", "review"}, new int []{R.id.name, R.id.date, R.id.ratingBar, R.id.usertext});
                    adapter.setViewBinder(new MyBinder());
                    listView.setAdapter(adapter);


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
                parameters.put("business", String.valueOf(title));

                return parameters;
            }
        };
        requestQueue.add(stringRequest);

        return view;
    }//end onCreateView

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
    }//end binder for rating bar adapter
}//end class