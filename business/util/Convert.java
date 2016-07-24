package business.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Convert {
	public static final String DATE_PATTERN = "MM/dd/yyyy"; 

	public static LocalDate localDateForString(String date) {  //pattern: "MM/dd/yyyy"
		return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN));
	}
	
	public static String localDateAsString(LocalDate date) {  //pattern: "MM/dd/yyyy"
		return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
	}
	
}
