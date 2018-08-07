package cloud.jetstream.domain;

import java.util.List;

/**
 * Helper class to improve readability from REST HTTP responses
 */
public class Aggregate {

  private String title;
  private List<Quote> quotes;

  public Aggregate(String title, List<Quote> quotes) {
    this.title = title;
    this.quotes = quotes;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<Quote> getQuotes() {
    return quotes;
  }

  public void setQuotes(List<Quote> quotes) {
    this.quotes = quotes;
  }

  public void addQuote(Quote quote) {
    this.quotes.add(quote);
  }
}
