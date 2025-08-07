package com.aurionpro.FoodOrderingApp.model.Admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.aurionpro.FoodOrderingApp.model.JDBCConnection.AdminService;

public class ManageDeliveryPartners {

	Scanner scanner = new Scanner(System.in);
	AdminService adminService = new AdminService();

	public void manageDeliveryPartners(Connection connection) throws SQLException {

		while (true) {
			System.out.println("***************Welcome to Manage Delivery Partners*******************");
			System.out.println("1.Add Delivery Partner");
			System.out.println("2.View All Delivery Partner");
			System.out.println("3.Deactivate  Delivery Partner");
			System.out.println("4.Reactivate Delivery Partner");
			System.out.println("5.Permanentaly Delete Delivery Partner");
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
				String partnername = null;
				while (true) {
					System.out.println("Enter Partner Name");
					partnername = scanner.nextLine();

					if (!partnername.matches("^[a-zA-Z ]+$")) {
						System.out.println("Invalid Name type");
						continue;
					}
					break;
				}

				String phoneNumber = null;
				while (true) {
					System.out.println("Enter Phone Number:");
					phoneNumber = scanner.nextLine();

					if (!phoneNumber.matches("\\d{10}")) {
						System.out.println("Invalid phone Number");
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

				adminService.addPartner(connection, partnername, phoneNumber, status);

			}

			case 2 -> {
				adminService.viewAllDeliveryPartners(connection);
			}

			case 3 -> {
				adminService.viewAllDeliveryPartners(connection);
				System.out.println("Enter id to Deactive Delivery partner");
				int id = scanner.nextInt();
				scanner.nextLine();

				adminService.deactivateDeliveryPartner(connection, id);
				System.out.println("Partner Deactivated");

			}

			case 4 -> {
				adminService.viewAllDeliveryPartners(connection);
				System.out.println("Enter id to Reactive Delivery partner");
				int id = scanner.nextInt();
				scanner.nextLine();

				adminService.reactivateDeliveryPartner(connection, id);
				System.out.println("Partner Re activated");
			}

			case 5 -> {
				adminService.viewAllDeliveryPartners(connection);
				System.out.println("Enter id to Permanently delete Delivery partner");
				int id = scanner.nextInt();
				scanner.nextLine();

				adminService.deleteDeliveryPartner(connection, id);
				System.out.println("Partner Deleted!");
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
