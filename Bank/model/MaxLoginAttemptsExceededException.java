package com.aurionpro.JDBC.Transaction.Bank.model;

public class MaxLoginAttemptsExceededException extends Exception{

	public MaxLoginAttemptsExceededException(String message) {
		super(message);
	}
}
