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
class YearMonthModel extends AbstractTableModel {

    List<BasicCalendar> calendars = new ArrayList<BasicCalendar>();
    List<DataChangedListener> list = new ArrayList<DataChangedListener>();
    BasicDate firstDate;

//    private BasicDate lastDate;
    public YearMonthModel(BasicDate b, List<BasicCalendar> list) {
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
        bd = bd.getCalendar().getDate(year, month, day);
        firstDate = bd;

    }

    public List<BasicCalendar> getCalendars() {
        return calendars;
    }

    @Override
    public int getRowCount() {
        return getSelectedDate().getCalendar().getYearType(getSelectedDate()).getSubPeriods().size();

        /*BasicDate s1=getSelectedDate();
        BasicDate s2=getSelectedDate().getCalendar().getDate(s1.getDate()+getSelectedDate().getCalendar().getLongCycleTime());
        int month=(int) ((s2.getDate()-s1.getDate())/MONTH);
        //int year=s2.getYear()-s1.getYear();
        return month;
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
        //    BasicDate bd=bd.getCalendar().getDate(col, col, col)
        if (col == 0) {

            return calendars.get(col).getDate(firstDate.getYear(), firstDate.getMonth() + row, firstDate.getDay());
        }
        //  if(firstDate.getDay()==22)throw new RuntimeException();
        //   hussam.println("row: "+row+" col:"+col+" date: "+calendars.get(col - 1).getDate(firstDate.getDate() + MONTH * (row )));
        BasicDate bd1 = calendars.get(0).getDate(firstDate.getYear(), firstDate.getMonth() + row, firstDate.getDay());
        return calendars.get(col - 1).getDate(bd1.getDate());
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

public class YearMonthTable extends Container implements ComponentWithHeader, ActionListener, CalendarController {

    ActionListener al;
    //   CalendarModel tm;
    Table header;
    CellEditor myEditor;
    Table table;

    YearMonthTable(BasicDate selectedDate, List<BasicCalendar> selectedCalendars) {
        table = new Table(new YearMonthModel(selectedDate, selectedCalendars), false) {
            @Override
            protected Component createCell(Object value, int row, int column, boolean editable) { // (1)

                BasicDate bd = (BasicDate) value;
                Button buttonRenderer = CellEditor.getBasicButton();
                if (fi == null) {
                    fi = FontImage.createMaterial(FontImage.MATERIAL_EXPLORE, buttonRenderer.getAllStyles());
                }

                buttonRenderer.addActionListener(YearMonthTable.this);
                buttonRenderer.setText(bd.getCalendar().getMonthName(bd).getName());
                if (myEditor == null) {
                    myEditor = new CellEditor();
                }
                if (bd.equals(bd.getCalendar().getMaximumDate())) {
                    myEditor.strikeThrough(buttonRenderer, bd);
                    return buttonRenderer;
                }
                if (bd.getCalendar().getYearType(bd).getSubPeriods().get(0).equals(bd.getCalendar().getMonthName(bd))) {
                    buttonRenderer.setText(bd.getYear() + buttonRenderer.getText());
                }
                myEditor.prepareReligious(buttonRenderer, bd);
                myEditor.prepareTodayMonth(buttonRenderer, bd);

                if (column == 0) {
                    buttonRenderer.setText(row + 1 + "");
                    myEditor.prepareSeasons(buttonRenderer, bd, CalendarFactory.getDefaultSeasonIdentifier());

                }
                if (column > 1) {
                    buttonRenderer.setText(buttonRenderer.getText() + " " + bd.getDay());
                }
                if (bd.getCalendar() instanceof LunerLocationCalendar) {
                    String x = getT("minutesmall") + ":" + bd.getMinute();
                    if (bd.getMinute() == 0) {
                        x = "";
                    }

                    LunerLocationCalendar cal = (LunerLocationCalendar) bd.getCalendar();
                    buttonRenderer.setText(getT(cal.getName()) + " " + bd.getDay() + x);

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
        //    hussam.println("selected metonic start: " + selectedDate);
        table.setRTL(getFormRTL());
        table.setDrawBorder(false);
        table.setScrollableY(true);
        table.setScrollVisible(false);
        createSpecialHeader();
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
        if (al != null) {
            al.actionPerformed(arg0);
        }
        int r = table.getSelectedRow();
        int c = table.getSelectedColumn();
        if (r < 0 || c <= 0) {

        }

        //  hussam.print("r: " + r + " c:" + c + " " + " ");
        Object o = getModel().getValueAt(r, c);
        if (o instanceof BasicDate) {
            BasicDate bd = (BasicDate) o;
            CalendarFactory.testDate(bd, c - 1);
            if (bd.equals(bd.getCalendar().getMaximumDate())) {
                getForm().proposeBuy(bd);
            } else {
                CalendarView dc = getForm().setView(CalendarViewType.MONTH_VIEW, bd);
            }
            //dc.setSelectedDate(bd);

        }

    }

    public YearMonthModel getModel() {
        return (YearMonthModel) table.getModel();
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
                buttonRenderer.addActionListener(YearMonthTable.this);
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
    FontImage fi = null;

    void addActionListener(ActionListener<ActionEvent> actionListener) {
        al = actionListener;
    }

    @Override
    public void setSelectedDate(BasicDate bd) {
        table.setModel(new YearMonthModel(bd, getModel().getCalendars()));
        header.setModel(createWeekModel());
    }

    @Override
    public BasicDate getSelectedDate() {
        return getModel().firstDate;

    }

    @Override
    public void setCalendars(List<BasicCalendar> list) {
        table.setModel(new YearMonthModel(getSelectedDate(), list));
    }

    @Override
    public String getHeader() {
        return getT(getSelectedDate().getCalendar().getYearType(getSelectedDate()).getName()) + " " + getSelectedDate().getYear() + " " + getT(getSelectedDate().getCalendar().getName());

    }

    @Override
    public List<BasicCalendar> getCalendars() {
        return getModel().getCalendars();
    }

    @Override
    public void next() {
        BasicDate bd = getSelectedDate();
        //hussam.print("old DAte: " + bd);

        bd = bd.getCalendar().getDate(bd.getYear() + 1, 1, 1);

        //hussam.println("new Date: " + bd);
        setSelectedDate(bd);
    }

    @Override
    public void previous() {
        BasicDate bd = getSelectedDate();

        bd = bd.getCalendar().getDate(bd.getYear() - 1, 1, 1);

        setSelectedDate(bd);
    }

    @Override
    public void addControllerListiner(DateControllerListener d) {

    }

    @Override
    public void removeControllerListiner(DateControllerListener d) {

    }

    @Override
    public String getChangeSymbol() {
        return "Y1";
    }

}
