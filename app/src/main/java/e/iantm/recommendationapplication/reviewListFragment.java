package e.iantm.recommendationapplication;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class reviewListFragment extends Fragment {

    String reviews, title;
    RequestQueue requestQueue;
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;
    View view;
    Resources res;
    ListView listView;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_review_list, null);
        listView = (ListView) view.findViewById(R.id.review_list);
        requestQueue = Volley.newRequestQueue(getContext());

        /*Bundle bundle = getArguments();
        if(bundle != null) {
            title = bundle.get("title").toString();
        } else {
            title = "error";
        }*/
        res = getResources();
        //String param = (res.getString(R.string.url) + "?param=Harlow");

        reviews = String.format(res.getString(R.string.reviews), res.getString(R.string.url));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, reviews, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray recommendations = response.getJSONArray("reviews");
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

                    /*if(title != null) {
                        TextView textView = new TextView(getContext());
                        textView.setText(title);

                        listView.addHeaderView(textView);
                    }*/

                    adapter = new SimpleAdapter(getContext(), list, R.layout.reviewslistview,
                            new String[] {"name", "date"/*, "stars"*/, "review"}, new int []{R.id.name, R.id.date/*, R.id.name*/, R.id.usertext});

                    listView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);

        return view;
    }
}
