import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.Perceptron;
import org.neuroph.util.TransferFunctionType;

import java.util.Arrays;

/**
 * Created by asnju on 2017/1/12.
 */
public class PerceptronSample {



    public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {
        for (DataSetRow dataRow : testSet.getRows()) {
            nnet.setInput(dataRow.getInput());
            nnet.calculate();
            double[] networkOutput = nnet.getOutput();
            System.out.println("Input: " + Arrays.toString(dataRow.getInput()));
            System.out.println("Ouput: " + Arrays.toString(networkOutput));
        }
    }

    public static void main(String[] args) {

        DataSet trainingSet = new DataSet(2, 1);

        trainingSet.addRow(new DataSetRow(new double[] {0, 0}, new double[] {0}));
        trainingSet.addRow(new DataSetRow(new double[] {0, 1}, new double[] {0}));
        trainingSet.addRow(new DataSetRow(new double[] {1, 0}, new double[] {0}));
        trainingSet.addRow(new DataSetRow(new double[] {1, 1}, new double[] {1}));

        // create perceptron neural network
        NeuralNetwork myPerceptron = new Perceptron(2, 1);


        // learn the training set
        myPerceptron.learn(trainingSet);
        // test perceptron
        System.out.println("Testing trained perceptron");
        testNeuralNetwork(myPerceptron, trainingSet);

        myPerceptron.save("mySamplePerceptron.nnet");

        // load saved neural network
        NeuralNetwork loadedPerceptron = NeuralNetwork.load("mySamplePerceptron.nnet");

        // set network input
        loadedPerceptron.setInput(1, 1);
        loadedPerceptron.calculate();

        double[] networkOutput = loadedPerceptron.getOutput();

        System.out.println("Test result: " + networkOutput[0]);



    }
}
