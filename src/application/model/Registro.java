package application.model;

public class Registro {
	private String idreg, matricula, nome, dtreg, hrreg, iduser, statusreg, nmstatus, reg_empregado, idnegado, idtipo,nmtipo, vrunit, count, qtdr, qq, dif,tlref,qtdm,total;
	
	public String get(String variavel) {
		String rtn = "";
		if(!(variavel == null || variavel.isEmpty())) {
			switch(variavel) {
			case "idreg":
				rtn = idreg;
				break;
			case "matricula":
				rtn = matricula;
				break;
			case "datareg":
				rtn = dtreg;
				break;
			case "horareg":
				rtn = hrreg;
				break;
			case "nome":
				rtn = nome;
				break;
			case "tipo":
				rtn = nmtipo;
				break;
			case "quantidade":
				rtn = count;
				break;
			case "idusuario":
				rtn = iduser;
				break;
			case "status":
				rtn = statusreg;
				break;
			case "nmstatus":
				rtn = nmstatus;
				break;
			case "registro":
				rtn = reg_empregado;
				break;
			case "idnegado":
				rtn = idnegado;
				break;
			case "idtipo":
				rtn = idtipo;
				break;
			case "vrunit":
				rtn = vrunit;
				break;
				//Variaveis para o faturamento mensal
			case "qtdr":
				rtn = qtdr;
				break;
			case "qtdm":
				rtn = qtdm;
				break;
			case "qq":
				rtn = qq;
				break;
			case "diferenca":
				rtn = dif;
				break;
			case "totalref":
				rtn = tlref;
				break;
			case "total":
				rtn = total;
				break;
			}
		}
		return rtn;
	}
	
	public void set(String variavel, String valor) {
		if(!(variavel == null || variavel.isEmpty())) {
			switch(variavel) {
			case "matriculaeregistro":
				matricula = valor;
				reg_empregado = valor;
				break;
			case "idreg":
				idreg = valor;
				break;
			case "matricula":
				matricula = valor;
				break;
			case "nome":
				nome = valor;
				break;
			case "tipo":
				nmtipo = valor;
				break;
			case "quantidade":
				count = valor;
				break;
			case "datareg":
				dtreg = valor;
				break;
			case "horareg":
				hrreg = valor;
				break;
			case "idusuario":
				iduser = valor;
				break;
			case "status":
				statusreg = valor;
				break;
			case "nmstatus":
				nmstatus = valor;
				break;
			case "registro":
				reg_empregado = valor;
				break;
			case "idnegado":
				idnegado = valor;
				break;
			case "idtipo":
				idtipo = valor;
				break;
			case "vrunit":
				vrunit = valor;
				break;
				//Variaveis para o faturamento mensal
			case "qtdr":
				qtdr = valor;
				break;
			case "qtdm":
				qtdm = valor;
				break;
			case "qq":
				qq = valor;
				break;
			case "diferenca":
				dif = valor;
				break;
			case "totalref":
				 tlref = valor;
				break;
			case "total":
				total = valor;
				break;
			}
		}
	}

	@Override
	public String toString() {
		return "Registro [dtreg=" + dtreg + ", idtipo=" + idtipo + ", nmtipo=" + nmtipo + ", vrunit=" + vrunit
				+ ", count=" + count + ", qtdr=" + qtdr + ", qq=" + qq + ", dif=" + dif + ", tlref=" + tlref + ", qtdm="
				+ qtdm + ", total=" + total + "]";
	}
}
