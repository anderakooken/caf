package application.controller;

import java.util.List;

import application.Main;
import application.dao.FuncionarioDao;
import application.model.Combobox;
import application.model.Funcionario;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import util.MaskTextField;

public class CAF_funcionarios_Controller {	
	    
	    private @FXML Label labelqnts;

	    
	    private @FXML MaskTextField txtcracha;	    
	    private @FXML MaskTextField txtmatricula;	    
	    private @FXML TextField txtnome;

	    
	    private @FXML ComboBox<Combobox> cbfuncao;    
	    private @FXML ComboBox<Combobox> cbsetor;

	    
	    private @FXML TableView<Funcionario> table;
	    private @FXML TableColumn<Funcionario, String> cmatricula;
	    private @FXML TableColumn<Funcionario, String> cnome;
	    private @FXML TableColumn<Funcionario, String> cfuncao;
	    private @FXML TableColumn<Funcionario, String> csetor;
	    private @FXML TableColumn<Funcionario, String> ccracha;
	    
	    FuncionarioDao dao = new FuncionarioDao();
	    Funcionario fun = new Funcionario();
	    
	    private @FXML void initialize() {
	    	atualizar();
	    	OnKeyPressed();
	    	setMask();
	    }
	    
	    //Adiciona um funcionario
	    private @FXML void adicionarfun(ActionEvent event) {
	    	if(txtmatricula.getText()==null || txtcracha.getText()==null || txtnome.getText()==null || cbfuncao.getValue()==null || cbsetor.getValue()==null ||
	    		txtmatricula.getText().isEmpty() || txtcracha.getText().isEmpty() || txtnome.getText().isEmpty() || cbfuncao.getValue().getNome().isEmpty() 
	    		|| cbsetor.getValue().getNome().isEmpty()) {
	    		Main.dialogBox("Preencha todos os campos para cadastrar um funcionário!", 1);
	    		return;
	    	}
	    	SetFun();
	    	if(Main.ConfirmationDialog("Você está cadastrando um funcionário", "Tem certeza disso?").get() == ButtonType.OK) {
	    		List<Funcionario> l = dao.listFun("matricula ='"+txtmatricula.getText()+"'");
	    		if(l.size()>0) {
	    			txtnome.setText(l.get(0).get("nome"));
	    			cbfuncao.selectionModelProperty().getValue().select(Main.selectCB(l.get(0).get("funcao"), cbfuncao));
	    			cbsetor.selectionModelProperty().getValue().select(Main.selectCB(l.get(0).get("setor"), cbfuncao));
	    			txtcracha.setText(l.get(0).get("idcartao"));
	    			Main.dialogBox("Não se pode cadastrar a mesma matricula!", 1);
	    			return;
	    		}
	    		dao.adicionarfun(fun);
	    		atualizar();
	    		limpar();
	    	}
	    }
	    //inicia os dados na table
	    private @FXML void initTable(){
	    	cmatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
	    	cnome.setCellValueFactory(new PropertyValueFactory<>("nome"));
	    	cfuncao.setCellValueFactory(new PropertyValueFactory<>("funcao"));
	    	csetor.setCellValueFactory(new PropertyValueFactory<>("setor"));
	    	ccracha.setCellValueFactory(new PropertyValueFactory<>("idcartao"));
	    	List<Funcionario> l = dao.listFun(null);
	    	labelqnts.setText(Integer.toString(l.size()));
	    	table.setItems(FXCollections.observableArrayList(l));
	    	
	    }
	    
	    
	    private @FXML void atualizarfluxos(ActionEvent event) {

	    }
	    
	    //Adiciona os dados no comboboxs, atraves de uma select no banco de dados
	    private void carregarItems() {
	    	cbfuncao.setItems(FXCollections.observableArrayList(dao.listItems("funcao")));
	    	cbsetor.setItems(FXCollections.observableArrayList(dao.listItems("setor")));
	    }
	    
	    private void atualizar() {
	    	initTable();
	    	carregarItems();
	    }
	    
	    private void OnKeyPressed() {
	    	txtmatricula.setOnKeyPressed(e->{
	    		if(e.getCode() == KeyCode.ENTER) {
	    			adicionarfun(null);
	    		}
	    	});
	    	txtnome.setOnKeyPressed(e->{
	    		if(e.getCode() == KeyCode.ENTER) {
	    			adicionarfun(null);
	    		}
	    	});
	    	txtcracha.setOnKeyPressed(e->{
	    		if(e.getCode() == KeyCode.ENTER) {
	    			adicionarfun(null);
	    		}
	    	});
	    	cbfuncao.setOnKeyPressed(e->{
	    		if(e.getCode() == KeyCode.ENTER) {
	    			adicionarfun(null);
	    		}
	    	});
	    	cbsetor.setOnKeyPressed(e->{
	    		if(e.getCode() == KeyCode.ENTER) {
	    			adicionarfun(null);
	    		}
	    	});
	    	
	    }
	    
	    private void limpar() {
	    	txtnome.setText(null);
	    	txtmatricula.setText(null);
	    	txtcracha.setText(null);
	    	
	    }
	    private void SetFun() {
	    	fun.set("nome", txtnome.getText().toUpperCase());
	    	fun.set("dbmr", txtmatricula.getText());
	    	fun.set("idcartao", txtcracha.getText());
	    	fun.set("funcao", cbfuncao.getValue().getNome().toUpperCase());
	    	fun.set("setor", cbsetor.getValue().getNome().toUpperCase());
	    }
	    private void setMask() {
	    	txtmatricula.setMask("NNNNNNNN");
	    	txtcracha.setMask("N!");
	    }
}
