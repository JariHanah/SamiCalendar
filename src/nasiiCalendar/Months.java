/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nasiiCalendar;

import java.util.List;

/**
 *
 * @author Hussam
 */
public enum Months implements PeriodType {
    MUHARRAM("muharram","muh", 30),
    SAFAR("safar", "sfr", 29),
    RABEI1("rabei1", "rb1",  30),
    RABEI2("rabei2", "rb2", 29),
    JAMAD1("jamad1","jm1",30),
    JAMAD2("jamad2", "rb2", 29),
    RAJAB("rajab", "rjb",  30),
    SHAABAN("shaaban", "shb", 29),
    RAMADHAN("ramadhan",  "rmd",30),
    SHAWWAL("shawwal", "shw", 29),
    THO_QIDAH("thoqidah", "qdh",30),
    THO_HIJA("thohija", "haj", 29),
    ALHARAM("alharam", "hrm", 30),
    SAFAR2("safar2", "sfr2", 29),
    RAJAB_MODHAR("rajabmodhar", "rjm", 30),
    RAJAB_RABEEIAH("rajabrabiah", "rjr",30),
    NASEI("nasi", "nsi", 30),
    JANUARY("january", "jan", 31),
    FEBUARY("febuary", "feb", 28),
    MARCH("march", "mar", 31),
    APRIL("april", "apr", 30),
    MAY("may","mayshort",31),
    JUNE("june", "jun",30),
    JULY("july", "jul",31),
    AUGUST("august", "aug", 31),
    SEPTEMBER("september","spt", 30),
    OCTOBER("october", "oct", 31),
    NOVEMBER("november", "nov",30),
    DECEMBER("december", "dec", 31),
    
    L01("zabih", "zbh", 13),
    L02("bolaa","bla",13),
    L03("saud", "sad",13),
    L04("akhbiyah", "akb", 13),
    L05("mokaddam", "mkd", 13),
    L06("moakhar", "mkr", 13),
    L07("rasha", "rsh", 13),
    L08("shartain", "sht", 13),
    L09("butain","btn", 13),
    L10("thuraya", "thr", 13),
    L11("addobran", "dbr", 13),
    L12("hagaah", "hgh", 13),
    L13("hanaah", "hnh",13),
    L14("thiraa",  "thrshort", 13),
    L15("nathrah", "nth", 13),
    L16("taraf", "trf", 13),
    L17("jabhah", "jbh", 14),
    L18("zobrah", "zbr", 13),
    L19("sarfah", "srf", 13),
    L20("alawaa", "awa",13),
    L21("sammak", "smk", 13),
    L22("alghfr", "gfr", 13),
    L23("zabana", "zbn", 13),
    L24("ikleel", "ikl", 13),
    L25("alkalb", "klb", 13),//////14
    L26("sholah","shl", 13),
    L27("naaim","nim",13),
    L28("baldah", "bld", 13),
    
    Z01("z01", "z01s", 32),
    Z02("z02", "z02s",28),
    Z03("z03", "z03s", 24),
    Z04("z04", "z04s", 38),///39
    Z05("z05",  "z05s", 25),
    Z06("z06", "z06s",37),
    Z07("z07", "z07s", 31),
    Z08("z08", "z08s",  20),
    Z09("z09","z09s", 37),
    Z10("z10", "z10s", 45),
    Z11("z11", "z11s", 25),
    Z12("z12", "z12s", 7),
    Z13("z13","z13s", 18), 
    
    TISHREI("tishrei", "tsh", 30), 
    CHESHVAN("cheshvan", "chv", 29), ////30
    KISLEV("kislev", "kis", 30),/////29 
    TEVET("tevet","tvt", 29),
    SHEVAT("shevat", "shv",30), 
    ADAR("adar","tshshort", 29), 
    ADAR2("adar2",  "adr2",30), 
    NISAN("nisan", "nsn", 30), 
    IYAR("iyar", "iyr",29), 
    SIVAN("sivan", "svn", 30), 
    TAMMUZ("tammuz", "tmz",29), 
    AV("av", "avshort",30), 
    ELUH("eluh", "elh", 29),
    
    HAMAL("hamal","hamalshort", 31),
    SAWR("sawr","sawrshort", 31),
    JAWZA("jawza","jawzashort", 31),
    SARATAN("saratan","saratanshort", 31),
    ASAD("asad", "asadshort", 31),
    SONBOLA("sonbola","sonbolashort", 31),
    MIZAN("mizan","mizanshort", 30),
    AQRAB("aqrab","aqrabshort", 30),
    QAWS("qaws","qawsshort", 30),
    JADI("jadi","jadishort", 30),
    DALW("dalw","dalwshort", 30),
    HUT("hut","hutshort", 29),/////30
    
    
    HILAL_MOON("moonMonth", "moonMonthShort", 29),
    BLACK_MOON("blackMonth", "blackMonthShort", 29);
    String enName;
    String enShort;
    int days;
    boolean isSmallLeap=false,isBigLeap=false;
    Months(String en, String sh1, int d) {
        enName = en;
        enShort=sh1;
        days=d;
        
    }
    
    public String getName(){
        return enName;
    }
    public String getShortName(){
        return enShort;
    }
   
    @Override
    public boolean isBigLeap() {
        return false;
    }

    @Override
    public boolean isSmallLeap() {
        return false;
    }

    @Override
    public int getTotalLengthInDays() {
        return days;
    }

    @Override
    public long getDurationInTime() {
        return days*BasicCalendar.DAY;
    }

    @Override
    public int getCountAsPeriods() {
        return 1;
    }

    @Override
    public List<PeriodType> getSubPeriods() {
        return null;
    }
}
class LeapMonth implements PeriodType{
    int days;
    Months month;

    public LeapMonth(int days, Months month) {
        this.days = days;
        this.month = month;
    }

    @Override
    public String getName() {
        return month.getName();
    }

    @Override
    public int getTotalLengthInDays() {
        return days;
    }

    @Override
    public long getDurationInTime() {
        return days*BasicCalendar.DAY;
    }

    @Override
    public boolean isBigLeap() {
        return false;
    }

    @Override
    public boolean isSmallLeap() {
        return true;
    }

    @Override
    public String getShortName() {
        return month.getShortName();
    }

    @Override
    public int getCountAsPeriods() {
        return 1;
    }

    @Override
    public List<PeriodType> getSubPeriods() {
        return null;
    }

    @Override
    public String toString() {
        return month.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
