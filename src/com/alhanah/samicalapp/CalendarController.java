/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import com.codename1.ui.Component;
import java.util.List;
import nasiiCalendar.BasicCalendar;
import nasiiCalendar.BasicDate;

/**
 *
 * @author DEll
 */
interface CalendarController extends CalendarListRequester {
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

    
    public void setSelectedDate(BasicDate bd);
    public BasicDate getSelectedDate();

    public  String getHeader();

    
    public abstract void next();

    public abstract void previous();

    public String getChangeSymbol();
    
    public void addControllerListiner(DateControllerListener d) ;
    public void removeControllerListiner(DateControllerListener d) ;
}

interface DateControllerListener {

    public void dateUpdated(Object source, BasicDate bd);
    
    
}

enum CalendarViewType{
    DAY_VIEW, 
    MONTH_VIEW, YEAR_VIEW, 
    YEAR_MONTH_VIEW ,
    METONIC_YEAR_TYPE_VIEW;
}

