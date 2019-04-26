package e.iantm.recommendationapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
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

/************************************************************
 Author - Ian McManus
 Version - 1.0.0
 Date - 30/04/2019
 Description - Fragment for the main review navigation bar

 ************************************************************/

public class ReviewFragment extends Fragment {

    String title, autoPostcode, autoName;
    RadioGroup radioGroup;
    RadioButton radioButton;
    AutoCompleteTextView search;
    List<String> list = new ArrayList<String>();
    TextView textProgress;
    ProgressBar progressBar;
    ImageView startSearch;
    String searchPlaces, userName;
    Resources res;
    Request request;
    StringRequest searchRequest;
    RequestQueue requestQueue;
    ArrayAdapter<String> adapter;
    RadioButton postCode, restaurant;
    private int pStatus = 0;
    private Handler handler = new Handler();
    SharedPreferences instructionsPref;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_review, null);
        instructionsPref = (getContext().getSharedPreferences("instructions", Context.MODE_PRIVATE));
        SharedPreferences.Editor editor = instructionsPref.edit();
        editor.putString("review", "false");
        editor.commit();

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        textProgress = (TextView)view.findViewById(R.id.txtProgress);
        startSearch = (ImageView)view.findViewById(R.id.imageView);
        radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
        postCode = (RadioButton) view.findViewById(R.id.radioButtonPostcode);

        requestQueue = Volley.newRequestQueue(getContext());
        res = getResources();
        autoPostcode = String.format(res.getString(R.string.autocompletePostcode), res.getString(R.string.url));
        autoName = String.format(res.getString(R.string.autocompleteName), res.getString(R.string.url));


        search = (AutoCompleteTextView) view.findViewById(R.id.search);
        radioGroup.clearCheck();
        search.setHint("Choose search option");
        Bundle bundle1 = getArguments();
        if(bundle1 != null) {
            userName = String.valueOf(bundle1.get("userName"));
            title = String.valueOf(bundle1.get("title"));
        }

            if (title.equals("null")) {


            } else if (title.equals("addReview")) {
                loadFragment(new AddReviewFragment(), title, userName, "addReview", "addReview");

            } else {
                loadFragment(new reviewListFragment(), title, userName, "showReviews", "reviewList");
            }





        switchSearch(searchRequest, autoPostcode, "title", requestQueue);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton)view.findViewById(selectedId);
                String radioButtonText = radioButton.getText().toString();
                if(adapter!=null) {
                    list.clear();
                    adapter.notifyDataSetChanged();
                }

                if(radioButtonText.equals("Postcode")) {

                    switchSearch(searchRequest, autoPostcode, "postcode", requestQueue);
                    search.setHint("Enter Postcode");
                } else if (radioButtonText.equals("Restaurant")) {
                    search.setHint("Enter Restaurant");
                    switchSearch(searchRequest, autoName, "title", requestQueue);
                }

            }
        });

        searchPlaces = String.format(res.getString(R.string.search), res.getString(R.string.url));
        startSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment myFragment = (Fragment)getChildFragmentManager().findFragmentByTag("reviewList");
                Fragment myFragment1 = (Fragment)getChildFragmentManager().findFragmentByTag("searchList");
                Fragment myFragment2 = (Fragment)getChildFragmentManager().findFragmentByTag("addReview");


                if(myFragment != null && myFragment.isVisible()) {
                    getChildFragmentManager().beginTransaction().remove(myFragment).commit();
                } else if (myFragment1 != null && myFragment1.isVisible()) {
                    getChildFragmentManager().beginTransaction().remove(myFragment1).commit();

                } else if (myFragment2 != null && myFragment2.isVisible()) {
                    getChildFragmentManager().beginTransaction().remove(myFragment2).commit();

                }
                final String searchText = search.getText().toString();

                request = new StringRequest(Request.Method.POST, searchPlaces, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (radioButton.getText().toString().equals("Postcode")) {

                            progressBar.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (pStatus <= 100) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.setProgress(pStatus);
                                                textProgress.setText(pStatus + " %");
                                            }
                                        });
                                        try {
                                            Thread.sleep(96);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        pStatus++;
                                    }
                                }
                            }).start();

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadFragment(new SearchListFragment(), null, userName, null, "searchList");
                                    progressBar.setVisibility(View.INVISIBLE);
                                    textProgress.setVisibility(View.INVISIBLE);
                                }
                            }, 10000);
                        } else if (radioButton.getText().toString().equals("Restaurant")) {
                            loadFragment(new reviewListFragment(), searchText, userName, "showReviews", "reviewList");
                        }
                        search.setText("");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("search", searchText);
                            parameters.put("user_name", userName);
                        return parameters;
                    }
                };
                requestQueue.add(request);
            }
        });


        return view;
    }

    public void switchSearch (StringRequest request, String url, final String getVariable, RequestQueue requestQueue) {

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray recommendations = jsonObject.getJSONArray("autocomplete");
                    int length = recommendations.length();
                    String item;
                    for (int i = 0; i < length; i++) {
                        JSONObject obj = recommendations.getJSONObject(i);
                        item = obj.getString(getVariable);
                        list.add(item);
                    }

                    adapter = new
                            ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);

                    search.setThreshold(1);
                    search.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private boolean loadFragment(Fragment fragment, String title, String userName, String option, String tag) {
        //switching fragment
        Bundle bundle1 = new Bundle();
        bundle1.putString("userName", userName);
        bundle1.putString("place", title);
        bundle1.putString("option", option);
        fragment.setArguments(bundle1);

        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.child_fragment_container, fragment, tag)
                    .commit();
            return true;
        }
        return false;
    }

    private void removeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }
}