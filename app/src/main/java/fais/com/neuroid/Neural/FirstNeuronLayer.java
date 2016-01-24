package fais.com.neuroid.Neural;

import fais.com.neuroid.Neural.Data.ErrorMatrix;

/**
 * Created by paweldylag on 24/01/16.
 */
public class FirstNeuronLayer extends NeuronLayer {

    private static final int FIRST_LAYER_INPUT_SIZE = 1;
    private final int networkSize;

    public FirstNeuronLayer(int networkSize, double sumOffset, double activationOffset, double learningOffset) {
        super(FIRST_LAYER_INPUT_SIZE, sumOffset, activationOffset, learningOffset);
        this.networkSize = networkSize;
    }

    @Override
    public double[] generateOutput(double[] inputs) {
        double[] outputs = new double[networkSize];
        for(int i = 0; i < size; i++) {
            // podly hack - nie chce mi sie zmieniac klasy neurona,
            // wiec laduje w niego tablice o jednym polu ;D
            double[] input = new double [1];
            input[0] = inputs[i];
            outputs[i] = neurons.get(i).generateOutput(input);
        }
        return outputs;
    }

    @Override
    protected void init() {
        for (int i = 0; i < size; i++) {
            neurons.add(new Neuron(FIRST_LAYER_INPUT_SIZE, sumOffset, activationOffset));
        }
    }

    @Override
    public String toString() {
        return "FirstNeuronLayer{}";
    }

    @Override
    public ErrorMatrix train(ErrorMatrix previousLayerErrors) {
        if (previousLayerErrors.getSize() != this.networkSize) throw new IllegalStateException("Previous layer error matrix size has different size.");
        // di = a * f(s) * (1-f(s)) * sum(wi*di)
        ErrorMatrix thisLayerErrors = new ErrorMatrix(size);
        // lecimy po wszystkich neuronach w tej warstwie
        for (int i = 0; i < this.size ; i++) {
            Neuron n = neurons.get(i);
            if (n.getState() != Neuron.STATE.LEARNING) throw new IllegalStateException("Neuron is not in LEARNING state.");
            // di = a * f(s) * (1-f(s)) * sum(wi*di)
            double neuronError = learningOffset * n.getLastOutput() * (1 - n.getLastOutput()) * previousLayerErrors.getSumOfColumn(i);
            // tutaj generujemy sum(wi*di), zeby kolejna warstwa miala latwiej :)
            // i przy okazji updateujemy wage tego neurona
            for (int j = 0; j < size; j++) {
                double sum = neuronError * n.getWeights()[j];
                // set(kolumna dla tego neurona, jego kolejna suma, wartosc tej sumy)
                thisLayerErrors.set(i, j, sum);
                // Ok, jak juz wygenerowalismy wartosc do macierzy bledow dla poprzedniej warstwy, to teraz poprawiamy wage w tej warstwie
                n.addErrorToWeight(j, learningOffset * neuronError * n.getLastInput()[0]);
            }
        }
        return thisLayerErrors;
    }
}
