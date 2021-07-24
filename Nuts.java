import java.awt.Point;
import java.util.HashSet;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner in= new Scanner(System.in);
        int n= in.nextInt();
        int m= in.nextInt();
        String emptyLine= in.nextLine();
        int[][] chocolateBar= new int[m][n];
        HashSet<Integer> rows= new HashSet<>();
        HashSet<Integer> columns= new HashSet<>();
        for (int i= 0; i < m; i++ ) {
            String next_str= in.nextLine();
            int num= 0;
            for (int j= 0; j < n; j++ ) {
                if (next_str.charAt(j) == 'n') {
                    num= 1;
                    rows.add(i);
                    rows.add(i + 1);
                    columns.add(j);
                    columns.add(j + 1);
                } else num= 0;
                if (i == 0 && j == 0) {
                    rows.add(i);
                    columns.add(j);
                    chocolateBar[i][j]= num;
                } else if (i == 0) {
                    chocolateBar[i][j]= chocolateBar[i][j - 1] + num;
                } else if (j == 0) {
                    chocolateBar[i][j]= chocolateBar[i - 1][j] + num;
                } else {
                    chocolateBar[i][j]= chocolateBar[i - 1][j] + chocolateBar[i][j - 1] + num -
                        chocolateBar[i - 1][j - 1];
                }
            }
        }
        rows.add(m);
        columns.add(n);
        int[] row_arr= new int[rows.size()];
        int[] col_arr= new int[columns.size()];
        int[] x_top_left= new int[m];
        int[] y_top_left= new int[n];
        int[] x_bot_right= new int[m + 1];
        int[] y_bot_right= new int[n + 1];
        int r0= 0;
        int c0= 0;
        for (int i : rows) {
            if (i != 0 || i != m) {
                row_arr[r0]= i;
                r0++ ;
            }
        }
        for (int i : columns) {
            if (i != 0 || i != n) {
                col_arr[c0]= i;
                c0++ ;
            }
        }
        int r= 0;
        for (int i : rows) {
            if (i != m) {
                x_top_left[i]= r;
                r++ ;
            }
        }
        int r1= 0;
        for (int i : rows) {
            if (i != 0) {
                x_bot_right[i]= r1;
                r1++ ;
            }
        }
        int r2= 0;
        for (int i : columns) {
            if (i != n) {
                y_top_left[i]= r2;
                r2++ ;
            }
        }
        int r3= 0;
        for (int i : columns) {
            if (i != 0) {
                y_bot_right[i]= r3;
                r3++ ;
            }
        }
        Point p1= new Point(0, 0);
        Point p2= new Point(m, n);
        Point[] initial= { p1, p2 };
        int row_size= rows.size();
        int col_size= columns.size();
        int[][][][] array= new int[row_size + 1][col_size + 1][row_size + 1][col_size + 1];
        for (int i= 0; i <= row_size; i++ ) {
            for (int j= 0; j <= col_size; j++ ) {
                for (int k= 0; k <= row_size; k++ ) {
                    for (int l= 0; l <= col_size; l++ ) {
                        array[i][j][k][l]= -1;
                    }
                }
            }
        }
        long startTime= System.nanoTime();
        // System.out.println(rows.size());
        // System.out.println(columns.size());
        System.out.println(Opt(initial, array, chocolateBar, x_top_left, y_top_left, x_bot_right,
            y_bot_right, row_arr, col_arr));
        long endTime= System.nanoTime();
        long timeElapsed= endTime - startTime;
        // System.out.println(timeElapsed);

    }

    static int numOfNuts(int[][] chocolateBar, Point[] points) {
        Point p1= points[0];
        Point p2= points[1];
        int num2= 0;
        if (p1.getX() == 0 && p1.getY() == 0) {
            num2= chocolateBar[(int) (p2.getX() - 1)][(int) (p2.getY() - 1)];
        } else if (p1.getX() == 0) {
            num2= chocolateBar[(int) (p2.getX() - 1)][(int) (p2.getY() - 1)] -
                chocolateBar[(int) (p2.getX() - 1)][(int) (p1.getY() - 1)];
        } else if (p1.getY() == 0) {
            num2= chocolateBar[(int) (p2.getX() - 1)][(int) (p2.getY() - 1)] -
                chocolateBar[(int) (p1.getX() - 1)][(int) (p2.getY() - 1)];
        } else {
            num2= chocolateBar[(int) (p2.getX() - 1)][(int) (p2.getY() - 1)] -
                chocolateBar[(int) (p2.getX() - 1)][(int) (p1.getY() - 1)] -
                chocolateBar[(int) (p1.getX() - 1)][(int) (p2.getY() - 1)] +
                chocolateBar[(int) (p1.getX() - 1)][(int) (p1.getY() - 1)];
        }
        return num2;
    }

    static int Opt(Point[] points,
        int[][][][] array, int[][] chocolateBar, int[] tlx, int[] tly,
        int[] brx, int[] bry, int[] rows, int[] cols) {
        if (array[tlx[(int) points[0].getX()]][tly[(int) points[0]
            .getY()]][brx[(int) points[1].getX()]][bry[(int) points[1].getY()]] != -1) {
            return array[tlx[(int) points[0].getX()]][tly[(int) points[0]
                .getY()]][brx[(int) points[1].getX()]][bry[(int) points[1].getY()]];
        }
        int numNuts= numOfNuts(chocolateBar, points);
        int totalBoxes= ((int) points[1].getX() - (int) points[0].getX()) *
            ((int) points[1].getY() - (int) points[0].getY());
        if (numNuts == 0 || numNuts == totalBoxes) {
            array[tlx[(int) points[0].getX()]][tly[(int) points[0]
                .getY()]][brx[(int) points[1].getX()]][bry[(int) points[1].getY()]]= 0;
            return 0;
        } else {
            int rowMin= Integer.MAX_VALUE;
            for (int p_row : rows) {
                if (p_row > points[0].getX() && p_row < points[1].getX()) {
                    Point p1_row= new Point(p_row, (int) points[1].getY());
                    Point p2_row= new Point(p_row,
                        (int) points[0].getY());
                    Point[] row_pointsTop= { points[0], p1_row };
                    Point[] row_pointsBottom= { p2_row, points[1] };
                    int row= Opt(row_pointsTop, array, chocolateBar, tlx, tly,
                        brx, bry, rows, cols) +
                        Opt(row_pointsBottom, array, chocolateBar, tlx, tly,
                            brx, bry, rows, cols);
                    if (rowMin > row) {
                        rowMin= row;
                    }
                }
            }
            int colMin= Integer.MAX_VALUE;
            for (int p_col : cols) {
                if (p_col > points[0].getY() && p_col < points[1].getY()) {
                    Point p1_col= new Point((int) points[1].getX(), p_col);
                    Point p2_col= new Point((int) points[0].getX(), p_col);
                    Point[] col_pointsLeft= { points[0], p1_col };
                    Point[] col_pointsRight= { p2_col, points[1] };
                    int col= Opt(col_pointsLeft, array, chocolateBar, tlx, tly,
                        brx, bry, rows, cols) +
                        Opt(col_pointsRight, array, chocolateBar, tlx, tly,
                            brx, bry, rows, cols);
                    if (colMin > col) {
                        colMin= col;
                    }
                }
            }
            int totalMin= rowMin;
            if (colMin < rowMin) {
                totalMin= colMin;
            }
            array[tlx[(int) points[0].getX()]][tly[(int) points[0]
                .getY()]][brx[(int) points[1].getX()]][bry[(int) points[1]
                    .getY()]]= totalMin + 1;
            return totalMin + 1;

        }
    }
}
