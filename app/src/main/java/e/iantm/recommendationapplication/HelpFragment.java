package e.iantm.recommendationapplication;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.regex.Pattern;

import static android.content.Context.ACCOUNT_SERVICE;

public class HelpFragment extends Fragment {

    private boolean accountsPermissionGranted;
    private static final int REQUEST_GET_ACCOUNT = 112;
    TextView help;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_help, null);
        help = (TextView)view.findViewById(R.id.help);


        if(android.os.Build.VERSION.SDK_INT > 22){
            if(isGETACCOUNTSAllowed()){
                // do your task

                getMailAddress();
            }else{
                requestGET_ACCOUNTSPermission();
            }

        }



        return view;
    }

    private boolean isGETACCOUNTSAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.GET_ACCOUNTS);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }


    //if you don't have the permission then Requesting for permission
    private void requestGET_ACCOUNTSPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(), android.Manifest.permission.GET_ACCOUNTS)){


        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions((Activity) getContext(),new String[]{android.Manifest.permission.GET_ACCOUNTS},REQUEST_GET_ACCOUNT);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == REQUEST_GET_ACCOUNT){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){


                Toast.makeText(getContext(),"Thanks You For Permission Granted ",Toast.LENGTH_LONG).show();

                getMailAddress();

            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(getContext(),"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }

    }

    public void getMailAddress(){


        AccountManager am = AccountManager.get(getContext()); // "this" references the current Context
        String acName = null;
        int end = 0;
        Account [] accounts = am.getAccounts();
       for (Account ac : accounts) {
           end = ac.name.indexOf("@");
           if(end != -1){
               acName = ac.name.substring(0, end);
           }  else {
               acName = ac.name;
           }
        }
        help.setText(acName);

    }
}