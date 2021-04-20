package ca.jrvs.apps.trading.model.view;

import java.util.List;

public class PortfolioView {

    List<SecurityRow> securityRows;

    public PortfolioView(List<SecurityRow> securityRows) {
        this.securityRows = securityRows;
    }

    public List<SecurityRow> getSecurityRows() {
        return securityRows;
    }

}