package data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Depreciated class. Replaced by QuizController
 * Created by Yuliang on 14/09/2016.
 */
public class SpellingLogic {

    private enum Status{FIRSTATTEMPT,SECONDATTEMPT};

    private SpellingDatabase _database;

    private StringProperty userAttempt;

    private String _currentLevel;
    private String[] _wordList;
    private int _position;
    private Status _status;

    public SpellingLogic(SpellingDatabase database,String levelKey){
        _database = database;
        _currentLevel = levelKey;
        _wordList = _database.getNormalQuiz(levelKey);
        _position = 0;
        _status = Status.FIRSTATTEMPT;
        userAttempt = new SimpleStringProperty(this,"userAttempt","");
        userAttempt.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                checkUserAttempt();
            }
        });
    }

    public void startTest() {
        read("Please spell: " + _wordList[_position]);
    }

    /**
     * @return
     */
    private void checkUserAttempt(){
        if (_status == Status.FIRSTATTEMPT) {
            if (_wordList[_position].toLowerCase().equals(userAttempt.getValue().toLowerCase())) {
                _database.incrementMastered(_currentLevel, _wordList[_position]);
                read("Correct!");
                _position++;
                if( _position>_wordList.length-1 ){
                    return;
                }
                read("Please spell: " + _wordList[_position]);
            } else {
                read("Incorrect. Please try again: " + _wordList[_position]);
                _status = Status.SECONDATTEMPT;
            }
        } else {//second attempted. Faulted.
            _status = Status.FIRSTATTEMPT;
            if (_wordList[_position].toLowerCase().equals(userAttempt.getValue().toLowerCase())) {
                _database.incrementFaulted(_currentLevel, _wordList[_position]);
                read("Correct!");
                _position++;
                if( _position>_wordList.length-1 ){
                    return;
                }
                read("Please spell: " + _wordList[_position]);
            } else {
                _database.incrementFailed(_currentLevel, _wordList[_position]);
                read("Incorrect");
                _position++;
                if( _position>_wordList.length-1 ){
                    return;
                }
                read("Please spell: " + _wordList[_position]);
            }
        }
        System.out.println("You entered: " + userAttempt);
    }

    //stub method
    public void read(String phrase){
        //System.out.println("FESTIVAL: " + phrase);
    }

    public String getUserAttempt() {
        return userAttempt.get();
    }

    public void setUserAttempt(String userAttempt) {
        this.userAttempt.set(userAttempt);
    }

    public StringProperty userAttemptProperty() {
        return userAttempt;
    }


}
