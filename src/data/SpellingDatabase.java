package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.*;

/**
 * SpellingDatabase is a serializable object and saves a HashMap of all the spelling words from each
 * level. With the level as the Key. It also stores the accuracy for each level.
 * Created by Yuliang Zhou on 12/09/2016.
 */
public class SpellingDatabase implements Serializable{

    private static final long serialVersionUID = 1L;

    private HashMap< String, ArrayList<Word> > _spellingWords;
    private HashMap< String, ArrayList<Word> > _failedWords;

    //4pts for Mastered, 2pts for Faulted, 0pts for Failed
    private HashMap< String, Integer > _scoreForLevel;
    private HashMap< String, Integer > _attemptsForLevel;

    private String _voice;
    private String _voiceSpeed;

    public SpellingDatabase(){
        _spellingWords = new HashMap<>();
        _failedWords = new HashMap<>();
        _scoreForLevel = new HashMap<>();
        _attemptsForLevel = new HashMap<>();
        _voice = "Default";
        _voiceSpeed = "1.00";
    }

    /**
     * Adds a new word given as a string to the database with a given level. If the
     * word is already in the list then it is not added. Words are saved in an
     * arraylist for each level which is contained in a hashmap with level being the key.
     * e.g. "Level 1" key contains the word "the"
     * @param levelKey
     * @param word
     */
    public void addNewWord(String levelKey, String word) {
        if(!_spellingWords.containsKey(levelKey)){
            ArrayList<Word> wordsInLevel = new ArrayList<>();
            wordsInLevel.add(new Word(word));
            _spellingWords.put(levelKey,wordsInLevel);
        }else{
            ArrayList<Word> s = _spellingWords.get(levelKey);
            boolean newWord = true;
            for(Word w : s){
                if(w.toString().equals(word)){
                    newWord = false;
                }
            }
            if(newWord) {//if it is a new word then add it to the spelling list. Else skip.
                _spellingWords.get(levelKey).add(new Word(word));
            }
        }
    }

    /**
     * Adds a word from existing spelling word list in the database in a given level to
     * the failed word list. If word is already in failed list then it wont add it again.
     * @param word, level
     */
    public void addFailedWord(String word, String level) {
        ArrayList<Word> levelWords = _spellingWords.get(level);
        for(int i=0;i<levelWords.size();i++){
            if (levelWords.get(i).toString().equals(word)){ //find index of the failed word in spelling list
                if(_failedWords.containsKey(level)){
                    if( _failedWords.get(level).contains(levelWords.get(i)) ){ //if word is already in failed list
                        return;
                    }
                    _failedWords.get(level).add(levelWords.get(i));
                }else{
                    ArrayList<Word> failedLevelWords = new ArrayList<>();
                    failedLevelWords.add(levelWords.get(i));
                    _failedWords.put(level,failedLevelWords);
                }
                break;
            }
        }
    }

    /**
     * Removes a word from the failed list.
     * @param word, level
     */
    public void removeFailedWord(String word,String level) {
        ArrayList<Word> levelFailed = _failedWords.get(level);
        for(int i=0;i<levelFailed.size();i++){
            if(levelFailed.get(i).toString().equals(word)){ // find index of failed word
                levelFailed.remove(i);
                break;
            }
        }
    }

    /**
     * Increments the Word object's mastered count field in the database.
     * @param levelkey
     * @param word
     */
    public void incrementMastered(String levelkey, String word){
        ArrayList<Word> levelwords = _spellingWords.get(levelkey);
        for(Word w : levelwords){
            if(w.toString().toLowerCase().equals(word)){
                w.set_mastered(w.get_mastered()+1);
            }
        }
    }

    /**
     * Increments the Word object's faulted count field in the database.
     * @param levelkey
     * @param word
     */
    public void incrementFaulted(String levelkey, String word){
        ArrayList<Word> levelwords = _spellingWords.get(levelkey);
        for(Word w : levelwords){
            if(w.toString().toLowerCase().equals(word)){
                w.set_faulted(w.get_faulted()+1);
            }
        }
    }

    /**
     * Increments the Word object's failed count field in the database.
     * @param levelkey
     * @param word
     */
    public void incrementFailed(String levelkey, String word){
        ArrayList<Word> levelwords = _spellingWords.get(levelkey);
        for(Word w : levelwords){
            if(w.toString().toLowerCase().equals(word)){
                w.set_failed(w.get_failed()+1);
            }
        }
    }

