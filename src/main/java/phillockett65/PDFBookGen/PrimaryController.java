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
 * PrimaryController is the class that is responsible for centralizing control.
 * It is instantiated by the FXML loader creates the Model.
 */
package phillockett65.PDFBookGen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;


public class PrimaryController {

    private Model model;


    /************************************************************************
     * Support code for the Initialization of the Controller.
     */

    /**
     * Responsible for constructing the Model and any local objects. Called by 
     * the FXMLLoader().
     */
    public PrimaryController() {
        // System.out.println("PrimaryController constructed.");
        model = new Model();
    }

    /**
     * Called by the FXML mechanism to initialize the controller. Called after 
     * the constructor to initialise all the controls.
     */
    @FXML public void initialize() {
        // System.out.println("PrimaryController initialized.");
        model.initialize();

        initializeTextBoxes();
        initializeCheckBoxes();
        initializeSelections();
        initializeSpinners();
        initializeStatusLine();
    }

    /**
     * Called by Application after the stage has been set. Completes any 
     * initialization dependent on other components being initialized.
     */
    public void init() {
        // System.out.println("PrimaryController init.");
        model.init();
        syncUI();
        setStatusMessage("Ready.");
    }

    /**
     * Save the current state to disc, called by the application on shut down.
     */
    public void saveState() {
        model.writeData();
    }

    /**
     * Synchronise all controls with the model. This should be the last step 
     * in the initialisation.
     */
    public void syncUI() {
        sourceDocumentTextField.setText(model.getSourceDocument());
        outputFileNameTextField.setText(model.getOutputFileName());

        firstCheckBox.setSelected(model.isFirstCheck());
        secondCheckBox.setSelected(model.isSecondCheck());
        thirdCheckBox.setSelected(model.isThirdCheck());

        firstRadioButton.setSelected(model.isFirstRadio());
        secondRadioButton.setSelected(model.isSecondRadio());
        thirdRadioButton.setSelected(model.isThirdRadio());

        pageSizeChoiceBox.setValue(model.getPageSize());
        sigSizeChoiceBox.setValue(model.getSigSize());
    }



    /************************************************************************
     * Support code for "File Names" panel.
     */

    @FXML
    private TextField sourceDocumentTextField;

    @FXML
    void sourceDocumentTextFieldKeyTyped(KeyEvent event) {
        // System.out.println("sourceDocumentTextFieldKeyTyped() " + event.toString());
        model.setOutputFileName(sourceDocumentTextField.getText());
    }

    @FXML
    private TextField outputFileNameTextField;

    @FXML
    void outputFileNameTextFieldKeyTyped(KeyEvent event) {
        // System.out.println("outputFileNameTextFieldKeyTyped() " + event.toString());
        model.setOutputFileName(outputFileNameTextField.getText());
    }


    /**
     * Initialize "File Names" panel.
     */
    private void initializeTextBoxes() {
        sourceDocumentTextField.setTooltip(new Tooltip("Source document"));
        outputFileNameTextField.setTooltip(new Tooltip("Name of generated output file, .pdf will be added automatically"));
    }



    /************************************************************************
     * Support code for "Check Boxes and Radio Buttons" panel.
     */

    @FXML
    private CheckBox firstCheckBox;

    @FXML
    private CheckBox secondCheckBox;

    @FXML
    private CheckBox thirdCheckBox;

    @FXML
    private ToggleGroup myToggleGroup;

    @FXML
    private RadioButton firstRadioButton;

    @FXML
    private RadioButton secondRadioButton;

    @FXML
    private RadioButton thirdRadioButton;

    @FXML
    void firstCheckBoxActionPerformed(ActionEvent event) {
        model.setFirstCheck(firstCheckBox.isSelected());
        final String state = model.isFirstCheck() ? "selected." : "unselected.";
        setStatusMessage("First check box " + state);
    }

    @FXML
    void secondCheckBoxActionPerformed(ActionEvent event) {
        model.setSecondCheck(secondCheckBox.isSelected());
        final String state = model.isSecondCheck() ? "selected." : "unselected.";
        setStatusMessage("Second check box " + state);
    }

    @FXML
    void thirdCheckBoxActionPerformed(ActionEvent event) {
        model.setThirdCheck(thirdCheckBox.isSelected());
        final String state = model.isThirdCheck() ? "selected." : "unselected.";
        setStatusMessage("Third check box " + state);
    }

