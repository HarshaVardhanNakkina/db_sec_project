package dbsec;

import java.util.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Graph {
  public HashMap<Integer, Node> adjList;
  int V;
  public boolean directed;

  public Graph(int V, boolean directed) {
    this.V = V;
    this.adjList = new HashMap<Integer, Node>();
    this.directed = directed;
  }

  public void addEdge(int src, int dst) {
    if (!this.adjList.containsKey(src))
      this.adjList.put(src, new Node(src));
    if (!this.adjList.containsKey(dst))
      this.adjList.put(dst, new Node(dst));

    this.adjList.get(src).outList.add(this.adjList.getOrDefault(dst, new Node(dst)));
    if (!this.directed)
      this.adjList.get(dst).outList.add(this.adjList.getOrDefault(src, new Node(src)));
  }

  public String BFS(Node n, int random, String hashAlgo) {
    String ghash = "";
    Queue<Node> q = new LinkedList<Node>();
    q.add(n);
    n.color = Color.GRAY;
    String outXor = n.labelHash = getCryptoHash(Integer.toString(n.label), hashAlgo);

    while (!q.isEmpty()) {
      int size = q.size();
      while (size-- > 0) {
        Node u = q.poll();
        for (Node child : u.outList) {
          if (child.labelHash.isEmpty())
            child.labelHash = getCryptoHash(Integer.toString(child.label), hashAlgo);
          outXor = xor(outXor, child.labelHash);
          if (child.color == Color.WHITE)
            q.add(child);
        }
        u.color = Color.BLACK;
        u.hashVal = getCryptoHash(random + outXor + u.label, hashAlgo);
        ghash = getCryptoHash(ghash + random + u.hashVal, hashAlgo);
      }
    }

    return ghash;
  }

  public void printGraph() {
    this.adjList.forEach((label, node) -> {
      System.out.print(label);
      for (Node child : node.outList)
        System.out.print("->" + child.label);
      System.out.println();
    });
  }

  public String xor(String s1, String s2) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s1.length() && i < s2.length(); i++)
      sb.append((char) (s1.charAt(i) ^ s2.charAt(i)));
    return sb.toString();
  }

  public String getCryptoHash(String input, String algorithm) {
    try {
      MessageDigest msgDigest = MessageDigest.getInstance(algorithm);
      byte[] inputDigest = msgDigest.digest(input.getBytes());
      BigInteger inputDigestBigInt = new BigInteger(1, inputDigest);
      String hashtext = inputDigestBigInt.toString(16);
      while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
      }
      return hashtext;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}