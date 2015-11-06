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

    public int[][] getConfusionMatrix() {
        // Normalize confusion matrix
        int[] rowTotals = new int[10];
        for (int i = 0; i < rowTotals.length; i++) {
            for (int j = 0; j < 10; j++) {
                rowTotals[i]+=confusionMatrixNonNormalized[i][j];
            }
        }

        int[][] confusionMatrix = new int[10][10]
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                confusionMatrix[i][j] = confusionMatrixNonNormalized[i][j]/rowTotals[i];
            }
        }

        return confusionMatrix;
    }

    // This is the diagonal of the confusionMatrix
    public int[] getClassificationRate() {
        int[][] confusionMatrix = getConfusionMatrix();

        int[] ret = new int[10];
        for (int i = 0; i < 10; i++) {
            ret[i] = confusionMatrix[i][i];
        }

        return ret;
    }
}
