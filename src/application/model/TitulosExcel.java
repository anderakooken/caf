package application.model;

public class TitulosExcel {
	private String cell,cell2,cell3,cell4,cell5;
	
	public TitulosExcel(String celula1, String celula2, String celula3, String celula4, 
			String celula5){
		if(celula1 != null) {
			this.cell = celula1;
		}
		if(celula2 != null) {
			this.cell2 = celula2;
		}
		if(celula3 != null) {
			this.cell3 = celula3;
		}
		if(celula4 != null) {
			this.cell4 = celula4;
		}
		if(celula5 != null) {
			this.cell5 = celula5;
		}
	}
	
	public String get(int celula) {
		String rtn="";
		if(!(celula==0)) {
			switch(celula) {
			case 1:
				rtn = cell;
				break;
			case 2:
				rtn = cell2;
				break;
			case 3:
				rtn = cell3;
				break;
			case 4:
				rtn = cell4;
				break;
			case 5:
				rtn = cell5; 
			}
		}
		return rtn;
	}
	public void set(int celula, String valor) {
		if(!(celula==0 && valor == null)) {
			switch(celula) {
			case 1:
				cell = valor;
				break;
			case 2:
				cell2 = valor;
				break;
			case 3:
				cell3 = valor;
				break;
			case 4:
				cell4 = valor;
				break;
			case 5:
				cell5 = valor; 
			}
		}
	}
}
