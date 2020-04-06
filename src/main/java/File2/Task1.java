package File2;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.zip.DataFormatException;

public class Task1 {
    private int day;
    private int month;
    private int year;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");


    public Task1() {
        day = 1;
        month = 1;
        year = 1970;
    }

    public Task1(int day, int month, int year) {
        try {
            LocalDate localDate = LocalDate.parse(day + "." + month + "." + year, formatter);
            this.setYear(localDate.getYear());
            this.setMonth(localDate.getMonthValue());
            this.setDay(localDate.getDayOfMonth());
        }
        catch (DateTimeParseException e) {
            throw  new DateTimeException("Такого дня не существует");
        }
    }

    private int getDay() {
        return day;
    }

    private void setDay(int day) {
        this.day = day;
    }

    private int getMonth() {
        return month;
    }

    private void setMonth(int month) {
        this.month = month;
    }

    private int getYear() {
        return year;
    }

    private void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "День " + this.day + "." + this.month + "." + this.year;
    }

    public String fullDateString() {
        return this.day + "." + this.month + "." + this.year;
    }

    public void addYear() {
        LocalDate localDate = LocalDate.of(this.year, this.month, this.day).plusYears(1);
        this.setYear(localDate.getYear());
        this.setMonth(localDate.getMonthValue());
        this.setDay(localDate.getDayOfMonth());
    }

    public void sub2Days() {
        LocalDate localDate = LocalDate.of(this.year, this.month, this.day).minusDays(2);
        this.setYear(localDate.getYear());
        this.setMonth(localDate.getMonthValue());
        this.setDay(localDate.getDayOfMonth());
    }

    public static void main(String[] args) {
        Task1 date1 = new Task1(29,     2, 2020);
        System.out.println(date1);
        date1.addYear();
        System.out.println("+ год: " + date1);
        date1.sub2Days();
        System.out.println("- 2 дня: " + date1);
    }
}
