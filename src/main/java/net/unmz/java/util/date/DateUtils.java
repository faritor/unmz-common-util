package net.unmz.java.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Project Name: 常用工具类集合
 * 功能描述：时间处理工具类
 *
 * @author Faritor
 * @version 1.0
 * @date 2017-11-06 18:51
 * @since JDK 1.8
 */
public class DateUtils {

    /**
     * 要用到的DATE Format的定义
     */
    public static final String DATE_FORMAT_DATE_ONLY = "yyyy-MM-dd"; // 年/月/日
    public static final String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss"; // 年/月/日
    public static final String DATE_FORMAT_DATETIME_14 = "yyyyMMddHHmmss"; // 年/月/日
    public static final String SHORT_DATE_FORMAT = "yyyyMMdd";
    public static final String HMS_FORMAT = "HH:mm:ss";


    private static DateUtils dateUtil = new DateUtils();

    private DateUtils() {

    }

    public static DateUtils getInstance() {
        return dateUtil;
    }

    /**
     * Base ISO 8601 Date format yyyyMMdd i.e., 20021225 for the 25th day of
     * December in the year 2002
     */
    public static final String ISO_DATE_FORMAT = "yyyyMMdd";

    /**
     * Expanded ISO 8601 Date format yyyy-MM-dd i.e., 2002-12-25 for the 25th
     * day of December in the year 2002
     */
    public static final String ISO_EXPANDED_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * Expanded ISO 8601 Date format yyyy-MM-dd HH:mm  2002-12-25 13:45 for the 25th
     * day of December in the year 2002
     */
    public static final String ISO_SHORT_DATE_FORMAT = "yyyy-MM-dd HH:mm";

    /**
     * yyyy-MM-dd hh:mm:ss
     */
    public static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy年MM月dd日
     */
    public static final String CHINESE_EXPANDED_DATE_FORMAT = "yyyy年MM月dd日";
    /**
     * yyyy/mm/dd；mm/dd；点:分:秒（12/24小时制）
     */
    public static final String TAIWAN_DATE_FORMAT = "yyyy/MM/ddHHmm";

    /**
     * Default lenient setting for getDate.
     */
    private static boolean LENIENT_DATE = false;

    /**
     * 根据时间变量返回时间字符串 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static Date dateFormart(Date date) {

        return stringToDate(dateToString(date, ISO_EXPANDED_DATE_FORMAT));
    }

    /**
     * 暂时不用
     *
     * @param JD
     * @return
     */
    protected static final float normalizedJulian(float JD) {

        float f = Math.round(JD + 0.5f) - 0.5f;

        return f;
    }

    public static String getGrpEndDate(String effdate, String loadEndDate,
                                       String loadBeginDate) {
        long qoutDays = getDateQuot(effdate, loadEndDate);
        String endDate = "";
        if (qoutDays >= 365) {

            endDate = dateToString(
                    addDay(addYear(stringToDate(loadBeginDate), 1), -1),
                    "yyyy-MM-dd");
        } else {
            endDate = loadEndDate;
        }
        return endDate;
    }

    /**
     * 浮点值转换成日期格式<br>
     * 暂时不用 Returns the Date from a julian. The Julian date will be converted to
     * noon GMT, such that it matches the nearest half-integer (i.e., a julian
     * date of 1.4 gets changed to 1.5, and 0.9 gets changed to 0.5.)
     *
     * @param JD
     *            the Julian date
     * @return the Gregorian date
     */
    public static final Date toDate(float JD) {

        /*
         * To convert a Julian Day Number to a Gregorian date, assume that it is
         * for 0 hours, Greenwich time (so that it ends in 0.5). Do the
         * following calculations, again dropping the fractional part of all
         * multiplicatons and divisions. Note: This method will not give dates
         * accurately on the Gregorian Proleptic Calendar, i.e., the calendar
         * you get by extending the Gregorian calendar backwards to years
         * earlier than 1582. using the Gregorian leap year rules. In
         * particular, the method fails if Y<400.
         */
        float Z = (normalizedJulian(JD)) + 0.5f;
        float W = (int) ((Z - 1867216.25f) / 36524.25f);
        float X = (int) (W / 4f);
        float A = Z + 1 + W - X;
        float B = A + 1524;
        float C = (int) ((B - 122.1) / 365.25);
        float D = (int) (365.25f * C);
        float E = (int) ((B - D) / 30.6001);
        float F = (int) (30.6001f * E);
        int day = (int) (B - D - F);
        int month = (int) (E - 1);

        if (month > 12) {
            month = month - 12;
        }

        int year = (int) (C - 4715); // (if Month is January or February) or
        // C-4716 (otherwise)

        if (month > 2) {
            year--;
        }

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1); // damn 0 offsets
        c.set(Calendar.DATE, day);

