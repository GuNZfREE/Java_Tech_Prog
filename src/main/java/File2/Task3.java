package File2;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Task3 {
    private int hour;
    private int minute;
    private int second;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:m:s");


    public Task3() {
        hour = 0;
        minute = 0;
        second = 0;
    }

    public Task3(int hour, int minute, int second) {
        try {
            LocalTime localTime = LocalTime.parse(hour + ":" + minute + ":" + second, formatter);
            this.setHour(localTime.getHour());
            this.setMinute(localTime.getMinute());
            this.setSecond(localTime.getSecond());
        }
        catch (DateTimeParseException e) {
            throw  new DateTimeException("Такого времени не существует");
        }
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "Время " + this.hour + ":" + this.minute + ":" + this.second;
    }

    public int countSeconds() {
        return this.hour * 3600 + this.minute * 60 + this.second;
    }

    public void add5Second() {
        LocalTime localTime = LocalTime.of(this.hour, this.minute, this.second).plusSeconds(5);
        this.setHour(localTime.getHour());
        this.setMinute(localTime.getMinute());
        this.setSecond(localTime.getSecond());
    }

    public static void main(String[] args) {
        Task3 time1 = new Task3(13,     55, 58);
        System.out.println(time1);
        time1.add5Second();
        System.out.println("+ 5 секунд: " + time1);
        System.out.println("количество секунд: " + time1.countSeconds());
    }
}
