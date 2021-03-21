/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alhanah.samicalapp;

import nasiiCalendar.locationBasid.City;
import com.codename1.location.Location;
import com.codename1.location.LocationManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import nasiiCalendar.AdCalendar;
import nasiiCalendar.BasicCalendar;
import nasiiCalendar.BasicDate;
import nasiiCalendar.BasicYear;
import nasiiCalendar.ByroniCalendar;
import nasiiCalendar.CalendarFactory;
import nasiiCalendar.GregoryCalendar;
import nasiiCalendar.HebrewCalendar;
import nasiiCalendar.JalaliCalendarIR;
import nasiiCalendar.JulianCalendar;
import nasiiCalendar.locationBasid.LunerLocationCalendar;
import nasiiCalendar.MyBasicCalendar;
import nasiiCalendar.Omari30YearLoop;
import nasiiCalendar.OmariCalendar;
import nasiiCalendar.PeriodStore;
import nasiiCalendar.PeriodType;
import nasiiCalendar.QazwiniCalendar;
import nasiiCalendar.SamiCalendar;
import nasiiCalendar.SamiFixed;
import nasiiCalendar.Solar128Calendar;
import nasiiCalendar.SolarStationsCalendar;
import nasiiCalendar.WsmiCalendar;
import nasiiCalendar.Zodiac13Calendar;
import static nasiiCalendar.BasicCalendar.*;
import nasiiCalendar.locationBasid.BlackFajrStandard;
import nasiiCalendar.locationBasid.GenericLunerCalendar;
import nasiiCalendar.locationBasid.LunerIdentifier;
import nasiiCalendar.locationBasid.UmAlquraStandard;
import org.shredzone.commons.suncalc.MoonPhase;
import org.shredzone.commons.suncalc.MoonTimes;
import org.shredzone.commons.suncalc.SunTimes;

/**
 *
 * @author DEll
 */
public class ATest {

    static SamiFixed sc = CalendarFactory.getSamiCalendar();
    static OmariCalendar oc = CalendarFactory.getOmariCalendar();
    static JulianCalendar jc = CalendarFactory.getJulianCalendar();
    static GregoryCalendar gc = CalendarFactory.getGregoryCalendar();
    static WsmiCalendar wc = CalendarFactory.getWsmiCalendar();
    static HebrewCalendar hc = CalendarFactory.getHebrewCalendar();
    static ByroniCalendar bc = CalendarFactory.getByroniCalendar();
    static QazwiniCalendar qc = CalendarFactory.getQazwiniCalendar();
    static AdCalendar ac = CalendarFactory.getAdCalendar();
    static JalaliCalendarIR jal = CalendarFactory.getJalaliIR();

    public static void testLunerCycleLength() {
        Location location = LocationManager.getLocationManager().getLastKnownLocation();
        long time = new Date().getTime();
        MoonTimes mt = MoonTimes.compute().at(location.getLatitude(), location.getLongitude()).on(new Date(time)).execute();
        long big = Long.MIN_VALUE;
        for (int i = 0; i < 100; i++) {

            MoonPhase mp = MoonPhase.compute().on(new Date(time)).execute();
            long hilal = CalendarFactory.cleanDate(mp.getTime().getTime());
            System.err.println("Length: " + (hilal / DAY - big / DAY) + " " + (hilal - big) / DAY + " " + ((hilal / DAY - big / DAY) == (hilal - big) / DAY));
            big = hilal;
            time = time + MONTH;
        }

    }

