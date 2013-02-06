package br.com.companhiadesistemas.itimcustomreports.activites;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Util {
	
	public static String getSqlDate(Date date){
		SimpleDateFormat gmtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		gmtFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return gmtFormat.format(date);
	}

}
