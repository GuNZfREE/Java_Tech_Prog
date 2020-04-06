package File1.Conditions;

import java.util.Scanner;

public class Task6 {

    static String checkTimeOfYear(int a) {
        if (a == 12 || a == 1 || a == 2) {
            return "Зима";
        }
        else if (a >= 3 && a < 6) {
            return "Весна";
        }
        else if (a >= 6 && a < 9) {
            return "Лето";
        }
        else {
            return "Осень";
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите число:");
                int a = scanner.nextInt();
                if (a < 1 || a > 12) {
                    System.out.println("Не по условию");
                }
                else {
                    System.out.println("Время года: " + checkTimeOfYear(a));
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
