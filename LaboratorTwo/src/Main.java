/*
 * Vipolnil: Kiseliov Maxim
 * Gruppa: I1602
 * Laboratornaya rabota #1
 */


import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.awt.event.ActionEvent;
import net.proteanit.sql.DbUtils;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;

public class Main {
	//GUI
	private JFrame frame;
	
	//DB vars
	private Connection connection;
	private Properties properties;
	private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/fitgym";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "1234";
	private static final String MAX_POOL = "250";
	
	//SQL vars
	private Statement stm;
	private String sql;
	private ResultSet query;
	
	private JTable table;
	
	//clients table vars
	private JTextField idclient;
	private JTextField fname;
	private JTextField lname;
	private JTextField dob;
	private JTextField phonenumber;
	private JTextField gender;
	private JTextField membership;
	private JTextField persmanager;
	

//START app
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

//UPDATE method
	private int updateDB(String query) {
		int Value = 0;
		try {
			Value = stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Value;
	}

//SELECT method
	private ResultSet selectDB() {
		sql = "SELECT clients.idclient, clients.fname, clients.lname, clients.dob, clients.phonenumber, clients.gender, gympass.passtype, managers.lname_m"
				+ " FROM clients"
				+ " INNER JOIN gympass ON clients.membership = gympass.idpass"
				+ " INNER JOIN managers ON clients.persmanager = managers.idmanager"
				+ " ORDER BY clients.idclient DESC";
		try {
			query = stm.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		table.setModel(DbUtils.resultSetToTableModel(query));
		return query;
	}

//CREATE app
	public Main() {
		initialize();
	}

// DB properties
	private Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
			properties.setProperty("user", USERNAME);
			properties.setProperty("password", PASSWORD);
			properties.setProperty("MaxPooledStatements", MAX_POOL);
		}
		return properties;
	}

//CONT OF FRAME
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(50, 50, 1035, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
//BUTTONS START		
		//CONNECT button
		JButton connect = new JButton("Connect to DB");
		connect.setFont(new Font("Times New Roman", Font.BOLD, 10));
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (connection == null) {
					try {
						Class.forName(DATABASE_DRIVER);
						connection = DriverManager.getConnection(DATABASE_URL, getProperties());
						stm = connection.createStatement();
						selectDB();
						System.out.println("-> Succesfully connected...");
					} catch (ClassNotFoundException | SQLException e) {
						(e).printStackTrace();
					}
				}
			}
		});
		connect.setActionCommand("Connect");
		connect.setBounds(760, 320, 120, 25);
		frame.getContentPane().add(connect);

		//RELOAD button
		JButton btnReload = new JButton("Reload");
		btnReload.setFont(new Font("Times New Roman", Font.BOLD, 10));
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectDB();
				System.out.println("-> Table is reloaded...");
					}
				});
		btnReload.setBounds(890, 320, 120, 25);
		frame.getContentPane().add(btnReload);		
		
		//EXIT button
		JButton exitt = new JButton("EXIT");
		exitt.setFont(new Font("Times New Roman", Font.BOLD, 10));
		exitt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (connection != null) {
					try {
						connection.close();
						connection = null;
						System.out.println("-> Connection is closed...");
						frame.dispose();
						System.out.println("-> App is closed...");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else {
					frame.dispose();
					System.out.println("-> App is closed...");
				}
			}
		});
		exitt.setBounds(890, 570, 120, 25);
		frame.getContentPane().add(exitt);
		
		//ADD CLIENT BUTTON
		JButton addClient = new JButton("Add Client");
		addClient.setFont(new Font("Times New Roman", Font.BOLD, 10));
		addClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String addNewClient = "INSERT INTO clients(fname, lname, dob, gender, phonenumber, membership, persmanager)"
						+ "VALUES ('"+fname.getText()+"', '"+lname.getText()+"', '"+dob.getText()+"', '"+gender.getText()+"', '"+phonenumber.getText()+"', '"+membership.getText()+"', '"+persmanager.getText()+"')";
				
				if (fname.getText().isEmpty() | lname.getText().isEmpty() | dob.getText().isEmpty() | gender.getText().isEmpty()
						| phonenumber.getText().isEmpty() | membership.getText().isEmpty() | persmanager.getText().isEmpty()) {
					System.out.println("Oops looks like we missing something");
				}
				else {
				updateDB(addNewClient);
					System.out.println("New client " + lname.getText() + " " + fname.getText() + " added succesfully");
					fname.setText("");
					lname.setText("");
					dob.setText("");
					gender.setText("");
					phonenumber.setText("");
					membership.setText("");
					persmanager.setText("");
					};
					};
				});
		addClient.setBounds(155, 570, 120, 25);
		frame.getContentPane().add(addClient);
		
		//REMOVE CLIENT
		JButton btnNewButton = new JButton("Remove Client");
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 10));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String removeClient = "DELETE FROM clients WHERE idclient = '"+idclient.getText()+"'";
				
				if (idclient.getText().isEmpty()) {
					System.out.println("Please introduce Client ID");
				}
				else {
				updateDB(removeClient);
					System.out.println("Client with ID: " + idclient.getText() + " removed succesfully");
					idclient.setText("");
					};
					};
				});
		btnNewButton.setBounds(400, 380, 120, 25);
		frame.getContentPane().add(btnNewButton);		
