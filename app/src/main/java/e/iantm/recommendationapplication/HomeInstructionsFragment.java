package e.iantm.recommendationapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/************************************************************
 Author - Ian McManus
 Version - 1.0.0
 Date - 30/04/2019
 Description - Fragment for the home page instructions on initial install

 ************************************************************/

public class HomeInstructionsFragment extends Fragment {

    ImageView exit, business, stars, distance, map, location;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home_instructions, null);

        exit = (ImageView) view.findViewById(R.id.imageView5);
        business = (ImageView) view.findViewById(R.id.imageView6);
        stars = (ImageView) view.findViewById(R.id.imageView7);
        distance = (ImageView) view.findViewById(R.id.imageView8);
        map = (ImageView) view.findViewById(R.id.imageView9);
        location = (ImageView) view.findViewById(R.id.imageView10);

        //timer for how long before showing instructions
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                exit.setVisibility(View.VISIBLE);
                business.setVisibility(View.VISIBLE);
                stars.setVisibility(View.VISIBLE);
                distance.setVisibility(View.VISIBLE);
                map.setVisibility(View.VISIBLE);
                location.setVisibility(View.VISIBLE);
                }
        }, 10000);//end handler for instructions timer


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .remove(HomeInstructionsFragment.this).commit();
            }
        });//end exit onClickListener

        return view;
    }//end onCreateView

}//end class