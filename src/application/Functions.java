package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Functions {
    
    public static void addvalues(Object[] parametros){
		int option = (int) parametros[0];

		if(option == 1){
			JSONObject cache = (JSONObject)parametros[2];
			
            File f = new File((String)parametros[1]);
            if(f.exists()){
                try (FileWriter fw = new FileWriter(f)) {
                    BufferedWriter bw = new BufferedWriter( fw );
                    bw.write(cache.toString());
                    bw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }else{
                try {
                    f.createNewFile();
                    addvalues(parametros);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
				
			}
		}else{
			String log = (String)parametros[2];
			
            File f = new File((String)parametros[1]);
            if(f.exists()){
                try (FileWriter fw = new FileWriter(f)) {
                    BufferedWriter bw = new BufferedWriter( fw );
                    bw.write(log);
                    bw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }else{
                try {
                    f.createNewFile();
                    addvalues(parametros);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
			
		}
		
	}

    public static List<Object> getvalues(Object[] parametros){
		int option = (int) parametros[0];
		List<Object> lista = new ArrayList<>();
		if(option == 1){
			JSONObject cache = new JSONObject();
	
            File f = new File((String)parametros[1]);
            if(f.exists()){
                try (FileReader fw = new FileReader(f)) {
                    BufferedReader bw = new BufferedReader( fw );
                    
                    try{
                        String i = "";
                        var l = bw.lines().toList();
                        for (int index = 0; index < l.size(); index++) {
                            i = i + l.get(index);	
                        }
                    
                        cache = new JSONObject(i);
                        lista.add(true);
                        lista.add(cache);
                    }catch(NullPointerException | JSONException e){
                        lista.add(false);
                        return lista;
                    }
                        
                    
                    
                    bw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    lista.add(false);
                }

            }else{
                try {
                    if(f.createNewFile() == true){
                    
                        lista = getvalues(parametros);
                    }else{
                        lista.add(false);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    lista.add(false);
                }
            }
			
		}
		
		return lista;
	}

}
