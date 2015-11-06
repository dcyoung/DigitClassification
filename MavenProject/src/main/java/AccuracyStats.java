import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nprince on 11/6/15.
 */
public class AccuracyStats {

    private int[][] confusionMatrixNonNormalized;

    public AccuracyStats() {
        this.confusionMatrixNonNormalized = new int[10][10];
    }

    public void addDatapoint(int actual, int classifiedAs) {
        this.confusionMatrixNonNormalized[actual][classifiedAs]++;
    }

    public double[][] getConfusionMatrix() {
        // Normalize confusion matrix
        int[] rowTotals = new int[10];
        for (int i = 0; i < rowTotals.length; i++) {
            for (int j = 0; j < 10; j++) {
                rowTotals[i]+=confusionMatrixNonNormalized[i][j];
            }
        }

        double[][] confusionMatrix = new double[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                confusionMatrix[i][j] = 1.0 * confusionMatrixNonNormalized[i][j]/rowTotals[i];
            }
        }

        return confusionMatrix;
    }

    // This is the diagonal of the confusionMatrix
    public double[] getClassificationRates() {
        double[][] confusionMatrix = getConfusionMatrix();

        double[] ret = new double[10];
        for (int i = 0; i < 10; i++) {
            ret[i] = confusionMatrix[i][i];
        }

        return ret;
    }
    // average
    public double getAverageClassificationRate() {
        double[] classificationRates =  this.getClassificationRates();
        double sum = 0;
        for (int i = 0; i < classificationRates.length; i++) {
        	sum += classificationRates[i];
        }

        return sum/classificationRates.length;
    }
    

    public void printConfusionMatrix() {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        double[][] confusionMatrix = getConfusionMatrix();

        System.out.println();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(df.format(confusionMatrix[i][j]));
                System.out.print(", ");
            }

            System.out.println();
        }
    }

    public void printClassificationRate() {

    }
}
