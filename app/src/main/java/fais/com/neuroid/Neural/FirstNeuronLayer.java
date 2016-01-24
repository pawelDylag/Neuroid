package fais.com.neuroid.Neural;

/**
 * Created by paweldylag on 24/01/16.
 */
public class FirstNeuronLayer extends NeuronLayer {

    private static final int FIRST_LAYER_INPUT_SIZE = 1;

    public FirstNeuronLayer(double sumOffset, double activationOffset, double learningOffset) {
        super(FIRST_LAYER_INPUT_SIZE, sumOffset, activationOffset, learningOffset);
    }

    @Override
    public double[] generateOutput(double[] inputs) {
        double[] outputs = new double[size];
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
}
