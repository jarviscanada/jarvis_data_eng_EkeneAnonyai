package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dto.MarketOrderDto;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final AccountDao accountDao;
    private final SecurityOrderDao securityOrderDao;
    private final QuoteDao quoteDao;
    private final PositionDao positionDao;

    @Autowired
    public OrderService(AccountDao accountDao, SecurityOrderDao securityOrderdao, QuoteDao quoteDao,
                        PositionDao positionDao) {
        this.accountDao = accountDao;
        this.securityOrderDao = securityOrderdao;
        this.quoteDao = quoteDao;
        this.positionDao = positionDao;
    }

    /**
     * Execute a market order
     *
     * @param orderDto market order
     * @return SecurityOrder from security_order table
     * @throws org.springframework.dao.DataAccessException if unable to get data from DAO
     * @throws IllegalArgumentException                    for invalid input
     */
    public SecurityOrder executeMarketOrder(MarketOrderDto orderDto) {
        if (orderDto == null || orderDto.getAccountId() == null || orderDto.getTicker() == null
                || orderDto.getSize() == null) {
            throw new IllegalArgumentException("Error: MarketOrderDto is null, or contains null values");
        }
        if (!quoteDao.existsById(orderDto.getTicker())) {
            throw new DataRetrievalFailureException(
                    "Error: Unable to find quote with ticker:" + orderDto.getTicker());
        }
        Account account = new Account();
        try {
            account = accountDao.findById(orderDto.getAccountId()).get();
        } catch (DataAccessException ex) {
            logger.error("Unable to get account get from DAO for account id:" + orderDto.getAccountId());
        }
        SecurityOrder securityOrder = new SecurityOrder();
        securityOrder.setAccountId(account.getId());
        securityOrder.setTicker(orderDto.getTicker());
        securityOrder.setPrice(0.0d);
        securityOrder.setSize(orderDto.getSize());
        securityOrder.setStatus("PENDING");
        securityOrderDao.save(securityOrder);
        if (securityOrder.getSize() < 0) {
            handleSellMarketOrder(orderDto, securityOrder, account);
        } else if (securityOrder.getSize() > 0) {
            handleBuyMarketOrder(orderDto, securityOrder, account);
        } else {
            throw new IllegalArgumentException("Error: order size cannot be 0");
        }
        return securityOrder;
    }

    /**
     * Helper method that executes a buy order
     *
     * @param marketOrderDto user order
     * @param securityOrder  to be saved in data database
     * @param account
     */
    protected void handleBuyMarketOrder(MarketOrderDto marketOrderDto, SecurityOrder securityOrder,
                                        Account account) {
        Quote quote = quoteDao.findById(marketOrderDto.getTicker()).get();
        securityOrder.setPrice(quote.getAskPrice());
        double price = securityOrder.getSize() * securityOrder.getPrice();
        if (account.getAmount() < price) {
            securityOrder.setStatus("CANCELLED");
            securityOrder.setNotes("Insufficient fund. Order amount:" + price);
        } else {
            securityOrder.setStatus("FILLED");
            account.setAmount(account.getAmount() - price);
            accountDao.save(account);
        }
        securityOrderDao.save(securityOrder);
    }

    /**
     * Helper method that executes a sell order
     *
     * @param marketOrderDto user order
     * @param securityOrder  to be saved in the database
     * @param account
     */
    protected void handleSellMarketOrder(MarketOrderDto marketOrderDto, SecurityOrder securityOrder,
                                         Account account) {
        Quote quote = quoteDao.findById(marketOrderDto.getTicker()).get();
        securityOrder.setPrice(quote.getBidPrice());
        Position position = positionDao.findById(account.getId(), marketOrderDto.getTicker()).get();
        if (position.getPosition() - securityOrder.getSize() < 0) {
            securityOrder.setStatus("CANCELLED");
            securityOrder.setNotes("Insufficient position.");
        } else {
            securityOrder.setStatus("FILLED");
            account.setAmount(account.getAmount() + securityOrder.getSize() * securityOrder.getPrice());
            accountDao.save(account);
        }
        securityOrderDao.save(securityOrder);
    }
}
