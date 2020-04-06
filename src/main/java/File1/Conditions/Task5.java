package File1.Conditions;

import java.util.Arrays;
import java.util.Scanner;

public class Task5 {

    static double squaresPos(double a) {
        return a > 0 ? a * a : a;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите число:");
            for(int i = 0; i < 3; i++) {
                double a = Double.parseDouble(scanner.next());
                System.out.println("Новое число - " + squaresPos(a));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
