package e.iantm.recommendationapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view =  inflater.inflate(R.layout.fragment_home, null);
        loadFragment(new MapFragment());
        return view;
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.child_fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        /*if (requestCode == HomeFragment.MY_PERMISSIONS_REQUEST_LOCATION){
            homeFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        else {*/
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //}
    }
}