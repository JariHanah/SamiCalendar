/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.layouts.BorderLayout;
import java.util.List;
import nasiiCalendar.BasicCalendar;
import nasiiCalendar.BasicDate;

/**
 *
 * @author DEll
 */
public class CalendarView extends Container implements CalendarController {

    CalendarControllerContainer current;
    CalendarViewType view;

    public CalendarView(CalendarViewType view, BasicDate bd) {
        this.view = view;
        prepareUI(bd);
    }

    public CalendarViewType getView() {
        return view;
    }

    public void setView(CalendarViewType c, BasicDate bd) {
        view = c;

        //    hussam.println("preparing new VIEW");
        prepareUI(bd);
        // hussam.println("Preparing ..."+getCalendars()+" current:"+current.getCalendars());;
    }

    @Override
    public void setSelectedDate(BasicDate bd) {
        current.setSelectedDate(bd);
    }

    @Override
    public BasicDate getSelectedDate() {
        return current.getSelectedDate();
    }

    @Override
    public void setCalendars(List<BasicCalendar> list) {
        current.setCalendars(list);
    }

    @Override
    public String getHeader() {
        return current.getHeader();
    }

    @Override
    public List<BasicCalendar> getCalendars() {
        return current.getCalendars();
    }

    @Override
    public void next() {
        current.next();
    }

    @Override
    public void previous() {
        current.previous();
    }

    @Override
    public void addControllerListiner(DateControllerListener d) {
        current.addControllerListiner(d);
    }

    @Override
    public void removeControllerListiner(DateControllerListener d) {
        current.removeControllerListiner(d);
    }

    private void prepareUI(BasicDate bd) {
        this.removeAll();
        
        setLayout(new BorderLayout());
        Container c=new Container();
        
        c.setLayout(new BorderLayout());
        
        CalendarController old = current;
        switch (view) {
            case DAY_VIEW:
                current = new DateController(bd, view);
                break;
            //    case DAY_VIEW:current=new ViewDate(bd);break;
            case MONTH_VIEW:
            case YEAR_VIEW:
                current = new DateController(bd, view);
                break;
            case YEAR_MONTH_VIEW:
            default:
                current = new DateController(bd, view);
        }
        current.addControllerListiner(new DateControllerListener() {
            @Override
            public void dateUpdated(Object source, BasicDate bd) {
                //    MainForm.getForm().getcu
            }
        });
        if (old != null) {
            
            current.setCalendars(old.getCalendars());
        }

        //   hussam.println("adding view: "+current+view);
        add(BorderLayout.CENTER,c);
        
        c.add(BorderLayout.CENTER,current);
        Component cc=current.getNorthView();
        if(cc!=null)c.add(BorderLayout.NORTH, current.getNorthView());
        
        revalidate();
        animate();
    }

    @Override
    public String getChangeSymbol() {
        return getChangeSymbol();
    }

}
