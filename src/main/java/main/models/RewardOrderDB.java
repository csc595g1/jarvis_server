package main.models;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class RewardOrderDB {
    private final String TABLE_REWARD_ORDER = "reward_order_tbl";
    Connection connection;
    
    public RewardOrderDB(){
        this.createRewardOrderTable();
    }
    
///////////////////////////////////////////////////////////////////////////////    
	//create if not exists
	private void createRewardOrderTable(){
	System.out.println("RewardOrderDB->createRewardOrderTable");
	
		try{
			connection = getConnection();
			String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_REWARD_ORDER 
			+ "(orderId BIGSERIAL PRIMARY KEY,"
			+ "remote_order_id VARCHAR(50) NOT NULL,"
			+ "account_identifier VARCHAR(250) NULL,"
			+ "customer VARCHAR(250) NULL,"
			+ "sku VARCHAR(250) NOT NULL,"
			+ "is_variable VARCHAR(5) NULL,"
			+ "denomination_value integer NULL,"
			+ "denomination_code VARCHAR(5) NULL,"
			+ "amount_charged_value integer NULL,"
			+ "amount_charged_code VARCHAR(5) NULL,"
			+ "max_price integer NULL,"
			+ "delivered_at VARCHAR(50) NULL,"
			+ "reward_token VARCHAR(50) NULL,"
			+ "reward_number VARCHAR(50) NULL,"
			+ "reward_expiration VARCHAR(50) NULL,"
			+ "tstamp timestamp);";
			
			Statement stmt = connection.createStatement();
			stmt.execute(sql);
			connection.close();
		} catch(URISyntaxException e) {
			e.getMessage();
			e.printStackTrace();
		} catch(SQLException e) {
			e.getMessage();
			e.printStackTrace();
		}
	}

	private static Connection getConnection() throws URISyntaxException, SQLException {
	
		//production code
		URI dbUri = new URI(System.getenv("DATABASE_URL"));
		
		//test - COMMENT OUT and use above code when deploying
		//URI dbUri = new URI("postgres://thuckapxnbbplj:y7Q-HMUC3AV3jN1k1cIDrWO6RW@ec2-54-225-215-233.compute-1.amazonaws.com:5432/ddu66lf1nisfq8");
		
		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + dbUri.getPath();
		
		return DriverManager.getConnection(dbUrl, username, password);
	
	}


}
