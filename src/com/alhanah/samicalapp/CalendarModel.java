/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import static com.alhanah.samicalapp.SamiApplication.getStore;
import static com.alhanah.samicalapp.SamiApplication.getT;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import nasiiCalendar.BasicCalendar;
import static nasiiCalendar.BasicCalendar.DAY;
import nasiiCalendar.BasicDate;
import nasiiCalendar.locationBasid.LunerLocationCalendar;
//import nasiiCalendar.CalendarFactory;

/**
 *
 * @author DEll
 */
public class CalendarModel extends AbstractTableModel{

    static CalendarModel createFullModel(BasicDate bd) {
        return createModel(bd, getStore().getCalendars(), CalendarViewType.YEAR_VIEW);
        
    }
    static CalendarModel createModel(BasicDate bd,List<BasicCalendar>cal, CalendarViewType ct) {
        //hussam.println("creating Models: "+cal);
        CalendarModel tm = new CalendarModel(bd, ct);
        for(BasicCalendar bc:cal){
            tm.addCalednar(bc);
        }
        return tm;
    }
    List<BasicCalendar>calendars;
    List<DataChangedListener>list=new ArrayList<DataChangedListener>();
    BasicDate firstDate;
    int dayOfWeek=0;
    CalendarViewType type;
    private BasicDate lastDate;

    CalendarViewType getCalType() {
        return type;
    }
    public BasicDate getLastDate(){
        return lastDate;
    }
    
    public BasicDate calcLastDate() {
        BasicDate bd=null;
        if(type==CalendarViewType.MONTH_VIEW){
            bd=firstDate.getCalendar().getDate(firstDate.getYear(), firstDate.getMonth(), firstDate.getCalendar().getMonthLength(firstDate));
            
        }else{
            bd=firstDate.getCalendar().getDate(firstDate.getYear()+1, 1, 1);
            bd.getCalendar().getDate(bd.getDate()-DAY);
        }
        lastDate=bd;
        return lastDate;
    }
    
    CalendarModel(BasicDate d, CalendarViewType type){
        this.type=type;
        calendars=new ArrayList<BasicCalendar>();
        //calendars.add(d.getCalendar());
        setDate(d);
        
    }
    public List<BasicCalendar>getCalendars(){
        return calendars;
    }
    public void setDate(BasicDate bd){
        //OmariCalendar o=(OmariCalendar)bd.getCalendar();
        //firstDate=bd.getCalendar().getDate(bd.getYear(), bd.getMonth(), 1);
    //    firstDate = CalendarFactory.getSamiCalendar().getDate(bd.getDate());
        int month= type==CalendarViewType.MONTH_VIEW ? bd.getMonth():1;
        firstDate = bd.getCalendar().getDate(bd.getYear(),month,1);
    //    hussam.println("firstdate:"+firstDate);
        Calendar c= Calendar.getInstance();
        c.setTime(new Date(firstDate.getDate()));
        dayOfWeek=c.get(Calendar.DAY_OF_WEEK);
        calcLastDate();
        fireModelUpdated();
    }
    
    public void addCalednar(BasicCalendar b){
        calendars.add(b);
        fireModelUpdated();
    }
    protected void fireModelUpdated(){
        for(DataChangedListener d:list){
            
            for(int i=0;i<getRowCount();i++){
                for(int o=0;o<getColumnCount();o++){
        //            hussam.println("r: "+i+" c: "+o);
                    try{
                        d.dataChanged(i, o);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    
                }
            }
        }
    }
    @Override
    public int getRowCount() {
        return type==CalendarViewType.MONTH_VIEW?6*calendars.size():56*calendars.size();
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public String getColumnName(int c) {
        switch (c){
            case 0:return getT(firstDate.getCalendar().getName());
            case 1:return getT("shortSunday");
            case 2:return getT("shortMonday");
            case 3:return getT("shortTuesday");
            case 4:return getT("shortWednesday");
            case 5:return getT("shortThursday");
            case 6:return getT("shortFriday");
            case 7:return getT("shortSaturday");
            
        }
        return "Extra";
    }

    @Override
    public boolean isCellEditable(int arg0, int arg1) {
        return true;
    }

    @Override
    public Object getValueAt(int r, int c) {
        
        switch(c){
            case 0:c=1;
            default:
                int days=(r/calendars.size())*7+(c-1)-dayOfWeek+1;
                BasicCalendar cal=calendars.get((r)%calendars.size());
                 BasicDate bd=cal.getDate(firstDate.getDate()+BasicCalendar.DAY*days);
                
                return bd;
                
        }
        
    }
    public BasicDate getFirstDate(){
        return firstDate;
    }
    @Override
    public void setValueAt(int arg0, int arg1, Object arg2) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addDataChangeListener(DataChangedListener arg0) {
        list.add(arg0);
    }

    @Override
    public void removeDataChangeListener(DataChangedListener arg0) {
        list.remove(arg0);
    }
    
}
