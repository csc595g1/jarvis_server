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
        System.out.println("RewardCatalogDB->insertRewardCatalogItem");
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
        finally { connection.close(); }
        
        return isCreated;
    }
    
    public Boolean deleteCatalog() {
        System.out.println("RewardCatalogDB->deleteCatalog");
        
        boolean isDeleted = false;

        try{
	        connection = getConnection();
	        String sql = "DELETE FROM " + TABLE_REWARD_CATALOG ;
	        
	        Statement stmt = connection.createStatement();
	        stmt.execute(sql);
	        connection.close();
	        isDeleted = Boolean.TRUE;
        }
        catch(URISyntaxException e) {
        	isDeleted = Boolean.FALSE;
            e.getMessage();
            e.printStackTrace();
        }
        catch(SQLException e) {
        	isDeleted = Boolean.FALSE;
            e.getMessage();
            e.printStackTrace();
        }

    	return isDeleted;
    }
    
    public ArrayList<RewardCatalog> getCatalogItems() {
        System.out.println("RewardCatalogDB->getCatalogItems");
        
        String catalogId = "";
        String brand = "";
        String image_url = "";
        String type = "";
        String description = "";
        String sku = "";
        Boolean is_variable = false;
        
        Integer denomination = 0;
        Integer min_price = 0;
        Integer max_price = 0;
             
        String currency_code = "";
        Boolean available = false;
        String country_code = "";
        String tstamp = "";

        ArrayList<RewardCatalog> rewardCatalog = new ArrayList<RewardCatalog>();
               
        String sql = "SELECT catalogId,brand,image_url,type,description,sku,is_variable,denomination,min_price,max_price,currency_code,available,country_code,tstamp FROM " 
                    + TABLE_REWARD_CATALOG 
                    + ";";
        try{
            connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
            	catalogId = rs.getString(1);
            	brand = rs.getString(2);
            	image_url = rs.getString(3);
            	type = rs.getString(4);
            	description = rs.getString(5);
            	sku = rs.getString(6);
            	is_variable = Boolean.valueOf(rs.getString(7));

            	currency_code = rs.getString(11);
            	available = Boolean.valueOf(rs.getString(12));
            	country_code = rs.getString(13);
            	tstamp = rs.getString(14);
            	
               	denomination = rs.getInt(8);
           		min_price = rs.getInt(9);
           		max_price = rs.getInt(10);
            	
            	rewardCatalog.add(new RewardCatalog(catalogId,brand,image_url,type,description,sku,is_variable,denomination,min_price,max_price,currency_code,available,country_code,tstamp));
            }

            connection.close();
            return rewardCatalog;
        }
        catch(URISyntaxException e){
            e.getMessage();
            e.printStackTrace();
            return null;
        }
        catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
            return null;
        }
    	
    }
    
    public ArrayList<RewardCatalog> getCatalogItemsByType(String rewardType) {
        System.out.println("RewardCatalogDB->getCatalogItemsByType");
        
        String catalogId = "";
        String brand = "";
        String image_url = "";
        String type = "";
        String description = "";
        String sku = "";
        Boolean is_variable = false;
        
        Integer denomination = 0;
        Integer min_price = 0;
        Integer max_price = 0;
             
        String currency_code = "";
        Boolean available = false;
        String country_code = "";
        String tstamp = "";

        ArrayList<RewardCatalog> rewardCatalog = new ArrayList<RewardCatalog>();
               
        String sql = "SELECT catalogId,brand,image_url,type,description,sku,is_variable,denomination,min_price,max_price,currency_code,available,country_code,tstamp FROM " 
                    + TABLE_REWARD_CATALOG 
                    + " WHERE type = '" + rewardType + "';";
        try{
            connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
            	catalogId = rs.getString(1);
            	brand = rs.getString(2);
            	image_url = rs.getString(3);
            	type = rs.getString(4);
            	description = rs.getString(5);
            	sku = rs.getString(6);
            	is_variable = Boolean.valueOf(rs.getString(7));

            	currency_code = rs.getString(11);
            	available = Boolean.valueOf(rs.getString(12));
            	country_code = rs.getString(13);
            	tstamp = rs.getString(14);
            	
               	denomination = rs.getInt(8);
           		min_price = rs.getInt(9);
           		max_price = rs.getInt(10);
            	
            	rewardCatalog.add(new RewardCatalog(catalogId,brand,image_url,type,description,sku,is_variable,denomination,min_price,max_price,currency_code,available,country_code,tstamp));
            }

            connection.close();
            return rewardCatalog;
        }
        catch(URISyntaxException e){
            e.getMessage();
            e.printStackTrace();
            return null;
        }
        catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
            return null;
        }
    	
    }
    
///////////////////////////////////////////////////////////////////////////////    
    //create if not exists
    private void createRewardCatalogTable(){
        System.out.println("RewardCatalogDB->createRewardCatalogTable");
        
        try{
	        connection = getConnection();
	        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_REWARD_CATALOG 
	                + "(catalogId BIGSERIAL PRIMARY KEY,"
	                + "brand VARCHAR(250) NOT NULL,"
	                + "image_url VARCHAR(250) NULL,"
	                + "type VARCHAR(250) NULL,"
	                + "description VARCHAR(250) NULL,"
	                + "sku VARCHAR(250) NOT NULL,"
	                + "is_variable VARCHAR(5) NULL,"
	                + "denomination integer NULL,"
	                + "min_price integer NULL,"
	                + "max_price integer NULL,"
	                + "currency_code VARCHAR(3) NULL,"
	                + "available VARCHAR(5) NULL,"
	                + "country_code VARCHAR(5) NULL,"
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
