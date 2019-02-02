package e.iantm.recommendationapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ReviewFragment extends Fragment {

    String title;
    EditText search;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        Bundle bundle = getArguments();
        if(bundle != null) {
            title = bundle.get("title").toString();
        }

        loadFragment(new reviewListFragment(), title);
        View view = inflater.inflate(R.layout.fragment_review, null);
        search = (EditText) view.findViewById(R.id.search);

        search.setText(title);
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