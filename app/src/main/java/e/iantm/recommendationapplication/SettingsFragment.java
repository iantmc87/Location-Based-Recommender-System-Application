package e.iantm.recommendationapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.omadahealth.lollipin.lib.managers.AppLock;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/************************************************************
 Author - Ian McManus
 Version - 1.0.0
 Date - 30/04/2019
 Description - Fragment for the settings page

 ************************************************************/

public class SettingsFragment extends PreferenceFragmentCompat {

    ListPreference systemPreference, radiusPreference;
    SwitchPreference wifiPreference, kidsPreference, groupPreference, wheelchairPreference, dogPreference, parkingPreference, pinLock;
    MultiSelectListPreference pricePreference, cuisinePreference, ambiencePreference, alcoholPreference;
    RequestQueue requestQueue;
    Request request;
    String systemValue, radiusValue;
    String updateSystem, updateRadius, updateCuisine, updateAlcohol, updateAmbience, updatePrice, updateKids, updateGroup, updateWheelchair, updateWifi, updateDog;
    final List<String> cuisineValues = new ArrayList<String>();
    final List<String> AmbienceValues = new ArrayList<String>();
    final List<String> priceRangeValues = new ArrayList<String>();
    final List<String> alcoholValues = new ArrayList<String>();

    String cuisineValuesText, ambienceValuesText, priceRangeValuesText, alcoholValuesText;
    Resources res;
    Preference pinChange, viewReviews, resetInstructions;
    SharedPreferences instructionsPref;
    String currValue, userName, getSettings, getAlcohol = null, getAmbience = null, getPriceRange = null, getCategories = null, getSystem = null, getRadius = null, getGoodForKids = null, getGoodForGroups = null, getDogsAllowed = null, getWifi = null, getWheelchair = null, getParking = null;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
        requestQueue = Volley.newRequestQueue(getContext());
        res = getResources();
        systemPreference = (ListPreference)findPreference("system_choice");
        cuisinePreference = (MultiSelectListPreference) findPreference("cuisine");
        radiusPreference = (ListPreference)findPreference("radius");
        wifiPreference = (SwitchPreference)findPreference("wifi");
        dogPreference = (SwitchPreference)findPreference("dogs");
        groupPreference = (SwitchPreference)findPreference("groups");
        wheelchairPreference = (SwitchPreference)findPreference("wheelchair");
        kidsPreference = (SwitchPreference)findPreference("kids");
        parkingPreference = (SwitchPreference)findPreference("parking");
        alcoholPreference = (MultiSelectListPreference) findPreference("alcohol");
        ambiencePreference = (MultiSelectListPreference)findPreference("ambience");
        pricePreference = (MultiSelectListPreference) findPreference("price");
        viewReviews = (Preference)findPreference("viewReviews");
        resetInstructions = (Preference)findPreference("instructions");

        instructionsPref = (getContext().getSharedPreferences("instructions", Context.MODE_PRIVATE));
        SharedPreferences.Editor editor = instructionsPref.edit();
        editor.putString("settings", "false");
        editor.commit();



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

                    JSONObject obj = recommendations.getJSONObject(0);

                    getSystem = obj.getString("system");
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

                    Set<String> alcohol = new HashSet<String>();
                    getAlcohol = obj.getString("alcohol");
                    if(getAlcohol == null){
                        alcohol.add("3");
                    } else {
                        if(getAlcohol.contains("none")) {
                            alcohol.add("2");
                        } if(getAlcohol.contains("beer and wine")) {
                            alcohol.add("3");
                        } if(getAlcohol.contains("full bar")) {
                            alcohol.add("4");
                        } if(getAlcohol.contains("byob")) {
                            alcohol.add("5");
                        }
                        alcoholPreference.setValues(alcohol);
                    }

                    Set<String> ambience = new HashSet<String>();
                    getAmbience = obj.getString("ambience");
                    if(getAmbience == null) {

                    } else {
                        if(getAmbience.contains("casual")){
                            ambience.add("2");
                        } if(getAmbience.contains("classy")){
                            ambience.add("3");
                        } if(getAmbience.contains("divey")){
                            ambience.add("4");
                        } if(getAmbience.contains("hipster")){
                            ambience.add("5");
                        } if(getAmbience.contains("intimate")){
                            ambience.add("6");
                        } if(getAmbience.contains("romantic")){
                            ambience.add("7");
                        } if(getAmbience.contains("trendy")){
                            ambience.add("8");
                        } if(getAmbience.contains("touristy")){
                            ambience.add("9");
                        } if(getAmbience.contains("upscale")){
                            ambience.add("10");
                        }
                        ambiencePreference.setValues(ambience);
                    }

