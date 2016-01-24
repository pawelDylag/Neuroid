package fais.com.neuroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fais.com.neuroid.Neural.Data.DataVector;
import fais.com.neuroid.Neural.Data.TrainDataSet;
import fais.com.neuroid.Neural.NeuralNetwork;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        NeuralNetwork network = new NeuralNetwork(3, 4);
        TrainDataSet dataset = new TrainDataSet();
        dataset.add(new DataVector(new double[]{1,1,0,0}));
        network.train(0.001, -1, dataset);
    }
}
