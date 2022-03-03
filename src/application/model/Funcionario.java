package application.model;

public class Funcionario {
	private String idreg, idcartao, nome, setor, funcao, matricula, registro, stsfnc, origem, horaacesso, data, quant;
	
	public String get(String variavel) {
		String rtn = "";
		if(!(variavel == null || variavel.isEmpty())) {
			switch(variavel) {
			case "idreg":
				rtn = idreg;
				break;
			case "idcartao":
				rtn = idcartao;
				break;
			case "nome":
				rtn = nome;
				break;
			case "setor":
				rtn = setor;
				break;
			case "funcao":
				rtn = funcao;
				break;
			case "matricula":
				rtn = matricula;
				break;
			case "registro":
				rtn = registro;
				break;
			case "quantidade":
				rtn = quant;
				break;
			case "status":
				rtn = stsfnc;
				break;
			case "origem":
				rtn = origem;
				break;
			case "ultimoacesso":
				rtn = horaacesso;
				break;
			}
		}
		return rtn;
	}
	
	public void set(String variavel, String valor) {
		if(!(variavel == null || variavel.isEmpty())) {
			switch(variavel) {
			case "dbmr":
				matricula = valor;
				registro = valor;
				break;
			case "idreg":
				idreg = valor;
				break;
			case "idcartao":
				idcartao = valor;
				break;
			case "nome":
				nome = valor;
				break;
			case "setor":
				setor = valor;
				break;
			case "funcao":
				funcao = valor;
				break;
			case "matricula":
				matricula = valor;
				break;
			case "registro":
				registro = valor;
				break;
			case "quantidade":
				quant = valor;
				break;
			case "status":
				stsfnc = valor;
				break;
			case "origem":
				origem = valor;
				break;
			case "ultimoacesso":
				horaacesso = valor;
				break;

			}
		}
	}
	
	public String getIdreg() {
		return idreg;
	}
	
	public String getHoraacesso() {
		return horaacesso;
	}

	public String getNome() {
		return nome;
	}
	public String getSetor() {
		return setor;
	}
	public String getFuncao() {
		return funcao;
	}
	public String getMatricula() {
		return matricula;
	}
	public String getStsfnc() {
		return stsfnc;
	}
	public String getIdcartao() {
		return idcartao;
	}

	public String getData() {
		return data;
	}

	public String getQuant() {
		return quant;
	}
}

	