                    Set<String> price = new HashSet<String>();
                    getPriceRange = obj.getString("price");
                    if(getPriceRange == null) {

                    } else {
                        if(getPriceRange.equals("cheap")){
                            price.add("2");
                        }  if(getPriceRange.equals("good value")){
                            price.add("3");
                        } if(getPriceRange.equals("average")){
                            price.add("4");
                        } if(getPriceRange.equals("expensive")){
                            price.add("5");
                        }
                    }

                    Set<String> cuisine = new HashSet<String>();
                    getCategories = obj.getString("categories");
                    if(getCategories == null) {

                    } else {
                        if(getCategories.contains("American")) {
                            cuisine.add("2");
                        } if(getCategories.contains("Caribbean")) {
                            cuisine.add("3");
                        } if(getCategories.contains("Chinese")) {
                            cuisine.add("4");
                        } if(getCategories.contains("English")) {
                            cuisine.add("5");
                        } if(getCategories.contains("French")) {
                            cuisine.add("6");
                        } if(getCategories.contains("German")) {
                            cuisine.add("7");
                        } if(getCategories.contains("Indian")) {
                            cuisine.add("8");
                        } if(getCategories.contains("Italian")) {
                            cuisine.add("9");
                        } if(getCategories.contains("Japanese")) {
                            cuisine.add("10");
                        } if(getCategories.contains("Korean")) {
                            cuisine.add("11");
                        } if(getCategories.contains("Mexican")) {
                            cuisine.add("12");
                        } if(getCategories.contains("Thai")) {
                            cuisine.add("13");
                        } if(getCategories.contains("Vietnamese")) {
                            cuisine.add("14");
                        }
                        cuisinePreference.setValues(cuisine);
                    }


                    getGoodForKids = obj.getString("good_for_kids");
                    if(getGoodForKids == null) {
                        kidsPreference.setChecked(false);
                    } else {
                        if(getGoodForKids.equals("GoodForKidsTrue")){
                            kidsPreference.setChecked(true);
                        } else {
                            kidsPreference.setChecked(false);
                        }
                    }


                    getGoodForGroups = obj.getString("good_for_groups");
                    if(getGoodForGroups == null){
                        groupPreference.setChecked(false);
                    } else {
                        if (getGoodForGroups.equals("RestaurantsGoodForGroups")) {
                            groupPreference.setChecked(true);
                        } else {
                            groupPreference.setChecked(false);
                        }
                    }

                    getDogsAllowed = obj.getString("dogs_allowed");
                    if(getDogsAllowed == null) {
                        dogPreference.setChecked(false);
                    } else {
                        if (getDogsAllowed.equals("DogsAllowedTrue")) {
                            dogPreference.setChecked(true);
                        } else {
                            dogPreference.setChecked(false);
                        }
                    }

                    getWifi = obj.getString("wifi");
                    if(getWifi == null){
                        wifiPreference.setChecked(false);
                    } else {
                        if (getWifi.equals("Wififree")) {
                            wifiPreference.setChecked(true);
                        } else {
                            wifiPreference.setChecked(false);
                        }
                    }

                    getWheelchair = obj.getString("wheelchair_accessible");
                    if(getWheelchair == null) {
                        wheelchairPreference.setChecked(false);
                    } else {
                        if (getWheelchair.equals("WheelchairAccessibleTrue")) {
                            wheelchairPreference.setChecked(true);
                        } else {
                            wheelchairPreference.setChecked(false);
                        }
                    }

                    getParking = obj.getString("parking");
                    if(getParking == null) {
                        parkingPreference.setChecked(false);
                    } else {
                        if(getParking.equals("ParkingTrue")){
                            parkingPreference.setChecked(true);
                        } else {
                            parkingPreference.setChecked(false);
                        }
                    }
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

        updateCuisine = String.format(res.getString(R.string.updateCuisine), res.getString(R.string.url));

        cuisinePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                //Set<String> currValue = cuisinePreference.getValues();
                cuisineValues.clear();
                currValue = o.toString();

                if(currValue.contains("2")) {
                    cuisineValues.add("American");
                } if(currValue.contains("3")) {
                    cuisineValues.add("Caribbean");
                } if(currValue.contains("4")) {
                    cuisineValues.add("Chinese");
                } if(currValue.contains("5")) {
                    cuisineValues.add("English");
                } if(currValue.contains("6")) {
                    cuisineValues.add("French");
                } if(currValue.contains("7")) {
                    cuisineValues.add("German");
                } if(currValue.contains("8")) {
                    cuisineValues.add("Indian");
                } if(currValue.contains("9")) {
                    cuisineValues.add("Italian");
                } if(currValue.contains("10")) {
                    cuisineValues.add("Japanese");
                } if(currValue.contains("11")) {
                    cuisineValues.add("Korean");
                } if(currValue.contains("12")) {
                    cuisineValues.add("Mexican");
                } if(currValue.contains("13")) {
                    cuisineValues.add("Thai");
                } if(currValue.contains("14")) {
                    cuisineValues.add("Vietnamese");

                }
                cuisineValuesText = cuisineValues.toString();
                //cuisineValuesText.replace("[", "");
               // Toast.makeText(getContext(), cuisineValuesText, Toast.LENGTH_SHORT).show();

