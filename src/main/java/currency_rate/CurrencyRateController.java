package currency_rate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/currency")
public class CurrencyRateController {
    @Autowired
    private CurrencyRateService currencyRateService;

    @RequestMapping(value = "/api/rate/{code}/{date}", method = RequestMethod.GET)
    @ResponseBody
    public Object rate(@PathVariable String code,
                       @PathVariable LocalDate date) {

        return currencyRateService.getRates(code, date);
    }

}
