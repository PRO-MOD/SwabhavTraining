package com.aurionpro.JDBC.Transaction.Bank.model;

public class InsufficientBalanceException extends Exception{

	public InsufficientBalanceException(String message) {
		super(message);
	}
}
