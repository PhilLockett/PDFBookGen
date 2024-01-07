/*  PDFBookGen - a simple application to generate a booklet from of a PDF.
 *
 *  Copyright 2024 Philip Lockett.
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

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


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
     * 
     * @param mainController used to call the centralized controller.
     */
    public void init(Stage primaryStage) {
        // System.out.println("PrimaryController init.");
        model.init(primaryStage);
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
        sourceDocumentTextField.setText(model.getSourceFilePath());
        generateButton.setDisable(!model.isSourceFilePath());

        outputFileNameTextField.setText(model.getOutputFileName());

        syncFirstPageSpinner();
        syncLastPageSpinner();

        rotateCheckBox.setSelected(model.isRotateCheck());

        paperSizeChoiceBox.setValue(model.getPaperSize());
        sigSizeChoiceBox.setValue(model.getSigSize());
        setPageCountMessage();
    }



    /************************************************************************
     * Support code for "File Names" panel.
     */

    @FXML
    private Button browseButton;


    /**
     * Use a FileChooser dialogue to select the source PDF file.
     * @return true if a file is selected, false otherwise.
     */
    private boolean openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Script File");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        if (model.isSourceFilePath()) {
            File current = new File(model.getSourceFilePath());
            if (current.exists()) {
                fileChooser.setInitialDirectory(new File(current.getParent()));
                fileChooser.setInitialFileName(current.getName());
            }
        }

        File file = fileChooser.showOpenDialog(model.getStage());
        if (file != null) {
            model.setSourceFilePath(file.getAbsolutePath());
            syncUI();

            return true;
        }

        return false;
    }

    @FXML
    void browseButtonActionPerformed(ActionEvent event) {
        openFile();
        setStatusMessage("Loaded file: " + model.getSourceFilePath());
    }

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
        sourceDocumentTextField.setTooltip(new Tooltip("Source PDF document"));
        outputFileNameTextField.setTooltip(new Tooltip("Name of generated output file, .pdf will be added automatically"));
        browseButton.setTooltip(new Tooltip("Select source PDF document"));
    }



    /************************************************************************
     * Support code for "Check Boxes and Radio Buttons" panel.
     */

    @FXML
    private CheckBox rotateCheckBox;

    @FXML
    void rotateCheckBoxActionPerformed(ActionEvent event) {
        model.setRotateCheck(rotateCheckBox.isSelected());
        final String state = model.isRotateCheck() ? "selected." : "unselected.";
        setStatusMessage("Rotate check box is " + state);
    }


    /**
     * Initialize "Check Boxes and Radio Buttons" panel.
     */
    private void initializeCheckBoxes() {
        rotateCheckBox.setTooltip(new Tooltip("Rotate reverse side of sheet 180 degrees"));
    }



    /************************************************************************
     * Support code for "Selections" panel.
     */

    @FXML
    private ChoiceBox<String> paperSizeChoiceBox;

    @FXML
    private ChoiceBox<String> sigSizeChoiceBox;

    @FXML
    private Label sigLabel;

    @FXML
    private Label sigCountLabel;

    @FXML
    private Label lastSigBeginLabel;

    @FXML
    private Label lastSigCountLabel;

    @FXML
    private Label lastSigBlanksLabel;

    private void setPageCountMessage() {
        sigLabel.setText(String.valueOf(model.getSigPageCount()));
        sigCountLabel.setText(String.valueOf(model.getSigCount()));
        lastSigBeginLabel.setText(String.valueOf(model.getLastSigFirstPage()));
        lastSigCountLabel.setText(String.valueOf(model.getLastSigPageCount()));
        lastSigBlanksLabel.setText(String.valueOf(model.getLastSigBlankCount()));
    }

    /**
     * Initialize "Selections" panel.
     */
    private void initializeSelections() {
        paperSizeChoiceBox.setItems(model.getPaperSizeList());
        sigSizeChoiceBox.setItems(model.getSigSizeList());

        paperSizeChoiceBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
            model.setPaperSize(newValue);
        });

        sigSizeChoiceBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
            model.setSigSize(newValue);
            syncUI();
        });

        paperSizeChoiceBox.setTooltip(new Tooltip("Paper size of the generated PDF document"));
        sigSizeChoiceBox.setTooltip(new Tooltip("Number of sheets of paper in each signature"));
        sigLabel.setTooltip(new Tooltip("Number of pages from the source document in each signature"));
        sigCountLabel.setTooltip(new Tooltip("Number of signatures in generated document"));
        lastSigBeginLabel.setTooltip(new Tooltip("First page from the source document in the last signature"));
        lastSigCountLabel.setTooltip(new Tooltip("Number of pages from the source document in the last signature"));
        lastSigBlanksLabel.setTooltip(new Tooltip("Number of blank pages in the last signature"));

        setPageCountMessage();
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

    @FXML
    private Button generateButton;

    @FXML
    void generateButtonActionPerformed(ActionEvent event) {
        final boolean success = model.generate();
        if (success)
            setStatusMessage("Generated: " + model.getOutputFilePath());
        else
            setStatusMessage("Failed to generate: " + model.getOutputFilePath());
    }


    private void setTotalPageCountMessage() {
        countLabel.setText("Total Page Count: " + model.getOutputPageCount());
    }

    private void syncFirstPageSpinner() {
        firstPageSpinner.setValueFactory(model.getFirstPageSVF());
    }

    private void syncLastPageSpinner() {
        lastPageSpinner.setValueFactory(model.getLastPageSVF());
    }

    /**
     * Initialize "Page Range" panel.
     */
    private void initializeSpinners() {
        syncFirstPageSpinner();
        firstPageSpinner.getValueFactory().wrapAroundProperty().set(false);
        
        firstPageSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            // System.out.println("intSpinner.Listener(" + newValue + "))");
            model.setFirstPage(newValue);
            setTotalPageCountMessage();
            syncLastPageSpinner();
            syncUI();
        });

        syncLastPageSpinner();
        lastPageSpinner.getValueFactory().wrapAroundProperty().set(false);
        
        lastPageSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            // System.out.println("doubleSpinner.Listener(" + newValue + "))");
            model.setLastPage(newValue);
            setTotalPageCountMessage();
            syncFirstPageSpinner();
            syncUI();
        });
        
        firstPageSpinner.setTooltip(new Tooltip("First page of source document to include in the generated document"));
        lastPageSpinner.setTooltip(new Tooltip("Last page of source document to include in the generated document"));
        countLabel.setTooltip(new Tooltip("Number of pages from the source document that will be included in the generated document"));
        generateButton.setTooltip(new Tooltip("Generate the PDF document in booklet form"));

        setTotalPageCountMessage();
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
        statusLabel.setTooltip(new Tooltip("Current status"));
        clearDataButton.setTooltip(new Tooltip("Caution! This irreversible action will reset the form data to default values"));
    }

}