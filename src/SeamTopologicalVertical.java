import edu.princeton.cs.algs4.Stack;

public class SeamTopologicalVertical {

    private final boolean[] marked;
    private final Stack<Integer> reversePost; // vertices in reverse postorder
    private final int width;
    private final int height;

    public SeamTopologicalVertical(int length, int width)
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
            for(int i = 0; i < width; i++) {
                dfs(i);
            }
        } else if (v / width < height - 1) {

            if(v % width != 0) dfs(v + width - 1);

            dfs(v + width);

            if(v % width != width - 1) dfs(v + width + 1);
            
        } else if (v / width == height - 1) {
            marked[marked.length - 1] = true;
        }

        reversePost.push(v);
    }
    public Iterable<Integer> reversePost()
    { return reversePost; }
}