package com.aurionpro.FoodOrderingApp.model.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import com.aurionpro.FoodOrderingApp.model.JDBCConnection.AdminService;
import com.aurionpro.FoodOrderingApp.model.JDBCConnection.CustomerService;

public class CustomerMenu {
	Scanner scanner = new Scanner(System.in);

	AdminService adminService = new AdminService();
	CustomerService customerService = new CustomerService();

	public void customerMenu(Connection connection) throws SQLException {

		while (true) {
			System.out.println("***************Welcome to Customer Menu*******************");
			System.out.println("1.View Food Menu");
			System.out.println("2.Add food to cart");
			System.out.println("3.View Cart");
			System.out.println("4.Manage Cart");
			System.out.println("5.Place order and get Bill");
			System.out.println("6.Search by item name or Cuisine");
//			System.out.println("7.view Order history");
			System.out.println("7.Logout");

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
				adminService.viewFullMenu(connection);
			}

			case 2 -> {

				int foodId = 0;
				int customerId = 0;
				int quantity = 0;

				while (true) {

					System.out.print("Enter Food ID to add to cart: ");
					try {
						foodId = scanner.nextInt();
						scanner.nextLine();
					}

					catch (InputMismatchException e) {
						System.out.println("Only numbers Allowed");
						scanner.next();
						continue;
					}
					break;
				}

				while (true) {

					System.out.print("Enter your Customer ID: ");
					try {
						customerId = scanner.nextInt();
						scanner.nextLine();
					}

					catch (InputMismatchException e) {
						System.out.println("Only numbers Allowed");
						scanner.next();
						continue;
					}
					break;
				}

				while (true) {

					System.out.print("Enter Quantity");
					try {
						quantity = scanner.nextInt();
						scanner.nextLine();
						if (quantity <= 0 || quantity > 5) {
							System.out.println("Minimum 1 and maximum 5 Quantity is allowed");
							continue;
						}
					}

					catch (InputMismatchException e) {
						System.out.println("Only numbers Allowed");
						scanner.next();
						continue;
					}
					break;
				}

				customerService.addToCart(connection, foodId, customerId, quantity);

			}

			case 3 -> {
				customerService.viewCart(connection);
			}

			case 4 -> {
				boolean inManageCart = true;
				int choice = 0;
				while (inManageCart) {
					System.out.println("1.Remove Items from Cart");
					System.out.println("2.Update Quantities of Item");
					System.out.println("3.Go Back");

					try {
						choice = scanner.nextInt();
						scanner.nextLine();
					} catch (InputMismatchException e) {
						System.out.println("Only numbers allowed");
						scanner.next();
						continue;
					}

					switch (choice) {
					case 1 -> {
						int itemId = 0;

						while (true) {

							System.out.print("Enter Item ID to remove from cart: ");
							try {
								itemId = scanner.nextInt();
								scanner.nextLine();
							}

							catch (InputMismatchException e) {
								System.out.println("Only numbers Allowed");
								scanner.next();
								continue;
							}
							break;
						}
						customerService.deleteItemsFromCart(connection, itemId);

					}

					case 2 -> {

						int itemId = 0;

						while (true) {

							System.out.print("Enter Item ID to update quantity : ");
							try {
								itemId = scanner.nextInt();
								scanner.nextLine();
							}

							catch (InputMismatchException e) {
								System.out.println("Only numbers Allowed");
								scanner.next();
								continue;
							}
							break;
						}

						int quantity = 0;
						while (true) {

							System.out.print("Enter Quantity");
							try {
								quantity = scanner.nextInt();
								scanner.nextLine();
								if (quantity <= 0 || quantity > 5) {
									System.out.println("Minimum 1 and maximum 5 Quantity is allowed");
									continue;
								}
							}

							catch (InputMismatchException e) {
								System.out.println("Only numbers Allowed");
								scanner.next();
								continue;
							}
							break;
						}

						customerService.updateQunatity(connection, itemId, quantity);
						System.out.println("Quantity Updated successfully");

					}

					case 3 -> {
						inManageCart = false;
					}

					default -> {
						System.out.println("Invalid choice!");
					}
					}
				}
			}

			case 5 -> {
				System.out.println("This is Your Cart");
				customerService.viewCart(connection);

				System.out.println("Here is Your Bill");

				int FinalBill = 0;
				int totalPrice = customerService.getTotalBill(connection);
				int totalDiscount = customerService.getDiscount(connection);

				FinalBill = totalPrice - totalDiscount;

				System.out.println("Total Order Price:₹" + totalPrice);
				System.out.println("Total Discount Price:₹" + totalDiscount);
				System.out.println("Final Payable Price:₹" + FinalBill);

				String choice = null;

				while (true) {
					System.out.println("Do you Want to procced for the order Placement");

					choice = scanner.nextLine();

					if (!choice.equalsIgnoreCase("yes") && !choice.equalsIgnoreCase("no")) {
						System.out.println("Enter Valid choice");
						continue;
					}

					break;
				}
				ArrayList<String> paymentMethod = customerService.paymentMethod(connection);
				if (choice.equalsIgnoreCase("yes")) {

					System.out.println("Select the Payment Method");
					for (int i = 0; i < paymentMethod.size(); i++) {
						System.out.println((i + 1) + "." + paymentMethod.get(i));
					}
				}

				else {
					return;
				}

				int input = -1;
				while (true) {
					System.out.print("Enter choice (1 to " + paymentMethod.size() + "): ");
					input = scanner.nextInt();
					try {

						if (input >= 1 && input <= paymentMethod.size()) {
							break;
						} else {
							System.out.println("Invalid choice. Please select from the list.");
						}
					} catch (NumberFormatException e) {
						System.out.println("Enter a valid number.");
					}
				}

				String selectedPayment = paymentMethod.get(input - 1);
				System.out.println("You selected: " + selectedPayment);

				ArrayList<String> deliveryPartners = customerService.deliveryPartner(connection);

				if (!deliveryPartners.isEmpty()) {
					Random random = new Random();
					int randomIndex = random.nextInt(deliveryPartners.size());
					String assignedPartner = deliveryPartners.get(randomIndex);

					System.out.println("Your Order will be Delivered by: " + assignedPartner);

				}

				else {
					System.out.println("No delivery partners available at the moment. Please try again later.");
				}

				customerService.clearCart(connection);

				System.out.println("Thank you!!");
			}

			case 6 -> {
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

						customerService.searchByFoodName(connection, foodName);

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
						customerService.searchByCuisineType(connection, cuisine);
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
						customerService.searchByCategory(connection, category);
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

			case 7 -> {
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
