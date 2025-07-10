package com.aurionpro.oops.interfaceClass.modal;

public class UPI implements IPaymentGatewayy{
	private double refundAmount;
	public void pay(double amount) {
		 System.out.println("Paid ₹" + amount + " using UPI.");
	}
	
	public void refund(double amount) {
		refundAmount=0.1*amount;
		 System.out.println("Refunded ₹" + refundAmount + " to UPI Account.");
	}
}
