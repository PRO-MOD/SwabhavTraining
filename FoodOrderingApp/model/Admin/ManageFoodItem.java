package com.aurionpro.FoodOrderingApp.model.Admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.aurionpro.FoodOrderingApp.model.JDBCConnection.AdminService;

public class ManageFoodItem {

	Scanner scanner = new Scanner(System.in);

	public void manageFoodItem(Connection connection) throws SQLException {

		while (true) {
			System.out.println("***************Welcome to Manage Food Item*******************");
			System.out.println("1.Add New Food Item");
			System.out.println("2.View full menu");
			System.out.println("3.Update food Item");
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

				double foodPrice = 0.0;
				while (true) {
					System.out.println("Enter Food Price");
					try {
						foodPrice = scanner.nextDouble();
						scanner.nextLine();

					}

					catch (InputMismatchException e) {
						System.out.println(e);
						System.out.println("Only numbers allowed");
						scanner.next();
						continue;
					}
					break;
				}

				double discount = 0.0;
				if (foodPrice > 1000) {
					discount = 0.1 * foodPrice;
				}

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

				String status = null;
				while (true) {
					System.out.println("Enter Status(Available or NotAvailable)");
					status = scanner.nextLine();

					if (!status.equalsIgnoreCase("Available") && !status.equalsIgnoreCase("NotAvailable")) {
						System.out.println("Invalid Status type");
						continue;
					}

					break;
				}

				AdminService adminService = new AdminService();
				adminService.addFoodItem(connection, foodName, foodPrice, discount, cuisine, category, status);

			}

			case 2 -> {
				AdminService adminService = new AdminService();
				adminService.viewFullMenu(connection);
			}

			case 3 -> {

				AdminService adminService = new AdminService();
				adminService.viewFullMenu(connection);

				System.out.println("Enter Id to update");
				int id = scanner.nextInt();
				scanner.nextLine();

				int choice = 0;
				boolean isupdate = true;
				while (isupdate) {
					System.out.println("1.update Name");
					System.out.println("2.update price");
					System.out.println("3.update cuisine Type");
					System.out.println("4.update Category");
					System.out.println("5.update Status");
					System.out.println("6.Exit");

					try {
						choice = scanner.nextInt();
						scanner.nextLine();
					}

					catch (InputMismatchException e) {
						System.out.println("numbers allowed");
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
						adminService.updateFoodName(connection, foodName, id);
						System.out.println("Name updated successfully.");

					}

					case 2 -> {
						double foodPrice = 0.0;
						while (true) {
							System.out.println("Enter Food Price");
							try {
								foodPrice = scanner.nextDouble();
								scanner.nextLine();

							}

							catch (InputMismatchException e) {
								System.out.println(e);
								System.out.println("Only numbers allowed");
								scanner.next();
								continue;
							}
							break;
						}
						adminService.updateFoodPrice(connection, foodPrice, id);

						double discount = 0.0;
						if (foodPrice > 1000) {
							discount = 0.1 * foodPrice;
						}

						adminService.updateDiscount(connection, discount, id);
						System.out.println("Price and discount updated successfully.");
					}

					case 3 -> {
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

						adminService.updateCuisineType(connection, cuisine, id);
						System.out.println("Cuisine Type updated successfully.");

					}

					case 4 -> {
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

						adminService.updateCategory(connection, category, id);
						System.out.println("Category updated successfully.");
					}

					case 5 -> {
						String status = null;
						while (true) {
							System.out.println("Enter Status(Available or NotAvailable)");
							status = scanner.nextLine();

							if (!status.equalsIgnoreCase("Available") && !status.equalsIgnoreCase("NotAvailable")) {
								System.out.println("Invalid Status type");
								continue;
							}

							break;
						}
						adminService.updateStatus(connection, status, id);
						System.out.println("Status updated successfully.");
					}

					case 6 -> {
						isupdate = false;
					}

					default -> {
						System.out.println("Invalid choice!");
					}

					}
				}

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
