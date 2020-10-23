/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import java.util.List;
import nasiiCalendar.BasicCalendar;

/**
 *
 * @author hmulh
 */
public interface  CalendarListRequester {
    public List<BasicCalendar>getCalendars();
    public void setCalendars(List<BasicCalendar>list);
}
