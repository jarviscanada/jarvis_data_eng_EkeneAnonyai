package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCExecutor {

    final static Logger logger = LoggerFactory.getLogger(JDBCExecutor.class);

    public static void main(String... args){
        BasicConfigurator.configure();
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", "hplussport", "postgres", "password");
        try{
            Connection connection = dcm.getConnection();
            CustomerDAO customerDAO = new CustomerDAO(connection);
            customerDAO.findAllSorted(20).forEach(System.out::println);
            System.out.println("Paged");
            for(int i=1;i<3;i++){
                System.out.println("Page number: " + i);
                customerDAO.findAllPaged(10, i).forEach(System.out::println);
            }
            OrderDAO orderDAO = new OrderDAO(connection);
            Order order = orderDAO.findById(1000);
            System.out.println(order);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
