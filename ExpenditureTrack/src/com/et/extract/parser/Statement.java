package com.et.extract.parser;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Statement {
	
	public LocalDate trxDate;
	public String company;
	public BigDecimal amount;
	public String remainder;
	public BigDecimal point;
	public BigDecimal totalAmount;
	public int currentInstallment;
	public int installmentCount;
	
	public Statement(LocalDate trxDate, String company, BigDecimal amount, String remainder, BigDecimal point) {
		this.trxDate = trxDate;
		this.company = company;
		this.amount = amount;
		this.remainder = remainder;
		this.point = point;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Statement [trxDate=");
		builder.append(trxDate);
		builder.append(", company=");
		builder.append(company);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", remainder=");
		builder.append(remainder);
		builder.append(", point=");
		builder.append(point);
		builder.append(", totalAmount=");
		builder.append(totalAmount);
		builder.append(", currentInstallment=");
		builder.append(currentInstallment);
		builder.append(", installmentCount=");
		builder.append(installmentCount);
		builder.append("]");
		return builder.toString();
	}
	
}
