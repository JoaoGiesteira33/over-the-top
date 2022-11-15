package oNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConfigFileParser {
    public static Map<String,List<String>> readFile(File f){
        Map<String,List<String>> result = new HashMap<>();

        try{
            Scanner myReader = new Scanner(f);
            while(myReader.hasNextLine()){
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        }catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }
            
        return result;
    }
}
