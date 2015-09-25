package currency_rate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import currency_rate.converters.JsonLocalDateSerializer;

import java.time.LocalDate;

/**
 * Created by lav on 25.09.15.
 */
public class CurrencyRate {
    private String code;
    private double rate;
    @JsonSerialize(using = JsonLocalDateSerializer.class)
    private LocalDate date;

    public CurrencyRate(String code, double rate, LocalDate date) {
        this.code = code;
        this.rate = rate;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
