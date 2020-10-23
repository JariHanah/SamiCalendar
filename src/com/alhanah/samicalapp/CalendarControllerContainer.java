/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import static com.alhanah.samicalapp.SamiApplication.getStore;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import java.util.ArrayList;
import java.util.List;
import nasiiCalendar.BasicCalendar;
import nasiiCalendar.BasicDate;
//import nasiiCalendar.CalendarFactory;

/**
 *
 * @author Hussam
 */
public abstract class CalendarControllerContainer extends Container  implements CalendarController{

    List<DateControllerListener> list = new ArrayList<DateControllerListener>();
    List<BasicCalendar> calendars = new ArrayList<>();
    BasicDate selected;
    
    public CalendarControllerContainer(BasicDate selected) {
        this.selected = selected;
        
    }
    
    public void setSelectedDate(BasicDate bd){
        selected=bd;
        
    }

    public BasicDate getSelectedDate(){
        return selected;
    }

    public void setCalendars(List<BasicCalendar> list) {
        calendars.clear();
        calendars.addAll(list);
    //    hussam.println("calendars: " + calendars);
        if (calendars.size() == 0) {
            calendars.add(getStore().getCalendar(BasicCalendar.SAMI_FIXED_ID));
        }
        
    }
    public abstract String getHeader();
    protected abstract void updateMyUI(Component source);

    public List<BasicCalendar> getCalendars() {
        return new ArrayList<BasicCalendar>(calendars);
    }

    public abstract void next();

    public abstract void previous();

    protected void fireControllerUpdated(Object source) {
        for (DateControllerListener d : list) {
            d.dateUpdated(source, getSelectedDate());
        }
    }

    public void addControllerListiner(DateControllerListener d) {
        list.add(d);
    }

    public void removeControllerListiner(DateControllerListener d) {
        list.remove(d);
    }

    abstract Component getNorthView() ;
}

