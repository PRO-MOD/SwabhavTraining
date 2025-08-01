package com.aurionpro.JDBC.Transaction.Bank.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Application {

	AccountService accountService = new AccountService();

	Scanner scanner = new Scanner(System.in);

	public void start(Connection connection)
			throws SQLException, InValidAmountException, AccountNotFoundException, MaxLoginAttemptsExceededException {
		while (true) {
			System.out.println("***************Welcome to Garhwal Bank******************");
			System.out.println("1.Login");
			System.out.println("2.Add Account");
			System.out.println("3.Forgot PIN");
			System.out.println("4.Exit Application");

			int choose = scanner.nextInt();
			scanner.nextLine();

			switch (choose) {
			case 1 -> {
				doLogin(connection);
			}

			case 2 -> {

				String AccountHolderName = null;
				while (true) {
					System.out.println("Enter Name:");
					AccountHolderName = scanner.nextLine();

					if (!AccountHolderName.matches("^[a-zA-Z ]+$")) {
						System.out.println("Invalid Name type");
						continue;
					}
					break;
				}

				String mobileNumber = null;
				while (true) {
					System.out.println("Enter Mobile Number:");
					mobileNumber = scanner.nextLine();

					if (!mobileNumber.matches("\\d{10}")) {
						System.out.println("Number Should be of 10 digits!");
						continue;
					}
					break;
				}

				double balance = 0.0;
				while (true) {
					System.out.println("Enter initial Deposit Amount");

					try {
						balance = scanner.nextDouble();
						scanner.nextLine();

						if (balance < 0) {
							System.out.println("Amount Should be positive");
							continue;

						}

					}

					catch (InputMismatchException e) {
						System.out.println(e);
						System.out.println("Only numbers allowed");
						scanner.next();
						continue;
					}
					break;
				}

				System.out.println("Generating your Account Number!");

				int accountNumber = 0;
				while (true) {

					accountNumber = (int) (Math.random() * 10000);

					if (!accountService.accountExits(connection, accountNumber)) {
						break;
					}
				}

				System.out.println("Your Account Number:" + accountNumber);

				String pin = null;
				while (true) {
					System.out.println("Set 4 Digits PIN");

					pin = scanner.nextLine();

					if (!pin.matches("\\d{4}")) {
						System.out.println("PIN Should be of 4 Digits!");
						continue;

					}

					break;
				}

				accountService.addAcount(connection, AccountHolderName, accountNumber, balance, mobileNumber, pin);

			}

			case 3 -> {

				int accountNumber = 0;

				String mobileNumber = null;
				while (true) {
					System.out.println("Enter Account Number");
					accountNumber = scanner.nextInt();
					scanner.nextLine();

					System.out.println("Enter Mobile Number:");
					mobileNumber = scanner.nextLine();

					if (!mobileNumber.matches("\\d{10}")) {
						System.out.println("Number Should be of 10 digits!");
						continue;
					}
					if (accountService.authenticateMobile(connection, accountNumber, mobileNumber)) {
						System.out.println("Mobile Number Verified!");
					}

					if (!accountService.authenticateMobile(connection, accountNumber, mobileNumber)) {
						System.out.println("Mobile Number does not match!");
						System.out.println("try Again!");
						continue;
					}

					break;
				}

				System.out.println("Enter updated PIN");
				String PIN = scanner.nextLine();

				accountService.editPIN(connection, accountNumber, mobileNumber, PIN);

			}

			case 4 -> {
				System.out.println("Thank you for Using.....");
				return;
			}

			default -> {
				System.out.println("Invalid Choice!");
			}
			}
		}
	}

	public void doLogin(Connection connection)
			throws SQLException, AccountNotFoundException, MaxLoginAttemptsExceededException {

		final int maxAttempt = 3;

		int attempt = 0;

		int acc = 0;
		while (true) {

			while (true) {
				System.out.print("Enter Account Number: ");
				try {
					acc = scanner.nextInt();
					scanner.nextLine();

					if (!accountService.accountExits(connection, acc)) {
						System.out.println("Account Doesnot exits!");
						System.out.println("Try Different Account!");
						continue;
					}
				}

				catch (InputMismatchException e) {
					System.out.println(e);
					System.out.println("Only numbers allowed");
					scanner.next();
					continue;
				}
				break;
			}

			String pin = null;

			while (true) {
				while (true) {
					System.out.println("Enter 4 Digits PIN");

					pin = scanner.nextLine();

					if (!pin.matches("\\d{4}")) {
						System.out.println("PIN Should be of 4 Digits!");
						continue;

					}
					break;
				}

				if (!accountService.authenticateUser(connection, acc, pin)) {
					System.out.println("Invalid Credentails!");
					attempt++;

					if (attempt >= maxAttempt) {
						throw new MaxLoginAttemptsExceededException("Too many login attempts.");
					}

					System.out.println((maxAttempt - attempt) + " attempts left!");

					System.out.println("Try Again!");
					continue;
				}
				if (accountService.authenticateUser(connection, acc, pin)) {
					System.out.println("Login successfull");
					afterLogin(connection);
					break;
				}

			}

		}
	}

	public void afterLogin(Connection connection) throws SQLException, MaxLoginAttemptsExceededException {

		while (true) {

			System.out.println("**********************Bank Menu*************************");
			System.out.println("1.View Accounts");
			System.out.println("2.View Balance");
			System.out.println("3.Transfer Money");
			System.out.println("4.Deposit Money");
			System.out.println("5.WithDraw Money");
			System.out.println("6.View Transaction History");
			System.out.println("7.Close Account");
			System.out.println("8.View/Edit Profile");
			System.out.println("9.Log Out");
			System.out.println("");

			int choose = 0;
			try {
				choose = scanner.nextInt();
				scanner.nextLine();
			}

			catch (InputMismatchException e) {
				System.out.println(e);
				System.out.println("Only numbers allowed");
				scanner.next();
				continue;
			}

			try {
				switch (choose) {
				case 1 -> {
					accountService.viewAllAcounts(connection);
				}

				case 2 -> {
					final int maxAttempts=3;
					int attempt=0;
					int acc = 0;

					while (true) {
						System.out.print("Enter Account Number: ");
						try {
							acc = scanner.nextInt();
							scanner.nextLine();

							if (!accountService.accountExits(connection, acc)) {
								System.out.println("Account Doesnot exits!");
								System.out.println("Try Different Account!");
								continue;
							}

						}

						catch (InputMismatchException e) {
							System.out.println(e);
							System.out.println("Only numbers allowed");
							scanner.next();
							continue;
						}
						break;
					}

					String pin = null;
					while (true) {
						System.out.println("Enter 4 Digits PIN");

						pin = scanner.nextLine();

						if (!accountService.authenticateUser(connection, acc, pin)) {
							System.out.println("Invalid PIN");
							attempt++;

							if (attempt >= maxAttempts) {
								throw new MaxLoginAttemptsExceededException("Too many login attempts.");
							}

							System.out.println((maxAttempts - attempt) + " attempts left!");

							System.out.println("Try Again!");
							continue;

						}

						break;
					}

					double bal = accountService.checkBalance(connection, acc, pin);
					System.out.println("Balance: ₹" + bal);

				}

				case 3 -> {
					int senderAccNumber = 0;
					final int maxAttempts=3;
					int attempt=0;

					while (true) {
						System.out.println("Enter Sender Account Number:");

						try {
							senderAccNumber = scanner.nextInt();
							scanner.nextLine();

							if (!accountService.accountExits(connection, senderAccNumber)) {
								System.out.println("Sender Account Doesnot exits!");
								System.out.println("Try Different Account!");
								continue;
							}

						}

						catch (InputMismatchException e) {
							System.out.println(e);
							System.out.println("Only numbers allowed");
							scanner.next();
							continue;
						}

						break;
					}

					int receiverAccNumber = 0;

					while (true) {
						System.out.println("Enter Receiver Account Number:");

						try {
							receiverAccNumber = scanner.nextInt();
							scanner.nextLine();

							if (!accountService.accountExits(connection, receiverAccNumber)) {
								System.out.println("Receiver Account Doesnot exits!");
								System.out.println("Try Different Account!");
								continue;
							}

						}

						catch (InputMismatchException e) {
							System.out.println(e);
							System.out.println("Only numbers allowed");
							scanner.next();
							continue;
						}

						break;
					}

					if (senderAccNumber == receiverAccNumber) {
						System.out.println("Sender and Receiver Accounts should be different!");
						continue;
					}

					double amount = 0.0;

					while (true) {
						System.out.println("Enter Amount");

						try {
							amount = scanner.nextDouble();
							scanner.nextLine();

							if (amount <= 0) {
								System.out.println("Amount Should be positive");
								continue;

							}

						}

						catch (InputMismatchException e) {
							System.out.println(e);
							System.out.println("Only numbers allowed");
							scanner.next();
							continue;
						}

						break;
					}

					String pin = null;
					while (true) {
						System.out.println("Enter 4 Digits PIN");

						pin = scanner.nextLine();

						if (!accountService.authenticateUser(connection, senderAccNumber, pin)) {
							System.out.println("Invalid PIN");
							attempt++;

							if (attempt >= maxAttempts) {
								throw new MaxLoginAttemptsExceededException("Too many login attempts.");
							}

							System.out.println((maxAttempts - attempt) + " attempts left!");

							System.out.println("Try Again!");
							continue;

						}

						break;
					}

					accountService.transfer(connection, senderAccNumber, receiverAccNumber, amount, pin);
					System.out.println("Transfer Succesfull");
				}

				case 4 -> {
					final int maxAttempts=3;
					int attempt=0;

					int accountNumber = 0;

					while (true) {
						System.out.print("Enter Account Number: ");

						try {
							accountNumber = scanner.nextInt();
							scanner.nextLine();

							if (!accountService.accountExits(connection, accountNumber)) {
								System.out.println(" Account Doesnot exits!");
								System.out.println("Try Different Account!");
								continue;
							}

						}

						catch (InputMismatchException e) {
							System.out.println(e);
							System.out.println("Only numbers allowed");
							scanner.next();
							continue;
						}
						break;
					}

					int amountDeposit = 0;

					while (true) {
						System.out.println("Enter Amount");

						try {
							amountDeposit = scanner.nextInt();
							scanner.nextLine();

							if (amountDeposit <= 0) {
								System.out.println("Amount Should be positive");
								continue;

							}

						}

						catch (InputMismatchException e) {
							System.out.println(e);
							System.out.println("Only Ingeter Numbers allowed");
							scanner.next();
							continue;
						}

						break;
					}

					String pin = null;
					while (true) {
						System.out.println("Enter 4 Digits PIN");

						pin = scanner.nextLine();

						if (!accountService.authenticateUser(connection, accountNumber, pin)) {
							System.out.println("Invalid PIN");
							attempt++;

							if (attempt >= maxAttempts) {
								throw new MaxLoginAttemptsExceededException("Too many login attempts.");
							}

							System.out.println((maxAttempts - attempt) + " attempts left!");

							System.out.println("Try Again!");
							continue;

						}

						break;
					}

					accountService.deposit(connection, amountDeposit, accountNumber, pin);
					connection.commit();
					System.out.println("Deposit Successfull!");
				}

				case 5 -> {

					int accountNumber = 0;
					final int maxAttempts=3;
					int attempt=0;
					
					while (true) {
						System.out.print("Enter Account Number: ");

						try {
							accountNumber = scanner.nextInt();
							scanner.nextLine();
							if (!accountService.accountExits(connection, accountNumber)) {
								System.out.println(" Account Doesnot exits!");
								System.out.println("Try Different Account!");
								continue;
							}

						}

						catch (InputMismatchException e) {
							System.out.println(e);
							System.out.println("Only numbers allowed");
							scanner.next();
							continue;
						}

						break;
					}

					int amountWithdraw = 0;

					while (true) {
						System.out.println("Enter Amount");

						try {
							amountWithdraw = scanner.nextInt();
							scanner.nextLine();
							if (amountWithdraw <= 0) {
								System.out.println("Amount Should be positive");
								continue;

							}

						}

						catch (InputMismatchException e) {
							System.out.println(e);
							System.out.println("Only Ingeter Numbers allowed");
							scanner.next();
							continue;
						}

						break;
					}

					String pin = null;
					while (true) {
						System.out.println("Enter 4 Digits PIN");

						pin = scanner.nextLine();

						if (!accountService.authenticateUser(connection, accountNumber, pin)) {
							System.out.println("Invalid PIN");
							
							attempt++;

							if (attempt >= maxAttempts) {
								throw new MaxLoginAttemptsExceededException("Too many login attempts.");
							}

							System.out.println((maxAttempts - attempt) + " attempts left!");

							System.out.println("Try Again!");
							continue;

						}

						break;
					}

					accountService.withdraw(connection, amountWithdraw, accountNumber, pin);
					connection.commit();
					System.out.println("Withdraw succesful!");
				}

				case 6 -> {
					int accountNumber = 0;
					
					

					while (true) {
						System.out.print("Enter Account Number: ");

						try {
							accountNumber = scanner.nextInt();

							if (!accountService.accountExits(connection, accountNumber)) {
								System.out.println("Account Doesnot exits!");
								System.out.println("Try Different Account!");
								continue;
							}

						}

						catch (InputMismatchException e) {
							System.out.println(e);
							System.out.println("Only numbers allowed");
							scanner.next();
							continue;
						}

						break;
					}

					accountService.checkTransactionHistory(connection, accountNumber);
				}

				case 7 -> {
					int accountNumber = 0;
					final int maxAttempts=3;
					int attempt=0;

					while (true) {
						System.out.print("Enter Account Number: ");

						try {
							accountNumber = scanner.nextInt();
							scanner.nextLine();

							if (!accountService.accountExits(connection, accountNumber)) {
								System.out.println(" Account Doesnot exits!");
								System.out.println("Try Different Account!");
								continue;
							}

						}

						catch (InputMismatchException e) {
							System.out.println(e);
							System.out.println("Only numbers allowed");
							scanner.next();
							continue;
						}

						break;
					}

					String pin = null;
					while (true) {
						System.out.println("Enter 4 Digits PIN");

						pin = scanner.nextLine();

						if (!accountService.authenticateUser(connection, accountNumber, pin)) {
							System.out.println("Invalid PIN");

							attempt++;

							if (attempt >= maxAttempts) {
								throw new MaxLoginAttemptsExceededException("Too many login attempts.");
							}

							System.out.println((maxAttempts - attempt) + " attempts left!");

							System.out.println("Try Again!");
							continue;

						}

						break;
					}

					accountService.closeAccount(connection, accountNumber, pin);
					System.out.println("Account Closed Successfully!");

				}

				case 8 -> {
					while (true) {
						System.out.println("1.View Profile");
						System.out.println("2.Edit Profile");
						System.out.println("3.Go to Main menu");

						int choice = scanner.nextInt();

						if (choice == 1) {
							System.out.println("Enter Account Number");
							int accountNumber = scanner.nextInt();
							accountService.viewProfile(connection, accountNumber);
						}

						else if (choice == 2) {
							EditProfile(connection);
						}

						else if (choice == 3) {
							break;
						}

						else {
							System.out.println("Enter valid choice!");
						}

					}

				}

				case 9 -> {
					System.out.println("Logging Out......");
					return;
				}

				default -> {
					System.out.println("Invalid Choice!");
					System.out.println("Choose betn 1-8");
				}

				}

//				connection.commit();
			}

			catch (SQLException | AccountNotFoundException | InsufficientBalanceException | InValidAmountException e) {
				try {
					connection.rollback();
					System.out.println("Transaction Failed! Rolled back.");
				} catch (SQLException ex) {
					System.out.println("Roll back Failed!");
				}

				System.out.println(e.getMessage());
			}

		}

	}

	public void EditProfile(Connection connection) throws SQLException {
		while (true) {
			System.out.println("1.Edit Name");
			System.out.println("2.Edit Mobile Number");
			System.out.println("3.Reset PIN");
			System.out.println("4.Back to Previous Menu");

			int choice = scanner.nextInt();

			switch (choice) {
			case 1 -> {
				int accountNumber = 0;
				while (true) {

					System.out.println("Enter Account Number");
					accountNumber = scanner.nextInt();
					scanner.nextLine();

					System.out.println("Enter 4 Digit PIN ");
					String PIN = scanner.nextLine();

					if (accountService.authenticateUser(connection, accountNumber, PIN)) {
						System.out.println("User Authenticated!");
						System.out.println();
						break;
					}

					if (!accountService.authenticateUser(connection, accountNumber, PIN)) {
						continue;
					}

				}

				System.out.println("Enter updated Name");
				String name = scanner.nextLine();

				accountService.editName(connection, accountNumber, name);
			}

			case 2 -> {
				int accountNumber = 0;
				while (true) {

					System.out.println("Enter Account Number");
					accountNumber = scanner.nextInt();
					scanner.nextLine();

					System.out.println("Enter 4 Digit PIN ");
					String PIN = scanner.nextLine();

					if (accountService.authenticateUser(connection, accountNumber, PIN)) {
						System.out.println("User Authenticated!");
						System.out.println();
						break;
					}

					if (!accountService.authenticateUser(connection, accountNumber, PIN)) {
						continue;
					}

				}

				String mobileNumber = null;
				while (true) {
					System.out.println("Enter Mobile Number:");
					mobileNumber = scanner.nextLine();

					if (!mobileNumber.matches("\\d{10}")) {
						System.out.println("Number Should be of 10 digits!");
						continue;
					}
					break;
				}

				accountService.editMobileNumber(connection, accountNumber, mobileNumber);
			}

			case 3 -> {
				int accountNumber = 0;
				while (true) {

					System.out.println("Enter Account Number");
					accountNumber = scanner.nextInt();
					scanner.nextLine();

					System.out.println("Enter 4 Digit PIN ");
					String PIN = scanner.nextLine();

					if (accountService.authenticateUser(connection, accountNumber, PIN)) {
						System.out.println("User Authenticated!");
						System.out.println();
						break;
					}

					if (!accountService.authenticateUser(connection, accountNumber, PIN)) {
						continue;
					}

				}
				String mobileNumber = null;
				while (true) {
					System.out.println("Enter Mobile Number:");
					mobileNumber = scanner.nextLine();

					if (!mobileNumber.matches("\\d{10}")) {
						System.out.println("Number Should be of 10 digits!");
						continue;
					}
					if (accountService.authenticateMobile(connection, accountNumber, mobileNumber)) {
						System.out.println("Mobile Number Verified!");
					}

					if (!accountService.authenticateMobile(connection, accountNumber, mobileNumber)) {
						System.out.println("Mobile Number does not match!");
						System.out.println("try Again!");
						continue;
					}

					break;
				}

				System.out.println("Enter updated PIN");
				String PIN = scanner.nextLine();

				accountService.editPIN(connection, accountNumber, mobileNumber, PIN);
			}

			case 4 -> {
				return;
			}
			}
		}

	}
}
