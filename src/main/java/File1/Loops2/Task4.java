package File1.Loops2;

import java.util.Scanner;

public class Task4 {

    static void getNumbers(double A, int N) {
        for (int i = 1; i < N; i++){
            double res = Math.log(i) / Math.log(A);
            if (Math.abs(Math.round(res) - res) < 1e-6) {
                System.out.print(i + " ");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите число A: ");
            int a = scanner.nextInt();
            System.out.println("Введите число N: ");
            int n = scanner.nextInt();
            getNumbers(a, n);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
