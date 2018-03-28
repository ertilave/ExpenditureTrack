package com.et.main.commons;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;

import com.google.gson.Gson;

public class Util {
	
	public static BigDecimal getAmount(String s) {
		try {
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setGroupingSeparator('.');
			symbols.setDecimalSeparator(',');
			String pattern = "#,##0.0#";
			//DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");
			DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
			decimalFormat.setParseBigDecimal(true);
			decimalFormat.setNegativePrefix("+");
			BigDecimal result = (BigDecimal) decimalFormat.parse(s);
			return result;
		} catch (ParseException e) {
			
		} 
		return new BigDecimal(0);
	}
	
	private static final HashMap<String,Integer> monthMap;
	static {
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		map.put("Ocak", 1);
		map.put("Þubat", 2);
		map.put("Mart", 3);
		map.put("Nisan", 4);
		map.put("Mayýs", 5);
		map.put("Haziran", 6);
		map.put("Temmuz", 7);
		map.put("Aðustos", 8);
		map.put("Eylül", 9);
		map.put("Ekim", 10);
		map.put("Kasým", 11);
		map.put("Aralýk", 12);
		monthMap = map;
	}
	
	public static LocalDate createTrxDate(String day,String month,String year) {
		int monthOrder = monthMap.get(month);
		return LocalDate.of(Integer.parseInt(year), monthOrder, Integer.parseInt(day));
	}
	
	public static String toJson(Object object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}
	
}
