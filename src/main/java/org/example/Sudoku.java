package org.example;

import org.apache.commons.lang3.SerializationUtils;

import java.util.*;

/**
 * @author liutao
 * @date 2023/10/16
 */
public class Sudoku {
    private static final int COUNT = 9; //9*9数独
    private static final ArrayList<Integer> list = new ArrayList<>() {{ //标准
        add(1);
        add(2);
        add(3);
        add(4);
        add(5);
        add(6);
        add(7);
        add(8);
        add(9);
    }};

    private static int[][] result = new int[COUNT][COUNT]; //数独结果

    private static boolean found = false; //是否找到数独

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ArrayList<ArrayList<Integer>> lists = new ArrayList<>() {{
            for (int i = 0; i < COUNT; i++) {
                add(new ArrayList<>(list));
            }
        }};
        int[][] sudoku = HandleInput(lists);
        Process(sudoku, 0, 0, lists);
        if (found) {
            for (int[] ints : result) {
                for (int j = 0; j < COUNT; j++) {
                    System.out.print(ints[j] + " ");
                }
                System.out.println();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("总耗时：" + (end - start) + "ms");
        return;
    }


    /**
     * 处理输入，形成数独数组
     *
     * @return {@link int[][]}
     */
    public static int[][] HandleInput(ArrayList<ArrayList<Integer>> lists) {
        int[][] sudoku = new int[COUNT][COUNT];
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < COUNT; i++) {
            String s = scanner.next();
            for (int j = 0; j < s.length(); j++) {
                if ('.' != s.charAt(j)) {
                    sudoku[i][j] = Integer.parseInt(String.valueOf(s.charAt(j)));
                    lists.get(i).remove((Integer) sudoku[i][j]);
                } else {
                    sudoku[i][j] = 0;
                }
            }
        }
        return sudoku;
    }

    /**
     * 回溯算法处理数独主流程
     *
     * @param sudoku
     * @param i
     * @param j
     * @param lists
     */
    public static void Process(int[][] sudoku, int i, int j, ArrayList<ArrayList<Integer>> lists) {
        if (found) {
            return;
        }
        if (i == COUNT) {
            if (Check(sudoku, COUNT - 1, COUNT - 1)) {
                found = true;
                result = sudoku;
            }
            return;
        }
        if (sudoku[i][j] != 0) { //该位置已被提前指定则跳过
            if (Check(sudoku, i, j)) {
                Process(sudoku, j + 1 < COUNT ? i : i + 1, j + 1 < COUNT ? j + 1 : 0, lists);
            } else {
                return;
            }
        }
        for (int k = 1; k <= COUNT; k++) {
            if (!lists.get(i).contains(k)) { //该数字在该行已被占用则换下一个数字
                continue;
            }
            ArrayList<ArrayList<Integer>> tempList = new ArrayList<>();
            tempList = SerializationUtils.clone(lists);
            int[][] tempSudoku = CopyArray(sudoku);
            tempList.get(i).remove((Integer) k);
            tempSudoku[i][j] = k;
            if (i < 1 || Check(tempSudoku, i, j)) {
                Process(tempSudoku, j + 1 < COUNT ? i : i + 1, j + 1 < COUNT ? j + 1 : 0, tempList);
            }
        }
    }

    /**
     * 复制二维数组
     *
     * @param a
     * @return {@link int[][]}
     */
    public static int[][] CopyArray(int[][] a) {
        int[][] b = new int[COUNT][COUNT];
        for (int i = 0; i < b.length; i++) {
            System.arraycopy(a[i], 0, b[i], 0, b.length);
        }
        return b;
    }

    /**
     * 检查当前矩阵是否满足数独要求
     *
     * @return boolean
     */
    public static boolean Check(int[][] sudoku, int Row, int Column) {
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < COUNT; i++) { //按列检查
            if (sudoku[i][Column] == 0) {
                break;
            }
            if (set.contains(sudoku[i][Column])) {
                return false;
            }
            set.add(sudoku[i][Column]);
        }
        return CheckByBlock(sudoku, (Row / 3) * 3, (Row / 3) * 3 + 3
                , (Column / 3) * 3, (Column / 3) * 3 + 3);
    }

    /**
     * 按块检查
     *
     * @param sudoku
     * @param minR
     * @param maxR
     * @param minC
     * @param maxC
     * @return boolean
     */
    public static boolean CheckByBlock(int[][] sudoku, int minR, int maxR, int minC, int maxC) {
        if (minR == COUNT) {
            return true;
        }
        HashSet<Integer> set = new HashSet<>();
        for (int i = minR; i < maxR; i++) { //按块检查
            for (int j = minC; j < maxC; j++) {
                if (sudoku[i][j] == 0) {
                    return true;
                }
                if (set.contains(sudoku[i][j])) {
                    return false;
                }
                set.add(sudoku[i][j]);
            }
        }
        return true;
    }
}

//8........
//..36.....
//.7..9.2..
//.5...7...
//....457..
//...1...3.
//..1....68
//..85...1.
//.9....4..