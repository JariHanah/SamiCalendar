/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp.store;

import nasiiCalendar.locationBasid.City;
import com.alhanah.samicalapp.SamiApplication;
import static com.alhanah.samicalapp.store.UserDefaults.UTF;
import com.codename1.io.CSVParser;
import com.codename1.io.Log;
import com.codename1.ui.Display;
import com.codename1.util.MathUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author hmulh
 */
public class CityList {

    List<City> list = new ArrayList<>();

    public static Comparator<City> getDistanceComparator(double lat, double lon) {
        Comparator<City> c = new Comparator<City>() {
            @Override
            public int compare(City o1, City o2) {
                double c1 = MathUtil.pow(o1.getLat() - lat, 2) + MathUtil.pow(o1.getLon() - lon, 2);
                double c2 = MathUtil.pow(o2.getLat() - lat, 2) + MathUtil.pow(o2.getLon() - lon, 2);
                if (c2 == c1) {
                    return 0;
                }
                if (c2 < c1) {
                    return 1;
                } else {
                    return -1;
                }
            }
        };
        return c;
    }

    public static Comparator<City> getAlphabetComparator() {
        Comparator<City> c = new Comparator<City>() {
            @Override
            public int compare(City o1, City o2) {
                return (o1.getName().compareTo(o2.getName()));

            }
        };
        return c;
    }

    public synchronized City getClosestCity(double lat, double lon) {
        if (list.size() == 0) {
            readCities();
        }
        List<City> l1 = new ArrayList<>(list);
        Collections.sort(l1, getDistanceComparator(lat, lon));
        return l1.get(0);
    }

    private synchronized void readCities() {
        if(list.size()>0)return;
        CSVParser parser = new CSVParser();
        InputStream f = Display.getInstance().getResourceAsStream(getClass(), "/worldcities.xlsx - Sheet1 (1).csv");
        list.clear();
        String[][] data = null;
        try {
            data = parser.parse(f, UTF);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        for (int i = 1; i < data.length; i++) {
            //hussam.println("size:"+data.length+" i: "+i+" olength: "+data[i].length);
            if (data[i].length < 1) {
                break;
            }
            City city = new City(data[i][0], data[i][1], Double.parseDouble(data[i][2]), Double.parseDouble(data[i][3]));
            list.add(city);
            SamiApplication.setT(city.getName(), city.getName(), city.getArabName());
        }
        Collections.sort(list, getAlphabetComparator());
        //hussam.println(list);
    }

    public synchronized  List<City> getCities() {
        if (list.size() == 0) {
            readCities();
        }
        return list;
    }

}
