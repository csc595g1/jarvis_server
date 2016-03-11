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
import java.sql.SQLException;
import java.sql.Statement;

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
