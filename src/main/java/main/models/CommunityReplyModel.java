/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.models;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Ed
 */
public class CommunityReplyModel {
    public int post_id,reply_id;
    public String email,content,dttm;
    
    public static CommunityReplyModel parseReply(String json) {
        CommunityReplyModel model = new CommunityReplyModel();
        try{
            JSONObject input = new JSONObject(json);
            if(input.has("post_id")){
                model.post_id = Integer.valueOf(input.getString("post_id"));
            }
            
            if(input.has("email")){
                model.email = input.getString("post_id");
            }
            
            if(input.has("content")){
                model.content = input.getString("content");
            }
            return model;
        }
        catch(JSONException e){}
        return model;
    }
}
