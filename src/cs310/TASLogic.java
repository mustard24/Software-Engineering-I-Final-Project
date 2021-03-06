package cs310;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Dustin Moody
 */
public class TASLogic {
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift){
        int dailyMin = 0;
        
        if (dailypunchlist.size() < 2){
            return 0;
        }
        
        for (int i = 0; i < dailypunchlist.size(); i=i+2){
            Punch clockIn = (Punch) dailypunchlist.get(i);
            Punch clockOut = (Punch) dailypunchlist.get(i+1);
                              
            if ((clockIn.getPunchTypeId()!=2) && (clockOut.getPunchTypeId()!=2)){
                long clockDifference = clockOut.cal2.getTimeInMillis()-clockIn.cal2.getTimeInMillis();
                dailyMin = dailyMin + (int) (clockDifference/60000);
            }
            
            if ((dailyMin > shift.getLunchDeduct()) && (clockIn.getLunchFlag() == false)){
                int lunchMins = dailyMin - shift.getLunchTime();
                return lunchMins;
            }
        }            
        return dailyMin;        
    }   
    
    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist){
        
        /* Create ArrayList Object */
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        
        for (int i = 0; i < dailypunchlist.size(); i++){
            
            Punch punch = dailypunchlist.get(i); 
            
            /* Create HashMap Object (one for every Punch!) */
            HashMap<String, String> punchData = new HashMap<>();

            /* Add Punch Data to HashMap */
            punchData.put("id", String.valueOf(punch.getID()));
            punchData.put("badgeid", String.valueOf(punch.getBadgeId()));
            punchData.put("terminalid", String.valueOf(punch.getTerminalId()));
            punchData.put("punchtypeid", String.valueOf(punch.getPunchTypeId()));
            punchData.put("punchdata", String.valueOf(punch.getPunchdata()));
            punchData.put("originaltimestamp", String.valueOf(punch.getOriginalTimeStamp()));
            punchData.put("adjustedtimestamp", String.valueOf(punch.getAdjustedTimeStamp()));

            /* Append HashMap to ArrayList */
            jsonData.add(punchData);    
        }

    String json = JSONValue.toJSONString(jsonData);
    return json;

    }
    
    public static double calculateAbsenteeism(ArrayList<Punch> punchlist, Shift shift){
        
        double totalNumberOfScheduledMinutes = 0;
        double absenteeismPercentage = 0;
        double totalNumberOfMinutes = 0;
        
        if(punchlist.size() < 2){
            return 0;
        }
        for(int i = 0; i < punchlist.size(); i = i +2){
            Punch clockIn = (Punch) punchlist.get(i);
            Punch clockOut = (Punch) punchlist.get(i+1);
            
            if((clockIn.getPunchTypeId()!=2) && (clockOut.getPunchTypeId()!=2)){
                long clockTimeDifference = clockOut.cal2.getTimeInMillis()-clockIn.cal2.getTimeInMillis();
                totalNumberOfMinutes = totalNumberOfMinutes + (int)(clockTimeDifference/60000);
            }
            if((totalNumberOfScheduledMinutes > shift.getLunchDeduct()) && (clockIn.getLunchFlag() == false)){
                totalNumberOfScheduledMinutes = totalNumberOfMinutes - shift.getLunchTime();
                return totalNumberOfScheduledMinutes;
            }
            return totalNumberOfMinutes;
        }
        absenteeismPercentage = (totalNumberOfScheduledMinutes - totalNumberOfMinutes)/totalNumberOfScheduledMinutes;
        return absenteeismPercentage;
    }
    
    public static String getPunchListPlusTotalsAsJSON(ArrayList<Punch> punchlist, Shift s) {
        
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        
        for (int i = 0; i < punchlist.size(); i++){
            
            Punch punch = punchlist.get(i); 
            
            /* Create HashMap Object (one for every Punch!) */
            HashMap<String, String> punchData = new HashMap<>();

            /* Add Punch Data to HashMap */
            punchData.put("id", String.valueOf(punch.getID()));
            punchData.put("badgeid", String.valueOf(punch.getBadgeId()));
            punchData.put("terminalid", String.valueOf(punch.getTerminalId()));
            punchData.put("punchtypeid", String.valueOf(punch.getPunchTypeId()));
            punchData.put("punchdata", String.valueOf(punch.getPunchdata()));
            punchData.put("originaltimestamp", String.valueOf(punch.getOriginalTimeStamp()));
            punchData.put("adjustedtimestamp", String.valueOf(punch.getAdjustedTimeStamp()));
            

            /* Append HashMap to ArrayList */
            jsonData.add(punchData);    
        }
        
            
        HashMap<String, String> absenteeData = new HashMap<>();
        
        absenteeData.put("totalminutes", String.valueOf(calculateTotalMinutes(punchlist, s))); 
        absenteeData.put("absenteeism", String.valueOf(calculateAbsenteeism(punchlist, s))); 
        jsonData.add(absenteeData); 
        
        String json = JSONValue.toJSONString(jsonData);
        return json;
    }
    
}
