package Infectionsimulation;

import Utilities.SliderBox;
import Utilities.UIElements;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Infectionsimulator extends UIElements {
    private Person.Status newStatus;
    private SimulationRenderer simulation;

    public static void main(String... args){
        launch(args);
    }

    @Override
    protected VBox startBox() {
        VBox box = new VBox();
        box.setPrefSize(700, 700);
        box.setAlignment(Pos.TOP_CENTER);
        SliderBox peopleSlider = new SliderBox("Anzahl Personen:", 500, 1000);
        SliderBox speedSlider = new SliderBox("Geschwindigkeit: ", 1, 10);
        SliderBox radiusSlider = new SliderBox("Infektionsradius: ", 3, 10);
        SliderBox infectionRateSlider = new SliderBox("Infekionswahrscheinlichkeit: ", 0.33, 1, 0, 0.1);
        SliderBox secondsSlider = new SliderBox("Sekunden bis Symptome erscheinen: ", 5, 10);
        SliderBox deathSlider = new SliderBox("Todesrate (in %): ", 5, 100, 0, 0.2);
        SliderBox symptomSlider = new SliderBox("Sekunden bis Person geheilt: ", 25, 100);
        Button startButton = new Button("Start");
        startButton.setOnAction(event -> {
            City city = new City(700, 700, speedSlider.getIntValue(), peopleSlider.getIntValue(), 3, radiusSlider.getIntValue(), infectionRateSlider.getValue(), secondsSlider.getIntValue(), deathSlider.getValue(), symptomSlider.getIntValue());
            openStage.setScene(new Scene(cityScene(city)));
            openStage.show();
        });
        box.getChildren().addAll(standardMenu(), peopleSlider, startButton, speedSlider, radiusSlider, infectionRateSlider, secondsSlider, deathSlider, symptomSlider);
        return box;
    }

    private VBox cityScene(City city){
        HBox box = new HBox();
        HBox simulationBox = new HBox();
        simulation = new SimulationRenderer(city, 700, 700);
        simulationBox.getChildren().addAll(simulation);
        // Start the simulation.
        simulation.start();
        simulation.setOnMousePressed(e -> {
            city.generatePerson((int)e.getSceneX(), (int)e.getSceneY() - 20, getNewStatus());
            simulation.getGraph().repaint();
        });
        box.getChildren().addAll(simulation, settingsBox(city));
        return new VBox(standardMenu(), box);
    }

    private VBox settingsBox(City city){
        SliderBox speedSlider = new SliderBox("Geschwindigkeit", city.getSpeed(), 10, 0);
        speedSlider.getSlider().valueProperty().addListener((ov, old_val, new_val) -> simulation.setSpeed((speedSlider.getIntValue())));
        SliderBox radiusSlider = new SliderBox("Infektionsradius: ", city.getInfectionRadius(), 10);
        radiusSlider.getSlider().valueProperty().addListener((ov, old_val, new_val) -> city.setInfectionRadius(radiusSlider.getIntValue()));
        SliderBox infectionRateSlider = new SliderBox("Infekionswahrscheinlichkeit: ", city.getInfectionChance(), 1, 0, 0.1, false);
        infectionRateSlider.getSlider().valueProperty().addListener((ov, old_val, new_val) -> city.setInfectionChance(infectionRateSlider.getValue()));
        SliderBox secondsSlider = new SliderBox("Sekunden bis Symptome erscheinen: ", city.getSecondsToSymptom(), 10);
        secondsSlider.getSlider().valueProperty().addListener((ov, old_val, new_val) -> city.setSecondsToSymptom(secondsSlider.getIntValue()));
        SliderBox deathSlider = new SliderBox("Todesrate (in %): ", city.getDeathRate(), 100, 0, 0.2, false);
        deathSlider.getSlider().valueProperty().addListener((ov, old_val, new_val) -> city.setDeathRate(deathSlider.getValue()));
        SliderBox symptomSlider = new SliderBox("Sekunden bis Person geheilt: ", city.getSectondsToRecovered(), 100);
        symptomSlider.getSlider().valueProperty().addListener((ov, old_val, new_val) -> city.setSectondsToRecovered(symptomSlider.getIntValue()));
        Text people = new Text("Anzahl Personen: ");
        TextField peopleno = new TextField(city.getPeople().size() + "");
        simulation.setOnMouseClicked(e -> peopleno.setText(city.getPeople().size() + ""));
        peopleno.setDisable(true);
        ArrayList<String> list = new ArrayList<>();
        for(Person.Status s: Person.Status.values()) list.add(s.getName());
        ChoiceBox<String> statusChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(list));
        statusChoiceBox.setValue(statusChoiceBox.getItems().get(0));
        statusChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->
                setNewStatus(Person.Status.values()[(int) newValue]));

        return new VBox(new HBox(people, peopleno, statusChoiceBox), speedSlider, radiusSlider, infectionRateSlider, secondsSlider, deathSlider, symptomSlider, simulation.getGraph());
    }

    private Person.Status getNewStatus(){ return newStatus == null ? Person.Status.HEALTHY: newStatus; }
    public void setNewStatus(Person.Status newStatus) { this.newStatus = newStatus; }

    @Override protected String getTitle() { return "Infektionssimulation"; }
}
