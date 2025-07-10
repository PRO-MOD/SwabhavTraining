package com.aurionpro.oops.interfaceClass.modal;

public class NetBanking implements IPaymentGatewayy{
	private double refundAmount;

	public void pay(double amount) {
		 System.out.println("Paid ₹" + amount + " using NetBanking.");
	}
	
	public void refund(double amount) {
		refundAmount=0.15*amount;
		 System.out.println("Refunded ₹" + refundAmount + " to NetBanking Account.");
	}
}
