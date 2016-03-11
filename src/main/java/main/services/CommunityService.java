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
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import main.models.CommunityDB;
import main.models.CommunityPostModel;
import main.models.UserLoginDB;

/**
 *
 * @author Ed
 */
@Path("/community")
public class CommunityService {
    
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
                jsonResp = new JSONObject();
                jsonResp.put("post_id", model.post_id);
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
