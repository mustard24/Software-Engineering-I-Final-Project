/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Dustin Moody
 */
package cs310;

import java.sql.*;
import java.util.*;
import org.json.simple.*;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;


public class TASDatabase{
    
    //Objects for get methods
    //private Punch punchQuery = new Punch();
    //private Badge badgeQuery = new Badge();
    //private Shift shiftQuery = new Shift(); 
    
    Connection conn;
    Statement stmt;

    public TASDatabase(){

        
        //Opens connection to database
        
        try{

            /* Identify the Server */

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String server = "jdbc:mysql://localhost/tas";
            String username = "teamc";
            String password = "CS310";
            System.out.println("Connecting to " + server + "...");
            
            
            /* Open Connection and statement*/
        
            conn = DriverManager.getConnection(server, username, password);
            stmt = conn.createStatement();
            
            /* Test Connection */
            
            if(conn.isValid(0)){
                
                /* Connection Open! */
                
                System.out.println("Connected Succesfully!"); 

            }
            
            else{
                
                System.out.println("Connection failed!"); 
                
            }
            
            
        }
        
        
        catch (Exception e) {
            System.err.println(e.toString());
        }
        
    }
    
    //Closes Connection
    public void closeConnection(){
        
        try{
            conn.close();
        }
        catch(Exception e){
            System.err.println(e.toString());
        }
        
    }
    
    //Closes Statement
    public void closeStatement(Statement stmt){
        
        try{
            stmt.close();
        }
        
        catch(Exception e){
            System.err.println(e.toString());
        }
    }
    
   public Badge getBadge(String id){
       
       Badge badgeQuery = null;
       boolean hasresults;
       ResultSet resultset = null;
       PreparedStatement pstSelect = null, pstUpdate = null;
       ResultSetMetaData metadata = null;
       int columnCount, resultCount, updateCount = 0;
       String key, query;

       //Query for badge info
       try{
           
           if (conn.isValid(0)){
               
               // Prepare Select Query
               query = "SELECT * FROM badge WHERE id = ?";
               pstSelect = conn.prepareStatement(query);
               pstSelect.setString(1, id);
               
               //Execute Select Query
               System.out.println("Submitting Query...");
               hasresults = pstSelect.execute();
               
               //Get Results
               while ( hasresults || pstSelect.getUpdateCount() != -1 ) {

                    if ( hasresults ) {
                        
                        /* Get ResultSet Metadata */
                        
                        resultset = pstSelect.getResultSet();
                        metadata = resultset.getMetaData();
                        columnCount = metadata.getColumnCount();
                        

                        /* Get Data; Print as Table Rows */
                        
                        while(resultset.next()) {
                            
                            /* Begin Next ResultSet Row */

                            System.out.println();
                            
                            /* Loop Through ResultSet Columns; Print Values */

                            String badgeID = resultset.getString("id");
                            String description = resultset.getString("description");
                            
                            badgeQuery = new Badge(badgeID, description);

                        }
                        
                    }

                    else {

                        resultCount = pstSelect.getUpdateCount();  

                        if ( resultCount == -1 ) {
                            break;
                        }

                    }
                    
                    /* Check for More Data */

                    hasresults = pstSelect.getMoreResults();

                } 
           
            }
           
       }
       catch(Exception e){
           
           System.err.println(e.toString());
           
       }
       
       finally {
            
            if (resultset != null) { try { resultset.close(); resultset = null; } catch (Exception e) {} }
            
            if (pstSelect != null) { try { pstSelect.close(); pstSelect = null; } catch (Exception e) {} }
            
            if (pstUpdate != null) { try { pstUpdate.close(); pstUpdate = null; } catch (Exception e) {} }
            
        }
       
       return badgeQuery;

       
   }
   
   public Shift getShift(int shiftid){
       
       Shift shiftQuery = null;
       String shiftidString = Integer.toString(shiftid);
       
       try{
           ResultSet resultset = stmt.executeQuery("SELECT * FROM shift WHERE id=" + shiftidString); 
           if (resultset != null){
               resultset.next();
               String id = resultset.getString("id");
               String description = resultset.getString("description");
               int gp = resultset.getInt("gracePeriod");
               int dock = resultset.getInt("dock");
               Time start = resultset.getTime("start");
               Time stop = resultset.getTime("stop");
               Time lunchStart = resultset.getTime("lunchStart");
               Time lunchStop = resultset.getTime("lunchStop");
               int lunchDeduct = resultset.getInt("lunchDeduct");
               int interval = resultset.getInt("interval");
               
               shiftQuery = new Shift(id, description, start, stop, interval, gp, dock, lunchStart, lunchStop, lunchDeduct);
               
           }
        }
       
       catch(Exception e){
           
           System.err.println(e.toString());
           
       }
       
       return shiftQuery; 

       
   }
   
