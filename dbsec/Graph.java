package dbsec;

import java.util.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
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

    if (!this.adjList.get(src).children.contains(this.adjList.get(dst)))
      this.adjList.get(src).children.add(this.adjList.get(dst));

    if (!this.directed)
      if (!this.adjList.get(dst).children.contains(this.adjList.get(src)))
        this.adjList.get(dst).children.add(this.adjList.getOrDefault(src, new Node(src)));
  }

  public String BFS(Node n, int random, String hashAlgo) {
    String gHash = "hymn_for_the_weekend";
    LinkedList<Node> q = new LinkedList<Node>();
    q.offer(n);
    n.color = Color.GRAY;
    String outXor = n.labelHash = getCryptoHash(Integer.toString(n.label), hashAlgo);

    while (!q.isEmpty()) {
      int size = q.size();
      while (size-- > 0) {
        Node u = q.poll();
        for (Node child : u.children) {
          u.outList.add(child);
          if (child.labelHash.isEmpty())
            child.labelHash = getCryptoHash(Integer.toString(child.label), hashAlgo);
          outXor = getCryptoHash(outXor + child.labelHash, hashAlgo); // xor function can also be
          if (child.color == Color.WHITE) {
            child.color = Color.GRAY;
            q.offer(child);
          }
        }
        u.color = Color.BLACK;
        u.hashVal = getCryptoHash(random + outXor + u.label, hashAlgo);
        gHash = getCryptoHash(gHash + random + u.hashVal, hashAlgo);
      }
    }

    return gHash;
  }

  public String BFSVrfy(Node n, int random, String hashAlgo) throws Exception {
    // * IMPLEMENTS THE FAIL-STOP / FAIL-WARN MECHANISM
    String gHash = "hymn_for_the_weekend";
    LinkedList<Node> q = new LinkedList<Node>();
    q.offer(n);
    n.color = Color.GRAY;
    String outXor = getCryptoHash(Integer.toString(n.label), hashAlgo);

    if (!outXor.equals(n.labelHash))
      throw new Exception(n.label + "'s label hash is not matching, data has been modified: initial outXor");

    while (!q.isEmpty()) {
      int size = q.size();
      while (size-- > 0) {
        Node u = q.poll();
        for (Node child : u.outList) {
          String childCalcHash = getCryptoHash(Integer.toString(child.label), hashAlgo);
          if (!childCalcHash.equals(child.labelHash))
            throw new Exception(
                child.label + "'s label hash is not matching, data has been modified: children traversal");
          outXor = getCryptoHash(outXor + childCalcHash, hashAlgo); // xor function can also be
          if (child.color == Color.WHITE) {
            child.color = Color.GRAY;
            q.offer(child);
          }
        }
        u.color = Color.BLACK;
        String calcHashVal = getCryptoHash(random + outXor + u.label, hashAlgo);
        if (!u.hashVal.equals(calcHashVal))
          throw new Exception(u.label + "'s Hashval is not matching, data has been modified");
        gHash = getCryptoHash(gHash + random + calcHashVal, hashAlgo);
      }
    }
    return gHash;
  }

  public void printGraph() {
    this.adjList.forEach((label, node) -> {
      System.out.print(node.label);
      for (Node child : node.children)
        System.out.print("->" + child.label);
      System.out.println();
    });
  }

  public void printHashvals() {
    this.adjList.forEach((label, node) -> {
      System.out.println(label + "->" + node.labelHash);
    });
  }

  public String xor(String s1, String s2) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s1.length() && i < s2.length(); i++)
      sb.append((char) (s1.charAt(i) ^ s2.charAt(i)));
    return sb.toString();
  }

  private static String bytesToHex(byte[] hash) {
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < hash.length; i++) {
      String hex = Integer.toHexString(0xff & hash[i]);
      if (hex.length() == 1)
        hexString.append('0');
      hexString.append(hex);
    }
    return hexString.toString();
  }

  public String getCryptoHash(String input, String algorithm) {
    try {
      MessageDigest msgDigest = MessageDigest.getInstance(algorithm);
      byte[] inputDigest = msgDigest.digest(input.getBytes(StandardCharsets.UTF_8));
      String hashtext = bytesToHex(inputDigest);
      // BigInteger inputDigestBigInt = new BigInteger(1, inputDigest);
      // String hashtext = inputDigestBigInt.toString(16);
      // while (hashtext.length() < 32) {
      // hashtext = "0" + hashtext;
      // }
      return hashtext;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}