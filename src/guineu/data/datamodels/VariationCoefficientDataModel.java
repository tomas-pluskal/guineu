/*
 * Copyright 2007-2013 VTT Biotechnology
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
package guineu.data.datamodels;

import guineu.data.DatasetType;
import guineu.data.impl.VariationCoefficientData;
import guineu.util.Tables.DataTableModel;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class VariationCoefficientDataModel extends AbstractTableModel implements DataTableModel {

        private static final long serialVersionUID = 1L;
        private String columns[];
        private Object[][] rows; //content all data
        private int numColumns;
        private int numRows;
        private List<String> columns_mol = new ArrayList<String>();
        private Color[] rowColor;

        public VariationCoefficientDataModel(List<VariationCoefficientData> data) {
                rowColor = new Color[data.size()];
                columns_mol.add("DatasetName");
                columns_mol.add("Coefficient variation");
                columns_mol.add("N Molecules");
                columns_mol.add("N Molecules known");
                columns_mol.add("N Experiments");
                set_samples(data);
        }

        public Color getRowColor(int row) {
                if (row < rowColor.length) {
                        return rowColor[row];
                } else {
                        return null;
                }
        }

        public void addRowColor(Color[] color) {
                this.rowColor = color;
        }

        /**
         * Makes a new rows[][] with the new data. First it adds the column names with "writeSamplesNames(x)", and then
         * rewrites all data (rows[][]).
         */
        public void set_samples(List<VariationCoefficientData> data) {
                this.writeSamplesName();
                numColumns = columns.length;
                this.writeData(data);
                numRows = rows.length;
        }

        public Object[][] getRows() {
                return rows;
        }

        public void setRows(Object[][] rows) {
                this.rows = rows;
                numRows = rows.length;
        }

        /**
         * Adds the name of the experiments in the "columns" variable. There are the title of the columns.
         * @param sampleNames list of all experiments names.
         */
        public void writeSamplesName() {
                columns = new String[columns_mol.size()];
                for (int i = 0; i < columns_mol.size(); i++) {
                        columns[i] = (String) columns_mol.get(i);
                }
        }

        /**
         * Puts all data into an object array.
         *
         * @param data VariationCoefficientData type
         */
        public void writeData(List<VariationCoefficientData> data) {
                rows = new Object[data.size()][this.columns.length];

                for (int i = 0; i < data.size(); i++) {
                        VariationCoefficientData vcdata = data.get(i);
                        rows[i][0] = vcdata.datasetName;
                        rows[i][1] = vcdata.variationCoefficient;
                        rows[i][2] = vcdata.numberMol;
                        rows[i][3] = vcdata.NumberIdentMol;
                        rows[i][4] = vcdata.numberExperiments;
                }
        }

        public int getColumnCount() {
                return numColumns;
        }

        public int getRowCount() {
                return numRows;
        }

        public Object getValueAt(final int row, final int column) {
                return rows[row][column];
        }

        @Override
        public String getColumnName(int columnIndex) {
                String str = columns[columnIndex];
                /* if (columnIndex == sortCol && columnIndex != 0)
                str += isSortAsc ? " >>" : " <<";*/
                return str;
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
        public void setValueAt(Object aValue, int row, int column) {
                rows[row][column] = aValue;
                fireTableCellUpdated(row, column);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
                return true;
        }

        public void setColumnCount(int count) {
                this.numColumns = count;
        }

        public Object[][] getData() {
                return rows;
        }

        /**
         * @see guineu.util.Tables.DataTableModel
         */
        public DatasetType getType() {
                return null;
        }

        /**
         * @see guineu.util.Tables.DataTableModel
         */
        public int getFixColumns() {
                return 0;
        }

        /**
         * @see guineu.util.Tables.DataTableModel
         */
        public void removeRows() {
                throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * @see guineu.util.Tables.DataTableModel
         */
        public void addColumn(String ColumnName) {
                throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Color getCellColor(int row, int column) {
                return null;
        }
}
