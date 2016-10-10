package data;

import gui.Festival;

import java.io.*;
import java.util.ArrayList;

/**
 * Singleton class so .scm is only ever edited at anyone time
 * Created by Yuliang on 10/10/2016.
 */
public class FestivalFileWriter {

    private static FestivalFileWriter _instance;
    private static File _festivalSCM;
    private static ArrayList<String> _lines;

    public static FestivalFileWriter getInstance(){
        if (_instance==null){
            _instance = new FestivalFileWriter();
        }
        return _instance;
    }

    public FestivalFileWriter(){
        _festivalSCM = new File(".festival.scm");
    }

    public static void changeVoice(String firstLine){
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
    
    public static void changeSpeed(String secondLine){
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
                    out.write(secondLine+"\n");
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

    public static void changeSpeechText(String thirdLine){
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
                    out.write(thirdLine+"\n");
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

    public static void main(String[] args){
        FestivalFileWriter.getInstance().changeVoice("(voice_akl_nz_jdt_diphone)");
        FestivalFileWriter.getInstance().changeSpeed("(Parameter.set 'Duration_Stretch 1.5)");
        FestivalFileWriter.getInstance().changeSpeechText("(SayText \"YASSSSSSSSSSSSSSS\")");
    }

}
