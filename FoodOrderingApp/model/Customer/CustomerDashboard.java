package com.aurionpro.FoodOrderingApp.model.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.aurionpro.FoodOrderingApp.model.Admin.AdminDashboard;
import com.aurionpro.FoodOrderingApp.model.JDBCConnection.CustomerService;

public class CustomerDashboard {

	Scanner scanner = new Scanner(System.in);

	public void customerDashboard(Connection connection) throws SQLException {

		while (true) {
			System.out.println("***************Welcome to Customer Dashboard*******************");
			System.out.println("1.Login");
			System.out.println("2.Register");
			System.out.println("3.Back to main Menu");

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
				System.out.println("Enter Username:");
				String username = scanner.nextLine();

				System.out.println("Enter Password");
				String password = scanner.nextLine();

				CustomerService customerService = new CustomerService();

				if (customerService.customerAuthentication(connection, username, password)) {
					CustomerMenu customer_menu = new CustomerMenu();
					customer_menu.customerMenu(connection);

				}

				else {
					System.out.println("Invalid credentails!");
				}
			}

			case 2 -> {

				String name = null;
				while (true) {
					System.out.println("Enter Name:");
					name = scanner.nextLine();

					if (!name.matches("^[a-zA-Z ]+$")) {
						System.out.println("Invalid Name type");
						continue;
					}
					break;
				}

				String emailId = null;
				while (true) {
					System.out.println("Enter Email id:");
					emailId = scanner.nextLine();

					if (!emailId.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
						System.out.println("Invalid Email");
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

				String address = null;
				while (true) {
					System.out.println("Enter Address:");
					address = scanner.nextLine();

					if (address == null || address.isEmpty() || address.length() == 0) {
						System.out.println("Invalid Address");
						continue;
					}
					break;
				}

				System.out.println("Set UserName:");
				String username = scanner.nextLine();
				System.out.println("Set Password:");
				String password = scanner.nextLine();

				CustomerService customerService = new CustomerService();
				customerService.registerCustomer(connection, name, address, phoneNumber, emailId, username, password);

			}

			case 3 -> {
				System.out.println("Redirecting to Main menu........");
				return;
			}

			default -> {
				System.out.println("Invalid choice");
			}

			}
		}

	}
}
