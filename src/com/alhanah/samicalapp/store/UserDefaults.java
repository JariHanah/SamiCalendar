/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp.store;

import nasiiCalendar.locationBasid.City;
import static com.alhanah.samicalapp.SamiApplication.getStore;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nasiiCalendar.BasicCalendar;
import nasiiCalendar.CalendarFactory;
import nasiiCalendar.SeasonIdentifier;

/**
 *
 * @author Hussam
 */
public class UserDefaults {
    public static final String UTF="UTF-8";
    public static final String MAIN_CALENDARS="main_calendars";
    public static final String MAIN_CITY="main_city";
    public static final String SEASON_IDENTIFIER="season_identifier";
    
    static UserDefaults instance=new UserDefaults();
    public static UserDefaults getInstance(){
        return instance;
    }
    private static final String CURRENT_CITY="current city";
    
    
    public UserDefaults() {
        List<BasicCalendar>list=getSelectedCalendars();
        if(list==null){
            Storage.getInstance().writeObject(MAIN_CALENDARS, Arrays.asList(new String[]{BasicCalendar.SAMI_FIXED_ID, BasicCalendar.OMARI_ID_16, BasicCalendar.GREG_ID}));
        }
        
    }
    
    public SeasonIdentifier getSeasonIdentifier(){
        String x= (String) Storage.getInstance().readObject(SEASON_IDENTIFIER);
        if(x==null){
            Storage.getInstance().writeObject(SEASON_IDENTIFIER, CalendarFactory.getSeasonSolar128().getName());
            return getSeasonIdentifier();
        }
        
        if(x.equals(CalendarFactory.getSeasonCalc().getName()))return CalendarFactory.getSeasonCalc();
        if(x.equals(CalendarFactory.getSeasonSolar128().getName()))return CalendarFactory.getSeasonSolar128();
        if(x.equals(CalendarFactory.getSeasonJalali().getName()))return CalendarFactory.getSeasonJalali();
        
        
        return CalendarFactory.getSeasonSolar128();
        
    }
    public void setSeasonIdentifier(SeasonIdentifier s){
        
        Storage.getInstance().writeObject(SEASON_IDENTIFIER, s.getName());
    }
    
    //public static final String BID="basic5";
    
    public String getFile(String key, String defaultFile){
        try (InputStream in = Storage.getInstance().createInputStream(key)) {
            String basic = Util.readToString(in, UTF);
            in.close();
            return basic;
        } catch (IOException e) {
            System.err.println("error: "+e.getMessage());
        }
        return defaultFile;
    }
    
    public void setFile(String key, String file){
        OutputStream os = null;
        try {
            os = Storage.getInstance().createOutputStream(key);
            os.write(file.getBytes(UTF));
            os.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
    public void saveSelectedCalendars(List<BasicCalendar>list){
        List<String> l=new ArrayList<>();
        for(BasicCalendar b:list){
            l.add(b.getName());
        }
        Storage.getInstance().writeObject(MAIN_CALENDARS, l);
        
    }
    public List<BasicCalendar> getSelectedCalendars(){
        List<String>list=  (List<String>) Storage.getInstance().readObject(MAIN_CALENDARS);
        if(list==null)return null;
        List<BasicCalendar>result=new ArrayList<>();
        for(Object s:list){
            result.add(getStore().getCalendar(s.toString()));
        }
        //hussam.println("reading store: "+result);
        return result;
    }
    public void setCity(City c){
        List<String>x=new  ArrayList<>();
        x.add(c.getName());
        x.add(c.getArabName());
        x.add(c.getLat()+"");
        x.add(c.getLon()+"");
        Storage.getInstance().writeObject(CURRENT_CITY, x);
    }
    public City getCity(){
        List<String>x=(List<String>) Storage.getInstance().readObject(CURRENT_CITY);
        City c=new City(x.get(0), x.get(1), Double.parseDouble(x.get(2)), Double.parseDouble(x.get(3)));
        return c;
    }
    
}
