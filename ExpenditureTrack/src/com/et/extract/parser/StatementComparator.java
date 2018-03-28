package com.et.extract.parser;

import java.util.Comparator;

public class StatementComparator implements Comparator<Statement> {

	public StatementComparator() {
	}

	public int compare(Statement stmt0, Statement stmt1) {
		return stmt0.amount.compareTo(stmt1.amount);
	}

}