    public static void testLunerLocationCalendar() {
        //    testLunerCycleLength();
        //   System.exit(0);
        System.err.println("");
        LunerLocationCalendar lc = null;//CalendarFactory.getLunerLocationCalendar();
        long today = new Date().getTime();

        long test1 = 1618174799999L;
        BasicDate b0 = lc.getDate(test1 - DAY);
        BasicDate b1 = lc.getDate(test1);
        BasicDate b2 = lc.getDate(test1 + DAY);
        BasicDate b3 = lc.getDate(test1 + 2 * DAY);
        System.err.println("b0: " + b0);
        System.err.println("b1: " + b1);
        System.err.println("b2: " + b2);
        System.err.println("b3: " + b3);

        System.err.println("Starting......................");
        for (int i = 0; i < 10000; i++) {
            long t1 = today + DAY * i;
            //   System.err.println("\ttesting for: "+i+"{"+new Date(t1)+"}");
            BasicDate bd1 = lc.getDate(t1);
            BasicDate bd2 = bd1.getCalendar().getDate(bd1.getYear(), bd1.getMonth(), bd1.getDay());
            BasicDate bd3 = bd2.getCalendar().getDate(bd2.getDate());
            System.err.println("b1: " + print(bd1) + " b2" + print(bd2) + " b3:" + print(bd3) + " Dif: " + (bd1.getDay() - bd3.getDay()) + "    " + (bd1.getDate() - bd2.getDate()) + "  " + (t1 - bd1.getDate()) + " " + new Date(bd1.getDate()));
            //     System.err.println("ML: "+lc.getMonthLength(bd1)+" CYCLCE:" + bd1 + " \n\t   " + sc.getDate(t1)+" \n\t" + gc.getDate(t1)+"\nt1
            CalendarFactory.test(bd1);
        }

    }

    static String print(BasicDate b) {
        return b.getYear() + " " + b.getDay();
    }

    public static void test(long time) {
        SamiFixed sc = CalendarFactory.getSamiCalendar();
        BasicDate bd = sc.getDate(time);
        CalendarFactory.test(bd);
        System.out.println("bd: " + bd);

        GregoryCalendar g = CalendarFactory.getGregoryCalendar();
        BasicDate bGreg = g.getDate(time);
        CalendarFactory.test(bGreg);
        System.out.println("bGreg: " + bGreg);

        JulianCalendar j = CalendarFactory.getJulianCalendar();
        BasicDate bJul = j.getDate(time);
        CalendarFactory.test(bJul);
        System.out.println("bJul: " + bJul);

        OmariCalendar o = CalendarFactory.getOmariCalendar();
        BasicDate bOmar = o.getDate(time);
        System.out.println("bOma: " + bOmar);
        CalendarFactory.test(bOmar);

        Solar128Calendar s128c = CalendarFactory.getSolar128Calendar();
        BasicDate s128 = s128c.getDate(time);
        System.out.println("128: " + s128);
        CalendarFactory.test(s128);

        SolarStationsCalendar lc = CalendarFactory.getLunerStationCalendar();
        BasicDate ld = lc.getDate(time);
        System.out.println("LunerS: " + ld);
        CalendarFactory.test(ld);

        Zodiac13Calendar zc = CalendarFactory.getZodiac13Calendar();
        BasicDate zd = zc.getDate(time);
        System.out.println("ZodiacS: " + zd);
        CalendarFactory.test(zd);

        BasicDate g10 = g.getDate(1900, 2, 1);
        BasicDate j10 = j.getDate(g10.getDate());
        System.err.println("g10:+ " + g10 + " j10: " + j10);
        g10 = g.getDate(1582, 10, 15);
        j10 = j.getDate(g10.getDate());
        System.err.println("g10:+ " + g10 + " j10: " + j10);
        g10 = g.getDate(1000, 1, 1);
        j10 = j.getDate(g10.getDate());
        System.err.println("g10:+ " + g10 + " j10: " + j10);
        System.err.println("-------------------");
        // BasicDate d1 = getDate(g, j, 1900, 2, 1);
        long y1 = -2207963212001L;
        long y2 = -2207876812001L;
        System.err.println("y difff: " + (y2 - y1) + "days: " + ((y2 - y1) / DAY) + " normalday is: " + DAY);

        int m = 1;
        for (m = 1; m < 2; m++) {
            for (int i = 13; i < 15; i++) {
                BasicDate d1 = getDate(g, j, 1900, m, i);
            }
        }

        System.out.println("end test");
        //    System.exit(0);
    }

