
/**
* UserInterface.java
*/
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
//import java.util.ArrayList;

public class UserInterface {

	final int NUM_EMPLOYEES = 8;
	final int NUM_CUSTOMERS = 12;
	protected Heap<Order> orders;
	protected HashTable<Customer> customers;
	protected HashTable<Employee> employees;
	protected BST<TechProduct> techProductByName;
	protected BST<TechProduct> techProductByModelNum;
	private Scanner userInput;

	UserInterface() {
		customers = new HashTable<>(NUM_CUSTOMERS);
		employees = new HashTable<>(NUM_EMPLOYEES);
		techProductByName = new BST<>();
		techProductByModelNum = new BST<>();
		ArrayList data = new ArrayList<Order>();
		orders = new Heap<Order>(data, new OrderComparator());
		userInput = new Scanner(System.in);
	}

	public void closeUserInput() {
		userInput.close();
	}

	public Scanner getUserInput() {
		return this.userInput;
	}

	public BST<TechProduct> getProductBST_Name() {
		return techProductByName;
	}

	public BST<TechProduct> getProductBST_ModelNum() {
		return techProductByModelNum;
	}

	public HashTable<Customer> getCustomerTable(){
		return customers;
	}

	public Heap<Order> getOrders() {
		return orders;
	}

	/**
	 * Calculates the priority of an Order based on shipping speed and date
	 * Helper method to placeOrder
	 * @param shippingSpeed the speed of the Order
	 * @param date the date the Order was placed
	 * @return priority the priority of the Order to add
	 */
	public long calculatePriority(int shippingSpeed, String date) {
		long priority = 0;
		String s1, s2;
		
		s1 = "" + shippingSpeed;
		s2 = date.replaceAll("\\s", "");
		s2 = s2.replaceAll("\\D", "");
		s1 = s1 + s2;
		
		//System.out.println(s1);
		//System.out.println(s2);
		
		priority = Long.parseLong(s1);
		
		return priority;
	}
	

	/**
	 * Creates a new account for the user
	 * 
	 * @return customer the new Customer account
	 */
	public Customer createCustomerAccount() {
		String firstName, lastName, login, password, address, city, state, zip;
		Customer customer;

		System.out.print("\nPlease enter your first name: ");
		firstName = userInput.nextLine();
		System.out.print("Please enter your last name: ");
		lastName = userInput.nextLine();
		System.out.print("Please enter your login: ");
		login = userInput.nextLine();
		System.out.print("Please enter your password: ");
		password = userInput.nextLine();
		//add address city state zip
		System.out.print("Please enter your address: ");
		address = userInput.nextLine();
		System.out.print("Please enter your city: ");
		city = userInput.nextLine();
		System.out.print("Please enter your state: ");
		state = userInput.nextLine();
		System.out.print("Please enter your zip: ");
		zip = userInput.nextLine();

		customer = new Customer(firstName, lastName, login, password, address, city, state, zip);
		customers.insert(customer);

		System.out.println("\nWelcome " + customer.getFirstName() + "!");
		return customer;
	}

	/**
	 * Prompts the customer for the way they would like to log in Calls the
	 * respective method for login depending on customer choice
	 * 
	 * @return customer the customer account
	 */
	public Customer customerLogin() {
		System.out
				.println("\nWould you like to login as a guest, create an account, or sign into an existing account?");
		System.out.print("Enter \'G\' to login as a guest, \'C\' to create an account, or \'S\' to sign in: ");

		String choice = userInput.nextLine();
		do {
			if (choice.equalsIgnoreCase("G")) {
				return guestLogin();
			} else if (choice.equalsIgnoreCase("C")) {
				return createCustomerAccount();
			} else if (choice.equalsIgnoreCase("S")) {
				return existingLogin();
			} else {
				System.out.println("Invalid input. Please enter \'G\', \'C\', or \'S\'");
			}
		} while (choice != "G" && choice != "g" && choice != "C" && choice != "c" && choice != "S" && choice != "s");

		return null;
	}

	public Employee employeeLogin() {
		String login, password, choice = "";
		Employee employee;

		System.out.print("\nPlease enter your login: ");
		login = userInput.nextLine();
		System.out.print("Please enter your password: ");
		password = userInput.nextLine();
		employee = new Employee(login, password);

		if (!employees.contains(employee)) {
			System.out.println("\nIt seems that we can't find your account... ");
			System.out.println("Would you like to try again or quit?");
			do {
				System.out.print("Enter \'T\' to try again or \'Q\' to quit: ");
				choice = userInput.nextLine();
				if (choice.equalsIgnoreCase("T")) {
					return employeeLogin();
				} else if (choice.equalsIgnoreCase("Q")) {
					return null;
				} else {
					System.out.println("\nInvalid input.\n");
				}
			} while (choice != "T" || choice != "t" || choice != "Q" || choice != "q");
		}

		employee = employees.get(employee);
		System.out.println("\nWelcome " + employee.getFirstName() + "!");

		return employee;
		// the employee the option to quit
	}

