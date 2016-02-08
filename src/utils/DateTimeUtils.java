package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeUtils {
	
	
	public static String getDateNow(String dateformat){
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat(dateformat);
		return dateFormat.format(cal.getTime());
	}
	public static String getDateNow(){		
		return getDateNow("dd/MM/yyyy HH:mm:ss");
	}
}
