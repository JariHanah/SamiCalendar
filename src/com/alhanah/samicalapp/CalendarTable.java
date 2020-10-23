/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the myEditor.
 */
package com.alhanah.samicalapp;

import static com.alhanah.samicalapp.MainForm.getFormRTL;
import static com.alhanah.samicalapp.SamiApplication.getT;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.table.AbstractTableModel;
import com.codename1.ui.table.Table;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.table.TableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import nasiiCalendar.BasicCalendar;
import static nasiiCalendar.BasicCalendar.DAY;
import nasiiCalendar.BasicDate;
import nasiiCalendar.CalendarFactory;
import nasiiCalendar.locationBasid.LunerLocationCalendar;

/**
 *
 * @author DEll
 */
public class CalendarTable extends Container implements ComponentWithHeader, ActionListener<ActionEvent>, CalendarController {

    ActionListener al;
    //   CalendarModel tm;
//    Table weekTable;
    CellEditor myEditor;
    //   Map<BasicCalendar, Boolean>calendars=new HashMap<BasicCalendar, Boolean>();
    Table table;

    CalendarTable(CalendarModel tm) {
        table = new Table(tm, false) {

            @Override
            protected Component createCell(Object value, int row, int column, boolean editable) { // (1)
                if (myEditor == null) {
                    //hussam.println("strange!!");
                    myEditor = new CellEditor();
                }

                Button buttonRenderer = CellEditor.getBasicButton();
                buttonRenderer.addActionListener(CalendarTable.this);

                if (((CalendarModel)getModel()).getCalType() == CalendarViewType.MONTH_VIEW) {
                    buttonRenderer.getAllStyles().setFont(myEditor.getMediumFont());
                } else {
                    buttonRenderer.getAllStyles().setFont(myEditor.getSmallFont());
                }
                buttonRenderer.setText(value.toString());
                if (row < 0) {
                    buttonRenderer.getUnselectedStyle().setBgColor(0xffffff);
                    Calendar g = CalendarFactory.getGreg();
                    g.setTime(new Date());
                    if (column == g.get(Calendar.DAY_OF_WEEK)) {
                        buttonRenderer.getUnselectedStyle().setBgColor(0xffff00);
                    }
                }
                BasicDate bd = (BasicDate) value;
                if (bd.getMonth() % 2 == 0) {
                    buttonRenderer.getAllStyles().setBgTransparency(170);
                }

                if (column == 0 && row >= 0) {

                    if (!(bd.getCalendar() instanceof LunerLocationCalendar) && bd.getCalendar().getMaximumDate().equals(bd)) {
                        myEditor.strikeThrough(buttonRenderer, bd);
                        buttonRenderer.setText(bd.getCalendar().getName());
                        return buttonRenderer;
                    }

                    buttonRenderer.setText(getT(bd.getCalendar().getMonthName(bd).getName()) + " " + getT(bd.getCalendar().getShortName()));
                    //        hussam.println("editor: "+myEditor);

                    myEditor.prepareSeasons(buttonRenderer, bd, CalendarFactory.getDefaultSeasonIdentifier());

                } else {
                    if (value instanceof BasicDate) {
                        //       BasicDate bd = (BasicDate) value;
                        CalendarModel tm = (CalendarModel) getModel();
                        buttonRenderer.getUnselectedStyle().setBgColor(0xffffff);
                        buttonRenderer.setText(bd.getDay() + "");
                        if (!(bd.getCalendar() instanceof LunerLocationCalendar) && bd.equals(bd.getCalendar().getMaximumDate())) {

                            myEditor.strikeThrough(buttonRenderer, bd);

                        } else {
                            if (bd.getDate() < tm.getFirstDate().getDate() || bd.getDate() > tm.getLastDate().getDate()) {
                                buttonRenderer.getUnselectedStyle().setBgTransparency(100);

                            }

                            if (bd.getCalendar().getName().equals(tm.getFirstDate().getCalendar().getName())) {
                                myEditor.prepareSeasons(buttonRenderer, bd, CalendarFactory.getDefaultSeasonIdentifier());
                                //    buttonRenderer.getAllStyles().setAlignment(Component.CENTER);
                            } else {
                                //buttonRenderer.getAllStyles().setAlignment(Component.RIGHT);
                                //   buttonRenderer.getAllStyles().setFont(Component.LEFT, 10);
                            }
                            if (bd.getDay() == 1) {
                                buttonRenderer.setText(getT(bd.getCalendar().getMonthName(bd).getName()) + " " + ((bd.getCalendar() instanceof LunerLocationCalendar) ? "" : bd.getYear()));
                                if (column >= 6) {
                                    buttonRenderer.setText(bd.getCalendar().getMonthName(bd).getName());
                                }

                            } else if (column != 0 && (bd.getDay() == 2 && column != 1) | (bd.getDay() == 3 && column != 1 && column != 2)) {
                                buttonRenderer.setText("");
                                buttonRenderer.getUnselectedStyle().setBgTransparency(0);
                                buttonRenderer.getUnselectedStyle().setBorder(null);
                                return buttonRenderer;

                            }
                            myEditor.prepareReligious(buttonRenderer, bd);
                            myEditor.prepareToday(buttonRenderer, bd);

                        }
                        if (bd.getCalendar() instanceof LunerLocationCalendar) {
                            LunerLocationCalendar cal = (LunerLocationCalendar) bd.getCalendar();
                            String x = ":" + bd.getMinute() + getT("minutesmall");
                            if (bd.getMinute() == 0) {
                                x = "";
                            }

                            if (bd.getDay() == 1) {
                                buttonRenderer.setText(getT(cal.getName()) + x);
                            } else {
                                buttonRenderer.setText(+bd.getDay() + "");
                            }
                        }

                    }
                }
                return buttonRenderer;
            }

            @Override
            protected TableLayout.Constraint createCellConstraint(Object value, int row, int column) {
                TableLayout.Constraint con = super.createCellConstraint(value, row, column);
                con.setHorizontalSpan(1);
                if (value instanceof BasicDate) {
                    BasicDate bd = (BasicDate) value;
                    if (bd.getDay() == 1 && column != 0) {
                        con.setHorizontalSpan(Math.min(3, 8 - column));
                    }
                }
                con.setWidthPercentage(column % 2 == 0 ? 12 : 13);
                int w = 100 / 9;
                switch (column) {
                    case 0:
                        w = 100 / 9 * 2;
                        break;
                    default:
                }
                con.setWidthPercentage(w);

                return con;
            }

        };
        //      this.tm = tm;
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
            }