	/**
	 * Prompts user for login info and checks for existing account
	 * 
	 * @return customer the customer account
	 */
	public Customer existingLogin() {
		String login, password, choice;

		System.out.print("\nPlease enter your login: ");
		login = userInput.nextLine();
		System.out.print("Please enter your password: ");
		password = userInput.nextLine();
		Customer customer = new Customer("", "", login, password);

		if (!customers.contains(customer)) {
			System.out.println("\nIt seems that we can't find your account... ");
			System.out.println("Would you like to make a new account, try to log in again, or quit?");
			do {
				System.out.print("Enter \'N\' to make a new account, \'L\' to log in again, or \'Q\' to quit: ");
				choice = userInput.nextLine();
				if (choice.equalsIgnoreCase("L")) {
					return existingLogin();
				} else if (choice.equalsIgnoreCase("N")) {
					return createCustomerAccount();
				} else if (choice.equalsIgnoreCase("Q")) {
					return null;
				} else {
					System.out.println("\nInvalid input.\n");
				}
			} while (choice != "N" || choice != "n" || choice != "L" || choice != "l");
		}

		customer = customers.get(customer);
		System.out.println("\nWelcome " + customer.getFirstName() + "!");

		return customer;
	}

	/**
	 * Logs in customer as a guest
	 * 
	 * @return customer the customer account
	 */
	public Customer guestLogin() {
		Customer customer = new Customer();
		customer.setFirstName("Guest");
		customer.setLastName("Guest");
		System.out.println("\nWelcome Guest!");
		return customer;
	}