    @FXML
    void myRadioButtonActionPerformed(ActionEvent event) {
        if (firstRadioButton.isSelected()) {
            model.setFirstRadio();
            setStatusMessage("First radio button selected.");
        }
        else
        if (secondRadioButton.isSelected()) {
            model.setSecondRadio();
            setStatusMessage("Second radio button selected.");
        }
        else
        if (thirdRadioButton.isSelected()) {
            model.setThirdRadio();
            setStatusMessage("Third radio button selected.");
        }
    }

    /**
     * Initialize "Check Boxes and Radio Buttons" panel.
     */
    private void initializeCheckBoxes() {
        firstCheckBox.setTooltip(new Tooltip("First check box"));
        secondCheckBox.setTooltip(new Tooltip("Second check box"));
        thirdCheckBox.setTooltip(new Tooltip("Third check box"));

        firstRadioButton.setTooltip(new Tooltip("First radio button"));
        secondRadioButton.setTooltip(new Tooltip("Second radio button"));
        thirdRadioButton.setTooltip(new Tooltip("Third radio button"));
    }



    /************************************************************************
     * Support code for "Selections" panel.
     */

    @FXML
    private ChoiceBox<String> pageSizeChoiceBox;

    @FXML
    private ChoiceBox<String> sigSizeChoiceBox;

    @FXML
    private Label sigLabel;

    private void setPageCountString() {
        sigLabel.setText("(" + model.getSigPageCount() + " pages)");
    }

    /**
     * Initialize "Selections" panel.
     */
    private void initializeSelections() {
        pageSizeChoiceBox.setItems(model.getPageSizeList());
        sigSizeChoiceBox.setItems(model.getSigSizeList());

        pageSizeChoiceBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
            model.setPageSize(newValue);
        });

        sigSizeChoiceBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
            model.setSigSize(newValue);
            setPageCountString();
        });

        pageSizeChoiceBox.setTooltip(new Tooltip("Paper size of the generated PDF document"));
        sigSizeChoiceBox.setTooltip(new Tooltip("Number of sheets of paper in each Signature"));
        sigLabel.setTooltip(new Tooltip("Number of pages from the source document in each Signature"));

        setPageCountString();
    }



    /************************************************************************
     * Support code for "Page Range" panel.
     */

    @FXML
    private Spinner<Integer> firstPageSpinner;

    @FXML
    private Spinner<Integer> lastPageSpinner;

    @FXML
    private Label countLabel;


    private void setTotalPageCountString() {
        countLabel.setText(String.valueOf(model.getTotalPageCount()));
    }

    /**
     * Initialize "Page Range" panel.
     */
    private void initializeSpinners() {
        firstPageSpinner.setValueFactory(model.getFirstPageSVF());
        firstPageSpinner.getValueFactory().wrapAroundProperty().set(false);
        firstPageSpinner.setTooltip(new Tooltip("Select first page to include in the generated document"));

        firstPageSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            // System.out.println("intSpinner.Listener(" + newValue + "))");
            model.setFirstPage(newValue);
            setTotalPageCountString();
        });

        lastPageSpinner.setValueFactory(model.getLastPageSVF());
        lastPageSpinner.getValueFactory().wrapAroundProperty().set(false);
        lastPageSpinner.setTooltip(new Tooltip("Select last page to include in the generated document"));

        lastPageSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            // System.out.println("doubleSpinner.Listener(" + newValue + "))");
            model.setLastPage(newValue);
            setTotalPageCountString();
        });

        countLabel.setTooltip(new Tooltip("Number of pages from the source document to include in the generated document"));

        setTotalPageCountString();
    }



    /************************************************************************
     * Support code for "Status Line" panel.
     */

    @FXML
    private Label statusLabel;

    @FXML
    private Button clearDataButton;

    @FXML
    void clearDataButtonActionPerformed(ActionEvent event) {
        model.defaultSettings();
        syncUI();
        setStatusMessage("Data reset.");
    }

    private void setStatusMessage(String message) {
        statusLabel.setText(message);
    }

    /**
     * Initialize "Status Line" panel.
     */
    private void initializeStatusLine() {
    }

}