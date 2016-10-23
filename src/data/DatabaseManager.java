package data;

import gui.DialogBox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Serializable object which contains a HashMap of all the SpellingDatabase objects which hold each spelling list.
 *
 * Created by Yuliang on 22/10/2016.
 */
public class DatabaseManager implements Serializable{

    private static final long serialVersionUID = 1L;

    private HashMap<String , SpellingDatabase > _spellingLists;

    /**
     * Automatically adds default spelling list
     */
    public DatabaseManager(){
        _spellingLists = new HashMap<>();
        addNewSpellingList("Default",new SpellingDatabase());
    }

    /**
     * Adds the SpellingDatabase object if it hasn't already been added. Name of list is the
     * name of the text file e.g. "animals.txt"
     * @param nameOfList
     * @param newList
     */
    public void addNewSpellingList(String nameOfList, SpellingDatabase newList){
        if(_spellingLists.containsKey(nameOfList)){
            DialogBox.errorDialogBox("Error","This file has already been added.");
        }else {
            _spellingLists.put(nameOfList, newList);
        }
    }

    /**
     * Adds a word to the level (specified by the key e.g."small animals" or "Level 5" in the Default list
     * @param levelKey
     * @param word
     */
    public void addNewDefaultWord(String levelKey, String word) {
        SpellingDatabase defaultList =_spellingLists.get("Default");
        defaultList.addNewWord(levelKey,word);
    }

    /**
     * Returns the SpellingDatabase object representing the spelling list specified by the key
     * given as a parameter.
     * @param nameOfList
     * @return
     */
    public SpellingDatabase getSpellingList(String nameOfList) {
        return _spellingLists.get(nameOfList);
    }

    /**
     * Returns an ArrayList of all the spellings lists (the key to the spelling list)
     * @return
     */
    public ArrayList<String> getSpellingKeys() {
        ArrayList<String> keys = new ArrayList<>();
        for ( String s :_spellingLists.keySet()){
            keys.add(s);
        }
        return keys;
    }

    /**
     * Adds the key to default SpellingDatabase levels.
     * @param levelCounter
     * @param levelKey
     */
    public void addNewDefaultLevel(int levelCounter, String levelKey) {
        SpellingDatabase defaultList =_spellingLists.get("Default");
        defaultList.addNewLevel(levelCounter,levelKey);
    }


    /**
     * Loops through each spelling list and clears the statistics of each list.
     * No spelling list is deleted - only the scores
     */
    public void clearAllStats() {
        for(String key : _spellingLists.keySet()){
            _spellingLists.get(key).clearStats();
        }
    }

    /**
     * Removes a SpellingDatabase object from the DatabaseManger. Will prevent "Default" list from being removed.
     * Also shows a pop-up to confirm if user wants to delete the spelling list.
     * @param spellingList
     */
    public void removeSpellingList(String spellingList){
        if(spellingList.equals("Default")){
            DialogBox.errorDialogBox("Cannot delete", "Sorry cannot delete the Default list.");
        }else{
            if(DialogBox.displayConfirmDialogBox("Deleting Spelling List","Deleting this list will " +
                    "delete all progress. Are you sure?")) {
                _spellingLists.remove(spellingList);
            }
        }
    }

}
