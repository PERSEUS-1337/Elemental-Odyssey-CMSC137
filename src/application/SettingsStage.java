package application;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;

public class SettingsStage {
    private Double masterVolume = MainGUIController.DEFAULT_MASTER_VOLUME;
    private Double musicVolume = MainGUIController.DEFAULT_MASTER_VOLUME;
    
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
        this.masterVolume = sliderMaster.getValue();
        // truncate the value to 2 decimal places
        this.masterVolume  = Math.round(this.masterVolume  * 100.0) / 100.0;
        System.out.println("Master Volume: " + this.masterVolume );
        // change the volume to percent
        this.masterVolume  = this.masterVolume  / 100;
        // change the volume of the media player from the MainGUIController
        MainGUIController.changeMusicVolume(this.masterVolume);

        // since we change the music using the master volume, we update the music volume slider
        this.musicVolume = this.masterVolume;
        this.sliderMusic.setValue(this.musicVolume * 100);

        // TODO: change the volume of the sounds (i.e., sprite sounds, etc.)
    }

    // method to change the volume of the music
    public void changeMusicVolume() {
        // get the value of the slider
        this.musicVolume = sliderMusic.getValue();
        // truncate the value to 2 decimal places
        this.musicVolume = Math.round(this.musicVolume * 100.0) / 100.0;
        System.out.println("Music Volume: " + this.musicVolume);
        // change the volume to percent
        this.musicVolume = this.musicVolume / 100;
        // change the volume of the media player from the MainGUIController
        MainGUIController.changeMusicVolume(this.musicVolume);

        // if the music volume is greater than the master volume, we also update the master volume slider
        if (this.musicVolume > this.masterVolume) {
            this.masterVolume = this.musicVolume;
            this.sliderMaster.setValue(this.masterVolume * 100);
        }
    }

}
