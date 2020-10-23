/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nasiiCalendar.locationBasid;

import nasiiCalendar.LunerCalendar;
import nasiiCalendar.MyDate;
import com.codename1.location.Location;
import java.util.Date;
import java.util.List;
import nasiiCalendar.BasicCalendar;
import nasiiCalendar.BasicDate;
import nasiiCalendar.CalendarFactory;
import nasiiCalendar.Omari30YearLoop;
import nasiiCalendar.PeriodType;
import static nasiiCalendar.BasicCalendar.DAY;
import nasiiCalendar.MyBasicCalendar;
import org.shredzone.commons.suncalc.MoonPhase;

/**
 *
 * @author Hussam
 */
public class GenericLunerCalendar extends LunerCalendar {

    private static final long FIRST_BLACK = -157789552418880000L;
    private final static int STARTDATE = -1455442;
    private final static long STARTLONG = FIRST_BLACK;//-METONIC353 * 4000 + START_SAMI +1 * MONTH-DAY;
    Location location;
  //  BasicYear locationYear;
    BasicCalendar calendar = new Omari30YearLoop();
    int minutes = 30;
    private final LunerIdentifier lunerIdentifier;

    public GenericLunerCalendar(MyBasicCalendar b,LunerIdentifier li) {
        super(li.getName(), li.getShortName(), b.getLongCycleTime(), b.getLongCycleYear(), b.getMatchTime(), b.getMatchYear());
        calendar=b;
        //locationYear = new BasicYear("Luner Year", "Luner Year", false, false, new PeriodType[]{Months.MUHARRAM, Months.SAFAR, Months.RABEI1, Months.RABEI2, Months.JAMAD1, Months.JAMAD2,
          //  Months.RAJAB, Months.SHAABAN, Months.RAMADHAN, Months.SHAWWAL, Months.THO_QIDAH, Months.THO_HIJA});
        lunerIdentifier = li;
    }

    public MoonPhase getPreviousPhase(long date) {

        MoonPhase p = MoonPhase.compute().on(new Date(date - 33 * DAY)).execute();
        MoonPhase p2 = MoonPhase.compute().on(new Date(p.getTime().getTime() + DAY)).execute();
        if (p2.getTime().getTime() < date & date - p2.getTime().getTime() > DAY) {
            return p2;
        }

        return p;
    }

    public int getMonthLength(BasicDate bd) {
        long day0 = lunerIdentifier.getPreviousMonth(bd.getDate());
        long day1 = lunerIdentifier.getNextMonth(bd.getDate());//prev.getTime().getTime()).time;
        if(day0==day1)System.err.println(new Date(day0)+" "+new Date(day1)+" "+new Date(bd.getDate()));
        int days = (int) ((day1 - day0) / DAY);
        return days;

    }
    /*
    @Override
    public BasicDate getDate(int y, int m, int d) {
        return calculateDate(y, m, d);
    }//*/

    @Override
    protected BasicDate calculateDate(int y, int m, int d) {
     //   BasicDate b = calendar.getDate(y, m, d);
        BasicDate f = calendar.getDate(y, m, 1);
       
        long previousMoon=lunerIdentifier.getPreviousMonth(f.getDate());
        long nextMoon=lunerIdentifier.getNextMonth(f.getDate());
        long thisMonth=f.getDate();
        long result=thisMonth;
        
        if(Math.abs(nextMoon-thisMonth)>Math.abs(previousMoon-thisMonth)){
            result=previousMoon;
     //       System.err.println("previous");
        }else{
            result=nextMoon;
           //System.err.println("next");
        }
        result=CalendarFactory.cleanDate(result+(d-1)*DAY);
        
        BasicDate r= new MyDate(result, d, m, y, this);
    //    System.err.println("calc: "+r);
       return getDate(result);
        
     //   return r;
    }

    @Override
    public boolean isLeapYear(int year) {
        return calendar.isLeapYear(year);
    }

    @Override
    public BasicDate getDate(long time) {
        time = CalendarFactory.cleanDate(time);

        long monthStart = lunerIdentifier.getPreviousMonth(time);
        long dur = time - CalendarFactory.cleanDate(monthStart);

        int days = (int) ((dur) / DAY)+1;
    //    System.err.println(new Date(time)+"\t"+dur);
  //      System.err.println("month start: " + new Date(monthStart) + "\t" + days);
        BasicDate calc = calendar.getDate(time);
        BasicDate calc2 = calendar.getDate(calc.getYear(), calc.getMonth(), days);
        if (Math.abs(calc.getDate() - calc2.getDate()) / DAY > 5) {
            if (days > 15) {
          //      System.err.println("days>15");
                if (calc.getMonth() > 1) {
                    
                    calc = new MyDate(time, days, calc.getMonth() - 1, calc.getYear(), this);
                } else {
                    calc = new MyDate(time, days, calc.getCalendar().getYearType(calc.getYear()-1).getSubPeriods().size(), calc.getYear() - 1, this);
                }
            } else if (days < 15) {
       //         System.err.println("days<15");
                if (calc.getMonth() < calc.getCalendar().getYearType(calc.getYear()).getSubPeriods().size()) {
                    calc = new MyDate(time, days, calc.getMonth() + 1, calc.getYear(), this);
                } else {
                    calc = new MyDate(time, days, 1, calc.getYear() + 1, this);
                }
            }
        } else {
         //   System.err.println("normal");
            calc = new MyDate(time, days, calc.getMonth(), calc.getYear(), this);
        //    System.err.println("\tcalculated: "+calc);
        }
        return calc;
    }

    @Override
    public PeriodType getYearType(int year) {
        return calendar.getYearType(year);
    }

    @Override
    public List<PeriodType> getYearTypes() {
        return calendar.getYearTypes();
    }
    
    public City getCity() {
        return lunerIdentifier.getCity();
    }

    public String toString() {
        return lunerIdentifier.toString();
    }

    public boolean isLocationBased() {
        return lunerIdentifier.isLocationBased();
    }

    public String getShortName() {
        return lunerIdentifier.getShortName();
    }
}
