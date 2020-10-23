/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import static com.alhanah.samicalapp.CellEditor.getBasicButton;
import static com.alhanah.samicalapp.MainForm.getFormRTL;
import static com.alhanah.samicalapp.SamiApplication.getT;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.table.AbstractTableModel;
import com.codename1.ui.table.Table;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.table.TableModel;

import java.util.ArrayList;
import java.util.List;
import nasiiCalendar.BasicCalendar;
import nasiiCalendar.BasicDate;
import nasiiCalendar.CalendarFactory;
import nasiiCalendar.locationBasid.LunerLocationCalendar;

/**
 *
 * @author Hussam
 */
class YearTypeCycleModel extends AbstractTableModel {

    List<BasicCalendar> calendars = new ArrayList<BasicCalendar>();
    List<DataChangedListener> list = new ArrayList<DataChangedListener>();
    BasicDate firstDate;

//    private BasicDate lastDate;
    public YearTypeCycleModel(BasicDate b, List<BasicCalendar> list) {
        this.calendars.addAll(list);
        fixDate(b);

    }

    public BasicDate getSelectedDate() {
        return firstDate;
    }

    private void fixDate(BasicDate bd) {

        int day = 1;
        int month = 1;
        int year = bd.getYear();
        //hussam.print("selected: " + bd);
        bd = bd.getCalendar().getCycleStart(bd.getDate());
        //hussam.println(" startCycle:" + bd);
        firstDate = bd;

    }

    public List<BasicCalendar> getCalendars() {
        return calendars;
    }

    @Override
    public int getRowCount() {
        BasicDate s1 = getSelectedDate();
        BasicDate s2 = getSelectedDate().getCalendar().getDate(s1.getDate() + getSelectedDate().getCalendar().getLongCycleTime());
        //int month=(int) ((s2.getDate()-s1.getDate())/MONTH);
        int year = s2.getYear() - s1.getYear();
        if (year > 353) {
            return 353;
        }
        return year;
        //*/

    }

    @Override
    public int getColumnCount() {

        return calendars.size() + 1;
    }

    @Override
    public String getColumnName(int arg0) {
        if (arg0 == 0) {
            return "nothing";
        }
        return calendars.get(arg0 - 1).getName();
    }

    @Override
    public boolean isCellEditable(int arg0, int arg1) {
        return true;
    }

    @Override
    public Object getValueAt(int row, int col) {

        if (col == 0) {
            return calendars.get(col).getDate(firstDate.getYear(), 1, 1);
        }
        BasicDate bd = firstDate.getCalendar().getDate(firstDate.getYear() + row, 1, 1);
        return calendars.get(col - 1).getDate(bd.getDate());
    }

    @Override
    public void setValueAt(int arg0, int arg1, Object arg2) {

    }

    @Override
    public void addDataChangeListener(DataChangedListener arg0) {
        list.add(arg0);
    }

    @Override
    public void removeDataChangeListener(DataChangedListener arg0) {
        list.remove(arg0);
    }

    CalendarViewType getCalType() {
        return CalendarViewType.YEAR_MONTH_VIEW;
    }

}

public class YearTypeCycleTable extends Container implements ComponentWithHeader, ActionListener, CalendarController {

    ActionListener al;
    Table header;
    CellEditor myEditor;
    Table table;

