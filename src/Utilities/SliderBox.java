package Utilities;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.text.NumberFormat;

public class SliderBox extends HBox {
    private Slider slider;
    private TextField field;

    public SliderBox(String name, int defaultValue){
        this(name, defaultValue, 100);
    }
    public SliderBox(String name, int defaultValue, int max){
        this(name, (double)defaultValue, (double)max);
    }
    public SliderBox(String name, double defaultValue, double max){
        this(name, defaultValue, max, 1, 1);
    }
    public SliderBox(String name, double defaultValue, double max, double min){
        this(name, defaultValue, max, min, 1);
    }
    public SliderBox(String name, double defaultValue, double max, double min, double tick){
        this(name, defaultValue, max, min, tick, true);
    }
    public SliderBox(String name, double defaultValue, double max, double min, double tick, boolean disabled){
        super();
        Text sliderName = new Text(name);

        sliderName.setWrappingWidth(100);
        slider = new Slider(min, max, tick);
        slider.setOrientation(Orientation.HORIZONTAL);
        slider.setValue(defaultValue);
        slider.setPrefWidth(400);
        slider.valueProperty().addListener((obs, oldval, newVal) ->
                slider.setValue(tick < 1 ? newVal.doubleValue() : newVal.intValue()));
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        field = new TextField();
        field.setAlignment(Pos.CENTER);
        field.setDisable(disabled);
        field.setOnKeyReleased(event -> { field.setText(field.getText().replaceAll("[^0-9]", ""));});
        field.textProperty().bindBidirectional(slider.valueProperty(), NumberFormat.getNumberInstance());
        this.getChildren().addAll(sliderName, slider, field);
    }


    public double getValue(){
        return slider.getValue();
    }
    public int getIntValue(){
        return (int)slider.getValue();
    }
    public void setValue(int value){
        slider.setValue(value);
    }
    public Slider getSlider() {
        return slider;
    }
    public TextField getField(){
        return field;
    }

}
