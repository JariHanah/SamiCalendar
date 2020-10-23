
    
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nasiiCalendar;

import java.util.Date;

/**
 *
 * @author Hussam
 */
public abstract class MyBasicCalendar implements BasicCalendar{

    
    private long matchTime;
    private int matchYear;
    private long bigOfTime;
    private long maxInstant;
    private long minInstant;
    private long cycleTime;
    private int cycleYear;
    private int startYear;
    private long startTime;
    private static final long STARTTIME=-631158216466114979L; //-200M
    public static final long ENDTIME=631132823551165020L; //200M
    private static final long STARTTIME3=-6311582164661149794L; //-200M
    private static final long ENDTIME3=6311328235511650207L; //200M
    private static final long STARTTIME2=METONIC*-40000;
    private static final long ENDTIME2=METONIC*40000;
    //private BasicCalendarFactory factory;
    //private final BasicDate DUMMY_DATE;
   // public static final long START_SAMI = -45881953199752l ;//-4000*METONIC;
//    public static final long BIGGNING_OF_TIME= METONIC*-100; 
//    private BasicDate dummyDate;
    private MyBasicCalendar(long cycleTime, int cycleYear, long matchTime, int matchYear,  long bigOfTime, long maxInstant) {
     //   this.factory=fac;
        this.matchTime = matchTime;
        this.matchYear = matchYear;
        this.bigOfTime = bigOfTime;
        this.cycleTime = cycleTime;
        this.cycleYear = cycleYear;
        this.minInstant=bigOfTime;
        this.maxInstant = maxInstant;
        
        calcTimes();
        //hussam.println("START: "+startTime+" y: "+startYear);
       // hussam.println("MAX: "+this.maxInstant +"is: "+(Long.MAX_VALUE>this.maxInstant));
        
    }
    public BasicDate getMaximumDate(){
        return getDate(getMaxInstant()-DAY);
    }

    @Override
    public BasicDate getMinimumDate() {
        return getDate(getMinInstant()+DAY);
    }
    
    protected MyBasicCalendar(long cycleTime, int cycleYear, long matchTime, int matchYear) {
        this(cycleTime, cycleYear, matchTime, matchYear, STARTTIME/4, ENDTIME);
        
    }
      /**
     * @return the minInstant
     */
    public long getMinInstant() {
        return minInstant;
    }

    /**
     * @param minInstant the minInstant to set
     */
    public void setMinInstant(long minInstant) {
        this.minInstant = minInstant;
        calcTimes();
    }
    protected abstract BasicDate calculateDate(int y, int m, int d);
    
    public BasicDate getDate(int y, int m, int d){
        BasicDate b=calculateDate(y, 1,1);
        int ymc=b.getCalendar().getYearType(b).getSubPeriods().size();
   //     hussam.println("ymc: "+ymc+" year: "+y+" month: "+m+" day:"+d);
        if(ymc<m){
        //    hussam.println("\tYear Correction: "+(y+1)+" "+(m-ymc)+" "+(d));
            return getDate(y+1, m-ymc, d);
        }
        b=calculateDate(y, m, 1);
        int mdc=b.getCalendar().getMonthLength(b);
        if(mdc<d){
        //    hussam.println("mdc: "+mdc+"\tMonthCorrection: "+y+" "+(m+1)+" "+(d-mdc));
            return getDate(y, m+1, d-mdc);
        } else {
            return calculateDate(y, m, d);
        }
    }
    @Override
    public BasicDate getCycleStart(long time){
        int x=(int) ((time-getStartTime())/getLongCycleTime());
        long result=x*getLongCycleTime()+getStartTime();
        BasicDate g=getDate(result);
        //hussam.println(getName()+" Cycle Start: "+g);
        
        return getDate(result);
    }
    
    protected boolean isDateInRange(BasicDate bd){
        return isDateInRange(bd.getDate());
    }
    protected boolean isDateInRange(long d){
        return (d>getMinInstant()& d<getMaxInstant());
    }
    
    private void calcTimes(){
        startYear=calcStartYear();
        startTime=calcStartTime();
       
    }
    
    protected void setBigOfTime(long min){
        this.bigOfTime=min;
    }
    public void setMaxInstant(long max){
        this.maxInstant=max;
        calcTimes();
    }
    protected long getMaxInstant(){
        return maxInstant;
    }
    
    public long getBigOfTime(){
        return bigOfTime;
    }
    
    public long getMatchTime(){
        return matchTime;
    }
    public int getMatchYear(){
        return matchYear;
    }
    
    protected int getStartYear(){
        return startYear;
    }
    protected long getStartTime(){
        return startTime;
    }
    
    protected long calcStartTime(){
        long x= getMatchTime()-(getMatchYear()-calcStartYear())/getLongCycleYear()*getLongCycleTime();
      /*  hussam.println(getClass()+ 
                "\n\tcalcStartTime: "+x +" "+new Date(x)+" calcStartYear:"+getStartYear()+
                "\n\t    MatchTime: "+getMatchTime()+    "    Match Year: "+getMatchYear()+
            //    "\n\t    Long Time: "+getLongCycleTime()+"     Long Year: "+getLongCycleYear()+
                "\n\t          BOT: "+getBigOfTime()+    "           Max: "+getMaxInstant());//*/
        return x;
    }
    protected int calcStartYear(){
        int  x=(int) (getMatchYear()-((getMatchTime()-getBigOfTime())/getLongCycleTime())*getLongCycleYear());
        //int  x=(int) (((getMatchTime()-getBigOfTime())/getLongCycleTime())*getLongCycleYear()+getMatchYear());
        //hussam.println(getClass()+" MinInstant:"+getBigOfTime()+" Match:"+getMatchTime()+" y:"+getMatchYear()+" startYear:"+x);
        
        return x;
    }
    public long getLongCycleTime(){
        return cycleTime;
    }
    public int getLongCycleYear(){
        return cycleYear;
    }

    @Override
    public final boolean isLeapYear(BasicDate bd) {
        return isLeapYear(bd.getYear());
    }
    @Override
    public final PeriodType getYearType(BasicDate bd){
        return getYearType(bd.getYear());
    }
    
    public String toString(){
       return getName() ;
    }
}
