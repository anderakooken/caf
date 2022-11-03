package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import application.model.Evento;

public class EventoDao {
	/*private Connection con;
	
	public EventoDao() {
		new ConnectionFactory();
		con = ConnectionFactory.getConnection();
	}*/
	
	
	public void CED(int tipo, Evento e) {
		String sql = "";
		String msg = "";
		switch(tipo) {
		case 1:
			sql = "INSERT INTO tb_tipos_refeicoes SET nmtipo='"+ e.get("nome")+"', "
			+ "vrunit='"+e.get("vrunit")+"', hr_ini='"+e.get("horainicial")+"', hr_fim='"+e.get("horafim")+"', idusuario='"+e.get("idusuario")+"'";
			msg = "Evento cadastrado com sucesso!";
			break;
		case 2:
			sql = "UPDATE tb_tipos_refeicoes SET nmtipo='"+ e.get("nome")+"', "
			+ "vrunit='"+e.get("vrunit")+"', hr_ini='"+e.get("horainicial")+"', hr_fim='"+e.get("horafim")+"', idusuario='"+e.get("idusuario")+"' WHERE idtipo='"+e.get("id")+"'";
			msg = "Evento atualizado com sucesso!";
			break;
		case 3:
			sql="DELETE FROM tb_tipos_refeicoes WHERE idtipo='"+e.get("id")+"'";
			msg="Evento deletado com sucesso!";
			break;
		}
		
		try (Connection con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);){
			
			ps.execute();
			ps.close();
			
			Main.dialogBox(msg, 2);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public List<Evento> listEvents(){
		List<Evento> l = new ArrayList<>();
		String sql = "SELECT * FROM tb_tipos_refeicoes";
		
		
		try (Connection con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();){
			
			
			while(rs.next()) {
				Evento e = new Evento();
				e.set("nome", rs.getString("nmtipo"));
				e.set("id", rs.getString("idtipo"));
				e.set("vrunit", rs.getString("vrunit"));
				e.set("horainicial", rs.getString("hr_ini"));
				e.set("horafim", rs.getString("hr_fim"));
				e.set("idusuario", rs.getString("idusuario"));
				
				l.add(e);
			}
			
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return l;
	}


	public List<Evento> listHis(String where) {
		List<Evento> l = new ArrayList<>();
		if(where == null) {
			System.out.println("erro where");
			where = "";
		}
		String sql = "SELECT tb_refeicoes_dia.idlnc, tb_refeicoes_dia.dtlnc, \r\n"
				+ "tb_refeicoes_dia.idtipo, tb_tipos_refeicoes.nmtipo, \r\n"
				+ "tb_refeicoes_dia.qtd, tb_refeicoes_dia.qtdq, tb_refeicoes_dia.iduser\r\n"
				+ "FROM tb_tipos_refeicoes INNER JOIN tb_refeicoes_dia ON tb_tipos_refeicoes.idtipo = tb_refeicoes_dia.idtipo\r\n"
				+ where
				+ "ORDER BY tb_refeicoes_dia.dtlnc DESC , tb_refeicoes_dia.idtipo;";
		

		try (Connection con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();){
			
			
			
			while(rs.next()) {
				Evento e = new Evento();
				e.set("id", rs.getString("idtipo"));
				e.set("data", rs.getString("dtlnc"));
				e.set("nome", rs.getString("nmtipo"));
				e.set("qtdm", rs.getString("qtd"));
				e.set("qq", rs.getString("qtdq"));
				l.add(e);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return l;
	}
}
