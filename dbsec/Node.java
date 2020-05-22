package dbsec;
import java.util.*;

// enum Color {
//     WHITE, GRAY, BLACK
// }

public class Node {

  public List<Node> outList; // order of traversl of children
  public List<Node> children;
  public String labelHash;
  public String hashVal;
  public Color color;
  public int label;

  public Node(int vertex) {
    this.label = vertex;
    this.outList = new ArrayList<Node>();
    this.children = new ArrayList<Node>();
    this.labelHash = "";
  }
}