/*  PDFBookGen - a JavaFX application 'framework' that uses Maven, FXML and CSS.
 *
 *  Copyright 2022 Philip Lockett.
 *
 *  This file is part of PDFBookGen.
 *
 *  PDFBookGen is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  PDFBookGen is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PDFBookGen.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Model is the class that captures the dynamic shared data plus some 
 * supporting constants and provides access via getters and setters.
 */
package phillockett65.PDFBookGen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SpinnerValueFactory;

public class Model {

    private final static String DATAFILE = "Settings.dat";


    /************************************************************************
     * General support code.
     */

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

        setFirstCheck(true);
        setSecondCheck(false);
        setThirdCheck(false);

        setSecondRadio();

        setInteger(10);
        setDouble(1.0);
        setDayIndex(1);

        setPageSizeIndex(7);
        setSigSizeIndex(0);
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

        data.setPageSizeIndex(getPageSizeIndex());
        data.setSigSizeIndex(getSigSizeIndex());

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

        setPageSizeIndex(data.getPageSizeIndex());
        setSigSizeIndex(data.getSigSizeIndex());
    
        setInteger(data.getMyInteger());
        setDouble(data.getMyDouble());
        setDayIndex(data.getDay());
    
        return true;
    }



    /************************************************************************
     * Support code for "File Names" panel.
     */

    private String myText;

    public void setMyText(String text) { myText = text; }
    public String getMyText() { return myText; }

    /**
     * Initialize "File Names" panel.
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

    private int pageSizeIndex;
    private ObservableList<String> pageSizeList = FXCollections.observableArrayList();
    
    private int sigSizeIndex;
    private ObservableList<String> sigSizeList = FXCollections.observableArrayList();

    public ObservableList<String> getPageSizeList() { return pageSizeList; }
    public void setPageSize(String value) { pageSizeIndex = pageSizeList.indexOf(value); }
    public String getPageSize() { return pageSizeList.get(pageSizeIndex); }
    public void setPageSizeIndex(int value) { pageSizeIndex = value; }
    public int getPageSizeIndex() { return pageSizeIndex; }

    public ObservableList<String> getSigSizeList() { return sigSizeList; }
    public void setSigSize(String value) { sigSizeIndex = sigSizeList.indexOf(value); }
    public String getSigSize() { return sigSizeList.get(sigSizeIndex); }
    public void setSigSizeIndex(int value) { sigSizeIndex = value; }
    public int getSigSizeIndex() { return sigSizeIndex; }
    public int getSigSheetCount() { return getSigSizeIndex() + 1; }
    public int getSigPageCount() { return getSigSheetCount() * 4; }


    /**
     * Initialize "Selections" panel.
     */
    private void initializeSelections() {

        pageSizeList.addAll("A0", "A1", "A2", "A3", "A4", "A5", "A6", "Letter", "Legal");
        sigSizeList.addAll("1 Sheet", "2 Sheets", "3 Sheets", "4 Sheets", "5 Sheets", "6 Sheets");
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
