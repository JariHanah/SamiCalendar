/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nasiiCalendar.locationBasid;

import java.util.Date;
import static nasiiCalendar.BasicCalendar.DAY;
import nasiiCalendar.CalendarFactory;
import org.shredzone.commons.suncalc.MoonPhase;
import org.shredzone.commons.suncalc.MoonTimes;
import org.shredzone.commons.suncalc.SunTimes;

/**
 *
 * @author hmulh
 */
public interface LunerIdentifier {
    public long getNextMonth(long time);
    public long getPreviousMonth(long time);
    public City getCity();
    public boolean isLocationBased();
    public String getName();
    public String getShortName();
}

