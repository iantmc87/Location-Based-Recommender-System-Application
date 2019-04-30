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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/************************************************************
 Author - Ian McManus
 Version - 1.0.0
 Date - 30/04/2019
 Description - Fragment for the recommendations on the home page

 ************************************************************/

public class RecommendedListView extends Fragment {

    String places, reviews, userName;

    SimpleAdapter adapter;
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    ListView listView;

    RequestQueue requestQueue;
    Resources res;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_recommended_list_view, null);

        listView = (ListView) view.findViewById(R.id.list);

        requestQueue = Volley.newRequestQueue(getContext());
        res = getResources();

        places = String.format(res.getString(R.string.recommendations), res.getString(R.string.url));

        SharedPreferences pref = getContext().getSharedPreferences("location", MODE_PRIVATE);
        final Double latitude = Double.parseDouble(pref.getString("latitude", null));
        final Double longitude = Double.parseDouble(pref.getString("longitude", null));

        Bundle bundle = getArguments();
        if(bundle != null) {
            userName = String.valueOf(bundle.get("userName"));
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, places, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray recommendations = jsonObject.getJSONArray("recommendations");
                    int length = recommendations.length();
                    HashMap<String, String> item;
                    for(int i = 0; i < length; i++) {
                        JSONObject obj = recommendations.getJSONObject(i);
                        item = new HashMap<String, String>();
                        item.put("title", obj.getString("name"));
                        item.put("summary", obj.getString("categories"));
                        DecimalFormat format = new DecimalFormat("0.00");
                        String distance = format.format(obj.getDouble("distance"));
                        item.put("distance", distance);
                        item.put("rating", obj.getString("rating"));
                        list.add(item);
                    }

                    adapter = new SimpleAdapter(getContext(), list, R.layout.recommendlistview,
                            new String[] {"title", "summary", "distance", "rating"}, new int []{R.id.title, R.id.summary, R.id.distanceFrom, R.id.ratingBar2});
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
                parameters.put("user_name", userName);
                parameters.put("latitude", latitude.toString());
                parameters.put("longitude", longitude.toString());

                return parameters;
            }
        };
        requestQueue.add(stringRequest);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView selectedItem = (TextView) view.findViewById(R.id.title);
                String selectedText = selectedItem.getText().toString();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("viewInfo", selectedText);
                startActivity(intent);
            }
        });

        return view;
    }//end onCreateView

    class MyBinder implements SimpleAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            if(view.getId() == R.id.ratingBar2){
                String stringval = (String) data;
                float ratingValue = Float.parseFloat(stringval);
                RatingBar ratingBar = (RatingBar) view;
                ratingBar.setRating(ratingValue);
                return true;
            }
            return false;
        }
    }//end myBinder adapter for populating rating bars
}//end Class