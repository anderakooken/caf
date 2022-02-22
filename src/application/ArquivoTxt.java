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
						+"\n             "+status.toUpperCase()
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
		tprelatorio = tprelatorio.toLowerCase();
		String caminho = "C:\\Mais Sabor\\CAF\\relatorio"+tprelatorio+".txt"; 
		List<Registro> registro = new ArrayList<>();
		if(tprelatorio.equals("dia")) {
			registro = daorel.RelatorioList("relDia", DataIni, DataFim);
			data = DataIni;
		} else if(tprelatorio.equals("movdiario")) {
			registro = daorel.RelatorioList("movdiario", DataIni, DataFim);
			data = DataIni;
		}
		else if(tprelatorio.equals("periodo")) {
			registro = daorel.RelatorioList("relPeriodo",DataIni, DataFim);
			data = DataIni + " - " + DataFim;
		}else if(tprelatorio.equals("faturamento mensal")) {
			registro = daorel.RelatorioList("faturamentoperiodo",DataIni, DataFim);
			data = DataIni + " - " + DataFim;
		}
		
		try(FileWriter fw = new FileWriter(caminho, false);
				BufferedWriter bf = new BufferedWriter(fw);
				PrintWriter p = new PrintWriter(bf)) {
			if(tprelatorio.equals("dia") || tprelatorio.equals("movdiario")) {
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
			}else if(tprelatorio.equals("periodo")) {
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
			} else if(tprelatorio.toLowerCase().equals("faturamento mensal")) {
				p.println("  					 ***CONTROLE DE ACESSO***\r\n"
						+ "******************************************************************************************\r\n"
						+ "     					   FATURA MENSAL\r\n"
						+ "     \r\n"
						+ "\r\n"
						+ "Refeição  |   Data|Consumido|ConsumoMin|Diferença|Quentinhas|Total ref. |Vr Unitário | Valor Total\r\n"
						+ "__________________________________________________________________________________________________________\r\n");
				String indice = "";
				for(int i = 0; i<registro.size(); i++) {
					reg = new Registro();
					reg = registro.get(i);
					reg = tratamentodelinhas(reg);
					if(indice.equals(reg.get("idtipo")) == false) {
						if(reg.get("idtipo").equals("6275") || reg.get("idtipo").equals("6275") && indice.isEmpty()) {
							p.println("__________________________________________________________________________________________________________\r\n"
									 +""+reg.get("idtipo")+" - CEIA \r\n"
									 +"__________________________________________________________________________________________________________\r\n");
						} else if(reg.get("idtipo").equals("2") || reg.get("idtipo").equals("2") && indice.isEmpty()) {
							p.println("__________________________________________________________________________________________________________\r\n"
									 +""+reg.get("idtipo")+" - DESJEJUM \r\n"
									 +"__________________________________________________________________________________________________________\r\n");
						} else if(reg.get("idtipo").equals("1")|| reg.get("idtipo").equals("1") && indice.isEmpty()) {
							p.println("__________________________________________________________________________________________________________\r\n"
									 +""+reg.get("idtipo")+" - ALMOÇO \r\n"
									 +"__________________________________________________________________________________________________________\r\n");
						} else if(reg.get("idtipo").equals("4679") || reg.get("idtipo").equals("4679") && indice.isEmpty()) {
							p.println("__________________________________________________________________________________________________________\r\n"
									 +""+reg.get("idtipo")+" - JANTAR \r\n"
									 +"__________________________________________________________________________________________________________\r\n");
						}
					}
					
					p.println(""+Main.getWeek(reg.get("datareg"))+" | "+Main.formatDatedb(reg.get("datareg"))+" | " + reg.get("qtdr") + " | " + 
					reg.get("qtdm")+" | "+reg.get("diferenca").substring(reg.get("diferenca").indexOf("-"))+ " | " + reg.get("qq") + " | " + reg.get("totalref")+" | " + reg.get("vrunit").replace(".",",")+"|   R$"+reg.get("total") +"\r\n");
					indice = reg.get("idtipo");
				}
			}
			p.close();
			ImprimireDeletar(caminho);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Registro tratamentodelinhas(Registro reg) {
		if(reg.get("qtdr").length()<3) {
			while(reg.get("qtdr").length()!=3) {
				reg.set("qtdr",reg.get("qtdr")+" ");
			}
		}
		if(reg.get("qtdm").length()<4) {
			while(reg.get("qtdm").length()!=4) {
				reg.set("qtdm",reg.get("qtdm")+" ");
			}
		}
		if(reg.get("diferenca").length()<4) {
			while(reg.get("diferenca").length()!=4) {
				reg.set("diferenca",reg.get("diferenca")+" ");
			}
		}
		if(reg.get("totalref").length()<3) {
			while(reg.get("totalref").length()!=3) {
				reg.set("totalref",reg.get("totalref")+" ");
			}
		}
		if(reg.get("vrunit").length()<5) {
			while(reg.get("vrunit").length()!=5) {
				reg.set("vrunit",reg.get("vrunit")+" ");
			}
		}
		if(reg.get("total").length()<8) {
			while(reg.get("total").length()!=8) {
				reg.set("total",reg.get("total")+" ");
			}
		}
		
		
		return reg;
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
