/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import static com.alhanah.samicalapp.MainForm.getFormRTL;
import static com.alhanah.samicalapp.SamiApplication.getAppVersion;
import static com.alhanah.samicalapp.SamiApplication.getStore;
import static com.alhanah.samicalapp.SamiApplication.getT;
import static com.alhanah.samicalapp.SamiApplication.showHelp;
import com.alhanah.samicalapp.store.UserDefaults;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.table.AbstractTableModel;
import com.codename1.ui.table.DefaultTableModel;
import com.codename1.ui.table.Table;
import com.codename1.ui.table.TableLayout;
import java.util.ArrayList;
import java.util.List;
import nasiiCalendar.BasicCalendar;
import static nasiiCalendar.BasicCalendar.DAY;
import nasiiCalendar.BasicDate;
import nasiiCalendar.CalendarFactory;
import nasiiCalendar.locationBasid.LunerLocationCalendar;
import nasiiCalendar.MyBasicCalendar;

/**
 *
 * @author DEll
 */
public class ViewDate extends Container implements ComponentWithHeader, CalendarController {

    Table table;
    MyDateSpinner spinner;
    CellEditor editor = new CellEditor();
    List<BasicCalendar> mainCalendars = new ArrayList<>();
    List<BasicCalendar> list = new ArrayList<>();
    private AccordianInfo accord;
    ViewDate(BasicDate bd, List<BasicCalendar> list) {
        //hussam.println("all list2: "+list);
        this.list=list;
        mainCalendars=UserDefaults.getInstance().getSelectedCalendars();
        table = new Table(new MyModel(bd, list), false) {
            @Override
            protected TableLayout.Constraint createCellConstraint(Object value, int row, int column) {
                TableLayout.Constraint con = super.createCellConstraint(value, row, column);
                con.setHorizontalSpan(1);

                con.setWidthPercentage(50);

//*/
                return con;
            }

            protected Component createCell(Object value, int row, int column, boolean editable) { // (1)
                if (value instanceof String | value == null) {
                    return super.createCell(value, row, column, editable);
                }
                final BasicDate b = (BasicDate) value;
                Button buttonRenderer = CellEditor.getBasicButton();
                buttonRenderer.setUIID("DateViewCell");
                if (editor == null) {
                    editor = new CellEditor();
                }
                if (column == 0) {
                    buttonRenderer.setText(b.getCalendar().getName());
                    editor.setURL(buttonRenderer);
                    buttonRenderer.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            showHelp(b.getCalendar().getName() + ".url");
                        }
                    });
                } else if (column == 1) {
                    editor.prepareReligious(buttonRenderer, b);
                    buttonRenderer.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            if (b.equals(b.getCalendar().getMaximumDate())) {
                                getForm().proposeBuy(b);
                            } else {
                                setSelectedDate(b);

                            }

                        }
                    });
                    if (b.getCalendar().getMaximumDate().equals(b)) {
                        editor.strikeThrough(buttonRenderer, b);
                        return buttonRenderer;
                    }
                    buttonRenderer.setText(getT(b.getCalendar().getMonthName(b).getName()) + " " + b.getDay() + " " + b.getYear());
                    if (b.getCalendar() instanceof LunerLocationCalendar) {
                        LunerLocationCalendar cal = (LunerLocationCalendar) b.getCalendar();
                        if (b.getDay() == 1) {
                            buttonRenderer.setText(getT(cal.getName()) + " 1: " + b.getMinute());
                        } else {
                            buttonRenderer.setText(getT(cal.getName()) + " " + b.getDay());
                        }
                    }

                } else if (column == 2) {
                    MyBasicCalendar m = (MyBasicCalendar) b.getCalendar();
                    if (getCalendars().contains(m)) {
                        buttonRenderer.setIcon(editor.getRemoveImage());
                        //        buttonRenderer.setText("hideCalendar");
                    } else {
                        //      buttonRenderer.setText("showCalendar");
                        buttonRenderer.setIcon(editor.getAddImage());
                    }
                    buttonRenderer.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            List<BasicCalendar> list = getCalendars();
                            if (list.contains(m)) {
                                list.remove(m);

                            } else {
                                list.add(m);

                            }
                            setCalendars(list);
                        }
                    });

                }

                //editor.prepareToday(buttonRenderer, b);
                return buttonRenderer;
            }

        };
    //    mainCalendars.add(CalendarFactory.getSamiCalendar());
      //  mainCalendars.add(getStore().getCalendar(BasicCalendar.OMARI_ID_16));
        //mainCalendars.add(getStore().getCalendar(BasicCalendar.AD_ID));
        setCalendars(list);
        table.setDrawBorder(false);
    //    table.setScrollableY(true);
        table.setScrollVisible(false);
        table.setRTL(getFormRTL());
        prepare();
        updateMyUI(null);
    }

    @Override
    public Container createSpecialHeader2() {
         return null;
    }

    MyModel getMyModel() {
        return (MyModel) table.getModel();
    }

    private void prepare() {

        spinner = new MyDateSpinner(getSelectedDate());
        spinner.addControllerListiner(new DateControllerListener() {
            @Override
            public void dateUpdated(Object source, BasicDate bd) {
                table.setModel(new MyModel(spinner.getSelectedDate(), getCalendars()));
                updateMyUI(spinner);
            }
        });
        table.setModel(new MyModel(getSelectedDate(), getStore().getCalendars()));
            
        table.setRTL(getFormRTL());
      //  table.setScrollableY(true);
        table.setDrawBorder(
                false);
        table.getAllStyles()
                .setBgImage(SamiApplication.getHanahLogo());
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        setScrollableY(true);
        
        Label span=new Label(getT("welcome")+" "+getT("version")+" "+getAppVersion());
        editor.prepareHeading(span);
        add(span);
        try{
        accord=new AccordianInfo(mainCalendars, getSelectedDate());
        }catch(Exception e){
            Dialog.show("3"+e.getMessage(), e.getMessage(), "o","ok");
            
            e.printStackTrace();
        }
        add(accord);
        span=new Label("dateconversion");
        editor.prepareHeading(span);
        add(span);
        add(spinner);
        span=new Label("allcalendars");
        editor.prepareHeading(span);
        add(span);
        
        add(table);
        
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

    @Override
    public String getChangeSymbol() {
        return "1D";
    }

    @Override
    public void setCalendars(List<BasicCalendar> list) {
        this.list=list;
        BasicDate b = getSelectedDate();
        try {
            BasicCalendar c = getCalendars().get(0);
            setSelectedDate(c.getDate(b.getDate()));
        } catch (RuntimeException re) {
            System.err.println("maybe empty: " + re.getMessage());

        }

    }

    public void setSelectedDate(BasicDate b) {
        table.setModel(new MyModel(b, getStore().getCalendars()));
        updateMyUI(null);
        accord.setSelectedDate(b);
        accord.updateMyUI();
    }

    protected void openMonthView(BasicDate b) {
        CalendarView dc = getForm().setView(CalendarViewType.MONTH_VIEW, getSelectedDate());
        //   dc.setSelectedDate(b);

    }

    protected void updateMyUI(Component source) {
        // editor.prepareAll(labelDate, getSelectedDate());

        List<BasicCalendar> list = getStore().getCalendars();//CalendarFactory.createAllCalendars();
        if (source != spinner) {
            spinner.setSelectedDate(getSelectedDate());
        }

        if (source != this) {
         //   table.setModel(new CalendarModel(getSelectedDate(), getStore().getCalendars()));
        }
  //      accord.setSelectedDate(getSelectedDate());
    //    accord.updateMyUI();
    }

    @Override
    public String getHeader() {
        return getT(CalendarFactory.getWeekDay(getSelectedDate())) + " " + getT(getSelectedDate().getCalendar().getMonthName(getSelectedDate()).getName()) + " " + getSelectedDate().getDay() + " " + getSelectedDate().getYear() + " " + getT(getSelectedDate().getCalendar().getName());
    }

    @Override
    public void next() {

        BasicDate b = getSelectedDate();
        b = b.getCalendar().nextDay(b);
        setSelectedDate(b);
    }

    @Override
    public void previous() {
        BasicDate b = getSelectedDate();
        b = b.getCalendar().getDate(b.getDate() - DAY);
        setSelectedDate(b);
    }

    @Override
    public Table createSpecialHeader() {
        Table table = new Table(new DefaultTableModel(new String[]{}, new Object[][]{{"cal2", "cal1"}}), false) {
            @Override
            protected TableLayout.Constraint createCellConstraint(Object value, int row, int column) {
                TableLayout.Constraint con = super.createCellConstraint(value, row, column);
                con.setHorizontalSpan(1);

                con.setWidthPercentage(50);

//*/
                return con;
            }

            @Override
            protected Component createCell(Object value, int row, int column, boolean editable) {
                Button buttonRenderer = editor.getBasicButton();
                buttonRenderer.setUIID("DateViewCell");
                buttonRenderer.setText(value.toString());
                
                return buttonRenderer;
            }
            
        };
        
       
        table.setRTL(getFormRTL());
        return table;
    }

    @Override
    public BasicDate getSelectedDate() {
        return getMyModel().getSelectedDate();
    }

    @Override
    public List<BasicCalendar> getCalendars() {
        return list;

    }

    @Override
    public void addControllerListiner(DateControllerListener d) {

    }

    @Override
    public void removeControllerListiner(DateControllerListener d) {

    }

}

class MyModel extends AbstractTableModel {

    BasicDate bd;
    List<BasicCalendar> list;

    MyModel(BasicDate m, List<BasicCalendar> list) {
        bd = m;
        this.list = list;
    }

    public List<BasicCalendar> getCalendars() {
        return list;
    }

    public BasicDate getSelectedDate() {
        return bd;
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int arg0) {
        switch (arg0) {
            case 0:
                return getT("Calendar");
            case 1:
                return getT("Date");
            case 2:
                return getT("showColumn");
            default:
                return "";

        }
    }

    @Override
    public boolean isCellEditable(int arg0, int arg1) {
        return true;
    }

    @Override
    public Object getValueAt(int r, int c) {
        return list.get(r).getDate(bd.getDate());

    }

    @Override
    public void setValueAt(int arg0, int arg1, Object arg2) {

    }

    @Override
    public void addDataChangeListener(DataChangedListener arg0) {

    }

    @Override
    public void removeDataChangeListener(DataChangedListener arg0) {

    }

}
