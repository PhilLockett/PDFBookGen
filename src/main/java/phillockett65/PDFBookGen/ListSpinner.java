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
 * ListSpinner is a Spinner wrapper class for an ObservableList of Strings.
 */
package phillockett65.BaseFXML;

import javafx.collections.ObservableList;
import javafx.scene.control.SpinnerValueFactory;

public class ListSpinner {

    private ObservableList<String> list;
    private SpinnerValueFactory<String> SVF;

    public ListSpinner(ObservableList<String> list) {
        this.list = list;
        SVF = new SpinnerValueFactory.ListSpinnerValueFactory<String>(list);
    }

    public ObservableList<String> getList() {
        return list;
    }

    public SpinnerValueFactory<String> getSVF() {
        return SVF;
    }

    public String getCurrent() {
        return SVF.getValue();
    }

    public int getIndex() {
        return list.indexOf(getCurrent());
    }

    public void setCurrent(String value) {
        SVF.setValue(value);
    }

    public void setIndex(int value) {
        SVF.setValue(list.get(value));
    }

    public void increment(int steps) {
        SVF.increment(steps);
    }

}
