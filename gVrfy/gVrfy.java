package gVrfy;

import java.util.*;
import dbsec.*;

public class gVrfy {

  public static String ghash = "";
  public static String hashAlgo = "SHA-256";
  public static int key = 1234554321;
  public static int random = 1357908642;
  public static int OPAD = Integer.parseInt("36", 16);
  public static int IPAD = Integer.parseInt("5c", 16);
  public static String graphTag = "";

  public boolean gVerify(List<Node> sourceList, Graph g, String gTag, String recv_ghash) {
    // * RESET THE COLORS FIRST
    System.out.println("\n============Verifying============");

    g.adjList.forEach((label, node) -> {
      node.color = Color.WHITE;
    });
    try {
      for (Node node : sourceList) {
        if (node.color == Color.WHITE) {
          try {
            ghash = g.BFSVrfy(node, random, hashAlgo);
          } catch (Exception e) {
            System.out.println(e);
          }
        }
      }
      ;
      if (!ghash.equals(recv_ghash))
        throw new Exception("ghash values are not matching, data has been modified");
      graphTag = g.getCryptoHash((key ^ OPAD) + g.getCryptoHash((key ^ IPAD) + random + ghash, hashAlgo), hashAlgo);

      if (!gTag.equals(graphTag))
        throw new Exception("graphTag values are not matching, data has been modified");
    } catch (Exception e) {
      System.out.println(e);
    }
    System.out.println("Hash value on receiver's side: " + graphTag);
    boolean isMatching = gTag.equals(graphTag);
    System.out.println("\n" + isMatching);
    if (isMatching)
      System.out.println("\nHash values are matching, data has not been modified");
    else
      System.out.println("\nHash values are not matching, data has been modified");

    return isMatching;
  }

  public static void main(String[] args) {
    System.out.println("\n============Verifying the graph============");
  }
}