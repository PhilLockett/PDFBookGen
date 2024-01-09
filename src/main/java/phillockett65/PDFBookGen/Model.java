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
 * Model is the class that captures the dynamic shared data plus some 
 * supporting constants and provides access via getters and setters.
 */
package phillockett65.PDFBookGen;

import java.io.File;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

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
        BuildSignature();
    }

    public Stage getStage() { return stage; }

    /**
     * Set all attributes to the default values.
     */
    public void defaultSettings() {
        setSourceFilePath("");
        setOutputFileName("booklet");

        setRotateCheck(true);

        setPageCount(100);
        setFirstPage(1);
        setLastPage(1);

        setPaperSizeIndex(7);
        setSigSize(1);
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

        data.setSourceDocument(getSourceFilePath());
        data.setOutputFileName(getOutputFileName());

        data.setRotateCheck(isRotateCheck());

        data.setPaperSizeIndex(getPaperSizeIndex());
        data.setSigSize(getSigSize());

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

        setSourceFilePath(data.getSourceDocument());
        setOutputFileName(data.getOutputFileName());

        setRotateCheck(data.isRotateCheck());

        setPaperSizeIndex(data.getPaperSizeIndex());
        setSigSize(data.getSigSize());
    
        setFirstPage(data.getFirstPage());
        setLastPage(data.getLastPage());
    
        return true;
    }



    /************************************************************************
     * Support code for "File Names" panel.
     */

    private String sourceDocument;
    public void setSourceFilePath(String text) {
        sourceDocument = text;
        setPageCount(fetchPageCount());
    }

    public int fetchPageCount() {
        if (isSourceFilePath())
            return PDFBook.getPDFPageCount(sourceDocument);

        return 1;
    }

    public String getSourceFilePath() { return sourceDocument; }
    public boolean isSourceFilePath() { return !sourceDocument.isBlank(); }

    private String outputFileName;
    public void setOutputFileName(String text) { outputFileName = text; }
    public String getOutputFileName() { return outputFileName; }
    public String getOutputFilePath() {
        File current = new File(getSourceFilePath());

        return current.getParent() + "\\" + outputFileName + ".pdf";
    }

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
    

    /**
     * Initialize "Check Boxes and Radio Buttons" panel.
     */
    private void initializeCheckBoxes() {
    }



    /************************************************************************
     * Support code for Signature class.
     */

     /**
     * Class to encapsulate necessary calculations for changes to the selected 
     * first source page, last source page or the count of sheets in a 
     * signature. A source page is a page from the source document. The 
     * generated document has 2 source pages on each page. Pages are printed 
     * on both sides, so there are 4 source pages on each printed sheet.
     * 
     * Available values calculated are:
     *   o Number of source pages in the generated document
     *   o Number of source pages in a signature
     *   o Number of signatures that will be generated
     *   o Source page number that the last signature starts with
     *   o Number of source pages in the last signature
     *   o Number of blank pages in the last signature
     */
    private class Signature {
        public final int pageCount;
        public final int sigPageCount;
        public final int sigCount;
        public final int lastSigFirstPage;
        public final int lastSigPageCount;
        public final int lastSigBlankCount;

        public Signature(int sigSize, int firstPage, int lastPage)
        {
            final int pageDiff = lastPage - firstPage;
            pageCount = pageDiff + 1;
            sigPageCount = sigSize * 4;
            final int fullSigCount = pageDiff / sigPageCount;
            final int fullSigPageCount = fullSigCount * sigPageCount;
            sigCount = fullSigCount + 1;
            lastSigFirstPage = firstPage + fullSigPageCount;
            lastSigPageCount = pageCount - fullSigPageCount;
            lastSigBlankCount = sigPageCount - lastSigPageCount;
        }

    }
    
    private Signature signature;
    private void BuildSignature() {
        signature = new Signature(getSigSize(), getFirstPage(), getLastPage());
    }

    public int getOutputPageCount() { return signature.pageCount; }               // Number of pages in generated document.
    public int getSigPageCount() { return signature.sigPageCount; }               // Number of pages in a signature.
    public int getSigCount() { return signature.sigCount; }                       // Number of signatures that will be generated.
    public int getLastSigFirstPage() { return signature.lastSigFirstPage; }       // Last signature starts with that source page.
    public int getLastSigPageCount() { return signature.lastSigPageCount; }       // Count of source pages in last signature.
    public int getLastSigBlankCount() { return signature.lastSigBlankCount; }     // Blank pages in last signature.



    /************************************************************************
     * Support code for "Selections" panel.
     */

    private int paperSizeIndex;
    private ObservableList<String> paperSizeList = FXCollections.observableArrayList();
    
    public ObservableList<String> getPaperSizeList() { return paperSizeList; }
    public void setPaperSizeIndex(int value) { paperSizeIndex = value; }
    public int getPaperSizeIndex() { return paperSizeIndex; }
    public void setPaperSize(String value) { setPaperSizeIndex(paperSizeList.indexOf(value)); }
    public String getPaperSize() { return paperSizeList.get(getPaperSizeIndex()); }


    private SpinnerValueFactory<Integer> sigSizeSVF;
    public SpinnerValueFactory<Integer> getSigSizeSVF() { return sigSizeSVF; }
    public int getSigSize() { return sigSizeSVF.getValue(); }
    public void setSigSize(int value) { sigSizeSVF.setValue(value); BuildSignature(); }

    // Use PDFBook object to generate booklet.
    public boolean generate() {
        PDFBook booklet = new PDFBook(getSourceFilePath(), getOutputFilePath());

        booklet.setPageSize(getPaperSize());
        booklet.setSheetCount(getSigSize());
        booklet.setRotate(isRotateCheck());

        final int first = getFirstPage();
        final int last = getLastPage();
        booklet.setFirstPage(first-1);
        booklet.setLastPage(last);

        booklet.genBooklet();

        return true;
    }

    /**
     * Initialize "Selections" panel.
     */
    private void initializeSelections() {

        paperSizeList.addAll("A0", "A1", "A2", "A3", "A4", "A5", "A6", "Letter", "Legal");
        sigSizeSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 1);
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
        BuildSignature();
    }

    private SpinnerValueFactory<Integer> firstPageSVF;
    public SpinnerValueFactory<Integer> getFirstPageSVF() { return firstPageSVF; }
    public int getFirstPage() { return firstPageSVF.getValue(); }
    public void setFirstPage(int value) { firstPageSVF.setValue(value); setLastPageRange(value); BuildSignature(); }

    private void setFirstPageRange(int value) {
        int current = getFirstPage();
        if (current > value)
            current = value;
        firstPageSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, value, current);
    }

    private SpinnerValueFactory<Integer> lastPageSVF;
    public SpinnerValueFactory<Integer> getLastPageSVF() { return lastPageSVF; }
    public int getLastPage() { return lastPageSVF.getValue(); }
    public void setLastPage(int value) { lastPageSVF.setValue(value); setFirstPageRange(value); BuildSignature(); }

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
