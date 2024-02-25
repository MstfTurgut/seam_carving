import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class AcyclicHorizontalSP {

    private final double[] distTo;  // distTo[v] = distance  of shortest s->v path
    private final int[] edgeTo;  // edgeTo[v] = last edge on shortest s->v path
    private final double[] weight;  // weight[v] = weight of a vertex v


    /**
     *
     * Computes shortest paths tree from last vertex to every other vertex in
     * the directed acyclic graph abstraction.
     *
     * @param weight Acyclic digraph abstraction.
     * @param width Width of the image for getting coordinates.
     */
    public AcyclicHorizontalSP(double[] weight, int width) {
        distTo = new double[weight.length];
        edgeTo = new int[weight.length];
        this.weight = Arrays.copyOf(weight, weight.length);

        validateVertex(weight.length - 2);

        for (int v = 0; v < weight.length; v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[weight.length - 2] = 0.0;

        // visit vertices in topological order
        HorizontalTopologicalSort top = new HorizontalTopologicalSort(weight.length, width);

        int height = ((weight.length - 2)/ width);

        for (int v : top.reversePost()) {
            if (v == weight.length - 2) {
                for (int i = 0; i <= (height - 1)*width; i += width) {
                    relax(v, i);
                }
            } else if (v % width < width - 1) {

                if (v / width != 0) relax(v, v + 1 - width);

                relax(v, v + 1);

                if (v / width != height - 1) relax(v, v + 1 + width);

            } else if (v % width == width - 1) {
                relax(v, weight.length - 1);
            }

        }
    }

    // relax an edge , v to w
    private void relax(int v, int w) {
        if (distTo[w] >= distTo[v] + weight[w]) {
            distTo[w] = distTo[v] + weight[w];
            edgeTo[w] = v;
        }
    }


    /**
     * Is there a path from the source vertex to vertex {@code v}?
     *
     * @param v Vertex to check.
     * @return {@code true} if there is a path from the source vertex
     */
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     *
     * @param v Target vertex.
     * @return The shortest path from source vertex to vertex {@code v}
     * as an iterable, and {@code null} if no such path
     */
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

    // throw an IllegalArgumentException unless {@code 0 <= v < weight.length}
    private void validateVertex(int v) {
        int l = weight.length;
        if (v < 0 || v >= l)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (l-1));
    }

}

