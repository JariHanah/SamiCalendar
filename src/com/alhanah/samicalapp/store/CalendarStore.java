/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp.store;

import nasiiCalendar.CalendarFactory;
import com.codename1.payment.Purchase;
import com.codename1.ui.Display;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nasiiCalendar.*;
import static nasiiCalendar.BasicCalendar.SKU_DAY_VIEW;
import static nasiiCalendar.BasicCalendar.SKU_FULL_VERSION;
import static nasiiCalendar.BasicCalendar.SKU_METONIC_VIEW;
import static nasiiCalendar.BasicCalendar.SKU_MONTH_VIEW;
import static nasiiCalendar.BasicCalendar.OMARI_ID;
import static nasiiCalendar.BasicCalendar.SAMI_ID;
import static nasiiCalendar.BasicCalendar.GREG_ID;
import static nasiiCalendar.BasicCalendar.JULIAN_ID;
import static nasiiCalendar.BasicCalendar.SOLAR_128_ID;
import static nasiiCalendar.BasicCalendar.SOLAR_STATIONS_ID;
import static nasiiCalendar.BasicCalendar.ZODIAC13_ID;
import static nasiiCalendar.BasicCalendar.AD_ID;
import static nasiiCalendar.BasicCalendar.BYRONI_ID;
import static nasiiCalendar.BasicCalendar.QAZWINI_ID;
import static nasiiCalendar.BasicCalendar.WSMI_ID;
import static nasiiCalendar.BasicCalendar.HEWBREW_ID;
import static nasiiCalendar.BasicCalendar.NEW_MOON_ID;

/**
 *
 * @author Hussam
 */
public class CalendarStore {

    public static final String NAME = ".name";
    public static final String DESC = ".desc";
    public static final String SKU = ".sku";
    public static final String SHORT=".short";
//    Map<String, String> itemNames;
  //  Map<String, String> itemDesc;
    //Map<String, BasicCalendarFactory> factories;
    Map<String, Boolean> purchased;

    CalendarFactory fac;
    
    public CalendarStore() {
        System.err.println("STOREEEEE ");
        fac = new CalendarFactory();
   //     itemNames = new HashMap<>();
     //   itemDesc = new HashMap<>();
        //factories = new HashMap<>();
        purchased = new HashMap<>();
        CalendarFactory fac = new CalendarFactory();
        CalendarFactory.setInstance(fac);

        fillMaps();
        prepareCalendars();
        System.err.println("Finished....");
        System.err.println("IS EDT: "+Display.getInstance().isEdt());
    }

    public List<BasicCalendar> getCalendars() {
        List<BasicCalendar>list= new ArrayList<BasicCalendar>();
        //list.add(new LunerLocationCalendar());
        list.addAll(fac.getCalendars());
        return list;

    }

    public BasicCalendar getCalendar(String sku) {
        return fac.getCalendar(sku);
    }

    public boolean isPurchased(String sku) {
        if(true)return true;
        if (!purchased.containsKey(sku)) {
            throw new NullPointerException("NULL SKU");
            //    return false;
        }
        if (purchased.get(SKU_FULL_VERSION)) {
            return true;
        }
        boolean full = purchased.get(sku);

        return full;
        //return Purchase.getInAppPurchase().wasPurchased(sku);
    }

    public String getItemName(String sku) {
        return sku+NAME;
    }

    public String getItemDescription(String sku) {
        return sku+DESC;
    }

    public List<String> getItemsForSale() {
        List<String> list = new ArrayList<>();
        for (String s : purchased.keySet()) {
            list.add(s);
        }
        return list;
    }

    private void addItem(String sku) {
        purchased.put(sku, false);
    }

    private void fillMaps() {

        addItem(SKU_FULL_VERSION);
        addItem(BasicCalendar.SAMI_FIXED_ID);
        addItem(AD_ID);
        addItem(BYRONI_ID);
        addItem(GREG_ID);
        addItem(JULIAN_ID);
        addItem(HEWBREW_ID);
        addItem(OMARI_ID);
        addItem(QAZWINI_ID);
        addItem(SAMI_ID);
        addItem(SOLAR_128_ID);
        addItem(BasicCalendar.JALALI_IR_ID);
        addItem(SOLAR_STATIONS_ID);
        addItem(WSMI_ID);
        addItem(ZODIAC13_ID);
        addItem(BasicCalendar.OMARI_ID_16);
        addItem(BasicCalendar.OMARI_ID_15);
        addItem(BasicCalendar.OMARI_ID_INDIAN);
        addItem(BasicCalendar.OMARI_ID_HABASH);
        addItem(NEW_MOON_ID);
        addItem(SKU_METONIC_VIEW);
        addItem(SKU_MONTH_VIEW);
        addItem(SKU_DAY_VIEW);

    }

    public void updatePurchaseList() {
        synchronized (purchased) {
            for (String x : purchased.keySet()) {
                
                if (Purchase.getInAppPurchase().wasPurchased(x)) {
                    purchased.put(x, Boolean.TRUE);
                } else {
                    purchased.put(x, Boolean.FALSE);
                   
                }

            }
        }
        prepareCalendars();
    }

    private void prepareCalendars() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(c.YEAR, 622);
        c.set(c.MONTH, 1);
        c.set(c.DATE, 1);

        long start = c.getTime().getTime();
        c.set(c.YEAR, 2040);
        long end = c.getTime().getTime();
        String id = BYRONI_ID;
        // BasicCalendar b=fac.getCalendar(id);

        for (BasicCalendar b : fac.getCalendars()) {
      //      System.err.println("b: "+b);
            MyBasicCalendar m = (MyBasicCalendar) b;
            if (isPurchased(m.getName())) {
                m.setMaxInstant(MyBasicCalendar.ENDTIME);
                m.setMinInstant(m.getBigOfTime());

            } else {
                m.setMaxInstant(end);
                m.setMinInstant(start);
            }
        }
    }

}
