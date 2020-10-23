/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nasiiCalendar.locationBasid;

import com.alhanah.samicalapp.store.CalendarStore;
import java.util.Calendar;
import java.util.Date;
import nasiiCalendar.BasicCalendar;
import static nasiiCalendar.BasicCalendar.DAY;
import nasiiCalendar.CalendarFactory;
import org.shredzone.commons.suncalc.MoonPhase;
import org.shredzone.commons.suncalc.MoonTimes;
import org.shredzone.commons.suncalc.SunTimes;

/**
 *
 * @author hmulh
 */
public class BlackFajrStandard implements LunerIdentifier{
 
    City city;

    public BlackFajrStandard() {
        this(City.MAKKA);
    }

    public BlackFajrStandard(City city) {
        this.city = city;
    }
    
    
    @Override
    public long getNextMonth(long time) {
        time=CalendarFactory.dayStart(time);
        MoonPhase blackPhase=MoonPhase.compute().on(new Date(time)).execute();
        long blackPhaseDay=CalendarFactory.dayStart(blackPhase.getTime().getTime());
        SunTimes st=SunTimes.compute().on(new Date(blackPhaseDay)).at(City.MAKKA.getLat(), City.MAKKA.getLon()).execute();
        if(st.getRise()==null)return blackPhaseDay+DAY;
        if(blackPhase.getTime().getTime()<st.getRise().getTime()){
            if(time==blackPhaseDay)return getNextMonth(time+DAY);
            return blackPhaseDay;
            
        }else{
        //    System.err.println("\tbl:"+blackPhase.getTime()+"\tst"+st.getRise()+" nextday");
            return blackPhaseDay+DAY;
            
            
        }
        
    }

    @Override
    public long getPreviousMonth(long time) {
        time=CalendarFactory.cleanDate(time);
        long clean=CalendarFactory.dayStart(time);
        long prev1=getNextMonth(clean-31*DAY);
        long prev2=getNextMonth(prev1+DAY);
        if(time>prev2)return prev2;
        return prev1;
        
    }

    @Override
    public City getCity() {
        return City.MAKKA;
    }

    @Override
    public boolean isLocationBased() {
        return true;
    }

    @Override
    public String getName() {
        return BasicCalendar.QAZWINI_FAJR_ID;
    }
    public String getShortName(){
        return BasicCalendar.QAZWINI_FAJR_ID+CalendarStore.SHORT;
    }

    @Override
    public String toString() {
        return "FajrC"; //To change body of generated methods, choose Tools | Templates.
    }
    
        
}
