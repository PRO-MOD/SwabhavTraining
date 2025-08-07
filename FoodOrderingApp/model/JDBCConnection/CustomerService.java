package com.aurionpro.FoodOrderingApp.model.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerService {

	public ArrayList<String> list = new ArrayList<>();
	int totalPrice = 0;
	int totalDiscount=0;

	public boolean customerAuthentication(Connection connection, String username, String password) throws SQLException {
		String customerAuthenticationQuery = "select * from customers where username=? and password=?";

		PreparedStatement customerAuthenticationStatement = connection.prepareStatement(customerAuthenticationQuery);

		customerAuthenticationStatement.setString(1, username);
		customerAuthenticationStatement.setString(2, password);

		ResultSet resultSetcustomerAuthentication = customerAuthenticationStatement.executeQuery();

		if (resultSetcustomerAuthentication.next()) {
			return true;
		}

		return false;

	}

	public void registerCustomer(Connection connection, String name, String address, String phoneNumber, String emailId,
			String username, String password) throws SQLException {
		String registerCustomerQuery = "insert into customers (name,address,phoneNumber,emailId,username,password) values(?,?,?,?,?,?)";

		PreparedStatement registerCustomerStatement = connection.prepareStatement(registerCustomerQuery);
		registerCustomerStatement.setString(1, name);
		registerCustomerStatement.setString(2, address);
		registerCustomerStatement.setString(3, phoneNumber);
		registerCustomerStatement.setString(4, emailId);
		registerCustomerStatement.setString(5, username);
		registerCustomerStatement.setString(6, password);

		registerCustomerStatement.execute();

		System.out.println("Customer Registered Added Succesfully!");

	}

	public void addToCart(Connection connection, int foodId, int customerId, int quantity) throws SQLException {
		String foodQuery = "SELECT * FROM foodItems WHERE food_id = ? and status='Available'";
		String insertQuery = "INSERT INTO cart (customer_id, food_id, name, cuisineType, category, price, discount,quantity) VALUES (?, ?, ?, ?, ?, ?, ?,?)";

		PreparedStatement foodQueryrStatement = connection.prepareStatement(foodQuery);
		foodQueryrStatement.setInt(1, foodId);

		PreparedStatement insertQueryStatement = connection.prepareStatement(insertQuery);

		ResultSet resultSetfoodQuery = foodQueryrStatement.executeQuery();

		if (resultSetfoodQuery.next()) {
			String name = resultSetfoodQuery.getString("name");
			String cuisineType = resultSetfoodQuery.getString("cuisineType");
			String category = resultSetfoodQuery.getString("category");
			double price = resultSetfoodQuery.getDouble("price");
			double totalPrice = price * quantity;
			double discount = 0.1 * totalPrice;

			insertQueryStatement.setInt(1, customerId);
			insertQueryStatement.setInt(2, foodId);
			insertQueryStatement.setString(3, name);
			insertQueryStatement.setString(4, cuisineType);
			insertQueryStatement.setString(5, category);
			insertQueryStatement.setDouble(6, totalPrice);
			insertQueryStatement.setDouble(7, discount);
			insertQueryStatement.setDouble(8, quantity);

			int rowsAffected = insertQueryStatement.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Food item added to cart successfully!");
			}

			else {
				System.out.println("Failed to add food item to cart.");
			}
		}

		else {
			System.out.println("Food item not found!");
		}

	}

	public void viewCart(Connection connection) throws SQLException {
		String viewCartQuery = "select * from cart";

		PreparedStatement viewCartStatement = connection.prepareStatement(viewCartQuery);
		ResultSet rs = viewCartStatement.executeQuery();
		printCart(rs);

	}

	private void printCart(ResultSet rs) throws SQLException {
		boolean found = false;
		while (rs.next()) {
			found = true;
			System.out.println("ItemId: " + rs.getInt("item_id") + "   CustomerId: " + rs.getInt("customer_id")
					+ "   FoodId: " + rs.getInt("food_id") + "   Food Name: " + rs.getString("name") + "   Quantity: "
					+ rs.getString("quantity") + "   Price: ₹" + rs.getDouble("price") + "   Discount: ₹"
					+ rs.getDouble("discount") + "   Cuisine Type: " + rs.getString("cuisineType") + "   Category: "
					+ rs.getString("category"));
		}
		if (!found) {
			System.out.println("There is nothing in the cart");
		}
	}

	public void viewFullMenu(Connection connection) throws SQLException {
		String viewFullMenuQuery = "select * from foodItems where status='Available'";

		PreparedStatement viewFullMenuStatement = connection.prepareStatement(viewFullMenuQuery);
		ResultSet rs = viewFullMenuStatement.executeQuery();
		printResults(rs);

	}

	private void printResults(ResultSet rs) throws SQLException {
		boolean found = false;
		while (rs.next()) {
			found = true;
			System.out.println("Id: " + rs.getInt("food_id") + "   Food Name: " + rs.getString("name") + "   Price: ₹"
					+ rs.getDouble("price") + "   Discount: ₹" + rs.getDouble("discount") + "   Cuisine Type: "
					+ rs.getString("cuisineType") + "   Category: " + rs.getString("category"));
		}
		if (!found) {
			System.out.println("There is nothing in the menu.");
		}
	}

	public void deleteItemsFromCart(Connection connection, int id) throws SQLException {
		String deleteQuery = "DELETE from cart WHERE item_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}

	public void updateQunatity(Connection connection, int id, int quantity) throws SQLException {
		String updateQuery = "UPDATE cart SET quantity = ? WHERE item_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
			stmt.setInt(1, quantity);
			stmt.setInt(2, id);

			stmt.executeUpdate();
		}
	}

	public void searchByFoodName(Connection connection, String name) throws SQLException {
		String query = "SELECT * FROM foodItems WHERE status = 'Available' AND name = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			printResults(rs);
		}
	}

	public void searchByCuisineType(Connection connection, String cuisine) throws SQLException {
		String query = "SELECT * FROM foodItems WHERE status = 'Available' AND cuisineType = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, cuisine);
			ResultSet rs = stmt.executeQuery();
			printResults(rs);
		}
	}

	public void searchByCategory(Connection connection, String category) throws SQLException {
		String query = "SELECT * FROM foodItems WHERE status = 'Available' AND category = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, category);
			ResultSet rs = stmt.executeQuery();
			printSearchResults(rs);
		}
	}

	private void printSearchResults(ResultSet rs) throws SQLException {
		boolean found = false;
		while (rs.next()) {
			found = true;
			System.out.println("Id: " + rs.getInt("food_id") + "   Food Name: " + rs.getString("name") + "   Price: ₹"
					+ rs.getDouble("price") + "   Discount: ₹" + rs.getDouble("discount") + "   Cuisine Type: "
					+ rs.getString("cuisineType") + "   Category: " + rs.getString("category") + "   Status: "
					+ rs.getString("status"));
		}
		if (!found) {
			System.out.println("No matching food items found.");
		}
	}

	public ArrayList paymentMethod(Connection connection) throws SQLException {
		String paymentMethod = "select method_name from paymentmethods where status='Active'";

		ArrayList <String>paymentList=new ArrayList<>();
		PreparedStatement stmt = connection.prepareStatement(paymentMethod);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			String method = rs.getString("method_name");
			paymentList.add(method);
		}
		return paymentList;
	}

	public int getTotalBill(Connection connection) throws SQLException {
		String bill = "select price from cart";

		PreparedStatement stmt = connection.prepareStatement(bill);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			int price = rs.getInt("price");
			totalPrice = totalPrice + price;
		}

		return totalPrice;

	}

	public int getDiscount(Connection connection) throws SQLException {
		String discount = "select discount from cart";

		PreparedStatement stmt = connection.prepareStatement(discount);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			int discountPrice = rs.getInt("discount");
			totalDiscount = totalDiscount + discountPrice;
		}

		return totalDiscount;
	}
	
	
	public ArrayList deliveryPartner(Connection connection) throws SQLException {
		String paymentMethod = "select name from deliverypartners where status='Active'";

		PreparedStatement stmt = connection.prepareStatement(paymentMethod);

		ArrayList <String>deliveryList=new ArrayList<>();
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			String partner = rs.getString("name");
			deliveryList.add(partner);
		}
		
		return deliveryList;

	}
	
	public void clearCart(Connection connection) throws SQLException {
		String clearQuery = "Truncate table cart";
		PreparedStatement stmt = connection.prepareStatement(clearQuery);
		
		stmt.execute();
	}

}
