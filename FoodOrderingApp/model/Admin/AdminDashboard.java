package com.aurionpro.FoodOrderingApp.model.Admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.aurionpro.FoodOrderingApp.model.JDBCConnection.AdminService;

public class AdminDashboard {
	Scanner scanner = new Scanner(System.in);
	AdminService adminService = new AdminService();

	public void adminDashboard(Connection connection) throws SQLException {

		while (true) {
			System.out.println("***************Welcome to Admin Dashboard*******************");
			System.out.println("1.Manage Food Items");
			System.out.println("2.Search by Food Name or Cuisine");
			System.out.println("3.View Registered Customers");
			System.out.println("4.Manage Delivery Partners");
			System.out.println("5.Manage Payment Methods");
//			System.out.println("6.View Revenue and All orders");
			System.out.println("6.Back to Main menu");

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
				ManageFoodItem manage_food_item = new ManageFoodItem();
				manage_food_item.manageFoodItem(connection);
			}

			case 2 -> {
				int choice = 0;
				boolean inSearchMenu = true;
				while (inSearchMenu) {
					System.out.println("Search by:");
					System.out.println("1.Food Name");
					System.out.println("2.Cuisine Type");
					System.out.println("3.Category");
					System.out.println("4.Exit");

					try {
						choice = scanner.nextInt();
						scanner.nextLine();
					}

					catch (InputMismatchException e) {
						System.out.println("Only numbers Allowed");
					}

					switch (choice) {
					case 1 -> {
						String foodName = null;
						while (true) {
							System.out.println("Enter Food Name");
							foodName = scanner.nextLine();

							if (!foodName.matches("^[a-zA-Z ]+$")) {
								System.out.println("Invalid Name type");
								continue;
							}
							break;
						}

						adminService.searchByFoodName(connection, foodName);

					}

					case 2 -> {
						String cuisine = null;
						while (true) {
							System.out.println("Enter Cuisine Type");
							cuisine = scanner.nextLine();

							if (!cuisine.matches("^[a-zA-Z ]+$")) {
								System.out.println("Invalid Name type");
								continue;
							}
							break;
						}
						adminService.searchByCuisineType(connection, cuisine);
					}

					case 3 -> {
						String category = null;
						while (true) {
							System.out.println("Enter Category");
							category = scanner.nextLine();

							if (!category.matches("^[a-zA-Z ]+$")) {
								System.out.println("Invalid Name type");
								continue;
							}
							break;
						}
						adminService.searchByCategory(connection, category);
					}

					case 4 -> {
						inSearchMenu = false;
					}

					default -> {
						System.out.println("Invalid choice!");
					}

					}
				}

			}

			case 3 -> {
				adminService.viewRegisteredCustomer(connection);
			}

			case 4 -> {
				ManageDeliveryPartners manage_delivery_partners = new ManageDeliveryPartners();
				manage_delivery_partners.manageDeliveryPartners(connection);
			}

			case 5 -> {
				ManagePaymentMethods manage_payment_methods = new ManagePaymentMethods();
				manage_payment_methods.managePaymentMethods(connection);
			}

			case 6 -> {
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
