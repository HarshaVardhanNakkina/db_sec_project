import java.util.*;
import java.io.*;

enum Color { WHITE, GRAY, BLACK }

class Node {

    public List<Node> outList;//= new ArrayList<Node>();
    public String labelHash;
    public String hashVal;
    public Color color;
    public int label;

    Node(int vertex) {
        this.label = vertex;
        this.color = Color.WHITE;
        this.labelHash = "";
        this.hashVal = "";
        this.outList = new ArrayList<Node>();
    }
}

class Graph {
    List<LinkedList<Node>> adjList;
    int V;
    Graph(int V) {
        this.V = V;
        adjList = new ArrayList<LinkedList<Node>>();
        for (int i = 0; i < V; i++) {
            adjList.set(i) = new LinkedList<Node>();
        }
    }
    public void addEdge(Graph graph,int src, int dst) {
        graph.adjList.get(src).add(new Node(dst));
        graph.adjList.get(dst).add(new Node(src));
    }
}

public class gHMAC {

    public static void main(String[] args) throws Exception {
        BufferedReader graphData = null;
        graphData = new BufferedReader(new FileReader("./data/sample.txt"));

        String currentLine;
        while((currentLine = graphData.readLine()) != null) {
            System.out.println(currentLine);
        }
    }
}
