/*
 * Copyright 2007-2010 VTT Biotechnology
 * This file is part of Guineu.
 *
 * Guineu is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * Guineu is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Guineu; if not, write to the Free Software Foundation, Inc., 51 Franklin St,
 * Fifth Floor, Boston, MA 02110-1301 USA
 */
package guineu.modules.identification.normalizationserum;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author scsandra
 */
public class StandardsDataModel extends AbstractTableModel {

    private String[] columns;
    private Vector<String> name;
    private Vector<Double> value;//content all data
    private int numColumns;
    private int numRows;
    Vector<StandardUmol> standards;

    public StandardsDataModel(Vector<StandardUmol> standards) {

        this.standards = standards;

        columns = new String[2];
        columns[0] = "Standard Name";
        columns[1] = "Amount";
        name = new Vector<String>();
        value = new Vector<Double>();
        for (StandardUmol std : standards) {
            name.addElement(std.getName());
            value.addElement(std.getRealAmount());
        }
        numRows = name.size();
        numColumns = 2;
    }

    public int getRowCount() {
        return numRows;
    }

    public int getColumnCount() {
        return numColumns;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (column == 0) {
            name.setElementAt((String) aValue, row);
        } else {
            value.setElementAt((Double) aValue, row);
        }

    }

    @Override
    public Class<?> getColumnClass(int c) {
        if (getValueAt(0, c) != null) {
            return getValueAt(0, c).getClass();
        } else {
            return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    public Object getValueAt(int row, int column) {
        if (column == 0) {
            return name.elementAt(row);
        } else {
            return value.elementAt(row);
        }
    }

    public void fillStandards() {
        for (int i = 0; i < this.name.size(); i++) {
            this.standards.elementAt(i).setRealAmount(value.elementAt(i));
        }
    }

    public void resetStandards() {
        for (int i = 0; i < this.value.size(); i++) {
            this.value.set(i, 0.0);
        }
        this.fireTableDataChanged();
    }
}
