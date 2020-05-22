package dbsec;

import java.util.*;

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

  public void printGraph() {
    this.adjList.forEach((label, node) -> {
      System.out.print(label);
      for (Node child : node.outList)
        System.out.print("->" + child.label);
      System.out.println();
    });
  }
}