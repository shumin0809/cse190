package com.cse190.petcafe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
//import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.eclipse.jetty.util.log.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;

public class Petcafe_api {
	
	private InputStream inputStream;
	private DefaultHttpClient httpclient;
	private HttpResponse httpResponse;
	private String json, body;
	private JSONObject jsonObject;
	private JSONObject result;
	private final String url = "https://serene-headland-1129.herokuapp.com/";
	private HttpPost client;
	
	public Petcafe_api() {
		// constructor
		inputStream = null;
		json = "";
		body = "";		
		httpclient = new DefaultHttpClient();
		jsonObject = new JSONObject();
		result = new JSONObject();
		client = new HttpPost();
	}

	public JSONArray addUser(UserProfileInformation person) throws JSONException{
        try {
            client = new HttpPost(url + "user");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();
            
            jsonObject.put("fbID", person.getFacebookUID());
            jsonObject.put("name", person.getUserName());
            jsonObject.put("first language", person.getFirstLanguage());
            jsonObject.put("second language", person.getSecondLanguage());
            jsonObject.put("latitude", person.getLatitude());
            jsonObject.put("longitude", person.getLongitude());
            //jsonObject.put("Available Time", person.availableTime());
            jsonObject.put("status", person.getStatus());
            jsonObject.put("age",  person.getAge());
            
			ja.put(jsonObject);
			json = ja.toString();
			
            StringEntity se = new StringEntity(json);
            client.setEntity(se);
	 		client.setHeader("METHOD", "CREATE");
			client.setHeader("Content-type", "application/json");
            
            httpResponse = httpclient.execute(client);		// execute!
            
            /*
            inputStream = httpResponse.getEntity().getContent();			// response
 
            if(inputStream != null)
                result = inputStreamToJSON(inputStream);
            else {
                result = null;
                System.out.println("Retuning inputStream is null!");
            }*/
            
            ResponseHandler<String> handler = new BasicResponseHandler();
			body = handler.handleResponse(httpResponse);
			
			int code = httpResponse.getStatusLine().getStatusCode();		// used for debugging
 
        } catch (Exception e) {
        	//Log.d("InputStream", e.getLocalizedMessage());
			e.printStackTrace();
			body = "[{msg:\"Request failed\"}]";
        }
        return new JSONArray(body);
    }
	
	public JSONArray modifyUser(UserProfileInformation person) throws JSONException{
        try {
            client = new HttpPost(url + "user");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();
            
            jsonObject.put("fbID", person.getFacebookUID());
            jsonObject.put("name", person.getUserName());
            jsonObject.put("first language", person.getFirstLanguage());
            jsonObject.put("second language", person.getSecondLanguage());
            jsonObject.put("latitude", person.getLatitude());
            jsonObject.put("longitude", person.getLongitude());
            //jsonObject.put("Available Time", person.availableTime());
            jsonObject.put("status", person.getStatus());
            jsonObject.put("age",  person.getAge());
            
			ja.put(jsonObject);
			json = ja.toString();
			
            StringEntity se = new StringEntity(json);
            client.setEntity(se);
	 		client.setHeader("METHOD", "UPDATE");
			client.setHeader("Content-type", "application/json");
            
            httpResponse = httpclient.execute(client);		// execute!
            
            /*
            inputStream = httpResponse.getEntity().getContent();			// response
 
            if(inputStream != null)
                result = inputStreamToJSON(inputStream);
            else {
                result = null;
                System.out.println("Retuning inputStream is null!");
            }*/
            
            ResponseHandler<String> handler = new BasicResponseHandler();
			body = handler.handleResponse(httpResponse);
			
			int code = httpResponse.getStatusLine().getStatusCode();		// used for debugging
 
        } catch (Exception e) {
            //Log.d("InputStream", e.getLocalizedMessage());
			e.printStackTrace();
			body = "[{msg:\"Request failed\"}]";
        }
        
        return new JSONArray(body);
    }
	
