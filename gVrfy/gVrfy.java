package gVrfy;

import java.util.*;
import dbsec.*;

public class gVrfy {

  public static String ghash = "";
  public static String hashAlgo = "SHA-256";
  public static int key = 1234554321;
  public static int random = 741852963;
  public static int OPAD = Integer.parseInt("36", 16);
  public static int IPAD = Integer.parseInt("5c", 16);
  public static String graphTag = "";

  public void gVerify(List<Node> sourceList, Graph g, String gTag, String recv_ghash) {
    // * RESET THE COLORS FIRST
    System.out.println("\n============Verifying============");

    g.adjList.forEach((label, node) -> {
      node.color = Color.WHITE;
      // if(label == 1) //* To verify fail-warn/stop mechanism
      // node.label = 5;
    });
    // System.out.println("sourceList size " + sourceList.size());
    // for(Node node: sourceList)
    // System.out.println(node.label);
    try {
      for (Node node : sourceList) {
        if (node.color == Color.WHITE)
          ghash = g.BFSVrfy(node, random, hashAlgo);
      }
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
  }
}