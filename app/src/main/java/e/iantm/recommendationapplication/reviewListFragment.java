package e.iantm.recommendationapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class reviewListFragment extends Fragment {

    String reviews, title, option, userName, getBusinessInfo;
    RequestQueue requestQueue;
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;
    View view;
    Resources res;
    ListView listView;
    TextView business, categories, address;
    FloatingActionButton addReview;
    Switch map;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_review_list, null);
        business = (TextView)view.findViewById(R.id.business_name);
        categories = (TextView)view.findViewById(R.id.categories);
        address = (TextView)view.findViewById(R.id.address);
        requestQueue = Volley.newRequestQueue(getContext());
        addReview = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        res = getResources();
        getBusinessInfo = String.format(res.getString(R.string.getBusinessInfo), res.getString(R.string.url));
        map = (Switch) view.findViewById(R.id.switch1);

        final Bundle bundle = getArguments();
        if(bundle != null) {
            title = String.valueOf(bundle.get("place"));
            option = String.valueOf(bundle.get("option"));
            userName = String.valueOf(bundle.get("user_name"));

            StringRequest stringRequest = new StringRequest(Request.Method.POST, getBusinessInfo, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONArray info = jsonObject.getJSONArray("info");
                        JSONObject obj = info.getJSONObject(0);
                        business.setText(obj.getString("name"));
                        categories.setText(obj.getString("categories"));
                        String address1 = obj.getString("constituency") + "\n" +
                                obj.getString("district") + "\n" + obj.getString("postcode");
                        address.setText(address1);


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
                    parameters.put("business", title);

                    return parameters;
                }
            };
            requestQueue.add(stringRequest);

        }
            if(option.equals("showReviews")){
                Toast.makeText(getContext(), title, Toast.LENGTH_SHORT).show();
            loadFragment1(new ShowReviewsFragment(), title, null);
            }
             else {

        }

        map.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    loadFragment1(new BusinessMapFragment(), title, null);
                } else {
                    loadFragment1(new ShowReviewsFragment(), title, null);
                }
            }
        });




        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.setOnCheckedChangeListener(null);
                map.setChecked(false);
                loadFragment1(new AddReviewFragment(), title, userName);

            }
        });

        return view;
    }

    private boolean loadFragment1(Fragment fragment, String title, String userName) {
        //switching fragment

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("userName", userName);
        fragment.setArguments(bundle);
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.review_child_fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
