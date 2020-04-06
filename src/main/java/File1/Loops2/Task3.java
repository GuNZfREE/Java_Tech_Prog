package File1.Loops2;

import java.util.Scanner;

public class Task3 {

    static double getNumber(double A, int N) {
        double r = A;
        for (int i = 1; i < N; i++) {
            r *= A;
        }
        return r;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите число A: ");
            int a = scanner.nextInt();
            System.out.println("Введите число N: ");
            int n = scanner.nextInt();
            System.out.println("A ^ N = " + getNumber(a, n));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