        return c.getTime();
    }

    /**
     * Returns the days between two dates. Positive values indicate that the
     * second date is after the first, and negative values indicate, well, the
     * opposite. Relying on specific times is problematic.
     *
     * @param early
     *            the "first date"
     * @param late
     *            the "second date"
     * @return the days between the two dates
     */
    public static final int daysBetween(Date early, Date late) {
        java.util.GregorianCalendar calst = new java.util.GregorianCalendar();
        java.util.GregorianCalendar caled = new java.util.GregorianCalendar();
        calst.setTime(early);
        caled.setTime(late);
        // 设置时间为0时
        calst.set(java.util.GregorianCalendar.HOUR_OF_DAY, 0);
        calst.set(java.util.GregorianCalendar.MINUTE, 0);
        calst.set(java.util.GregorianCalendar.SECOND, 0);
        caled.set(java.util.GregorianCalendar.HOUR_OF_DAY, 0);
        caled.set(java.util.GregorianCalendar.MINUTE, 0);
        caled.set(java.util.GregorianCalendar.SECOND, 0);

        // 得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000 / 3600 / 24) - (int) (calst
                .getTime().getTime() / 1000 / 3600 / 24));
        return days;
    }

    /**
     * Returns the days between two dates. Positive values indicate that the
     * second date is after the first, and negative values indicate, well, the
     * opposite.
     *
     * @param early
     * @param late
     * @return the days between two dates.
     */
    public static final int daysBetween(Calendar early, Calendar late) {

        return (int) (toJulian(late) - toJulian(early));
    }

    /**
     * Return a Julian date based on the input parameter. This is based from
     * calculations found at <a
     * href="http://quasar.as.utexas.edu/BillInfo/JulianDatesG.html">Julian Day
     * Calculations (Gregorian Calendar)</a>, provided by Bill Jeffrys.
     *
     * @param c
     *            a calendar instance
     * @return the julian day number
     */
    public static final float toJulian(Calendar c) {

        int Y = c.get(Calendar.YEAR);
        int M = c.get(Calendar.MONTH);
        int D = c.get(Calendar.DATE);
        int A = Y / 100;
        int B = A / 4;
        int C = 2 - A + B;
        float E = (int) (365.25f * (Y + 4716));
        float F = (int) (30.6001f * (M + 1));
        float JD = C + D + E + F - 1524.5f;

        return JD;
    }

    /**
     * 暂时不用 Return a Julian date based on the input parameter. This is based
     * from calculations found at <a
     * href="http://quasar.as.utexas.edu/BillInfo/JulianDatesG.html">Julian Day
     * Calculations (Gregorian Calendar)</a>, provided by Bill Jeffrys.
     *
     * @param date
     * @return the julian day number
     */
    public static final float toJulian(Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return toJulian(c);
    }

    /**
     * 日期增加
     *
     * @param isoString
     *            日期字符串
     * @param fmt
     *            格式
     * @param field
     *            年/月/日 Calendar.YEAR/Calendar.MONTH/Calendar.DATE
     * @param amount
     *            增加数量
     * @return
     * @throws ParseException
     */
    public static final String dateIncrease(String isoString, String fmt,
                                            int field, int amount) {

        try {
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(stringToDate(isoString, fmt, true));
            cal.add(field, amount);

            return dateToString(cal.getTime(), fmt);

        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Time Field Rolling function. Rolls (up/down) a single unit of time on the
     * given time field.
     *
     * @param isoString
     * @param field
     *            the time field.
     * @param up
     *            Indicates if rolling up or rolling down the field value.
     * @param fmt
     *            use formating char's
     * @exception ParseException
     *                if an unknown field value is given.
     */
    public static final String roll(String isoString, String fmt, int field,
                                    boolean up) throws ParseException {

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(stringToDate(isoString, fmt));
        cal.roll(field, up);

        return dateToString(cal.getTime(), fmt);
    }

    /**
     * Time Field Rolling function. Rolls (up/down) a single unit of time on the
     * given time field.
     *
     * @param isoString
     * @param field
     *            the time field.
     * @param up
     *            Indicates if rolling up or rolling down the field value.
     * @exception ParseException
     *                if an unknown field value is given.
     */
    public static final String roll(String isoString, int field, boolean up)
            throws ParseException {

        return roll(isoString, DATETIME_PATTERN, field, up);
    }

    /**
     * 字符串转换为日期java.util.Date
     *
     * @param dateText
     *            字符串
     * @param format
     *            日期格式
     * @param lenient
     *            日期越界标志
     * @return
     */
    public static Date stringToDate(String dateText, String format,
                                    boolean lenient) {

        if (dateText == null) {

            return null;
        }

        DateFormat df = null;

        try {

            if (format == null) {
                df = new SimpleDateFormat();
            } else {
                df = new SimpleDateFormat(format);
            }

            // setLenient avoids allowing dates like 9/32/2001
            // which would otherwise parse to 10/2/2001
            df.setLenient(false);

            return df.parse(dateText);
        } catch (ParseException e) {

            return null;
        }
    }

    /**
     * 字符串转换为日期java.util.Date
     *
     * @param dateString
     *            字符串
     * @param format
     *            日期格式
     * @return
     */
    public static Date stringToDate(String dateString, String format) {

        return stringToDate(dateString, format, LENIENT_DATE);
    }

    /**
     * 字符串转换为日期java.util.Date
     *
     * @param dateString
     *            字符串
     */
    public static Date stringToDate(String dateString) {
        if (!"".equals(dateString) && dateString != null) {
            // ISO_DATE_FORMAT = "yyyyMMdd";
            if (dateString.trim().length() == 8) {
                return stringToDate(dateString, ISO_DATE_FORMAT, LENIENT_DATE);
            } else if (dateString.trim().length() == 10) {
                // ISO_EXPANDED_DATE_FORMAT = "yyyy-MM-dd";
                return stringToDate(dateString, ISO_EXPANDED_DATE_FORMAT,
                        LENIENT_DATE);
            } else if (dateString.trim().length() == 19) {
                // DATETIME_PATTERN = "yyyy-MM-dd hh:mm:ss";
                return stringToDate(dateString, DATETIME_PATTERN, LENIENT_DATE);
            } else if (dateString.trim().length() == 11) {
                // CHINESE_EXPANDED_DATE_FORMAT = "yyyy年MM月dd日";
                return stringToDate(dateString, CHINESE_EXPANDED_DATE_FORMAT,
                        LENIENT_DATE);
            }
        }
        return null;
    }

    /**
     * 根据时间变量返回时间字符串
     *
     * @return 返回时间字符串
     * @param pattern
     *            时间字符串样式
     * @param date
     *            时间变量
     */
    public static String dateToString(Date date, String pattern) {

        if (date == null) {

            return null;
        }

        try {

            SimpleDateFormat sfDate = new SimpleDateFormat(pattern);
            sfDate.setLenient(false);

            return sfDate.format(date);
        } catch (Exception e) {

            return null;
        }
    }

    /**
     * 根据时间变量返回时间字符串 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        return dateToString(date, ISO_EXPANDED_DATE_FORMAT);
    }

    /**
     * 返回当前时间
     *
     * @return 返回当前时间
     */
    public static Date getCurrentDateTime() {
        java.util.Calendar calNow = java.util.Calendar.getInstance();
        java.util.Date dtNow = calNow.getTime();

        return dtNow;
    }

    /**
     * 返回当前日期字符串
     *
     * @param pattern
     *            日期字符串样式
     * @return
     */
    public static String getCurrentDateString(String pattern) {
        return dateToString(getCurrentDateTime(), pattern);
    }

    /**
     * 返回当前日期字符串 yyyy-MM-dd
     *
     * @return
     */
    public static String getCurrentDateString() {
        return dateToString(getCurrentDateTime(), ISO_EXPANDED_DATE_FORMAT);
    }

    /**
     * 返回当前日期+时间字符串 yyyy-MM-dd hh:mm:ss
     *
     * @param date
     * @return
     */
    public static String dateToStringWithTime(Date date) {

        return dateToString(date, DATETIME_PATTERN);
    }

    /**
     * 日期增加-按日增加
     *
     * @param date
     * @param days
     * @return java.util.Date
     */
    public static Date dateIncreaseByDay(Date date, int days) {

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

    /**
     * 日期增加-按月增加
     *
     * @param date
     * @param mnt
     * @return java.util.Date
     */
    public static Date dateIncreaseByMonth(Date date, int mnt) {

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, mnt);

        return cal.getTime();
    }

    /**
     * 日期增加-按年增加
     *
     * @param date
     * @param mnt
     * @return java.util.Date
     */
    public static Date dateIncreaseByYear(Date date, int mnt) {

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, mnt);

        return cal.getTime();
    }

    /**
     * 日期增加-按年增加
     *
     * @param date
     * @param mnt
     * @return java.util.Date
     */
    public static String dateIncreaseByYearforString(String date, int mnt) {
        Date date1 = stringToDate(date);
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date1);
        cal.add(Calendar.YEAR, mnt);
        String ss = dateToString(cal.getTime(), ISO_DATE_FORMAT);
        return dateIncreaseByDay(ss, -1);
    }

    /**
     * 日期增加
     *
     * @param date
     *            日期字符串 yyyy-MM-dd
     * @param days
     * @return 日期字符串 yyyy-MM-dd
     */
    public static String dateIncreaseByDay(String date, int days) {
        return dateIncreaseByDay(date, ISO_DATE_FORMAT, days);
    }

    /**
     * 日期增加
     *
     * @param date
     *            日期字符串
     * @param fmt
     *            日期格式
     * @param days
     * @return
     */
    public static String dateIncreaseByDay(String date, String fmt, int days) {
        return dateIncrease(date, fmt, Calendar.DATE, days);
    }

    /**
     * 日期字符串格式转换
     *
     * @param dateString
     *            日期字符串
     * @param desfmt
     *            目标日期格式
     * @return
     */
    public static String stringToString(String dateString, String desfmt) {
        // ISO_DATE_FORMAT = "yyyyMMdd";
        if (dateString.trim().length() == 8) {
            return stringToString(dateString, ISO_DATE_FORMAT, desfmt);
        } else if (dateString.trim().length() == 10) {
            // ISO_EXPANDED_DATE_FORMAT = "yyyy-MM-dd";
            return stringToString(dateString, ISO_EXPANDED_DATE_FORMAT, desfmt);
        } else if (dateString.trim().length() == 19) {
            // DATETIME_PATTERN = "yyyy-MM-dd hh:mm:ss";
            return stringToString(dateString.substring(0, 10),
                    ISO_EXPANDED_DATE_FORMAT, desfmt);
        } else if (dateString.trim().length() == 11) {
            // CHINESE_EXPANDED_DATE_FORMAT = "yyyy年MM月dd日";
            return stringToString(dateString, CHINESE_EXPANDED_DATE_FORMAT,
                    desfmt);
        }
        return null;
    }

    /**
     * 日期字符串格式转换
     *
     * @param src
     *            日期字符串
     * @param srcfmt
     *            源日期格式
     * @param desfmt
     *            目标日期格式
     * @return
     */
    public static String stringToString(String src, String srcfmt, String desfmt) {
        return dateToString(stringToDate(src, srcfmt), desfmt);
    }

    /**
     * yyyy年MM月dd日至yyyy年MM月dd日 获取止期
     *
     * @param src
     *            日期字符串
     * @param desfmt
     *            目标日期格式
     * @return
     */
    public static String getPolicyEndDate(String src, String srcfmt,
                                          String desfmt) {
        if (src == null || src.trim().equals("") || src.length() < 23)
            return null;
        src = src.substring(12, 23);
        return stringToString(src, srcfmt, desfmt);
    }

    /**
     * yyyy年MM月dd日至yyyy年MM月dd日 获取止期
     *
     * @param src
     *            日期字符串
     * @param desfmt
     *            目标日期格式
     * @return
     */
    public static String getPolicyEffDate(String src, String srcfmt,
                                          String desfmt) {
        if (src == null || src.trim().equals("") || src.length() < 23)
            return null;
        src = src.substring(0, 11);
        return stringToString(src, srcfmt, desfmt);
    }

    /**
     * yyyy年MM月dd日
     *
     * @param src
     *            日期字符串
     * @return
     */
    public static String getChineseDate(String src) {
        String renStr = null;
        if (!"".equals(src) && src != null) {
            renStr = src.substring(0, 4) + "年" + src.substring(5, 6) + "月"
                    + src.substring(6, 8) + "日";
        }
        return renStr;
    }

    /**
     * 两个日期之间的天数
     *
     * @param time1
     * @param time2
     * @return
     */
    public static long getDateQuot(String time1, String time2) {
        long quot = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = ft.parse(time1);
            Date date2 = ft.parse(time2);
            quot = date2.getTime() - date1.getTime();
            quot = quot / 1000 / 60 / 60 / 24;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return quot;
    }

    /**
     * 在日期上加指定的年数
     *
     * @param
     * @param
     * @return
     */
    public static Date addYear(Date date1, int addYear) {
        Date resultDate = null;
        Calendar c = Calendar.getInstance();
        c.setTime(date1); // 设置当前日期
        c.add(Calendar.YEAR, addYear); // 日期加1
        resultDate = c.getTime(); // 结果
        return resultDate;
    }

    /**
     * 在日期上加指定的月数
     *
     * @param
     * @param
     * @return
     */
    public static Date addMonth(Date date1, int addMonth) {
        Date resultDate = null;
        Calendar c = Calendar.getInstance();
        c.setTime(date1); // 设置当前日期
        c.add(Calendar.MONTH, addMonth); // 日期加1
        resultDate = c.getTime(); // 结果
        return resultDate;
    }

    /**
     * 在日期上加指定的天数,返回Date类型
     *
     * @param
     * @param
     * @return
     */
    public static Date addDay(Date date1, int addDay) {
        Date resultDate = null;
        Calendar c = Calendar.getInstance();
        c.setTime(date1); // 设置当前日期
        c.add(Calendar.DATE, addDay); // 日期加1
        resultDate = c.getTime(); // 结果
        return resultDate;
    }

    /**
     * 在日期上加指定的天数,返回String类型
     *
     * @param
     * @param
     * @return
     */
    public static String addDayByString(String date, int addDay) {
        String retDate = "";
        // 空值校验
        if (date == null || "".equals(date)
                || "".equals(date.replaceAll("-", "").trim())) {
            return retDate;
        }
        Date date1 = stringToDate(date);
        Calendar c = Calendar.getInstance();
        c.setTime(date1); // 设置当前日期
        c.add(Calendar.DATE, addDay); // 日期加1
        Date resultDate = c.getTime(); // 结果
        return dateToString(resultDate);
    }

    /**
     * 获取月份的第一天
     * @param date
     * @return
     */
    public static String getFirstDayOfMonth(Date date){
        Calendar   ca   =   Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH,   1);
        Date   firstDate   =   ca.getTime();
        return dateToString(firstDate);
    }

    /**
     * 获取月份的最后一天
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date){
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH,   1);
        Date firstDate = ca.getTime();
        return addDay(addMonth(firstDate, 1), -1);
    }

    /**
     * 获取日期最早时间，如传入2014-12-26，返回2014-12-26 0:00:00
     * @param date
     * @return
     */
    public static Date getDateStart(Date date){
        Calendar   ca   =   Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.HOUR, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        return ca.getTime();
    }

    /**
     * 获取日期最晚时间，如传入2014-12-26，返回2014-12-26 23:59:59
     * @param date
     * @return
     */
    public static Date getDateEnd(Date date){
        Calendar   ca   =   Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.HOUR, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        return  ca.getTime();
    }



    public ArrayList<Date> getDateList(Date startTime, Date endTime) {
        ArrayList<Date> timeList = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        timeList.add(cal.getTime());
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(startTime);
        endCalendar.setTime(endTime);
        long startMill = startCalendar.getTimeInMillis();
        long endMill = endCalendar.getTimeInMillis();
        while (startMill < endMill) {
            startMill += 24 * 3600 * 1000;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startMill);
            timeList.add(calendar.getTime());
        }
        return timeList;
    }

    /**
     * 获取前后七天
     * @param num   7：后七天     -7：前七天
     * @return
     */
    public static String getPreOrNextDay(Date date,int num) {
        Calendar   ca   =   Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.DAY_OF_MONTH,  num);//此处要用add方法
        Date   firstDate   =   ca.getTime();
        return dateToString(firstDate);
    }

