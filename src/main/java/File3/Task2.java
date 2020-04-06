package File3;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Task2 {
    private double defaultDollar = 69.55;
    private String name;
    private double cost;
    private int year;
    private final OkHttpClient httpClient = new OkHttpClient();


    public Task2() {
        this.setName("Тест 1");
        this.setCost(19.99);
        this.setYear(1970);
    }

    public Task2(String name, double cost, int year) {
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

    public double getDollars() {
        Request request = new Request.Builder()
                .url("https://api.exchangeratesapi.io/latest?base=USD&&symbols=RUB")
//                .addHeader("User-Agent", "OkHttp Bot")
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.out.println("Use default exchange = " + defaultDollar);
                return defaultDollar;
            }

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class).getAsJsonObject("rates");
            double usd = jsonObject.get("RUB").getAsDouble();
            System.out.println("Current USD exchange = " + usd);
            return usd;
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println(("Cannot parse price, give standard course"));
        }
        return defaultDollar;
    }

    public void changePrice() {
        double usd = getDollars();
        BigDecimal bd = new BigDecimal(this.getCost() / usd);
        bd = bd.setScale(2, RoundingMode.HALF_DOWN);
        this.setCost(bd.doubleValue());
    }

    public static void main(String[] args) {
        Task2 product1 = new Task2("Тестовый товар",     69.99, 2020);
        System.out.println(product1);
        product1.changePrice();
        System.out.println("Новая стоимость товара: " + product1.getCost() + " USD");
    }
}
