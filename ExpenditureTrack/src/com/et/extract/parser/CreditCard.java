package com.et.extract.parser;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CreditCard {
	
	public String cardNo;
	public String cardOwner;
	public BigDecimal cardTotal;
	public ArrayList<Statement> statements;
	public ArrayList<String> statements_str;
	
	public CreditCard(String cardNo, String cardOwner) {
		super();
		this.cardNo = cardNo;
		this.cardOwner = cardOwner;
		this.cardTotal = new BigDecimal(0);
		this.statements = new ArrayList<Statement>();
		this.statements_str = new ArrayList<String>();
	}
	
	public void addStmt(String stmt) {
		statements_str.add(stmt);
	}
	
	public void addStmt(Statement stmt) {
		statements.add(stmt);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CreditCard [cardNo=");
		builder.append(cardNo);
		builder.append(", cardOwner=");
		builder.append(cardOwner);
		builder.append(", cardTotal=");
		builder.append(cardTotal);
		builder.append(", statements_Count=");
		builder.append(statements.size());
		builder.append(", statements=");
		builder.append(statements);
		builder.append("]");
		return builder.toString();
	}
	
	public void calculateTotal() {
		this.cardTotal = new BigDecimal(0);
		for (Statement stmt : this.statements) {
			this.cardTotal = this.cardTotal.add(stmt.amount);
		}		
	}
	
}
