package fais.com.neuroid.presentation.activities;


import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fais.com.neuroid.Neural.Data.DataVector;
import fais.com.neuroid.Neural.Neuron;
import fais.com.neuroid.R;
import fais.com.neuroid.presentation.AndroidOutputProvider;
import fais.com.neuroid.utilities.Util;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecognitionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecognitionFragment extends Fragment {

    private static final int GRID_SIZE = 4;

    @Bind(R.id.activity_game_grid_view)
    GridView gridView;

    AndroidOutputProvider outputProvider;


    public RecognitionFragment() {
        // Required empty public constructor
    }

    public static RecognitionFragment newInstance() {
        RecognitionFragment fragment = new RecognitionFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_recognition, container, false);
        ButterKnife.bind(this, view);
        // init game with starting params
        initGame();
        // set input listeners

        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int position = gridView.pointToPosition((int) event.getX(), (int) event.getY());
                        Point point = Util.convert1DIndexTo2D(position, GRID_SIZE);
                        if (outputProvider != null) {
                            if (checkCoords(point.x, point.y, GRID_SIZE))
                                outputProvider.drawOnBoard(point.x, point.y, R.drawable.board_touched_thumbnail);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        return view;
    }

    public boolean checkCoords(int x, int y, int boardSize) {
        if (x >= boardSize || x < 0) {
            return false;
        }
        if (y >= boardSize || y < 0) {
            return false;
        }
        return true;
    }

    public void initGame() {
        outputProvider = new AndroidOutputProvider(gridView, GRID_SIZE, getActivity());
    }


    @OnClick(R.id.clear_button)
    public void clearClick(View v) {
        outputProvider.clearPointsList();
        initGame();
    }

    @OnClick(R.id.go_button)
    public void goClick(View v) {
        if (((MainActivity)getActivity()).getNeuralNetwork().getState() == Neuron.STATE.LEARNING) {
            Toast.makeText(getActivity(), "Network is still learning!", Toast.LENGTH_SHORT).show();
        } else {
            DataVector output = ((MainActivity)getActivity()).getNeuralNetwork().generateOutput(new DataVector(outputProvider.getPointsTable()));
            Toast.makeText(getActivity(), output.toUIString(), Toast.LENGTH_LONG).show();
        }
    }

}
