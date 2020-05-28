package dbsec;

import java.util.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Graph {
  public HashMap<Integer, Node> adjList;
  public boolean directed;
  private static boolean warn; // by default warn is false

  public Graph(boolean directed) {
    this.adjList = new HashMap<Integer, Node>();
    this.directed = directed;
    warn = false;
  }

  public Graph(boolean directed, boolean w) {
    this.adjList = new HashMap<Integer, Node>();
    this.directed = directed;
    warn = w;
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
        this.adjList.get(dst).children.add(this.adjList.get(src));
  }

  public String BFS(Node n, int random, String hashAlgo) {
    String gHash = "hymn_for_the_weekend";
    LinkedList<Node> q = new LinkedList<Node>();
    n.color = Color.GRAY;
    q.offer(n);
    // BigInteger outXor = n.labelHash = n.getHash(hashAlgo);
    while (!q.isEmpty()) {
      int size = q.size();
      while (size-- > 0) {
        Node u = q.poll();
        // outXor = u.getHash(hashAlgo);
        BigInteger outXor = u.labelHash = u.getHash(hashAlgo);
        for (Node child : u.children) {
          u.outList.add(child);
          if (child.labelHash == null)
            child.labelHash = child.getHash(hashAlgo);
          outXor = outXor.xor(child.labelHash);
          if (child.color == Color.WHITE) {
            child.color = Color.GRAY;
            q.offer(child);
          }
        }
        u.color = Color.BLACK;
        u.hashVal = getCryptoHash(random + outXor.toString() + u.label, hashAlgo);
        gHash = getCryptoHash(gHash + random + u.hashVal, hashAlgo);
      }
    }

    return gHash;
  }

  public void warnOrStop(String msg) throws Exception {
    if (warn) {
      System.out.println(msg);
    } else
      throw new Exception(msg);
  }

  public String BFSVrfy(Node n, int random, String hashAlgo) throws Exception {
    // * IMPLEMENTS THE FAIL-STOP / FAIL-WARN MECHANISM
    String gHash = "hymn_for_the_weekend";
    LinkedList<Node> q = new LinkedList<Node>();
    n.color = Color.GRAY;
    q.offer(n);
    // BigInteger outXor = n.getHash(hashAlgo);
    // if (!outXor.equals(n.labelHash))
    // warnOrStop(n.label + "'s label hash is not matching, data has been modified:
    // initial outXor");

    while (!q.isEmpty()) {
      int size = q.size();
      while (size-- > 0) {
        Node u = q.poll();
        BigInteger outXor = u.getHash(hashAlgo);
        if (!outXor.equals(u.labelHash))
          warnOrStop(u.label + "'s label hash is not matching, data has been modified: initial outXor");

        for (Node child : u.outList) {
          if (child.labelHash == null)
            child.labelHash = child.getHash(hashAlgo);
          BigInteger childCalcHash = child.getHash(hashAlgo);

          if (!childCalcHash.equals(child.labelHash))
            warnOrStop(child.label + "'s label hash is not matching, data has been modified: children traversal");
          outXor = outXor.xor(childCalcHash);
          if (child.color == Color.WHITE) {
            child.color = Color.GRAY;
            q.offer(child);
          }
        }
        u.color = Color.BLACK;
        String calcHashVal = getCryptoHash(random + outXor.toString() + u.label, hashAlgo);
        if (!u.hashVal.equals(calcHashVal))
          warnOrStop(u.label + "'s Hashval is not matching, data has been modified");
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