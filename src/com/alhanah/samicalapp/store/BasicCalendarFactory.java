/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp.store;

import nasiiCalendar.BasicCalendar;
import nasiiCalendar.BasicCalendar;
import nasiiCalendar.MyBasicCalendar;

/**
 *
 * @author Hussam
 */
public abstract class BasicCalendarFactory {
    long start;
    long end;
    String id;

    public BasicCalendarFactory(long start, long end, String id) {
        this.start = start;
        this.end = end;
        this.id = id;
    }
    
    
    public String getCalendarID(){
        return id;
    }
    public void setStart(long start){
        this.start=start;
    }
    public long getEnd(){
        return end;
    }

    public long getStart() {
        return start;
    }

    public void setEnd(long end) {
        this.end = end;
    }
    public BasicCalendar getBasicCalendar(){
        return createCalendar();
    }
    protected abstract BasicCalendar createCalendar();
}
