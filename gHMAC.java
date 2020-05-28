import java.util.*;
import java.io.*;

import dbsec.*;
import gVrfy.*;

public class gHMAC {

  public static List<Node> sourceList = new ArrayList<Node>();
  public static String ghash = "";
  public static String hashAlgo = "SHA-256";
  public static int key = 1234554321;
  public static int random = 741852963;
  public static int OPAD = Integer.parseInt("36", 16);
  public static int IPAD = Integer.parseInt("5c", 16);
  public static String graphTag = "";

  public static void main(String[] args) throws Exception {
    BufferedReader graphData = null;
    graphData = new BufferedReader(new FileReader("./data/sample.txt"));
    String currentLine = graphData.readLine();
    int n = Integer.parseInt(currentLine.trim());

    // * param 1 => true: for dir graphs; false: for undir graphs
    // * param 2 => false: to fail-stop; true: fail-warn (default is false)
    Graph graph = new Graph(false, true);
    while ((currentLine = graphData.readLine()) != null) {
      String[] nodes = currentLine.trim().split("\\s+");
      int src = Integer.parseInt(nodes[0]), dst = Integer.parseInt(nodes[1]);
      if (src != dst)
        graph.addEdge(src, dst);
    }
    graphData.close();

    graph.adjList.forEach((label, node) -> {
      node.color = Color.WHITE;
    });
    System.out.println("============Calculating============\n");

    graph.adjList.forEach((label, node) -> {
      if (node.color == Color.WHITE) {
        sourceList.add(node);
        ghash = graph.BFS(node, random, hashAlgo);
      }
    });
    // System.out.println("sourceList size " + sourceList.size());
    // for(Node node: sourceList)
    // System.out.println(node.label);
    graphTag = graph.getCryptoHash((key ^ OPAD) + graph.getCryptoHash((key ^ IPAD) + random + ghash, hashAlgo),
        hashAlgo);
    System.out.println("Hash value on sender's side: " + graphTag);

    gVrfy verifier = new gVrfy();
    long startTime = System.nanoTime();
    verifier.gVerify(sourceList, graph, graphTag, ghash);
    long endTime = System.nanoTime();
    double duration = (endTime - startTime) / 1000000.0 / 1000.0;
    System.out.println(n + "," + duration);

  }
}
