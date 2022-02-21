package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import application.model.Combobox;
import application.model.Funcionario;

public class FuncionarioDao {
	private Connection con;
	
	public FuncionarioDao(){
		new ConnectionFactory();
		con = ConnectionFactory.getConnection();
	}
	
	public void adicionarfun(Funcionario f) {
		String sql = "INSERT INTO tb_funcionarios SET idcartao='"+f.get("idcartao")+"', nome='"+f.get("nome")+"',"
				+ " setor='"+f.get("setor")+"', funcao='"+f.get("funcao")+"', matricula='"+f.get("matricula")+"', registro='"+f.get("registro")+"',"
				+ "origem=1";
		
			PreparedStatement ps;
			try {
				ps = con.prepareStatement(sql);
				ps.execute();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Main.dialogBox("Erro ao cadastrar!", 1);
				return;
			}
			
			
			Main.dialogBox("Funcionário cadastrado com sucesso!", 2);
	}
	
	public List<Funcionario> listFun(String where){
		String sql = "SELECT*FROM tb_funcionarios WHERE stsfnc=0 ";
		if(where != null) {
			sql += "AND "+where;
		}
		List<Funcionario> l = new ArrayList<>();
		try(PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();){
			while(rs.next()) {
				Funcionario f = new Funcionario();
				f.set("matricula", rs.getString("matricula"));
				f.set("nome", rs.getString("nome"));
				f.set("idcartao", rs.getString("idcartao"));
				f.set("idreg", rs.getString("idreg"));
				f.set("setor", rs.getString("setor"));
				f.set("funcao", rs.getString("funcao"));
				f.set("registro", rs.getString("registro"));
				l.add(f);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return l;
	}
	
	public List<Combobox> listItems(String valor){
		List<Combobox> l = new ArrayList<>();
		
		String sql="SELECT "+valor+" as nome FROM tb_funcionarios GROUP BY "+valor;
		try(PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();){
			int i=0;
			while(rs.next()) {
				 i++;
				 Combobox c = new Combobox(i,rs.getString("nome"));
				 l.add(c);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return l;
	}
}
