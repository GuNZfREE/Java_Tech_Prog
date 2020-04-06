package File1.Loops1;

import java.util.Scanner;

public class Task1 {

    static boolean checkSum(int a) {
        int sum = 0;
        while (a != 0) {
            sum += a % 10;
            a /= 10;
        }
        return checkNumber(sum);
    }

    static boolean checkNumber(int a) {
        return a % 5 != 0 && a % 3 == 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите начальное число: ");
            int a = scanner.nextInt();
            System.out.println("Введите конечное число: ");
            int b = scanner.nextInt();
            System.out.println("Подходящие числа: ");
            for(int i = a; i <= b; i++) {
                if (checkNumber(i) && checkSum(i)) {
                    System.out.print(i + " ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