	public JSONArray deleteUser(UserProfileInformation person) throws JSONException{
        try {
            client = new HttpPost(url + "user");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();
            
            jsonObject.put("fbID", person.getFacebookUID());
            
			ja.put(jsonObject);
			json = ja.toString();
			
            StringEntity se = new StringEntity(json);
            client.setEntity(se);
	 		client.setHeader("METHOD", "DELETE");
			client.setHeader("Content-type", "application/json");
            
            httpResponse = httpclient.execute(client);		// execute!
            
            /*
            inputStream = httpResponse.getEntity().getContent();			// response
 
            if(inputStream != null)
                result = inputStreamToJSON(inputStream);
            else {
                result = null;
                System.out.println("Retuning inputStream is null!");
            }*/
            
            ResponseHandler<String> handler = new BasicResponseHandler();
			body = handler.handleResponse(httpResponse);
			
			int code = httpResponse.getStatusLine().getStatusCode();		// used for debugging
 
        } catch (Exception e) {
            //Log.d("InputStream", e.getLocalizedMessage());
			e.printStackTrace();
			body = "[{msg:\"Request failed\"}]";
        }

        return new JSONArray(body);
    }
	
	public JSONArray getUser(UserProfileInformation person) throws JSONException{
        try {
            client = new HttpPost(url + "user");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();
            
            jsonObject.put("fbID", person.getFacebookUID());
            
			ja.put(jsonObject);
			json = ja.toString();
			
            StringEntity se = new StringEntity(json);
            client.setEntity(se);
	 		client.setHeader("METHOD", "GET");
			client.setHeader("Content-type", "application/json");
            
            httpResponse = httpclient.execute(client);		// execute!
            
            /*
            inputStream = httpResponse.getEntity().getContent();			// response
 
            if(inputStream != null)
                result = inputStreamToJSON(inputStream);
            else {
                result = null;
                System.out.println("Retuning inputStream is null!");
            }*/
            
            ResponseHandler<String> handler = new BasicResponseHandler();
			body = handler.handleResponse(httpResponse);
			
			int code = httpResponse.getStatusLine().getStatusCode();		// used for debugging
 
        } catch (Exception e) {
            //Log.d("InputStream", e.getLocalizedMessage());
			e.printStackTrace();
			body = "[{msg:\"Request failed\"}]";
        }

        return new JSONArray(body);
    }
	
	public JSONArray addFriend(FriendInformation person1, FriendInformation person2) throws JSONException{
        try {
            client = new HttpPost(url+"/friend");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();
            
            jsonObject.put("fbID1", person1.getFriendFacebookID());
            jsonObject.put("fbID2", person2.getFriendName());
            
			ja.put(jsonObject);
			json = ja.toString();
			
            StringEntity se = new StringEntity(json);
            client.setEntity(se);
	 		client.setHeader("METHOD", "CREATE");
			client.setHeader("Content-type", "application/json");
            
            httpResponse = httpclient.execute(client);		// execute!
            
            /*
            inputStream = httpResponse.getEntity().getContent();			// response
 
            if(inputStream != null)
                result = inputStreamToJSON(inputStream);
            else {
                result = null;
                System.out.println("Retuning inputStream is null!");
            }*/
            
            ResponseHandler<String> handler = new BasicResponseHandler();
			body = handler.handleResponse(httpResponse);
			
			int code = httpResponse.getStatusLine().getStatusCode();		// used for debugging
 
        } catch (Exception e) {
            //Log.d("InputStream", e.getLocalizedMessage());
			e.printStackTrace();
			body = "[{msg:\"Request failed\"}]";
        }
        return new JSONArray(body);
    }
	
	public JSONArray deleteFriend(FriendInformation person1, FriendInformation person2) throws JSONException{
        try {
            client = new HttpPost(url+"/friend");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("fbID1", person1.getFriendFacebookID());
            jsonObject.put("fbID2", person2.getFriendName());
            
			ja.put(jsonObject);
			json = ja.toString();
			
            StringEntity se = new StringEntity(json);
            client.setEntity(se);
	 		client.setHeader("METHOD", "DELETE");
			client.setHeader("Content-type", "application/json");
            
            httpResponse = httpclient.execute(client);		// execute!
            
            /*
            inputStream = httpResponse.getEntity().getContent();			// response
 
            if(inputStream != null)
                result = inputStreamToJSON(inputStream);
            else {
                result = null;
                System.out.println("Retuning inputStream is null!");
            }*/
            
            ResponseHandler<String> handler = new BasicResponseHandler();
			body = handler.handleResponse(httpResponse);
			
			int code = httpResponse.getStatusLine().getStatusCode();		// used for debugging
 
        } catch (Exception e) {
            //Log.d("InputStream", e.getLocalizedMessage());
			e.printStackTrace();
			body = "[{msg:\"Request failed\"}]";
        }

        return new JSONArray(body);
    }
	
