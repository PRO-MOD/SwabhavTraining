package com.aurionpro.JDBC.Transaction.Bank.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountService {

	public void addAcount(Connection connection, String accountHolderName, int accountNumber, double balance,
			String mobileNumber, String PIN) throws SQLException, InValidAmountException {
		String addAccountQuery = "insert into Accounts (AccountHolderName,AccountNumber,Balance,AccountStatus,MobileNumber,PIN) values(?,?,?,'Active',?,?)";

		if (balance < 0) {
			throw new InValidAmountException("Enter Valid Initial Balance!");
		}

		try (PreparedStatement addAccountStatement = connection.prepareStatement(addAccountQuery)) {
			addAccountStatement.setString(1, accountHolderName);
			addAccountStatement.setInt(2, accountNumber);
			addAccountStatement.setDouble(3, balance);
			addAccountStatement.setString(4, mobileNumber);
			addAccountStatement.setString(5, PIN);

			addAccountStatement.execute();

			connection.commit();

			System.out.println("Account Added Succesfully!");
		}

	}

	public double checkBalance(Connection connection, int accountNumber, String PIN)
			throws SQLException, AccountNotFoundException {
		String checkBalanceQuery = "select balance from Accounts where AccountNumber =? AND PIN=?";

		try (PreparedStatement checkBalanceStatement = connection.prepareStatement(checkBalanceQuery)) {
			checkBalanceStatement.setInt(1, accountNumber);
			checkBalanceStatement.setString(2, PIN);

			ResultSet resultSetBalance = checkBalanceStatement.executeQuery();

			if (resultSetBalance.next()) {
				return resultSetBalance.getDouble("balance");
			}

			throw new AccountNotFoundException("Account Not Found");
		}

	}

	public void viewAllAcounts(Connection connection) throws SQLException {
		String viewAllAccountsQuery = "select * from Accounts where AccountStatus='Active'";

		try (PreparedStatement viewAllAccountsStatement = connection.prepareStatement(viewAllAccountsQuery)) {
			ResultSet resultSetViewAllAccounts = viewAllAccountsStatement.executeQuery();

			while (resultSetViewAllAccounts.next()) {
				System.out.println("Id:" + resultSetViewAllAccounts.getInt("id") + "   AccountHolderName:"
						+ resultSetViewAllAccounts.getString("AccountHolderName") + "   AccountNumber:"
						+ resultSetViewAllAccounts.getString("AccountNumber") + "   Balance:₹"
						+ resultSetViewAllAccounts.getDouble("Balance") + "   Mobile Number:"
						+ resultSetViewAllAccounts.getString("MobileNumber") + "   AccountStatus:"
						+ resultSetViewAllAccounts.getString("AccountStatus"));
			}
		}

	}

	public void withdraw(Connection connection, double amount, int accountNumber, String PIN)
			throws SQLException, InsufficientBalanceException, AccountNotFoundException, InValidAmountException {
		String withdrawQuery = "update Accounts set balance=balance-? where AccountNumber=? and AccountStatus='Active' and PIN=?";

		double balance = checkBalance(connection, accountNumber, PIN);

		if (amount <= 0) {
			throw new InValidAmountException("Amount should be greater then 0");
		}

		if (balance < amount) {
			throw new InsufficientBalanceException("Not Enough Balance!");
		}

		try (PreparedStatement withdrawStatement = connection.prepareStatement(withdrawQuery)) {
			withdrawStatement.setDouble(1, amount);
			withdrawStatement.setInt(2, accountNumber);
			withdrawStatement.setString(3, PIN);

			withdrawStatement.execute();
			
			

		}

		String fromName = getUserName(connection, accountNumber);

		addToHistory(connection, fromName, accountNumber, "WithDrawn ₹" + amount + " from " + fromName);
		
		

	}

	public void deposit(Connection connection, double amount, int accountNumber, String PIN)
			throws SQLException, InValidAmountException, AccountNotFoundException {
		String depositQuery = "update Accounts set balance=balance+? where AccountNumber=? and AccountStatus='Active' AND PIN=?";

		if (amount <= 0) {
			throw new InValidAmountException("Amount should be greater then 0");
		}

		try (PreparedStatement depositStatement = connection.prepareStatement(depositQuery)) {

			depositStatement.setDouble(1, amount);
			depositStatement.setInt(2, accountNumber);
			depositStatement.setString(3, PIN);

			depositStatement.execute();
			
			

		}

		String fromName = getUserName(connection, accountNumber);

		addToHistory(connection, fromName, accountNumber, "Deposited ₹" + amount + " to " + fromName);

		
	}

	public void checkTransactionHistory(Connection connection, int accountNumber) throws SQLException {
		String viewTransactionHistoryQuery = "select * from TransactionHistory where AccountNumber=? and AccountStatus='Active'";

		try (PreparedStatement viewTransactionHistoryStatement = connection
				.prepareStatement(viewTransactionHistoryQuery)) {
			viewTransactionHistoryStatement.setInt(1, accountNumber);

			ResultSet resultSetViewTransactionHistory = viewTransactionHistoryStatement.executeQuery();

			while (resultSetViewTransactionHistory.next()) {
				System.out.println("Id:" + resultSetViewTransactionHistory.getInt("id") + "   AccountHolderName:"
						+ resultSetViewTransactionHistory.getString("AccountHolderName") + "   Mobile Number:"
						+ resultSetViewTransactionHistory.getString("AccountNumber") + "   TransactionDetails:"
						+ resultSetViewTransactionHistory.getString("TransactionDetails") + "   TimeStamp:"
						+ resultSetViewTransactionHistory.getTimestamp("TimeStamp") + "   AccountStatus:"
						+ resultSetViewTransactionHistory.getString("AccountStatus"));
			}

			System.out.println("");
		}

	}

	public void addToHistory(Connection connection, String accountHolderName, int accountNumber,
			String transactionDetails) throws SQLException {
		String addToHistoryQuery = "insert into TransactionHistory (AccountHolderName,AccountNumber,TransactionDetails,TimeStamp,AccountStatus) values(?,?,?,now(),'Active')";

		try (PreparedStatement addToHistoryStatement = connection.prepareStatement(addToHistoryQuery)) {
			addToHistoryStatement.setString(1, accountHolderName);
			addToHistoryStatement.setInt(2, accountNumber);
			addToHistoryStatement.setString(3, transactionDetails);

			addToHistoryStatement.execute();
		}
		
		

	}

	public String getUserName(Connection connection, int accountNumber) throws SQLException, AccountNotFoundException {
		String getName = "select AccountHolderName from Accounts where AccountNumber=? and AccountStatus='Active'";

		try (PreparedStatement nameStatement = connection.prepareStatement(getName)) {
			nameStatement.setInt(1, accountNumber);
			ResultSet rs2 = nameStatement.executeQuery();

			if (rs2.next()) {
				return rs2.getString("AccountHolderName");
			}
			throw new AccountNotFoundException("Account Not Found!");
		}

	}

	public void transfer(Connection connection, int fromAccount, int toAccount, double amount, String PIN)
			throws AccountNotFoundException, SQLException, InsufficientBalanceException, InValidAmountException {

		withdraw(connection, amount, fromAccount, PIN);
		deposit(connection, amount, toAccount, getPIN(connection,toAccount));

		String fromName = getUserName(connection, fromAccount);
		String toName = getUserName(connection, toAccount);

		addToHistory(connection, fromName, fromAccount, "Transferred ₹" + amount + " to " + toName);
		addToHistory(connection, toName, toAccount, "Received ₹" + amount + " from " + fromName);
		
		connection.commit();

	}

	public boolean accountExits(Connection connection, int accountNumber) throws SQLException {
		String exitsQuery = "select * from accounts where AccountNumber=? and AccountStatus='Active'";

		try (PreparedStatement accountExitsStatement = connection.prepareStatement(exitsQuery)) {
			accountExitsStatement.setInt(1, accountNumber);

			ResultSet resultSetAccountExits = accountExitsStatement.executeQuery();

			if (resultSetAccountExits.next()) {
				return true;
			}
		}

		return false;

	}

	public void closeAccount(Connection connection, int accountNumber, String PIN) throws SQLException {
		String closeAccountQuery = "update accounts set AccountStatus='InActive' where AccountNumber=? AND PIN=?";

		try (PreparedStatement closeAccountStatement = connection.prepareStatement(closeAccountQuery)) {
			closeAccountStatement.setInt(1, accountNumber);
			closeAccountStatement.setString(2, PIN);
			closeAccountStatement.execute();
		}
		
		connection.commit();
	}

	public boolean authenticateUser(Connection connection, int accountNumber, String PIN) throws SQLException {
		String authenticateUserQuery = "select * from accounts where AccountNumber=? and AccountStatus='Active' and PIN=?";

		try (PreparedStatement authenticateUserStatement = connection.prepareStatement(authenticateUserQuery)) {
			authenticateUserStatement.setInt(1, accountNumber);
			authenticateUserStatement.setString(2, PIN);

			ResultSet resultSetauthenticateUser = authenticateUserStatement.executeQuery();

			if (resultSetauthenticateUser.next()) {
				return true;
			}
		}

		return false;
	}

	public void viewProfile(Connection connection, int accountNumber) throws SQLException {
		String viewProfileQuery = "select * from accounts where AccountNumber=? and AccountStatus='Active'";

		try (PreparedStatement viewProfileStatement = connection.prepareStatement(viewProfileQuery)) {
			viewProfileStatement.setInt(1, accountNumber);

			ResultSet resultSetViewProfile = viewProfileStatement.executeQuery();

			while (resultSetViewProfile.next()) {
				System.out.println("AccountHolderName:" + resultSetViewProfile.getString("AccountHolderName"));
				System.out.println("Account Number:" + resultSetViewProfile.getString("AccountNumber"));
				System.out.println("Mobile Number:" + resultSetViewProfile.getString("MobileNumber"));
				System.out.println("Balance:" + resultSetViewProfile.getString("Balance"));
				System.out.println("Account Status:" + resultSetViewProfile.getString("AccountStatus"));
			}

			System.out.println("");
		}
	}

	public void editName(Connection connection, int accountNumber, String AccountHolderName) throws SQLException {
		String editNameQuery = "Update accounts set AccountHolderName=? where AccountNumber=?";

		try (PreparedStatement editNameStatement = connection.prepareStatement(editNameQuery)) {
			editNameStatement.setString(1, AccountHolderName);
			editNameStatement.setInt(2, accountNumber);
			editNameStatement.execute();

			System.out.println("AccountHolder Name Updated Successfully!");
		}
		
		connection.commit();
	}

	public void editMobileNumber(Connection connection, int accountNumber, String mobileNumber) throws SQLException {
		String editMobileNumberQuery = "Update accounts set MobileNumber=? where AccountNumber=?";

		try (PreparedStatement editMobileNumberStatement = connection.prepareStatement(editMobileNumberQuery)) {
			editMobileNumberStatement.setString(1, mobileNumber);
			editMobileNumberStatement.setInt(2, accountNumber);
			editMobileNumberStatement.execute();

			System.out.println("Mobile Number Updated Successfully!");
		}
		connection.commit();
	}

	public void editPIN(Connection connection, int accountNumber, String mobileNumber, String PIN) throws SQLException {
		String editPINQuery = "Update accounts set PIN=? where AccountNumber=? and  MobileNumber=?";

		try (PreparedStatement editPINStatement = connection.prepareStatement(editPINQuery)) {
			editPINStatement.setString(1, PIN);
			editPINStatement.setInt(2, accountNumber);
			editPINStatement.setString(3, mobileNumber);
			editPINStatement.execute();

			System.out.println("PIN Updated Successfully!");

		}
		connection.commit();
	}
	
	
	
	public boolean authenticateMobile(Connection connection, int accountNumber,String mobileNumber) throws SQLException {
		String authenticateMobileQuery = "select * from accounts where AccountNumber=? and AccountStatus='Active' and MobileNumber=?";

		try (PreparedStatement authenticateMobileStatement = connection.prepareStatement(authenticateMobileQuery)) {
			authenticateMobileStatement.setInt(1, accountNumber);
			authenticateMobileStatement.setString(2, mobileNumber);

			ResultSet resultSetauthenticateMobile = authenticateMobileStatement.executeQuery();

			if (resultSetauthenticateMobile.next()) {
				return true;
			}
		}

		return false;
	}
	
	public String getPIN(Connection connection,int accountNumber) throws SQLException, AccountNotFoundException {
		String getPINQuery="select PIN from accounts where AccountNumber=?";
		
		
		try (PreparedStatement getPINStatement = connection.prepareStatement(getPINQuery)) {
			getPINStatement.setInt(1, accountNumber);
			ResultSet rs2 = getPINStatement.executeQuery();

			if (rs2.next()) {
				return rs2.getString("PIN");
			}
			throw new AccountNotFoundException("Account Not Found!");
		}
	}
}
