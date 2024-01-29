import edu.princeton.cs.algs4.Stack;

public class SeamTopologicalHorizontal {

    private final boolean[] marked;
    private final Stack<Integer> reversePost; // vertices in reverse postorder
    private final int width;
    private final int height;

    public SeamTopologicalHorizontal(int length, int width)
    {
        reversePost = new Stack<>();
        marked = new boolean[length];
        this.width = width;
        this.height = (length - 2)/ width;

        for (int v = 0; v < length; v++)
            if (!marked[v]) dfs(v);
    }

    private void dfs(int v) {
        if(marked[v]) return;
        marked[v] = true;
        if (v == marked.length - 2) {
            for(int i = 0; i <= (height - 1)*width; i += width) {
                dfs(i);
            }
        } else if (v % width < width - 1) {

            if(v / width != 0) dfs(v + 1 - width);

            dfs(v + 1);

            if(v / width != height - 1) dfs(v + 1 + width);

        } else if (v % width == width - 1) {
            marked[marked.length - 1] = true;
        }

        reversePost.push(v);
    }
    public Iterable<Integer> reversePost()
    { return reversePost; }

}