//BUTTONS END
		
//TABLE with data
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 1000, 300);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		table.setRowHeight(25);
		scrollPane.setViewportView(table);
		
		
//FIELDS START
		//CLIENT F NAME field
		fname = new JTextField();
		fname.setBounds(155, 350, 120, 25);
		frame.getContentPane().add(fname);
		fname.setColumns(10);
		
		JLabel fname = new JLabel("Client Name:");
		fname.setFont(new Font("Times New Roman", Font.BOLD, 12));
		fname.setBounds(10, 350, 120, 25);
		frame.getContentPane().add(fname);
		
		//CLIENT L NAME field
		lname = new JTextField();
		lname.setBounds(155, 380, 120, 25);
		frame.getContentPane().add(lname);
		lname.setColumns(10);
		
		JLabel lname = new JLabel("Client Surname:");
		lname.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lname.setBounds(10, 380, 120, 25);
		frame.getContentPane().add(lname);
		
		//CLIENT DOB
		dob = new JTextField();
		dob.setBounds(155, 410, 120, 25);
		frame.getContentPane().add(dob);
		dob.setColumns(10);
		
		JLabel dob = new JLabel("DoB (e.g. YYYY-MM-DD):");
		dob.setFont(new Font("Times New Roman", Font.BOLD, 12));
		dob.setBounds(10, 410, 150, 25);
		frame.getContentPane().add(dob);
		
		//CLIENT GENDER
		gender = new JTextField();
		gender.setBounds(155, 440, 120, 25);
		frame.getContentPane().add(gender);
		gender.setColumns(10);
		
		JLabel gender = new JLabel("Gender (e.g. M/F):");
		gender.setFont(new Font("Times New Roman", Font.BOLD, 12));
		gender.setBounds(10, 440, 120, 25);
		frame.getContentPane().add(gender);
		
		//CLIENT PHONENUMBER
		phonenumber = new JTextField();
		phonenumber.setBounds(155, 470, 120, 25);
		frame.getContentPane().add(phonenumber);
		phonenumber.setColumns(10);
		
		JLabel phonenumber = new JLabel("Phone Num.:");
		phonenumber.setFont(new Font("Times New Roman", Font.BOLD, 12));
		phonenumber.setBounds(10, 470, 120, 25);
		frame.getContentPane().add(phonenumber);
		
		//CLIENT MEMBERSHIP
		membership = new JTextField();
		membership.setBounds(155, 500, 120, 25);
		frame.getContentPane().add(membership);
		membership.setColumns(10);
		
		JLabel membership = new JLabel("Membership ID (e.g. 1/2/3):");
		membership.setFont(new Font("Times New Roman", Font.BOLD, 12));
		membership.setBounds(10, 500, 150, 25);
		frame.getContentPane().add(membership);
		
		//CLIENT PERSONAL MANAGER
		persmanager = new JTextField();
		persmanager.setBounds(155, 530, 120, 25);
		frame.getContentPane().add(persmanager);
		persmanager.setColumns(10);
		
		JLabel persmanager = new JLabel("P. Manager ID (e.g. 1/2):");
		persmanager.setFont(new Font("Times New Roman", Font.BOLD, 12));
		persmanager.setBounds(10, 530, 150, 25);
		frame.getContentPane().add(persmanager);

		//CLIENT ID
		idclient = new JTextField();
		idclient.setBounds(400, 350, 120, 25);
		frame.getContentPane().add(idclient);
		idclient.setColumns(10);
		
		JLabel idclient = new JLabel("Client ID:");
		idclient.setFont(new Font("Times New Roman", Font.BOLD, 12));
		idclient.setBounds(320, 350, 120, 25);
		frame.getContentPane().add(idclient);
//FIELDS END

	}
}
