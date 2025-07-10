package com.aurionpro.oops.interfaceClass.test;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.aurionpro.oops.interfaceClass.modal.Checkout;
import com.aurionpro.oops.interfaceClass.modal.CreditCard;
import com.aurionpro.oops.interfaceClass.modal.IPaymentGatewayy;
import com.aurionpro.oops.interfaceClass.modal.NetBanking;
import com.aurionpro.oops.interfaceClass.modal.UPI;

public class PaymentGatewayyTest {
	public static void main(String[] args) {
		
		
		Scanner scanner=new Scanner(System.in);
		
		double payAmount=0.0;
		while (true) {
			System.out.println("Enter Pay amount");
			
			try {
				
				payAmount = scanner.nextDouble();
			}
			
			catch (InputMismatchException e) {
				System.out.println(e);
				System.out.println("String is not allowed");
				scanner.next();
			}

			if (payAmount > 0.0) {
				break;
			}

			else {
				System.out.println("Plz enter valid Amount!");
			}
		}

		scanner.nextLine();
		
		String paymentGateway;
		while (true) {
			System.out.println("Choose the Payment Gateway to Complete the Payment(CreditCard or UPI or NetBanking)");

			paymentGateway = scanner.nextLine();
			if (paymentGateway.equalsIgnoreCase("CreditCard")) {
				IPaymentGatewayy creditCard = new CreditCard();
//				Checkout checkout1 = new Checkout(creditCard);
//				checkout1.completePurchase(payAmount);
//				checkout1.processRefund(payAmount);
				creditCard.pay(payAmount);
				creditCard.refund(payAmount);
				break;
			}

			else if (paymentGateway.equalsIgnoreCase("UPI")) {
				IPaymentGatewayy upi = new UPI();
//				Checkout checkout1 = new Checkout(upi);
//				checkout1.completePurchase(payAmount);
//				checkout1.processRefund(payAmount);
				
				upi.pay(payAmount);
				upi.refund(payAmount);
				break;
			}

			else if (paymentGateway.equalsIgnoreCase("NetBanking")) {
				IPaymentGatewayy netBanking = new NetBanking();
//				Checkout checkout1 = new Checkout(netBanking);
//				checkout1.completePurchase(payAmount);
//				checkout1.processRefund(payAmount);
				
				netBanking.pay(payAmount);
				netBanking.refund(payAmount);
				break;
			}
			
			else {
				System.out.println("Choose valid Payment Gateway!");
			}

		}

		
	}
}
