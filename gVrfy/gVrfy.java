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
    System.out.println("\n============Verifying============");
    Random rand = new Random();
    // * RESET THE COLORS FIRST
    g.adjList.forEach((label, node) -> {
      node.color = Color.WHITE;
      // * To verify fail-warn/stop mechanism
      // if (label == 1) {
      //   node.label = 5;
      // }
    });
    // Node x = null;
    // for (Map.Entry<Integer, Node> mapElement : g.adjList.entrySet()) {
    // int key = mapElement.getKey();
    // if(key == 1)
    // x = mapElement.getValue();
    // if (x != null)
    // mapElement.getValue().outList.remove(x);
    // }
    // System.out.println("sourceList size " + sourceList.size());
    // for(Node node: sourceList)
    // System.out.println(node.label);
    // * Now run the BFS using sourceList
    try {
      for (Node node : sourceList) {
        if (node.color == Color.WHITE) {
          ghash = g.BFSVrfy(node, random, hashAlgo);
        }
      }
      if (!ghash.equals(recv_ghash))
        g.warnOrStop("ghash values are not matching, data has been modified");
      graphTag = g.getCryptoHash((key ^ OPAD) + g.getCryptoHash((key ^ IPAD) + random + ghash, hashAlgo), hashAlgo);

      if (!gTag.equals(graphTag))
        g.warnOrStop("graphTag values are not matching, data has been modified");
    } catch (Exception e) {
      System.out.println(e);
    }

    System.out.println("\nHash value on receiver's side: " + graphTag);

    boolean isMatching = gTag.equals(graphTag);
    System.out.println("\n" + isMatching);
    if (isMatching)
      System.out.println("\nHash values are matching, data has not been modified");
    else
      System.out.println("\nHash values are not matching, data has been modified");
  }
}