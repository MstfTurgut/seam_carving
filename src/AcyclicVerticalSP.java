import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class AcyclicVerticalSP {

    private final double[] distTo;
    private final int[] edgeTo;
    private final double[] weight;

    public AcyclicVerticalSP(double[] weight, int width) {
        distTo = new double[weight.length];
        edgeTo = new int[weight.length];
        this.weight = Arrays.copyOf(weight, weight.length);

        validateVertex(weight.length - 2);

        for (int v = 0; v < weight.length; v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[weight.length - 2] = 0.0;

        // visit vertices in topological order
        SeamTopologicalVertical top = new SeamTopologicalVertical(weight.length, width);

        int height = ((weight.length - 2)/ width);

        for (int v : top.reversePost()) {
            if (v == weight.length - 2) {
                for (int i = 0; i < width; i++) {
                    relax(v, i);
                }
            } else if (v / width < height- 1) {

                if (v % width != 0) relax(v, v + width - 1);
                relax(v, v + width);
                if (v % width != width - 1) relax(v, v + width + 1);

            } else if (v / width == height - 1) {
                relax(v, weight.length - 1);
            }

        }
    }

    private void relax(int v, int w) {
        if (distTo[w] >= distTo[v] + weight[w]) {
            distTo[w] = distTo[v] + weight[w];
            edgeTo[w] = v;
        }
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }


    public Iterable<Integer> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) {
            return null;
        }
        Stack<Integer> path = new Stack<>();
        for (int e = edgeTo[v]; e != weight.length - 2; e = edgeTo[e]) {
            path.push(e);
        }
        return path;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int l = distTo.length;
        if (v < 0 || v >= l)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (l-1));
    }

}

