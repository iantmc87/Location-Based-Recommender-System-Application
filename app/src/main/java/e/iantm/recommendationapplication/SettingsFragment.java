package e.iantm.recommendationapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.omadahealth.lollipin.lib.managers.AppLock;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends PreferenceFragmentCompat {

    ListPreference systemPreference, radiusPreference, cuisinePreference, ambiencePreference;
    SwitchPreference wifiPreference, kidsPreference, groupPreference, wheelchairPreference, dogPreference;
   /* MultiSelectListPreference pricePreference;*/
    RequestQueue requestQueue;
    Request request;
    String systemValue, radiusValue;//, priceValue;
    String updateSystem, updateRadius, /*updatePrice,*/ updateKids, updateGroup, updateWheelchair, updateWifi, updateDog;
    Resources res;
    Preference pinChange;
    String userName;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
        requestQueue = Volley.newRequestQueue(getContext());
        res = getResources();

        Bundle bundle = getArguments();
        if(bundle != null) {
            userName = String.valueOf(bundle.get("userName"));
        }

        systemPreference = (ListPreference)findPreference("system");
        systemPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                systemValue = o.toString();
                updateSystem = String.format(res.getString(R.string.systemPHP), res.getString(R.string.url));
                request = new StringRequest(Request.Method.POST, updateSystem, new Response.Listener<String>() {
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
                        if (systemValue.equals("1")) {
                            parameters.put("system", "Content-Based");
                        } else if (systemValue.equals("2")) {
                            parameters.put("system", "Collaborative");
                        } else if (systemValue.equals("3")) {
                            parameters.put("system", "Hybrid");
                        }
                        parameters.put("user_name", userName);

                        return parameters;
                    }
                    };
                requestQueue.add(request);


                return true;
            }
        }); //end system preference on change listener


        updateRadius = String.format(res.getString(R.string.updateRadius), res.getString(R.string.url));
        radiusPreference = (ListPreference)findPreference("radius");
        radiusPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                radiusValue = o.toString();
                request = new StringRequest(Request.Method.POST, updateRadius, new Response.Listener<String>() {
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
                        parameters.put("radius", String.valueOf(radiusValue));
                        parameters.put("user_name", userName);

                        return parameters;
                    }
                };
                requestQueue.add(request);


                return true;
            }
        }); //end radius preference on change listener

        /*cuisinePreference = (ListPreference)findPreference("cuisine");
        cuisinePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                textValue = o.toString();
                Toast.makeText(getContext(), updateSystem, Toast.LENGTH_SHORT).show();
                request = new StringRequest(Request.Method.POST, updateSystem, new Response.Listener<String>() {
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
                        if(textValue.equals("1")) {
                            parameters.put("system", "Chinese");
                        } else if(textValue.equals("2")) {
                            parameters.put("system", "English");
                        } else if(textValue.equals("3")) {
                            parameters.put("system", "American");
                        } else if(textValue.equals("4")) {
                            parameters.put("system", "Indian");
                        } else if(textValue.equals("5")) {
                            parameters.put("system", "Mexican");
                        } else if(textValue.equals("6")) {
                            parameters.put("system", "Thai");
                        } else if(textValue.equals("7")) {
                            parameters.put("system", "Korean");
                        } else if(textValue.equals("8")) {
                            parameters.put("system", "Japanese");
                        }

                        return parameters;
                    }
                };
                requestQueue.add(request);


                return true;
            }
        }); //end cuisine preference on change listener

        ambiencePreference = (ListPreference)findPreference("ambience");
        ambiencePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                textValue = o.toString();
                Toast.makeText(getContext(), updateSystem, Toast.LENGTH_SHORT).show();
                request = new StringRequest(Request.Method.POST, updateSystem, new Response.Listener<String>() {
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
                        if(textValue.equals("1")) {
                        parameters.put("system", "Casual");
                        } else if(textValue.equals("2")) {
                            parameters.put("system", "Classy");
                        } else if(textValue.equals("3")) {
                            parameters.put("system", "Divey");
                        } else if(textValue.equals("4")) {
                            parameters.put("system", "Hipster");
                        } else if(textValue.equals("5")) {
                            parameters.put("system", "Intimate");
                        } else if(textValue.equals("6")) {
                            parameters.put("system", "Romantic");
                        } else if(textValue.equals("7")) {
                            parameters.put("system", "Trendy");
                        } else if(textValue.equals("8")) {
                            parameters.put("system", "Touristy");
                        } else if(textValue.equals("9")) {
                            parameters.put("system", "Upscale");
                        }

                        return parameters;
                    }
                };
                requestQueue.add(request);


                return true;
            }
        }); //end ambience preference on change listener*/

       /* pricePreference = (MultiSelectListPreference) findPreference("price");
        pricePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                priceValue = o.toString();
                updatePrice = String.format(res.getString(R.string.updatePrice), res.getString(R.string.url));
                Toast.makeText(getContext(), updatePrice, Toast.LENGTH_SHORT).show();
                request = new StringRequest(Request.Method.POST, updateSystem, new Response.Listener<String>() {
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
                        parameters.put("system", String.valueOf(priceValue));

                        return parameters;
                    }
                };
                requestQueue.add(request);


                return true;
            }
        });*/ //end price preference on change listener

        updateWifi = String.format(res.getString(R.string.updateWifi), res.getString(R.string.url));
        wifiPreference = (SwitchPreference)findPreference("wifi");
        wifiPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                final boolean isWifiOn = (Boolean) o;
                request = new StringRequest(Request.Method.POST, updateWifi, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        if(isWifiOn) {
                            parameters.put("wifi", "WiFifree");
                            parameters.put("user_name", userName);
                        }

                        return parameters;
                    }
                };

                requestQueue.add(request);
                return true;
            }
        });

        updateDog = String.format(res.getString(R.string.updateDog), res.getString(R.string.url));
        dogPreference = (SwitchPreference)findPreference("dogs");
        dogPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                final boolean isDogOn = (Boolean) o;
                request = new StringRequest(Request.Method.POST, updateDog, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        if(isDogOn) {
                            parameters.put("dog", "DogsAllowedTrue");
                            parameters.put("user_name", userName);
                        }

                        return parameters;
                    }
                };
                requestQueue.add(request);
                return true;
            }
        });

        updateGroup = String.format(res.getString(R.string.updateGroup), res.getString(R.string.url));
        groupPreference = (SwitchPreference)findPreference("groups");
        groupPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                final boolean isGroupsOn = (Boolean) o;
                request = new StringRequest(Request.Method.POST, updateGroup, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        if(isGroupsOn) {
                            parameters.put("group", "RestaurantsGoodForGroupsTrue");
                            parameters.put("user_name", userName);
                        }

                        return parameters;
                    }
                };
                requestQueue.add(request);
                return true;
            }
        });

        updateWheelchair = String.format(res.getString(R.string.updateWheelchair), res.getString(R.string.url));
        wheelchairPreference = (SwitchPreference)findPreference("wheelchair");
        wheelchairPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                final boolean isWheelchairOn = (Boolean) o;
                request = new StringRequest(Request.Method.POST, updateWheelchair, new Response.Listener<String>() {
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
                        if(isWheelchairOn) {
                            parameters.put("wheelchair", "WheelChairAccessibleTrue");
                            parameters.put("user_name", userName);

                        }

                        return parameters;

                    }
                };
                requestQueue.add(request);


                return true;
            }
        }); //end radius preference on change listener

        updateKids = String.format(res.getString(R.string.updateKids), res.getString(R.string.url));
        kidsPreference = (SwitchPreference)findPreference("kids");
        kidsPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                final boolean isKidsOn = (Boolean) o;
                request = new StringRequest(Request.Method.POST, updateKids, new Response.Listener<String>() {
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
                        if(isKidsOn) {
                            parameters.put("kids", "GoodForKidsTrue");
                            parameters.put("user_name", userName);
                        }

                        return parameters;

                    }
                };
                requestQueue.add(request);


                return true;
            }
        }); //end radius preference on change listener

        pinChange = (Preference)findPreference("changepin");
        pinChange.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent intent = new Intent(getActivity(), CustomPinActivity.class);
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.CHANGE_PIN);
                startActivity(intent);
                return true;
            }
        });
    }






    /*@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.xml.settings, null);

        Preference preference = (Preference)findPreference(R.id.)
        return view;
    }*/
}
