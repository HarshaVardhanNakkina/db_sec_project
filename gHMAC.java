import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import dbsec.*;
import gVrfy.*;

public class gHMAC {

  private static List<Node> sourceList = new ArrayList<Node>();
  private static String ghash = "";
  private static String hashAlgo = "SHA-256";
  public static int key = 1234554321;
  public static int random = 1357908642;
  public static int OPAD = Integer.parseInt("36", 16);
  public static int IPAD = Integer.parseInt("5c", 16);
  public static String graphTag = "";

  public static void main(String[] args) throws Exception {
    BufferedReader graphData = null;
    graphData = new BufferedReader(new FileReader("./data/email-Enron.txt"));
    String currentLine = graphData.readLine();
    int n = Integer.parseInt(currentLine.trim());

    Graph graph = new Graph(n, false); // * true: for dir graphs; false: for undir graphs
    while ((currentLine = graphData.readLine()) != null) {
      String[] nodes = currentLine.trim().split("\\s+");
      int src = Integer.parseInt(nodes[0]), dst = Integer.parseInt(nodes[1]);
      graph.addEdge(src, dst);
    }

    graphData.close();
    graph.adjList.forEach((label, node) -> {
      if (node.color == Color.WHITE) {
        sourceList.add(node);
        ghash = graph.BFS(node, random, hashAlgo);
      }
    });
    graphTag = graph.getCryptoHash((key ^ OPAD) + graph.getCryptoHash((key ^ IPAD) + random + ghash, hashAlgo),
        hashAlgo);
    gVrfy verifier = new gVrfy();

    System.out.println("Hash value on sender's side: " + graphTag);
    verifier.gVerify(sourceList, graph, graphTag, ghash);

  }
}