    public static BasicDate getDate(BasicCalendar from, BasicCalendar to, int y, int m, int d) {
        BasicDate bd = from.getDate(y, m, d);
        BasicDate tobd = to.getDate(bd.getDate());
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date(bd.getDate()));
        System.err.println("from: " + bd + " DOW:" + c.get(java.util.Calendar.DAY_OF_WEEK) + " Date: " + c.getTime() + " to: " + tobd);
        CalendarFactory.test(bd);
        CalendarFactory.test(tobd);
        return bd;
    }

    public static void testYearLength() {
        Set<Integer> set1 = new HashSet<Integer>();
        Set<Integer> set2 = new HashSet<Integer>();
        List<Integer> monthLengths = new ArrayList<>();
        MyBasicCalendar sm = CalendarFactory.getSamiCalendar();
        System.err.println("Set: " + set1);
        for (int i = 0; i < 20000; i++) {
            monthLengths = new ArrayList<>();

            BasicDate bd = sm.getDate(i, 1, 1);
            int length = bd.getCalendar().getYearLength(bd);
            //    System.err.println("\t"+i+" "+length);
            set1.add(length);
            int total = 0;
            for (int o = 0; o < bd.getCalendar().getYearType(bd).getSubPeriods().size(); o++) {
                BasicDate bd2 = sm.getDate(bd.getYear(), o + 1, 1);
                int monthLength = sm.getMonthLength(bd2);
                monthLengths.add(monthLength);
                //    System.err.println("\t\tMonth: " + (o + 1) + " length: " + monthLength);
                total += monthLength;
            }
            set2.add(total);
            //   if(total!=length)
            //      System.err.println(i+" Total: " + length+" Total Month: "+total+" ?:"+monthLengths);
        }
        List<Integer> list = new ArrayList<>(set1);
        Collections.sort(list);
        System.err.println("Set: " + list);

        list.clear();
        list.addAll(set2);
        Collections.sort(list);
        System.err.println("Set: " + list);

        System.err.println("sami start: " + new Date(-45881953199752L));
        System.err.println("sami start: " + new Date(START_SAMI));
        System.err.println("sami start: " + new Date(CalendarFactory.cleanDate(-45881953200001L + DAY)).getTime());
        //    System.err.println("sami start: "+new Date(CalendarFactory.cleanDate(-45881953199752L-DAY)).getTime());     

        //  System.err.println("382: "+l382);
        //   System.exit(0);
    }

    public static void printJAVAStartEndDates() {
        System.err.println("START JAV: " + new Date(Long.MIN_VALUE));
        System.err.println("END   JAV: " + new Date(Long.MAX_VALUE));
        //    System.err.println("BOT:     : " + getDate(-200000000, 1, 1) + " t:" + getDate(-200000000, 1, 1).getTime());
        //  System.err.println("EOT:     : " + getDate(200000000, 1, 1) + " t:" + getDate(200000000, 1, 1).getTime());

    }

    public static Date getDate2(int year, int m, int d) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(java.util.Calendar.YEAR, year);
        c.set(java.util.Calendar.MONTH, m);
        c.set(java.util.Calendar.DATE, d);
        return c.getTime();

    }

    public static void testAdjustedDate() {
        MyBasicCalendar c = CalendarFactory.getSamiCalendar();
        MyBasicCalendar c1 = CalendarFactory.getGregoryCalendar();

        for (int i = 1; i < 40; i++) {
            BasicDate bd = c.getDate(1479, 7, i);
            //    System.err.println("Adjusted: "+bd+ " --- "+c1.getDate(bd.getDate()));
        }

    }

    public static void testMovements() {

        System.err.println("");
        BasicCalendar b = CalendarFactory.getOmariCalendar();

        BasicDate old = b.getDate(2000, 1, 1);
        for (int y = 1; y < 40; y++) {
            BasicDate bd = b.getDate(1479, 8, y);
            bd = b.getDate(bd.getDate());
            long t = bd.getDate();
            System.err.println(new Date(t) + " bd:" + bd + " " + bd.getCalendar().getMonthLength(bd));
            //      System.err.println("");
            if (bd.equals(old)) {
                System.err.println("WOOOOOOOOOOOOW");
            }
            old = bd;
        }

        //  System.exit(0);
    }

    public static void testNewMoon() {
        City city = SamiApplication.getBasicLocation();
        long newMoon = 0;
        TimeZone zone = TimeZone.getDefault();
        double total = 0;
        double count = 0;
        long time = new Date().getTime();
        for (int i = 0; i < 3000; i++) {
            MoonTimes m = MoonTimes.compute().timezone(zone).on(new Date(time)).midnight().at(city.getLat(), city.getLon()).execute();
            SunTimes s = SunTimes.compute().timezone(zone).on(new Date(time)).midnight().at(city.getLat(), city.getLon()).execute();
            if (m.getSet() == null | s.getSet() == null) {
                //    System.err.println("\tNULL Sun:"+s.getSet()+"\tMoon:"+m.getSet());
                time += DAY;
                continue;
            }
            long result = m.getSet().getTime() - s.getSet().getTime();
            String x = "";
            if (result < MINUTE * 120 & result > 0 * MINUTE & (time - newMoon) / DAY > 20) {
                int days = (int) ((time - newMoon) / DAY);
                if (days > 30 | days < 29) {
                    System.err.println("problem");
                }
                x = "\t" + days + "\t";
                newMoon = time;
                System.err.println(x + "Moon:" + m.getSet() + "\tSun:" + s.getSet() + "\tminutes:" + result / MINUTE);
                if (days < 35) {
                    count++;
                    total += days;
                }
            }
            time += DAY;

        }
        System.err.println("average: " + (total / count));

        MoonPhase mp = MoonPhase.compute().on(new Date()).midnight().execute();
        System.err.println("new moon: " + new Date(getNewMoon(mp.getTime().getTime())));

        System.exit(0);
    }

    public static long getNewMoon(long blackMoon) {
        City city = SamiApplication.getBasicLocation();
        long time = blackMoon;
        TimeZone zone = TimeZone.getDefault();
        while (true) {
            MoonTimes m = MoonTimes.compute().timezone(zone).on(new Date(time)).midnight().at(city.getLat(), city.getLon()).execute();
            SunTimes s = SunTimes.compute().timezone(zone).on(new Date(time)).midnight().at(city.getLat(), city.getLon()).execute();
            long result = m.getSet().getTime() - s.getSet().getTime();
            if (result < MINUTE * 120 & result > 0 * MINUTE) {
                return m.getSet().getTime();

            }

            time += DAY;

        }
        //return blackMoon;
    }

    public static void testJalali() {
        BasicDate b = gc.getDate(2025, 3, 24);
        //    System.err.println(b);
        b = jal.getDate(b.getDate());
        //System.err.println("\n"+b);
        b = jal.getDate(b.getDate() - DAY);
        System.err.println(b);
        b = jal.getDate(b.getDate() - DAY);
        System.err.println(b);
        b = jal.getDate(b.getDate() - DAY);
        System.err.println(b);
        b = jal.getDate(b.getDate() - DAY);
        System.err.println(b);
        b = jal.getDate(b.getDate() - DAY);
        System.err.println(b);

        //    System.err.println(b.getCalendar().getDate(b.getDate()+DAY));
        //  System.err.println(b.getCalendar().getDate(b.getDate()+DAY));
        //System.err.println(b.getCalendar().getDate(b.getDate()+DAY));
        //     System.err.println(jal.getDate(1404,1,1));
        //      System.err.println(jal.getDate(1404,1,2));
        //      System.exit(0);
        for (int i = 475; i < 3000; i++) {
            System.err.println(i + " \t" + JalaliCalendarIR.isLeap(i));
        }

        //  //    BasicDate g=gc.getDate(2020, 3, 20);
        //     System.err.println(g+"\t"+jal.getDate(g.getDate()));
        BasicDate b3 = jal.getDate(1398, 12, 29);
        System.err.println("b3: " + b3);
        b3 = jal.getDate(1398, 12, 30);
        System.err.println("b3: " + b3);
        b3 = jal.getDate(1399, 1, 1);
        System.err.println("b3: " + b3);
        b3 = jal.getDate(1399, 1, 2);
        System.err.println("b3: " + b3);
        //   System.exit(0);
        for (int o = 0; o < 5; o++) {
            System.err.println("");
            for (int i = 0; i < 5; i++) {
                BasicDate b0 = jal.getDate(1398 + o, 12, 29 + i);
                BasicDate b1 = jal.getDate(1398 + o + 1, 1, 1 + i);

                System.err.println(b0.getCalendar().isLeapYear(b0) + " b: " + b0);
                // System.err.println(b1.getCalendar().isLeapYear(b1)+" \tb: "+b1);

            }
        }
        //System.exit(0);
    }
    // static long MONTH=(long)(DAY*2.5);

    public static int getMonthLength(BasicDate bd) {
        long start = 0;//OmariCalendar.STARTLONG;
        long dur = bd.getDate() - start;//CalendarFactory.cleanDate(bd.getDate()) - start;
        long m = (int) (dur / MONTH);
        long durFloor = m * MONTH;
        long durCieling = (m + 1) * MONTH;
        int result = (int) ((durCieling / DAY - durFloor / DAY));
        return result;

    }

    public static BasicDate getDate(int y, int m, int d) {
        BasicDate b = calculateDate(y, 1, 1);
        int ymc = b.getCalendar().getYearType(b).getSubPeriods().size();
        if (ymc < m) {
            return getDate(y + 1, m - ymc, d);
        }
        b = calculateDate(y, m, 1);
        int mdc = getMonthLength(b);
        // System.err.println("mdc: "+mdc+"<?"+d);
        if (mdc < d) {
            //   System.err.println(mdc+" "+y+" "+m+" "+d+"\tMonthCorrection: "+y+" "+(m+1)+" "+(d-mdc));
            return getDate(y, m + 1, d - mdc);
        } else {
            return calculateDate(y, m, d);
        }
    }

    public static BasicDate calculateDate(int year1, int month, int day) {
        month--;
        day--;
        int year = year1 - OmariCalendar.STARTDATE;
        long time = year * MONTH * 12 + month * MONTH + DAY * day;// + OmariCalendar.STARTLONG;
        time = CalendarFactory.cleanDate(time);
//        MyDate sd = new MyDate(time, ++day, ++month, year1, oc);
        return null;
    }

    public static void test16() {
        //System.err.println(jc.getDate(622, 7, 16));
        Omari30YearLoop o = new Omari30YearLoop();
        //System.err.println(o.isLeapYear(1441));
        //System.err.println(o.getDate(1441, 12, 30));
        System.exit(0);
        BasicDate p = o.getDate(1456, 12, 29);
        for (int y = 1; y < 360 * 100; y++) {
            BasicDate b = o.getDate(1, 1, y);
            if (Math.abs(p.getDate() - b.getDate()) > DAY) {
                System.err.println("missed Day\n\t" + "" + p + "\n\t" + b);
            } else if (getC(b.getDate()).get(Calendar.DATE) == getC(p.getDate()).get(Calendar.DATE)) {
                System.err.println("Same Day" + "\n\t" + p + "\n\t" + b);
            } else {
                //System.err.println("");
            }
            //  System.err.println(b+" "+b.getCalendar().isLeapYear(b));
            p = b;
        }
        System.err.println("");

        for (int i = 0; i < 90; i++) {
            BasicDate b1 = o.getDate(i, 1, 1);
            System.err.println(i + " " + o.isLeapYear(b1));
        }
        BasicDate b = o.getDate(1442, 2, 14);

        System.err.println(b + " " + b.getCalendar().isLeapYear(b));
        System.err.println("Today: " + o.getDate(new Date().getTime()));

        System.exit(0);
    }

    public static Calendar getC(long t) {
        Calendar c = CalendarFactory.getGreg();
        c.setTime(new Date(t));
        return c;
    }

    public static void testthis() {
        for (int i = 0; i < 100; i++) {
            BasicDate b = getDate(1443, 3, i);
            System.err.println(b + " Length:" + getMonthLength(b));
        }

        //System.err.println(b+" "+getMonthLength(b));
        System.exit(0);
    }

    public static void testOmari() {
        System.err.println(gc.getDate(2020, 8, 20));
        BasicDate b = oc.getDate(1442, 1, 1);
        int i = 29;
        b = oc.getDate(1442, 1, i);
        System.err.println(b + " " + b.getCalendar().getMonthLength(b));
        b = oc.getDate(1442, 1, ++i);
        System.err.println(b + " " + b.getCalendar().getMonthLength(b));
        b = oc.getDate(1442, 1, ++i);
        System.err.println(b + " " + b.getCalendar().getMonthLength(b));

        System.err.println("");
        b = oc.getDate(1443, 1, i = 29);
        System.err.println(b + " " + b.getCalendar().getMonthLength(b));
        b = oc.getDate(1443, 1, ++i);
        System.err.println(b + " " + b.getCalendar().getMonthLength(b));
        b = oc.getDate(1443, 1, ++i);
        System.err.println(b + " " + b.getCalendar().getMonthLength(b));

        System.exit(0);
    }

    public static void printType(PeriodType p, String m) {
        System.err.println(m + p.getTotalLengthInDays());
        if (p instanceof PeriodStore) {
            PeriodStore ps = (PeriodStore) p;
            int x = 0;
            for (PeriodType t : ps.getSubPeriods()) {
                if (t instanceof BasicYear) {
                    x += t.getTotalLengthInDays();
                    //System.err.println(m+t.getTotalLengthInDays()+"\t"+x++);
                } else {
                    printType(t, m + "\t");

                }

            }
            System.err.println(m + "\t" + x);
        }
    }

    public static void testSamiFixed() {
        SamiCalendar s = (SamiCalendar) CalendarFactory.getInstance().getCalendar(BasicCalendar.SAMI_ID);
        SamiFixed f = (SamiFixed) CalendarFactory.getInstance().getCalendar(BasicCalendar.SAMI_FIXED_ID);
        //  printType(f.store, "\t");
        System.exit(0);
        //  BasicDate b = f.getDate(1399, 9, 20);
        //    System.err.println("b: " + b);
        //   System.err.println(f.getS.getSubPeriods().get(0).getSubPeriods());
        double totalDays = 0;
        double totalDays2 = 0;
        for (int i = 1; i < 353 * 1000; i++) {

            BasicDate b = f.getDate(-105, i, 1);
            BasicDate bs = s.getDate(-105, i, 1);
            totalDays += f.getMonthLength(b);
            totalDays2 += s.getMonthLength(bs);
            double rate1 = (totalDays / i - (double) MONTH / DAY);
            double rate2 = (totalDays2 / i - (double) MONTH / DAY);
            System.err.println((Math.min(Math.abs(rate1), Math.abs(rate2)) == Math.abs(rate1) ? "Yes" : "No") + " Fixed: " + (totalDays - totalDays2) + " " + rate1 + " cal:" + rate2);
            // if (Math.abs(b.getDate() - bs.getDate() )>1*DAY) {
            System.err.println(i + " " + i % 353 + " " + b.getCalendar().getYearType(b).isSmallLeap() + " " + b.getCalendar().getYearType(b) + " YL: " + b.getCalendar().getYearLength(b) + " ML:" + b.getCalendar().getMonthLength(b) + "\tSamiFixed: " + b);
            //      System.err.println(bs.getCalendar().getYearType(bs)+" YL: " + bs.getCalendar().getYearLength(bs) + " ML:" + bs.getCalendar().getMonthLength(bs) + "\tSamiCal  : " + bs);
            //    System.err.println("\t"+b.getCalendar().getYearLength(b));
            //  System.err.println("\t"+bs.getCalendar().getYearLength(bs));
            //  System.err.println("");
            // }

        }
        long t = s.getDate(1, 1, 1).getDate();
        for (int i = 1; i < 1; i++) {

            BasicDate b = f.getDate(t);
            BasicDate bs = s.getDate(t);
            //if (Math.abs(b.getDate() - bs.getDate() )>1*DAY) {
            System.err.println(b.getCalendar().getYearType(b).isSmallLeap() + " " + b.getCalendar().getYearType(b) + " YL: " + b.getCalendar().getYearLength(b) + " ML:" + b.getCalendar().getMonthLength(b) + "\tSamiFixed: " + b);
            System.err.println(bs.getCalendar().getYearType(bs) + " YL: " + bs.getCalendar().getYearLength(bs) + " ML:" + bs.getCalendar().getMonthLength(bs) + "\tSamiCal  : " + bs);
            //    System.err.println("\t"+b.getCalendar().getYearLength(b));
            //  System.err.println("\t"+bs.getCalendar().getYearLength(bs));
            System.err.println("");
            //}
            t += DAY * 366;

        }
        System.exit(0);
    }

    public static void testFirstMoon() {
        long t = sc.getDate(1, 1, 1).getDate();
        MoonTimes m = MoonTimes.compute().at(SamiApplication.getBasicLocation().getLat(), SamiApplication.getBasicLocation().getLon()).on(new Date(t)).execute();
        MoonPhase p = MoonPhase.compute().on(new Date(t)).execute();
        System.err.println("first set: " + p.getTime().getTime());

        System.exit(0);
    }

    static void testNewMoon2() {
        MoonPhase mp = MoonPhase.compute().on(new Date()).execute();
        System.err.println(mp);
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        System.err.println(Arrays.asList(TimeZone.getAvailableIDs()));
        System.err.println(Arrays.asList(TimeZone.getDefault()));
        System.err.println(10800000 / MINUTE);
        c.setTimeZone(TimeZone.getTimeZone("us"));
        System.err.println(c.getTime());
        System.exit(0);
    }

    public static void testGeneric() {
        LunerIdentifier l = new UmAlquraStandard();
        GenericLunerCalendar cal = new GenericLunerCalendar(new Omari30YearLoop(), l);
        long start = new Date().getTime();
        BasicDate prev = null;
        for (int i = 0; i < 1000; i++) {

            BasicDate b1 = cal.getDate(start + i * DAY);
            if (b1.equals(prev)) {
                continue;
            }
            System.err.println(b1);
            prev = b1;
        }
        start = start - 100 * DAY;
        long p1 = 0;
        for (int i = 0; i < 1000; i++) {

            long b1 = l.getNextMonth(start + i * DAY);
            if (b1 == p1) {
                continue;
            }
            System.err.println(new Date(b1) + "\t" + (b1 - p1) / DAY);
            p1 = b1;
        }

        System.exit(0);
    }

    public static void testFajr() {
        BlackFajrStandard f = new BlackFajrStandard();
        GenericLunerCalendar g = new GenericLunerCalendar(new QazwiniCalendar(), f);
        long month = g.getDate(1399, 1, 1).getDate();
        long tempMonth = month;
        for (int i = 0; i < 2000; i++) {
            long nm = f.getPreviousMonth(month + i * DAY);
            BasicDate d1 = g.getDate(1399, 1, i + 1);
            BasicDate c1 = g.getDate(month + i * DAY);

            //    System.err.println(i+" calc:"+c1+" "+g.getMonthLength(c1)+"\tWritten"+d1+" "+g.getMonthLength(d1)+"\t"+((d1.getDate()==c1.getDate())?"":"WRONG"));
            CalendarFactory.test(d1);
            CalendarFactory.test(c1);

            tempMonth = nm;
        }

        // System.exit(0);
    }
    static BasicCalendar ad = CalendarFactory.getInstance().getCalendar(BasicCalendar.AD_ID);

    static void printWasmi(BasicDate bd) {
        BasicDate a = ad.getDate(bd.getDate());
        int monthLength = bd.getCalendar().getMonthLength(bd);
        String weekDay = CalendarFactory.getWeekDay(bd);

        System.err.println(
                a.getYear() + "\t"
                + a.getMonth() + "\t"
                + a.getDay() + "\t"
                + monthLength + "\t"
                + bd.getCalendar().getMonthName(bd)+"\t"
                + weekDay + "\t"
                + bd.getYear() + "\t"
                + bd.getMonth() + "\t"
                + bd.getDay() + "\t"
                + bd);
    }

    public static void printWasmi() {
        BasicCalendar cal = CalendarFactory.getInstance().getCalendar(BasicCalendar.SAMI_LUNER_ID);

        for (int i = -105; i < 1600; i++) {

            BasicDate bd = cal.getDate(i, 1, 1);
            int size=bd.getCalendar().getYearType(bd).getSubPeriods().size();
            for (int o = 0; o < size; o++) {
                bd = cal.getDate(i, o+1, 1);
                
                printWasmi(bd);
            }
        }
        System.exit(0);
    }

    public static void test() {
     //   printWasmi();
        // testFajr();
        // testGeneric();

        //    testcorrelation(); 
        //   testNewMoonFullYear();
        //    testFirstMoon();
        //    testSamiFixed();
        //      test16();
//    testOmari();
        //   testJalali();
        //       testNewMoon();
        // System.exit(0);
        //   testLunerLocationCalendar();
        //   testMovements();
        //    if(true)return;
        //    testConversions();
        //    testYearLength();
        //      System.exit(0);
        //    testAdjustedDate();
        // System.exit(0);
    }

    private static void test(BasicCalendar c1, int y1, int m1, int d1, BasicCalendar c2, int y2, int m2, int d2) {
        BasicDate bd = c1.getDate(y1, m1, d1);
        System.err.println("orig:" + new Date(bd.getDate()));
        BasicDate bd2 = c2.getDate(bd.getDate());
        CalendarFactory.test(bd);
        CalendarFactory.test(bd2);

        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date(bd.getDate()));
        if (bd2.getYear() == y2 && bd2.getMonth() == m2 && bd2.getDay() == d2) {

            System.err.println("Success " + bd + " " + bd2 + " DayOfWeek: " + c.getTime());

        } else {
            System.err.println("Failed: " + bd + " " + bd2 + " Should be: " + y2 + " " + m2 + " " + d2);
        }
    }

    public static void testConversions() {
        test(gc, 1900, 1, 13, jc, 1900, 1, 1);
        test(gc, 1900, 1, 14, jc, 1900, 1, 2);
        test(gc, 1900, 1, 1, sc, 1899, 12, 20);
        test(gc, 2020, 8, 31, sc, 2020, 8, 18);

        test(gc, 516, 1, 23, hc, 4276, 5, 1);
        test(gc, 516, 1, 23, wc, -105, 1, 1);
//        test(gc, 516,1, 23, bc, -106, 7, 1);
        test(gc, 516, 1, 23, qc, -105, 3, 1);
        System.err.println("631");
        test(gc, 631, 2, 11, hc, 4391, 6, 1);
        test(gc, 631, 2, 11, wc, 10, 1, 1);
        test(gc, 631, 2, 11, bc, 9, 8, 1);
        test(gc, 631, 2, 11, qc, 10, 3, 1);
        System.err.println("qaz");
        test(gc, 524, 11, 15, qc, -97, 13, 1);
        test(gc, 527, 11, 12, qc, -94, 13, 1);
        test(gc, 530, 11, 9, qc, -91, 13, 1);
        test(gc, 532, 11, 16, qc, -89, 13, 1);
        test(gc, 535, 11, 14, qc, -86, 13, 1);

        test(gc, 517, 7, 8, qc, -104, 13, 1);

        System.err.println("byroni");
        test(gc, 517, 7, 8, bc, -105, 13, 1);
        test(gc, 520, 7, 4, bc, -102, 13, 1);
        test(gc, 523, 7, 2, bc, -99, 13, 1);
        test(gc, 525, 7, 9, bc, -97, 13, 1);
        test(gc, 528, 7, 6, bc, -94, 13, 1);
        test(gc, 531, 7, 3, bc, -91, 13, 1);
        test(gc, 517, 7, 11, bc, -89, 13, 1);
        test(gc, 536, 7, 7, bc, -86, 13, 1);
        test(gc, 539, 7, 5, bc, -83, 13, 1);
        test(gc, 542, 7, 1, bc, -80, 13, 1);
        test(gc, 544, 7, 9, bc, -78, 13, 1);
        test(gc, 547, 7, 6, bc, -75, 13, 1);
        test(gc, 550, 7, 3, bc, -72, 13, 1);
        test(gc, 555, 7, 8, bc, -67, 13, 1);
        test(gc, 558, 7, 5, bc, -64, 13, 1);
        test(gc, 561, 7, 1, bc, -61, 13, 1);

        test(gc, 2020, 9, 18, wc, 1399, 9, 1);

        BasicCalendar z = CalendarFactory.getZodiac13Calendar();
        //     BasicDate bd=z.getDate(CalendarFactory.getCurrentDate().getDate());
        System.err.println("Testing ");
        for (int i = 0; i < 2000; i++) {
            BasicDate b1 = z.getDate(2020, 1, i + 1);
            System.err.println(b1 + " " + b1.getCalendar().getMonthLength(b1) + " " + b1.getCalendar().getMonthName(b1));
        }
        //   System.exit(0);

    }

    public static void testConversions2() {
        GregoryCalendar gc = CalendarFactory.getGregoryCalendar();
        System.err.println("testing coversions");
        BasicDate bd = null;
        bd = gc.getDate(2020, 2, 28);
        CalendarFactory.test(bd);
        bd = gc.getDate(2020, 2, 29);
        CalendarFactory.test(bd);
        bd = gc.getDate(2020, 3, 1);
        CalendarFactory.test(bd);

        bd = wc.getDate(1399, 1, 1);
        CalendarFactory.test(bd);
        bd = bc.getDate(1000, 1, 1);
        CalendarFactory.test(bd);
        bd = qc.getDate(500, 1, 1);
        CalendarFactory.test(bd);
        bd = hc.getDate(5555, 1, 1);
        CalendarFactory.test(bd);
        bd = hc.getDate(6000, 1, 1);
        CalendarFactory.test(bd);

        System.err.println("Finished Testing Conversions");

        System.err.println("testing start times");
    }

}
