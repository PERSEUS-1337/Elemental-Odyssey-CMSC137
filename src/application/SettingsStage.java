package application;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;

public class SettingsStage {
    public static Double masterVolume = MainGUIController.DEFAULT_MASTER_VOLUME;
    public static Double musicVolume = MainGUIController.DEFAULT_MASTER_VOLUME;
    
    public static final Integer WINDOW_WIDTH = 600;
    public static final Integer WINDOW_HEIGHT = 400;

    @FXML
    private Slider sliderMaster;

    @FXML
    private Slider sliderMusic;

    @FXML
    private Slider sliderSound;

    // method to change the volume of the master volume (master = any music + sounds playing in the game)
    public void changeMasterVolume() {
        // changing the music first

        // get the value of the slider
        SettingsStage.masterVolume = sliderMaster.getValue();
        // truncate the value to 2 decimal places
        SettingsStage.masterVolume  = Math.round(SettingsStage.masterVolume  * 100.0) / 100.0;
        System.out.println("Master Volume: " + SettingsStage.masterVolume );
        // change the volume to percent
        SettingsStage.masterVolume  = SettingsStage.masterVolume  / 100;
        // change the volume of the media player from the MainGUIController
        MainGUIController.changeMusicVolume(SettingsStage.masterVolume);

        // since we change the music using the master volume, we update the music volume slider
        SettingsStage.musicVolume = SettingsStage.masterVolume;
        this.sliderMusic.setValue(SettingsStage.musicVolume * 100);

        // TODO: change the volume of the sounds (i.e., sprite sounds, etc.)
    }

    // method to change the volume of the music
    public void changeMusicVolume() {
        // get the value of the slider
        SettingsStage.musicVolume = sliderMusic.getValue();
        // truncate the value to 2 decimal places
        SettingsStage.musicVolume = Math.round(SettingsStage.musicVolume * 100.0) / 100.0;
        System.out.println("Music Volume: " + SettingsStage.musicVolume);
        // change the volume to percent
        SettingsStage.musicVolume = SettingsStage.musicVolume / 100;
        // change the volume of the media player from the MainGUIController
        MainGUIController.changeMusicVolume(SettingsStage.musicVolume);

        // if the music volume is greater than the master volume, we also update the master volume slider
        if (SettingsStage.musicVolume > SettingsStage.masterVolume) {
            SettingsStage.masterVolume = SettingsStage.musicVolume;
            this.sliderMaster.setValue(SettingsStage.masterVolume * 100);
        }
    }

}
