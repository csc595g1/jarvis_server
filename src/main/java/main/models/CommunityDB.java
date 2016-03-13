/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.models;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ed
 * 
 * Table Structures:
 * community_post_tbl: post_id, email, content, dttm
 * community_reply_tbl: reply_id, post_id, email, content, dttm
 * upvote_post_tbl: post_id, dttm, email
 * upvote_reply_tbl: reply_id, email, dttm
 */
public class CommunityDB {
    
    Connection connection;
    //todo create separate class for user image stuff;
    private final String TABLE_COMMUNITY_POST_TBL = "community_post_tbl";
    private final String TABLE_COMMUNITY_REPLY_TBL = "community_reply_tbl";
    private final String TABLE_UPVOTE_POST_TBL = "upvote_post_tbl";
    private final String TABLE_UPVOTE_REPLY_TBL = "upvote_reply_tbl";
    
    
    public boolean insertReply(CommunityReplyModel model) throws SQLException{
        String sql = "insert into " + TABLE_COMMUNITY_REPLY_TBL + " (post_id,email,content,dttm) values (" + model.post_id + ",'" + model.email + "','" + model.content + "',now());";
        Statement stmt = connection.createStatement();
        int i = stmt.executeUpdate(sql);
        connection.close();
        if(i < 1){
            System.out.println("insert failed for reply");
            return false;}
        else return true;
    }
    
    public int getReplyCountForPost(int post_id) throws SQLException{
        int count = 0;
        String sql = "select count(*) from community_reply_tbl where post_id = " + post_id + ";";
        try{
            connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("debug query get post list");
            System.out.println("-------------------------");
            while(rs.next()){
                count = rs.getInt(1);
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
        finally{connection.close();}
        System.out.println("Reply count: " + count);
        return count;
    }
    
    public List<CommunityPostModel> getPostList() throws SQLException{
        List<CommunityPostModel> returnList = new ArrayList();
        
        String sql = "select * from " + TABLE_COMMUNITY_POST_TBL + ";";
        
        try{
            connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("debug query get post list");
            System.out.println("-------------------------");
            while(rs.next()){
                CommunityPostModel model = new CommunityPostModel();
                model.post_id = rs.getInt(1);
                model.email = rs.getString(2);
                model.content = rs.getString(3);
                model.dttm = rs.getString(4);
                returnList.add(model);
                System.out.println(model.post_id+", " + model.email+", " +model.content+", " +model.dttm);
            }
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
        finally{connection.close();}
        return returnList;
    }
    
    public List<CommunityReplyModel> getRepliesForPost(int post_id) throws SQLException{
        List<CommunityReplyModel> modelList = new ArrayList();
        String sql = "select * from " + TABLE_COMMUNITY_REPLY_TBL + " where post_id = " + post_id+";";
        try{
            connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("debug query get reply list");
            System.out.println("-------------------------");
            while(rs.next()){
                CommunityReplyModel model = new CommunityReplyModel();
                model.reply_id = rs.getInt(1);
                model.post_id = rs.getInt(2);
                model.email = rs.getString(3);
                model.content = rs.getString(4);
                model.dttm = rs.getString(5);
                modelList.add(model);
            }
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
        finally{connection.close();}
        return modelList;
    }
    
    
    /**
     * Build tables in following order:
     * 1. community post
     * 2. replies
     * 3. upvote post
     * 4. upvote reply
     */
    public CommunityDB(){
        this.createCommunityPostTable();
        this.createCommunityReplyTable();
        this.createUpvotePostTable();
        this.createUpvoteReplyTable();
}
    
    private void createUpvoteReplyTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "
                + TABLE_UPVOTE_REPLY_TBL
                + "(reply_id integer references community_reply_tbl(reply_id), "
                + "email varchar(250) NOT NULL, "
                + "dttm timestamp);";
        try{
            connection = getConnection();
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
    
    private void createUpvotePostTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "
                + TABLE_UPVOTE_POST_TBL
                + "(post_id integer references community_post_tbl(post_id), "
                + "email varchar(250) NOT NULL, "
                + "dttm timestamp);";
        try{
            connection = getConnection();
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
    
    private void createCommunityReplyTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "
                + TABLE_COMMUNITY_REPLY_TBL 
                + "(reply_id BIGSERIAL PRIMARY KEY, "
                + "post_id integer references community_post_tbl(post_id), "
                + "email varchar(250) NOT NULL, "
                + "content VARCHAR(250) NOT NULL, "
                + "dttm timestamp);";
        try{
            connection = getConnection();
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
    
    private void createCommunityPostTable(){
        String sql = "CREATE TABLE IF NOT EXISTS "
                + TABLE_COMMUNITY_POST_TBL 
                + "(post_id BIGSERIAL PRIMARY KEY, "
                + "email VARCHAR(250) NOT NULL, "
                + "content VARCHAR(250) NOT NULL, "
                + "dttm timestamp);";
        
        try{
            connection = getConnection();
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

        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + dbUri.getPath();
        
        return DriverManager.getConnection(dbUrl, username, password);
       
    }    
    
}
