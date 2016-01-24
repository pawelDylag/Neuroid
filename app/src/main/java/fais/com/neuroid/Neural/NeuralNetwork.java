package fais.com.neuroid.Neural;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import fais.com.neuroid.Neural.Data.DataVector;
import fais.com.neuroid.Neural.Data.ErrorMatrix;
import fais.com.neuroid.Neural.Data.TrainDataSet;

/**
 * Created by paweldylag on 24/01/16.
 */
public class NeuralNetwork {

    private static final String TAG = NeuralNetwork.class.getSimpleName();
    private static final double SUM_OFFSET = 1.0;
    private static final double ACTIVATION_OFFSET = 1.0;
    private static final double LEARNING_OFFSET = 1.0;

    private final int numberOfLayers;
    private final int numberOfNeuronsInEachLayer;
    private final List<NeuronLayer> layers;

    private final NeuralNetworkCallbacks callback;

    /** Callbacs for informing about network state and progress */
    public interface NeuralNetworkCallbacks {
        void onStartTraining(String msg);
        void onTrainingProgress(String msg, DataVector data);
        void onFinishTraining(String msg, DataVector data);
        void onError(String message);
    }

    public NeuralNetwork(int numberOfLayers, int numberOfNeuronsInEachLayer, NeuralNetworkCallbacks callback) {
        this.numberOfLayers = numberOfLayers;
        this.numberOfNeuronsInEachLayer = numberOfNeuronsInEachLayer;
        this.layers = new ArrayList<>();
        this.callback = callback;
        this.init();
    }

    private void init() {
        if (numberOfLayers < 2)
            throw new IllegalArgumentException("Layer number must be greater than 2");
        if (numberOfNeuronsInEachLayer <= 0)
            throw new IllegalArgumentException("Neuron number must be greater than 0");
        // add first layer
        layers.add(new FirstNeuronLayer(numberOfNeuronsInEachLayer, SUM_OFFSET, ACTIVATION_OFFSET, LEARNING_OFFSET));
        // add inner layers in loop
        for (int i = 0; i < numberOfLayers - 2; i++) {
            layers.add(new NeuronLayer(numberOfNeuronsInEachLayer, SUM_OFFSET, ACTIVATION_OFFSET, LEARNING_OFFSET));
        }
        // add last layer
        layers.add(new LastNeuronLayer(numberOfNeuronsInEachLayer, SUM_OFFSET, ACTIVATION_OFFSET, LEARNING_OFFSET));
    }

    private void changeState(Neuron.STATE state) {
        for (NeuronLayer nl : layers) {
            nl.setState(state);
        }
    }

    public DataVector generateOutput(DataVector input) {
        double[] lastData;
        // pobieramy output z pierwszej warstwy
        FirstNeuronLayer fnl = (FirstNeuronLayer) layers.get(0);
        lastData = fnl.generateOutput(input.getData());
        for(int i = 1; i < numberOfLayers; i++) {
            NeuronLayer nl = layers.get(i);
            lastData = nl.generateOutput(lastData);
        }
        return new DataVector(lastData);
    }


    public double train(double minError, int maxLoops, TrainDataSet trainDataSet) {
        double finalError = Double.MAX_VALUE;
        long logTime = System.currentTimeMillis();
        DataVector finalOutput = null;
        int loopCounter = 0;
        LastNeuronLayer lnl = (LastNeuronLayer) layers.get(numberOfLayers - 1);
        // wrzucamy siec w stan uczenia
        changeState(Neuron.STATE.LEARNING);
        // okej, lecimy, dopoki error sie nie zmniejszy do oczekiwanych rozmiarow
        while (maxLoops < 0 || loopCounter < maxLoops) {
            // bierzemy kolejny losowy zestaw uczacy
            DataVector trainData = trainDataSet.nextRandom();
            // puszczamy raz siec
            finalOutput = this.generateOutput(trainData);
            if (System.currentTimeMillis() - logTime > 1000l){
                callback.onTrainingProgress("Progress ", finalOutput);
                logTime = System.currentTimeMillis();
            }
            // obliczamy blad
            finalError = calculateError(trainData, finalOutput);
            // sprawdzamy warunek wyjscia
            if (finalError <= minError) {
                break;
            }
            // obliczamy bledy dla ostatniej warstwy
            ErrorMatrix lastErrorMatrix = lnl.train(trainData);
            // propagujemy bledy wstecz
            for (int i = numberOfLayers - 2; i >= 0; i-- ){
                lastErrorMatrix = layers.get(i).train(lastErrorMatrix);
            }
        }
        // wrzucamy siec w stan normalny
        changeState(Neuron.STATE.NORMAL);
        // wypisujemy info
        if (finalOutput != null) {
           callback.onFinishTraining("Finished", finalOutput);
        }
        return finalError;
    }

    /**
     * Oblicza namwiekszy blad pomiaru w danym porownaniu
     * d1 > d2
     */
    private double calculateError(DataVector d1, DataVector d2) {
        if (d1.getSize() != d2.getSize()) throw new IllegalArgumentException("d1 has different size than d2");
        double[] diff = new double[d1.getSize()];
        for (int i = 0; i < d1.getSize(); i++) {
            diff[i] = d1.get(i) - d2.get(i);
        }
        double maxError = diff[0];
        for (int i = 0; i < d1.getSize(); i++) {
            if (diff[i] > maxError) {
                maxError = diff[i];
            }
        }
        return maxError;
    }


}
