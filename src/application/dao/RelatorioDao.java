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
		case "movdiario":
			sql = "SELECT r.dtreg, t.idtipo as id, CONCAT(t.idtipo,' - ', t.nmtipo) AS nome, "
					+ "COUNT(r.idreg) as qtd, s.nmstatus FROM tb_registros r\r\n"
					+ "LEFT JOIN tb_tipos_refeicoes t ON r.idtipo = t.idtipo \r\n"
					+ "LEFT JOIN tb_status s ON r.statusreg = s.idstatus \r\n"
					+ "where (r.dtreg between '"+DataIni+"'  AND '"+DataFim+"') AND (r.statusreg = 1 OR r.statusreg = 3)\r\n"
					+ "GROUP BY r.dtreg, nome";
			break;
		case "faturamentoperiodo":
			sql = "SELECT tb_tipos_refeicoes.indice, tb_tipos_refeicoes.nmtipo as nomereal, CONCAT(tb_tipos_refeicoes.indice,' - ', tb_tipos_refeicoes.nmtipo) as nome, \r\n"
					+ "tb_refeicoes_dia.dtlnc, tb_tipos_refeicoes.nmtipo, (If(q.qtdr is null, 0 ,q.qtdr)) AS qtdrd,\r\n"
					+ "tb_tipos_refeicoes.vrunit, If(tb_refeicoes_dia.qtd is null,0,tb_refeicoes_dia.qtd) AS qtdm,\r\n"
					+ "If(tb_refeicoes_dia.qtdq  is null,0,tb_refeicoes_dia.qtdq) AS qq, (If(tb_refeicoes_dia.qtd is null,0,If(q.qtdr is null,0,q.qtdr))-tb_refeicoes_dia.qtd) AS dif, \r\n"
					+ " (If(tb_refeicoes_dia.qtd>If(q.qtdr is null,0,q.qtdr),tb_refeicoes_dia.qtd,(If(q.qtdr is null,0,q.qtdr))+If(tb_refeicoes_dia.qtdq is null,0,tb_refeicoes_dia.qtdq))) AS tlref, \r\n"
					+ "If(If(tb_refeicoes_dia.qtd is null,0,tb_refeicoes_dia.qtd)>q.qtdr,(If(tb_refeicoes_dia.qtd is null,0,\r\n"
					+ "tb_refeicoes_dia.qtd)+If(tb_refeicoes_dia.qtdq is null,0,tb_refeicoes_dia.qtdq))*tb_tipos_refeicoes.vrunit,\r\n"
					+ "(If(q.qtdr is null,0,q.qtdr)+If(tb_refeicoes_dia.qtdq is null,0,tb_refeicoes_dia.qtdq))*tb_tipos_refeicoes.vrunit) AS total\r\n"
					+ "FROM tb_tipos_refeicoes \r\n"
					+ "INNER JOIN tb_refeicoes_dia LEFT JOIN \r\n"
					+ "(SELECT tb_registros.idtipo, tb_registros.dtreg, Count(tb_registros.idreg) AS qtdr\r\n"
					+ "FROM tb_registros\r\n"
					+ "WHERE tb_registros.statusreg=1 Or tb_registros.statusreg=3\r\n"
					+ "GROUP BY tb_registros.idtipo, tb_registros.dtreg) q\r\n"
					+ "ON tb_refeicoes_dia.dtlnc = q.dtreg AND tb_refeicoes_dia.idtipo = q.idtipo ON tb_tipos_refeicoes.idtipo = tb_refeicoes_dia.idtipo\r\n"
					+ "WHERE tb_refeicoes_dia.dtlnc Between '"+DataIni+"' And '"+DataFim+"'\r\n"
					+ "ORDER BY tb_tipos_refeicoes.indice,tb_tipos_refeicoes.nmtipo, tb_refeicoes_dia.dtlnc;";
			break;
		}
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(!(tipo.toLowerCase().equals("faturamentoperiodo"))) {
				while(rs.next()) {
						Registro reg = new Registro();
						if(tipo.toLowerCase().equals("reldia") || tipo.toLowerCase().equals("movdiario") ) {
							reg.set("datareg", rs.getString("dtreg"));
						}
						reg.set("idtipo", rs.getString("id"));
						reg.set("tipo", rs.getString("nome"));
						reg.set("quantidade", rs.getString("qtd"));
						l.add(reg);
				}
			}else {
				while(rs.next()) {
					Registro reg = new Registro();
					reg.set("idtipo", rs.getString("indice"));
					reg.set("nome", rs.getString("nomereal"));
					reg.set("datareg", rs.getString("dtlnc"));
					reg.set("tipo", rs.getString("nome"));
					reg.set("qtdr", rs.getString("qtdrd"));
					reg.set("qtdm", rs.getString("qtdm"));
					reg.set("qq", rs.getString("qq"));
					reg.set("vrunit", rs.getString("vrunit"));
					reg.set("diferenca", rs.getString("dif"));
					reg.set("totalref", rs.getString("tlref"));
					reg.set("total", rs.getString("total"));
					l.add(reg);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return l;
	}
}
