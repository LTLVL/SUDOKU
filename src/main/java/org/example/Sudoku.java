package org.example;
import org.apache.commons.lang3.SerializationUtils;

import java.util.*;

/**
 * @author liutao
 * @date 2023/10/16
 */
public class Sudoku {
    private static final int COUNT = 9; //9*9数独
    private static final ArrayList<Integer> list = new ArrayList<>() {{
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

    private static ArrayList<int[][]> result = new ArrayList<>();


    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> lists = new ArrayList<>() {{
            for (int i = 0; i < COUNT; i++) {
                add(new ArrayList<>(list));
            }
        }};
        int[][] sudoku = HandleInput(lists);
        Process(sudoku, 0, 0, lists);
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

    public static void Process(int[][] sudoku, int i, int j, ArrayList<ArrayList<Integer>> lists) {
        if (i == COUNT) {
            if (Check(sudoku)) {
                result.add(sudoku);
            }
            return;
        }
        if (sudoku[i][j] != 0) { //该位置已被指定则跳过
            Process(sudoku, j + 1 < COUNT ? i : i + 1, j + 1 < COUNT ? j + 1 : 0, lists);
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
            Process(tempSudoku, j + 1 < COUNT ? i : i + 1, j + 1 < COUNT ? j + 1 : 0, tempList);
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
    public static boolean Check(int[][] sudoku) {
        for (int i = 0; i < COUNT; i++) { //按列检查
            HashSet<Integer> set = new HashSet<>(list);
            for (int j = 0; j < COUNT; j++) {
                if (sudoku[j][i] == 0) {
                    continue;
                }
                if (set.contains(sudoku[j][i])) {
                    set.remove(sudoku[j][i]);
                    continue;
                }
                return false;
            }
        }

//        for (int i = 0; i < COUNT; i++){ //按块检查
//            HashSet<Integer> set = new HashSet<>(list);
//            for (int j = 0; j < COUNT; j++) {
//                if(sudoku[i][j] == 0){
//                    continue;
//                }
//                if(set.contains(sudoku[i][j])){
//                    set.remove(sudoku[i][j]);
//                    continue;
//                }
//                return false;
//            }
//        }

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