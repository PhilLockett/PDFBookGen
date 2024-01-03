/*  BaseFXML - a JavaFX application 'framework' that uses Maven, FXML and CSS.
 *
 *  Copyright 2022 Philip Lockett.
 *
 *  This file is part of BaseFXML.
 *
 *  BaseFXML is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  BaseFXML is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with BaseFXML.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Model is the class that captures the dynamic shared data plus some 
 * supporting constants and provides access via getters and setters.
 */
package phillockett65.BaseFXML;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;

public class Model {

    private final static String DATAFILE = "Settings.dat";


    /************************************************************************
     * General support code.
     */

    /**
     * Convert a real colour value (0.0 to 1.0) to an int (0 too 256).
     * @param value to convert.
     * @return converted value.
     */
    private int colourRealToInt(double value) {
        return (int)(value * 256);
    }

    /**
     * @return the file path of the settings data file.
     */
    public String getSettingsFile() {
        return DATAFILE;
    }



    /************************************************************************
     * Support code for the Initialization of the Model.
     */

    /**
     * Responsible for constructing the Model and any local objects. Called by 
     * the controller.
     */
    public Model() {
    }

    /**
     * Called by the controller after the constructor to initialise any 
     * objects after the controls have been initialised.
     */
    public void initialize() {
        // System.out.println("Model initialized.");

        initializeTextBoxes();
        initializeCheckBoxes();
        initializeSelections();
        initializeSpinners();
        initializeStatusLine();

        if (!readData())
            defaultSettings();
    }

    /**
     * Called by the controller after the stage has been set. Completes any 
     * initialization dependent on other components being initialized.
     */
    public void init() {
        // System.out.println("Model init.");
    }

    /**
     * Set all attributes to the default values.
     */
    public void defaultSettings() {
        setMyText("Hello World");
        setMyBigText("");

        setFirstCheck(true);
        setSecondCheck(false);
        setThirdCheck(false);

        setSecondRadio();

        setInteger(10);
        setDouble(1.0);
        setDayIndex(1);

        setMonthIndex(6);
        setBestDayIndex(0);
        setMyColour(Color.RED);
    }



    /************************************************************************
     * Support code for state persistence.
     */

    /**
     * Instantiate a DataStore, populate it with data and save it to disc.
     * @return true if data successfully written to disc, false otherwise.
     */
    public boolean writeData() {
        DataStore data = new DataStore();

        data.setMyText(getMyText());
        data.setMyBigText(getMyBigText());

        data.setFirstCheck(isFirstCheck());
        data.setSecondCheck(isSecondCheck());
        data.setThirdCheck(isThirdCheck());

        if (isFirstRadio())
            data.setFirstRadio();
        else
        if (isSecondRadio())
            data.setSecondRadio();
        else
        if (isThirdRadio())
            data.setThirdRadio();

        data.setMonthIndex(getMonthIndex());
        data.setBestDayIndex(getBestDayIndex());
        data.setMyColour(getMyColour());

        data.setMyInteger(getInteger());
        data.setMyDouble(getDouble());
        data.setDay(getDayIndex());
    
        if (!DataStore.writeData(data, getSettingsFile())) {
            data.dump();

            return false;
        }

        return true;
    }

    /**
     * Get a DataStore populated with data previously stored to disc and update
     * the model with the data.
     * @return true if the model is successfully updated, false otherwise.
     */
    public boolean readData() {
        DataStore data = DataStore.readData(getSettingsFile());
        if (data == null)
            return false;

        setMyText(data.getMyText());
        setMyBigText(data.getMyBigText());

        setFirstCheck(data.getFirstCheck());
        setSecondCheck(data.getSecondCheck());
        setThirdCheck(data.getThirdCheck());

        if (data.isFirstRadio())
            setFirstRadio();
        else
        if (data.isSecondRadio())
            setSecondRadio();
        else
        if (data.isThirdRadio())
            setThirdRadio();

        setMonthIndex(data.getMonthIndex());
        setBestDayIndex(data.getBestDayIndex());
        setMyColour(data.getMyColour());
    
        setInteger(data.getMyInteger());
        setDouble(data.getMyDouble());
        setDayIndex(data.getDay());
    
        return true;
    }



    /************************************************************************
     * Support code for "Text Boxes" panel.
     */

    private String myText;
    private String myBigText;

    public void setMyText(String text) { myText = text; }
    public String getMyText() { return myText; }
    public void setMyBigText(String text) { myBigText = text; }
    public String getMyBigText() { return myBigText; }

    /**
     * Initialize "Text Boxes" panel.
     */
    private void initializeTextBoxes() {
    }



    /************************************************************************
     * Support code for "Check Boxes and Radio Buttons" panel.
     */

    private boolean firstCheck;
    private boolean secondCheck;
    private boolean thirdCheck;

    private enum RadioSelection { FIRST, SECOND, THIRD };

