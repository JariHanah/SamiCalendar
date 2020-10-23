/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import static com.alhanah.samicalapp.MainForm.getFormRTL;
import static com.alhanah.samicalapp.SamiApplication.getStore;
import static com.alhanah.samicalapp.SamiApplication.getT;
import com.codename1.components.MultiButton;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Label;
import java.util.Date;
import nasiiCalendar.BasicCalendar;
import static nasiiCalendar.BasicCalendar.DAY;
import nasiiCalendar.BasicDate;
import nasiiCalendar.CalendarFactory;
import nasiiCalendar.JalaliCalendarIR;
import nasiiCalendar.locationBasid.LunerLocationCalendar;
import nasiiCalendar.PeriodType;
import nasiiCalendar.Months;
import nasiiCalendar.SamiCalendar;
import nasiiCalendar.SeasonIdentifier;
import nasiiCalendar.Solar128Calendar;

/**
 *
 * @author Hussam
 */
public class CellEditor {

    public static final String DAY_UIID = "MyCalendarDay";
    public static final String DATE_VIEW_UIID = "MyDateViewCell";
    public static final Font largeFont = Font.createSystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_LARGE);
    public static final Font mediumFont = Font.createSystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    public static final Font smallFont = Font.createSystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    public static final Font urlFont = Font.createSystemFont(Font.FACE_MONOSPACE, Font.STYLE_UNDERLINED, Font.SIZE_MEDIUM);
    FontImage fi;
    FontImage show, hide;
    static String HEADER="Heading";
    private static String LABEL_TRANS="LabelTrans";
    public CellEditor() {
        show = FontImage.createMaterial(FontImage.MATERIAL_ADD, getBasicButton().getAllStyles());
        hide = FontImage.createMaterial(FontImage.MATERIAL_REMOVE, getBasicButton().getAllStyles());

    }

    public FontImage getAddImage() {
        return show;
    }

    public FontImage getRemoveImage() {
        return hide;
    }

    public static MultiButton getBasicMultiButton() {
        MultiButton buttonRenderer = new MultiButton();
        buttonRenderer.setRTL(getFormRTL());
        buttonRenderer.setUIID(DAY_UIID);
        buttonRenderer.getUnselectedStyle().setBgTransparency(180);
        return buttonRenderer;
    }

    public static Button getBasicButton() {
        Button buttonRenderer = new Button();
        buttonRenderer.setRTL(getFormRTL());
        buttonRenderer.setUIID(DAY_UIID);
        buttonRenderer.getUnselectedStyle().setBgTransparency(220);
        return buttonRenderer;
    }

    protected void prepareReligious(Button buttonRenderer, BasicDate bd) {

        PeriodType m = bd.getCalendar().getMonthName(bd);
        if (m.equals(Months.RAMADHAN)) {
            buttonRenderer.getUnselectedStyle().setBgColor(0xffccee);
            buttonRenderer.getUnselectedStyle().setFgColor(0x000000);
        }
        if (m.equals(Months.NASEI) || m.equals(Months.ADAR2)) {
            buttonRenderer.getUnselectedStyle().setBgColor(0xff8080);
        }
        if (m.equals(Months.ALHARAM) | m.equals(Months.RAJAB_MODHAR) | m.equals(Months.RAJAB_RABEEIAH)) {
            buttonRenderer.getUnselectedStyle().setBgColor(0xb30000);
            buttonRenderer.getUnselectedStyle().setFgColor(0xffffff);
        }

        if (m.equals(Months.RAMADHAN) & bd.getDay() == 10 & bd.getCalendar() instanceof SamiCalendar) {
            buttonRenderer.getUnselectedStyle().setBgColor(0x000000);
            buttonRenderer.getUnselectedStyle().setFgColor(0xffffff);
        }
        if ((m.equals(Months.SAFAR) | m.equals(Months.SAFAR2) | m.equals(Months.RABEI1) | m.equals(Months.RABEI2)) & bd.getCalendar() instanceof SamiCalendar) {
            //   buttonRenderer.getUnselectedStyle().setBgColor(0xff6666);
            //   buttonRenderer.getUnselectedStyle().getBorder().setPaintOuterBorderFirst(true);
            //    buttonRenderer.getUnselectedStyle().getBorder().setThickness(0);

            //     buttonRenderer.getUnselectedStyle().setFgColor(0xffffff);
        }
        if ((m.equals(Months.MUHARRAM) || m.equals(Months.TISHREI)) & bd.getDay() == 10) {
            buttonRenderer.getUnselectedStyle().setBgColor(0x000000);
            buttonRenderer.getUnselectedStyle().setFgColor(0xffffff);
        }

    }
    public int getSeasonJalali(BasicDate bd2) {
        JalaliCalendarIR jc = (JalaliCalendarIR) getStore().getCalendar(BasicCalendar.JALALI_IR_ID);
        BasicDate bd = jc.getDate(bd2.getDate());
        if (bd.equals(bd.getCalendar().getMaximumDate())) {
            return 0;
        }

        BasicDate s1 = jc.getDate(bd.getYear(), 1, 1);
        BasicDate sum1 = jc.getDate(bd.getYear(), 4, 1);
        BasicDate f1 = jc.getDate(bd.getYear(), 7, 1);
        BasicDate w2 = jc.getDate(bd.getYear(), 10, 1);
        // hussam.println(bd+" "+w1+" "+s1+" testing : "+(bd.getDate()>=w1.getDate())+" testing: "+(bd.getDate()<=s1.getDate()));
        if (bd.getDate() >= s1.getDate() && bd.getDate() < sum1.getDate()) {
            return 2;
            //          hussam.println("blue: "+bd);
        }
        if (bd.getDate() >= sum1.getDate() && bd.getDate() < f1.getDate()) {
            //c.setUIID("SpringLabel");
            return 3;
            //           hussam.println("green: "+bd);
        }
        if (bd.getDate() >= f1.getDate() && bd.getDate() < w2.getDate()) {
            return 4;
        }
        if (bd.getDate() >= w2.getDate()) {
            return 1;
        }
        
        return 0;
    }
    public int getSeasons128(BasicDate bd2) {
        Solar128Calendar sc = (Solar128Calendar) getStore().getCalendar(BasicCalendar.SOLAR_128_ID);
        BasicDate bd = sc.getDate(bd2.getDate());
        if (bd.equals(bd.getCalendar().getMaximumDate())) {
            return 0;
        }

        BasicDate w1 = sc.getDate(bd.getYear(), 1, 1);
        BasicDate s1 = sc.getDate(bd.getYear(), 3, 21);
        BasicDate sum1 = sc.getDate(bd.getYear(), 6, 20);
        BasicDate f1 = sc.getDate(bd.getYear(), 9, 22);
        BasicDate w2 = sc.getDate(bd.getYear(), 12, 21);
        BasicDate w3 = sc.getDate(bd.getYear(), 12, 31);
        // hussam.println(bd+" "+w1+" "+s1+" testing : "+(bd.getDate()>=w1.getDate())+" testing: "+(bd.getDate()<=s1.getDate()));
        if (bd.getDate() >= w1.getDate() && bd.getDate() < s1.getDate()) {
            return 1;
            //          hussam.println("blue: "+bd);
        }
        if (bd.getDate() >= s1.getDate() && bd.getDate() < sum1.getDate()) {
            //c.setUIID("SpringLabel");
            return 2;
            //           hussam.println("green: "+bd);
        }
        if (bd.getDate() >= sum1.getDate() && bd.getDate() < f1.getDate()) {
            return 3;
        }
        if (bd.getDate() >= f1.getDate() && bd.getDate() < w2.getDate()) {
            return 4;
        }
        if (bd.getDate() >= w2.getDate() && bd.getDate() < w3.getDate()) {
            return 1;

        }
        return 0;
    }

    protected void prepareSeasons(Component c, BasicDate bd2, SeasonIdentifier si) {
        if ((bd2.getCalendar() instanceof LunerLocationCalendar)) {
            return;
        }

        if (bd2.equals(bd2.getCalendar().getMaximumDate())) {
            return;
        }
        SeasonIdentifier.Season season = si.getSeason(bd2);
        c.getUnselectedStyle().setFgColor(0x000000);

        switch (season) {
            case WINTER:
                c.getUnselectedStyle().setBgColor(0x99ccff);
                break;
            case SPRING:
                c.getUnselectedStyle().setBgColor(0x99ffcc, true);
                break;
            case SUMMER:
                c.getUnselectedStyle().setBgColor(0xff8080);
                break;
            case FALL:
                c.getUnselectedStyle().setBgColor(0xffccb3);
                break;
        }

    }

    protected void prepareToday(Button c, BasicDate cellDate) {
        if (cellDate.getCalendar() instanceof LunerLocationCalendar) {
            return;
        }

        //if(true)return;
        //    strikeThrough(c, cellDate);
        if (cellDate.equals(cellDate.getCalendar().getMaximumDate())) {
            return;
        }

        BasicDate today = cellDate.getCalendar().getDate(new Date().getTime());
        if (today.equals(cellDate)) {
            c.getUnselectedStyle().setBgColor(0xffff00);
            c.getUnselectedStyle().setFgColor(0x000000);
            //    return;
        }

    }

    protected void prepareTodayMonth(Button c, BasicDate cellDate) {
        if (cellDate.getCalendar() instanceof LunerLocationCalendar) {
            return;
        }

        if (cellDate.equals(cellDate.getCalendar().getMaximumDate())) {
            return;
        }
        BasicDate today = cellDate.getCalendar().getDate(new Date().getTime());
        BasicDate s1 = cellDate.getCalendar().getDate(cellDate.getYear(), cellDate.getMonth(), 1);
        BasicDate s2 = cellDate.getCalendar().getDate(cellDate.getYear(), cellDate.getMonth(), cellDate.getCalendar().getMonthLength(cellDate));

        if (today.getDate() >= s1.getDate() && today.getDate() <= s2.getDate()) {
            c.getUnselectedStyle().setBgColor(0xffff00);
            c.getUnselectedStyle().setFgColor(0x000000);
        }
        //   hussam.println("YES: "+today+" "+cellDate);

    }

    protected void prepareTodayYear(Button c, BasicDate cellDate) {
        if (cellDate.getCalendar() instanceof LunerLocationCalendar) {
            return;
        }

        if (cellDate.equals(cellDate.getCalendar().getMaximumDate())) {
            return;
        }
        BasicDate today = cellDate.getCalendar().getDate(new Date().getTime());
        BasicDate s1 = cellDate.getCalendar().getDate(cellDate.getYear(), 1, 1);
        BasicDate s2 = cellDate.getCalendar().getDate(cellDate.getYear() + 1, 1, 1);

        if (today.getDate() >= s1.getDate() && today.getDate() <= s2.getDate()) {
            c.getUnselectedStyle().setBgColor(0xffff00);
            c.getUnselectedStyle().setFgColor(0x000000);

        }
        //   hussam.println("YES: "+today+" "+cellDate);

    }

    protected void strikeThrough(Button labelDate, BasicDate selectedDate) {
        if ((selectedDate.getCalendar() instanceof LunerLocationCalendar)) {
            return;
        }
        if (selectedDate.equals(selectedDate.getCalendar().getMaximumDate())) {
            labelDate.setText("buyCell");
            if (fi == null) {
                fi = FontImage.createMaterial(FontImage.MATERIAL_EXPLORE, labelDate.getAllStyles());
            }

            labelDate.setIcon(fi);
            //   labelDate.getAllStyles().setStrikeThru(true);
            labelDate.getAllStyles().setAlignment(Component.RIGHT);
            //   labelDate.getAllStyles().setBgColor(0x000050);
            return;
        }
    }

    protected Component prepareHeading(Component c) {
        c.setUIID(HEADER);

        c.getAllStyles().setFont(largeFont);
        c.getAllStyles().setFgColor(0xffffff);
        return c;
    }

    protected void prepareAll(Button labelDate, BasicDate selectedDate) {
        if ((selectedDate.getCalendar() instanceof LunerLocationCalendar)) {
            return;
        }

        if (selectedDate.equals(selectedDate.getCalendar().getMaximumDate())) {

            return;
        }
        prepareSeasons(labelDate, selectedDate, CalendarFactory.getDefaultSeasonIdentifier());
        prepareReligious(labelDate, selectedDate);
        prepareToday(labelDate, selectedDate);

    }

    void prepareYearType(Button c, BasicDate cellDate) {
        if ((cellDate.getCalendar() instanceof LunerLocationCalendar)) {
            return;
        }

        if (cellDate.equals(cellDate.getCalendar().getMaximumDate())) {
            return;
        }
        if (cellDate.getCalendar().isLeapYear(cellDate)) {
            c.getUnselectedStyle().setBgColor(0xb30000);
            c.getUnselectedStyle().setFgColor(0xffffff);
        }

    }

    public Font getSmallFont() {
        return smallFont;
    }

    public Font getMediumFont() {
        return mediumFont;
    }

    void setURL(Button buttonRenderer) {
        buttonRenderer.getAllStyles().setFont(urlFont);
        buttonRenderer.getAllStyles().setFgColor(0x0099ff);
    }

    void setDateInfo(MultiButton buttonRenderer, BasicDate bd, BasicCalendar get) {
        BasicDate result = get.getDate(bd.getDate());
        buttonRenderer.setTextLine1(getT(result.getCalendar().getMonthName(result).getName()));
        buttonRenderer.setTextLine2(result.getDay() + " " + result.getYear());
        buttonRenderer.setTextLine3(getT(get.getName()));

    }

    void setDayDifference(MultiButton buttonRenderer, BasicDate selectedDate) {
        int x = (int) ((selectedDate.getDate() - CalendarFactory.cleanDate(new Date().getTime())) / DAY);
        buttonRenderer.setTextLine1("" + x);
        buttonRenderer.setTextLine2(getT("daydiff"));
    }
    final static String UIID_SEASON = "seasonLabel";

    void setSeason(Label buttonRenderer, BasicDate selectedDate, SeasonIdentifier si) {
        buttonRenderer.setUIID(DATE_VIEW_UIID);
        
        prepareSeasons(buttonRenderer, selectedDate, si);
        SeasonIdentifier.Season season=si.getSeason(selectedDate);
        String x = season.getName();
        
        buttonRenderer.getAllStyles().setFgColor(0x000000);
        buttonRenderer.getAllStyles().setFont(largeFont);
        buttonRenderer.setText(x);
    }

    void setNewMoonCycle(MultiButton buttonRenderer, BasicDate bd, LunerLocationCalendar llc) {
        if (llc == null) {
            buttonRenderer.setTextLine1(getT("missingLocation"));
            buttonRenderer.setTextLine2(getT("missingLocationExtra"));
            return;
        }
        BasicDate result = llc.getDate(bd.getDate());
        buttonRenderer.setTextLine1(result.getDay() + "");

        buttonRenderer.setTextLine2(getT(llc.getName()));// + " " + getT(result.getCalendar().getMonthName(result).getName()));//getT(result.getCalendar().getMonthName(result).getName()) + " " + );
        //    buttonRenderer.setTextLine3(DAY_UIID);
    }

    void setBlackMoonCycle(MultiButton buttonRenderer, BasicDate bd) {
        BasicCalendar cal = getStore().getCalendar(BasicCalendar.BLACK_MOON_ID);
        BasicDate result = cal.getDate(bd.getDate());
        buttonRenderer.setTextLine1(result.getDay() + "");

        buttonRenderer.setTextLine2(getT(Months.BLACK_MOON.getName()));// + getT(result.getCalendar().getMonthName(result).getShortName()));//getT(result.getCalendar().getMonthName(result).getName()) + " " + );
    }

    Font getLargeFont() {
        return largeFont;
    }
    
    Label getBasicLabel(){
        Label l=new Label();
        l.setUIID(LABEL_TRANS);
        return l;
    }
    Label getBigLabel() {
        Label l = getBasicLabel();
        l.getAllStyles().setFont(largeFont);
        return l;
    }

    Label getMediumLabel() {
        Label l = getBasicLabel();
        l.getAllStyles().setFont(mediumFont);
        return l;
    }

    Label getSmallLabel() {
        Label l = getBasicLabel();
        l.getAllStyles().setFont(smallFont);
        return l;
    }
}
