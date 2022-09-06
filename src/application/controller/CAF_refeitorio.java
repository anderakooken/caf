package application.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import application.ArquivoTxt;
import application.Main;
import application.dao.RefeitorioDao;
import application.model.Funcionario;
import application.model.Registro;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class CAF_refeitorio {
	
    private @FXML Pane pane = new Pane();
    
    public static Scene scene;
    
    private @FXML Label lbhora;
    private @FXML Label labelacesso;
    private @FXML Label labelevento;
    private @FXML Label lbdata;
    private @FXML Label lbrefeicao;
    
    private @FXML Button btnadicionar;
    
    private @FXML Button btnmovimento;

    
    private @FXML TextField txtnome;
    private @FXML TextField txtultimo;
    private @FXML TextField txtsetor;
    private @FXML TextField txtfuncao;
	private @FXML TextField txtmatricula;
	private String idcartao = "";
    
    
    private @FXML TableView<Funcionario> table;
    private @FXML TableColumn<Funcionario, String> colunafuncao;
    private @FXML TableColumn<Funcionario, String> colunasituacao;  
    private @FXML TableColumn<Funcionario, String> colunadepartamento;
    private @FXML TableColumn<Funcionario, String> colunanome;
    private @FXML TableColumn<Funcionario, String> colunamatricula;
    private @FXML TableColumn<Funcionario, String> colunaacesso;
    
    /*---------------------Vars--------------------*/
    public static RefeitorioDao dao = new RefeitorioDao();
    private Funcionario funcionario = new Funcionario();
    private Registro reg = new Registro();
 	private DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
 	private SimpleDateFormat formatador = new SimpleDateFormat("HH:mm:ss");
 	private String data = dt.format(LocalDateTime.now());
 	private String dataf = data.substring(8,10) + "/" + data.substring(5,7) + "/" + data.substring(0, 4);
 	private String status;
 	private String matrc_real;
 	private String cor = "-fx-background-color: ";
 	 /*---------------------Vars--------------------*/
    private @FXML void initialize() {
   
		
    	lbdata.setText(dataf);
    	//int hora = Integer.parseInt(data.substring(11,13));
    	List<String> l = dao.lista();
    	if(l.size()>0 && !(l.isEmpty())) {
    		lbrefeicao.setText(l.get(0));
    	}else {
    		lbrefeicao.setText("-");
    	}
    	
    	txtmatricula.setFocusTraversable(true);
    	txtmatricula.setOnKeyPressed(e ->{
    		if(e.getCode() == KeyCode.ENTER) {
    			btnadicionar(null);
    		}
    		if(e.getCode() == KeyCode.TAB) {
    			btnadicionar(null);
    		}
    	});
    	
    	initTable();
    	hora();
    }
    
	public @FXML void btnadicionar(ActionEvent event) {
		
		txtmatricula.requestFocus();
		
		//Se o cracha nao for de acordo com o calculo ou 
		if(txtmatricula.getText() == null) {
			
			txtmatricula.setText(null);
			return;
		}
		if(!(txtmatricula.getText().matches("[+-]?\\d*(\\.\\d+)?")) || txtmatricula.getText().contains(".") || txtmatricula.getText().contains(",")) {
			labelevento.setText("Leitura incorreta tente novamente!");
			txtmatricula.setText(null);
			return;
		}
		try{
			if(txtmatricula.getText().isEmpty() || txtmatricula.getText().length()<2 || Integer.parseInt(txtmatricula.getText())-2<=0) {

				//Main.dialogBox("Leitura incorreta passe o crachá novamente.", 1);
				labelevento.setText("Leitura incorreta passe o crachá novamente!");
				txtmatricula.setText(null);
				return;
			}
		}catch(NumberFormatException e){
			labelevento.setText("Leitura incorreta passe o crachá novamente!");
			txtmatricula.setText(null);
			return;
		}
		if(txtmatricula.getText().substring(0,txtmatricula.getLength()-2).length() != Integer.parseInt(txtmatricula.getText().substring(0,2))) {
			//Main.dialogBox("Leitura incorreta passe o crachá novamente.", 1);
			labelevento.setText("Leitura incorreta passe o crachá novamente!");
			txtmatricula.setText(null);
			return;
		}
		
		txtmatricula.setText(txtmatricula.getText().substring(2));
		idcartao = txtmatricula.getText();
		txtmatricula.setText(null);
		
		
		//Matricula gerencial
		if(idcartao.equals("9000077")) {
			matrc_real = idcartao;
			//labelacesso.setText("Matricula Inv�lida");
			txtnome.setText("ACESSO GERENCIAL");
			status = "3";
			labelacesso.setStyle(cor+"ORANGE;");
			labelacesso.setText("Acesso Gerencial Autorizado");
			setRegistro();
			dao.CREG(reg);
			atualizar();
			ArquivoTxt.writeComprovante("Aprovado", idcartao);
			labelevento.setText("-");
			return;
		}
		
		if(dao.verifica(idcartao) == false){
			//Main.dialogBox("Matrícula não encontrada!", 1);
			labelevento.setText("Matrícula não encontrada!");
			atualizar();
			return;
		}
		
		//Setando os dados pegos pelo banco de dados
		funcionario = new Funcionario();
		List<Funcionario> l = dao.Registro(idcartao, "funcionarios");
		if(l.size() != -1) {
			funcionario = l.get(0);
			txtnome.setText(funcionario.get("nome"));
			txtsetor.setText(funcionario.get("setor"));
			txtfuncao.setText(funcionario.get("funcao"));
			//pegando a matricula real para salvar no banco de dados em breve
			matrc_real = funcionario.get("matricula");
			
			
			//qtdlib quantidade de vezes que se pode comer, no refeitorio.
			String qtdlib = dao.qtdlib(matrc_real);
			
			String cont = dao.contagem("tb_registros", "matricula='" + matrc_real+"'", "idreg", "hoje");
			//verifica se tem qtdlib/direito de comer
			if(qtdlib == null || qtdlib.isEmpty()) {
				//compara a contagem de vezes que a pessoa comeu no dia com a quantidade que ela pode comer
				qtdlib = "1";
				
			}/*else {
				int i = Integer.parseInt(qtdlib)+1;
				if(i==0) {
					//Codigo do exceto tb_adcionais que nao esta sendo utilizado no CAF
				}
			}*/
			
			if(Integer.parseInt(cont) < Integer.parseInt(qtdlib)) {
				cont = dao.contagem("tb_registros", "matricula = '"+matrc_real+"'", "idreg", null);
				//verifica se ja houve registro anteriores antes
				if(Integer.parseInt(cont) > 0) {
					l = dao.Registro(matrc_real, null);
					funcionario = l.get(0);
					txtultimo.setText(funcionario.get("ultimoacesso"));
				}else {
					txtultimo.setText("Sem Acesso Anterior " + matrc_real);
				}
				
				labelacesso.setStyle(cor+"#758c48");
				labelacesso.setText("Autorizado");
				status = "1"; // status de autorizado
				setRegistro();
				dao.CREG(reg);
				atualizar();
				ArquivoTxt.writeComprovante("Aprovado", idcartao);
				labelevento.setText("-");
			}
			else {
				//Se a pessoa nao almocou hoje, mas nao tem direito a refeicao
				if(cont.equals("0")) {
					txtultimo.setText("Sem Acesso Anterior - Sem Direito a Refeição");
				}else {
					//Se a pessoa almocou hoje e nao tem mais direito de comer
					l = dao.Registro(matrc_real, null);
					funcionario = l.get(0);
					txtultimo.setText(funcionario.get("ultimoacesso"));
					labelacesso.setStyle(cor+"#ba1419");
					labelacesso.setText("Negado");
					status = "2"; // status negado
					setRegistro();
					dao.CREG(reg);
					atualizar();
					labelevento.setText("-");
				}
			}
			
		}else {
			//Main.dialogBox("Matricula sem registro no banco de dados!", 1);
			labelevento.setText("Matricula sem registro no banco de dados!");
		}
    }
    
    @FXML void btnmovimento(ActionEvent event) {
    
    	System.out.println(lbhora.getText().substring(0,8));
    	txtmatricula.requestFocus();
    	Main.modify.modify("view/CAF_refeitorio_relatorio.fxml", "Controle de Acesso - Relatório");
    }
    
    private void initTable() {
    	colunafuncao.setCellValueFactory(new PropertyValueFactory<>("funcao"));
    	colunanome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	colunaacesso.setCellValueFactory(new PropertyValueFactory<>("horaacesso"));
    	colunamatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
    	colunadepartamento.setCellValueFactory(new PropertyValueFactory<>("setor"));
    	colunasituacao.setCellValueFactory(new PropertyValueFactory<>("stsfnc"));
    	
    	table.setItems(FXCollections.observableArrayList(dao.Registro(null, null)));
    }
    
    private void atualizar() {
    	dt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    	List<String> l = dao.lista();
    	if(l.size()>0 && !(l.isEmpty())) {
    		lbrefeicao.setText(l.get(0));
    	}else {
    		lbrefeicao.setText("-");
    	}
    	data = dt.format(LocalDateTime.now());
    	dataf = data.substring(8,10) + "/" + data.substring(5,7) + "/" + data.substring(0, 4);
    	lbdata.setText(dataf);
    	initTable();
    }
    
    private void setRegistro() {
    	List<String> l = dao.lista();
    	dt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		reg.set("nome", txtnome.getText());
		reg.set("matriculaeregistro", matrc_real);
		data = dt.format(LocalDateTime.now());
		reg.set("horareg", data.substring(11,19));
		reg.set("datareg", data.substring(0,10));
		
		if(l.size()>0 && !(l.isEmpty())) {
			reg.set("idtipo", l.get(2));
			reg.set("vrunit", l.get(1));
		}else {
			reg.set("idtipo", "0");
			reg.set("vrunit", "0.00");
		}
		reg.set("idusuario", Main.user.getMatricula());
		reg.set("status", status);
    }
    
    private void hora() {
    	KeyFrame frame = new KeyFrame(Duration.millis(1000), e -> {
    		Date agora = new Date();
    		lbhora.setText(formatador.format(agora).toUpperCase());
    		if(lbhora.getText().equals("10:00:00") || lbhora.getText().equals("16:00:00") || lbhora.getText().equals("04:00:00") || lbhora.getText().equals("22:00:00")) {
    			atualizar();
    		}
    	});
		Timeline timeline = new Timeline(frame);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
    }
}

