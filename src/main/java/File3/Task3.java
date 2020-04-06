package File3;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Task3 {
    private double length;
    private double width;
    private double height;


    public Task3() {
        this.setLength(0);
        this.setWidth(0);
        this.setHeight(0);
    }

    public Task3(double length, double width, double height) {
        this.setLength(length);
        this.setWidth(width);
        this.setHeight(height);
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Комната, длина = " + this.getLength() + ", ширина = " + this.getWidth() + ", высота = " + this.getHeight();
    }

    public double wallsSquare() {
        return 2 * this.getLength() * this.getHeight() + 2 * this.getHeight() * this.getWidth();
    }

    public double wallsSquareWithoutWindow() {
        return wallsSquare() - 2 * 15 - 2 * 8;
    }


    public static void main(String[] args) {
        Task3 room1 = new Task3(6,     3, 2.8);
        System.out.println(room1);
        System.out.println("Площадь вся: " + room1.wallsSquare());
        System.out.println("Площадь без окон и двери: " + room1.wallsSquareWithoutWindow());
    }
}
