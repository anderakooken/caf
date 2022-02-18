package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import application.model.Funcionario;
import application.model.Registro;

public class RefeitorioDao {
	private static Connection con;
	public RefeitorioDao() {
		new ConnectionFactory();
		con = ConnectionFactory.getConnection();
	}
	
	public DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public List<String> lista() {
		String sql = "SELECT * FROM tb_tipos_refeicoes";
		int data = Integer.parseInt(df.format(LocalDateTime.now()).substring(11,13));
		List<String> lista = new ArrayList<>();
			
			try {
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					
						if(data >= rs.getInt("hr_ini") && data <= rs.getInt("hr_fim")) {
							lista.add(rs.getString("nmtipo"));
							lista.add(rs.getString("vrunit"));
							lista.add(rs.getString("idtipo"));
					
						}
				}
				}catch(SQLException e) {
				e.printStackTrace();
			}
		
		return lista;
	}
	
	public void CREG(Registro reg) {
		String sql = "INSERT INTO tb_registros SET matricula = '" + reg.get("matricula") + "', dtreg ='" + reg.get("datareg") + "',"
				+ " hrreg = '"+reg.get("horareg") + "', idusuario='" + reg.get("idusuario") + "', statusreg='"+reg.get("status")+"',"
				+ " reg_empregado = '"+reg.get("registro") +"', idnegado='"+reg.get("idnegado")+"', idtipo = '"+reg.get("idtipo") + "',"
				+ " vrunit= '"+reg.get("vrunit") + "'";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.execute();
			ps.close();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Funcionario> Registro(String valor, String tabela){
		//Parametro funcionario para pegar os dados pela matricula, e pegar o ultimo registro feito
		List<Funcionario> fun = new ArrayList<>();
		String data = df.format(LocalDateTime.now());
		String sql = "SELECT r.matricula, f.idcartao, r.dtreg, r.hrreg, s.nmstatus, f.nome, f.setor, f.funcao FROM tb_registros r "
				+ "LEFT JOIN tb_status s ON r.statusreg = s.idstatus "
				+ "LEFT JOIN tb_funcionarios f ON r.matricula = f.matricula ";
		if(tabela == null) {
			if(valor == null) {
				sql +=  "WHERE r.dtreg='" + data.substring(0, 10) +"' "
						+ " ORDER BY r.hrreg DESC";
			}else {
				sql += "WHERE r.matricula = " + valor
						+" ORDER BY dtreg DESC LIMIT 1";
			}
		}else {
			switch(tabela.toLowerCase()) {
			case "funcionarios":
				sql = "SELECT * FROM tb_funcionarios WHERE idcartao = '" + valor + "'";
				break;
			}
		}
		
				
			try {
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
					if(valor == null) {
						while(rs.next()) {
							Funcionario func = new Funcionario();
							func.set("matricula", rs.getString("matricula"));
							func.set("nome", rs.getString("nome"));
							func.set("setor", rs.getString("setor"));
							func.set("funcao", rs.getString("funcao"));
							func.set("status", rs.getString("nmstatus"));
							func.set("ultimoacesso", rs.getString("r.hrreg"));
		
							
							fun.add(func);
						}
					}else {
						//Unico funcionario
						if(rs.next()) {
							Funcionario func = new Funcionario();
							func.set("idcartao", rs.getString("idcartao"));
							func.set("matricula", rs.getString("matricula"));
							func.set("nome", rs.getString("nome"));
							func.set("setor", rs.getString("setor"));
							func.set("funcao", rs.getString("funcao"));
							//Se a tabela for nulo, quer dizer q esta pegando da tabela registro, entao pega status e dtreg
							if(tabela ==null) {
							func.set("status", rs.getString("nmstatus"));
								String datareg = rs.getString("r.dtreg");
								func.set("ultimoacesso",  datareg.substring(8,10) 
								+"/"+ datareg.substring(5,7) +"/"+ datareg.substring(0,4) + " " + rs.getString("r.hrreg"));
							}
							fun.add(func);
						}
					}
				rs.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		return fun;
	}

	
	public boolean verifica(String valor) {
		
		String 	sql = "SELECT idcartao FROM tb_funcionarios WHERE idcartao= '" + valor + "';";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public String qtdlib(String valor) {
		String sql = "SELECT qtdlib FROM tb_adcionais WHERE matricula= '" + valor + "';";
		String qtdlib = "";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				qtdlib = rs.getString("qtdlib");
				if(qtdlib == null || qtdlib.isEmpty()) {
					qtdlib = "1";
				}else {
					int i = Integer.parseInt(qtdlib) + 1;
					qtdlib = Integer.toString(i);
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return qtdlib;
	}
	
	public String contagem(String tabela, String where, String coluna, String dia) {
		String cont = "";
		String sql = "SELECT COUNT("+coluna+") FROM "+ tabela + " ";
		 
		if(dia == "hoje") {
			sql += "WHERE dtreg = '" + df.format(LocalDateTime.now()).substring(0, 10) + "' AND statusreg = '1' ";
		}
		if(where != null && dia == null) {
			sql+=  "WHERE " + where + "";
		}else if(where !=null && dia !=null) {
			sql += "AND " + where;
		}
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				cont = rs.getString(1);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return cont;
	}
}
