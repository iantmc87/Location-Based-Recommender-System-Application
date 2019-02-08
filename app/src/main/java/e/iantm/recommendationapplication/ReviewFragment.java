package e.iantm.recommendationapplication;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ReviewFragment extends Fragment {

    String title;
    EditText search;
    TextView textProgress;
    ProgressBar progressBar;
    ImageView startSearch;
    String searchPlaces, userName;
    Resources res;
    Request request;
    RequestQueue requestQueue;
    private int pStatus = 0;
    private Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_review, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        textProgress = (TextView)view.findViewById(R.id.txtProgress);
        startSearch = (ImageView)view.findViewById(R.id.imageView);
        requestQueue = Volley.newRequestQueue(getContext());
        res = getResources();
        Bundle bundle = getArguments();
        if(bundle!=null) {
            title = String.valueOf(bundle.get("title"));
            //Toast.makeText(getContext(), title, Toast.LENGTH_SHORT).show();

            if (title.equals("null")) {
                //loadFragment(new SearchListFragment(), null);
            } else {
                loadFragment(new reviewListFragment(), title);

            }
        }

        Bundle bundle1 = getArguments();
        if(bundle1 != null) {
            userName = String.valueOf(bundle.get("userName"));
        }

        search = (EditText) view.findViewById(R.id.search);

        //search.setText(title);

        searchPlaces = String.format(res.getString(R.string.search), res.getString(R.string.url));
        startSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                final String searchText = search.getText().toString();
                request = new StringRequest(Request.Method.POST, searchPlaces, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                                    loadFragment(new SearchListFragment(), null);
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    textProgress.setVisibility(View.INVISIBLE);
                                                }
                                            }, 10000);

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