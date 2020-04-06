package File1.Conditions;

import java.util.Scanner;

public class Task3 {

    static double calcLength(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }

    static void compare(double x1, double y1, double x2, double y2) {
        double length1 = calcLength(x1, y1);
        double length2 = calcLength(x2, y2);
        System.out.println("Первая точка удалена на " + length1);
        System.out.println("Вторая точка удалена на " + length2);
        if (length1 > length2) {
            System.out.println("Первая удалена больше");
        } else if (length1 < length2) {
            System.out.println("Вторая удалена больше");
        }
        else {
            System.out.println("Обе равноудаленны");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите координаты x и y первой точки:");
            double x1 = Double.parseDouble(scanner.next());
            double y1 = Double.parseDouble(scanner.next());

            System.out.println("Введите координаты x и y второй точки:");
            double x2 = Double.parseDouble(scanner.next());
            double y2 = Double.parseDouble(scanner.next());

            compare(x1, y1, x2, y2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
