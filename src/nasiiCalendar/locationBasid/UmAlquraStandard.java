package nasiiCalendar.locationBasid;


import com.alhanah.samicalapp.store.CalendarStore;
import java.util.Date;
import nasiiCalendar.BasicCalendar;
import static nasiiCalendar.BasicCalendar.DAY;
import nasiiCalendar.CalendarFactory;
import org.shredzone.commons.suncalc.MoonPhase;
import org.shredzone.commons.suncalc.MoonTimes;
import org.shredzone.commons.suncalc.SunTimes;

public class UmAlquraStandard implements LunerIdentifier{
    
    public long testMoonSet(long time){
         MoonTimes mt=MoonTimes.compute().on(new Date(time)).at(City.MAKKA.getLat(), City.MAKKA.getLon()).execute();
        SunTimes st=SunTimes.compute().on(new Date(time)).at(City.MAKKA.getLat(), City.MAKKA.getLon()).execute();
        if(mt.getSet().getTime()>=st.getSet().getTime()){
            return CalendarFactory.dayStart(time+DAY);
        }else{
            return testMoonSet(time+DAY);
        }
    }
    
    @Override
    public long getNextMonth(long time) {
        time=CalendarFactory.dayStart(time);
        MoonPhase blackPhase=MoonPhase.compute().on(new Date(time)).execute();
        long blackPhaseDay=CalendarFactory.dayStart(blackPhase.getTime().getTime());
        MoonTimes mt=MoonTimes.compute().on(new Date(blackPhaseDay)).at(City.MAKKA.getLat(), City.MAKKA.getLon()).execute();
        SunTimes st=SunTimes.compute().on(new Date(blackPhaseDay)).at(City.MAKKA.getLat(), City.MAKKA.getLon()).execute();
        if(st.getSet()==null)return time;
        if(blackPhase.getTime().getTime()<st.getSet().getTime()){
            return testMoonSet(CalendarFactory.dayStart(blackPhaseDay));
            
        }else{
            return testMoonSet(blackPhaseDay+DAY);
            
            
        }
        
    }

    @Override
    public long getPreviousMonth(long time) {
        long prev1=getNextMonth(time-31*DAY);
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
        return BasicCalendar.UMM_ALQURA_CALENDAR;
    }
    public String getShortName(){
        return BasicCalendar.UMM_ALQURA_CALENDAR+CalendarStore.SHORT;
    }
    
}