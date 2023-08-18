import java.util.*;

/**
 * A class to calculate the maximum value in a list after applying operations.
 */
class SparseTable {
    int[][] table;

    /**
     * Constructor to build the sparse table.
     *
     * @param arr The input array.
     */
    SparseTable(int[] arr) {
        int n = arr.length;
        int logN = (int) Math.floor(Math.log(n) / Math.log(2)) + 1;
        table = new int[n][logN];

        for (int i = 0; i < n; i++) {
            table[i][0] = arr[i];
        }

        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 0; i + (1 << j) - 1 < n; i++) {
                table[i][j] = Math.max(table[i][j - 1], table[i + (1 << (j - 1))][j - 1]);
            }
        }
    }

    /**
     * Perform a query in the sparse table.
     *
     * @param left  The left index of the range.
     * @param right The right index of the range.
     * @return The maximum value in the specified range.
     */
    int query(int left, int right) {
        int k = (int) Math.floor(Math.log(right - left + 1) / Math.log(2));
        return Math.max(table[left][k], table[right - (1 << k) + 1][k]);
    }
}

/**
 * Main class that solves the problem.
 */
public class Main {
    static int[] arr;
    static int[] lazy;
    static SparseTable st;

    /**
     * Perform an update with lazy propagation in the array.
     *
     * @param left  The left index of the range to update.
     * @param right The right index of the range to update.
     * @param value The value to add to the range.
     */
    static void update(int left, int right, int value) {
        arr[left] += value;
        lazy[left] += value;
        if (right + 1 < arr.length) {
            lazy[right + 1] -= value;
        }
    }

    /**
     * Apply lazy propagation to the array.
     */
    static void applyLazy() {
        for (int i = 1; i < arr.length; i++) {
            lazy[i] += lazy[i - 1];
            arr[i] += lazy[i];
        }
    }

    /**
     * Main method that reads input and solves the problem.
     *
     * @param args Command line arguments (unused).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int X = scanner.nextInt();
        int Y = scanner.nextInt();

        arr = new int[X];
        lazy = new int[X];
        
        for (int i = 0; i < Y; i++) {
            int left = scanner.nextInt() - 1;
            int right = scanner.nextInt() - 1;
            int value = scanner.nextInt();
            update(left, right, value);
        }

        applyLazy();
        st = new SparseTable(arr);

        int max = st.query(0, X - 1);
        System.out.println(max);
    }
}
