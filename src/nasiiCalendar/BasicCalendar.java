/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nasiiCalendar;

import java.util.List;
import java.util.TimeZone;

/**
 *
 * @author Hussam Almulhim
 */
public interface BasicCalendar {
    
    public static final String SKU_FULL_VERSION = "com.alhanah.sami.full";
    public static final String SAMI_ID = "com.alhanah.sami.sami";
    public static final String SAMI_FIXED_ID="com.alhanah.sami.sami.fixed";
    public static final String SAMI_LUNER_ID="com.alhanah.sami.sami.luner";
    public static final String GREG_ID = "com.alhanah.sami.greg";
    public static final String JULIAN_ID = "com.alhanah.sami.julian";
    public static final String JALALI_IR_ID="com.alhanah.jalili.ir";
    public static final String JALALI_SA_ID="com.alhanah.jalili.sa";
    
    public static final String SOLAR_128_ID = "com.alhanah.sami.solar128";
    public static final String SOLAR_STATIONS_ID = "com.alhanah.sami.solarstations";
    public static final String ZODIAC13_ID = "com.alhanah.sami.zodiac";
    public static final String AD_ID = "com.alhanah.sami.ad";
    public static final String BYRONI_ID = "com.alhanah.sami.byroni";
    public static final String QAZWINI_ID = "com.alhanah.sami.qazwini";
    public static final String WSMI_ID = "com.alhanah.sami.wsmi";
    public static final String HEWBREW_ID = "com.alhanah.sami.hebrew";

    public static final String NEW_MOON_ID = "com.alhanah.sami.newmoon";
    public static final String BLACK_MOON_ID = "com.alhanah.sami.blackmoon";

    public static final String SKU_METONIC_VIEW = "com.alhanah.sami.metonicview";
    public static final String SKU_YEAR_VIEW = "com.alhanah.sami.yearview";
    public static final String SKU_MONTH_VIEW = "com.alhanah.sami.monthview";
    public static final String SKU_DAY_VIEW = "com.alhanah.sami.dayview";
    
    public static final String QAZWINI_FAJR_ID="com.alhanah.sami.qazwini.fajr";
    
    
    public static final String OMARI_ID = "com.alhanah.sami.omari";
    public static final String OMARI_ID_16 = "com.alhanah.sami.omari.16";
    public static final String OMARI_ID_15 = "com.alhanah.sami.omari.15";
    public static final String OMARI_ID_INDIAN = "com.alhanah.sami.omari.indian";
    public static final String OMARI_ID_HABASH = "com.alhanah.sami.omari.habash";
    public static final String UMM_ALQURA_CALENDAR="com.alhanah.sami.umalqura1423";
    
    
    public static final long CALENDAR_ZERO_START = -42564970799000L;
 //   public static final long CALENDAR_START = -42534385199000L;
  //  public static final long CALENDAR_METONIC_START = -42281751599000L;
    
    public static final long SECONDS = 1000L;
    public static final long MINUTE = SECONDS * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;
    public static final long WEEK = DAY * 7;     //29.530587981
    public static final long MONTH = (long) (DAY * 29.530587981);//29.53059
    public static final long YEAR_TROPICAL = (long) (DAY * 365.242189 );
    public static final long YEAR_SIDEREAL = (long) (DAY * 365.256363);
    public static final long METONIC = MONTH * 235;
    
    public static final long START_SAMI = -45881953199752L+INIT.off;// -45881866800001L;//-45881953200001L;//-45881953199752l ;//-4000*METONIC;
    public BasicDate getMaximumDate();
    public BasicDate getMinimumDate();
    
    public BasicDate getCycleStart(long cycleLength);
 //   public BasicCalendarFactory getFactory();
    boolean isLeapYear(BasicDate bd);
    boolean isLeapYear(int year);
    public int getMonthLength(BasicDate bd);
    public int getYearLength(BasicDate bd);
    public BasicDate getDate(long time);
    public BasicDate getDate(int y, int m, int d);
    public BasicDate nextDay(BasicDate bd);
    public PeriodType getYearType(BasicDate bd);
    public PeriodType getYearType(int year);
    public List<PeriodType> getYearTypes();
    
    public PeriodType getMonthName(BasicDate bd);
   
    public BasicDate getDateByName(PeriodType bm, BasicDate bd);
    public long getLongCycleTime();
    public int getLongCycleYear();
    /*
    public String getShortEnglishName();
    public String getShortArabicName();
    public String getEnglishName();
    public String getArabicName();
    //*/
    public String getName();
    public String getShortName();
    
}
class INIT{
    static TimeZone t=TimeZone.getDefault();
    static long off=t.getRawOffset();
}