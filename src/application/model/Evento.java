package application.model;

public class Evento {
	private String id, vrunit, hrini, hrfim, nome, iduser, qtdmin, data, qq;
	
	public String get(String variavel) {
		String rtn = "";
		if(!(variavel == null || variavel.isEmpty())) {
			switch(variavel) {
			case "id":
				rtn = id;
				break;
			case "vrunit":
				rtn = vrunit;
				break;
			case "horainicial":
				rtn = hrini;
				break;
			case "horafim":
				rtn = hrfim;
				break;
			case "nome":
				rtn = nome;
				break;
			case "idusuario":
				rtn = iduser;
				break;
			case "qtdm":
				rtn = qtdmin;
				break;
			case "data":
				rtn = data;
				break;
			case "qq":
				rtn = qq;
				break;
			}
		}
		return rtn;
	}
	public void set(String variavel, String valor) {
		if(!(variavel == null || variavel.isEmpty())) {
			switch(variavel) {
			case "id":
				id = valor;
				break;
			case "vrunit":
				vrunit = valor;
				break;
			case "horainicial":
				hrini = valor;
				break;
			case "horafim":
				hrfim = valor;
				break;
			case "nome":
				nome = valor;
				break;
			case "idusuario":
				iduser = valor;
				break;
			case "qtdm":
				qtdmin = valor;
				break;
			case "qq":
				qq = valor;
				break;
			case "data":
				data = valor;
				break;
			}
		}
	}
	public String getQtdmin() {
		return qtdmin;
	}
	public String getData() {
		return data;
	}
	public String getQq() {
		return qq;
	}
	public String getId() {
		return id;
	}
	public String getVrunit() {
		return vrunit;
	}
	public String getHrini() {
		return hrini;
	}
	public String getHrfim() {
		return hrfim;
	}
	public String getNome() {
		return nome;
	}
}
