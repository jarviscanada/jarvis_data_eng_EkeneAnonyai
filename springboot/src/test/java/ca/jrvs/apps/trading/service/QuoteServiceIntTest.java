package ca.jrvs.apps.trading.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jrvs.apps.trading.TestConf;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConf.class})
@Sql({"classpath:schema.sql"})
public class QuoteServiceIntTest {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private QuoteDao quoteDao;

    private Quote savedQuote;

    @Before
    public void setup() {
        quoteDao.deleteAll();
        savedQuote = new Quote();
        savedQuote.setAskPrice(10d);
        savedQuote.setAskSize((long) 10);
        savedQuote.setBidPrice(10.2d);
        savedQuote.setBidSize((long) 10);
        savedQuote.setId("AAPL");
        savedQuote.setLastPrice(10.1d);
        quoteDao.save(savedQuote);
    }

    @After
    public void tearDown() {
        quoteDao.deleteById(savedQuote.getId());
    }

    @Test
    public void findIexQuoteByTicker() {
        IexQuote iexQuote = quoteService.findIexQuoteByTicker("GOOGL");
        assertEquals(iexQuote.getSymbol(), "GOOGL");
        try {
            iexQuote = quoteService.findIexQuoteByTicker("Hey There");
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void updateMarketData() {
        quoteService.updateMarketData();
        Quote testQuote = quoteDao.findById(savedQuote.getId()).get();
        assertNotEquals(savedQuote, testQuote);
    }

    @Test
    public void saveQuotes() {
        List<Quote> quotes = quoteService.saveQuotes(Arrays.asList("GOOGL"));
        assertEquals(quoteDao.findById("GOOGL").get().getId(), "GOOGL");
    }

    @Test
    public void saveQuote() {
        Quote quote = new Quote();
        quote.setAskPrice(10d);
        quote.setAskSize((long) 10);
        quote.setBidPrice(10.2d);
        quote.setBidSize((long) 10);
        quote.setId("FB");
        quote.setLastPrice(10.1d);
        quoteService.saveQuote(quote);
        assertEquals(quote, quoteDao.findById(quote.getId()).get());
    }

    @Test
    public void findAllQuotes() {
        List<Quote> quotes = quoteDao.findAll();
        assertEquals(savedQuote, quotes.get(0));
    }

}