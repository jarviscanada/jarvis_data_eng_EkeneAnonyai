package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.view.PortfolioView;
import ca.jrvs.apps.trading.model.view.SecurityRow;
import ca.jrvs.apps.trading.model.view.TraderAccountView;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DashboardService {

    private TraderDao traderDao;
    private PositionDao positionDao;
    private AccountDao accountDao;
    private QuoteDao quoteDao;

    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

    @Autowired
    public DashboardService(TraderDao traderDao, PositionDao positionDao,
                            AccountDao accountDao, QuoteDao quoteDao) {
        this.traderDao = traderDao;
        this.positionDao = positionDao;
        this.accountDao = accountDao;
        this.quoteDao = quoteDao;
    }

    /**
     * Create and return a traderAccountView by trader ID
     *
     * @param traderId must not be null
     * @return traderAccountView
     * @throws IllegalArgumentException if traderId is null or not found
     */
    public TraderAccountView getTraderAccount(Integer traderId) {
        if (traderId == null) {
            throw new IllegalArgumentException("traderId cannot be null");
        }
        if (!traderDao.existsById(traderId)) {
            throw new IllegalArgumentException("Cannot find trader with id:" + traderId);
        }
        Trader trader = traderDao.findById(traderId).get();
        Account account = findAccountByTraderId(traderId);
        return new TraderAccountView(trader, account);

    }

    /**
     * Create and return a portfolioView by trader ID
     *
     * @param traderId must not be null
     * @return portfolioView
     * @throws IllegalArgumentException if traderId is null or nout found
     */
    public PortfolioView getProfileViewByTraderId(Integer traderId) {
        if (traderId == null) {
            throw new IllegalArgumentException("traderId cannot be null");
        }
        List<SecurityRow> securityRows = new ArrayList<>();
        Account account = accountDao.findByTraderId(traderId).get();
        Trader trader = traderDao.findById(traderId).get();
        List<Position> positionList = positionDao.findAllByAccountId(account.getId());
        positionList.forEach(position -> {
            Quote quote = quoteDao.findById(position.getTicker()).get();
            securityRows.add(new SecurityRow(quote.getTicker(), position, quote));
        });
        return new PortfolioView(securityRows);
    }

    private Account findAccountByTraderId(Integer traderId) {
        return accountDao.findByTraderId(traderId).orElseThrow(()-> new IllegalArgumentException("Invalid trader id"));
    }
}
