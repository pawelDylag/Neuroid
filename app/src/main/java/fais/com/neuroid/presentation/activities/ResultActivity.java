package fais.com.neuroid.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import fais.com.neuroid.Neural.Data.DataVector;
import fais.com.neuroid.Neural.Data.TrainDataSet;
import fais.com.neuroid.Neural.NeuralNetwork;
import fais.com.neuroid.R;


public class ResultActivity extends AppCompatActivity {


    @Bind(R.id.text_view_result)
    public TextView resultView;

    private NeuralNetwork network;
    private double[] points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set content view
        setContentView(R.layout.activity_result);
        // bind this view
        ButterKnife.bind(this);
        // init game with starting params
        readIntent();
        startNetworkTraining();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    private void readIntent() {
        Bundle bundle = getIntent().getExtras();
        points = bundle.getDoubleArray(MainActivity.LIST_KEY);
        for (int i = 0; i < points.length; i++)
            Log.i("points", "i = " + i + "  points[i] = " + points[i]);
    }


    //@OnClick(R.id.button_start)
    public void startNetworkTraining() {
        // Executor jest po to, zeby walnac operacje w osobnym watku
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // tworzymy siec z 3 warstw, 4 neuronami w kazdej, i dajemy callbacki, zebysmy mogli reagowac na wydarzenia
                network = new NeuralNetwork(3, 4, new NeuralNetwork.NeuralNetworkCallbacks() {
                    @Override
                    public void onStartTraining(String msg) {

                    }

                    @Override
                    public void onTrainingProgress(String msg, final DataVector data) {
                        ResultActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resultView.setText(data.toUIString());
                            }
                        });
                    }

                    @Override
                    public void onFinishTraining(String msg, DataVector data) {

                    }

                    @Override
                    public void onError(String message) {

                    }
                });
                // dodajemy nowy pattern do datasetu
                TrainDataSet dataset = new TrainDataSet();
                dataset.add(new DataVector(new double[]{1, 1, 0, 0}));
                //dataset.add(new DataVector(points));
                // zapuszczamy uczenie. -1 oznacza, ze bedzie while w kolko chodzilo
                network.train(0.001, -1, dataset);
            }
        });
    }
}
