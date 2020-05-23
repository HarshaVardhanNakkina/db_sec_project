package dbsec;
import java.util.*;

// enum Color {
//     WHITE, GRAY, BLACK
// }

public class Node {

  public Set<Node> outList; // order of traversl of children
  public Set<Node> children;
  public String labelHash;
  public String hashVal;
  public Color color;
  public int label;

  public Node(int vertex) {
    this.label = vertex;
    this.outList = new HashSet<Node>();
    this.children = new HashSet<Node>();
    this.labelHash = "";
  }
}