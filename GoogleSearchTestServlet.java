
package com.example.myproject;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusRequestInitializer;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.sql.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GoogleSearchTestServlet extends HttpServlet {


  private static final String API_KEY = "AIzaSyBuTyKN7QxiZSV9TNCgIs5OFY_68L7sfD8";

  private static final long serialVersionUID = 1;

  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
  static final String DB_URL = "jdbc:mysql://localhost/sample";

  //  Database credentials
  static final String USER = "username";
  static final String PASS = "password";
  Connection conn = null;
  Statement stmt = null;
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpTransport httpTransport = new UrlFetchTransport();
    JsonFactory jsonFactory = new JacksonFactory();

    Plus plus = new Plus.Builder(httpTransport, jsonFactory, null).setApplicationName("")
    					.setGoogleClientRequestInitializer(new PlusRequestInitializer(API_KEY)).build();

    ActivityFeed myActivityFeed = plus.activities().search("Google").execute();
    List<Activity> myActivities = myActivityFeed.getItems();

    //resp.setContentType("text/html");
    resp.setStatus(200);
    try 
    {
	    Class.forName("com.mysql.jdbc.Driver");
	    conn = DriverManager.getConnection(DB_URL, USER, PASS);
	    stmt = conn.createStatement();
	    for (Activity a : myActivities) 
	    {
	    	String sql = "INSERT INTO SAMPLETABLE VALUES(" + a.getTitle()+","+a.getUrl()+")";
	    	stmt.executeUpdate(sql);
	    }    
	    stmt.close();
	    conn.close();
    }
    catch(Exception e)
    {
      //Handle errors for Class.forName
      e.printStackTrace();
    }
 }
}