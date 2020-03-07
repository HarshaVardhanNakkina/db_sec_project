import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

enum Color {
    WHITE, GRAY, BLACK
}

class Node {

    public List<Node> outList;// = new ArrayList<Node>();
    public String labelHash;
    public String hashVal;
    public Color color;
    public int label;

    Node(int vertex) {
        this.label = vertex;
        // this.color = Color.WHITE;
        // this.labelHash = "";
        // this.hashVal = "";
        this.outList = new ArrayList<Node>();
    }
}

class Graph {
    List<LinkedList<Node>> adjList;
    int V;
    boolean directed;
    Map<Integer, Node> vertexMap = new HashMap<Integer, Node>();

    Graph(int V, boolean directed) {
        this.V = V;
        this.adjList = new ArrayList<LinkedList<Node>>();
        this.directed = directed;
        for (int i = 0; i < V; i++) {
            this.adjList.add(new LinkedList<Node>());
        }
    }

    void addEdge(int src, int dst) {
        this.adjList.get(src).add(new Node(dst));
        vertexMap.put(src, new Node(src));
        if (!this.directed) {
            this.adjList.get(dst).add(new Node(src));
            vertexMap.put(dst, new Node(dst));
        }
    }

    void printGraph() {
        for (int i = 0; i < this.V; i++) {
            System.out.print(i);
            for (Node child : this.adjList.get(i)) {
                System.out.print("->" + child.label);
            }
            System.out.println();
        }
    }
}

public class gHMAC {

    public static String getCryptoHash(String input, String algorithm) {
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

    public static String xor(String s1, String s2) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s1.length() && i < s2.length(); i++)
            sb.append((char) (s1.charAt(i) ^ s2.charAt(i)));
        return sb.toString();
    }

    public static void initProps(Graph graph) {

        for (int v = 0; v < graph.V; v++) {
            for (Node n : graph.adjList.get(v)) {
                n.color = Color.WHITE;
                n.labelHash = "";
                n.hashVal = "";
            }
            graph.vertexMap.get(v).color = Color.WHITE;
        }

    }

    public static String DFS(Graph g, int v) {
        g.vertexMap.get(v).color = Color.GRAY;
        String outXor = getCryptoHash(Integer.toString(v), "SHA-256");
        g.vertexMap.get(v).labelHash = outXor;

        for (Node child : g.adjList.get(v)) {
            g.vertexMap.get(v).outList.add(child);
            if (child.labelHash.length() == 0)
                child.labelHash = getCryptoHash(Integer.toString(child.label), "SHA-256");

            outXor = xor(outXor, child.labelHash);
        }

    }

    public static void main(String[] args) throws Exception {
        BufferedReader graphData = null;
        Graph graph = new Graph(6, true); // true: for dir graphs; false: for undir graphs

        graphData = new BufferedReader(new FileReader("./data/sample.txt"));

        String currentLine;
        while ((currentLine = graphData.readLine()) != null) {
            String[] nodes = currentLine.split("\\s+");
            graph.addEdge(Integer.parseInt(nodes[0]), Integer.parseInt(nodes[1]));
        }

        graph.printGraph();
        initProps(graph);

        List<Integer> sourceList = new ArrayList<Integer>();
        String ghash;

        for (int v = 0; v < graph.V; v++) {
            if (graph.visited[v] == Color.WHITE) {
                sourceList.add(v);
                ghash = DFS(graph, v);
            }
        }

        // for(int v = 0; v < graph.V; v++) {

        // }

        graphData.close();
    }
}