    /**
     * getNormalQuiz returns 10 random words from a given level as a String array.
     * @param levelKey
     * @return String[] words from level x
     */
    public String[] getNormalQuiz(String levelKey){
        ArrayList<Word> levelWords = _spellingWords.get(levelKey);
        Collections.shuffle(levelWords);
        String[] testList = new String[10];
        for(int i=0;i<10;i++){
            testList[i] = levelWords.get(i).toString();
        }
        return testList;
    }

    /**
     * getReview Quiz returns 10 or less words from a given level as a String array.
     * @param levelKey
     * @return String[] words from level x
     */
    public String[] getReviewQuiz(String levelKey) {
        ArrayList<Word> levelWords = _failedWords.get(levelKey);
        if ( levelWords==null ){
            return new String[0];
        }
        Collections.shuffle(levelWords);
        String[] testList;
        if (levelWords.size()<10){
            testList = new String[levelWords.size()];
            for(int i=0;i<levelWords.size();i++){
                testList[i] = levelWords.get(i).toString();
            }
        }else{
            testList = new String[10];
            for(int i=0;i<10;i++){
                testList[i] = levelWords.get(i).toString();
            }
        }
        return testList;
    }

    /**
     * Updates the score for the given level in the database.
     * @param score
     * @param numberOfWords
     * @param level
     */
    public void addScore(int score, int numberOfWords, String level){
        if( _scoreForLevel.containsKey(level) ){
            _scoreForLevel.put(level, _scoreForLevel.get(level) + score );
        }else{ //if the level has not been attempted yet
            _scoreForLevel.put(level, score );
        }
        if( _attemptsForLevel.containsKey(level) ){
            _attemptsForLevel.put(level, _attemptsForLevel.get(level) + numberOfWords );
        }else{ //if the level has not been attempted yet
            _attemptsForLevel.put(level, numberOfWords );
        }
    }

    /**
     * Returns the accuracy score of the specified level. Returns 0.0 if level has not been
     * attempted yet
     * @param level
     * @return
     */
    public double getAccuracyScore(String level){
        if( _scoreForLevel.containsKey(level) || _attemptsForLevel.containsKey(level) ){
            return ( (double) _scoreForLevel.get(level)/(_attemptsForLevel.get(level)*4) )*100;
        }else{
            return 0.0;
        }
    }

    /**
     * Removes all words from failed list and resets accuracy level for each level.
     * Also for each word, mastered, faulted and faiiled is rest to 0.
     */
    public void clearStats(){
        _scoreForLevel = new HashMap<>();
        _attemptsForLevel = new HashMap<>();
        _failedWords = new HashMap<>();
        for( String level : _spellingWords.keySet()){ // loop through each level
            ArrayList<Word> currentLevel = _spellingWords.get(level);
            for(Word w : currentLevel){
                w.clear();
            }
        }
    }

    /**
     * Returns an ObservableList of all the elements of the specified level that have been attempted
     * @param levelKey
     * @return
     */
    public ObservableList getLevel(String levelKey) {
        ObservableList<Word> level = FXCollections.observableArrayList();
        ArrayList<Word> levelWords = _spellingWords.get(levelKey);
        for(Word w : levelWords){
            if(w.attempted()) {
                level.add(w);
            }
        }
        return level;
    }

    /**
     * Returns an ArrayList of strings of the levels in the spelling database in order from lowest level to highest.
     * @return
     */
    public ArrayList<String> getAllLevels() {
        ArrayList<String> levels = new ArrayList<>();
        levels.addAll(_spellingWords.keySet());
        Collections.sort(levels, new LevelComparator());
        return levels;
    }

    /**
     * Sorts the spelling levels from Level 1 to Level 11.
     */
    private class LevelComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            String[] level1 = o1.split(" ");
            String[] level2 = o2.split(" ");
            int n1 = Integer.parseInt(level1[1]);
            int n2 = Integer.parseInt(level2[1]);
            if(n1<n2){
                return -1;
            }else if(n1>n2){
                return 1;
            }else{
                return 0;
            }
        }
    }

    public String get_voice() {
        return _voice;
    }

    public void set_voice(String _voice) {
        this._voice = _voice;
    }

    public String get_voiceSpeed() {return _voiceSpeed;}

    public void set_voiceSpeed(String _voiceSpeed) { this._voiceSpeed = _voiceSpeed;}

    /**
     * Debugging purposes only
     */
    public void printDatabase(){
        for (String key : _spellingWords.keySet()) {
            System.out.println(key);
            System.out.println(Arrays.toString(_spellingWords.get(key).toArray()));
        }
    }


}