	/**
	 * To read in customerFile
	 */
	private void loadCustomers(File file) {
		String firstName, lastName, login, password, address, city, state, zip;
		try {
			Scanner fileInput = new Scanner(file);
			while (fileInput.hasNextLine()) {
				firstName = fileInput.nextLine();
				if(firstName.equals("")){
					fileInput.close();
					return;
				}
				lastName = fileInput.nextLine();
				login = fileInput.nextLine();
				password = fileInput.nextLine();
				address = fileInput.nextLine();
				city = fileInput.nextLine();
				state = fileInput.nextLine();
				zip = fileInput.nextLine();
				// System.out.println(new Customer(firstName, lastName, login, password,
				// address, city, state, zip));
				int numShipped = Integer.parseInt(fileInput.nextLine());
				List<TechProduct> shippedProductsInOrder = new List<>();
				List<Order> orderShipped = new List<>();
				// System.out.println(numShipped);

				// if (numShipped == 0) {
				// 	numShipped = 0;
				// 	fileInput.nextLine();
				// 	orderShipped = new List<>();
				// 	shippedProductsInOrder = new List<>();
				// }

				if (numShipped > 0) {
					// int numOfItems = fileInput.nextInt();
					for (int i = 0; i < numShipped; i++) {

						int numOfItems = Integer.parseInt(fileInput.nextLine());
						// fileInput.nextLine();
						for (int j = 0; j < numOfItems; j++) {
							String deviceName = fileInput.nextLine();
							String brand = fileInput.nextLine();
							String modelNum = fileInput.nextLine();
							double msrp = Double.parseDouble(fileInput.nextLine());
							int year = Integer.parseInt(fileInput.nextLine());
							String desc = fileInput.nextLine();
							TechProduct orderedProduct = new TechProduct(deviceName, brand, modelNum, msrp, year, desc);
							shippedProductsInOrder.addLast(orderedProduct);
						}
						// System.out.println(shippedProductsInOrder);
						String date = fileInput.nextLine();
						// fileInput.nextLine();
						int shippingSpeed = Integer.parseInt(fileInput.nextLine());
						long priority = Long.parseLong(fileInput.nextLine());
						Order shippedOrder = new Order(
								new Customer(firstName, lastName, login, password, address, city, state, zip), date,
								shippedProductsInOrder, shippingSpeed, priority);
						orderShipped.addLast(shippedOrder);
						shippedProductsInOrder = new List<>();
						// System.out.println(shippedOrder);
					}

				}
				else{
					numShipped = 0;
					//int numOfItems = Integer.parseInt(fileInput.nextLine());
					orderShipped = new List<>();
					shippedProductsInOrder = new List<>();
				}

				int numUnshipped = Integer.parseInt(fileInput.nextLine());

				List<TechProduct> unshippedOrders = new List<>();
				List<Order> orderunShipped = new List<>();

				// if (numUnshipped == 0) {
				// 	numUnshipped = 0;
				// 	orderunShipped = new List<>();
				// 	unshippedOrders = new List<>();
				// }

				if (numUnshipped > 0) {
					// int numOfItems = fileInput.nextInt();
					for (int i = 0; i < numUnshipped; i++) {
						int numOfItems = Integer.parseInt(fileInput.nextLine());
						// fileInput.nextLine();
						// System.out.println(numOfItems);
						for (int j = 0; j < numOfItems; j++) {
							String deviceName = fileInput.nextLine();
							String brand = fileInput.nextLine();
							String modelNum = fileInput.nextLine();
							double msrp = Double.parseDouble(fileInput.nextLine());
							int year = Integer.parseInt(fileInput.nextLine());
							String desc = fileInput.nextLine();
							TechProduct orderedProduct = new TechProduct(deviceName, brand, modelNum, msrp, year, desc);
							unshippedOrders.addLast(orderedProduct);
							// System.out.println(orderedProduct);
							// System.out.println(unshippedOrders);
						}
						String date = fileInput.nextLine();
						int shippingSpeed = Integer.parseInt(fileInput.nextLine());
						long priority = Long.parseLong(fileInput.nextLine());
						Order unshippedOrder = new Order(
								new Customer(firstName, lastName, login, password, address, city, state, zip), date,
								unshippedOrders, shippingSpeed, priority);
						orderunShipped.addLast(unshippedOrder);
						orders.insert(unshippedOrder);
						unshippedOrders = new List<>();
						// System.out.println(orderunShipped);
						// System.out.println("check 1");
					}
					// System.out.println(unshippedOrders);
				}
				else{
					numUnshipped = 0;
					orderunShipped = new List<>();
					unshippedOrders = new List<>();
					//int numOfItems = Integer.parseInt(fileInput.nextLine());
				}
				// else{
				// orderunShipped = new List<Order>();
				// numUnshipped = 0;
				// }
				// System.out.println("check 2");
				customers.insert(new Customer(firstName, lastName, login, password, address, city, state, zip,
					orderShipped, orderunShipped, numShipped, numUnshipped));
				// System.out.println("check");
				// System.out.println(new Customer(firstName, lastName, login, password,
				// address, city, state, zip, orderShipped, orderunShipped, numShipped,
				// numUnshipped));
			}
			fileInput.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		//System.out.println(customers);
		// System.out.println(customers.toString());
		//customers.printTable();
		// System.out.println(orders.peek());
		// System.out.println(orders.getHeapSize());
	}

	/**
	 * To read in employeeFile
	 */
	private void loadEmployees(File file) {
		String firstName, lastName, login, password;
		try {
			Scanner fileInput = new Scanner(file);
			while (fileInput.hasNextLine()) {
				firstName = fileInput.nextLine();
				lastName = fileInput.nextLine();
				login = fileInput.nextLine();
				password = fileInput.nextLine();
				employees.insert(new Employee(firstName, lastName, login, password));
			}
			fileInput.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * To read in productFile()
	 */
	private void loadProducts(File file) {
		String deviceName, brand, modelNum, desc;
		double msrp;
		int year;
		NameComparator nc = new NameComparator();
		modelNumComparator mc = new modelNumComparator();

		try {
			Scanner fileInput = new Scanner(file);
			while (fileInput.hasNextLine()) {
				deviceName = fileInput.nextLine();
				if(deviceName.equals("")){
					fileInput.close();
					return;
				}
				brand = fileInput.nextLine();
				modelNum = fileInput.nextLine();
				msrp = Double.parseDouble(fileInput.nextLine());
				year = Integer.parseInt(fileInput.nextLine());
				desc = fileInput.nextLine();
				TechProduct product = new TechProduct(deviceName, brand, modelNum, msrp, year, desc);
				techProductByName.insert(product, nc);
				techProductByModelNum.insert(product, mc);
			}
			fileInput.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		//techProductByModelNum.inOrderPrint();
		//techProductByName.preOrderPrint();
		//System.out.println(techProductByName.getRoot());
		// System.out.println(techProductByName.getSize());
	}

	public void renderCustomerMenu(Customer customer, BST<TechProduct> name, BST<TechProduct> modelNum, File pFile, File cFile, UserInterface ui, Heap<Order> o){
		boolean done = false;
		CustomerInterface ci = new CustomerInterface(customer);
		while(done != true){
			ci.customerPrompt();
			String userChoice = userInput.nextLine();
			switch(userChoice){
				case "A":
				case "a":
					ci.searchProduct(name, modelNum);
					break;
				case "B":
				case "b":
					ci.listProducts(name, modelNum);
					break;
				case "C":
				case "c":
					if(customer.getFirstName().equals("Guest")) {
						String choice;
						
						System.out.println("\nIt seems that we don't have enough information to place an order.");
						System.out.println("Would you like to create a new account?");
						System.out.print("Enter \'Y\' for Yes or \'N\' for No: ");
						choice = userInput.nextLine();
						if(choice.equalsIgnoreCase("N")) {
							return;
						}
						customer = createCustomerAccount();
						ci = new CustomerInterface(customer);
					}
					ci.placeOrder(name, customer, o);
					break;
				case "D":
				case "d":
					ci.viewOrders();
					break;
				case "E":
				case "e":
					//quit method
					if(!(customer.getFirstName().equals("Guest"))){
						ci.quit(cFile, pFile, ui);
						done = true;
					}
					else{
						done = true;
					}
					break;
				default:
				System.out.println("Invalid Choice");
					break;
	
			}
		}
	}

	public void renderEmployeeMenu(Employee e, BST<TechProduct> name, BST<TechProduct> modelNum, HashTable<Customer> c, File pFile, File cFile, UserInterface ui, Heap<Order> o){
		boolean done = false;
		EmployeeInterface ei = new EmployeeInterface(e);
		while(done != true){
			ei.employeePrompt();
			String userChoice = userInput.nextLine();
			switch(userChoice){
				case "A":
				case "a":
					ei.searchCustomer(c);
					break;
				case "B":
				case "b":
					ei.displayCustomers(c);
					break;
				case "C":
				case "c":
					ei.viewOrdersByPriority(o);
					break;
				case "D":
				case "d":
					ei.shipOrder(o, c);
					break;
				case "E":
				case "e":
					ei.listProducts(name, modelNum);
					break;
				case "F":
				case "f":
					ei.addProduct(name, modelNum);
					break;
				case "G":
				case "g":
					ei.removeProduct(name, modelNum);
					break;
				case "H":
				case "h":
					//quit
					ei.quit(cFile, pFile, ui);
					done = true;
					break;
				default:
				System.out.println("Invalid Choice");
					break;
			}
		}
	}

	public void quit(File cFile, File pFile, UserInterface ui){ //Takes care of writing to files
		
		try{
			// Creates a FileOutputStream
			FileOutputStream file = new FileOutputStream(pFile);
			// Creates a PrintStream
			PrintStream output = new PrintStream(file);
			ui.getProductBST_Name().write(output);
		}catch(IOException e){
			e.printStackTrace();
		}
		try{
			// Creates a FileOutputStream
			FileOutputStream file1 = new FileOutputStream(cFile);
			// Creates a PrintStream
			PrintStream output1 = new PrintStream(file1);
			ui.getCustomerTable().write(output1);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		// System.out.println("Welcome to Tech Inc.!\n");
		System.out.println("Welcome to the 22C Tech Store!\n");

		UserInterface ui = new UserInterface();
		String userType;

		// will shipped orders and unshipped orders be stored in 2 separate heaps?, no there should only be one heap for unshipped orders
		// there needs to be a way to display shipped orders after removing them from the heap
		File customerFile = new File("customers.txt");
		File employeeFile = new File("employees.txt");
		File productFile = new File("techproducts.txt");
		//File productFile1 = new File("techproductTEST.txt");
		
		ui.loadCustomers(customerFile);
		ui.loadEmployees(employeeFile);
		ui.loadProducts(productFile);
		
		Customer customer = new Customer();
		Employee employee = new Employee();

		System.out.println("Are you a customer or employee?");
		System.out.print("Enter \'C\' for Customer or \'E\' for Employee: ");
		userType = ui.getUserInput().nextLine();
		
		if(userType.equalsIgnoreCase("C")) {
			customer = ui.customerLogin();
			if(customer != null) {
				//CustomerInteface ci = new CustomerInterface(customer);
				ui.renderCustomerMenu(customer, ui.getProductBST_Name(), ui.getProductBST_ModelNum(), productFile, customerFile, ui, ui.getOrders());
			}
		}
		else if(userType.equalsIgnoreCase("E")) {
			employee = ui.employeeLogin();
			if(employee != null) {
				ui.renderEmployeeMenu(employee, ui.getProductBST_Name(), ui.getProductBST_ModelNum(), ui.getCustomerTable(), productFile, customerFile, ui, ui.getOrders());
				// EmployeeInterface ei = new EmployeeInterface(employee);
			}
		}
		ui.getUserInput().close();
		System.out.println("\nGoodbye! Thank you for visiting!");
	}
}
