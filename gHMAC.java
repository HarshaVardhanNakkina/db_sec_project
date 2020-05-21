import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Node {

  public List<Node> outList; // children
  public String labelHash;
  public String hashVal;
  public Color color;
  public int label;

  Node(int vertex) {
    this.label = vertex;
    this.outList = new ArrayList<Node>();
  }
}

class Graph {
  HashMap<Integer, Node> adjList;
  int V;
  boolean directed;

  Graph(int V, boolean directed) {
    this.V = V;
    this.adjList = new HashMap<Integer, Node>();
    this.directed = directed;
  }

  void addEdge(int src, int dst) {
    if (!this.adjList.containsKey(src))
      this.adjList.put(src, new Node(src));
    if (!this.adjList.containsKey(dst))
      this.adjList.put(dst, new Node(dst));

    this.adjList.get(src).outList.add(this.adjList.getOrDefault(dst, new Node(dst)));
    if (!this.directed)
      this.adjList.get(dst).outList.add(this.adjList.getOrDefault(src, new Node(src)));
  }

  void printGraph() {
    this.adjList.forEach((label, node) -> {
      System.out.print(label);
      for (Node child : node.outList)
        System.out.print("->" + child.label);
      System.out.println();
    });
  }
}

public class gHMAC {
  public static void main(String[] args) throws Exception {
    BufferedReader graphData = null;
    graphData = new BufferedReader(new FileReader("./data/sample.txt"));
    String currentLine = graphData.readLine();
    int n = Integer.parseInt(currentLine.trim());

    Graph graph = new Graph(n, false); //* true: for dir graphs; false: for undir graphs
    while ((currentLine = graphData.readLine()) != null) {
      String[] nodes = currentLine.trim().split("\\s+");
      int src = Integer.parseInt(nodes[0]), dst = Integer.parseInt(nodes[1]);
      graph.addEdge(src, dst);
    }

    graphData.close();
    graph.printGraph();
  }
}