package e.iantm.recommendationapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ReviewFragment extends Fragment {

    String title;
    EditText search;
    TextView textView;
    ImageView startSearch;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_review, null);
        textView = (TextView) view.findViewById(R.id.textView);
        startSearch = (ImageView)view.findViewById(R.id.imageView);
        Bundle bundle = getArguments();
        title = String.valueOf(bundle.get("title"));

        if(title != "navBar") {
            loadFragment(new reviewListFragment(), title);
            textView.setVisibility(View.INVISIBLE);
        } else {
            //loadFragment(new SearchListFragment(), null);
        }

        search = (EditText) view.findViewById(R.id.search);

        search.setText(title);

        startSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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