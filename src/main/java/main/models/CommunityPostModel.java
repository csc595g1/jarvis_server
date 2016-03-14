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
public class CommunityPostModel {
    public int post_id;
    public String email;
    public String content;
    public String dttm;
    
    public static CommunityPostModel parsePostJson(String json){
        CommunityPostModel model = new CommunityPostModel();
        try{
            JSONObject input = new JSONObject(json);
            if(input.has("email")){
                model.email = input.getString("email");
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
