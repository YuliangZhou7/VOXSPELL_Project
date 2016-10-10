package gui;

/**
 * Created by Yuliang on 6/09/2016.
 * ControlledScreen is implemented from https://github.com/acaicedo/JFX-MultiScreen/blob/master/ScreensFramework/src/screensframework/ControlledScreen.java
 *
 */
public interface ControlledScreen {

    /**
     * Sets the parent screen controller, type MasterController, to this ControlledScreen
     * implementation.
     * @param screenParent
     */
    public void setScreenParent(MasterController screenParent);

    /**
     * This method is called after setScreenParent is called. Used for setting up the ControlledScreen
     * object's state.
     */
    public void setup();
}
