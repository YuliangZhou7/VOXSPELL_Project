package data;


import java.io.Serializable;

/**
 * The Word object is a serializable class which stores the spelling of a word along with the number
 * of times it has been Mastered, Faulted, and Failed.
 * @author Yuliang Zhou yzho746 7/09/2016
 *
 */
public class Word implements Serializable{

    private static final long serialVersionUID = 1L;

    private String _word;
    private int _mastered;
    private int _faulted;
    private int _failed;

    public Word(String word){
        _word = word;
        _mastered = 0;
        _faulted = 0;
        _failed = 0;
    }

    public String get_word() {
        return _word;
    }

    public void set_word(String _word) {
        this._word = _word;
    }

    public int get_mastered() {
        return _mastered;
    }

    public void set_mastered(int _mastered) {
        this._mastered = _mastered;
    }

    public int get_faulted() {
        return _faulted;
    }

    public void set_faulted(int _faulted) {
        this._faulted = _faulted;
    }

    public int get_failed() {
        return _failed;
    }

    public void set_failed(int _failed) {
        this._failed = _failed;
    }

    /**
     * Resets the Mastered, Faulted, and Failed count for the word
     */
    public void clear(){
        _mastered = 0;
        _faulted = 0;
        _failed = 0;
    }

    /**
     * Returns true if the word has been attempted, false otherwise.
     * @return
     */
    public boolean attempted(){
        boolean attempted = false;
        if( (_mastered != 0) || (_faulted != 0) || (_failed != 0) ){
            attempted = true;
        }
        return attempted;
    }

    /**
     * Returns the name of the word
     */
    @Override
    public String toString() {
        return _word;
    }



}

