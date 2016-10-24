package gui;

/**
 *
 * Created by Yuliang Zhou 24/10/2016
 */
public class HelpScreenController implements ControlledScreen {

    private MasterController _myParentController;


    @Override
    public void setScreenParent(MasterController screenParent) {
        _myParentController = screenParent;
    }

    @Override
    public void setup() {

    }

    @Override
    public void displayScreen() {

    }

    /**
     * This method is called when the back button is pressed and Switches the scene to the TITLE screen.
     */
    public void backButtonPressed(){
        _myParentController.buttonClickSound();
        _myParentController.setScreen(Main.Screen.TITLE);
    }



}