//    /**
//     * 把字符串转成日期类型
//     * 输入的日期格式:yyyy-MM-dd HH:mm:ss
//     *
//     * @param str 日期字符串
//     * @return 转换后的日期
//     * @throws ParseException
//     * @see LocalDateTime
//     */
//    public static LocalDateTime parseLocalDateTime(String str) throws ParseException {
//        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
//    }
//
//    /**
//     * 把字符串转成日期类型
//     * 输入的日期格式:yyyy-MM-dd
//     *
//     * @param str 日期字符串
//     * @return 转换后的日期
//     * @throws ParseException
//     * @see LocalDate
//     */
//    public static LocalDate parseLocalDate(String str) throws ParseException {
//        return LocalDate.parse(str, DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_ONLY));
//    }
//
//    /**
//     * 获得当前的日期毫秒
//     *
//     * @return 当前毫秒数
//     */
//    public static long nowTimeMillis() {
//        return Clock.systemDefaultZone().millis();
//    }
//
//    /**
//     * 获取从1970年到现在的秒数
//     *
//     * @return 秒数
//     */
//    public static long nowEpochSecond() {
//        return Clock.systemDefaultZone().instant().getEpochSecond();
//    }
//
//    /**
//     * 获得当前的时间戳
//     *
//     * @return 时间点
//     */
//    public static Instant nowTimestamp() {
//        return Instant.now(Clock.systemDefaultZone());
//    }
//
//    /**
//     * yyyy-MM-dd 当前日期
//     *
//     * @return 当前日期 yyyy-MM-dd
//     */
//    public static String getCurrentDate() {
//        return LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_ONLY));
//    }
//
//    /**
//     * 获取当前日期时间 yyyy-MM-dd HH:mm:ss
//     *
//     * @return 获取当前日期时间 yyyy-MM-dd HH:mm:ss
//     */
//    public static String getCurrentDateTime() {
//        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
//    }
//
//    /**
//     * 获取当前日期时间
//     *
//     * @param format 格式字符串
//     * @return 获取当前日期时间
//     */
//    public static String getCurrentDateTime(String format) {
//        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
//    }
//
//    /**
//     * 获取当前时间 HH:mm:ss
//     *
//     * @return 获取当前时间 HH:mm:ss
//     */
//    public static String getCurrentTime() {
//        return LocalTime.now().format(DateTimeFormatter.ofPattern(HMS_FORMAT));
//    }
//
//    /**
//     * yyyy-MM-dd 格式化传入日期
//     *
//     * @param date 日期
//     * @return yyyy-MM-dd 日期
//     */
//    public static String formaterDate(LocalDate date) {
//        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_ONLY));
//    }
//
//    /**
//     * yyyyMMdd 格式化传入日期
//     *
//     * @param date 传入的日期
//     * @return yyyyMMdd 字符串
//     */
//    public static String formaterDateToyyyyMMdd(LocalDate date) {
//        return date.format(DateTimeFormatter.ofPattern(SHORT_DATE_FORMAT));
//    }
//
//    /**
//     * 将localDateTime 格式化成yyyy-MM-dd HH:mm:ss
//     *
//     * @param dateTime
//     * @return
//     */
//    public static String formaterLocalDateTime(LocalDateTime dateTime) {
//        return dateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
//    }
//
//    /**
//     * 将localDateTime 格式化成yyyyMMddHHmmss
//     *
//     * @param dateTime
//     * @return
//     */
//    public static String formaterLocalDateTime14(LocalDateTime dateTime) {
//        return dateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME_14));
//    }
//
//    /**
//     * yyyy-MM-dd HH:mm:ss
//     * 时间点转换成日期字符串
//     *
//     * @param instant 时间点.
//     * @return 日期时间 yyyy-MM-dd HH:mm:ss
//     */
//    public static String parseInstantToDataStr(Instant instant) throws ParseException {
//        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
//    }
//
//    /**
//     * 得到时间戳格式字串
//     *
//     * @param date 长日期
//     * @return UTC 格式的时间戳字符串
//     */
//    public static String getTimeStampStr(LocalDateTime date) {
//        return date.toInstant(ZoneOffset.UTC).toString();
//    }
//
//    /**
//     * 计算 second 秒后的时间
//     *
//     * @param date   长日期
//     * @param second 需要增加的秒数
//     * @return 增加后的日期
//     */
//    public static LocalDateTime addSecond(LocalDateTime date, int second) {
//        return date.plusSeconds(second);
//    }
//
//    /**
//     * 计算 minute 分钟后的时间
//     *
//     * @param date   长日期
//     * @param minute 需要增加的分钟数
//     * @return 增加后的日期
//     */
//    public static LocalDateTime addMinute(LocalDateTime date, int minute) {
//        return date.plusMinutes(minute);
//    }
//
//    /**
//     * 计算 hour 小时后的时间
//     *
//     * @param date 长日期
//     * @param hour 增加的小时数
//     * @return 增加后的日期
//     */
//    public static LocalDateTime addHour(LocalDateTime date, int hour) {
//        return date.plusHours(hour);
//    }
//
//    /**
//     * 计算 day 天后的时间
//     *
//     * @param date 长日期
//     * @param day  增加的天数
//     * @return 增加后的日期
//     */
//    public static LocalDateTime addDay(LocalDateTime date, int day) {
//        return date.plusDays(day);
//    }
//
//    /**
//     * 计算 month 月后的时间
//     *
//     * @param date  长日期
//     * @param month 需要增加的月数
//     * @return 增加后的日期
//     */
//    public static LocalDateTime addMoth(LocalDateTime date, int month) {
//        return date.plusMonths(month);
//    }
//
//    /**
//     * 计算 year 年后的时间
//     *
//     * @param date 长日期
//     * @param year 需要增加的年数
//     * @return 增加后的日期
//     */
//    public static LocalDateTime addYear(LocalDateTime date, int year) {
//        return date.plusYears(year);
//    }
//
//    /**
//     * 得到day的起始时间点。
//     * 一天开始的时间为　0:0:0
//     *
//     * @param date 短日期
//     * @return yyyy-MM-dd HH:mm:ss 字符串
//     */
//    public static String getDayStart(LocalDate date) {
//        LocalDateTime localDateTime = LocalDateTime.of(date, LocalTime.of(0, 0, 0));
//        return localDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
//    }
//
//    /**
//     * 得到day的结束时间点。
//     * 一天结束的时间为 23:59:59
//     *
//     * @param date date 短日期
//     * @return yyyy-MM-dd HH:mm:ss 字符串
//     */
//    public static String getDayEnd(LocalDate date) {
//        LocalDateTime localDateTime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));
//        return localDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
//    }
//
//    /**
//     * 根据日期字符串获取时间戳
//     *
//     * @param dateStr yyyy-MM-dd HH:mm:ss
//     * @return 时间信息
//     */
//    public static Instant parseDataStrToInstant(String dateStr) throws ParseException {
//        return parseLocalDateTime(dateStr).toInstant(ZoneOffset.UTC);
//    }
//
//    /**
//     * 取得两个日期之间相差的年数
//     * getYearsBetween
//     *
//     * @param t1 开始时间
//     * @param t2 结果时间
//     * @return t1到t2间的年数，如果t2在 t1之后，返回正数，否则返回负数
//     */
//    public static long getYearsBetween(LocalDate t1, LocalDate t2) {
//        return t1.until(t2, ChronoUnit.YEARS);
//    }
//
//    /**
//     * 取得两个日期之间相差的日数
//     *
//     * @param t1 开始日期
//     * @param t2 结束日期
//     * @return t1到t2间的日数，如果t2 在 t1之后，返回正数，否则返回负数
//     */
//    public static long getDaysBetween(LocalDate t1, LocalDate t2) {
//        return t1.until(t2, ChronoUnit.DAYS);
//    }
//
//    /**
//     * 取得两个日期之间相差的月数
//     *
//     * @param t1 开始日期
//     * @param t2 结束日期
//     * @return t1到t2间的日数，如果t2 在 t1之后，返回正数，否则返回负数
//     */
//    public static long getMonthsBetween(LocalDate t1, LocalDate t2) {
//        return t1.until(t2, ChronoUnit.MONTHS);
//    }
//
//    /**
//     * 取得两个日期之间相差的小时数
//     *
//     * @param t1 开始长日期
//     * @param t2 结束长日期
//     * @return t1到t2间的日数，如果t2 在 t1之后，返回正数，否则返回负数
//     */
//    public static long getHoursBetween(LocalDateTime t1, LocalDateTime t2) {
//        return t1.until(t2, ChronoUnit.HOURS);
//    }
//
//    /**
//     * 取得两个日期之间相差的秒数
//     *
//     * @param t1 开始长日期
//     * @param t2 结束长日期
//     * @return t1到t2间的日数，如果t2 在 t1之后，返回正数，否则返回负数
//     */
//    public static long getSecondsBetween(LocalDateTime t1, LocalDateTime t2) {
//        return t1.until(t2, ChronoUnit.SECONDS);
//    }
//
//    /**
//     * 取得两个日期之间相差的分钟
//     *
//     * @param t1 开始长日期
//     * @param t2 结束长日期
//     * @return t1到t2间的日数，如果t2 在 t1之后，返回正数，否则返回负数
//     */
//    public static long getMinutesBetween(LocalDateTime t1, LocalDateTime t2) {
//        return t1.until(t2, ChronoUnit.MINUTES);
//    }
//
//    /**
//     * 获取今天是星期几
//     *
//     * @return 返回今天是周几
//     * @see DayOfWeek
//     */
//    public static DayOfWeek getWeek() {
//        return LocalDate.now().getDayOfWeek();
//    }
//
//    /**
//     * 判断时间是否在制定的时间段之类
//     *
//     * @param date  需要判断的时间
//     * @param start 时间段的起始时间
//     * @param end   时间段的截止时间
//     * @return true or false
//     */
//    public static boolean isBetween(LocalDateTime date, LocalDateTime start, LocalDateTime end) {
//        if (date == null || start == null || end == null) {
//            throw new IllegalArgumentException("日期不能为空");
//        }
//        return date.isAfter(start) && date.isBefore(end);
//    }
//
//    /**
//     * 得到传入日期,周起始时间
//     * 这个方法定义:周一为一个星期开始的第一天
//     *
//     * @param date 日期
//     * @return 返回一周的第一天
//     */
//    public static LocalDate getWeekStart(LocalDate date) {
//        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
//    }
//
//    /**
//     * 得到当前周截止时间
//     * 这个方法定义:周日为一个星期开始的最后一天
//     *
//     * @param date 日期
//     * @return 返回一周的最后一天
//     */
//    public static LocalDate getWeekEnd(LocalDate date) {
//        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
//    }
//
//    /**
//     * 得到month的终止时间点.
//     *
//     * @param date 日期
//     * @return 传入的日期当月的结束日期
//     */
//    public static LocalDate getMonthEnd(LocalDate date) {
//        return date.with(TemporalAdjusters.lastDayOfMonth());
//    }
//
//    /**
//     * 得到当月起始时间
//     *
//     * @param date 日期
//     * @return 传入的日期当月的开始日期
//     */
//    public static LocalDate getMonthStart(LocalDate date) {
//
//        return date.with(TemporalAdjusters.firstDayOfMonth());
//    }
//
//    /**
//     * 得到当前年起始时间
//     *
//     * @param date 日期
//     * @return 传入的日期当年的开始日期
//     */
//    public static LocalDate getYearStart(LocalDate date) {
//        return date.with(TemporalAdjusters.firstDayOfYear());
//    }
//
//    /**
//     * 得到当前年最后一天
//     *
//     * @param date 日期
//     * @return 传入的日期当年的结束日期
//     */
//    public static LocalDate getYearEnd(LocalDate date) {
//        return date.with(TemporalAdjusters.lastDayOfYear());
//    }
//
//    /**
//     * 取得季度第一天
//     *
//     * @param date 日期
//     * @return 传入的日期当季的开始日期
//     */
//    public static LocalDate getSeasonStart(LocalDate date) {
//        return getSeasonDate(date)[0];
//    }
//
//    /**
//     * 取得季度最后一天
//     *
//     * @param date 日期
//     * @return 传入的日期当季的结束日期
//     */
//    public static LocalDate getSeasonEnd(LocalDate date) {
//        return getSeasonDate(date)[2].with(TemporalAdjusters.lastDayOfMonth());
//    }
//
//    /**
//     * 取得季度月的第一天
//     *
//     * @param date 日期
//     * @return 返回一个当前季度月的数组
//     */
//    public static LocalDate[] getSeasonDate(LocalDate date) {
//        LocalDate[] season = new LocalDate[3];
//        int nSeason = getSeason(date);
//        int year = date.getYear();
//        if (nSeason == 1) {// 第一季度
//            season[0] = LocalDate.of(year, Month.JANUARY, 1);
//            season[1] = LocalDate.of(year, Month.FEBRUARY, 1);
//            season[2] = LocalDate.of(year, Month.MARCH, 1);
//        } else if (nSeason == 2) {// 第二季度
//            season[0] = LocalDate.of(year, Month.APRIL, 1);
//            season[1] = LocalDate.of(year, Month.MAY, 1);
//            season[2] = LocalDate.of(year, Month.JUNE, 1);
//        } else if (nSeason == 3) {// 第三季度
//            season[0] = LocalDate.of(year, Month.JULY, 1);
//            season[1] = LocalDate.of(year, Month.AUGUST, 1);
//            season[2] = LocalDate.of(year, Month.SEPTEMBER, 1);
//        } else if (nSeason == 4) {// 第四季度
//            season[0] = LocalDate.of(year, Month.OCTOBER, 1);
//            season[1] = LocalDate.of(year, Month.NOVEMBER, 1);
//            season[2] = LocalDate.of(year, Month.DECEMBER, 1);
//        }
//        return season;
//    }
//
//    /**
//     * 判断当前期为每几个季度
//     *
//     * @param date 日期
//     * @return 1 第一季度 2 第二季度 3 第三季度 4 第四季度
//     */
//    public static int getSeason(LocalDate date) {
//
//        int season = 0;
//
//        Month month = date.getMonth();
//
//        switch (month) {
//            case JANUARY:
//            case FEBRUARY:
//            case MARCH:
//                season = 1;
//                break;
//            case APRIL:
//            case MAY:
//            case JUNE:
//                season = 2;
//                break;
//            case JULY:
//            case AUGUST:
//            case SEPTEMBER:
//                season = 3;
//                break;
//            case OCTOBER:
//            case NOVEMBER:
//            case DECEMBER:
//                season = 4;
//                break;
//            default:
//                break;
//        }
//        return season;
//    }
//
//    /**
//     * 获取当前时间的前多少,yyyy-MM-dd
//     *
//     * @param days 天数
//     * @return yyyy-MM-dd
//     */
//    public static String subDays(int days) {
//        return LocalDate.now().minusDays(days).format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_ONLY));
//    }
//
//    /**
//     * 判断开始时间和结束时间，是否超出了当前时间的一定的间隔数限制 如：开始时间和结束时间，不能超出距离当前时间90天
//     *
//     * @param startDate 开始时间
//     * @param endDate   结束时间按
//     * @param interval  间隔数
//     * @return true or false
//     */
//    public static boolean isOverIntervalLimit(LocalDate startDate, LocalDate endDate, int interval) {
//        return getDaysBetween(startDate, endDate) >= interval;
//    }
//
//    /**
//     * 获取昨天的日期 格式串:yyyy-MM-dd
//     *
//     * @return yyyy-MM-dd
//     */
//    public static String getYesterday() {
//        return getYesterday(LocalDate.now());
//    }
//
//    /**
//     * 获取昨天的日期 格式串:yyyy-MM-dd
//     *
//     * @return yyyy-MM-dd
//     */
//    public static String getYesterday(LocalDate date) {
//        return date.minusDays(1).format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_ONLY));
//    }
//
//    /**
//     * 上月第一天
//     *
//     * @return 日期
//     */
//    public static LocalDate getPreviousMonthFirstDay() {
//
//        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
//    }
//
//    /**
//     * 上月最后一天
//     *
//     * @return 日期
//     */
//    public static LocalDate getPreviousMonthLastDay() {
//        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
//    }
//
//    // 获得当天0点时间
//    public static Date getTimesmorning() {
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        return cal.getTime();
//    }
//
//
//    /**
//     * 获得当天近一周
//     * 最近一周，一周前的日期时间
//     *
//     * @return
//     */
//    public static LocalDateTime getAWeekFromNow() {
//        LocalDateTime date = LocalDateTime.now();
//        return date.plusWeeks(-1);//7天前
//    }
//
//    /**
//     * 获得当天近一月
//     * 最近一月，一个月前的日期时间
//     *
//     * @return
//     */
//    public static LocalDateTime getAMonthFromNow() {
//        LocalDateTime date = LocalDateTime.now();
//        return date.plusMonths(-1);//一个月前
//    }
//
//    /**
//     * 获得当天近三个月
//     * 最近三个月，三个月前的日期时间
//     *
//     * @return
//     */
//    public static LocalDateTime getThreeMonthFromNow() {
//        LocalDateTime date = LocalDateTime.now();
//        return date.plusMonths(-1);//一个月前
//    }
//
//    /**
//     * 获得当天近一年
//     * 最近一年，一年前的日期时间
//     *
//     * @return
//     */
//    public static LocalDateTime getAYearFromNow() {
//        LocalDateTime date = LocalDateTime.now();
//        return date.plusYears(-1);//一年前
//    }
//
//    @Override
//    public String toString() {
//        return super.toString();
//    }
//
//    /**
//     * 将时间戳转成yyyyMMdd HH:mm:ss字符串. <br/>
//     *
//     * @param timestamp
//     * @return
//     * @throws ParseException
//     */
//    public static String timestampToDateStr(String timestamp) throws ParseException {
//        Long timestampL = Long.parseLong(timestamp);
//        return parseInstantToDataStr(new Date(timestampL).toInstant());
//    }
//
//    /**
//     * 通过 起始时间算出中间的时间差值 格式为  某天某小时某分钟
//     *
//     * @param startTime
//     * @param endTime
//     * @return
//     */
//    public static String getDate(Timestamp startTime, Timestamp endTime) {
//        LocalDateTime start = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//        LocalDateTime end = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//        long min = DateUtils.getMinutesBetween(start, end);
//        String remainDays = "";
//        long day = min / 60 / 24;
//        long hour = min / 60 - day * 24;
//        long minutes = min - hour * 60 - day * 24 * 60;
//        if (day > 0)
//            remainDays += day + "天";
//        if (hour > 0 || (hour == 0 && minutes > 0))
//            remainDays += hour + "小时";
//        if (minutes > 0)
//            remainDays += minutes + "小时";
//        return remainDays;
//    }
}
