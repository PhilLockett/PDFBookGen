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
import javafx.stage.Stage;

public class Model {

    private final static String DATAFILE = "Settings.dat";

    private Stage stage;


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
    public void init(Stage primaryStage) {
        // System.out.println("Model init.");
        
        stage = primaryStage;
    }

    public Stage getStage() { return stage; }

    /**
     * Set all attributes to the default values.
     */
    public void defaultSettings() {
        setSourceDocument(".");
        setOutputFileName("Enter filename");

        setRotateCheck(true);
        setPadCheck(false);

        setPageCount(100);
        setFirstPage(1);
        setLastPage(1);

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

        data.setOutputFileName(getOutputFileName());

        data.setRotateCheck(isRotateCheck());
        data.setPadCheck(isPadCheck());

        data.setPageSizeIndex(getPageSizeIndex());
        data.setSigSizeIndex(getSigSizeIndex());

        data.setFirstPage(getFirstPage());
        data.setLastPage(getLastPage());
    
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

        setSourceDocument(data.getSourceDocument());
        setOutputFileName(data.getOutputFileName());

        setRotateCheck(data.isRotateCheck());
        setPadCheck(data.isPadCheck());

        setPageSizeIndex(data.getPageSizeIndex());
        setSigSizeIndex(data.getSigSizeIndex());
    
        setFirstPage(data.getFirstPage());
        setLastPage(data.getLastPage());
    
        return true;
    }



    /************************************************************************
     * Support code for "File Names" panel.
     */

    private String sourceDocument;
    public void setSourceDocument(String text) { sourceDocument = text; }
    public String getSourceDocument() { return sourceDocument; }

    private String outputFileName;
    public void setOutputFileName(String text) { outputFileName = text; }
    public String getOutputFileName() { return outputFileName; }

    /**
     * Initialize "File Names" panel.
     */
    private void initializeTextBoxes() {
    }



    /************************************************************************
     * Support code for "Check Boxes and Radio Buttons" panel.
     */

    private boolean rotateCheck;
    public void setRotateCheck(boolean state) { rotateCheck = state; }
    public boolean isRotateCheck() { return rotateCheck; }
    
    private boolean padCheck;
    public void setPadCheck(boolean state) { padCheck = state; }
    public boolean isPadCheck() { return padCheck; }


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
    
    public ObservableList<String> getPageSizeList() { return pageSizeList; }
    public void setPageSize(String value) { pageSizeIndex = pageSizeList.indexOf(value); }
    public String getPageSize() { return pageSizeList.get(pageSizeIndex); }
    public void setPageSizeIndex(int value) { pageSizeIndex = value; }
    public int getPageSizeIndex() { return pageSizeIndex; }

    private int sigSizeIndex;
    private ObservableList<String> sigSizeList = FXCollections.observableArrayList();

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

    private int pageCount = 50;
    public int getPageCount() { return pageCount; }
    public void setPageCount(int value) {
        pageCount = value;
        firstPageSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, value, 1);
        lastPageSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, value, value);
    }

    private SpinnerValueFactory<Integer> firstPageSVF;
    public SpinnerValueFactory<Integer> getFirstPageSVF() { return firstPageSVF; }
    public int getFirstPage() { return firstPageSVF.getValue(); }
    public void setFirstPage(int value) { firstPageSVF.setValue(value); setLastPageRange(value); }

    private void setFirstPageRange(int value) {
        int current = getFirstPage();
        if (current > value)
            current = value;
        firstPageSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, value, current);
    }

    private SpinnerValueFactory<Integer>  lastPageSVF;
    public SpinnerValueFactory<Integer> getLastPageSVF() { return lastPageSVF; }
    public int getLastPage() { return lastPageSVF.getValue(); }
    public void setLastPage(int value) { lastPageSVF.setValue(value); setFirstPageRange(value); }
    public int getTotalPageCount() { return getLastPage()-getFirstPage()+1; }

    private void setLastPageRange(int value) {
        int current = getLastPage();
        if (current < value)
            current = value;
        lastPageSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(value, getPageCount(), current);
    }

    /**
     * Initialize "Spinners" panel.
     */
    private void initializeSpinners() {
        firstPageSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        lastPageSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
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
