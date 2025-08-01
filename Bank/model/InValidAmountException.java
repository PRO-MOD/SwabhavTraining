package com.aurionpro.JDBC.Transaction.Bank.model;

public class InValidAmountException extends Exception{

	public InValidAmountException(String message) {
		super(message);
	}
}