    private RadioSelection radioSelected;

    public void setFirstCheck(boolean state) { firstCheck = state; }
    public void setSecondCheck(boolean state) { secondCheck = state; }
    public void setThirdCheck(boolean state) { thirdCheck = state; }

    public boolean isFirstCheck() { return firstCheck; }
    public boolean isSecondCheck() { return secondCheck; }
    public boolean isThirdCheck() { return thirdCheck; }

    public void setFirstRadio() { radioSelected = RadioSelection.FIRST; }
    public void setSecondRadio() { radioSelected = RadioSelection.SECOND; }
    public void setThirdRadio() { radioSelected = RadioSelection.THIRD; }

    public boolean isFirstRadio() { return radioSelected == RadioSelection.FIRST; }
    public boolean isSecondRadio() { return radioSelected == RadioSelection.SECOND; }
    public boolean isThirdRadio() { return radioSelected == RadioSelection.THIRD; }

    /**
     * Initialize "Check Boxes and Radio Buttons" panel.
     */
    private void initializeCheckBoxes() {
    }



    /************************************************************************
     * Support code for "Selections" panel.
     */

    private int monthIndex;
    private ObservableList<String> monthList = FXCollections.observableArrayList();

    private int bestDayIndex;
    private ObservableList<String> bestDayList = FXCollections.observableArrayList();

    private Color myColour;

    public ObservableList<String> getMonthList() { return monthList; }
    public void setMonth(String value) { monthIndex = monthList.indexOf(value); }
    public String getMonth() { return monthList.get(monthIndex); }
    public void setMonthIndex(int value) { monthIndex = value; }
    public int getMonthIndex() { return monthIndex; }

    public ObservableList<String> getBestDayList() { return bestDayList; }
    public void setBestDay(String value) { bestDayIndex = bestDayList.indexOf(value); }
    public String getBestDay() { return bestDayList.get(bestDayIndex); }
    public void setBestDayIndex(int value) { bestDayIndex = value; }
    public int getBestDayIndex() { return bestDayIndex; }

    public Color getMyColour() { return myColour; }
    public void setMyColour(Color colour) { myColour = colour; }

    /**
     * @return myColour as a displayable RGB string.
     */
    public String getMyColourString() {
        return String.format("rgb(%d, %d, %d)",
                colourRealToInt(myColour.getRed()),
                colourRealToInt(myColour.getGreen()),
                colourRealToInt(myColour.getBlue()));
    }

    /**
     * Initialize "Selections" panel.
     */
    private void initializeSelections() {
        bestDayList.add("New Year");
        bestDayList.add("Good Friday");
        bestDayList.add("Easter Monday");
        bestDayList.add("Victoria Day");
        bestDayList.add("Canada Day");
        bestDayList.add("Civic Holiday");
        bestDayList.add("Labour Day");
        bestDayList.add("Thanksgiving Day");
        bestDayList.add("Remembrance Day");
        bestDayList.add("Christmas Day");
        bestDayList.add("Boxing Day");

        monthList.addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    }



    /************************************************************************
     * Support code for "Spinners" panel.
     */

    private SpinnerValueFactory<Integer> integerSVF;

    private SpinnerValueFactory<Double>  doubleSVF;

    private ObservableList<String> daysOfWeekList = FXCollections.observableArrayList();
    private ListSpinner day;

    public SpinnerValueFactory<Integer> getIntegerSVF() { return integerSVF; }
    public SpinnerValueFactory<Double> getDoubleSVF() { return doubleSVF; }
    public SpinnerValueFactory<String> getDaySpinnerSVF() { return day.getSVF(); }

    public int getInteger() { return integerSVF.getValue(); }
    public double getDouble() { return doubleSVF.getValue(); }
    public void setInteger(int value) { integerSVF.setValue(value); }
    public void setDouble(double value) { doubleSVF.setValue(value); }

    public String getDay() { return day.getCurrent(); }
    public int getDayIndex() { return day.getIndex(); }
    public void setDay(String value) { day.setCurrent(value); }
    public void setDayIndex(int value) { day.setIndex(value); }
    public void incrementDay(int step) { day.increment(step); }

    /**
     * Initialize "Spinners" panel.
     */
    private void initializeSpinners() {
        integerSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 10);
        doubleSVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, 20.0, 1.0, 0.2);

        daysOfWeekList.add("Monday");
        daysOfWeekList.add("Tuesday");
        daysOfWeekList.add("Wednesday");
        daysOfWeekList.add("Thursday");
        daysOfWeekList.add("Friday");
        daysOfWeekList.add("Saturday");
        daysOfWeekList.add("Sunday");

        day = new ListSpinner(daysOfWeekList);
    }



    /************************************************************************
     * Support code for "Status Line" panel.
     */

    /**
     * Initialize "Status Line" panel.
     */
    private void initializeStatusLine() {
    }


}
