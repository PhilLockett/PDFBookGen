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
 * DataStore is a class that serializes the settings data for saving and 
 * restoring to and from disc.
 */
package phillockett65.PDFBookGen;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DataStore implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sourceDocument;
    private String outputFileName;

    private Boolean rotateCheck;
    private Boolean padCheck;

    private Integer pageSizeIndex;
    private Integer sigSizeIndex;

    private Integer firstPage;
    private Integer lastPage;



    /************************************************************************
     * Support code for the Initialization of the DataStore.
     */

    public DataStore() {
    }

    public String getSourceDocument() { return sourceDocument; }
    public void setSourceDocument(String text) { this.sourceDocument = text; }
    public String getOutputFileName() { return outputFileName; }
    public void setOutputFileName(String text) { this.outputFileName = text; }

    public Boolean isRotateCheck() { return rotateCheck; }
    public void setRotateCheck(Boolean check) { this.rotateCheck = check; }
    public Boolean isPadCheck() { return padCheck; }
    public void setPadCheck(Boolean check) { this.padCheck = check; }

    public Integer getPageSizeIndex() { return pageSizeIndex; }
    public void setPageSizeIndex(Integer index) { this.pageSizeIndex = index; }
    public Integer getSigSizeIndex() { return sigSizeIndex; }
    public void setSigSizeIndex(Integer index) { this.sigSizeIndex = index; }

    public Integer getFirstPage() { return firstPage; }
    public void setFirstPage(Integer value) { this.firstPage = value; }
    public Integer getLastPage() { return lastPage; }
    public void setLastPage(Integer value) { this.lastPage = value; }



    /************************************************************************
     * Support code for debug.
     */

     /**
      * Print data store on the command line.
      */
      public void dump() {
        System.out.println("sourceDocument = " + sourceDocument);
        System.out.println("outputFileName = " + outputFileName);
        System.out.println("rotateCheck = " + rotateCheck);
        System.out.println("padCheck = " + padCheck);
        System.out.println("pageSizeIndex = " + pageSizeIndex);
        System.out.println("sigSizeIndex = " + sigSizeIndex);
        System.out.println("firstPage = " + firstPage);
        System.out.println("lastPage = " + lastPage);
    }



    /************************************************************************
     * Support code for static public interface.
     */

    /**
     * Static method that receives a populated DataStore and writes it to disc.
     * @param dataStore contains the data.
     * @param settingsFile path of the settings data file.
     * @return true if data successfully written to disc, false otherwise.
     */
    public static boolean writeData(DataStore dataStore, String settingsFile) {
        boolean success = false;

        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(settingsFile));

            objectOutputStream.writeObject(dataStore);
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return success;
    }


    /**
     * Static method that instantiates a DataStore, populates it from disc 
     * and returns it.
     * @param settingsFile path of the settings data file.
     * @return a populated DataStore if data successfully read from disc, null otherwise.
     */
    public static DataStore readData(String settingsFile) {
        DataStore dataStore = null;

        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(settingsFile));

            dataStore = (DataStore)objectInputStream.readObject();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return dataStore;
    }


}

