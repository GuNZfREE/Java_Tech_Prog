package File1.Loops1;

import java.util.Scanner;

public class Task4 {

    static void fib(int N) {
        int pred = 0;
        int cur = 1;
        while (cur < N) {
            System.out.print(cur + " ");
            int i = pred;
            pred = cur;
            cur += i;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите число: ");
            int N = scanner.nextInt();
            fib(N);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
