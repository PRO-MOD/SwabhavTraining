package com.aurionpro.FoodOrderingApp.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.aurionpro.FoodOrderingApp.model.Admin.AdminDashboard;
import com.aurionpro.FoodOrderingApp.model.Customer.CustomerDashboard;
import com.aurionpro.FoodOrderingApp.model.JDBCConnection.AdminService;

public class Application {

	Scanner scanner = new Scanner(System.in);

	public void start(Connection connection) throws SQLException {
		while (true) {
			System.out.println("*****************Welcome to Garhwal Hotel*************************");
			System.out.println("1.Admin");
			System.out.println("2.Customer");
			System.out.println("3.Exit Application");
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

				AdminService adminService = new AdminService();

				if (adminService.adminAuthentication(connection, username, password)) {
					AdminDashboard admin = new AdminDashboard();
					admin.adminDashboard(connection);

				}

				else {
					System.out.println("Invalid credentails!");
				}
			}

			case 2 -> {
				CustomerDashboard customer = new CustomerDashboard();
				customer.customerDashboard(connection);
			}

			case 3 -> {
				System.out.println("Thank you for Using Application....");

				return;
			}

			default -> {
				System.out.println("Invalid Choice!");
			}
			}

		}
	}
}
