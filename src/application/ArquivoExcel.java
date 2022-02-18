package application;


import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import application.dao.RefeitorioDao;
import application.model.Funcionario;
import application.model.Registro;
import application.model.TitulosExcel;

public class ArquivoExcel {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private XSSFRow row;
	private RefeitorioDao dao;
	private XSSFCellStyle style;
	private XSSFFont fonte;
	
	
	public void CriareImprimirRelatorioNutrionor(String titulo, String planilha, String relatorio, String DataIni, String DataFim) {
		if(titulo==null || planilha==null) {
			Main.dialogBox("Titulo, Planilha e colunas não podem ser nulos!", 1);
			return;
		}
		relatorio = relatorio.toLowerCase();
		//Criando um workbook
		workbook = new XSSFWorkbook();
		int colunas = 0;
		TitulosExcel tcell = null;
		switch(relatorio) {
		case "extratodia":
			colunas = 2;
			tcell = new TitulosExcel("Refeição", null,"Qtd",null,null);
			break;
		}
		//Criando um spreadsheet
		sheet = workbook.createSheet(planilha);
	
		//Criando cells e setando valores
		row = sheet.createRow(0);
		
		sheet.addMergedRegion(new CellRangeAddress(0,1,0,colunas));
		
		Cell celltitulo = row.createCell(0);
		setStyle(1);
		celltitulo.setCellStyle(style);
		celltitulo.setCellValue("CONTROLE DE ACESSO");
		row = sheet.createRow(2);
		
		sheet.addMergedRegion(new CellRangeAddress(2,3,0,colunas));
		Cell cellextrato = row.createCell(0);
		setStyle(5);

		cellextrato.setCellStyle(style);
		cellextrato.setCellValue(titulo);
		
		row = sheet.createRow(4);
		if(DataFim == null) {
			setStyle(4);

			Cell celldata = row.createCell(0);
			sheet.addMergedRegion(new CellRangeAddress(4,4,1,2));
			Cell celldatavalor = row.createCell(1);
			celldata.setCellValue("Data:");
			celldata.setCellStyle(style);
			celldatavalor.setCellValue(DataIni);
			celldatavalor.setCellStyle(style);
			row = sheet.createRow(5);
			sheet.addMergedRegion(new CellRangeAddress(5,5,0,1));
			setStyle(2);
			style.setBorderBottom(BorderStyle.THIN);
		}else {
			
		}
		
		
		Cell cell0 = row.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue(tcell.get(1));
	
		Cell cell1 = row.createCell(1);
		cell1.setCellValue(tcell.get(2));
		cell1.setCellStyle(style);
		
		Cell cell2 = row.createCell(2);
		cell2.setCellValue(tcell.get(3));
		cell2.setCellStyle(style);
			

		/*Cell cell3 = row.createCell(3);
		cell3.setCellValue(tcell.get(4));
		cell3.setCellStyle(style);
			

		Cell cell4 = row.createCell(4);
		cell4.setCellValue(tcell.get(5));
		cell4.setCellStyle(style);*/
			
		List<Registro> registro = new ArrayList<>();
		if(relatorio.equals("extratodia")) {
			setStyle(3);
			dao = new RefeitorioDao();
			//registro = dao.RegistroList("nutrinorreldia", "WHERE dtreg = '"+DataIni+"' AND statusreg = '1'");
			System.out.println(registro.size());	
			for(int i = 0; i< registro.size(); i++) {
				row = sheet.createRow(i+6);

				for(int j=0; j<=colunas; j++) {
					Cell cell = row.createCell(j);
					Registro reg = new Registro();
					reg = registro.get(i);
					switch(cell.getColumnIndex()) {
						case 0:
							cell.setCellValue(reg.get("idtipo"));
							break;
						case 1:
							cell.setCellValue(reg.get("tipo"));
							break;
						case 2:
							cell.setCellValue(reg.get("quantidade"));
					}				
					cell.setCellStyle(style);
				}

			}
		}
		
		//Auto Regular os tamanhos das colunas
		for(int colum=0; colum<=colunas; colum++) {
			sheet.autoSizeColumn(colum);
		}
		
		/*for(int j = 0; j<=colunas;j++) {
			String letra = "";
			
			switch(j) {
			case 0:
				letra = "A";
				break;
			case 1:			
				letra = "B";
				break;
			case 2:
				letra = "C";
				break;
			case 3:
				letra = "D";
				break;
			}*/
		
			CellRangeAddress region = CellRangeAddress.valueOf("A4:C4");
			BorderStyle borda = BorderStyle.MEDIUM;
			RegionUtil.setBorderBottom(borda, region, sheet);
			borda = BorderStyle.THIN;
			region  = CellRangeAddress.valueOf("A6:C6");
			RegionUtil.setBorderBottom(borda, region, sheet);

		//Escrevendo a criacao do arquivo em excel
		try {
			FileOutputStream out = new FileOutputStream(new File("C:\\CAF\\Registros.xlsx"));
			workbook.write(out);
			out.close();
			workbook.close();
			System.out.println("Arquivo criado com sucesso!");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Desktop d = Desktop.getDesktop();
		File file = new File("C:\\CAF\\Registros.xlsx");
		try {
			d.print(file);
			System.out.println("Arquivo imprimido com sucesso!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*try {
			Thread.sleep(10000);
			file.delete();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public void CriareImprimirComprovante(String idcartao) {
		//Criando um workbook
		workbook = new XSSFWorkbook();
		
		//Criando um spreadsheet
		sheet = workbook.createSheet("Comprovante");
	
		//Criando cells e setando valores
		row = sheet.createRow(0);
		
		sheet.addMergedRegion(new CellRangeAddress(0,1,0,1));
		Cell celltitulo = row.createCell(0);
		row = sheet.createRow(2);
		
		sheet.addMergedRegion(new CellRangeAddress(2,3,0,1));
		Cell cellstatus = row.createCell(0);
		row.createCell(4);
		
		
		setStyle(1);
		celltitulo.setCellStyle(style);
		celltitulo.setCellValue("CONTROLE DE ACESSO");
		setStyle(5);
		cellstatus.setCellStyle(style);
		cellstatus.setCellValue("APROVADO");
		
			
		//tipo usado para testes, aqui é onde ocorre a insercao de dados.
		
		dao = new RefeitorioDao();
		List<Funcionario> fun = new ArrayList<>();
		fun = dao.Registro(idcartao,"funcionarios");
		

		if(fun.size()>=0) {
			Funcionario funcionario = new Funcionario();
			funcionario = fun.get(0);

			
			for(int row = 4; row<9; row++) {	
				this.row = sheet.createRow(row);
				for(int colun=0; colun<2; colun++) {
					Cell cell = this.row.createCell(colun);
					switch(row) {
					case 4:
						switch(colun) {
						case 0:
							cell.setCellValue("ID Cartão:");
							break;
						case 1:
							cell.setCellValue(funcionario.get("idcartao"));
							break;
						}
						break;
					case 5:
						switch(colun) {
						case 0:
							cell.setCellValue("Matricula:");
							break;
						case 1:
							cell.setCellValue(funcionario.get("matricula"));  
							break;
						}
						break;
					case 6:
						switch(colun) {
						case 0:
							cell.setCellValue("Nome:");
							break;
						case 1:
							cell.setCellValue(funcionario.get("nome"));
							break;
						}
						break;
					case 7:
						switch(colun) {
						case 0:
							cell.setCellValue("Setor:");
							break;
						case 1:
							cell.setCellValue(funcionario.get("setor"));
							break;
						}
						break;
					case 8:
						switch(colun) {
						case 0:
							cell.setCellValue("Função:");
							break;
						case 1:
							cell.setCellValue(funcionario.get("funcao")); 
							break;
						}
						break;
					}
					switch(colun) {
					case 0:
						setStyle(4);
						cell.setCellStyle(style);
						break;
					case 1:
						setStyle(3);
						cell.setCellStyle(style);
						break;
						}
					}
				}
			}
			//Auto ajustar as colunas
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			for(int j = 1; j<=2; j++) {
				String letra = "";
				if(j==1) {
					letra = "A";
				}else {
					letra = "B";
				}
				for(int i=3; i<=9; i++) {
					CellRangeAddress region = CellRangeAddress.valueOf(letra+i);
					BorderStyle borda = BorderStyle.DASHED;
					RegionUtil.setBorderBottom(borda, region, sheet);
					RegionUtil.setBorderLeft(borda, region, sheet);
					RegionUtil.setBorderRight(borda, region, sheet);
					RegionUtil.setBorderTop(borda, region, sheet);
				}
			}
			try {
				FileOutputStream out = new FileOutputStream(new File("C:\\CAF\\Comprovante.xlsx"));
				workbook.write(out);
				out.close();
				workbook.close();
				System.out.println("Arquivo criado com sucesso!");
				Desktop d = Desktop.getDesktop();
				File file = new File("C:\\CAF\\Comprovante.xlsx");
				d.print(file);
				System.out.println("Arquivo imprimido com sucesso!");
				Thread.sleep(10000);
				file.delete();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	//metodo para setar o estilo da celula
	private void setStyle(int tipo) {
		switch(tipo) {
		//Titulo Controle de Acesso
		case 1:
			style = workbook.createCellStyle();
			fonte = workbook.createFont();
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			style.setFillBackgroundColor(IndexedColors.BLACK.index);
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			//style.setFillBackgroundColor(color);
			fonte.setFontHeightInPoints((short)11);
			fonte.setBold(true);
			fonte.setColor(IndexedColors.WHITE.index);
			style.setFont(fonte);
			break;
		//Titulo da coluna
		case 2:
			fonte = workbook.createFont();
			style = workbook.createCellStyle();
			fonte.setFontHeightInPoints((short)12);
			style.setAlignment(HorizontalAlignment.CENTER);
			fonte.setBold(true);
			style.setFont(fonte);
			break;
		//Dados
		case 3:
			fonte = workbook.createFont();
			style = workbook.createCellStyle();
			style.setAlignment(HorizontalAlignment.CENTER);
			fonte.setFontHeightInPoints((short)8);
			style.setFont(fonte);
			break;
		case 4:
			fonte = workbook.createFont();
			style = workbook.createCellStyle();
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.CENTER);;
			fonte.setFontHeightInPoints((short)8);
			fonte.setBold(true);
			style.setFont(fonte);
			break;
			//Titulo de status
		case 5:
			style = workbook.createCellStyle();
			fonte = workbook.createFont();
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.CENTER);

			fonte.setFontHeightInPoints((short)14);
			fonte.setBold(true);
			style.setFont(fonte);
			break;
		}
		
	}
}
