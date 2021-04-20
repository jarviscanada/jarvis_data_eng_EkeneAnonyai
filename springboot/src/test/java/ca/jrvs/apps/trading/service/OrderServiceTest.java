package ca.jrvs.apps.trading.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dto.MarketOrderDto;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    //capture parameter when calling securityOrderDao.save()
    @Captor
    ArgumentCaptor<SecurityOrder> captorSecurityOrder;

    //mock all dependencies
    @Mock
    private AccountDao accountDao;

    @Mock
    private SecurityOrderDao securityOrderDao;

    @Mock
    private QuoteDao quoteDao;

    @Mock
    private PositionDao positionDao;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void executeMarketOrder() {
        Account mockAccount = new Account();
        mockAccount.setAmount(100d);
        when(accountDao.findById(anyInt())).thenReturn(Optional.of(mockAccount));

        Quote mockQuote = new Quote();
        mockQuote.setAskPrice(10d);
        mockQuote.setBidPrice(20d);
        when(quoteDao.existsById(anyString())).thenReturn(true);
        when(quoteDao.findById(anyString())).thenReturn(Optional.of(mockQuote));

        SecurityOrder mockSecurityOrder = new SecurityOrder();

        Position mockPosition = new Position();
        mockPosition.setPosition(10);
        when(positionDao.findById(any(), any())).thenReturn(Optional.of(mockPosition));

        MarketOrderDto orderDto = new MarketOrderDto();
        try {
            orderService.executeMarketOrder(orderDto);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }

        orderDto.setAccountId(1);
        orderDto.setTicker("AAPL");
        orderDto.setSize(1000);
        SecurityOrder testOrder = orderService.executeMarketOrder(orderDto);
        assertEquals("CANCELLED", testOrder.getStatus());

        orderDto.setSize(10);
        testOrder = orderService.executeMarketOrder(orderDto);
        assertEquals("FILLED", testOrder.getStatus());

        mockPosition.setPosition(1);
        testOrder = orderService.executeMarketOrder(orderDto);
        assertEquals("CANCELLED", testOrder.getStatus());

        orderDto.setSize(-100);
        testOrder = orderService.executeMarketOrder(orderDto);
        assertEquals("FILLED", testOrder.getStatus());
    }
}