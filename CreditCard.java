package com.aurionpro.oops.interfaceClass.modal;

public class CreditCard implements IPaymentGatewayy{
	private double refundAmount;
	

	public void pay(double amount) {
		 System.out.println("Paid ₹" + amount + " using Credit Card.");
	}
	
	public void refund(double amount) {
		refundAmount=0.25*amount;
		 System.out.println("Refunded ₹" + refundAmount + " to Credit Card.");
	}
}
