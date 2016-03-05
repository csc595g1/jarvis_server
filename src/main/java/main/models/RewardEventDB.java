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

public class RewardEventDB {

    private final String TABLE_REWARD_EVENTS = "reward_event_tbl";
    Connection connection;
    
    public RewardEventDB(){
        this.createRewardEventTable();
    }
    
    public ArrayList<RewardEvent> getRewardEventsByUserId(String userId){
        String eventId = "";
        String eventCategory = "";
        Integer units = 0;
        String title = "";
        String tstamp = "";
        ArrayList<RewardEvent> rewardEvents = new ArrayList<RewardEvent>();
               
        String sql = "SELECT eventId, userId, eventCategory, units, title, tstamp FROM " + TABLE_REWARD_EVENTS + " WHERE userId = '" + userId + "' order by tstamp desc;";
        try{
            connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
            	eventId = rs.getString(1);
            	eventCategory = rs.getString(3);
            	units = rs.getInt(4);
            	title = rs.getString(5);
            	tstamp = rs.getString(6);
            	rewardEvents.add(new RewardEvent(eventId,userId,eventCategory,units,title,tstamp));
            }

            connection.close();
            return rewardEvents;
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
    
    public int getTotalPointsForUser(String email) throws SQLException{
        String sql = "select coalesce(sum(units),0) as points from "+TABLE_REWARD_EVENTS+" where userid = '"+email+"';";
        int sum = 0;
        
        try{
            connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                sum = rs.getInt("points");
                System.out.println("sum = " + sum);
                return sum;
            }
        }
        catch(URISyntaxException e){
            e.getMessage();
            e.printStackTrace();
            return 0;
        }
        catch(SQLException e){
            e.getMessage();
            e.printStackTrace();
            return 0;
        }
        finally{
            connection.close();
        }
        
        return sum;
    }
    
    public boolean insertRewardEvent(RewardEvent event) throws SQLException{
        System.out.println("insertrewardevent called...");
        boolean isCreated = false;
        int count = 0;
        String sql;
        
        try{
        	Statement statement;
            connection = getConnection();        
            sql = "INSERT INTO " 
            		+ TABLE_REWARD_EVENTS 
            		+ " (userId,eventCategory,units,title,tstamp) VALUES ("
            		+ " '"+event.getUserId()+"'"
            		+ ",'"+event.getEventCategory()+"'"
            		+ ","+event.getUnits()
            		+ ",'"+event.getTitle()+"'"
            		+ ",'now');";
            statement = connection.createStatement();
            //isCreated = statement.execute(sql);
            statement.executeUpdate(sql);
            System.out.println("isCreate = " + isCreated);
            System.out.println("INSERT INTO " + TABLE_REWARD_EVENTS + " "+ event.getUserId());
            
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
    
    public Boolean createRewardEventRecords() {
        boolean isCreated = false;
        int count = 0;
        String sql;
        
        try{
        	Statement statement;
            connection = getConnection();        
            sql = "INSERT INTO " 
            		+ TABLE_REWARD_EVENTS 
            		+ " (userId,eventCategory,units,title,tstamp) VALUES ("
            		+ " 'test1@test.com'"
            		+ ",'maintenance'"
            		+ ",5"
            		+ ",'Sump Pump Maintenance'"
            		+ ",'now');";
            statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("INSERT INTO " + TABLE_REWARD_EVENTS + " test1@test.com");
            
            statement.clearBatch();
            statement.close();
            connection.close();
            
            connection = getConnection();
            sql = "INSERT INTO " 
            		+ TABLE_REWARD_EVENTS 
            		+ " (userId,eventCategory,units,title,tstamp) VALUES ("
            		+ " 'test2@test.com'"
            		+ ",'maintenance'"
            		+ ",5"
            		+ ",'Water Heater Maintenance'"
            		+ ",'now');";
            statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("INSERT INTO " + TABLE_REWARD_EVENTS + " test2@test.com");
            
            statement.clearBatch();
            statement.close();
            connection.close();
            
            connection = getConnection();
            sql = "INSERT INTO " 
            		+ TABLE_REWARD_EVENTS 
            		+ " (userId,eventCategory,units,title,tstamp) VALUES ("
            		+ " 'test3@test.com'"
            		+ ",'article'"
            		+ ",10"
            		+ ",'Article on Sump Pump Maintenance'"
            		+ ",'now');";
            statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("INSERT INTO " + TABLE_REWARD_EVENTS + " test3@test.com");
            
            statement.clearBatch();
            statement.close();
            connection.close();
            
            connection = getConnection();
            sql = "INSERT INTO " 
            		+ TABLE_REWARD_EVENTS 
            		+ " (userId,eventCategory,units,title,tstamp) VALUES ("
            		+ " 'test3@test.com'"
            		+ ",'appointment'"
            		+ ",10"
            		+ ",'Appointment for Sump Pump Maintenance'"
            		+ ",'now');";
            statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("INSERT INTO " + TABLE_REWARD_EVENTS + " test3@test.com");
            
            statement.clearBatch();
            statement.close();
            connection.close();
            
            
            isCreated = true;

            connection.close();
        }
        catch(URISyntaxException e){
            e.getMessage();
            e.printStackTrace();}
        catch(SQLException e){
            e.getMessage();
            e.printStackTrace();}
        
        System.out.println("isCreated for createRewardEventRecords = " + isCreated);
        return isCreated;
    }
    
    //create if not exists
    private void createRewardEventTable(){
        
        try{
        connection = getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_REWARD_EVENTS 
                + "(eventId BIGSERIAL PRIMARY KEY,"
                + "userId VARCHAR(250) NOT NULL,"
                + "eventCategory VARCHAR(25) NOT NULL,"
                + "units integer NOT NULL,"
                + "title VARCHAR(250) NOT NULL,"
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
        
//        Properties props = new Properties();
//        props.setProperty("user",username);
//        props.setProperty("password", password);
//        props.setProperty("ssl", "true");
        
        return DriverManager.getConnection(dbUrl, username, password);
        //return DriverManager.getConnection(dbUrl,props);
       
    }

}
