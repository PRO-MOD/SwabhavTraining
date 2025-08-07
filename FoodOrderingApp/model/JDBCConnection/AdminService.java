package com.aurionpro.FoodOrderingApp.model.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminService {

	public boolean adminAuthentication(Connection connection, String username, String password) throws SQLException {
		String adminAuthenticationQuery = "select * from admin_Users where username=? and password=?";

		PreparedStatement adminAuthenticationStatement = connection.prepareStatement(adminAuthenticationQuery);

		adminAuthenticationStatement.setString(1, username);
		adminAuthenticationStatement.setString(2, password);

		ResultSet resultSetAdminAuthentication = adminAuthenticationStatement.executeQuery();

		if (resultSetAdminAuthentication.next()) {
			return true;
		}

		return false;

	}

	public void addFoodItem(Connection connection, String foodName, double foodPrice, double discount, String cuisine,
			String category, String status) throws SQLException {
		String addFoodItemQuery = "insert into foodItems (name,price,discount,cuisineType,category,status) values(?,?,?,?,?,?)";

		PreparedStatement addFoodItemStatement = connection.prepareStatement(addFoodItemQuery);
		addFoodItemStatement.setString(1, foodName);
		addFoodItemStatement.setDouble(2, foodPrice);
		addFoodItemStatement.setDouble(3, discount);
		addFoodItemStatement.setString(4, cuisine);
		addFoodItemStatement.setString(5, category);
		addFoodItemStatement.setString(6, status);

		addFoodItemStatement.execute();

		System.out.println("Food item added Succesfully!");

	}

	public void viewFullMenu(Connection connection) throws SQLException {
		String viewFullMenuQuery = "select * from foodItems";

		PreparedStatement viewFullMenuStatement = connection.prepareStatement(viewFullMenuQuery);
		ResultSet rs = viewFullMenuStatement.executeQuery();
		printResults(rs);

	}

	public void updateFoodName(Connection connection, String name, int id) throws SQLException {
		String updateQuery = "UPDATE foodItems SET name = ? WHERE food_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
			stmt.setString(1, name);
			stmt.setInt(2, id);
			stmt.executeUpdate();
		}
	}

	public void updateFoodPrice(Connection connection, double price, int id) throws SQLException {
		String updateQuery = "UPDATE foodItems SET price = ? WHERE food_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
			stmt.setDouble(1, price);
			stmt.setInt(2, id);
			stmt.executeUpdate();
		}
	}

	public void updateCuisineType(Connection connection, String cuisine, int id) throws SQLException {
		String updateQuery = "UPDATE foodItems SET cuisineType = ? WHERE food_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
			stmt.setString(1, cuisine);
			stmt.setInt(2, id);
			stmt.executeUpdate();
		}
	}

	public void updateCategory(Connection connection, String category, int id) throws SQLException {
		String updateQuery = "UPDATE foodItems SET category = ? WHERE food_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
			stmt.setString(1, category);
			stmt.setInt(2, id);
			stmt.executeUpdate();
		}
	}

	public void updateStatus(Connection connection, String status, int id) throws SQLException {
		String updateQuery = "UPDATE foodItems SET status = ? WHERE food_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
			stmt.setString(1, status);
			stmt.setInt(2, id);
			stmt.executeUpdate();
		}
	}

	public void updateDiscount(Connection connection, double discount, int id) throws SQLException {
		String updateQuery = "UPDATE foodItems SET discount = ? WHERE food_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
			stmt.setDouble(1, discount);
			stmt.setInt(2, id);
			stmt.executeUpdate();
		}
	}

	public void searchByFoodName(Connection connection, String name) throws SQLException {
		String query = "SELECT * FROM foodItems where name = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			printResults(rs);
		}
	}

	public void searchByCuisineType(Connection connection, String cuisine) throws SQLException {
		String query = "SELECT * FROM foodItems WHERE  cuisineType = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, cuisine);
			ResultSet rs = stmt.executeQuery();
			printResults(rs);
		}
	}

	public void searchByCategory(Connection connection, String category) throws SQLException {
		String query = "SELECT * FROM foodItems where category = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, category);
			ResultSet rs = stmt.executeQuery();
			printResults(rs);
		}
	}

	private void printResults(ResultSet rs) throws SQLException {
		boolean found = false;
		while (rs.next()) {
			found = true;
			System.out.println("Id: " + rs.getInt("food_id") + "   Food Name: " + rs.getString("name") + "   Price: ₹"
					+ rs.getDouble("price") + "   Discount: ₹" + rs.getDouble("discount") + "   Cuisine Type: "
					+ rs.getString("cuisineType") + "   Category: " + rs.getString("category")
					+ "   Status: " + rs.getString("status"));
		}
		if (!found) {
			System.out.println("No matching food items found.");
		}
	}
	
	
	public void viewRegisteredCustomer(Connection connection) throws SQLException {
		String query="select * from customers";
		
		PreparedStatement stmt = connection.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();
		boolean found=false;
		while(rs.next()) {
			found=true;
			
			System.out.println("Id: " + rs.getInt("customer_id") + "   Name: " + rs.getString("name") + "   Address: "
					+ rs.getString("address") + "   Phone Number: ₹" + rs.getString("phoneNumber") + "   Email Id: "
					+ rs.getString("emailId"));
		}
		
		if (found==false) {
			System.out.println("No Registered Customers");
		}
	}
	
	

	public void addPartner(Connection connection, String partnerName, String contactNumber,String status) throws SQLException {
		String addPartnerQuery = "insert into deliverypartners (name,contactNumber,status) values(?,?,?)";

		PreparedStatement addPartnerQueryStatement = connection.prepareStatement(addPartnerQuery);
		addPartnerQueryStatement.setString(1, partnerName);
		addPartnerQueryStatement.setString(2, contactNumber);
		addPartnerQueryStatement.setString(3, status);
		

		addPartnerQueryStatement.execute();

		System.out.println("Partner added Succesfully!");

	}
	
	
	public void viewAllDeliveryPartners(Connection connection) throws SQLException {
		String query="select * from deliverypartners";
		
		PreparedStatement stmt = connection.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();
		boolean found=false;
		while(rs.next()) {
			found=true;
			
			System.out.println("Id: " + rs.getInt("partner_id") + "   Name: " + rs.getString("name") + "   Contact Number: "
					+ rs.getString("contactNumber") + "   Status:" + rs.getString("status"));
		}
		
		if (found==false) {
			System.out.println("No Registered Partners");
		}
	}
	
	
	public void deactivateDeliveryPartner(Connection connection, int id) throws SQLException {
		String updateQuery = "UPDATE deliverypartners SET status ='Deactive' WHERE partner_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}
	
	public void reactivateDeliveryPartner(Connection connection, int id) throws SQLException {
		String updateQuery = "UPDATE deliverypartners SET status = 'Active' WHERE partner_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}
	
	public void deleteDeliveryPartner(Connection connection, int id) throws SQLException {
		String deleteQuery = "DELETE from deliverypartners WHERE partner_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}

	
	

	public void addPaymentMethod(Connection connection, String name,String status) throws SQLException {
		String addPaymentMethodQuery = "insert into paymentmethods (method_name,status) values(?,?)";

		PreparedStatement addPaymentMethodStatement = connection.prepareStatement(addPaymentMethodQuery);
		addPaymentMethodStatement.setString(1, name);
		addPaymentMethodStatement.setString(2, status);
		

		addPaymentMethodStatement.execute();

		System.out.println("Payment Method added Succesfully!");

	}
	
	
	public void viewAllPaymentMethods(Connection connection) throws SQLException {
		String query="select * from paymentmethods";
		
		PreparedStatement stmt = connection.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();
		boolean found=false;
		while(rs.next()) {
			found=true;
			
			System.out.println("Id: " + rs.getInt("method_id") + "   Name: " + rs.getString("method_name")+"   Status:" + rs.getString("status"));
		}
		
		if (found==false) {
			System.out.println("No Registered payment Methods");
		}
	}
	
	
	public void deactivatePaymentMethod(Connection connection, int id) throws SQLException {
		String updateQuery = "UPDATE paymentmethods SET status = 'Deactive' WHERE method_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}
	
	public void reactivatePaymentMethod(Connection connection, int id) throws SQLException {
		String updateQuery = "UPDATE paymentmethods SET status = 'Active' WHERE method_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}
	
	public void deletePaymentMethod(Connection connection, int id) throws SQLException {
		String updateQuery = "DELETE from paymentmethods WHERE method_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}

}
