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

import android.location.Location;

import com.cse190.petcafe.PetInformation;
import com.cse190.petcafe.UserProfileInformation;

//import android.net.http.AndroidHttpClient;

public class Petcafe_api {

    private InputStream inputStream;
    private HttpClient httpclient;
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

    /*
     * USER FUNCTIONS
     */

    public JSONArray addUser(UserProfileInformation person) throws JSONException{
        try {
            client = new HttpPost(url + "user");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("fb_id", person.getFacebookUID());
            jsonObject.put("name", person.getUserName());
            jsonObject.put("first_lang", person.getFirstLanguage());
            jsonObject.put("second_lang", person.getSecondLanguage());
            jsonObject.put("latitude", person.getLatitude());
            jsonObject.put("longitude", person.getLongitude());
            //jsonObject.put("Available Time", person.availableTime());
            jsonObject.put("status", person.getStatus());
            //jsonObject.put("age",  person.getAge());

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

            jsonObject.put("fb_id", person.getFacebookUID());
            jsonObject.put("name", person.getUserName());
            jsonObject.put("first_lang", person.getFirstLanguage());
            jsonObject.put("second_lang", person.getSecondLanguage());
            jsonObject.put("latitude", person.getLatitude());
            jsonObject.put("longitude", person.getLongitude());
            //jsonObject.put("Available Time", person.availableTime());
            jsonObject.put("status", person.getStatus());
            //jsonObject.put("age",  person.getAge());

            jsonObject.put("key", makeKey("fb_id", person.getFacebookUID()));

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

            jsonObject.put("fb_id", person.getFacebookUID());

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

            jsonObject.put("fb_id", person.getFacebookUID());

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

    /*
     * FRIEND FUNCTIONS
     */

    public JSONArray addFriend(FriendInformation person1, FriendInformation person2, boolean fromFacebook) throws JSONException{
        try {
            client = new HttpPost(url+"friends");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            if(Integer.parseInt(person1.getFriendFacebookID()) < Integer.parseInt(person2.getFriendFacebookID())) {
                jsonObject.put("fb_id1", person1.getFriendFacebookID());
                jsonObject.put("fb_id2", person2.getFriendFacebookID());
            }

            else {
                jsonObject.put("fb_id1", person2.getFriendFacebookID());
                jsonObject.put("fb_id2", person1.getFriendFacebookID());
            }
            jsonObject.put("from_facebook", fromFacebook ? "1" : "0");

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

    // FOR "UNFRIEND" OPTION
    public JSONArray deleteFriend(FriendInformation person1, FriendInformation person2) throws JSONException{
        try {
            client = new HttpPost(url+"friends");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            /*
            if(Integer.parseInt(person1.getFriendFacebookID()) < Integer.parseInt(person2.getFriendFacebookID())) {
                jsonObject.put("fb_id1", person1.getFriendFacebookID());
                jsonObject.put("fb_id2", person2.getFriendFacebookID());
            }

            else {
                jsonObject.put("fb_id1", person2.getFriendFacebookID());
                jsonObject.put("fb_id2", person1.getFriendFacebookID());
            }*/

            jsonObject.put("fb_id", person1.getFriendFacebookID());

            JSONArray ja2 = new JSONArray();
            ja2.put(person2.getFriendFacebookID());

            jsonObject.put("friends", ja2);


            jsonObject.put("verify", "false");

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

    public JSONArray getFriends(UserProfileInformation person) throws JSONException{
        try {
            client = new HttpPost(url+"friends");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("fb_id", person.getFacebookUID());

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

    // This function is only for REQUESTING for 'friending"
    public JSONArray requestFriend(FriendInformation person1, FriendInformation person2) throws JSONException{
        try {
            client = new HttpPost(url+"requestFriend");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("requester_id", person1.getFriendFacebookID());
            jsonObject.put("friend_id", person2.getFriendFacebookID());

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


    // IMPORTANT!!!!!! REQUESTER ALWAYS COMES FIRST!!!
    public JSONArray rejectFriend(FriendInformation person1, FriendInformation person2) throws JSONException{
        try {
            client = new HttpPost(url+"rejectFriend");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("requester_id", person1.getFriendFacebookID());
            jsonObject.put("rejecter_id", person2.getFriendFacebookID());

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

    public JSONArray getRequest(UserProfileInformation person) throws JSONException{
        try {
            client = new HttpPost(url+"requestFriend");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("friend_id", person.getFacebookUID());

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

    public JSONArray getReject(UserProfileInformation person) throws JSONException{
        try {
            client = new HttpPost(url+"rejectFriend");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("requester_id", person.getFacebookUID());

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

    public JSONArray verifyFriend(String[] friendsFbIDArray) throws JSONException{
        try {
            client = new HttpPost(url+"friends");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            for(int i = 0; i < friendsFbIDArray.length; i++) {
                jsonObject.put("fb_id" + i, friendsFbIDArray[i]);
            }

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


    /*
     * PET FUNCTIONS
     */
    public JSONArray addPet(PetInformation pet) throws JSONException{
        try {
            client = new HttpPost(url+"pet");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("owner_id", pet.getPetOwnerFacebookID());
            jsonObject.put("name", pet.getPetName());
            jsonObject.put("species", pet.getPetSpecies());
            jsonObject.put("breed", pet.getPetBreed());
            jsonObject.put("gender", pet.getPetGender());
            jsonObject.put("age", pet.getPetAge());
            jsonObject.put("description", pet.getPetDescription());

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

    public JSONArray modifyPet(PetInformation pet) throws JSONException{
        try {
            client = new HttpPost(url+"pet");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("owner_id", pet.getPetOwnerFacebookID());
            jsonObject.put("name", pet.getPetName());
            jsonObject.put("species", pet.getPetSpecies());
            jsonObject.put("breed", pet.getPetBreed());
            jsonObject.put("gender", pet.getPetGender());
            jsonObject.put("age", pet.getPetAge());
            jsonObject.put("description", pet.getPetDescription());

            jsonObject.put("key", makeKey("owner_id", pet.getPetOwnerFacebookID()));

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

    public JSONArray deletePet(PetInformation pet) throws JSONException{
        try {
            client = new HttpPost(url+"pet");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("owner_id", pet.getPetOwnerFacebookID());
            jsonObject.put("name", pet.getPetName());

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
            client = new HttpPost(url+"pet");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("name", pet.getPetName());
            jsonObject.put("species", pet.getPetSpecies());
            jsonObject.put("breed", pet.getPetBreed());
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

    public JSONArray getMyPets(UserProfileInformation person) throws JSONException{
        try {
            client = new HttpPost(url+"pet");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("uid", person.getFacebookUID());

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

    /*
     * IMAGE FUNCTIONS
     */
    public JSONArray addImage(byte[] byteA, BlogPostInformation... post) throws JSONException{
        try {
            client = new HttpPost(url+"postImage");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("post_id", post[0].getId());
            jsonObject.put("image", byteA);

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

    public JSONArray addImage(byte[] byteA, PetInformation... pet) throws JSONException{
        try {
            client = new HttpPost(url+"petImage");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("pet_id", pet[0].getPetID());
            jsonObject.put("image", byteA);

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

    public JSONArray getPostImage(BlogPostInformation blog) throws JSONException{
        try {
            client = new HttpPost(url+"postImage");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("post_id", blog.getId());

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

    public JSONArray getPostImage(PetInformation pet) throws JSONException{
        try {
            client = new HttpPost(url+"petImage");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("pet_id", pet.getPetID());

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

    /*
     * POST FUNCTIONS
     */
    public JSONArray addPost(BlogPostInformation post) throws JSONException{
        try {
            client = new HttpPost(url+"post");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("author_id", post.getFacebookId());
            jsonObject.put("title", post.getTitle());
            jsonObject.put("type", post.getType());
            jsonObject.put("body", post.getBody());
            jsonObject.put("rating", post.getRating());
            jsonObject.put("tag", post.getTag());

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

    public JSONArray modifyPost(BlogPostInformation post) throws JSONException{
        try {
            client = new HttpPost(url+"post");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("author_id", post.getFacebookId());
            jsonObject.put("title", post.getTitle());
            jsonObject.put("type", post.getType());
            jsonObject.put("body", post.getBody());
            jsonObject.put("rating", post.getRating());
            jsonObject.put("tag", post.getTag());

            jsonObject.put("key", makeKey("author_id", post.getFacebookId()));

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

    public JSONArray deletePost(BlogPostInformation post) throws JSONException{
        try {
            client = new HttpPost(url+"post");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("author_id", post.getFacebookId());
            jsonObject.put("title", post.getTitle());

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

    public JSONArray getMyPost(String facebookID) throws JSONException{
        try {
            client = new HttpPost(url+"post");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("author_id", facebookID);

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

    public JSONArray getFriendsPost(String facebookID) throws JSONException{
        try {
            client = new HttpPost(url+"post");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("fb_id", facebookID);

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

    // SEARCHING FUNCTION FOR THE POST
    public JSONArray getPostByFilters (String title, String tag, String type) throws JSONException{
        try {
            client = new HttpPost(url+"post");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("title", title);
            jsonObject.put("tag", tag);
            jsonObject.put("type", type);

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

    /*
     * COMMENTS FUNCTIONS
     */
    public JSONArray addComment(UserProfileInformation person, BlogPostInformation post, String body) throws JSONException{
        try {
            client = new HttpPost(url+"comment");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            //TODO: determine "pid" jsonObject.put("pid", post.getFacebookId());
            jsonObject.put("post_id", post.getId());
            jsonObject.put("commenter_id", person.getFacebookUID());
            jsonObject.put("body", body);

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

    public JSONArray deleteComment(CommentInformation comment) throws JSONException{
        try {
            client = new HttpPost(url+"comment");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            //TODO: determine "pid" jsonObject.put("pid", post.getFacebookId());
            jsonObject.put("id", comment.getComment_id());

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

    public JSONArray getComment(UserProfileInformation person, String body) throws JSONException{
        try {
            client = new HttpPost(url+"comment");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            //TODO: determine "pid" jsonObject.put("pid", post.getFacebookId());
            jsonObject.put("commenter_id", person.getFacebookUID());
            jsonObject.put("body", body);

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

    /*
     * GEOLOCATION GETTER
     */
    public JSONArray getNearPeople(UserProfileInformation user) throws JSONException{
        try {
            client = new HttpPost(url+"user");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();

            jsonObject.put("longitude", user.getLongitude());
            jsonObject.put("latitude", user.getLatitude());
            jsonObject.put("radius", 100);
            //jsonObject.put("pets", );
            //jsonObject.put("first_lang", );
            jsonObject.put("fb_id", user.getFacebookUID());

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


    /*
     * HELPER FUNCTION
     */
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

    private JSONObject makeKey(String key, String val) throws JSONException {

        JSONObject j = new JSONObject();
        j.put(key, val);
        return j;
    }
    
    
    // get near people 
    public JSONArray getUserByLocation(UserProfileInformation person) throws JSONException{
        try {
            client = new HttpPost(url + "user");
            JSONArray ja = new JSONArray();
            jsonObject = new JSONObject();
            
            jsonObject.put("fb_id", person.getFacebookUID());
            jsonObject.put("longitude", person.getLongitude());
            jsonObject.put("latitude", person.getLatitude());
            
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
}
