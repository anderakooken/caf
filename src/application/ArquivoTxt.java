package application;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import application.dao.RefeitorioDao;
import application.dao.RelatorioDao;
import application.model.Funcionario;
import application.model.Registro;

public class ArquivoTxt {
	private static RefeitorioDao dao;
	private static RelatorioDao daorel;
	public static void writeComprovante(String status, String idcartao, String ultimoacesso) {
		List<Funcionario> fun = new ArrayList<>();
		dao = new RefeitorioDao();
		String caminho = "C:\\Mais Sabor\\CAF\\comprovante.txt"; 
		fun = dao.Registro(idcartao,"funcionarios");
		try(FileWriter fw = new FileWriter(caminho, false);
			BufferedWriter bf = new BufferedWriter(fw);
			PrintWriter p = new PrintWriter(bf)) {
				p.println("     ***CONTROLE DE ACESSO***"
						+ "\n*******************************"
						+"\n          "+status.toUpperCase()
						+ "\n*******************************"
						+"\nID Cartão: " + idcartao
						+"\nMatricula: " + fun.get(0).get("matricula")
						+"\nNome: " + fun.get(0).get("nome")
						+"\nSetor: " + fun.get(0).get("setor")
						+"\nFunção: " + fun.get(0).get("funcao")
						+"\nUltimo Acesso: " + ultimoacesso
						+ "\n*******************************");
						
				p.close();		
				ImprimireDeletar(caminho);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeRelNutrinor(String tprelatorio, String DataIni, String DataFim) {
		dao = new RefeitorioDao();
		daorel = new RelatorioDao();
		String datar = "";
		Registro reg;
		String data = "";
		String caminho = "C:\\Mais Sabor\\CAF\\relatorio"+tprelatorio+".txt"; 
		List<Registro> registro = new ArrayList<>();
		if(tprelatorio.toLowerCase().equals("dia")) {
			registro = daorel.RelatorioList("relDia", DataIni, DataFim);
			data = DataIni;
		}else if(tprelatorio.toLowerCase().equals("periodo")) {
			registro = daorel.RelatorioList("relPeriodo",DataIni, DataFim);
			data = DataIni + " - " + DataFim;
		}else if(tprelatorio.toLowerCase().equals("faturamento mensal")) {
			//registro = dao.RegistroList("nutrinorreldia", "WHERE dtreg BETWEEN '"+DataIni+"' AND '"+DataFim+"' AND statusreg = '1'");
			data = DataIni + " - " + DataFim;
		}
		
		try(FileWriter fw = new FileWriter(caminho, false);
				BufferedWriter bf = new BufferedWriter(fw);
				PrintWriter p = new PrintWriter(bf)) {
			if(tprelatorio.toLowerCase().equals("dia")) {
				datar = "";
				p.println("    ***CONTROLE DE ACESSO***\r\n"
						+ "*******************************\r\n\n"
						+ "     EXTRATO DE REFEIÇÕES");
				
				for(int i = 0; i<registro.size(); i++) {
					reg = new Registro();
					reg = registro.get(i);
					if(datar.isEmpty() || !(datar.equals(reg.get("datareg")))) {
						p.println("\n*******************************"
								+ "\n\nData:     " + reg.get("datareg")
								+ "\n\n     Refeição          Qtd"
								+ "\n     _____________________");
					}
					p.println("\n     "+reg.get("idtipo")+"  -  "+reg.get("tipo")+"  ("+reg.get("quantidade")+")");
					datar = reg.get("datareg");
				}
			}else if(tprelatorio.toLowerCase().equals("periodo")) {
				p.println("  ***CONTROLE DE ACESSO***\r\n"
						+ "*******************************\r\n"
						+ "     EXTRATO DE REFEIÇÕES");
				p.println("\n*******************************"
						+ "\n\nData:     " + data
						+ "\n\n     Refeição          Qtd"
						+ "\n     _____________________");
				for(int i = 0; i<registro.size(); i++) {
					reg = new Registro();
					reg = registro.get(i);
					p.println("\n     "+reg.get("idtipo")+"  -  "+reg.get("tipo")+"  ("+reg.get("quantidade")+")");
					datar = reg.get("datareg");
				}
			} /*else if(tprelatorio.toLowerCase().equals("faturamento mensal")) {
				p.println("  					***CONTROLE DE ACESSO***\r\n"
						+ "**********************************************************************************************************\r\n"
						+ "     					      FATURA MENSAL\r\n"
						+ "     \r\n"
						+ "\r\n"
						+ "Refeição     |     Data	|Consumido|ConsumoMin|Diferença|Quentinhas|Total ref. |	Vr Unitário |  Valor Total\r\n"
						+ "__________________________________________________________________________________________________________\r\n");
				
				
			}*/
			p.close();
			ImprimireDeletar(caminho);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void ImprimireDeletar(String caminho) {
		Desktop dk = Desktop.getDesktop();
		File file = new File(caminho);
		
		try {
			dk.print(file);
			Thread.sleep(5000);
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		file.delete();
	}
}
