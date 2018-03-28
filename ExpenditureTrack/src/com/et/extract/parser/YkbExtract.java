package com.et.extract.parser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.et.main.commons.Util;

public class YkbExtract extends Extract {
	
	private final String periodDebtRegex = "Dönem\\sBorcu\\s:\\s(([0123456789\\.+]*,{1}[0123456789]{2})\\sTL)";
	private Pattern patternPeriodDebt;
	
	private final String cardRegex = "Kart\\sNumarasý\\s:\\s(\\d\\d\\d\\d\\s\\d\\d\\*\\*\\s\\*\\*\\*\\*\\s\\d\\d\\d\\d)\\s(.+)";
	private Pattern patternCard;
	
	private final String cardRegex2 = "\\s?Kart\\sNumarasý\\s:\\s(\\d\\d\\d\\d\\d\\d\\*\\*\\s\\*\\*\\*\\*\\d\\d\\d\\d)\\s(.*)";
	private Pattern patternCard2;
	
	private final String statementRegex = "(\\d\\d)\\s(\\w{3,7})\\s(2018|2017)\\s(.*?)\\s([0123456789\\.+]*,{1}[0123456789]{2})\\s?([0123456789,\\.]*\\s\\/\\s\\d*)?\\s?(\\d*)?"; 
	private Pattern patternStatement;

	private final String worldpuanRegex = "WORLDPUAN\\s([0123456789\\.+]*)\\s([0123456789\\.+]*)\\s([0123456789\\.+]*)\\s"; 
	private Pattern patternWorld;
	
	private final String previousDebtRegex = "ÖNCEKÝ\\sDÖNEM\\sHESAP\\sÖZETÝ\\sBORCU\\s([0123456789\\.+]*,{1}[0123456789]{2})"; 
	private Pattern patternPreviousDebt;
	
	private final String extractDateRegex = "Hesap\\sKesim\\sTarihi\\s:\\s(\\d\\d)\\s(\\w{3,7})\\s(2018|2017)"; 
	private Pattern patternExtractDate;
	
	private final String paymentRegex = "ÖDEME-ÝNTERNET\\sBANKACILIÐI\\s([0123456789\\.+]*,{1}[0123456789]{2})"; 
	private Pattern patternPayment;
	
	private final String installmentRegex = "([0123456789\\.]*,{1}[0123456789]{2})\\sTL'lik\\siþlemin\\s(\\d+)\\s\\/\\s(\\d+)\\staksidi"; 
	private Pattern patternInstallment;
	
	public YkbExtract() {
		patternPeriodDebt = Pattern.compile(periodDebtRegex,Pattern.UNICODE_CHARACTER_CLASS);
		patternCard = Pattern.compile(cardRegex,Pattern.UNICODE_CHARACTER_CLASS);
		patternCard2 = Pattern.compile(cardRegex2,Pattern.UNICODE_CHARACTER_CLASS);
		patternStatement = Pattern.compile(statementRegex,Pattern.UNICODE_CHARACTER_CLASS);
		patternWorld = Pattern.compile(worldpuanRegex,Pattern.UNICODE_CHARACTER_CLASS);
		patternPreviousDebt = Pattern.compile(previousDebtRegex,Pattern.UNICODE_CHARACTER_CLASS);
		patternExtractDate = Pattern.compile(extractDateRegex,Pattern.UNICODE_CHARACTER_CLASS);
		patternPayment = Pattern.compile(paymentRegex,Pattern.UNICODE_CHARACTER_CLASS);
		patternInstallment = Pattern.compile(installmentRegex,Pattern.UNICODE_CHARACTER_CLASS);
	}
	
	public void parseExtract(String pdfTxt) {
		
		Matcher matcher;
		BigDecimal previousDebt = new BigDecimal(0);
		Statement stmt = null;
		
		for (String s:pdfTxt.split("\\n")) {
			if (this.debt == null) {
				matcher = patternPeriodDebt.matcher(s);
				if (matcher.find()) {
					this.debt = Util.getAmount(matcher.group(2));
					this.debt_currency = matcher.group(1);
				}
			}
			if (this.extractDate == null) {
				matcher = patternExtractDate.matcher(s);
				if (matcher.find()) {
					this.extractDate = Util.createTrxDate(matcher.group(1),matcher.group(2),matcher.group(3));
				}
			}
			
			matcher = patternPreviousDebt.matcher(s);
			if (matcher.find()) {
				previousDebt = Util.getAmount(matcher.group(1)).negate();
			} else {
				matcher = patternPayment.matcher(s);
				if (matcher.find()) {
					this.payment = this.payment.add(Util.getAmount(matcher.group(1)));
				} else {
					matcher = patternInstallment.matcher(s);
					if (matcher.find()) {
						stmt.totalAmount = Util.getAmount(matcher.group(1));
						stmt.currentInstallment = Integer.parseInt(matcher.group(2));
						stmt.installmentCount = Integer.parseInt(matcher.group(3));
					} else {
						matcher = patternStatement.matcher(s);
						if (matcher.find()) {
							if (!previousDebt.equals(Util.getAmount(matcher.group(5)))) {
								stmt = new Statement(Util.createTrxDate(matcher.group(1),matcher.group(2),matcher.group(3)), matcher.group(4), Util.getAmount(matcher.group(5)), matcher.group(6), Util.getAmount(matcher.group(7)));
								this.cards.get(this.cards.size()-1).addStmt(stmt);
								this.statements.add(stmt);
							}
						} else {
							matcher = patternCard.matcher(s);
							if (matcher.find()) {
								this.addCard(new CreditCard(matcher.group(1),matcher.group(2)));
							} else {
								matcher = patternCard2.matcher(s);
								if (matcher.find()) {
									this.addCard(new CreditCard(matcher.group(1),matcher.group(2)));
								} else {
									matcher = patternWorld.matcher(s);
									if (matcher.find()) {
										break;
									} //else System.out.print(s);								
								}
							}
						}
					}
				}
			}
		}
		
	}

	@Override
	public void printExtract() {
		
		BigDecimal totals = new BigDecimal(0);
		
		System.out.println("Dönem:"+this.extractDate);
		System.out.println("Dönem Borcu:"+this.debt);
		
		for (CreditCard card : this.cards) {
			card.calculateTotal();
			totals = totals.add(card.cardTotal);
			System.out.println(card.toString());
		}
		
		System.out.println("Toplam:"+totals);
		/*
		this.sortStatements();
		for (Statement stmt : this.statements) {
			System.out.println(stmt.toString());
		}
		*/
		String str = Util.toJson(this.cards.get(0).statements.get(0));
		System.out.println(str);
	}

	@Override
	public void printExtractGroupByCompany() {
		HashMap<String,BigDecimal> map = new HashMap<String,BigDecimal>();
		
		for (CreditCard card : this.cards) {
			for (Statement stmt : card.statements) {
				if (map.get(stmt.company) == null)
					map.put(stmt.company, stmt.amount);
				else
					map.put(stmt.company, map.get(stmt.company).add(stmt.amount));
			}
		}
		
		for (Map.Entry me : map.entrySet()) {
	          System.out.println(me.getKey() + ": " + me.getValue());
	    }
	}
	
	public void sortStatements() {
	    this.statements.sort(new StatementComparator());
	    
	}

}
