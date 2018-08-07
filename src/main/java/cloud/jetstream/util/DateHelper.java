package cloud.jetstream.util;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Helper class for date formatting to use in queries
 */
@Service
public class DateHelper {

  /**
   * Converts date given in String format to java Date object
   * @param date Date given in yyyy-MM-dd format
   * @return Date object representing the date given as argument
   */
  public Date stringToDate(String date) {
      Date myDate = null;
      try {
          myDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
      } catch(ParseException e) {
          System.err.println("Error parsing date from get request");
          e.printStackTrace();
      }
      return myDate;
  }

  /**
   * Gets the end of a given date
   * @param date Date object
   * @return End of date argument
   */
  public Date getEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

  /**
   * Converts month given in String format to java Date object representing beginning of month
   * @param month Day of month in yyyy-MM format
   * @return Date object representing the beginning of the given smonth
   */
  public Date stringToMonthStart(String month) {
        Date myMonth = null;
        try {
            myMonth = new SimpleDateFormat("yyyy-MM").parse(month);
        } catch(ParseException e) {
            System.err.println("Error parsing month from get request");
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(myMonth);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

  /**
   * Gets the end of a given month
   * @param monthStart Date object with month to find the end of
   * @return Date object representing the end of the given month
   */
  public Date getEndOfMonth(Date monthStart) {
        if(monthStart == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(monthStart);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return getEnd(c.getTime());
    }
}
