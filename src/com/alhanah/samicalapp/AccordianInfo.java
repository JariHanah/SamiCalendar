/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import static com.alhanah.samicalapp.MainForm.getFormRTL;
import static com.alhanah.samicalapp.SamiApplication.getT;
import com.alhanah.samicalapp.store.UserDefaults;
import com.codename1.components.Accordion;
import com.codename1.components.MultiButton;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.TextArea;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import java.util.ArrayList;
import java.util.List;
import nasiiCalendar.BasicCalendar;
import nasiiCalendar.BasicDate;
import nasiiCalendar.CalendarFactory;
import nasiiCalendar.SeasonIdentifier;
import nasiiCalendar.locationBasid.LunerLocationCalendar;

/**
 *
 * @author hmulh
 */
public class AccordianInfo extends Accordion implements UIUpdatable {

    //  List<DateDisplay> display = new ArrayList<>();
    List<BasicCalendar> calendars = new ArrayList<>();
    BasicDate selectedDate;
    CellEditor editor = new CellEditor();
    CalendarChooserDialog chooser;
    List<UIUpdatable> ui = new ArrayList<>();
    MultiButton b1, b2;
    Label seasonButton;
    Button updateCityButton;
    MultiButton dayDiff;
    int max;
    LunerLocationCalendar llc;
    
    ButtonGroup bg;
    
    public AccordianInfo(List<BasicCalendar> list, BasicDate selected) {
        max = 3;
        calendars = list.subList(0, max);
        selectedDate = selected;
        setScrollableY(false);
        prepareUI();

    }

    private void prepareUI() {
        chooser = new CalendarChooserDialog(new CalendarListRequester() {
            @Override
            public List<BasicCalendar> getCalendars() {
                return calendars; //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void setCalendars(List<BasicCalendar> list) {
                list.addAll(calendars);
                calendars = list.subList(0, max);
                UserDefaults.getInstance().saveSelectedCalendars(calendars);
                for (int i = 0; i < ui.size(); i++) {
                    ui.get(i).updateMyUI();
                }

            }

        }, 3);

        DateDisplay display1 = null, display2 = null, display3 = null;
        display1 = new DateDisplay(0, 0);
        display2 = new DateDisplay(1, 1);
        display3 = new DateDisplay(2, 1);
        display1.setRTL(getFormRTL());
        display2.setRTL(getFormRTL());
        display3.setRTL(getFormRTL());
        Command cmd = null;
        display1.setEnabled(false);
        cmd = new Command("Configure Calendars") {
            public void actionPerformed(ActionEvent e) {
                chooser.show();
                System.err.println("showing!!!");
            }
        };

        Container main = BorderLayout.center(display1);//.add(BorderLayout.EAST, new Button(cmd));
        Button b = new Button(cmd);
        b.setRTL(getFormRTL());
        main.setRTL(getFormRTL());
        editor.prepareHeading(b);

        Container c = BoxLayout.encloseY(b, new MoreInfo(display1));
        c.setRTL(getFormRTL());
        addContent(main, c);
        Container lunerCont = new Container();
        lunerCont.setLayout(new BoxLayout(BoxLayout.X_AXIS));
        lunerCont.setRTL(getFormRTL());
        updateCityButton = new Button(new Command("updatecity") {
            @Override
            public void actionPerformed(ActionEvent evt) {
                updateLocation();
            }

        });
        updateCityButton.setRTL(getFormRTL());
        editor.prepareHeading(updateCityButton);

        c = BoxLayout.encloseY(updateCityButton);
        addContent(lunerCont, c);

        editor.prepareHeading(lunerCont);
        editor.prepareHeading(this);
        b1 = new MultiButton();
        b2 = new MultiButton();
        b1.setEnabled(false);
        b2.setEnabled(false);
        b1.setRTL(getFormRTL());
        b2.setRTL(getFormRTL());
        
        lunerCont.add(b1);
        lunerCont.add(b2);

        Container sub1 = BorderLayout.center(display2);
        addContent(sub1, new MoreInfo(display2));
        Container sub2 = BorderLayout.center(display3);
        addContent(sub2, new MoreInfo(display3));
        sub1.setRTL(getFormRTL());
        sub2.setRTL(getFormRTL());
        seasonButton = new Button();
        seasonButton.setRTL(getFormRTL());
        SeasonIdentifier s =UserDefaults.getInstance().getSeasonIdentifier();
        Image i= SamiApplication.getHanahLogo();
        RadioButton rb1 = new RadioButton(CalendarFactory.getSeasonSolar128().getName());
        RadioButton rb2 = new RadioButton(CalendarFactory.getSeasonJalali().getName());
        RadioButton rb3 = new RadioButton(CalendarFactory.getSeasonCalc().getName());
        rb1.setMaterialIcon(FontImage.MATERIAL_CIRCLE);
        rb2.setMaterialIcon(FontImage.MATERIAL_CIRCLE);
        rb3.setMaterialIcon(FontImage.MATERIAL_CIRCLE);
        rb1.setRTL(MainForm.getFormRTL());
        rb2.setRTL(MainForm.getFormRTL());
        rb3.setRTL(MainForm.getFormRTL());
        bg=new ButtonGroup(rb1, rb2, rb3);
        if(s.getName().equals(rb1.getText()))rb1.setSelected(true);
        if(s.getName().equals(rb2.getText()))rb2.setSelected(true);
        if(s.getName().equals(rb3.getText()))rb3.setSelected(true);
        Container by=BoxLayout.encloseY(rb1, rb2, rb3);
        by.setRTL(getFormRTL());
        addContent(seasonButton, by);
        rb1.setSelected(true);
        
        dayDiff = new MultiButton();
        dayDiff.setRTL(getFormRTL());
        dayDiff.setEnabled(false);
        addContent(dayDiff, new TextArea(getT("expectnewrelease")));
        ui.add(display1);
        ui.add(display2);
        ui.add(display3);
        setRTL(getFormRTL());
        updateMyUI();

    }

