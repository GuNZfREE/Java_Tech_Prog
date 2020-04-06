package File1.Arrays;

import java.math.BigInteger;
import java.util.Scanner;

public class Task4 {

    static BigInteger getDCG(BigInteger[] array) {
        BigInteger r = array[0];
        for (int i = 1; i < array.length; i++) {
            r = r.gcd(array[i]);
        }
        return r;
    }

    static BigInteger getLCM(BigInteger[] array) {
        BigInteger r = array[0];
        for (int i = 1; i < array.length; i++) {
            r = r.divide(r.gcd(array[i])).multiply(array[i]);
        }
        return r;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Введите количество элементов: ");
            int N = scanner.nextInt();
            BigInteger[] array = new BigInteger[N];
            for (int i = 0; i < N; i++) {
                System.out.print("Введите число: ");
                array[i] = scanner.nextBigInteger();
            }
            System.out.println("НОД: " + getDCG(array));
            System.out.println("НОК: " + getLCM(array));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
