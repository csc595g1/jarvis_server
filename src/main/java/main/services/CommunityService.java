/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.services;

import com.sun.jersey.api.json.JSONConfiguration;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import main.models.CommunityDB;
import main.models.CommunityPostModel;
import main.models.CommunityReplyModel;
import main.models.RewardEvent;
import main.models.RewardEventDB;
import main.models.UserLoginDB;

/**
 *
 * @author Ed
 */
@Path("/community")
public class CommunityService {
    
    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertPost(String json)throws SQLException,URISyntaxException{
        //insert upvote
        CommunityPostModel model = CommunityPostModel.parsePostJson(json);
        CommunityDB commDB = new CommunityDB();
        commDB.insertPost(model);
        
        //insert points for upvote
        RewardEvent event = new RewardEvent();
        event.setEventCategory("Community");
        event.setTitle("Posted to Community");
        event.setUnits(50);
        event.setUserId(model.email);
        RewardEventDB eventdb = new RewardEventDB();
        eventdb.insertRewardEvent(event);
        
        //return OK
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/upvotespost")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUpvotesForPost(@QueryParam("post_id") String post_id)throws SQLException,JSONException{
        int convertPostId = Integer.parseInt(post_id);
        CommunityDB commDB = new CommunityDB();
        int count = commDB.getReplyCountForPost(convertPostId);
        JSONObject obj = new JSONObject();
        obj.put("count", count);
        return Response.status(Response.Status.OK).entity(obj).build();
    }
    
    @GET
    @Path("/userupvoted")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUpvotesForUserByPost(@QueryParam("post_id") String post_id, @QueryParam("email") String email)throws SQLException,JSONException,URISyntaxException{
        int convertPostId = Integer.parseInt(post_id);
        CommunityDB commDB = new CommunityDB();
        boolean hasPosted = commDB.hasUserUpvoted(email, convertPostId);
        JSONObject obj = new JSONObject();
        obj.put("hasUserUpvoted", hasPosted);
        return Response.status(Response.Status.OK).entity(obj).build();
    }
    
    @POST
    @Path("/postupvote")
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertUpvoteForUser(@QueryParam("post_id") String post_id, @QueryParam("email") String email)throws SQLException,JSONException,URISyntaxException{
        int convertPostId = Integer.parseInt(post_id);
        CommunityDB commDB = new CommunityDB();
        commDB.insertPostUpvote(email, convertPostId);
        
        //get email of poster
        String origemail = commDB.getEmailForPost(Integer.parseInt(post_id));
        
        //insert points for upvote
        RewardEvent event = new RewardEvent();
        event.setEventCategory("Upvote");
        event.setTitle("Upvoted Post");
        event.setUnits(10);
        event.setUserId(origemail);
        RewardEventDB eventdb = new RewardEventDB();
        eventdb.insertRewardEvent(event);
        
        
        return Response.status(Response.Status.OK).build();
    }
    
    @POST
    @Path("/reply")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postReply(String requestJson) throws JSONException, SQLException,URISyntaxException{
        CommunityReplyModel model = CommunityReplyModel.parseReply(requestJson);
        CommunityDB db = new CommunityDB();
        db.insertReply(model);
        
        //insert points for upvote
        RewardEvent event = new RewardEvent();
        event.setEventCategory("Reply");
        event.setTitle("Replied to Post");
        event.setUnits(10);
        event.setUserId(model.email);
        RewardEventDB eventdb = new RewardEventDB();
        eventdb.insertRewardEvent(event);
        
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/replies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRepliesForPost(@QueryParam("post_id") String post_id) throws JSONException{
        int convertPostId = Integer.parseInt(post_id);
        CommunityDB commDB = new CommunityDB();
        List<CommunityReplyModel> modelList;
        JSONObject jsonResp = new JSONObject();
        JSONArray jarray = new JSONArray();
        
        try{
            modelList = commDB.getRepliesForPost(convertPostId);
            for(CommunityReplyModel model : modelList){
                String name = new UserLoginDB().getUserName(model.email);
                jsonResp = new JSONObject();
                jsonResp.put("reply_id", model.reply_id);
                jsonResp.put("post_id", model.post_id);
                jsonResp.put("email", model.email);
                jsonResp.put("name", name);
                jsonResp.put("content", model.content);
                jsonResp.put("dttm", model.dttm);
                jarray.put(jsonResp);
            }
        }
        catch (SQLException e) {
			// TODO Auto-generated catch block/events
			e.printStackTrace();
                        return Response.status(Response.Status.NO_CONTENT).build();
		}
        catch (JSONException e) {
			// TODO Auto-generated catch block/events
			e.printStackTrace();
                        return Response.status(Response.Status.NO_CONTENT).build();
		}
        
        if(jarray.length() == 0){
                    return Response.status(Response.Status.NO_CONTENT).build();
                }
        jsonResp = new JSONObject();
        jsonResp.put("replies", jarray);
        return Response.status(Response.Status.OK).entity(jsonResp).build();
    }
    
    @GET
    @Path("/getPosts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosts()throws JSONException{
        CommunityDB commDB = new CommunityDB();
        List<CommunityPostModel> modelList;
        JSONObject jsonResp = new JSONObject();
        JSONArray jarray = new JSONArray();
        
        try{
            modelList = commDB.getPostList();
            for(CommunityPostModel model:modelList){
                String name = new UserLoginDB().getUserName(model.email);
                int count = commDB.getReplyCountForPost(model.post_id);
                jsonResp = new JSONObject();
                jsonResp.put("post_id", model.post_id);
                jsonResp.put("countOfReplies",count);
                jsonResp.put("email",model.email);
                jsonResp.put("name", name);
                jsonResp.put("content",model.content);
                jsonResp.put("dttm",model.dttm);
                jarray.put(jsonResp);
            }
        }catch (SQLException e) {
			// TODO Auto-generated catch block/events
			e.printStackTrace();
                        return Response.status(Response.Status.NO_CONTENT).build();
		}
        catch (JSONException e) {
			// TODO Auto-generated catch block/events
			e.printStackTrace();
                        return Response.status(Response.Status.NO_CONTENT).build();
		}
        //just to instantiate everything for now...
        //System.out.println("building tables....");
        //return Response.status(Response.Status.NO_CONTENT).build();
        if(jarray.length() == 0){
                    return Response.status(Response.Status.NO_CONTENT).build();
                }
        jsonResp = new JSONObject();
        jsonResp.put("posts", jarray);
        return Response.status(Response.Status.OK).entity(jsonResp).build();
    }
}
