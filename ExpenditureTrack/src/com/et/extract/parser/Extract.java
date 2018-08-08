package com.et.extract.parser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Extract {
	
	public BigDecimal debt;
	public String debt_currency;
	public ArrayList<CreditCard> cards;
	public LocalDate extractDate;
	public ArrayList<Statement> statements;
	public BigDecimal payment;
	private String extractFileByte;
	
	public Extract() {
		this.cards = new ArrayList<CreditCard>();
		this.statements = new ArrayList<Statement>();
		this.payment = new BigDecimal(0);
	}

	public CreditCard getCard(String cardNo){
		for (CreditCard card:cards){
			if (card.cardNo.equals(cardNo)) return card;
		}
		return null;
	}
	
	public int addCard(CreditCard card) {
		if (getCard(card.cardNo)==null) {
			cards.add(card);
			return cards.size();
		}
		return -1;
	}
	
	public abstract void parseExtract(String pdfTxt);
	
	public abstract void printExtract();
	
	public abstract void printExtractGroupByCompany();

	public String getExtractFileByte() {
		return extractFileByte;
	}

	public void setExtractFileByte(String extractFileByte) {
		this.extractFileByte = extractFileByte;
	}
		
}
