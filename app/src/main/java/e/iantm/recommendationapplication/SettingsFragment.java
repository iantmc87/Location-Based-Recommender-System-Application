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
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends PreferenceFragmentCompat {

    ListPreference systemPreference, radiusPreference, cuisinePreference, ambiencePreference;
    SwitchPreference wifiPreference, kidsPreference, groupPreference, wheelchairPreference, dogPreference;
   /* MultiSelectListPreference pricePreference;*/
    RequestQueue requestQueue;
    Request request;
    StringRequest stringRequest;
    String systemValue, radiusValue;//, priceValue;
    String updateSystem, updateRadius, /*updatePrice,*/ updateKids, updateGroup, updateWheelchair, updateWifi, updateDog;
    Resources res;
    Preference pinChange;
    String userName, getSettings, getSystem = null, getRadius = null, getGoodForKids = null, getGoodForGroups = null, getDogsAllowed = null, getWifi = null, getWheelchair = null, getParking = null;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
        requestQueue = Volley.newRequestQueue(getContext());
        res = getResources();
        systemPreference = (ListPreference)findPreference("system_choice");
        radiusPreference = (ListPreference)findPreference("radius");
        wifiPreference = (SwitchPreference)findPreference("wifi");
        dogPreference = (SwitchPreference)findPreference("dogs");
        groupPreference = (SwitchPreference)findPreference("groups");
        wheelchairPreference = (SwitchPreference)findPreference("wheelchair");
        kidsPreference = (SwitchPreference)findPreference("kids");

        Bundle bundle = getArguments();
        if(bundle != null) {
            userName = String.valueOf(bundle.get("userName"));
        }

        getSettings = String.format(res.getString(R.string.singleSettings), res.getString(R.string.url));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getSettings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray recommendations = jsonObject.getJSONArray("get_settings");

                    Toast.makeText(getContext(), recommendations.toString(), Toast.LENGTH_SHORT).show();
                    JSONObject obj = recommendations.getJSONObject(0);

                    /*getSystem = obj.getString("system");
                    if(getSystem == null) {
                        systemPreference.setValue("1");
                    } else {
                        if(getSystem.equals("Content-Based")){
                            systemPreference.setValue("1");
                        } else if (getSystem.equals("Collaborative")) {
                            systemPreference.setValue("2");
                        } else if (getSystem.equals("Hybrid")) {
                            systemPreference.setValue("3");
                        }
                    }
                    getRadius = obj.getString("radius");
                    radiusPreference.setValue(getRadius);


                    getGoodForKids = obj.getString("good_for_kids");
                    if(getGoodForKids != null){
                        kidsPreference.setChecked(true);
                    } else {
                        kidsPreference.setChecked(false);
                    }

                    getGoodForGroups = obj.getString("good_for_groups");
                    if(getGoodForGroups != null) {
                        groupPreference.setChecked(true);
                    } else {
                        groupPreference.setChecked(false);
                    }

                    getDogsAllowed = obj.getString("dogs_allowed");
                    if(getDogsAllowed != null) {
                        dogPreference.setChecked(true);
                    } else {
                        dogPreference.setChecked(false);
                    }

                    getWifi = obj.getString("wifi");
                    if(getWifi != null) {
                        wifiPreference.setChecked(true);
                    } else {
                        wifiPreference.setChecked(false);
                    }

                    getWheelchair = obj.getString("wheelchair_accessible");
                    if(getWheelchair != null) {
                        wheelchairPreference.setChecked(true);
                    } else {
                        wheelchairPreference.setChecked(false);
                    }

                    getParking = obj.getString("parking");

                    Toast.makeText(getContext(), getSystem, Toast.LENGTH_SHORT).show();*/



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
        wifiPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                final boolean isWifiOn = (Boolean) o;
                switchMethod(request, updateWifi, isWifiOn, userName, "wifi", "Wififree", requestQueue);

                return true;
            }
        });

        updateDog = String.format(res.getString(R.string.updateDog), res.getString(R.string.url));
        dogPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                final boolean isDogOn = (Boolean) o;
                switchMethod(request, updateDog, isDogOn, userName, "dog", "DogsAllowedTrue", requestQueue);
                return true;
            }
        });

        updateGroup = String.format(res.getString(R.string.updateGroup), res.getString(R.string.url));
        groupPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                final boolean isGroupsOn = (Boolean) o;
                switchMethod(request, updateGroup, isGroupsOn, userName, "group", "RestaurantsGoodForGroups", requestQueue);

                return true;
            }
        });

        updateWheelchair = String.format(res.getString(R.string.updateWheelchair), res.getString(R.string.url));
        wheelchairPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                final boolean isWheelchairOn = (Boolean) o;

                switchMethod(request, updateWheelchair, isWheelchairOn, userName, "wheelchair", "WheelchairAccessibleTrue", requestQueue);

                return true;
            }
        }); //end radius preference on change listener

        updateKids = String.format(res.getString(R.string.updateKids), res.getString(R.string.url));
        kidsPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                final boolean isKidsOn = (Boolean) o;

                switchMethod(request, updateKids, isKidsOn, userName, "kids", "GoodForKidsTrue", requestQueue);

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

    public void switchMethod (Request request, String url, final Boolean option, final String userName, final String param, final String text, RequestQueue requestQueue) {

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                if (option) {
                    parameters.put(param, text);

                } else if (!option) {
                    parameters.put(param, "");
                }
                parameters.put("user_name", userName);
                return parameters;
            }
        };


        requestQueue.add(request);
    }
}
