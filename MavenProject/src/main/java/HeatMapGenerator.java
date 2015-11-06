import javafx.util.Pair;
import org.tc33.jheatchart.HeatChart;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

class ConfusionComparator implements Comparator<Pair<Integer,Integer>>{
    double[][] confusionMatrix;

    public ConfusionComparator(double[][] confusionMatrix) {
        this.confusionMatrix = confusionMatrix;
    }

    // Overriding the compare method to sort the age
    public int compare(Pair<Integer, Integer> first, Pair<Integer, Integer> second){
        double one = confusionMatrix[first.getKey()][first.getValue()];
        double two = confusionMatrix[second.getKey()][second.getValue()];
        if (one > two) return -1;
        if (one < two) return 1;
        return 0;
    }
}

public class HeatMapGenerator {

    public HeatMapGenerator(AccuracyStats stats, ArrayList<double[][]> likelihoods) throws IOException {
        double[][] confusionMatrix = stats.getConfusionMatrix();
        PriorityQueue<Pair<Integer,Integer>> pq = new PriorityQueue<Pair<Integer,Integer>>(10*10, new ConfusionComparator(confusionMatrix));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                // don't include diagonal
                if (i != j) {
                    pq.add(new Pair<Integer, Integer>(i, j));
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            Pair<Integer,Integer> indices = pq.poll();
            int r = indices.getKey();
            int c = indices.getValue();
            System.out.println(i + " Using r = " + r + " c = " + c);

            generateHeatMap(likelihoods.get(r), "Heat chart for r=" + r, "r=" + r + "-heat-chart.png");
            generateHeatMap(likelihoods.get(c), "Heat chart for c=" + c, "c=" + c + "-heat-chart.png");

            double[][] odds = getSpecifiedOdd(likelihoods.get(r), likelihoods.get(c);
            generateHeatMap(odds, "Heat chart for odds of r=" + r + " vs c=" + c, r + "-" + c + "-odds-heat-chart.png");
        }
    }

    private void generateHeatMap(double[][] data, String title, String filename) throws IOException {
        HeatChart map = new HeatChart(data);

        map.setTitle(title);
        map.setHighValueColour(Color.red);
        map.setLowValueColour(Color.blue);

        map.saveToFile(new File(filename));
    }
}
