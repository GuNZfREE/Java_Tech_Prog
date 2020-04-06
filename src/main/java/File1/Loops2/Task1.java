package File1.Loops2;

import java.util.Scanner;

public class Task1 {

    static void getNumbers(int a, int b) {
        for (int i = a; i <= b; i++){
            System.out.println(" " + i);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите начальное число: ");
            int a = scanner.nextInt();
            System.out.println("Введите конечное число: ");
            int b = scanner.nextInt();
            System.out.println("Подходящие числа: ");
            getNumbers(a, b);
            System.out.println("Количество чисел" + (b - a + 1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
