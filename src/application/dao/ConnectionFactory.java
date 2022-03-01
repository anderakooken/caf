package application.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	private static String url = "jdbc:mysql://192.168.254.216:3306/caf?useTimezone=true&serverTimezone=UTC";
	private static String user = "caf";
	private static String password = "CAF2000@";

	public static Connection getConnection() {
		// nome do driver.
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			// endereco e parametros de conexao com o banco.
			return DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
