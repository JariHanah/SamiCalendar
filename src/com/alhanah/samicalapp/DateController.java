/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import static com.alhanah.samicalapp.SamiApplication.getStore;
import com.alhanah.samicalapp.store.UserDefaults;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.table.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nasiiCalendar.BasicCalendar;
import nasiiCalendar.BasicDate;

/**
 *
 * @author Hussam
 */
public class DateController extends CalendarControllerContainer {

    //   TextField yearTF;
    ComponentWithHeader centerView;
//    boolean samiFlag = true;

 //   Dialog chooseCalDialog;
    CalendarViewType type;
    String symbol;

    public DateController() {
        this(CalendarViewType.YEAR_VIEW);
    }

    public DateController(CalendarViewType view) {

        this(getStore().getCalendar(BasicCalendar.SAMI_FIXED_ID).getDate(new Date().getTime()), view);

    }
    
    public DateController(BasicDate bd, CalendarViewType cm) {
        super(bd);
        type = cm;
        //BasicDate dateFirst = CalendarFactory.getSamiCalendar().getDate(bd.getDate());
        setupData();
        prepareLayout();
        updateMyUI(null);
        
    }

    private void setupData() {
             if(calendars.size()==0)
           calendars.add(getStore().getCalendar(BasicCalendar.SAMI_FIXED_ID));

    }

    public List<BasicCalendar> getSelectedCalendars() {
        return new ArrayList<BasicCalendar>(calendars);

    }

    public void setCalendars(List<BasicCalendar> c) {
        super.setCalendars(c);
        BasicCalendar bc = (BasicCalendar) calendars.get(0);
        BasicDate dateFirst = bc.getDate(getSelectedDate().getDate());
        int month = type == CalendarViewType.MONTH_VIEW ? dateFirst.getMonth() : 1;
        dateFirst = bc.getDate(dateFirst.getYear(), month, 1);
        //hussam.println("setting......................: " + dateFirst);
        setSelectedDate(dateFirst);

    }

    private ComponentWithHeader createMyTable() {

        switch (type) {
            case DAY_VIEW:
                return new ViewDate(getSelectedDate(),UserDefaults.getInstance().getSelectedCalendars());
            case YEAR_MONTH_VIEW:
                return new YearMonthTable(getSelectedDate(), getSelectedCalendars());
            case MONTH_VIEW:
            case YEAR_VIEW: //hussam.println("Selected: "+getSelectedCalendars());
                return new CalendarTable(CalendarModel.createModel(getSelectedDate(), getSelectedCalendars(), type));

            case METONIC_YEAR_TYPE_VIEW:
            default:
                return new YearTypeCycleTable(getSelectedDate(), getSelectedCalendars());

        }

    }
    public CalendarController getController(){
        return (CalendarController) this.centerView;
    }
    public void prepareLayout() {
        Container tabCont = new Container(new BorderLayout());

        setLayout(new BorderLayout());
        centerView = createMyTable();
        ((Container)centerView).getAllStyles().setBgImage(SamiApplication.getHanahLogo());
        //    calendatTable.getAllStyles().setb
        tabCont.add(BorderLayout.NORTH, centerView.createSpecialHeader());
        Container c = MainForm.getForm().prepareFloatingButton((Container) getController());
        tabCont.add(BorderLayout.CENTER, c);

        add(BorderLayout.CENTER, tabCont);
        Container sCont = new Container(new GridLayout(1, 4));//BoxLayout.X_AXIS));
        symbol = getController().getChangeSymbol();

        Button s = new Button("<" + symbol);
        s.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                previous();
                updateMyUI(null);
                fireControllerUpdated(null);
            }

        });
        sCont.add(s);

        Image icon = FontImage.createMaterial(FontImage.MATERIAL_ARROW_DOWNWARD, s.getStyle());
        //    icon=getHanahLogo();
        //ScaleImageButton si = new ScaleImageButton(icon);
        //si.setBackgroundType(Style.BACKGROUND_IMAGE_TILE_BOTH);
        s = new Button(icon);
        s.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setToday();
                updateMyUI(null);
                fireControllerUpdated(null);
            }

        });
        ;
        sCont.add(s);
        s = new Button(symbol + ">");
        s.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                next();
                updateMyUI(null);
                fireControllerUpdated(null);
            }

        });

        sCont.add(s);
        sCont.add(new Button(new Command("", FontImage.createMaterial(FontImage.MATERIAL_ZOOM_OUT, s.getAllStyles())) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                zoomOut();

            }

        }));
        //sCont.add(hanah);

        this.add(BorderLayout.SOUTH, sCont);
        //this.add(gCont);
    }

    public void zoomOut() {
        switch (type) {
            case DAY_VIEW: openView(CalendarViewType.MONTH_VIEW);break;
            case MONTH_VIEW:
                openView(CalendarViewType.YEAR_VIEW);
                break;
            case YEAR_VIEW:
                openView(CalendarViewType.YEAR_MONTH_VIEW);
                break;
            case YEAR_MONTH_VIEW:
                openView(CalendarViewType.METONIC_YEAR_TYPE_VIEW);
                break;

        }
    }

    protected void updateMyUI(Component source) {
        //long t=bd.getDate();
        String t = getSelectedDate().getCalendar().getShortName();
        //      yearTF.setText(getSelectedDate().getYear() + " " + getSelectedDate().getCalendar().getMonthName(getSelectedDate()) + " " + t);
//        firstLabel.setText(dateFirst.toString());

    }

    Table getCalendarTable() {
        return (Table) centerView;
    }

    public void openView(CalendarViewType type) {
        //    hussam.println("selected type: "+type);
        CalendarView dc = getForm().setView(type, getSelectedDate());
//        dc.setSelectedDate(getSelectedDate());
       
    }

    private MainForm getForm() {
        try {
            MainForm m = (MainForm) CN.getCurrentForm();
            return m;
        } catch (ClassCastException e) {
            System.err.println("not form: " + e.getMessage());
            return null;
        }
    }

    public void setSelectedDate(BasicDate bd) {
        if (!getSelectedCalendars().contains(bd.getCalendar())) {
            List<BasicCalendar> b = new ArrayList<BasicCalendar>();
            b.add(bd.getCalendar());
            b.addAll(getSelectedCalendars());
            setCalendars(b);

        }
        super.setSelectedDate(bd);
        getController().setCalendars(getSelectedCalendars());
        getController().setSelectedDate(bd);
        //calendatTable.get.setModel(calendatTable.createWeekModel());
        // calendatTable.setModel(CalendarModel.createModel(bd, getSelectedCalendars(), type));
        //calendatTable.getWeekTable().setModel(calendatTable.createWeekModel());

    }

    public String getHeader() {
        return getController().getHeader();
        //return getT(getSelectedDate().getCalendar().getShortName()) + " " + getSelectedDate().getYear() + " " + ((ct == CalendarViewType.MONTH_VIEW) ? getSelectedDate().getMonth() : "");
    }

    public void setToday() {
        BasicDate dateFirst = getController().getSelectedDate().getCalendar().getDate(new Date().getTime());

        setSelectedDate(dateFirst);
    }

    @Override
    public void next() {

        getController().next();

    }

    @Override
    public void previous() {

        getController().previous();

    }

    @Override
    public String getChangeSymbol() {
        return getController().getChangeSymbol();
    }

    @Override
    Component getNorthView() {
        if(type==CalendarViewType.DAY_VIEW)
            return (centerView.createSpecialHeader2());
        return null;
    }

}
