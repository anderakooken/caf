package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import application.model.Funcionario;

public class AdicionaisDao {
	private Connection con;
	public AdicionaisDao() {
		new ConnectionFactory();
		con = ConnectionFactory.getConnection();
	}
	public void CE(Funcionario f,int tipo) {
		String sql = "";
		String msg = "";
		String c = "";
		if(tipo==0) {
			return;
		}
		switch(tipo) {
		case 1:
			sql = "INSERT INTO tb_adcionais SET matricula='"+f.get("matricula")+"', "
					+ "qtdlib='"+f.get("quantidade")+"', emp_matricula='"+f.get("matricula")+"', idusuario='"+Main.user.getMatricula()+"'";
			msg="Cadastro realizado com sucesso!";
			c= "Cadastrar";
			break;
		case 2:
			sql = "UPDATE tb_adcionais SET qtdlib='"+f.get("quantidade")+"', idusuario='"+Main.user.getMatricula()+"' WHERE matricula='"+f.get("matricula")+"'";
			msg="Atualização realizada com sucesso!";
			c= "Atualizar";
			break;
		}
		
		
		try(Connection con = ConnectionFactory.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);){
			ps.execute();
			ps.close();
			Main.dialogBox(msg, 2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Main.dialogBox("Erro ao "+c+"! Reveja os dados novamente!", 1);
		}
	}

	
	public List<Funcionario> listAdcionais(String where){
		List<Funcionario> l = new ArrayList<>();
		String sql = "SELECT a.matricula, f.registro, f.nome, f.setor, f.funcao, a.qtdlib FROM tb_adcionais a "
				+ "INNER JOIN tb_funcionarios f ON a.matricula = f.matricula ";
		if(where !=null) {
			sql += where;
		}
		
		try(PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();){
			while(rs.next()) {
				Funcionario f = new Funcionario();
				f.set("matricula", rs.getString("matricula"));
				f.set("registro", rs.getString("registro"));
				f.set("nome", rs.getString("nome"));
				f.set("funcao", rs.getString("funcao"));
				f.set("setor", rs.getString("setor"));
				f.set("quantidade", rs.getString("qtdlib"));
				l.add(f);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}
	
	public boolean matriculaverify(String matricula) {
		
		String sql = "SELECT emp_matricula FROM tb_adcionais WHERE emp_matricula='"+matricula+"'";
		try(Connection con = ConnectionFactory.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();){
			
			
			if(rs.next()) {
				return true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
}
