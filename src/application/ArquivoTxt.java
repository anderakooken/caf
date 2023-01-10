package application;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import application.dao.RefeitorioDao;
import application.dao.RelatorioDao;
import application.model.Funcionario;
import application.model.Imprimir;
import application.model.Registro;

public class ArquivoTxt {
	private static RefeitorioDao dao;
	private static RelatorioDao daorel;
	private static final String strFileJSON = System.getenv("APPDATA") + "\\CAF\\" + "\\printers.json";
	private static final Logger log = Logger.getLogger(ArquivoTxt.class);
	private static Thread th = new Thread();
	private static Thread toPrint = new Thread();
	private static JSONObject cacheJSON = new JSONObject();

	public static void writeComprovante(String status, String idcartao) {

		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String data = df.format(LocalDateTime.now());
		Funcionario fun = new Funcionario();
		dao = new RefeitorioDao();
		String caminho = "comprovante"; 
		File f = null;
		try {
			f = File.createTempFile(caminho,".txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Main.dialogBox("Erro ao criar o arquivo temporário, tente novamente!", 1);
			e1.printStackTrace();
		}
		if(!(idcartao.equals("9000077"))) {
			fun = dao.Registro(idcartao,"funcionarios").get(0);
		}else {
			fun.set("matricula", " ");
			fun.set("setor", " ");
		}
		
		
		try(FileWriter fw = new FileWriter(f.getPath(), false);
			BufferedWriter bf = new BufferedWriter(fw);
			PrintWriter p = new PrintWriter(bf);) {
				
				p.println("CONTROLE DE ACESSO"
						+ "\n"
						+status.toUpperCase()
						+"\nID: " + idcartao
						+"\nMat: " + fun.get("matricula")
						+"\nSetor: " + fun.get("setor")
						+"\nData: " + data.substring(8,10) + "/" + data.substring(5,7) + "/" + data.substring(0, 4) + " - "+ data.substring(11, 19)
						+ "\n\n\n");
				p.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ImprimireDeletar(f.getPath());
	}
	
	public static void writeRel(String tprelatorio, String DataIni, String DataFim) {
		//---------------------variaveis------------------------
		dao = new RefeitorioDao();
		daorel = new RelatorioDao();
		String datar = "";
		Registro reg;
		String data = "";
		tprelatorio = tprelatorio.toLowerCase();
		//String caminho = "C:\\Mais Sabor\\CAF\\relatorio "+tprelatorio+".txt"; 
		File f = null;
		try {
			f = File.createTempFile(tprelatorio, ".txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Main.dialogBox("Erro ao criar o arquivo temporário", 1);
			e1.printStackTrace();
		}
		List<Registro> registro = new ArrayList<>();
		
		//---------------------variaveis------------------------
		
		//---------------------definindo o tipo de relatorio------------------------
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
		//---------------------definindo o tipo de relatorio------------------------
		//---------------------Identificando e escrevendo o relatorio------------------------
		try(FileWriter fw = new FileWriter(f.getPath(), false);
				BufferedWriter bf = new BufferedWriter(fw);
				PrintWriter p = new PrintWriter(bf)) {
			
			int total = 0;
			String tipo = "";
			
			if(tprelatorio.equals("dia")) {
				tipo = "imprimir";
				datar = "";
				p.println("CONTROLE DE ACESSO\r\n"
						+ "\n"
						+ "EXTRATO DE REFEIÇÕES");
				total = 0;
				for(int i = 0; i<registro.size(); i++) {
					
					
					reg = new Registro();
					reg = registro.get(i);
					reg = tratamentodelinhas(reg,"extrato");
					
					
					if(datar.isEmpty() || !(datar.equals(reg.get("datareg")))) {
						if(!(datar.isEmpty()) && datar.equals(reg.get("datareg"))) {
							p.println("\n                       total="+total);
						}
						p.println("\n\nData:" + reg.get("datareg")
								+ "\n\nRefeição        Qtd"
								+ "\n_____________________");
					}
					
					
					p.println("\n"+reg.get("idtipo")+"  -  "+reg.get("tipo")+" ("+reg.get("quantidade")+")");
					
					total = total + Integer.parseInt(reg.get("quantidade"));
					datar = reg.get("datareg");
				}
				p.println("\n\n\n");
				
				
			}else if(tprelatorio.equals("periodo")) {
				tipo = "imprimir";
				p.println("CONTROLE DE ACESSO\r\n"
						+ "EXTRATO DE REFEIÇÕES");
				p.println("\n"
						+ "\n\nData:" + data
						+ "\n\nRefeição        Qtd"
						+ "\n_____________________");
				total=0;
				for(int i = 0; i<registro.size(); i++) {
					reg = new Registro();
					reg = registro.get(i);
					reg = tratamentodelinhas(reg,"extrato");
					p.println("\n"+reg.get("idtipo")+"  -  "+reg.get("tipo")+" ("+reg.get("quantidade")+")");
					total = total + Integer.parseInt(reg.get("quantidade"));
					datar = reg.get("datareg");
				}
				p.println("\n                  total=" + total + "\r\n\n\n");
			} else if(tprelatorio.toLowerCase().equals("faturamento mensal")) {
				tipo = "abrir";
				
				p.println("                               ***CONTROLE DE ACESSO***\r\n"
						+ "********************************************************************************\r\n"
						+ "                                  FATURAMENTO MENSAL\r\n"
						+ "     \r\n"
						+ "Refeição\r\n"
						+ "Data|Consumido|ConsumoMin|Diferença|Quentinhas|Totalref.|Vr Unitário|Valor Total\r\n");
						   
				//variaveis
				String indice = "";
				String totalnome = "";
				double totalsoma = 0;
				int totalconsumido = 0;
				int totalconsumin = 0;
				int totaldif = 0;
				int totalref = 0;
				int totalqq = 0;
				DecimalFormat formato = new DecimalFormat("#.##");
				reg = new Registro();
				
				
				for(int i = 0; i<registro.size(); i++) {
					reg = new Registro();
					reg = registro.get(i);
					reg = tratamentodelinhas(reg,"faturamentomensal");
					
					if(indice.equals(reg.get("idtipo")) == false && i>0){
						totalnome = "Total de "+registro.get(i-1).get("nome")+":";
						totalnome = tratarlinhacomref(totalnome, (Main.getWeek(reg.get("datareg")) + " - " + Main.formatDatedb(reg.get("datareg"))));
						totalconsumido = Integer.parseInt(tratarlinhacomref(Integer.toString(totalconsumido), reg.get("qtdr")));
						totalconsumin = Integer.parseInt(tratarlinhacomref(Integer.toString(totalconsumin), reg.get("qtdm")));
						totaldif = Integer.parseInt(tratarlinhacomref(Integer.toString(totaldif), reg.get("diferenca")));
						totalref = Integer.parseInt(tratarlinhacomref(Integer.toString(totalref), reg.get("totalref")));
						totalqq = Integer.parseInt(tratarlinhacomref(Integer.toString(totalqq), reg.get("qq")));
						
						p.println("        "+totalnome+" "+totalconsumido+" | "+totalconsumin+" | "+totaldif+" | "+totalqq+" | "+totalref+" |     | R$"+formato.format(totalsoma)+"\r\n");
						totalnome = ""; totalconsumido = 0; totalconsumin = 0; totaldif = 0; totalref = 0; totalqq = 0; totalsoma=0;
						
					}
					
					if(indice.equals(reg.get("idtipo")) == false) {
						
							p.println("________________________________________________________________________________\r\n\n"
									 +"   "+reg.get("tipo")+" \r\n"
									 +"________________________________________________________________________________\r\n");
					}
					           
					p.println("        "+Main.getWeek(reg.get("datareg"))+" - "+Main.formatDatedb(reg.get("datareg"))+" | " + reg.get("qtdr") + " | " + 
					reg.get("qtdm")+" | "+reg.get("diferenca").substring(reg.get("diferenca").indexOf("-"))+ " | " + reg.get("qq") + " | " + reg.get("totalref")+" | " + reg.get("vrunit").replace(".",",")+"| R$"+reg.get("total") +"\r\n");
					indice = reg.get("idtipo");
					//somas
					totalref += Integer.parseInt(reg.get("totalref").replaceAll("\\s", ""));
					totaldif += Integer.parseInt(reg.get("diferenca").replaceAll("\\s", ""));
					totalconsumin += Integer.parseInt(reg.get("qtdm").replaceAll("\\s", ""));
					totalconsumido += Integer.parseInt(reg.get("qtdr").replaceAll("\\s", ""));
					totalsoma = totalsoma + Double.parseDouble(reg.get("total").replaceAll("\\s", ""));
					totalqq += Integer.parseInt(reg.get("qq").replaceAll("\\s", ""));
					//somas
					if(i == registro.size()-1){
						totalnome = "Total de "+registro.get(i-1).get("nome")+":";
						totalnome = tratarlinhacomref(totalnome, (Main.getWeek(reg.get("datareg")) + " - " + Main.formatDatedb(reg.get("datareg"))));
						totalconsumido = Integer.parseInt(tratarlinhacomref(Integer.toString(totalconsumido), reg.get("qtdr")));
						totalconsumin = Integer.parseInt(tratarlinhacomref(Integer.toString(totalconsumin), reg.get("qtdm")));
						totaldif = Integer.parseInt(tratarlinhacomref(Integer.toString(totaldif), reg.get("diferenca")));
						totalref = Integer.parseInt(tratarlinhacomref(Integer.toString(totalref), reg.get("totalref")));
						totalqq = Integer.parseInt(tratarlinhacomref(Integer.toString(totalqq), reg.get("qq")));
						
						p.println("        "+totalnome+" "+totalconsumido+" | "+totalconsumin+" | "+totaldif+" | "+totalqq+" | "+totalref+" |     | R$"+formato.format(totalsoma)+"\r\n");
						
						
					}
				}		                                                                       
				//p.println("\n        Total Geral: "+totalconsumido+" | "+totalconsumin+" | "+totaldif+" | "+totalqq+" |  "+totalref+"      "+formato.format(totalsoma)+"\r\n");
				
			}  else if(tprelatorio.toLowerCase().equals("movdiario")) {
				tipo = "abrir";
				String indice = "";
				total = 0;
				p.println("*******************************************************************************\r\n"
						+ "                                 MOVIMENTO DIÁRIO\r\n");
						
				for(int i = 0; i<registro.size(); i++) {
					reg = new Registro();
					reg = registro.get(i);
					reg = tratamentodelinhas(reg,"movdiario");
					if(indice.equals(reg.get("datareg")) == false){
						if(indice.equals(reg.get("datareg")) == false && !(indice.isEmpty())) {
							p.println("________________________________________________________________________________\r\n");
						}
						p.println("Data do movimento: "+data+"\r\n"
								+ "\r\n"
								+ "           Refeição                                             Quantidade\r\n"
								+ "           ________________________________________________________________\r\n");
					}
						p.println("           "+reg.get("tipo")+"                                             "+reg.get("quantidade")+"\r\n");
					
					indice = reg.get("datareg");
					total = total + Integer.parseInt(reg.get("quantidade"));
				}
				p.println("                                                              Total:"+total+"");
			}
			
			//---------------------Identificando e escrevendo o relatorio------------------------
			
			p.close();
	
			if(tipo.equals("imprimir")) {
				ImprimireDeletar(f.getPath());
			}else if(tipo.equals("abrir")) {
				abrirarquivo(f.getPath());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Registro tratamentodelinhas(Registro reg, String tipo) {
		if(tipo.toLowerCase().equals("faturamentomensal")) {
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
		}else if(tipo.equals("extrato")) {
			while(reg.get("tipo").length()<8) {
				reg.set("tipo", reg.get("tipo")+" ");
			}
		}else if(tipo.equals("movdiario")) {
			while(reg.get("tipo").length()<12) {
				reg.set("tipo", reg.get("tipo")+" ");
			}
		}
		
		
		return reg;
	}
	
	public static String tratarlinhacomref(String tratavel, String ref) {
		while (tratavel.length() < ref.length()) {
			tratavel = tratavel + " ";
		}
		return tratavel;
	}
	
	/**
	 * Método para imprimir e deletar o arquivo
	 * @param caminho
	 */
	public static void ImprimireDeletar(String caminho) {
	
		saveFileCache(caminho);

		if(!th.isAlive()){
			th = new Thread(() -> {
				PrintFilesInCache();
			});
			th.setPriority(Thread.MAX_PRIORITY);
			th.start();
		}


	}

	/**
	 * Imprime arquivos que estão no cache JSON (que não foram imprimidos)
	 */
	public static void PrintFilesInCache(){
		try{

			if(ExistsFilesToPrint()){
				
				if(cacheJSON.has("printers")){

					JSONArray printers = cacheJSON.getJSONArray("printers");

					
						
					for (int i = 0; i < printers.length(); i++) {
						
						try{
		
							File printer = new File(printers.getString(i));
		
							if(printer.exists()){
		
								Imprimir imprimir = new Imprimir(
									"A7",
									1, 
									printer
								);
								
								int seconds = 0;

								while(toPrint.isAlive() && seconds < 5){

									try {
										Thread.sleep(1000);
										seconds++;
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}

								if(toPrint.isAlive()){

									toPrint.stop();
									log.error("Erro ao imprimir!");
									return;
									
								}else{
									toPrint = new Thread(() ->{
										imprimir.imprimir();
										printer.delete();
									});

									toPrint.start();
								}

								
		
							}

							
		
						}catch(NullPointerException e){
							log.error("Error NullPointer no FOR :: PrintFilesInCache()",e);
						}
						
					}

					cacheJSON.remove("printers");

					FileOutputStream writeFile = new FileOutputStream(strFileJSON);
					writeFile.write(cacheJSON.toString().getBytes());
					writeFile.close();
				}
			

			}
			

		}catch(IOException e){

			log.error("Erro ao ler o arquivo das impressões pendentes no arquivo JSON!", e);

		}
	}


	/**
	 * Função para verificar se existe arquivos no cache arrayJSON para imprimir.
	 */
	public static boolean ExistsFilesToPrint(){
		try{
			File fileCache = new File(strFileJSON);
			if(fileCache.exists()){

				FileInputStream readFile = new FileInputStream(strFileJSON);
				cacheJSON = new JSONObject(new String(readFile.readAllBytes()));
				readFile.close();
				
				if(cacheJSON.has("printers")){

					JSONArray printers = cacheJSON.getJSONArray("printers");

					if(printers.length() > 0){
				
						return true;

					}
				}

			}
		
		}catch(IOException e){
			log.error("Erro ao verificar o arquivo", e);
		}

		return false;
	}

	/**
	 * Salva o caminho do arquivo como cache no arquivo JSON.
	 * @param caminho
	 */
	public static void saveFileCache(String caminho){

		try{
			File fileJSON = new File(strFileJSON);
			
			JSONArray filesNotPrinted = new JSONArray();
			
			

			if(fileJSON.exists()){

				FileInputStream readFile = new FileInputStream(fileJSON);
				cacheJSON = new JSONObject(new String(readFile.readAllBytes()));
				readFile.close();

				if(cacheJSON.has("printers")){

					filesNotPrinted = cacheJSON.getJSONArray("printers");

				}
				

			}else{
				File folder = new File(System.getenv("APPDATA") + "\\CAF");
				if (!folder.exists()) {
					if(folder.mkdir()){
						fileJSON.createNewFile();
					}else{
						return;
					}
					
				}else{
					fileJSON.createNewFile();
				}

				

			}

			filesNotPrinted.put(caminho);
			cacheJSON.put("printers", filesNotPrinted);

			FileOutputStream writeFile = new FileOutputStream(fileJSON);
			writeFile.write(cacheJSON.toString().getBytes());
			writeFile.close();
			
		}catch(IOException e){

			log.error("Erro ao salvar o print no arquivo JSON!", e);

		}
	}

	/**
	 * Método para abrir um arquivo.
	 * @param caminho
	 */
	public static void abrirarquivo(String caminho) {
		//Desktop dk =;
		File file = new File(caminho);
		try {
			Desktop.getDesktop().open(file);
			file.deleteOnExit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
