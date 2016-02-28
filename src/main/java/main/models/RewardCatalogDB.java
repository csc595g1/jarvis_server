package main.models;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class RewardCatalogDB {

    private final String TABLE_REWARD_CATALOG = "reward_catalog_tbl";
    Connection connection;
    
    public RewardCatalogDB(){
        this.createRewardCatalogTable();
    }
    
    public boolean insertRewardCatalogItem(RewardCatalog catalog) throws SQLException{
        System.out.println("insertRewardCatalogItem called...");
        boolean isCreated = false;
        int count = 0;
        String sql;
        
        try{
        	Statement statement;
            connection = getConnection();   
            
            if (catalog.getDenomination() == 0) {
                sql = "INSERT INTO " 
                		+ TABLE_REWARD_CATALOG 
                		+ " (brand,image_url,type,description,sku,is_variable,denomination,currency_code,available,country_code,tstamp) VALUES ("
                		+ " '"+catalog.getBrand()+"'"
                		+ ",'"+catalog.getImage_url()+"'"
                		+ ",'"+catalog.getType()+"'"
                		+ ",'"+catalog.getDescription()+"'"
                		+ ",'"+catalog.getSku()+"'"
                		+ ",'"+catalog.getIs_variable()+"'"
                		+ ",'"+catalog.getDenomination()+"'"    /////////////Look into nulls
                		+ ",'"+catalog.getCurrency_code()+"'"
                		+ ",'"+catalog.getAvailable()+"'"
                		+ ",'"+catalog.getCountry_code()+"'"
                		+ ",'now');";
            	
            } else {
                sql = "INSERT INTO " 
                		+ TABLE_REWARD_CATALOG 
                		+ " (brand,image_url,type,description,sku,is_variable,min_price,max_price,currency_code,available,country_code,tstamp) VALUES ("
                		+ " '"+catalog.getBrand()+"'"
                		+ ",'"+catalog.getImage_url()+"'"
                		+ ",'"+catalog.getType()+"'"
                		+ ",'"+catalog.getDescription()+"'"
                		+ ",'"+catalog.getSku()+"'"
                		+ ",'"+catalog.getIs_variable()+"'"
                		+ ",'"+catalog.getMin_price()+"'"    /////////////Look into nulls
                		+ ",'"+catalog.getMax_price()+"'"    /////////////Look into nulls
                		+ ",'"+catalog.getCurrency_code()+"'"
                		+ ",'"+catalog.getAvailable()+"'"
                		+ ",'"+catalog.getCountry_code()+"'"
                		+ ",'now');";
            	
            }
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("isCreate = " + isCreated);
            System.out.println("INSERT INTO " + TABLE_REWARD_CATALOG + " "+ catalog.getBrand());
            
            statement.clearBatch();
            statement.close();
            connection.close();
            isCreated = true;
        }
            catch(URISyntaxException e){
                System.out.println("caught URIexception");
            e.getMessage();
            e.printStackTrace();
            return false;
            }
        catch(SQLException e){
            System.out.println("caught sql exception");
            e.getMessage();
            e.printStackTrace();
        return false;
        }
        finally{connection.close();}
        return isCreated;
    }
    
    
    
///////////////////////////////////////////////////////////////////////////////    
    //create if not exists
    private void createRewardCatalogTable(){
        
        try{
        connection = getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_REWARD_CATALOG 
                + "(eventId BIGSERIAL PRIMARY KEY,"
                + "brand VARCHAR(250) NOT NULL,"
                + "image_url VARCHAR(250) NOT NULL,"
                + "type VARCHAR(250) NOT NULL,"
                + "description VARCHAR(250) NOT NULL,"
                + "sku VARCHAR(250) NOT NULL,"
                + "is_variable VARCHAR(5) NULL,"
                + "denomination integer NULL,"
                + "min_price integer NULL,"
                + "max_price integer NULL,"
                + "currency_code VARCHAR(3) NOT NULL,"
                + "available VARCHAR(5) NOT NULL,"
                + "country_code VARCHAR(5) NOT NULL,"
                + "tstamp timestamp);";
        
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        connection.close();
        }
        catch(URISyntaxException e){
            e.getMessage();
            e.printStackTrace();
        }
        catch(SQLException e){
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
