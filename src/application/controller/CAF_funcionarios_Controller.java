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

public class CAF_funcionarios_Controller {	
	    
	    private @FXML Label labelqnts;

	    
	    private @FXML TextField txtcracha;	    
	    private @FXML TextField txtmatricula;	    
	    private @FXML TextField txtnome;
	    private @FXML TextField txtid;

	    
	    private @FXML ComboBox<Combobox> cbfuncao;    
	    private @FXML ComboBox<Combobox> cbsetor;

	    
	    private @FXML TableView<Funcionario> table;
	    private @FXML TableColumn<Funcionario, String> cmatricula;
	    private @FXML TableColumn<Funcionario, String> cnome;
	    private @FXML TableColumn<Funcionario, String> cid;
	    private @FXML TableColumn<Funcionario, String> cfuncao;
	    private @FXML TableColumn<Funcionario, String> csetor;
	    private @FXML TableColumn<Funcionario, String> ccracha;
	    
	    FuncionarioDao dao = new FuncionarioDao();
	    Funcionario fun = new Funcionario();
	    
	    private @FXML void initialize() {
	    	atualizar();
	    	OnKeyPressed();
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
	    	if(!(txtmatricula.getText().matches("[+-]?\\d*(\\.\\d+)?")) || txtmatricula.getText().contains(".") || txtmatricula.getText().contains(",")) {
	    		Main.dialogBox("Digite uma matricula válida!", 1);
	    		txtmatricula.setText(null);
	    		return;
	    	}
	    	if(!(txtcracha.getText().matches("[+-]?\\d*(\\.\\d+)?")) || txtcracha.getText().contains(".") || txtcracha.getText().contains(",")) {
	    		Main.dialogBox("Digite um crachá válido!", 1);
	    		txtcracha.setText(null);
	    		return;
	    	}
	    	
	    	
	    		//verifica a matricula
	    		List<Funcionario> l = dao.listFun("matricula ='"+txtmatricula.getText()+"'");
	    		List<Funcionario> idc = dao.listFun("idcartao='"+txtcracha.getText()+"'");
	    		
	    		if(txtid.getText() == null && (l.size()>0 || idc.size()>0) || txtid.getText().isEmpty() && (l.size()>0 || idc.size()>0)) {
	    			String msg = "";
	    			if(l.isEmpty()) {
	    				l = idc;
	    				msg = "Não se pode cadastrar o mesmo crachá!";
	    			}else {
	    				msg = "Não se pode cadastrar a mesma matricula!";
	    			}
	    			txtnome.setText(l.get(0).get("nome"));
	    			cbfuncao.selectionModelProperty().getValue().select(Main.selectCB(l.get(0).get("funcao"), cbfuncao));
	    			cbsetor.selectionModelProperty().getValue().select(Main.selectCB(l.get(0).get("setor"), cbfuncao));
	    			txtcracha.setText(l.get(0).get("idcartao"));
	    			Main.dialogBox(msg, 1);
	    			return;
	    		}
	    		
	    		else if(txtid.getText() == null && l.isEmpty() || txtid.getText().isEmpty() && l.isEmpty()) {
	    			if(Main.ConfirmationDialog("Você está cadastrando um funcionário", "Tem certeza disso?").get() == ButtonType.OK) {
	    				dao.funCED(fun,1);
	    				
	    			}
	    		}
	    		if(!(txtid.getText().matches("[+-]?\\d*(\\.\\d+)?")) || txtid.getText().contains(".") || txtid.getText().contains(",")) {
	    			Main.dialogBox("Digite um ID válido!", 1);
	    			txtid.setText(null);
	    			return;
	    		}
	    		
	    		else if(txtid.getText() != null || !(txtid.getText().isEmpty())) {
	    			if(Main.ConfirmationDialog("Você está atualizando um funcionário", "Tem certeza disso?").get() == ButtonType.OK) {
	    				fun.set("idreg", txtid.getText());
	    				dao.funCED(fun, 2);
	    			}
	    		}
	    		
	    		
	    		atualizar();
	    		limpar();
	    	
	    }
	    //inicia os dados na table
	    private @FXML void initTable(){
	    	cmatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
	    	cnome.setCellValueFactory(new PropertyValueFactory<>("nome"));
	    	cfuncao.setCellValueFactory(new PropertyValueFactory<>("funcao"));
	    	csetor.setCellValueFactory(new PropertyValueFactory<>("setor"));
	    	ccracha.setCellValueFactory(new PropertyValueFactory<>("idcartao"));
	    	cid.setCellValueFactory(new PropertyValueFactory<>("idreg"));
	    	
	    	
	    	List<Funcionario> l = dao.listFun(null);
	    	labelqnts.setText(Integer.toString(l.size()));
	    	table.setItems(FXCollections.observableArrayList(l));
	    	
	    }
	    
	    private @FXML void tableclick() {
	    	int i;
	    	i= table.getSelectionModel().getSelectedIndex();
	    	if(i>=0) {
	    		Funcionario fun = table.getItems().get(i);
	    		
	    		txtnome.setText(fun.get("nome"));
	    		txtmatricula.setText(fun.get("matricula"));
	    		txtcracha.setText(fun.get("idcartao"));
	    		txtid.setText(fun.get("idreg"));
	    		cbfuncao.selectionModelProperty().get().select(Main.selectCB(fun.get("funcao"), cbfuncao));
	    		cbsetor.selectionModelProperty().get().select(Main.selectCB(fun.get("setor"), cbsetor));
	    	}
	    }
	    
	    private @FXML void excluirfuncionario(ActionEvent event) {
	    	if(txtid.getText() == null || txtid.getText().isEmpty()) {
	    		Main.dialogBox("Preencha todos os campos para cadastrar um funcionário!", 1);
	    		return;
	    	}
	    	if(!(txtid.getText().matches("[+-]?\\d*(\\.\\d+)?")) || txtid.getText().contains(".") || txtid.getText().contains(",")) {
    			Main.dialogBox("Digite um ID válido!", 1);
    			txtid.setText(null);
    			return;
    		}
	    	if(Main.ConfirmationDialog("Você está excluindo um funcionário", "Tem certeza disso?").get() == ButtonType.OK) {
	    		fun.set("idreg", txtid.getText());
	    		dao.funCED(fun, 3);
	    		atualizar();
	    		limpar();
	    	}
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
	    	txtid.setOnKeyPressed(e->{
	    		if(e.getCode() == KeyCode.ENTER) {
	    			adicionarfun(null);
	    		}
	    	});
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
}
