package com.aurionpro.FoodOrderingApp.model.Admin;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ManageItemDeletion {

	Scanner scanner = new Scanner(System.in);

	public void manageItemDeletion() {

		while (true) {
			System.out.println("***************Welcome to Manage Item deletion*******************");
			System.out.println("1.Deactivate Item");
			System.out.println("2.Permanentaly Delete item");
			System.out.println("3.Reactivate Item");
			System.out.println("4.Back to Admin menu");

			int choose = 0;

			try {
				choose = scanner.nextInt();
				scanner.nextLine();

			}

			catch (InputMismatchException e) {
				System.out.println("Only numbers allowed");
				scanner.next();
			}

			switch (choose) {
			case 1 -> {

			}

			case 2 -> {

			}

			case 3 -> {

			}

			case 4 -> {
				System.out.println("Redirecting to Admin menu........");
				return;
			}

			default -> {
				System.out.println("Invalid choice");
			}

			}
		}

	}
}