    YearTypeCycleTable(BasicDate selectedDate, List<BasicCalendar> selectedCalendars) {
        table = new Table(new YearTypeCycleModel(selectedDate, selectedCalendars), false) {
            @Override
            protected Component createCell(Object value, int row, int column, boolean editable) { // (1)

                BasicDate bd = (BasicDate) value;
                Button buttonRenderer = getBasicButton();
                if (fi == null) {
                    fi = FontImage.createMaterial(FontImage.MATERIAL_EXPLORE, buttonRenderer.getAllStyles());
                }
                if (myEditor == null) {
                    myEditor = new CellEditor();
                }
                if (bd.equals(bd.getCalendar().getMaximumDate())) {
                    buttonRenderer.setText("buyCell");
                    buttonRenderer.setIcon(fi);
                    myEditor.strikeThrough(buttonRenderer, bd);
                    return buttonRenderer;
                }

                buttonRenderer.addActionListener(YearTypeCycleTable.this);
                buttonRenderer.setText(bd.getCalendar().getYearType(bd).getName());

                if (column > 1) {
                    buttonRenderer.setText(buttonRenderer.getText() + " " + bd.getYear() + " " + bd.getMonth());
                }
                //    if (bd.getCalendar().getYearType(bd).getMonths().get(0).equals(bd.getCalendar().getMonthName(bd))) {

                //  }
                myEditor.prepareYearType(buttonRenderer, bd);
                //    myEditor.prepareReligious(buttonRenderer, bd);
                myEditor.prepareTodayYear(buttonRenderer, bd);

                if (column == 0) {
                    buttonRenderer.setText((bd.getYear() + row) + "");
                    myEditor.prepareSeasons(buttonRenderer, bd, CalendarFactory.getDefaultSeasonIdentifier());

                }
                if (bd.getCalendar() instanceof LunerLocationCalendar) {
                    LunerLocationCalendar cal = (LunerLocationCalendar) bd.getCalendar();
                    String x = getT("minutesmall") + ":" + bd.getMinute();
                    if (bd.getMinute() == 0) {
                        x = "";
                    }
                    buttonRenderer.setText(getT(cal.getShortName()) + " " + bd.getDay() + x);

                }
                //   hussam.println("metonic: "+value);
                return buttonRenderer;
            }

            protected TableLayout.Constraint createCellConstraint(Object value, int row, int column) {
                TableLayout.Constraint con = super.createCellConstraint(value, row, column);
                con.setHorizontalSpan(1);
                int base = 15;
                con.setWidthPercentage((column == 0) ? base : ((100 - base) / (getModel().getColumnCount() - 1)));

                return con;
            }

        };
        //   hussam.println("selected metonic start: " + selectedDate);
        table.setDrawBorder(false);
        table.setScrollableY(true);
        table.setScrollVisible(false);
        createSpecialHeader();
        table.setRTL(getFormRTL());
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER,table);
                table.getAllStyles().setBgImage(SamiApplication.getHanahLogo());

    }

    @Override
    public Container createSpecialHeader2() {
        return null;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void actionPerformed(ActionEvent arg0) {

        int r = table.getSelectedRow();
        int c = table.getSelectedColumn();

        //  hussam.print("r: " + r + " c:" + c + " " + " ");
        Object o = getModel().getValueAt(r, c);
        if (o instanceof BasicDate) {
            BasicDate bd = (BasicDate) o;
            CalendarFactory.testDate(bd, c - 1);
            if (bd.equals(bd.getCalendar().getMaximumDate())) {
                getForm().proposeBuy(bd);
            } else {
                CalendarView dc = getForm().setView(CalendarViewType.YEAR_MONTH_VIEW, bd);
            }
            //     dc.setSelectedDate(bd);

        }

    }

    public YearTypeCycleModel getModel() {
        return (YearTypeCycleModel) table.getModel();
    }

    public TableModel createWeekModel() {
        AbstractTableModel wm = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return 1;
            }

            @Override
            public int getColumnCount() {
                int x = getModel().getColumnCount();
                return x;
            }

            @Override
            public String getColumnName(int c) {
                if (c == 0) {
                    return "#";
                }
                return getModel().getCalendars().get(c - 1).getName();

            }

            @Override
            public boolean isCellEditable(int arg0, int arg1) {
                return true;
            }

            @Override
            public Object getValueAt(int row, int column) {
                if (column == 0) {
                    return "#";
                }
                return getModel().getCalendars().get(column - 1).getName();
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
        };
        return wm;
    }

    public Table createSpecialHeader() {

        header = new Table(createWeekModel(), false) {
            protected Component createCell(Object value, int row, int column, boolean editable) { // (1)

                Button buttonRenderer = getBasicButton();
                buttonRenderer.addActionListener(YearTypeCycleTable.this);
                buttonRenderer.setText(value.toString());
                return buttonRenderer;
            }

            protected TableLayout.Constraint createCellConstraint(Object value, int row, int column) {
                TableLayout.Constraint con = super.createCellConstraint(value, row, column);
                con.setHorizontalSpan(1);
                int base = 15;
                con.setWidthPercentage((column == 0) ? base : ((100 - base) / (getModel().getColumnCount() - 1)));

                return con;
            }
        };//*/
        header.setRTL(getFormRTL());
        return header;
    }
    FontImage fi;

    void addActionListener(ActionListener<ActionEvent> actionListener) {
        al = actionListener;
    }

    @Override
    public void setSelectedDate(BasicDate bd) {
        table.setModel(new YearTypeCycleModel(bd, getModel().getCalendars()));
        header.setModel(createWeekModel());
    }

    @Override
    public BasicDate getSelectedDate() {
        return getModel().firstDate;

    }

    @Override
    public void setCalendars(List<BasicCalendar> list) {
        table.setModel(new YearTypeCycleModel(getSelectedDate(), list));
    }

    @Override
    public String getHeader() {
        return getSelectedDate().getYear() + "<>" + (getSelectedDate().getYear() + (getModel().getRowCount())) + " " + getT(getSelectedDate().getCalendar().getName());

    }

    @Override
    public List<BasicCalendar> getCalendars() {
        return getModel().getCalendars();
    }

    @Override
    public void next() {
        BasicDate bd = getSelectedDate();
       // hussam.print("NEXT>>>> old Date: " + bd + " adding: " + bd.getCalendar().getLongCycleYear());

        bd = bd.getCalendar().getDate(bd.getYear() + bd.getCalendar().getLongCycleYear(), 1, 1);

       // hussam.println("new Date: " + bd);
        setSelectedDate(bd);
    }

    @Override
    public void previous() {
        BasicDate bd = getSelectedDate();
        //hussam.print("PREVIOUS>>>> old Date: " + bd);

        bd = bd.getCalendar().getDate(bd.getYear() - bd.getCalendar().getLongCycleYear(), 1, 1);
        //.println("new Date: " + bd);

        setSelectedDate(bd);
    }

    @Override
    public String getChangeSymbol() {
        BasicDate bd = getSelectedDate();
        return "Y" + ((bd.getCalendar().getLongCycleYear() > 353) ? 353 : bd.getCalendar().getLongCycleYear());
    }

    @Override
    public void addControllerListiner(DateControllerListener d) {
        
    }

    @Override
    public void removeControllerListiner(DateControllerListener d) {
        
    }

}
