package fais.com.neuroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Handler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fais.com.neuroid.Neural.Data.DataVector;
import fais.com.neuroid.Neural.Data.TrainDataSet;
import fais.com.neuroid.Neural.NeuralNetwork;

public class MainActivity extends AppCompatActivity {

    private NeuralNetwork network;

    @Bind(R.id.button_start)
    public Button startButton;

    @Bind(R.id.text_view_result)
    public TextView resultView;


    @OnClick(R.id.button_start)
    public void startNetworkTraining(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                network = new NeuralNetwork(3, 4, new NeuralNetwork.NeuralNetworkCallbacks() {
                    @Override
                    public void onStartTraining(String msg) {

                    }

                    @Override
                    public void onTrainingProgress(String msg, final DataVector data) {
                        MainActivity.this.runOnUiThread(new Runnable() {
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
                TrainDataSet dataset = new TrainDataSet();
                dataset.add(new DataVector(new double[]{1, 1, 0, 0}));
                network.train(0.001, -1, dataset);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }
}
