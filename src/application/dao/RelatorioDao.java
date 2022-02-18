package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.model.Combobox;
import application.model.Registro;

public class RelatorioDao {
	Connection con;
	public RelatorioDao() {
		new ConnectionFactory();
		con = ConnectionFactory.getConnection();
	}
	
	public List<Combobox> listItems(){
		List<Combobox> l = new ArrayList<>();
		
		String sql="SELECT idrelatorio as id, descrel as nome FROM tb_relatorios";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Combobox c = new Combobox(rs.getInt("id"), rs.getString("nome"));
				l.add(c);
			}
				
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return l;
	}
	
	public List<Registro> RelatorioList(String tipo, String DataIni, String DataFim){
		List<Registro> l = new ArrayList<>();
		tipo = tipo.toLowerCase();
		String sql = "";
		switch(tipo.toLowerCase()) {
		case "reldia":
			sql ="SELECT r.dtreg, r.idtipo as id, t.nmtipo as nome, Count(r.idreg) AS qtd\r\n"
					+ "FROM tb_status s INNER JOIN tb_registros r ON s.idstatus = r.statusreg INNER JOIN tb_tipos_refeicoes t ON r.idtipo = t.idtipo\r\n"
					+ "WHERE r.statusreg=1\r\n"
					+ "GROUP BY r.dtreg, r.idtipo, t.nmtipo\r\n"
					+ "HAVING r.dtreg Between '"+DataIni+"' And '"+DataFim+"'\r\n"
					+ "ORDER BY r.dtreg, r.idtipo;";
			break;
		case "relperiodo":
			sql ="SELECT r.idtipo as id, t.nmtipo as nome, Count(r.idreg) AS qtd\r\n"
					+ "FROM tb_status s INNER JOIN tb_registros r ON s.idstatus = r.statusreg INNER JOIN tb_tipos_refeicoes t ON r.idtipo = t.idtipo\r\n"
					+ "WHERE r.statusreg=1 and r.dtreg Between '"+DataIni+"' And '"+DataFim+"'\r\n"
					+ "GROUP BY r.idtipo, t.nmtipo\r\n"
					+ "ORDER BY r.dtreg, r.idtipo;";
			break;
		}
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					Registro reg = new Registro();
					if(tipo.toLowerCase().equals("reldia")) {
						reg.set("datareg", rs.getString("dtreg"));
					}
					reg.set("idtipo", rs.getString("id"));
					reg.set("tipo", rs.getString("nome"));
					reg.set("quantidade", rs.getString("qtd"));
					l.add(reg);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return l;
	}
}
