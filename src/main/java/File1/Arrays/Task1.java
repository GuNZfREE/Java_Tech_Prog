package File1.Arrays;

import java.util.Scanner;

public class Task1 {

    static void printNumbers(int[] a, boolean pos) {
        for (int i : a){
            if (pos && i % 2 == 0)
                System.out.print(" " + i);
            else if (!pos && i % 2 != 0) {
                System.out.print(" " + i);
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Введите количество элементов: ");
            int N = scanner.nextInt();
            int[] array = new int[N];
            for (int i = 0; i < N; i++) {
                System.out.print("Введите число: ");
                array[i] = scanner.nextInt();
            }
            System.out.print("Четные числа:");
            printNumbers(array, true);
            System.out.print("Нечетные числа:");
            printNumbers(array, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
