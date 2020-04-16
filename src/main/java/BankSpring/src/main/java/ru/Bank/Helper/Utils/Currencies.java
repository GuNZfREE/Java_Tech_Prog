package ru.Bank.Helper.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Currencies {
    public static final Set<String> currencies = Stream.of("RUB", "USD", "EUR")
            .collect(Collectors.toCollection(HashSet::new));

    public static final Map<String, BigDecimal> defaultExchangeValue = Stream.of(new Object[][]{
            { "RUB/USD", new BigDecimal(69.69) },
            { "RUB/EUR", new BigDecimal(75.12) },
            { "USD/EUR", new BigDecimal(0.92) },
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (BigDecimal) data[1]));


    public static Map<Integer, String> getMapCurrencies() {
        Set<String> currencies = Currencies.currencies;
        Map<Integer, String> curr = new HashMap<>();
        int i = 1;
        for (String currency: currencies) {
            curr.put(i, currency);
            i++;
        }
        return curr;
    }

    public static BigDecimal converterValue(String base, String symb, BigDecimal money) {
        return getValue(base, symb, money);
    }

    private static BigDecimal getValue(String baseCur, String symbCur, BigDecimal money) {
        String base2Cur = baseCur + "/" + symbCur;
        String cur2Base = symbCur + "/" + baseCur;
        BigDecimal value = defaultExchangeValue.get(base2Cur);
        if (value == null) {
            value = money.divide(defaultExchangeValue.get(cur2Base), 2, RoundingMode.DOWN);
        }
        else {
            value = value.multiply(money).setScale(2, RoundingMode.DOWN);
        }
        return value;
    }

}
