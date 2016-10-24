package data;

import java.io.*;
import java.util.ArrayList;

/**
 * Singleton class so .scm is only ever edited at anyone time
 * Created by Yuliang on 10/10/2016.
 */
public class FestivalFileWriter {

    private static FestivalFileWriter _instance;
    private File _festivalSCM;
    private  ArrayList<String> _lines;

    public static FestivalFileWriter getInstance(){
        if (_instance==null){
            _instance = new FestivalFileWriter();
        }
        return _instance;
    }

    private FestivalFileWriter(){
        _festivalSCM = new File(".festival.scm");
    }

    /**
     * Changes first line of scm file
     * @param firstLine
     */
    public void changeVoice(String firstLine){
        _lines = new ArrayList<>();
        try{
            FileReader fr = new FileReader(_festivalSCM);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ( (line = br.readLine()) != null){
                _lines.add(line);
            }
            fr.close();
            br.close();

            FileWriter fw = new FileWriter(_festivalSCM);
            BufferedWriter out = new BufferedWriter(fw);
            for(int i=0;i<_lines.size();i++){
                if(i==0){//replace first line
                    out.write(firstLine+"\n");
                }else {
                    out.write(_lines.get(i)+"\n");
                }
            }
            out.flush();
            out.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Changes the speed of the scm file to the specified speed given as a parameter.
     * @param speed - e.g "1.0"
     */
    public void changeSpeed(String speed){
        _lines = new ArrayList<>();
        try{
            FileReader fr = new FileReader(_festivalSCM);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ( (line = br.readLine()) != null){
                _lines.add(line);
            }
            fr.close();
            br.close();

            FileWriter fw = new FileWriter(_festivalSCM);
            BufferedWriter out = new BufferedWriter(fw);
            for(int i=0;i<_lines.size();i++){
                if(i==1){//replace first line
                    out.write("(Parameter.set 'Duration_Stretch " + speed + ")" + "\n");
                }else {
                    out.write(_lines.get(i)+"\n");
                }
            }
            out.flush();
            out.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Changes last line of scm file to read out the phrase specified as a parameter.
     * @param phrase
     */
    public void changeSpeechText(String phrase){
        _lines = new ArrayList<>();
        try{
            FileReader fr = new FileReader(_festivalSCM);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ( (line = br.readLine()) != null){
                _lines.add(line);
            }
            fr.close();
            br.close();

            FileWriter fw = new FileWriter(_festivalSCM);
            BufferedWriter out = new BufferedWriter(fw);
            for(int i=0;i<_lines.size();i++){
                if(i==2){//replace first line
                    out.write("(SayText \"" + phrase + "\")" + "\n");
                }else {
                    out.write(_lines.get(i)+"\n");
                }
            }
            out.flush();
            out.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


}
