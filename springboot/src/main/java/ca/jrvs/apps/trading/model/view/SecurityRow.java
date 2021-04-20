package ca.jrvs.apps.trading.model.view;

import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;

public class SecurityRow {

    String ticker;

    public String getTicker() {
        return ticker;
    }

    public Position getPosition() {
        return position;
    }

    public Quote getQuote() {
        return quote;
    }

    Position position;
    Quote quote;

    public SecurityRow(String ticker, Position position, Quote quote) {
        this.ticker = ticker;
        this.position = position;
        this.quote = quote;
    }

}