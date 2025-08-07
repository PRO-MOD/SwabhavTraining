package com.aurionpro.FoodOrderingApp.model.Admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.aurionpro.FoodOrderingApp.model.JDBCConnection.AdminService;

public class ManagePaymentMethods {
	AdminService adminService = new AdminService();

	Scanner scanner = new Scanner(System.in);

	public void managePaymentMethods(Connection connection) throws SQLException {

		while (true) {
			System.out.println("***************Welcome to Manage Payment Methods*******************");
			System.out.println("1.Add Payment Method");
			System.out.println("2.View All Payment Method");
			System.out.println("3.Deactivate Payment Method");
			System.out.println("4.Reactivate Payment Method");
			System.out.println("5.Permanentaly  Payment Method");
			System.out.println("6.Back to Admin menu");

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
				String methodeName = null;
				while (true) {
					System.out.println("Enter Partner Name");
					methodeName = scanner.nextLine();

					if (!methodeName.matches("^[a-zA-Z ]+$")) {
						System.out.println("Invalid Name type");
						continue;
					}
					break;
				}

				String status = null;
				while (true) {
					System.out.println("Enter Status(Active or Deactive)");
					status = scanner.nextLine();

					if (!status.equalsIgnoreCase("Active") && !status.equalsIgnoreCase("Deactive")) {
						System.out.println("Invalid Status type");
						continue;
					}

					break;
				}

				adminService.addPaymentMethod(connection, methodeName, status);

			}

			case 2 -> {
				adminService.viewAllPaymentMethods(connection);
			}

			case 3 -> {
				adminService.viewAllPaymentMethods(connection);
				System.out.println("Enter id to Deactive Payment Mehthod");
				int id = scanner.nextInt();
				scanner.nextLine();

				adminService.deactivatePaymentMethod(connection, id);
				System.out.println("Partner Deactivated");
			}

			case 4 -> {
				adminService.viewAllPaymentMethods(connection);
				System.out.println("Enter id to Reactive Payment Mehthod");
				int id = scanner.nextInt();
				scanner.nextLine();

				adminService.reactivatePaymentMethod(connection, id);
				;
				System.out.println("Partner Deactivated");
			}

			case 5 -> {
				adminService.viewAllPaymentMethods(connection);
				System.out.println("Enter id to Permanently delete Payment Mehthod");
				int id = scanner.nextInt();
				scanner.nextLine();

				adminService.deletePaymentMethod(connection, id);
				System.out.println("Payment Method Deleted!");
			}

			case 6 -> {
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
