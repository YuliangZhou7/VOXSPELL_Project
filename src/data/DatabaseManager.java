package data;

import gui.DialogBox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * TODO: hashmap of all spelling lists SpellingDatabase objects
 * Created by Yuliang on 22/10/2016.
 */
public class DatabaseManager implements Serializable{

    private HashMap<String , SpellingDatabase > _spellingLists;

    /**
     * Automatically adds default spelling list
     */
    public DatabaseManager(){
        _spellingLists = new HashMap<>();
        addNewSpellingList("Default",new SpellingDatabase());
    }

    public void addNewSpellingList(String nameOfList, SpellingDatabase newList){
        if(_spellingLists.containsKey(nameOfList)){
            DialogBox.errorDialogBox("Error","This file has already been added.");
        }else {
            _spellingLists.put(nameOfList, newList);
        }
    }

    public void addNewDefaultWord(String levelKey, String word) {
        SpellingDatabase defaultList =_spellingLists.get("Default");
        defaultList.addNewWord(levelKey,word);
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
}
