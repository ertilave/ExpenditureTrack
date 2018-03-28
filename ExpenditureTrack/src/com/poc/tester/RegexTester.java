package com.poc.tester;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTester {

	public static void main(String[] args) {
		String stmt="120,55 TL'lik iþlemin 5 / 5 taksidi"; 
		//Pattern pattern = Pattern.compile("(\\d\\d)\\s(\\w{3,7})\\s(2018|2017)\\s(.{40})\\s([0123456789,\\.+]*)\\s?([0123456789,\\.]*\\s\\/\\s\\d*)?\\s?(\\d*)?",Pattern.UNICODE_CHARACTER_CLASS);
		//Pattern pattern = Pattern.compile("(\\d\\d)\\s(\\w{3,7})\\s(2018|2017)\\s(.*?)\\s([0123456789\\.+]*,{1}[0123456789]{2})\\s?([0123456789,\\.]*\\s\\/\\s\\d*)?\\s?(\\d*)?",Pattern.UNICODE_CHARACTER_CLASS);
		Pattern pattern = Pattern.compile("([0123456789\\.]*,{1}[0123456789]{2})\\sTL'lik\\siþlemin\\s(\\d+)\\s\\/\\s(\\d+)\\staksidi",Pattern.UNICODE_CHARACTER_CLASS);

		Matcher matcher = pattern.matcher(stmt);
		if (matcher.find()) {
			System.out.print(matcher.group(1));
			System.out.print(".");
			System.out.print(matcher.group(2));
			System.out.print(".");
			System.out.print(matcher.group(3));
			System.out.print(":");
/*			System.out.print(matcher.group(4));
			System.out.print(":");
			System.out.print(matcher.group(5));
			if (matcher.group(6)==null) {
				System.out.print("::");
				System.out.println(matcher.group(7));				
			} else {
				System.out.print("-");
				System.out.print(matcher.group(6));
				
				System.out.print("-");
				System.out.println(matcher.group(7));
			}*/
		} /*else {
			pattern = Pattern.compile("(\\d\\d)\\s(\\w{3,7})\\s(2018|2017)\\s(.*?)\\s([0123456789\\.+]*,{1}[0123456789]{2})\\s?([0123456789,\\.]*\\s\\/\\s\\d*)?\\s?(\\d*)?",Pattern.UNICODE_CHARACTER_CLASS);
						
			matcher = pattern.matcher(stmt);
			if (matcher.find()) {
				System.out.print(matcher.group(1));
				System.out.print(".");
				System.out.print(matcher.group(2));
				System.out.print(".");
				System.out.print(matcher.group(3));
				System.out.print(":");
				System.out.print(matcher.group(4));
				System.out.print(":");
				System.out.print(matcher.group(5));
				System.out.print(":");
				System.out.print(matcher.group(6));
				System.out.print(":");
				System.out.print(matcher.group(7));
			}
		}*/
	}

}
