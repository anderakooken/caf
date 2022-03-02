package application;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

import application.model.Combobox;
import application.model.Usuario;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;


public class Main extends Application {
	public static Stage Stagemain;
	public static ModifyScenes modify = new ModifyScenes();
	public static Usuario user = new Usuario();
	private static HttpURLConnection connection;
	public void start(Stage primaryStage) {
		
		try {
			Stagemain = primaryStage;
			Pane FXMLlogin = FXMLLoader.load(getClass().getResource("view/CAF_inicio.fxml"));
			Scene MainScene = new Scene(FXMLlogin);
			Stagemain.setOnCloseRequest(e -> Platform.exit());
			Stagemain.setTitle("CAF - Controle de Acesso");
			Stagemain.setResizable(false);
			Stagemain.getIcons().add(new Image(this.getClass().getResourceAsStream("view/Icons/java.png")));
			Stagemain.setScene(MainScene);
			Stagemain.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	//Funcao para o dialog warning e information
		public static void dialogBox(String text, int i){
			Stage stage;
			Alert alert = new Alert(null);
			if(i==1) {
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning Dialog");
					alert.setHeaderText("Atenção!");
					stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image(Main.class.getResourceAsStream("view/Icons/Atencao.png")));
					alert.setContentText(text);
					alert.show();
					
			} else if(i==2) {
				alert = new Alert(AlertType.INFORMATION);
				
				alert.setTitle("Information Dialog");
				alert.setHeaderText("Atenção!");
				stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(Main.class.getResourceAsStream("view/Icons/information.png")));
				alert.setContentText(text);
				alert.show();
			}
			
		}
		//Funcao para o dialog confirmation.
		public static Optional<ButtonType> ConfirmationDialog(String Header, String text){
			Stage stage;
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(Main.class.getResourceAsStream("view/Icons/confirm.png")));
			alert.setHeaderText(Header);
			alert.setContentText(text);

			Optional<ButtonType> result = alert.showAndWait();
			
			
			return result;
			
		}
		
	/*	public static void imprimir(String tipo) {
			switch(tipo.toLowerCase()) {
			case "relatorio hoje":
				Desktop dk = Desktop.getDesktop();
				try(FileWriter fw = new FileWriter("C:\\CAF\\impressao.txt",false);
						BufferedWriter bf = new BufferedWriter(fw);
						PrintWriter print = new PrintWriter(bf);) {
					
					print.print("");
					
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//File file = new File("C:\\teste.pdf");
				try {
					dk.print(new File("C:\\impressao.txt"));
					//dk.open(file);
					//System.out.println("teste");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}*/
		
		
		public static int selectCB(String item, ComboBox<Combobox> cb) {
	    	int i = -1;
	    	if(!(item == null || item.isEmpty())) {
		    	for(i=0; i<=cb.getItems().size(); i++) {
					cb.selectionModelProperty().getValue().select(i);
					if(item.toUpperCase().equals(cb.getValue().getNome().toUpperCase())) {
						i = cb.getItems().size();
					}
				}
	    	}
	    	return i;
	    }
		
		public static void reiniciar() {
			for(int i=0; i<ModifyScenes.listS.size(); i++) {
				Stage s = ModifyScenes.listS.get(i);
				s.close();
				ModifyScenes.listS.remove(i);
			}
			ModifyScenes.close();
			Main.Stagemain.close();
			Main.user = new Usuario();
			Platform.runLater(() -> new Main().start(new Stage()));
			
		}
		
		public static String getWeek(String date){ //ex 07/03/2017
		    String dayWeek = "---";
		    GregorianCalendar gc = new GregorianCalendar();
		    try {
		        gc.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
		        switch (gc.get(Calendar.DAY_OF_WEEK)) {
		            case Calendar.SUNDAY:
		                dayWeek = "DOM";
		                break;
		            case Calendar.MONDAY:
		                dayWeek = "SEG";
		                break;
		            case Calendar.TUESDAY:
		                dayWeek = "TER";
		            break;
		            case Calendar.WEDNESDAY:
		                dayWeek = "QUA";
		                break;
		            case Calendar.THURSDAY:
		                dayWeek = "QUI";
		                break;
		            case Calendar.FRIDAY:
		                dayWeek = "SEX";
		                break;
		            case Calendar.SATURDAY:
		                dayWeek = "SAB";

		        }
		    } catch (ParseException e) {
		        e.printStackTrace();
		    }
		    return dayWeek;
		}
		public static String formatDatedb(String data) {
			String newdate = "";
			newdate = data.substring(8,10) + "/" + data.substring(5,7) + "/" + data.substring(0, 4);
			return newdate;
		}
		
		public static Boolean verifyJSON(String login, String password) {
			BufferedReader reader;
			String line;
			StringBuffer responseContent = new StringBuffer();
			//Codificando login e senha para o formato url
			login = URLEncoder.encode(login, StandardCharsets.UTF_8);
			password = URLEncoder.encode(password, StandardCharsets.UTF_8);
			
				try {
					//Estabelecendo conexão
					URL url = new URL("http://192.168.254.216/auth.php?auth&login="+login+"&passwd="+password+"");
					connection = (HttpURLConnection) url.openConnection();
					
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					connection.setReadTimeout(5000);
					
					int status = connection.getResponseCode();
					if(status==500) {
						dialogBox("Usuário não existe ou está desabilitado!",1);
						return false;
					}
					if(status==200) {
						reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						//Passando o corpo JSON
						while((line=reader.readLine())!=null) {
							responseContent.append(line);
						}
						reader.close();
					}
					//Verificando o JSON
					String responsebody = responseContent.toString();
					if(responsebody.isEmpty() || responsebody == null) {
						dialogBox("Nome de Usuário ou Senha incorreto!",1);
						return false;
					}
					//Pegando o json array, passando pra objeto e depois passando novamente para array.
					JSONArray jsonarray = new JSONArray(responsebody);
					JSONObject jsonobjeto = jsonarray.getJSONObject(0);
					JSONArray grupos = jsonobjeto.getJSONArray("grupos");
					//Verificando se tem acesso ao CAF
					for(int i = 0; i<grupos.length(); i++) {
						if(grupos.get(i).equals("CAF")) {
							return true;
						}
					}
				
					dialogBox("Você não tem permissão para acessar esse programa!",1);
					return false;
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					connection.disconnect();
				}
				return false;
		}
		
}
