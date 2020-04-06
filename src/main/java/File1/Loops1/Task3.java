package File1.Loops1;

import java.util.Scanner;

public class Task3 {

    static boolean checkNumber(int a) {
        return a != 0 && (a & a - 1) == 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите число: ");
            int a = scanner.nextInt();
            if (checkNumber(a)) {
                System.out.print("Является");
            } else {
                System.out.print("Не является");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
