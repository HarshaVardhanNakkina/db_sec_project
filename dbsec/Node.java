package dbsec;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

// enum Color {
//     WHITE, GRAY, BLACK
// }

public class Node {

  public Set<Node> outList; // order of traversl of children
  public Set<Node> children;
  public BigInteger labelHash;
  public String hashVal;
  public Color color;
  public int label;

  public Node(int vertex) {
    this.label = vertex;
    this.outList = new HashSet<Node>();
    this.children = new HashSet<Node>();
    this.hashVal = "";
    this.labelHash = BigInteger.ZERO;
  }

  public BigInteger getHash(String algorithm) {
    try {
      String input = Integer.toString(this.label);
      MessageDigest msgDigest = MessageDigest.getInstance(algorithm);
      byte[] inputDigest = msgDigest.digest(input.getBytes(StandardCharsets.UTF_8));
      return new BigInteger(1, inputDigest);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}