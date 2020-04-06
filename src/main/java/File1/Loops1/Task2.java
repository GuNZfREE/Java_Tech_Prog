package File1.Loops1;

import java.util.Scanner;

public class Task2 {

    static boolean checkNumber(int a) {
        return a % 5 == 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите конечное число: ");
            int b = scanner.nextInt();
            System.out.println("Подходящие числа: ");
            for(int i = 1; i <= b; i++) {
                if (checkNumber(i)) {
                    System.out.print(i + " ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
