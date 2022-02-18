package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Main;

public class MainDao {
	private static Connection con;
	@SuppressWarnings("static-access")
	public MainDao() {
		con = new ConnectionFactory().getConnection();
	}
	
	public boolean UserConfirm(String nmuser, String pwrc){
		String sql = "SELECT * FROM tb_usuarios WHERE nmuser=? and login=?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, nmuser);
			ps.setString(2, pwrc);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Main.user.setMatricula(rs.getString("matricula"));
				Main.user.setMatricula_emp(rs.getString("matricula_emp"));
				Main.user.setNmuser(rs.getString("nmuser"));
				Main.user.setNvacesso(rs.getInt("nvacesso"));
				Main.user.setPwrc(rs.getString("login"));
				return true;
			}
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return false;

	}
	
	/*public List<Combobox> ListItems(String ID, String nome, String tabela){
		List<Combobox> lista = new ArrayList<>();
		String sql = "SELECT "+ID+" as id, "+nome+" as nome FROM "+tabela;
		if(!(ID == null || ID.isEmpty() || nome == null || nome.isEmpty() || tabela == null || tabela.isEmpty())) {
			try {
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					Combobox c = new Combobox(rs.getInt("id"), rs.getString("nome"));
					lista.add(c);
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return lista;
	}*/

}