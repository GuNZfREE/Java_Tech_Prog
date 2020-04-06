package File1.Conditions;

import java.util.Scanner;

public class Task2 {

    static double allowanceCalc(int a, int b) {
        if (a >= 2 && a < 5) {
            return b * 0.02;
        }
        else if (a >= 5 && a < 10) {
            return b * 0.05;
        }
        return 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите зарплату и стаж:");
            int a = Integer.parseInt(scanner.next());
            int b = Integer.parseInt(scanner.next());

            double allowance = allowanceCalc(a, b);
            System.out.println("Надбавка: " + allowance);
            System.out.println("Сумма к выплате: " + (b + allowance));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
