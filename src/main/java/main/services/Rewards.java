package main.services;

import com.sun.jersey.api.json.JSONConfiguration;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import main.models.RewardEvent;
import main.models.RewardEventDB;
import main.models.RewardCatalog;
import main.models.RewardCatalogDB;

@Path("/rewards")
public class Rewards {
	
	RewardEventDB rewardEventDB = new RewardEventDB();

	@GET
	@Path("/testdata")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createTestData() throws JSONException{
		JSONObject jsonResponse = new JSONObject();
		
		createRewardEventTestData();
		
		try {
			jsonResponse.put("rewardEventTestData", "created");
		} catch (JSONException e) {
			// TODO Auto-generated catch block/events
			e.printStackTrace();
		}
		
		return Response.status(Response.Status.OK).entity(jsonResponse).build();
		
	}

	@GET
	@Path("/eventtypes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRewardsEventTypes() throws JSONException{
		JSONObject jsonResponse = new JSONObject();
		
		ArrayList<String> ary = getRewardEventTypes();
		
		try {
			jsonResponse.put("rewardEventTypes", new JSONArray(ary));
		} catch (JSONException e) {
			// TODO Auto-generated catch block/events
			e.printStackTrace();
		}
		
		return Response.status(Response.Status.OK).entity(jsonResponse).build();
		
	}

	@GET
	@Path("/getevents")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRewardsById(@QueryParam("email") String email) throws JSONException{
		JSONObject jsonResponse = new JSONObject();
		JSONArray jarray = new JSONArray();
		ArrayList<RewardEvent> ary = null;
		
		if (email != null) {
			ary = getRewards(email);
		}

		try {
                    for (RewardEvent r : ary){
                        jsonResponse = new JSONObject();
                        jsonResponse.put("eventId", r.getEventId());
                        jsonResponse.put("eventCategory", r.getEventCategory());
                        jsonResponse.put("title", r.getTitle());
                        jsonResponse.put("units", r.getUnits());
                        jsonResponse.put("dttm",r.getTstamp());
                        jarray.put(jsonResponse);
                        //jarray.add(jsonResponse);
                    }
			//jsonResponse.put("rewardEvents", new JSONArray(ary));
                        //System.out.println(jsonResponse);
		} catch (JSONException e) {
			// TODO Auto-generated catch block/events
			e.printStackTrace();
                        return Response.status(Response.Status.NO_CONTENT).build();
		}
		if(jarray.length() == 0){
                    return Response.status(Response.Status.NO_CONTENT).build();
                }
                jsonResponse = new JSONObject();
                jsonResponse.put("events", jarray);
		return Response.status(Response.Status.OK).entity(jsonResponse).build();
		
	}

//	@GET
//	@Path("/events")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response getRewards() {
//		JSONObject jsonResponse = new JSONObject();
//		
//		ArrayList<RewardEvent> ary = getRewards(null);
//		
//		try {
//			jsonResponse.put("rewardEvents", new JSONArray(ary));
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block/events
//			e.printStackTrace();
//		}
//		
//		return Response.status(Response.Status.OK).entity(jsonResponse).build();
//		
//	}

