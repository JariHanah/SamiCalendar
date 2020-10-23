/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.spinner.Picker;
import java.util.Date;
import java.util.List;
import nasiiCalendar.BasicCalendar;
import static nasiiCalendar.BasicCalendar.DAY;
import nasiiCalendar.BasicDate;
import nasiiCalendar.CalendarFactory;
import org.shredzone.commons.suncalc.MoonPhase;
import org.shredzone.commons.suncalc.MoonTimes;
import org.shredzone.commons.suncalc.SunTimes;
import nasiiCalendar.PeriodType;

/**
 *
 * @author DEll
 */
public class MyDateSpinner extends CalendarControllerContainer {
    CellEditor editor= new CellEditor();
    Picker year = new Picker();
    Picker month = new Picker();
    Picker day = new Picker();
    Button calendarType=new Button();
    Button weekDay=new Button();
    TextArea moonArea=new TextArea();
    
    Button updateLocation;
    Label hilalDate;
    Label ccLabel;
    
    public MyDateSpinner(BasicDate selected) {
        super(selected);
        prepareMyUI();

    }

    void prepareMyUI() {
        getAllStyles().setBgTransparency(150);
        getAllStyles().setBgColor(0x000000);
        calendarType.getAllStyles().setFgColor(0xffffff);
        setRTL(MainForm.getFormRTL());
        updateMyUI(null);
        ActionListener a = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                readSpinner();
                fireControllerUpdated(arg0.getSource());
            }
        };
        year.addActionListener(a);
        month.addActionListener(a);
        day.addActionListener(a);
    //    calendarType.setUIID(CellEditor.DAY_UIID);
        //label.setText(getT(selected.getCalendar().getName())+" "+selected.getYear()+" "+selected.getCalendar().getMonthName(selected)+" "+selected.getDay());
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Container t1=new Container();
        Container t2=new Container();
        t1.setRTL(MainForm.getFormRTL());
        t2.setRTL(MainForm.getFormRTL());
        t1.setLayout(new FlowLayout(Component.CENTER));
        t2.setLayout(new BoxLayout(BoxLayout.X_AXIS));
        t2.setLayout(new FlowLayout(Component.CENTER));
        t1.add(calendarType);
        t1.add(weekDay);
        
        t2.add(month);
        t2.add(day);
        t2.add(year);
        add(t1);
        add(t2);
    //    add(moonArea);
        calendarType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                MainForm.getForm().showCalenderChooser();
            }
        });
        //hussam.println(Arrays.asList(year.getStrings()));
        //hussam.println("year: " + year.getSelectedString());
        //hussam.println("month: " + month.getSelectedString());
        //hussam.println("day: " + day.getSelectedString());
    }

    private void readSpinner() {
        BasicDate b=getSelectedDate();
        BasicCalendar c = b.getCalendar();
        int y = Integer.parseInt(year.getSelectedString());
        int m = month.getSelectedStringIndex()+1;
        int d = Integer.parseInt(day.getSelectedString());
    //    hussam.println("reading: "+y+" "+m+" "+d);
        b=c.getDate(y, 1, 1);
        PeriodType yt = c.getYearType(b);
        if (yt.getSubPeriods().size() < m) {
            m = yt.getSubPeriods().size();
            month.setSelectedString(yt.getSubPeriods().get(yt.getSubPeriods().size() - 1).getName());
        }
        b=c.getDate(y, m, 1);
        if (c.getMonthLength(b) < d + 1) {
            
            d = c.getMonthLength(b);
            day.setSelectedString("" + d);
        }
        b = c.getDate(y, m, d);
        setSelectedDate(b);
    }
    public MoonTimes getMoon(){
        MoonTimes mt=MoonTimes.compute()
                .on(new Date(getSelectedDate().getDate()-DAY))
                .at(24.7136, 46.6753)
                .execute();
        
        return mt;
                
        
    }
    public MoonPhase getPhase(){
        MoonPhase p= MoonPhase.compute().on(new Date(getSelectedDate().getDate())).execute();
        
        return p;
    }
    public SunTimes getSunSet(){
        SunTimes st=SunTimes.compute()
                .on(new Date(getSelectedDate().getDate()-DAY))
                .at(24.7136, 46.6753)
                .execute();
        
        return st;
    }
    @Override
    protected void updateMyUI(Component source) {
        //if(source!=labelDate)labelDate.setText(getT(selected.getCalendar().getName())+" "+selected.getYear()+" "+selected.getCalendar().getMonthName(selected)+" "+selected.getDay());
        calendarType.setText(getSelectedDate().getCalendar().getName());
        weekDay.setText(CalendarFactory.getWeekDay(selected));
        weekDay.setUIID(CellEditor.DATE_VIEW_UIID);
        editor.prepareSeasons(weekDay, getSelectedDate(), CalendarFactory.getDefaultSeasonIdentifier());
        editor.prepareToday(weekDay, getSelectedDate());
        if(getMoon().getSet()!=null)
        moonArea.setText("MoonSet:"+getMoon().getSet().toString()+"\n"+
                "Sun Set:"+getSunSet().getSet().toString()+"\n"+
                "Phase  :"+getPhase().getTime());
        String[] strs = null;
        if (source != year) {
            year.setType(Display.PICKER_TYPE_STRINGS);
            strs = new String[1000];
            for (int i = 0; i < strs.length; i++) {
                strs[i] = "" + (i + getSelectedDate().getYear() - 500);
            }
            year.setStrings(strs);
            year.setSelectedString(getSelectedDate().getYear() + "");
        }

        if (source != month) {
            month.setType(Display.PICKER_TYPE_STRINGS);
            List<PeriodType> months = getSelectedDate().getCalendar().getYearType(getSelectedDate()).getSubPeriods();
            strs = new String[months.size()];
            for (int i = 0; i < months.size(); i++) {
                strs[i] = months.get(i).getName();
            }
            month.setSelectedString(getSelectedDate().getCalendar().getMonthName(getSelectedDate()).getName());

            month.setStrings(strs);
        }
        if (source != day) {
            int x = getSelectedDate().getCalendar().getMonthLength(getSelectedDate());
            strs = new String[x];
            for (int i = 0; i < x; i++) {
                strs[i] = "" + (i + 1);
            }
            day.setStrings(strs);
            day.setSelectedString(getSelectedDate().getDay() + "");
        }
        
    }

    public void setSelectedDate(BasicDate b) {
        super.setSelectedDate(b);
        //setSelectedDate(b);
        year.setSelectedString(b.getYear() + "");
    //    hussam.println("setting date: "+b);
        month.setSelectedString(b.getCalendar().getMonthName(b).getName());
        day.setSelectedString(b.getDay() + "");
        year.animate();
        updateMyUI(null);
        this.revalidate();

    }

    @Override
    public String getHeader() {
        return getSelectedDate().toString();
    }

    @Override
    public void next() {
        //    selected=selected.getCalendar().nextDay(selected);
    }

    @Override
    public void previous() {

    }

    @Override
    public String getChangeSymbol() {
        return "D";
    }

    @Override
    Component getNorthView() {
        return null;
    }

}
