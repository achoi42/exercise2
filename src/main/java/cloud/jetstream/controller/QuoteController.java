package cloud.jetstream.controller;

import cloud.jetstream.domain.Aggregate;
import cloud.jetstream.domain.Quote;
import cloud.jetstream.repository.QuoteRepository;
import cloud.jetstream.util.DateHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Controller class for Quote Model, handles API endpoints with injected quote repository
 */
@RestController
public class QuoteController {

    private static final String JSON_URL = "https://bootcamp-training-files.cfapps.io/week2/week2-stocks.json";

    @Autowired
    private QuoteRepository quoteRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DateHelper dateHelper;

    /**
     * Empty constructor for Jackson compatibility
     */
    protected QuoteController() { }


    /**
     * Endpoint to dynamically load and map JSON data into MySQL database and Quote POJO's
     * @return List of Quote objects loaded into database
     * @throws IOException
     */
    @PostMapping("/load")
    public List<Quote> load() throws IOException {
        URL myUrl = new URL(JSON_URL);
        List<Quote> mappedQuotes = objectMapper.readValue(myUrl, new TypeReference<List<Quote>>(){});
        quoteRepository.deleteAll();
        return quoteRepository.save(mappedQuotes);
    }

    /**
     * Endpoint to retrieve daily aggregates for given stock and date
     * @param sym Stock symbol
     * @param date Date of query in yyyy-MM-dd format
     * @return List of stock quotes returned by database query
     */
    @GetMapping("/{sym}/{date}")
    public List<Aggregate> aggregate(@PathVariable(name="sym") String sym, @PathVariable(name = "date") String date) {
        Date queryDate = dateHelper.stringToDate(date);
        Date endOfQueryDate = dateHelper.getEnd(queryDate);

        List<Quote> max = quoteRepository.findDailyMax(sym, queryDate, endOfQueryDate);
        Aggregate maxPresent = new Aggregate("highest price", max);
        List<Quote> min = quoteRepository.findMonthlyMin(sym, queryDate, endOfQueryDate);
        Aggregate minPresent = new Aggregate("lowest price", min);
        Quote totalQuote = new Quote(sym, 0.0, quoteRepository.findDailyVolume(sym, queryDate, endOfQueryDate), queryDate);
        List<Quote> total = Arrays.asList(totalQuote);
        Aggregate totalPresent = new Aggregate("total volume", total);
        List<Quote> closing = quoteRepository.findClosingPrice(sym, queryDate, endOfQueryDate);
        Aggregate closePresent = new Aggregate("closing price", closing);

        Aggregate[] aggregates = new Aggregate[] {maxPresent, minPresent, totalPresent, closePresent};
        return Arrays.asList(aggregates);
    }

    /**
     * Endpoint to retrieve monthly aggregates for given stock and month
     * @param sym Stock symbol
     * @param month Month of query in yyyy-MM format
     * @return List of stock quotes returned by database query
     */
    @GetMapping("/monthly/{sym}/{month}")
    public List<Aggregate> monthly(@PathVariable(name="sym") String sym, @PathVariable(name = "month") String month) {
        Date queryMonth = dateHelper.stringToMonthStart(month);
        Date endOfQueryMonth = dateHelper.getEndOfMonth(queryMonth);

        List<Quote> max = quoteRepository.findMonthlyMax(sym, queryMonth, endOfQueryMonth);
        Aggregate maxPresent = new Aggregate("monthly maximum", max);
        List<Quote> min = quoteRepository.findMonthlyMin(sym, queryMonth, endOfQueryMonth);
        Aggregate minPresent = new Aggregate("monthly minimum", min);
        Quote totalQuote = (new Quote(sym, 0.0, quoteRepository.findMonthlyVolume(sym, queryMonth, endOfQueryMonth), queryMonth));
        List<Quote> total = Arrays.asList(totalQuote);
        Aggregate totalPresent = new Aggregate("monthly total volume", total);
        Aggregate[] aggregates = new Aggregate[]{maxPresent, minPresent, totalPresent};
        return Arrays.asList(aggregates);
    }
}
