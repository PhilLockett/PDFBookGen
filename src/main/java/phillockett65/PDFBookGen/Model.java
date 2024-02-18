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

        initializeFileNamesPanel();
        initializeOutputContentPanel();
        initializeSignatureStatePanel();
        initializeStatusLine();

        if (!readData())
            defaultSettings();

        // Calculate the signature data AFTER reading previous settings.
        BuildSignature();
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
    public String getTitle() { return stage.getTitle(); }

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

        setPaperSize("Letter");
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

        data.setPaperSize(getPaperSize());
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

        setPaperSize(data.getPaperSize());
        setSigSize(data.getSigSize());
    
        setFirstPage(data.getFirstPage());
        setLastPage(data.getLastPage());
    
        return true;
    }



    /************************************************************************
     * Support code for "File Names" panel.
     */

    private String sourceDocument;
    private String outputFilePath;

    /**
     * Set the file path for the source PDF document.
     * @param text string of the source document file path.
     */
    public void setSourceFilePath(String text) {
        sourceDocument = text;
        setPageCount(fetchPageCount());
    }

    /**
     * @return the number of pages in the current source document.
     */
    private int fetchPageCount() {
        if (isSourceFilePath())
            return PDFBook.getPDFPageCount(sourceDocument);

        return 1;
    }

    /**
     * @return the file path for the current source PDF document.
     */
    public String getSourceFilePath() { return sourceDocument; }

    /**
     * @return true if a source document has been selected, false otherwise.
     */
    public boolean isSourceFilePath() { if (sourceDocument == null) return false; return !sourceDocument.isBlank(); }

    private String fileStem(String fileName) {
        if (!fileName.contains("."))
            return fileName;

        return fileName.substring(0,fileName.lastIndexOf("."));
    }

    private String getFileParent(String file) {
        if (file.isBlank())
            return "."; 

        File current = new File(file);
        return current.getParent();
    }

    private String getOutputFileParent() {
        if (isOutputFilePath())
            return getFileParent(getOutputFilePath());

        if (isSourceFilePath())
            return getFileParent(getSourceFilePath());

        return ".";
    }

    /**
     * Set the file name for the generated PDF document.
     * @param text string of the file name for the generated document.
     */
    public void setOutputFileName(String text) { outputFilePath = getOutputFileParent() + "\\" + text + ".pdf"; }

    /**
     * Set the file name for the generated PDF document.
     * @param text string of the file name for the generated document.
     */
    public void setOutputFilePath(String text) { outputFilePath = text; }

    /**
     * @return the file name for the generated PDF document.
     */
    public String getOutputFileName() {
        File current = new File(getOutputFilePath());
        return fileStem(current.getName());
    }

    /**
     * @return the full file path for the generated PDF document.
     */
    public String getOutputFilePath() { return outputFilePath; }

    /**
     * @return true if a source document has been selected, false otherwise.
     */
    public boolean isOutputFilePath() { if (outputFilePath == null) return false; return !outputFilePath.isBlank(); }

    /**
     * Initialize "File Names" panel.
     */
    private void initializeFileNamesPanel() {
    }



    /************************************************************************
     * Support code for "Output Content" panel.
     */

    private String paperSize;
    private ObservableList<String> paperSizeList = FXCollections.observableArrayList();

    private boolean rotateCheck;
    private int pageCount = 50;

    private SpinnerValueFactory<Integer> firstPageSVF;
    private SpinnerValueFactory<Integer> lastPageSVF;


    /**
     * @return the Observable List for the paper size choice box.
     */
    public ObservableList<String> getPaperSizeList() { return paperSizeList; }

    /**
     * Note the selected paper size.
     * @param value of the currently selected paper size as a string.
     */
    public void setPaperSize(String value) { paperSize = value; }

    /**
     * @return the currently selected paper size string.
     */
    public String getPaperSize() { return paperSize; }


    /**
     * Indicate whether the reverse side page is to be rotated.
     * @param state true if the reverse page is to be rotated, false otherwise.
     */
    public void setRotateCheck(boolean state) { rotateCheck = state; }

    /**
     * @return true if the reverse side page is to be rotated, false otherwise.
     */
    public boolean isRotateCheck() { return rotateCheck; }


    private int getPageCount() { return pageCount; }
    private void setPageCount(int value) {
        pageCount = value;
        firstPageSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, value, 1);
        lastPageSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, value, value);
        BuildSignature();
    }


    /**
     * @return the Value Factory for the first page spinner.
     */
    public SpinnerValueFactory<Integer> getFirstPageSVF() { return firstPageSVF; }

    private int getFirstPage() { return firstPageSVF.getValue(); }
    private void setFirstPage(int value) { firstPageSVF.setValue(value); }

    /**
     * Selected first page has changed, so synchronize values.
     */
    public void syncFirstPage() { setLastPageRange(); BuildSignature(); }

    private void setFirstPageRange() {
        final int last = getLastPage();
        int current = getFirstPage();
        if (current > last)
            current = last;
        firstPageSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, last, current);
    }

    /**
     * @return the Value Factory for the last page spinner.
     */
    public SpinnerValueFactory<Integer> getLastPageSVF() { return lastPageSVF; }

    private int getLastPage() { return lastPageSVF.getValue(); }
    private void setLastPage(int value) { lastPageSVF.setValue(value); }

    /**
     * Selected last page has changed, so synchronize values.
     */
    public void syncLastPage() { setFirstPageRange(); BuildSignature(); }

    private void setLastPageRange() {
        final int first = getFirstPage();
        int current = getLastPage();
        if (current < first)
            current = first;
        lastPageSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(first, getPageCount(), current);
    }


    /**
     * Use a PDFBook instance to generate booklet.
     * @return true if document was generated, false otherwise.
     */
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
     * Initialize "Output Content" panel.
     */
    private void initializeOutputContentPanel() {
        paperSizeList.addAll("A0", "A1", "A2", "A3", "A4", "A5", "A6", "Letter", "Legal");
        firstPageSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        lastPageSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
    }



    /************************************************************************
     * Support code for "Signature State" panel.
     */

    private SpinnerValueFactory<Integer> sigSizeSVF;
    private Signature signature;

    /**
     * @return the Value Factory for the signature size spinner.
     */
    public SpinnerValueFactory<Integer> getSigSizeSVF() { return sigSizeSVF; }

    private int getSigSize() { return sigSizeSVF.getValue(); }
    private void setSigSize(int value) { sigSizeSVF.setValue(value); }

    /**
     * Signature size has changed, so synchronize values.
     */
    public void syncSigSize() { BuildSignature(); }


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
    
    private void BuildSignature() {
        signature = new Signature(getSigSize(), getFirstPage(), getLastPage());
    }

    /**
     * @return the number of source pages in the generated document.
     */
    public int getOutputPageCount() { return signature.pageCount; }

    /**
     * @return the number of source pages in a signature.
     */
    public int getSigPageCount() { return signature.sigPageCount; }

    /**
     * @return the number of signatures that will be generated.
     */
    public int getSigCount() { return signature.sigCount; }

    /**
     * @return the source page number that the last signature starts with.
     */
    public int getLastSigFirstPage() { return signature.lastSigFirstPage; }

    /**
     * @return the number of source pages in the last signature.
     */
    public int getLastSigPageCount() { return signature.lastSigPageCount; }

    /**
     * @return the number of blank pages in the last signature.
     */
    public int getLastSigBlankCount() { return signature.lastSigBlankCount; }


    /**
     * Initialize "Signature State" panel.
     */
    private void initializeSignatureStatePanel() {
        sigSizeSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 1);
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
