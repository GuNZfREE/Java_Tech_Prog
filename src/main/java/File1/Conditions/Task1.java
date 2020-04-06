package File1.Conditions;

import java.util.Scanner;

public class Task1 {

    static float sumOfSquares(float a, float b) {
        return a * a + b * b;
    }

    static float squareOfSum(float a, float b) {
        float sum = a + b;
        return sum * sum;
    }

    static void compare(float a, float b) {
        float squareSum = squareOfSum(a, b);
        float sumSquare = sumOfSquares(a, b);
        System.out.println("Квадрат суммы:" + squareSum);
        System.out.println("Сумма квадратов:" + sumSquare);
        if (sumSquare <= squareSum) {
            System.out.println("Квадрат суммы больше");
        } else {
            System.out.println("Сумма квадратов больше");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите 2 числа:");
            float a = Float.parseFloat(scanner.next());
            float b = Float.parseFloat(scanner.next());

            compare(a, b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
