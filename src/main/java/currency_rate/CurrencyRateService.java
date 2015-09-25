package currency_rate;

import currency_rate.xml.ValCursType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lav on 25.09.15.
 */
@Service
public class CurrencyRateService {
    private static final String API_URL = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=";
    @Value("${updatePeriod}")
    private int updatePeriod;
    private static long lastUpdate = 0;
    private Map<LocalDate, Map<String, Double>> rates = new HashMap<>();
    private DateTimeFormatter sberDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CurrencyRate getRates(String currency, LocalDate date) {
        if (date == null) {
            date = LocalDate.now().plusDays(1);
        }

        long currentTimeMillis = System.currentTimeMillis();
        long elapsedTime = currentTimeMillis - lastUpdate;

        if (elapsedTime > updatePeriod || !rates.containsKey(date)) {
            loadRates(date);
            lastUpdate = currentTimeMillis;
        }

        Double rate = rates.get(date).get(currency);
        return new CurrencyRate(currency, rate, date);
    }

    public synchronized void loadRates(LocalDate date) {
        String strDate = date.format(sberDateFormatter);

        String url = API_URL + strDate;
        try {
            JAXBContext jc = JAXBContext.newInstance(ValCursType.class);
            Unmarshaller um = jc.createUnmarshaller();

            URL website = new URL(url);
            ValCursType data = (ValCursType) um.unmarshal(new BufferedInputStream(website.openStream()));

            Map<String, Double> currencyMap = rates.computeIfAbsent(date, localDate -> new HashMap<>());

            data.getValute().forEach(valuteType -> {
                double rate = valuteType.getValue() / valuteType.getNominal();
                rate = round(rate);
                currencyMap.put(valuteType.getCharCode(), rate);
            });
        } catch (JAXBException | IOException e) {
            throw new RuntimeException("Failed to fetch " + url, e);
        }
    }

    private Double round(double v) {
        String str = Double.valueOf(v).toString();
        BigDecimal d = new BigDecimal(str);
        return d.setScale(4, RoundingMode.HALF_UP).doubleValue();
    }
}
