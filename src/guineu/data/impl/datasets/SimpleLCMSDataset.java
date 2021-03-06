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
package guineu.data.impl.datasets;

import guineu.data.Dataset;
import guineu.data.DatasetType;
import guineu.data.LCMSColumnName;
import guineu.data.PeakListRow;
import guineu.data.impl.SampleDescription;
import guineu.data.impl.peaklists.SimplePeakListRowLCMS;
import guineu.util.Range;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * LC-MS data set implementation.
 *
 * @author SCSANDRA
 */
public class SimpleLCMSDataset implements Dataset {

        private String datasetName;
        private List<PeakListRow> peakList;
        private List<String> sampleNames;
        private List<String> parameterNames;
        private HashMap<String, SampleDescription> parameters;
        private DatasetType type;
        private String infoDataset = "";
        private int ID;
        private int numberRows = 0;
        private List<Color> rowColor;

        /**
         *
         * @param datasetName Name of the dataset
         */
        public SimpleLCMSDataset(String datasetName) {
                this.datasetName = datasetName;
                this.peakList = new ArrayList<PeakListRow>();
                this.sampleNames = new ArrayList<String>();
                this.parameters = new HashMap<String, SampleDescription>();
                this.parameterNames = new ArrayList<String>();
                this.rowColor = new ArrayList<Color>();
                type = DatasetType.LCMS;
        }

        public void setID(int ID) {
                this.ID = ID;
        }

        public int getID() {
                return ID;
        }

        public void addParameterValue(String experimentName, String parameterName, String parameterValue) {
                if (parameters.containsKey(experimentName)) {
                        SampleDescription p = parameters.get(experimentName);
                        p.addParameter(parameterName, parameterValue);
                } else {
                        SampleDescription p = new SampleDescription();
                        p.addParameter(parameterName, parameterValue);
                        parameters.put(experimentName, p);
                }
                if (!this.parameterNames.contains(parameterName)) {
                        parameterNames.add(parameterName);
                }
        }

        public void deleteParameter(String parameterName) {
                for (String experimentName : sampleNames) {
                        if (parameters.containsKey(experimentName)) {
                                SampleDescription p = parameters.get(experimentName);
                                p.deleteParameter(parameterName);
                        }
                }
                this.parameterNames.remove(parameterName);
        }

        public String getParametersValue(String experimentName, String parameterName) {
                if (parameters.containsKey(experimentName)) {
                        SampleDescription p = parameters.get(experimentName);
                        return p.getParameter(parameterName);
                } else {
                        return null;
                }
        }

        @Override
        public List<String> getParameterAvailableValues(String parameter) {
                List<String> availableParameterValues = new ArrayList<String>();
                for (String rawDataFile : this.sampleNames) {
                       String paramValue = this.getParametersValue(rawDataFile, parameter);
                        if (paramValue != null && !paramValue.isEmpty() && !availableParameterValues.contains(paramValue)) {
                                availableParameterValues.add(paramValue);
                        }
                }
                return availableParameterValues;
        }

        public List<String> getParametersName() {
                return parameterNames;
        }

        public String getDatasetName() {
                return this.datasetName;
        }

        public void setDatasetName(String datasetName) {
                this.datasetName = datasetName;
        }

        public void addRow(PeakListRow peakListRow) {
                this.peakList.add(peakListRow);
        }

        public void addColumnName(String sampleName) {
                this.sampleNames.add(sampleName);
        }

        public void addColumnName(String columnName, int position) {
                this.sampleNames.set(position, columnName);
        }

        public List<String> getAllColumnNames() {
                return this.sampleNames;
        }

        public PeakListRow getRow(int i) {
                return this.peakList.get(i);
        }

        public List<PeakListRow> getRows() {
                return this.peakList;
        }

        public int getNumberRows() {
                return this.peakList.size();
        }

        public int getNumberRowsdb() {
                return this.numberRows;
        }

        public void setNumberRows(int numberRows) {
                this.numberRows = numberRows;
        }

        public int getNumberCols() {
                return this.sampleNames.size();
        }

        public DatasetType getType() {
                return type;
        }

        public void setType(DatasetType type) {
                this.type = type;
        }

        /**
         * Add new rows into the data set. The rows can be in any kind of
         * Collection class.
         *
         * @param rows Rows to be added.
         */
        public void addAll(Collection<? extends SimplePeakListRowLCMS> rows) {
                for (SimplePeakListRowLCMS r : rows) {
                        peakList.add(r);
                }
        }

        public void removeRow(PeakListRow row) {
                try {
                        this.peakList.remove(row);
                } catch (Exception e) {
                        System.out.println("No row found");
                }
        }

        public String getInfo() {
                return infoDataset;
        }

        public void setInfo(String info) {
                this.infoDataset = info;
        }

        @Override
        public SimpleLCMSDataset clone() {
                SimpleLCMSDataset newDataset = new SimpleLCMSDataset(this.datasetName);
                for (String experimentName : this.sampleNames) {
                        newDataset.addColumnName(experimentName);
                        for (String parameterName : this.parameterNames) {
                                newDataset.addParameterValue(experimentName, parameterName, this.getParametersValue(experimentName, parameterName));
                        }

                }
                for (PeakListRow peakListRow : this.peakList) {
                        newDataset.addRow(peakListRow.clone());
                }
                newDataset.setType(this.type);

                newDataset.infoDataset = infoDataset;

                return newDataset;
        }

        @Override
        public String toString() {
                return this.getDatasetName();
        }

        /**
         * Returns the rows inside the RT and m/z range given as a parameter.
         *
         * @param rtRange Retention time range
         * @param mzRange m/z range
         * @return Array with the rows inside this RT and m/z range
         */
        public PeakListRow[] getRowsInsideRTAndMZRange(Range rtRange, Range mzRange) {
                List<PeakListRow> rows = new ArrayList<PeakListRow>();
                for (PeakListRow row : this.peakList) {
                        if (rtRange.contains((Double) row.getVar(LCMSColumnName.RT.getGetFunctionName()))
                                && mzRange.contains((Double) row.getVar(LCMSColumnName.MZ.getGetFunctionName()))) {
                                rows.add(row);
                        }
                }
                return rows.toArray(new PeakListRow[0]);
        }

        /**
         * Returns the retention time range of all the rows.
         *
         * @return Retention time range
         */
        public Range getRowsRTRange() {
                double min = Double.MAX_VALUE;
                double max = 0;
                for (PeakListRow row : this.peakList) {
                        double RTvalue = (Double) row.getVar(LCMSColumnName.RT.getGetFunctionName());
                        if (RTvalue < min) {
                                min = RTvalue;
                        }
                        if (RTvalue > max) {
                                max = RTvalue;
                        }
                }
                return new Range(min, max);
        }

        public List<PeakListRow> getSelectedRows() {
                List<PeakListRow> selectedRows = new ArrayList<PeakListRow>();
                for (PeakListRow row : this.getRows()) {
                        if (row.isSelected()) {
                                selectedRows.add(row);
                        }
                }
                return selectedRows;
        }

        @Override
        public void removeSampleNames() {
                this.sampleNames.clear();
        }

        @Override
        public Color[] getRowColor() {
                return this.rowColor.toArray(new Color[0]);
        }

        @Override
        public void addRowColor(Color rowColor) {
                this.rowColor.add(rowColor);
        }

        @Override
        public Color getCellColor(int row, int column) {
                return this.getRow(row).getColor(column);
        }

        @Override
        public void setCellColor(Color cellColor, int row, int column) {
                this.getRow(row).setColor(cellColor, column);
        }
}
