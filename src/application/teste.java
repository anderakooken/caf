package application;

import java.util.ArrayList;
import java.util.List;

import application.dao.AdicionaisDao;
import application.model.Funcionario;

public class teste {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AdicionaisDao dao = new AdicionaisDao();
		List<Funcionario> flist = new ArrayList<>();
		System.out.println(flist.size());
	}

}
