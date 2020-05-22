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
    graphData = new BufferedReader(new FileReader("./data/email-Enron.txt"));
    String currentLine = graphData.readLine();
    int n = Integer.parseInt(currentLine.trim());

    Graph graph = new Graph(n, true); // * true: for dir graphs; false: for undir graphs
    while ((currentLine = graphData.readLine()) != null) {
      String[] nodes = currentLine.trim().split("\\s+");
      int src = Integer.parseInt(nodes[0]), dst = Integer.parseInt(nodes[1]);
      graph.addEdge(src, dst);
    }

    graphData.close();
    graph.adjList.forEach((label, node) -> {
      node.color = Color.WHITE;
    });
    graph.adjList.forEach((label, node) -> {
      if (node.color == Color.WHITE) {
        sourceList.add(node);
        ghash = graph.BFS(node, random, hashAlgo);
      }
    });
    graphTag = graph.getCryptoHash((key ^ OPAD) + graph.getCryptoHash((key ^ IPAD) + random + ghash, hashAlgo),
        hashAlgo);

    gVrfy verifier = new gVrfy();
    // System.out.println("sourceList size " + sourceList.size());
    // for(Node node: sourceList)
    // System.out.println(node.label);

    System.out.println("Hash value on sender's side: " + graphTag);
    verifier.gVerify(sourceList, graph, graphTag, ghash);

  }
}