	public JSONArray addPet(PetInformation pet) throws JSONException{
        try {
            client = new HttpPost(url+"/pet");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("name", pet.getPetName());
            jsonObject.put("species", pet.getPetSpecies());
            jsonObject.put("gender", pet.getPetGender());
            jsonObject.put("age", pet.getPetAge());
            
			ja.put(jsonObject);
			json = ja.toString();
			
            StringEntity se = new StringEntity(json);
            client.setEntity(se);
	 		client.setHeader("METHOD", "CREATE");
			client.setHeader("Content-type", "application/json");
            
            httpResponse = httpclient.execute(client);		// execute!
            
            /*
            inputStream = httpResponse.getEntity().getContent();			// response
 
            if(inputStream != null)
                result = inputStreamToJSON(inputStream);
            else {
                result = null;
                System.out.println("Retuning inputStream is null!");
            }*/
            
            ResponseHandler<String> handler = new BasicResponseHandler();
			body = handler.handleResponse(httpResponse);
			
			int code = httpResponse.getStatusLine().getStatusCode();		// used for debugging
 
        } catch (Exception e) {
            //Log.d("InputStream", e.getLocalizedMessage());
			e.printStackTrace();
			body = "[{msg:\"Request failed\"}]";
        }

        return new JSONArray(body);
    }
	
	public JSONArray deletePet(PetInformation pet) throws JSONException{
        try {
            client = new HttpPost(url+"/pet");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("name", pet.getPetName());
            jsonObject.put("species", pet.getPetSpecies());
            jsonObject.put("gender", pet.getPetGender());
            jsonObject.put("age", pet.getPetAge());
            
			ja.put(jsonObject);
			json = ja.toString();
			
            StringEntity se = new StringEntity(json);
            client.setEntity(se);
	 		client.setHeader("METHOD", "DELETE");
			client.setHeader("Content-type", "application/json");
            
            httpResponse = httpclient.execute(client);		// execute!
            
            /*
            inputStream = httpResponse.getEntity().getContent();			// response
 
            if(inputStream != null)
                result = inputStreamToJSON(inputStream);
            else {
                result = null;
                System.out.println("Retuning inputStream is null!");
            }*/
            
            ResponseHandler<String> handler = new BasicResponseHandler();
			body = handler.handleResponse(httpResponse);
			
			int code = httpResponse.getStatusLine().getStatusCode();		// used for debugging
 
        } catch (Exception e) {
            //Log.d("InputStream", e.getLocalizedMessage());
			e.printStackTrace();
			body = "[{msg:\"Request failed\"}]";
        }

        return new JSONArray(body);
    }
	
	public JSONArray getPet(PetInformation pet) throws JSONException{
        try {
            client = new HttpPost(url+"/pet");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("name", pet.getPetName());
            jsonObject.put("species", pet.getPetSpecies());
            jsonObject.put("gender", pet.getPetGender());
            jsonObject.put("age", pet.getPetAge());
            
			ja.put(jsonObject);
			json = ja.toString();
			
            StringEntity se = new StringEntity(json);
            client.setEntity(se);
	 		client.setHeader("METHOD", "GET");
			client.setHeader("Content-type", "application/json");
            
            httpResponse = httpclient.execute(client);		// execute!
            
            /*
            inputStream = httpResponse.getEntity().getContent();			// response
 
            if(inputStream != null)
                result = inputStreamToJSON(inputStream);
            else {
                result = null;
                System.out.println("Retuning inputStream is null!");
            }*/
            
            ResponseHandler<String> handler = new BasicResponseHandler();
			body = handler.handleResponse(httpResponse);
			
			int code = httpResponse.getStatusLine().getStatusCode();		// used for debugging
 
        } catch (Exception e) {
            //Log.d("InputStream", e.getLocalizedMessage());
			e.printStackTrace();
			body = "[{msg:\"Request failed\"}]";
        }

        return new JSONArray(body);
    }
	
	// helper
    private JSONObject inputStreamToJSON(InputStream inputStream) throws IOException, JSONException {
    		 
    	StringBuffer buffer = new StringBuffer();
    	String line = null;
  
    	try {
    		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	  
    		while ((line = reader.readLine()) != null) {
    			buffer.append(line);
    		}
    	} 
    	
    	catch (Exception e) { /*report an error*/ }
 
    	JSONObject jsonObject = new JSONObject(buffer.toString());

  		return jsonObject;
	}
}
