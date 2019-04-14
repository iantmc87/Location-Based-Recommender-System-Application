package e.iantm.recommendationapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WaitingScreenFragment extends Fragment {

    private TextView textProgress;
    private ProgressBar waiting;
    private int pStatus = 0;
    private Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_waiting_screen, null);
        waiting = (ProgressBar)view.findViewById(R.id.progressBar);
        textProgress = (TextView) view.findViewById(R.id.txtProgress);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pStatus <= 100) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            waiting.setProgress(pStatus);
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

        return view;
    }

}
