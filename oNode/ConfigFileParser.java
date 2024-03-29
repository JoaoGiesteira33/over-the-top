package oNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * Classe auxiliar que faz parse
 * de ficheiros na pasta configFiles,
 * ficheiros esses que têm informação sobre
 * os vizinhos de cada nodo.
 */
public class ConfigFileParser {
    public static Map<String,List<String>> readFile(File f){
        Map<String,List<String>> result = new HashMap<>();

        try{
            Scanner myReader = new Scanner(f);
            while(myReader.hasNextLine()){
                String data = myReader.nextLine();

                //Dividir linha
                String[] ips = data.split(" ");
                
                //Criar lista de vizinhos
                List<String> vizinhos = new ArrayList<>();
                for(int i = 1 ; i < ips.length ; i++){
                    vizinhos.add(ips[i]);
                }
                
                //Adicionar entrada ao Map
                result.put(ips[0], vizinhos);
            }
            myReader.close();
        }catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }
            
        return result;
    }
}
