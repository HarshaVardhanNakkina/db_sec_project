import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import dbsec.*;

public class gHMAC {

  // public static String xor(String s1, String s2) {
  //   StringBuilder sb = new StringBuilder();
  //   for (int i = 0; i < s1.length() && i < s2.length(); i++)
  //     sb.append((char) (s1.charAt(i) ^ s2.charAt(i)));
  //   return sb.toString();
  // }

  // public static String getCryptoHash(String input, String algorithm) {
  //   try {
  //     MessageDigest msgDigest = MessageDigest.getInstance(algorithm);
  //     byte[] inputDigest = msgDigest.digest(input.getBytes());
  //     BigInteger inputDigestBigInt = new BigInteger(1, inputDigest);
  //     String hashtext = inputDigestBigInt.toString(16);
  //     while (hashtext.length() < 32) {
  //       hashtext = "0" + hashtext;
  //     }
  //     return hashtext;
  //   } catch (NoSuchAlgorithmException e) {
  //     throw new RuntimeException(e);
  //   }
  // }

  private static List<Node> sourceList = new ArrayList<Node>();
  private static String ghash = "";
  private static String hashAlgo = "SHA-256";
  public static int key = 1234554321;
  public static int random = 1357908642;
  public static int OPAD = Integer.parseInt("36", 16);
  public static int IPAD = Integer.parseInt("5c", 16);
  public static String graphTag = "";

  // public static String BFS(Node n) {

  //   Queue<Node> q = new LinkedList<Node>();
  //   q.add(n);
  //   n.color = Color.GRAY;
  //   String outXor = n.labelHash = getCryptoHash(Integer.toString(n.label), hashAlgo);

  //   while (!q.isEmpty()) {
  //     int size = q.size();
  //     while (size-- > 0) {
  //       Node u = q.poll();
  //       for (Node child : u.outList) {
  //         if (child.labelHash.isEmpty())
  //           child.labelHash = getCryptoHash(Integer.toString(child.label), hashAlgo);
  //         outXor = xor(outXor, child.labelHash);
  //         if (child.color == Color.WHITE)
  //           q.add(child);
  //       }
  //       u.color = Color.BLACK;
  //       u.hashVal = getCryptoHash(random + outXor + u.label, hashAlgo);
  //       ghash = getCryptoHash(ghash + random + u.hashVal, hashAlgo);
  //     }
  //   }

  //   return ghash;
  // }

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
    graphTag = graph.getCryptoHash((key ^ OPAD) + graph.getCryptoHash((key ^ IPAD) + random + ghash, hashAlgo), hashAlgo);

    System.out.println(graphTag);
  }
}