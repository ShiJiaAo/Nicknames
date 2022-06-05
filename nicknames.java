/**
 * Name: Shi Jiaao
 * Student ID: XXXXXXXXX
 */

import java.io.*;
import java.util.*;

public class nicknames {
  // the Vertex and AVL classes are made with reference to the lecture program
  public static class Vertex {
    Vertex(long value, long count) {
      this.value = value;
      this.count = count;
      this.parent = this.left = this.right = null;
      this.height = 0;
    }

    public Vertex parent, left, right;
    public long value;
    public long count;
    public int height;
    public int size;
  }

  public static class AVL {
    public Vertex root;

    public AVL() {
      root = null;
    }

    public int max(int height1, int height2) {
      if (height1 > height2) {
        return height1;
      }
      return height2;
    }

    public int findHeight(Vertex T) {
      Vertex L = T.left;
      Vertex R = T.right;

      // first assume the 2 vertices are null, hence the -1 in heights
      int heightL = -1;
      int heightR = -1;

      // if not null..
      if (L != null) {
        heightL = L.height;
      }
      if (R != null) {
        heightR = R.height;
      }
      return max(heightL, heightR) + 1;
    }

    public int bf(Vertex T) { // balance factor
      if (T.left != null && T.right != null) {
        return T.left.height - T.right.height;
      }
      if (T.left == null && T.right == null) {
        return 0;
      }
      if (T.left == null) {
        return (-1) - T.right.height;
      }
      return T.left.height - (-1);
    }

    public Vertex search(long key) {
      Vertex Target = search(root, key);
      return Target;
    }

    public Vertex search(Vertex Target, long key) {
      if (Target == null) {
        return null;
      }
      if (Target.value == key) {
        return Target;
      }
      if (Target.value < key) {
        return search(Target.right, key);
      }
      return search(Target.left, key);
    }

    public void insert(long key, long count) {
      root = insert(root, key, count);
    }

    public Vertex insert(Vertex T, long v, long count) {
      if (T == null) {
        return new Vertex(v, count);
      }

      if (T.value < v) {
        T.right = insert(T.right, v, count);
        T.right.parent = T;
      } else {
        T.left = insert(T.left, v, count);
        T.left.parent = T;
      }
      T.height = findHeight(T);

      // balancing..
      if (bf(T) == 2) {
        if (0 <= bf(T.left) && bf(T.left) <= 1) {
          T = rotateRight(T);
          return T;
        }
        if (bf(T.left) == -1) {
          T.left = rotateLeft(T.left);
          T = rotateRight(T);
          return T;
        }
      }
      if (bf(T) == -2) {
        if (-1 <= bf(T.right) && bf(T.right) <= 0) {
          T = rotateLeft(T);
          return T;
        }
        if (bf(T.right) == 1) {
          T.right = rotateRight(T.right);
          T = rotateLeft(T);
          return T;
        }
      }
      return T;
    }

    public Vertex rotateLeft(Vertex T) {
      Vertex R = T.right;
      R.parent = T.parent;
      T.parent = R;
      T.right = R.left;
      if (T.right != null) {
        T.right.parent = T;
      }
      R.left = T;
      T.height = findHeight(T);
      R.height = findHeight(R);
      return R;
    }

    public Vertex rotateRight(Vertex T) {
      Vertex L = T.left;
      L.parent = T.parent;
      T.parent = L;
      T.left = L.right;
      if (T.left != null) {
        T.left.parent = T;
      }
      L.right = T;
      T.height = findHeight(T);
      L.height = findHeight(L);
      return L;
    }
  }

  /* method declarations start */

  /* reads the next integer in the input using bufferedreader, returns the read value */
  public static int readNextInt(BufferedReader br) throws IOException {
    int value;
    String inputReader = br.readLine();
    String[] inputHelper = inputReader.split(" ");
    value = Integer.parseInt(inputHelper[0]);
    return value;
  }

  /* fills the AVL trees with the input, returns the filled AVL array */
  public static AVL[] fillAVL(AVL[] toFill, int numItems, BufferedReader br) throws IOException {
    for (int i = 0; i < numItems; i++) {
      String name = br.readLine();
      int length = name.length();

      // will insert into a total of N AVL trees where N is the length of the name.
      // first inserts the first character of the name into toFill[0], then
      // the first 2 characters of the name into toFill[1], and so on..
      long key = 0;
      for (int j = 0; j < length; j++) {
        key = key * 31 + name.charAt(j);
        Vertex Target = toFill[j].search(key);
        if (Target == null) {
          toFill[j].insert(key, 1);
        } else {
          Target.count += 1;
        }
      }
    }
    return toFill;
  }

  /* sees if there is an entry in the AVL tree for a given key. if found, prints the count of the entry, else prints 0 */
  public static void findMatch(AVL[] fullNames, int nicknameLength, long key, PrintWriter pw) throws IOException {
    // the AVL tree that will be searched depends on the length of the nickname. for example if the nickname's length
    // is 5, tree[5 - 1] will be searched
    Vertex print = fullNames[nicknameLength - 1].search(key);
    if (print == null) {
      pw.println("0");
      return;
    }
    pw.println(print.count);
  }

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

    // setting up 10 AVL trees
    int A = readNextInt(br);
    AVL[] fullNames = new AVL[10];
    for (int i = 0; i < 10; i++) {
      fullNames[i] = new AVL();
    }

    // reading in full names
    fullNames = fillAVL(fullNames, A, br);

    // reading the nicknames and finding if they match with any full names by
    // referencing the AVL trees
    int B = readNextInt(br);
    for (int i = 0; i < B; i++) {
      String nickname = br.readLine();
      int length = nickname.length();
      long key = 0;
      for (int j = 0; j < length; j++) {
        key = key * 31 + nickname.charAt(j);
      }
      findMatch(fullNames, length, key, pw);
    }

    br.close();
    pw.close();
  }
}