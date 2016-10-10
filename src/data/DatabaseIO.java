package data;

import gui.DialogBox;
import gui.Main;

import java.io.*;

/**
 * DatabaseIO object manages all object serialization. Reads SpellingDatabase object from .ser file
 * into an object. Writes the SpellingDatabase object into a hidden .ser file.
 * Also reads spelling list text file and appends the words to the SpellingDatabase object upon opening
 * the object.
 * TODO: allow for different .ser files for different lists
 * Created by Yuliang Zhou on 8/09/2016.
 */
public class DatabaseIO {

    /**
     * Upon creating the GUI frame open the saved data if it exists.
     * Otherwise create a new object.
     * Only called in initialisation of main GUI frame.
     * @param inputFile
     * @return
     */
    public SpellingDatabase openData(File inputFile) {
        SpellingDatabase data = null;
        if(inputFile.exists()){
            try {
                FileInputStream streamIn = new FileInputStream(inputFile);
                ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
                data = (SpellingDatabase) objectinputstream.readObject();
                streamIn.close();
                objectinputstream.close();
                System.out.println("Open old object");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{//if serialized data does not exist then create a new object
            data = new SpellingDatabase();
            System.out.println("New object created");
        }
        updateDefaultWordList(data);
        return data;
    }


    /**
     * Saves the SpellingStatsModel object instance to a hidden .ser file.
     * Called when main GUI frame is closed.
     * @param data
     * @param out
     */
    public void writeData(SpellingDatabase data,File out){
        if(!out.exists()){ //create hidden .ser file if it does not exist
            try {
                out.createNewFile();
            } catch (IOException e) {
                //error creating hidden .ser file
            }
        }
        //write SpellingStatsModel object instance's data to the hidden file
        try {
            FileOutputStream fout = new FileOutputStream(out, false);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(data);
            fout.close();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Reads each line of the default word list file and adds any new words to current word list object
     * in SpellingStatsModel.
     * This method is called after reading the data.
     * @param database
     */
    public void updateDefaultWordList(SpellingDatabase database){
        try {
            InputStream in = DatabaseIO.class.getResourceAsStream("/resources/Default_Spelling_List.txt");
            InputStreamReader fr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String levelKey = "";
            while((line = br.readLine())!=null){
                if(line.charAt(0) == '%' ){//get level key
                    levelKey = line.substring(1);
                }else{
                    database.addNewWord(levelKey, line.trim());
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            DialogBox.errorDialogBox("Error","Could not find spelling list text file. Please refer to README and try again.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads each line of the specified spelling list file (given as the second parameter) and appends any new words
     * to the SpellingDatabase object.
     * @param database
     * @param customSpellingList
     */
    public void updateWordList(SpellingDatabase database, File customSpellingList){
        try {
            FileReader fr = new FileReader(customSpellingList);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String levelKey = "";
            while((line = br.readLine())!=null){
                if(line.charAt(0) == '%' ){//get level key
                    levelKey = line.substring(1);
                }else{
                    database.addNewWord(levelKey, line.trim());
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            DialogBox.errorDialogBox("Error","Could not find spelling list text file. Please refer to README and try again.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