                request = new StringRequest(Request.Method.POST, updateCuisine, new Response.Listener<String>() {
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
                        parameters.put("cuisine", cuisineValuesText);
                        parameters.put("user_name", userName);

                        return parameters;
                    }
                };
                requestQueue.add(request);

                return true;
            }
        }); //end cuisine preference on change listener

        updateAmbience = String.format(res.getString(R.string.updateAmbience), res.getString(R.string.url));

        ambiencePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                AmbienceValues.clear();
                currValue = o.toString();

                if(currValue.contains("2")) {
                    AmbienceValues.add("casual");
                } if(currValue.contains("3")) {
                    AmbienceValues.add("classy");
                } if(currValue.contains("4")) {
                    AmbienceValues.add("divey");
                } if(currValue.contains("5")) {
                    AmbienceValues.add("hipster");
                } if(currValue.contains("6")) {
                    AmbienceValues.add("intimate");
                } if(currValue.contains("7")) {
                    AmbienceValues.add("romantic");
                } if(currValue.contains("8")) {
                    AmbienceValues.add("trendy");
                } if(currValue.contains("9")) {
                    AmbienceValues.add("touristy");
                } if(currValue.contains("10")) {
                    AmbienceValues.add("upscale");
                }

                ambienceValuesText = AmbienceValues.toString();

                request = new StringRequest(Request.Method.POST, updateAmbience, new Response.Listener<String>() {
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
                        parameters.put("ambience", ambienceValuesText);
                        parameters.put("user_name", userName);
                        return parameters;
                    }
                };
                requestQueue.add(request);

                return true;
            }
        }); //end ambience preference on change listener

        updatePrice = String.format(res.getString(R.string.updatePriceRange), res.getString(R.string.url));

        pricePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                priceRangeValues.clear();
                currValue = o.toString();

                if(currValue.contains("2")) {
                    priceRangeValues.add("cheap");
                } if(currValue.contains("3")) {
                    priceRangeValues.add("good value");
                } if(currValue.contains("4")) {
                    priceRangeValues.add("average");
                } if(currValue.contains("5")) {
                    priceRangeValues.add("expensive");
                }

                priceRangeValuesText = priceRangeValues.toString();
                Toast.makeText(getContext(), priceRangeValuesText, Toast.LENGTH_SHORT).show();

                request = new StringRequest(Request.Method.POST, updatePrice, new Response.Listener<String>() {
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
                        parameters.put("price", priceRangeValuesText);
                        parameters.put("user_name", userName);

                        return parameters;
                    }
                };
                requestQueue.add(request);

                return true;
            }
        }); //end price preference on change listener

        updateAlcohol = String.format(res.getString(R.string.updateAlcohol), res.getString(R.string.url));
        alcoholPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                alcoholValues.clear();
                currValue = o.toString();

                if(currValue.contains("2")) {
                    alcoholValues.add("none");
                } if(currValue.contains("3")) {
                    alcoholValues.add("beer and wine");
                } if(currValue.contains("4")) {
                    alcoholValues.add("full bar");
                } if(currValue.contains("5")) {
                    alcoholValues.add("byob");
                }

                alcoholValuesText = alcoholValues.toString();
                Toast.makeText(getContext(), alcoholValuesText, Toast.LENGTH_SHORT).show();

                request = new StringRequest(Request.Method.POST, updateAlcohol, new Response.Listener<String>() {
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
                        parameters.put("alcohol", alcoholValuesText);
                        parameters.put("user_name", userName);
                        return parameters;
                    }
                };
                requestQueue.add(request);

                return true;
            }
        });

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

        pinLock = (SwitchPreference)findPreference("pincode");
        pinLock.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                final Boolean isLockOn = (Boolean) o;

                if(isLockOn) {
                    Intent intent = new Intent(getActivity(), CustomPinActivity.class);
                    intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), CustomPinActivity.class);
                    intent.putExtra(AppLock.EXTRA_TYPE, AppLock.DISABLE_PINLOCK);
                    startActivity(intent);
                }
                return true;
            }
        });

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

        viewReviews.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("viewInfo", "viewReviews");
                //intent.putExtra("username", userName);
                Toast.makeText(getContext(), userName, Toast.LENGTH_SHORT).show();
                startActivity(intent);
                return true;
            }
        });

        resetInstructions.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                instructionsPref = (getContext().getSharedPreferences("instructions", Context.MODE_PRIVATE));
                SharedPreferences.Editor editor = instructionsPref.edit();

                editor.putString("home", "true");
                editor.putString("review", "true");
                editor.putString("settings", "true");
                editor.commit();

                Toast.makeText(getContext(), "Instructions Reset", Toast.LENGTH_SHORT).show();
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