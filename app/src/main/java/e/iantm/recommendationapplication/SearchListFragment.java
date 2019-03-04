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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SearchListFragment extends Fragment {

    RequestQueue requestQueue;
    Resources res;
    ListView listView;
    String places, userName;
    SimpleAdapter adapter;
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_list, null);
        listView = (ListView)view.findViewById(R.id.searchList);
        requestQueue = Volley.newRequestQueue(getContext());
        res = getResources();
        places = String.format(res.getString(R.string.recommendations), res.getString(R.string.url));
        Bundle bundle = getArguments();
        SharedPreferences pref = getContext().getSharedPreferences("location", MODE_PRIVATE);
        final Double latitude = Double.parseDouble(pref.getString("latitude", null));
        final Double longitude = Double.parseDouble(pref.getString("longitude", null));
        if(bundle!=null){
            userName = String.valueOf(bundle.get("userName"));
        }


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, places, new Response.Listener<String>() {
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
                        list.add(item);
                    }

                    adapter = new SimpleAdapter(getContext(), list, R.layout.recommendlistview,
                            new String[] {"title", "summary", "distance"}, new int []{R.id.title, R.id.summary, R.id.distanceFrom});

                    listView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("user_name", userName);
                parameters.put("latitude", latitude.toString());
                parameters.put("longitude", longitude.toString());

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView selectedItem = (TextView) view.findViewById(R.id.title);
                //String selectedItem1 = (String) listView.getItemAtPosition(position);
                String selectedText = selectedItem.getText().toString();

                //Toast.makeText(getContext(), selectedText, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("viewInfo", selectedText);
                startActivity(intent);
                //loadFragment1(new ReviewFragment(), selectedItem.getText().toString());
            }
        });*/

        return view;
    }

    private boolean loadFragment(Fragment fragment, String title) {
        //switching fragment
        Bundle bundle1 = new Bundle();
        bundle1.putString("place", title);
        fragment.setArguments(bundle1);

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
