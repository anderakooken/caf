package application.model;

public class Usuario {
	private static String matricula, matricula_emp;
	private static int nvacesso;
	private static String nmuser ;
	private static String pwrc;
	
	public  String getMatricula() {
		return matricula;
	}



	public  void setMatricula(String matricula) {
		Usuario.matricula = matricula;
	}



	public  String getMatricula_emp() {
		return matricula_emp;
	}



	public  void setMatricula_emp(String matricula_emp) {
		Usuario.matricula_emp = matricula_emp;
	}



	public  int getNvacesso() {
		return nvacesso;
	}



	public  void setNvacesso(int nvacesso) {
		Usuario.nvacesso = nvacesso;
	}



	public  String getNmuser() {
		return nmuser;
	}



	public  void setNmuser(String nmuser) {
		Usuario.nmuser = nmuser;
	}



	public  String getPwrc() {
		return pwrc;
	}



	public  void setPwrc(String pwrc) {
		Usuario.pwrc = pwrc;
	}



	
	
	
	
}
