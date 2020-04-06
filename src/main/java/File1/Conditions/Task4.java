package File1.Conditions;

import java.util.Arrays;
import java.util.Scanner;

public class Task4 {

    static Boolean checkRightTriagle(double a, double b, double c) {
        Double[] triagleSide = {a, b, c};
        Arrays.sort(triagleSide);
        return triagleSide[0] * triagleSide[0] + triagleSide[1] * triagleSide[1] == triagleSide[2] * triagleSide[2];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите 3 стороны:");
            double a = Double.parseDouble(scanner.next());
            double b = Double.parseDouble(scanner.next());
            double c = Double.parseDouble(scanner.next());

            System.out.println("Треугольник прямоугольный? - " + checkRightTriagle(a, b, c));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