    public void updateLocation() {
        SamiApplication.prepareCities(new Runnable() {
            @Override
            public void run() {
                updateMyUI();
            }
        });
    }

    public void updateMyUI() {
        if (SamiApplication.getBasicLocation() != null) {
            updateCityButton.setText(getT(SamiApplication.getBasicLocation().getName()) + ":" + getT("updateLocation"));
            if (llc == null || llc.getCity().equals(SamiApplication.getBasicLocation())) {
                llc = new LunerLocationCalendar(SamiApplication.getBasicLocation());
            }
            editor.setNewMoonCycle(b1, selectedDate, llc);
            editor.setBlackMoonCycle(b2, selectedDate);
        }else{
            b1.setTextLine1("missingLocation");
            b1.setTextLine2("missingLocationExtra");
            b2.setTextLine1("missingLocation");
            b2.setTextLine2("missingLocationExtra");
            
        }
        editor.setDayDifference(dayDiff, selectedDate);
        switch(bg.getSelectedIndex()){
            case 0: CalendarFactory.setDefaultSeasonIdentifier(CalendarFactory.getSeasonSolar128());break;
            case 1: CalendarFactory.setDefaultSeasonIdentifier(CalendarFactory.getSeasonJalali());break;
            case 2: CalendarFactory.setDefaultSeasonIdentifier(CalendarFactory.getSeasonCalc());break;
        }
         editor.setSeason(seasonButton, selectedDate, CalendarFactory.getDefaultSeasonIdentifier());
            
        for (UIUpdatable u : ui) {
            if (u != this) {
                u.updateMyUI();
            }
        }
    }

    void setSelectedDate(BasicDate selectedDate) {
        this.selectedDate = selectedDate;

    }

    class MoreInfo extends Container implements UIUpdatable {

        DateDisplay display;
        TextArea text;

        public MoreInfo(DateDisplay display) {
            this.display = display;
            text = new TextArea();
            setLayout(new BorderLayout());
            //    add(BorderLayout.CENTER, text);
            updateMyUI();
        }

        public void updateMyUI() {
            text.setRTL(MainForm.getFormRTL());
            text.setText(getT("expectnewrelease"));

        }

    }

    class DateDisplay extends Container implements UIUpdatable {

        int cal;
        List<Label> labels = new ArrayList<>();

        DateDisplay(int cal, int size) {
            this.cal = cal;
            switch (size) {
                case 0:

                    labels.add(editor.getBigLabel());
                    labels.add(editor.getBigLabel());
                    break;
                case 1:

                    labels.add(editor.getMediumLabel());
                    labels.add(editor.getMediumLabel());
                    break;
            }
            //  labels.add(editor.getMediumLabel());
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            add(labels.get(0));
            add(labels.get(1));
            //    add(labels.get(2));
            updateMyUI();
        }

        public void updateMyUI() {
            BasicDate result = calendars.get(cal).getDate(selectedDate.getDate());
            labels.get(0).setText(getT(result.getCalendar().getMonthName(result).getName()) + " " + result.getDay() + " " + result.getYear());
            //    labels.get(1).setText(result.getDay() + " " + result.getYear());
            labels.get(1).setText(getT(calendars.get(cal).getName()));

        }

        public void setCalendar(int c) {
            cal = c;
        }

        private int getCalendar() {
            return cal;
        }
    }
}

interface UIUpdatable {

    public void updateMyUI();
}