   public Shift getShift(Badge badge){
       
       Shift shiftQuery = null;
       String badgeid = badge.getID();
       
       boolean hasresults;
       ResultSet resultset = null;
       PreparedStatement pstSelect = null, pstUpdate = null;
       ResultSetMetaData metadata = null;
       int columnCount, resultCount, updateCount = 0;
       String key, query;

       //Query for badge info
       try{
           
           if (conn.isValid(0)){
               
               // Prepare Select Query
               query = "SELECT * FROM employee WHERE badgeid = ?";
               pstSelect = conn.prepareStatement(query);
               pstSelect.setString(1, badgeid);
               
               //Execute Select Query
               System.out.println("Submitting Query...");
               hasresults = pstSelect.execute();
               
               //Get Results
               while ( hasresults || pstSelect.getUpdateCount() != -1 ) {

                    if ( hasresults ) {
                        
                        /* Get ResultSet Metadata */
                        
                        resultset = pstSelect.getResultSet();
                        metadata = resultset.getMetaData();
                        columnCount = metadata.getColumnCount();
                        

                        /* Get Data; Print as Table Rows */
                        
                        while(resultset.next()) {
                            
                            /* Begin Next ResultSet Row */

                            System.out.println();
                            
                            /* Loop Through ResultSet Columns; Print Values */

                            int shiftID = resultset.getInt("shiftid");
                            
                            shiftQuery = getShift(shiftID);

                        }
                        
                    }

                    else {

                        resultCount = pstSelect.getUpdateCount();  

                        if ( resultCount == -1 ) {
                            break;
                        }

                    }
                    
                    /* Check for More Data */

                    hasresults = pstSelect.getMoreResults();

                } 
           
            }
           
       }
       catch(Exception e){
           
           System.err.println(e.toString());
           
       }
       
       finally {
            
            if (resultset != null) { try { resultset.close(); resultset = null; } catch (Exception e) {} }
            
            if (pstSelect != null) { try { pstSelect.close(); pstSelect = null; } catch (Exception e) {} }
            
            if (pstUpdate != null) { try { pstUpdate.close(); pstUpdate = null; } catch (Exception e) {} }
            
        }
       
       return shiftQuery;
       
   }
   
   public Punch getPunch(int punchid){
       
       Punch punchQuery = null;
       String id = Integer.toString(punchid);
       boolean hasresults;
       ResultSet resultset = null;
       PreparedStatement pstSelect = null, pstUpdate = null;
       ResultSetMetaData metadata = null;
       int columnCount, resultCount, updateCount = 0;
       String key, query;

       //Query for badge info
       try{
           
           if (conn.isValid(0)){
               
               // Prepare Select Query
               query = "SELECT *, UNIX_TIMESTAMP(`ORIGINALTIMESTAMP`) * 1000 AS ts FROM punch WHERE id = ?";
               pstSelect = conn.prepareStatement(query);
               pstSelect.setString(1, id);
               
               //Execute Select Query
               System.out.println("Submitting Query...");
               hasresults = pstSelect.execute();
               
               //Get Results
               while ( hasresults || pstSelect.getUpdateCount() != -1 ) {

                    if ( hasresults ) {
                        
                        /* Get ResultSet Metadata */
                        
                        resultset = pstSelect.getResultSet();
                        metadata = resultset.getMetaData();
                        columnCount = metadata.getColumnCount();
                        

                        /* Get Data; Print as Table Rows */
                        
                        while(resultset.next()) {
                            
                            /* Begin Next ResultSet Row */

                            System.out.println();
                            
                            /* Loop Through ResultSet Columns; Print Values */

                        //int getid = resultset.getInt("id");
                        int terminalID = resultset.getInt("terminalid");
                        int ptID = resultset.getInt("punchtypeid");
                        long timeStamp = resultset.getLong("ts");
                        String badgeID = resultset.getString("badgeId");

                        Badge badge = getBadge(badgeID);

                        punchQuery = new Punch(badge, terminalID, ptID);
                        punchQuery.setTimeStamp(timeStamp); 
                        
                        System.out.println(punchQuery.getOriginalTimeStamp());

                        }
                        
                    }

                    else {

                        resultCount = pstSelect.getUpdateCount();  

                        if ( resultCount == -1 ) {
                            break;
                        }

                    }
                    
                    /* Check for More Data */

                    hasresults = pstSelect.getMoreResults();

                } 
           
            }
           
       }
       catch(Exception e){
           
           System.err.println(e.toString());
           
       }
       
       finally {
            
            if (resultset != null) { try { resultset.close(); resultset = null; } catch (Exception e) {} }
            
            if (pstSelect != null) { try { pstSelect.close(); pstSelect = null; } catch (Exception e) {} }
            
            if (pstUpdate != null) { try { pstUpdate.close(); pstUpdate = null; } catch (Exception e) {} }
            
        }
       
       return punchQuery;
       
   }
}