	@PUT
	@Path("/event")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRewardEvent(String requestJson) throws JSONException{
            System.out.println("web request to create a reward event");
            System.out.println(requestJson);
		JSONObject jsonRequest = new JSONObject(requestJson);
		JSONObject jsonResponse = new JSONObject();
		RewardEvent createRewardEvent = null;
                boolean success = false;
		
		if (jsonRequest.has("userId") &&
				jsonRequest.has("eventCategory") &&
				jsonRequest.has("units") &&
				jsonRequest.has("title")) {
			createRewardEvent = new RewardEvent("",
									jsonRequest.getString("userId"),
									jsonRequest.getString("eventCategory"),
									jsonRequest.getInt("units"),
									jsonRequest.getString("title"),
									"");
                        try{
			success = rewardEventDB.insertRewardEvent(createRewardEvent);
                        }
                        catch(SQLException e){
                            System.out.println("sql exception in service");
                            e.getMessage();
                            e.printStackTrace();
                        }
                        System.out.println("success = " + success);
		}

		//RewardEvent rewardEvent = createRewardEvent(createRewardEvent);
		//boolean success = rewardEventDB.insertRewardEvent(createRewardEvent);
		try {
			if (success) {
				jsonResponse.put("eventId", createRewardEvent.getEventId());
				jsonResponse.put("userId", createRewardEvent.getUserId());
				jsonResponse.put("eventCategory", createRewardEvent.getEventCategory());
				jsonResponse.put("units", createRewardEvent.getUnits().toString());
				jsonResponse.put("title", createRewardEvent.getTitle());
				jsonResponse.put("tstamp", createRewardEvent.getTstamp());
				jsonResponse.put("eventCreated", Boolean.TRUE.toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block/events
			e.printStackTrace();
		}
		
                if(success){
		return Response.status(Response.Status.OK).entity(jsonResponse).build();
                }
                else{
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
		
	}

	@DELETE
	@Path("/events/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRewardEventByUserId(@PathParam ("userId")  String userId) throws JSONException{
		JSONObject jsonResponse = new JSONObject();
		
		Boolean flag = Boolean.FALSE;
		if (userId != null) {
			flag = deleteRewardEvent(userId);
		} else {
			flag = Boolean.FALSE;
		}


		try {
			if (flag) {
				jsonResponse.put("eventsDeleted", Boolean.TRUE.toString());
			} else {
				jsonResponse.put("eventsDeleted", Boolean.FALSE.toString());
			}
			
		} catch (JSONException e) {
			jsonResponse.put("error", e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
		}
		
		return Response.status(Response.Status.OK).entity(jsonResponse).build();
		
	}

	@DELETE
	@Path("/event/{eventId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRewardEventByEventId(@PathParam ("eventId")  Integer eventId) throws JSONException{
		JSONObject jsonResponse = new JSONObject();
		
		Boolean flag = Boolean.FALSE;
		if (eventId != null) {
			flag = deleteRewardEvent(eventId);
		} else {
			flag = Boolean.FALSE;
		}


		try {
			if (flag) {
				jsonResponse.put("eventDeleted", Boolean.TRUE.toString());
			} else {
				jsonResponse.put("eventDeleted", Boolean.FALSE.toString());
			}
			
		} catch (JSONException e) {
			jsonResponse.put("error", e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
		}
		
		return Response.status(Response.Status.OK).entity(jsonResponse).build();
		
	}
	
	@DELETE
	@Path("/catalog")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRewardCatalog() throws JSONException{
		JSONObject jsonResponse = new JSONObject();
		
		Boolean flag = Boolean.FALSE;

		try {
			flag = this.deleteRewardCatalogDB();
			
			if (flag) {
				jsonResponse.put("catalogDeleted", Boolean.TRUE.toString());
			} else {
				jsonResponse.put("catalogDeleted", Boolean.FALSE.toString());
			}
			
		} catch (JSONException e) {
			jsonResponse.put("error", e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
		}
		
		return Response.status(Response.Status.OK).entity(jsonResponse).build();
		
	}

	@GET
	@Path("/catalog")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCatalog() throws JSONException{
        System.out.println("web request to get the catalog");
        
		JSONObject jsonResponse = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		ArrayList<RewardCatalog> ary = getCatalogItemsDB();
		
		try {
            for (RewardCatalog rewardCatalog : ary) {
                jsonResponse = new JSONObject();
                jsonResponse.put("catalogId", rewardCatalog.getCatalogId());
                jsonResponse.put("brand", rewardCatalog.getBrand());
                jsonResponse.put("image_url", rewardCatalog.getImage_url());
                jsonResponse.put("type", rewardCatalog.getType());
                jsonResponse.put("description", rewardCatalog.getDescription());
                jsonResponse.put("sku", rewardCatalog.getSku());
                jsonResponse.put("is_variable", rewardCatalog.getIs_variable());

                jsonResponse.put("denomination", String.valueOf(rewardCatalog.getDenomination()));
                jsonResponse.put("min_price", String.valueOf(rewardCatalog.getMin_price()));
                jsonResponse.put("max_price", String.valueOf(rewardCatalog.getMax_price()));
                
                jsonResponse.put("currency_code", rewardCatalog.getCurrency_code());
                jsonResponse.put("available", rewardCatalog.getAvailable());
                jsonResponse.put("country_code", rewardCatalog.getCountry_code());
                jsonResponse.put("dttm",rewardCatalog.getTstamp());
                
                jsonArray.put(jsonResponse);
            }
            //System.out.println(jsonResponse);
		} catch (JSONException e) {
			// TODO Auto-generated catch block/events
			e.printStackTrace();
            return Response.status(Response.Status.NO_CONTENT).build();
		}
		if (jsonArray.length() == 0) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
		
        jsonResponse = new JSONObject();
        jsonResponse.put("catalogItems", jsonArray);
		
        return Response.status(Response.Status.OK).entity(jsonResponse).build();
		
	}

	@GET
	@Path("/loadcatalog")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loadCatalog() throws JSONException{
        System.out.println("web request to load the catalog");
        
		JSONObject jsonResponse = new JSONObject();
		
		Boolean isLoaded = loadCatalogTable();
		
		try {
			if (isLoaded) {
				jsonResponse.put("catalogLoaded", Boolean.TRUE.toString());
			} else {
				jsonResponse.put("catalogLoaded", Boolean.FALSE.toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block/events
			e.printStackTrace();
		}
		
		return Response.status(Response.Status.OK).entity(jsonResponse).build();
		
	}

	

	/***************************/
	/* Internal Database calls */
	/***************************/
	private Boolean loadCatalogTable() {
        System.out.println("Rewards->loadCatalogTable");
		Boolean isLoaded = Boolean.FALSE;
		
		RewardCatalogDB rewardCatalogDB = new RewardCatalogDB();
		
		try {
			URL url = new URL("https://sandbox.tangocard.com/raas/v1.1/rewards");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", "Basic Q29ubmVjdGVkSG9tZVRlc3Q6OVp2a0F0THQyQmt6QUtYdHlidU1sTVh4QjJ3SVpMWmNWQXJIU0d3cTJXWEVoZldmTkNmc0VFaXlv");
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			
			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

//			String output;
//			System.out.println("Output from Server .... \n");
//			while ((output = br.readLine()) != null) {
//				System.out.println("loadCatalogTable->Reading catalog from Tango...");
//			}

			
			
			String output;
			StringBuilder jsonCatalog = new StringBuilder();
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println("loadCatalogTable->Reading catalog from Tango...");
				jsonCatalog.append(output);
			}

			JSONObject catalog = new JSONObject(jsonCatalog.toString());
			
			if (catalog.has("success") && catalog.getString("success") == "true") {
				System.out.println("Output from Catalog is success == true .... \n");
			} else {
				System.out.println("Output from Catalog is success == false .... \n");
			}
			
			conn.disconnect();
			
			isLoaded = Boolean.TRUE;

			
		} catch (MalformedURLException e) {
			isLoaded = Boolean.FALSE;
			e.printStackTrace();
		} catch (IOException e) {
			isLoaded = Boolean.FALSE;
			e.printStackTrace();
		} catch (JSONException e) {
			isLoaded = Boolean.FALSE;
			e.printStackTrace();
		}
		
		return isLoaded;
	}
	
	private Boolean deleteRewardCatalogDB() {
        System.out.println("Rewards->deleteRewardCatalogDB");
		Boolean isDeleted = Boolean.FALSE;
		
		RewardCatalogDB rewardCatalogDB = new RewardCatalogDB();
		
		isDeleted = rewardCatalogDB.deleteCatalog();
		
		return isDeleted;
	}
	
	private ArrayList<RewardCatalog> createCatalogItem(RewardCatalog rewardCatalog) {
        System.out.println("Rewards->createCatalogItem");
		
		ArrayList<RewardCatalog> ary = new ArrayList<RewardCatalog>();
		
		RewardCatalogDB rewardCatalogDB = new RewardCatalogDB();
		try {
			rewardCatalogDB.insertRewardCatalogItem(rewardCatalog);
		} catch(SQLException e){
            System.out.println("sql exception in service");
            e.getMessage();
            e.printStackTrace();
        } finally {
			rewardCatalogDB = null;        	
        }
		
		ary.add(rewardCatalog);

		return ary;
		
		
	}
	
	private ArrayList<RewardCatalog> getCatalogItemsDB() {
        System.out.println("Rewards->getCatalogItemsDB");
		
		ArrayList<RewardCatalog> rewardCatalog = new ArrayList<RewardCatalog>();
		
		RewardCatalogDB rewardCatalogDB = new RewardCatalogDB();
		
		rewardCatalog = rewardCatalogDB.getCatalogItems();
		
		return rewardCatalog;
	}
	
	private RewardEvent createRewardEvent(RewardEvent rewardEvent) {
		Boolean isCreated = Boolean.FALSE;
		RewardEvent createdRewardEvent = null;
		
		final Calendar now = Calendar.getInstance(TimeZone.getDefault());
		
		if (rewardEvent.getUserId().equals("test1@test.com")) {
			createdRewardEvent = new RewardEvent("0","test1@test.com","maintenance",5,"Sump Pump Maintenance",String.valueOf(now.getTimeInMillis()));
			isCreated = Boolean.TRUE;
		} else if (rewardEvent.getUserId().equals("test2@test.com")) {
			createdRewardEvent = new RewardEvent("0","test2@test.com","maintenance",5,"Water Heater Maintenance",String.valueOf(now.getTimeInMillis()));
			isCreated = Boolean.TRUE;
		} else if (rewardEvent.getUserId().equals("test3@test.com")) {
			createdRewardEvent = new RewardEvent("0","test3@test.com","article",10,"Article for community",String.valueOf(now.getTimeInMillis()));
			isCreated = Boolean.TRUE;
		} else {
			isCreated = Boolean.FALSE;
		}
		
		if (isCreated == Boolean.FALSE) {
			createdRewardEvent = null;
		} 

		return createdRewardEvent;
	}
	
	private Boolean deleteRewardEvent(String userId) {
		Boolean isDeleted = Boolean.FALSE;
		
		if (userId.equals("test1@test.com")) {
			isDeleted = Boolean.TRUE;
		} else if (userId.equals("test2@test.com")) {
			isDeleted = Boolean.TRUE;
		} else if (userId.equals("test3@test.com")) {
			isDeleted = Boolean.TRUE;
		} else {
			isDeleted = Boolean.FALSE;
		}
		
		return isDeleted;
	}
	
	private Boolean deleteRewardEvent(Integer eventId) {
		Boolean isDeleted = Boolean.FALSE;
		
		if (eventId.equals(0)) {
			isDeleted = Boolean.TRUE;
		} else if (eventId.equals(1)) {
			isDeleted = Boolean.TRUE;
		} else if (eventId.equals(2)) {
			isDeleted = Boolean.TRUE;
		} else {
			isDeleted = Boolean.FALSE;
		}
		
		return isDeleted;
	}
	
	private ArrayList<RewardEvent> getRewards(String userId) {
		
            ArrayList<RewardEvent> events = null;
            
            events = new RewardEventDB().getRewardEventsByUserId(userId);
            
            return events;
            
//		ArrayList<String> ary = new ArrayList<String>();
//		
//		if (userId == null){
//			ary.add("Test1OneRewardEvent");
//			ary.add("Test1TwoRewardEvent");
//			ary.add("Test1ThreeRewardEvent");
//			ary.add("Test1FourRewardEvent");
//			
//			ary.add("Test2OneRewardEvent");
//			ary.add("Test2TwoRewardEvent");
//			ary.add("Test2ThreeRewardEvent");
//			ary.add("Test2FourRewardEvent");
//			
//			ary.add("Test3OneRewardEvent");
//			ary.add("Test3TwoRewardEvent");
//			ary.add("Test3ThreeRewardEvent");
//			ary.add("Test3FourRewardEvent");
//		} else if (userId.equalsIgnoreCase("test1@test.com")){
//			ary.add("Test1OneRewardEvent");
//			ary.add("Test1TwoRewardEvent");
//			ary.add("Test1ThreeRewardEvent");
//			ary.add("Test1FourRewardEvent");
//		} else if (userId.equalsIgnoreCase("test2@test.com")) {
//			ary.add("Test2OneRewardEvent");
//			ary.add("Test2TwoRewardEvent");
//			ary.add("Test2ThreeRewardEvent");
//			ary.add("Test2FourRewardEvent");
//		} else if (userId.equalsIgnoreCase("test3@test.com")) {
//			ary.add("Test3OneRewardEvent");
//			ary.add("Test3TwoRewardEvent");
//			ary.add("Test3ThreeRewardEvent");
//			ary.add("Test3FourRewardEvent");
//		} else {
//			ary.add("No events found");
//		}
//
//		return ary;
		
	}
	
	private ArrayList<String> getRewardEventTypes() {
		
		ArrayList<String> ary = new ArrayList<String>();
		
		ary.add("maintenance");
		ary.add("article");
		ary.add("appointment");
		ary.add("luck");

		return ary;
		
	}
	
	private void createRewardEventTestData() {
		
		rewardEventDB.createRewardEventRecords();
		
	}
	
}
