import java.util.*;
import java.io.*;

public class genData {
  public static void main(String[] args) throws IOException {
    int n = 30000;
		FileWriter fw = new FileWriter("./data/" + n +".txt", false);
		Random rand = new Random();
		fw.write(String.valueOf(n) + "\n");
		for (int i = 0; i < n; i++) {
      int edges = rand.nextInt(50) + 1;
      for (int j = 0; j < edges; j++)
        fw.write(i + " " + rand.nextInt(n) + "\n");
		}
		fw.close();
  }
}