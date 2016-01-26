package fais.com.neuroid.presentation.activities;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fais.com.neuroid.Neural.Data.DataVector;
import fais.com.neuroid.Neural.Data.TrainDataSet;
import fais.com.neuroid.Neural.NeuralNetwork;
import fais.com.neuroid.Neural.Neuron;
import fais.com.neuroid.R;


public class LearnFragment extends Fragment {

    @Bind(R.id.radio_1)
    public CheckBox data1;
    @Bind(R.id.radio_2)
    public CheckBox data2;
    @Bind(R.id.radio_3)
    public CheckBox data3;

    @Bind(R.id.edit_text_1_offset)
    public EditText offset1;
    @Bind(R.id.edit_text_2_offset)
    public EditText offset2;
    @Bind(R.id.edit_text_3_offset)
    public EditText offset3;

    @Bind(R.id.button_learn)
    public Button buttonLearn;

    @Bind(R.id.text_view_progress)
    public TextView progressTextView;

    @Bind(R.id.loader)
    public ProgressBar loader;

    public LearnFragment() {
        // Required empty public constructor
    }
    public static LearnFragment newInstance() {
        LearnFragment fragment = new LearnFragment();
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
        View view =  inflater.inflate(R.layout.fragment_learn, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @OnClick(R.id.button_learn)
    public void startLearning() {
        // sprawdzamy, czy siec sie nie uczy w tej chwili
        NeuralNetwork oldNetwork = ((MainActivity)getActivity()).getNeuralNetwork();
        if(oldNetwork != null && oldNetwork.getState() == Neuron.STATE.LEARNING) {
            return;
        }
        double sumOffset = Double.parseDouble(offset1.getText().toString());
        double learningOffset = Double.parseDouble(offset2.getText().toString());
        double activationOffset = Double.parseDouble(offset3.getText().toString());
        final NeuralNetwork network = new NeuralNetwork(3, new int[]{16, 10, 2}, new NeuralNetwork.NeuralNetworkCallbacks() {
            @Override
            public void onStartTraining(String msg) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loader.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onTrainingProgress(String msg, DataVector data, double currentError) {
                final String message = msg + System.lineSeparator() + currentError;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressTextView.setText(message);
                    }
                });
            }

            @Override
            public void onFinishTraining(final String msg, DataVector data, final double currentError) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loader.setVisibility(View.INVISIBLE);
                        progressTextView.setText("LEARNED! Final error = " + currentError);
                    }
                });
            }

            @Override
            public void onError(final String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loader.setVisibility(View.INVISIBLE);
                        progressTextView.setText("ERROR: " + message);
                    }
                });

            }
        });

        network.setActivationOffset(activationOffset);
        network.setLearningOffset(learningOffset);
        network.setSumOffset(sumOffset);

        // ustawiamy w aktywnosci ta siec
        ((MainActivity)getActivity()).setNeuralNetwork(network);

        // pobieramy dane z UI
        final TrainDataSet dataset = new TrainDataSet(2);
        if (data1.isChecked()) {
//            dataset.add();
        }
        if (data2.isChecked()) {
//            dataset.add();
        }
        if (data3.isChecked()) {
//            dataset.add();
        }

        dataset.add(new DataVector(new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,}, 0));
        dataset.add(new DataVector(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 1));
        // zapuszczamy uczenie. -1 oznacza, ze bedzie while w kolko chodzilo
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                network.train(0.01, -1, dataset);
            }
        });
    }


}