            if (c == 0) {
                CalendarView dc = getForm().setView(CalendarViewType.MONTH_VIEW, bd);
                dc.setSelectedDate(bd);

            } else {
                CalendarView dc = getForm().setView(CalendarViewType.DAY_VIEW, bd);
                dc.setSelectedDate(bd);

            }
        }

    }

    public TableModel createWeekModel() {
        AbstractTableModel wm = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return 0;
            }

            @Override
            public int getColumnCount() {
                return getModel().getColumnCount();
            }

            @Override
            public String getColumnName(int c) {
                CalendarModel cm = (CalendarModel) getModel();
                // hussam.println("cccccccccccc: "+c);
                //hussam.println("weekTable: "+cm.getFirstDate());
                switch (c) {
                    case 0:
                        return getT(cm.getFirstDate().getCalendar().getName());
                    case 1:
                        return getT("shortSunday");
                    case 2:
                        return getT("shortMonday");
                    case 3:
                        return getT("shortTuesday");
                    case 4:
                        return getT("shortWednesday");
                    case 5:
                        return getT("shortThursday");
                    case 6:
                        return getT("shortFriday");
                    case 7:
                        return getT("shortSaturday");

                }
                return "Extra";
            }

            @Override
            public boolean isCellEditable(int arg0, int arg1) {
                return false;
            }

            @Override
            public Object getValueAt(int row, int column) {
                return getColumnName(column);
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

        return new Table(createWeekModel(), true) {
            {
                setRTL(getFormRTL());
            }

            protected Component createCell(Object value, int row, int column, boolean editable) { // (1)

                Button buttonRenderer = new Button();
                buttonRenderer.setRTL(getFormRTL());
                buttonRenderer.addActionListener(CalendarTable.this);
                if (value == null) {
                    System.err.println("strange null date: " + value + " " + row + " " + column + " ");
                    return buttonRenderer;
                }
                buttonRenderer.setText(value.toString());
                buttonRenderer.setUIID("MyCalendarDay");
                //        buttonRenderer.getAllStyles().setAlignment(Component.CENTER);
                if (row < 0) {
                    buttonRenderer.getUnselectedStyle().setBgColor(0xffffff);
                    Calendar g = CalendarFactory.getGreg();
                    g.setTime(new Date());
                    if (column == g.get(Calendar.DAY_OF_WEEK)) {
                        buttonRenderer.getUnselectedStyle().setBgColor(0xffff00);
                    }
                }
                return buttonRenderer;
            }

            protected TableLayout.Constraint createCellConstraint(Object value, int row, int column) {
                TableLayout.Constraint con = super.createCellConstraint(value, row, column);
                con.setHorizontalSpan(1);

                con.setWidthPercentage(column % 2 == 0 ? 12 : 13);
                int w = 100 / 9;
                switch (column) {
                    case 0:
                        w = 100 / 9 * 2;
                        break;
                    default:
                }
                con.setWidthPercentage(w);

                return con;
            }
        };

    }

    @Override
    protected void onScrollX(int scrollX) {
        super.onScrollX(scrollX); //To change body of generated methods, choose Tools | Templates.
        //    hussam.println("x"+scrollX);
    }

    @Override
    protected void onScrollY(int scrollY) {
        super.onScrollY(scrollY); //To change body of generated methods, choose Tools | Templates.

        Component c = getComponentAt(50, scrollY);
        if (c instanceof Table) {
            //    hussam.println("table "+getWidth()+" "+getHeight()+" s "+scrollY);
            return;
        }
        Button b = (Button) c;

        //   hussam.println("c: "+b.getText());
    }

    void addActionListener(ActionListener<ActionEvent> actionListener) {
        al = actionListener;
    }

    public CalendarModel getModel() {
        return (CalendarModel) table.getModel();
    }

    @Override
    public void setSelectedDate(BasicDate bd) {
      //  getModel().setDate(bd);
       // table.setModel(table.getModel());
        table.setModel(CalendarModel.createModel(bd, getCalendars(), getModel().getCalType()));
    }

    @Override
    public BasicDate getSelectedDate() {
        return getModel().getFirstDate();
    }

    @Override
    public void setCalendars(List<BasicCalendar> list) {
        //   hussam.println("cccccccccccccccc: +"+list);
        table.setModel(CalendarModel.createModel(getSelectedDate(), list, getModel().getCalType()));
    }

    @Override
    public String getHeader() {
        return ((getModel().getCalType() == CalendarViewType.YEAR_VIEW) ? "" : "" + getT(getSelectedDate().getCalendar().getMonthName(getSelectedDate()).getName())) + " "
                + getSelectedDate().getYear() + " "
                + getT(getSelectedDate().getCalendar().getName());

    }

    @Override
    public List<BasicCalendar> getCalendars() {
        return getModel().getCalendars();
    }

    @Override
    public void next() {
        BasicDate bd = getSelectedDate();
        switch (getModel().getCalType()) {
            case YEAR_VIEW:
                bd = bd.getCalendar().getDate(bd.getYear() + 1, 1, 1);
                break;
            case MONTH_VIEW:
                int x = bd.getCalendar().getMonthLength(bd);
                bd = bd.getCalendar().getDate(bd.getDate() + (x + 1) * DAY);
                break;
            //    case METONIC_VIEW:bd.getCalendar().getDate(bd.getYear() + 19, 1, 1);break;
        }

        setSelectedDate(bd);
    }

    @Override
    public void previous() {
        BasicDate bd = getSelectedDate();
        switch (getModel().getCalType()) {
            case YEAR_VIEW:
                bd = bd.getCalendar().getDate(bd.getYear() - 1, 1, 1);
                break;
            case MONTH_VIEW:
                int x = bd.getCalendar().getMonthLength(bd);
                bd = bd.getCalendar().getDate(bd.getDate() - (x - 1) * DAY);
            //    case METONIC_VIEW:bd.getCalendar().getDate(bd.getYear() - 19, 1, 1);break;
        }
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
        if (getModel().getCalType() == CalendarViewType.MONTH_VIEW) {
            return "M1";
        }
        return "Y1";
    }

}
/*
abstract class TableWiThHeader2 extends Table implements CalendarController, ComponentWithHeader {

    List<DateControllerListener> list = new ArrayList<>();

    public TableWiThHeader2(TableModel model, boolean head) {
        super(model, head);
    }

    
    @Override
    public void addControllerListiner(DateControllerListener d) {
        list.add(d);
    }

    @Override
    public void removeControllerListiner(DateControllerListener d) {
        list.remove(d);

    }

    protected void fireTabelUpdated() {
        for (DateControllerListener d : list) {
            d.dateUpdated(this, getSelectedDate());
        }
    }

}
//*/
