/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nasiiCalendar;

import java.util.Date;
import java.util.TreeMap;
import nasiiCalendar.BasicCalendar;
import nasiiCalendar.BasicDate;
import nasiiCalendar.CalendarFactory;
import nasiiCalendar.MyBasicCalendar;
import nasiiCalendar.PeriodType;
import static nasiiCalendar.BasicCalendar.DAY;
import static nasiiCalendar.BasicCalendar.MONTH;

/**
 *
 * @author Hussam
 */
public abstract class LunerCalendar extends MyBasicCalendar implements BasicCalendar {

    public static final long YEAR_HARAM = MONTH * 13;
    public static final long YEAR_LUNER = MONTH * 12;
    
    final TreeMap<Long, Integer> LUNER_YEAR = new TreeMap<Long, Integer>();
    final TreeMap<Integer, Long> LUNER_YEAR_DURATION = new TreeMap<Integer, Long>();
    String englishName;
    
    String shortEnglishName;
   
    
    
    public LunerCalendar(String eng, String shE, long cycleTime, int cycleYear, long matchTime, int matchYear) {
        super(cycleTime, cycleYear, matchTime, matchYear);
        englishName = eng;
        this.shortEnglishName = shE;
        
    }
    public String getName(){
        return englishName;
    }
    public String getShortName(){
        return shortEnglishName;
    }
    

    void setupMap(int[] list) {
        long length = 0;
        long total = 0;
        for (int i = 0; i < list.length; i++) {
            LUNER_YEAR.put(total, i);
            LUNER_YEAR_DURATION.put(i, total);
            length = (list[i] == 0) ? YEAR_LUNER : YEAR_HARAM;
            total += length;
        }
    }

    public String getShortEnglishName() {
        return shortEnglishName;
    }
    

    @Override
    public int getMonthLength(BasicDate bd) {
        long start=getStartTime();
        long dur = bd.getDate()-start;//CalendarFactory.cleanDate(bd.getDate()) - start;
        long m = (int) (dur / MONTH);
        long durFloor=m*MONTH;
        long durCieling = (m + 1) * MONTH;
        int result = (int) ((durCieling/DAY- durFloor / DAY));
        return result;

    }
    @Override
    public int getYearLength(BasicDate bd) {
        long start=getStartTime();
        long dur = bd.getCalendar().getDate(bd.getYear(),1,1).getDate()- start;
        int d = (int) (dur / DAY);
        
        long durCieling = bd.getCalendar().getYearType(bd).getSubPeriods().size()*MONTH+dur;
        int d2=(int) (durCieling/DAY);
        int result = d2-d;
     //   hussam.println("\t\t"+result);//+" "+new Date(durFloor+start)+" "+new Date(durCieling+start));
        return result;
    }
 //   @Override
    public int getYearLength2(BasicDate bd) {
        BasicDate from = getDate(bd.getYear(), 1, 1);
        BasicDate to = getDate(bd.getYear() + 1, 1, 1);
        
        int result =1+(int)(CalendarFactory.cleanDate(to.getDate())/DAY) - (int)(CalendarFactory.cleanDate(from.getDate()) / DAY);
        //hussam.println("\t"+from.getYear()+" "+result+" "+new Date(CalendarFactory.cleanDate(from.getDate()))+" "+new Date(CalendarFactory.cleanDate(to.getDate())));
        return result;
    }

    void setupMap2() {
        long t = 0;
        int i = 0;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_HARAM;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_LUNER;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_LUNER;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_HARAM;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_LUNER;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_LUNER;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_HARAM;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_LUNER;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_HARAM;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_LUNER;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_LUNER;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_HARAM;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_LUNER;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_LUNER;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_HARAM;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_LUNER;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_HARAM;
        LUNER_YEAR_DURATION.put(i, t);
        LUNER_YEAR.put(t, i++);
        t += YEAR_LUNER;
        //    LUNER_YEAR_DURATION.put(i, t);
        //     LUNER_YEAR.put(t, i++);

    }

    public void test(long t) {
    //    java.util.Calendar c = java.util.Calendar.getInstance();
        BasicDate b1 = getDate(t);
        BasicDate b2 = getDate(b1.getYear(), b1.getMonth(), b1.getDay());
        BasicDate b3 = getDate(b2.getDate());
        Date d1 = new Date(t);
        Date d2 = new Date(b1.getDate());
        Date d3 = new Date(b2.getDate());
        Date d4 = new Date(b3.getDate());

        if (b2.equals(b1) && b2.equals(b3)) {
        } else {
            System.err.println("GregDate: " + d1 + " " + d2 + " " + d3 + " " + d4);
            System.err.println("Dates b1: " + b1.getDate() + " b2: " + b2.getDate() + " b3: " + b3.getDate());
            System.err.println("days b1: " + b1.getDay() + " b2: " + b2.getDay() + " b3: " + b3.getDay());
            System.err.println("Months b1: " + b1.getMonth() + " b2: " + b2.getMonth() + " b3: " + b3.getMonth());
            System.err.println("years b1: " + b1.getYear() + " b2: " + b2.getYear() + " b3: " + b3.getYear());
            System.err.println("Calendars: " + b1.getCalendar() + " " + b2.getCalendar() + " " + b3.getCalendar());
            System.err.println("Objects: " + b1 + " " + b2 + " " + b3);
            throw new RuntimeException("Date Conversion Wrong");
        }
    }

    @Override
    public PeriodType getMonthName(BasicDate bd) {
       // hussam.println(bd+" date: "+ " "+getYearType(bd).getMonths().toString());
   //     hussam.println(" date: "+bd+ " "+getYearType(bd)+" "+bd.getCalendar()+" "+bd.getMonth());
    //    System.out.println("Type"+getYearLength(bd)+" months in year: " +getYearType(bd).getMonths().size()+ " month: "+bd.getMonth());
        return getYearType(bd).getSubPeriods().get(bd.getMonth() - 1);
    }

    @Override
    public BasicDate getDateByName(PeriodType bm, BasicDate bd) {
        //hussam.println("date: "+(getYearType(bd).getMonths().indexOf(bm)+1)+ " "+getYearType(bd).getMonths().toString());
        return new MyDate(bd.getDate(), bd.getDay(),getYearType(bd).getSubPeriods().indexOf(bm) + 1, bd.getYear(), this);
    }

    @Override
    public BasicDate nextDay(BasicDate bd) {
        long time = bd.getDate();
        return getDate(time + DAY);
    }

}

