package gui;

import data.SpellingDatabase;
import data.Word;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The settings screen contains a piechart and table view of the user's statistics. Clicking on
 * an item in the spelling list choicebox changes the level choicebox to the levels from that list.
 * Clicking an item from the level choicebox updates the table view and refreshes the piechart.
 * Author: Yuliang Zhou 7/09/2016
 */
public class StatsScreenController implements ControlledScreen{

    private MasterController _myParentController;

    private SpellingDatabase _database;

    @FXML
    private Label _accuracyForLevel;
    @FXML
    private ChoiceBox<String> _levelSelection;
    @FXML
    private ChoiceBox<String> _spellingLists;
    @FXML
    private TableView<Word> _table;
    @FXML
    private TableColumn<Word, String> _wordColumn;
    @FXML
    private TableColumn<Word, Integer> _masteredColumn;
    @FXML
    private TableColumn<Word, Integer> _faultedColumn;
    @FXML
    private TableColumn<Word, Integer> _failedColumn;
    @FXML
    private PieChart _piechart;

    @Override
    public void setScreenParent(MasterController screenParent) {
        _myParentController = screenParent;
    }

    @Override
    public void setup() {
        //set up spelling list combobox selection
        _spellingLists.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue!=null) {
                    _myParentController.set_currentSpellingList(newValue);
                    updateScreen();
                }
            }
        });

        //setup level combobox selection
        _levelSelection.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(newValue!=null) {
                    updateCharts();
                }
            }
        });

        //setup table columns
        _wordColumn.setCellValueFactory(new PropertyValueFactory<>("_word"));
        _masteredColumn.setCellValueFactory(new PropertyValueFactory<>("_mastered"));
        _faultedColumn.setCellValueFactory(new PropertyValueFactory<>("_faulted"));
        _failedColumn.setCellValueFactory(new PropertyValueFactory<>("_failed"));

    }

    @Override
    public void displayScreen() {
        //update spelling list model
        _database = _myParentController.getCurrentSpellilngModel();

        //get all spelling lists
        List<String> list = _myParentController.getSpellingListKeys();
        ObservableList obList = FXCollections.observableList(list);
        _spellingLists.setItems(obList);
        _spellingLists.setValue(_myParentController.get_currentSpellingList());

        updateScreen();
    }

    public void backButtonPressed(){
        _myParentController.buttonClickSound();
        _myParentController.setScreen(Main.Screen.TITLE);
    }

    public void updateScreen(){
        _database = _myParentController.getCurrentSpellilngModel();

        //update level list
        _levelSelection.getItems().clear();
        ArrayList < String > levels = _database.getAllLevels();
        for( String levelNumber : levels ) {
            _levelSelection.getItems().add(levelNumber);
        }
        _levelSelection.setValue(levels.get(0));

        updateCharts();
    }

    public void updateCharts(){
        //get all attempted words in level
        _table.setItems(_database.getLevel(_levelSelection.getValue()));

        //set accuracy rating
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        Number n =_database.getAccuracyScore(_levelSelection.getValue());
        _accuracyForLevel.setText("Accuracy: " + df.format(n.doubleValue()) +"%");

        //update pie chart
        ObservableList<Word> levels = _database.getLevel(_levelSelection.getValue());
        int masteredCount = 0;
        int faultedCount = 0;
        int failedCount = 0;
        for( Word currentWord : levels ) {
            masteredCount += currentWord.get_mastered();
            faultedCount += currentWord.get_faulted();
            failedCount += currentWord.get_failed();
        }
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
        new PieChart.Data("Mastered", masteredCount),
                new PieChart.Data("Faulted", faultedCount),
                new PieChart.Data("Failed", failedCount)
         );
        if(masteredCount==0 && faultedCount==0 && failedCount==0) {
            _piechart.setVisible(false);
        }else{
            _piechart.setVisible(true);
            _piechart.setData(pieChartData);
            _piechart.setLegendVisible(false);
            applyColors( pieChartData,"green", "orange", "red" );
        }
    }

    private void applyColors(ObservableList<PieChart.Data> pieChartData,String... pieColors) {
        int i = 0;
        for (PieChart.Data data : pieChartData) {
            Node n = data.getNode();
            n.setStyle( "-fx-pie-color: " + pieColors[i % pieColors.length] + ";" );
            i++;
        }
    }

}
