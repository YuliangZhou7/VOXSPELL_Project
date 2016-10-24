package data;

import gui.DialogBox;

import java.io.*;

/**
 * DatabaseIO object manages all object serialization. Reads DatabaseManager object from .ser file
 * into an object. Writes the SpellingDatabase object into a hidden .ser file.
 * Also reads spelling list text file and appends the words to the SpellingDatabase object
 *
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
    public DatabaseManager openData(File inputFile) {
        DatabaseManager data = null;
        if(inputFile.exists()){
            try {
                FileInputStream streamIn = new FileInputStream(inputFile);
                ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
                data = (DatabaseManager) objectinputstream.readObject();
                streamIn.close();
                objectinputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{//if serialized data does not exist then create a new DatabaseManager and add "Default" spelling list
            data = new DatabaseManager();
            updateDefaultWordList(data);
        }
        return data;
    }


    /**
     * Saves the DatabaseManager object instance to a hidden .ser file.
     * Called when main GUI frame is closed.
     * @param data
     * @param out
     */
    public void writeData(DatabaseManager data, File out){
        if(!out.exists()){ //create hidden .ser file if it does not exist
            try {
                out.createNewFile();
            } catch (IOException e) {
                DialogBox.errorDialogBox("Error","Error saving data. Please refer to manual");
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
     * This method is called only when a new DatabaseManager is created.
     * @param database
     */
    public void updateDefaultWordList(DatabaseManager database){
        try {
            InputStream in = DatabaseIO.class.getResourceAsStream("/resources/Default_Spelling_List.txt");
            InputStreamReader fr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String levelKey = "";
            int levelCounter = 0;
            while((line = br.readLine())!=null){
                if(line.charAt(0) == '%' ){//get level key
                    levelCounter++;
                    levelKey = line.substring(1);
                    //add key to the level keys hashmap
                    database.addNewDefaultLevel(levelCounter,levelKey);
                }else{
                    database.addNewDefaultWord(levelKey, line.trim());
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
     * Reads each line of the .txt file and appends words to each level to a SpellingDatabase object.
     * Returns true if the spelling list is compatible.
     * @param database
     * @param customSpellingList
     * @return
     */
    public boolean readNewWordList(SpellingDatabase database, File customSpellingList){
        boolean isSuccessful=false;
        try {
            FileReader fr = new FileReader(customSpellingList);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String levelKey = "";
            int levelCounter = 0;
            while((line = br.readLine())!=null){
                if(line.charAt(0) == '%' ){//get level key
                    isSuccessful = true;
                    levelCounter++;
                    if(levelCounter > 11){ // if there are more than 11 levels then spelling list is invalid
                        return false;
                    }
                    //update the level key and add it to the level keys hashmap
                    levelKey = line.substring(1);
                    database.addNewLevel(levelCounter,levelKey);
                }else{
                    database.addNewWord(levelKey, line.trim());
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            DialogBox.errorDialogBox("Error","Could not find spelling list text file. Please refer to README and try again.");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally{
            if(isSuccessful) {
                return true;
            }else{
                return false;
            }
        }
    }

}
