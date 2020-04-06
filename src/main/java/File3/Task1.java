package File3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Task1 {
    private String name;
    private double cost;
    private int year;


    public Task1() {
        this.setName("Тест 1");
        this.setCost(19.99);
        this.setYear(1970);
    }

    public Task1(String name, double cost, int year) {
        this.setName(name);
        this.setCost(cost);
        this.setYear(year);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Товар " + this.getName() + ", Цена " + this.getCost() + ", Год производства " + this.getYear();
    }


    public int diffYearNow() {
        LocalDate dateNow = LocalDate.now();
        return dateNow.getYear() - this.getYear();
    }

    public void increaseCost() {
        if (this.getYear() == LocalDate.now().getYear()) {
            BigDecimal bd = new BigDecimal(this.getCost() * 1.2);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            this.setCost(bd.doubleValue());
        }
    }

    public static void main(String[] args) {
        Task1 product1 = new Task1("Тестовый товар",     69.99, 2020);
        System.out.println(product1);
        System.out.println("Товар был изготовален " + product1.diffYearNow() + " лет назад");
        product1.increaseCost();
        System.out.println("Новая стоимость товара после проверки: " + product1.getCost());
    }
}
