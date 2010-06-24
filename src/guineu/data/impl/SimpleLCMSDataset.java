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
package guineu.data.impl;

import guineu.data.Dataset;
import guineu.data.PeakListRow;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author SCSANDRA
 */
public class SimpleLCMSDataset implements Dataset {

    private String datasetName;
    private List<PeakListRow> peakList;
    private Vector<String> nameExperiments;
    private Vector<String> parameterNames;
    private Hashtable<String, Parameters> parameters;
    private DatasetType type;
    private String infoDataset = "";

    public SimpleLCMSDataset(String datasetName) {
        this.datasetName = datasetName;
        this.peakList = new ArrayList<PeakListRow>();
        this.nameExperiments = new Vector<String>();
        this.parameters = new Hashtable<String, Parameters>();
        this.parameterNames = new Vector<String>();
        this.parameterNames.addElement("Samples");
        type = DatasetType.LCMS;
    }

    public void addParameter(String experimentName, String parameterName, String parameterValue) {
        if (parameters.containsKey(experimentName)) {
            Parameters p = parameters.get(experimentName);
            p.addParameter(parameterName, parameterValue);
        } else {
            Parameters p = new Parameters();
            p.addParameter(parameterName, parameterValue);
            parameters.put(experimentName, p);
        }
        if (!this.parameterNames.contains(parameterName)) {
            parameterNames.addElement(parameterName);
        }
    }
   
    public void deleteParameter(String parameterName) {
        for (String experimentName : nameExperiments) {
            if (parameters.containsKey(experimentName)) {
                Parameters p = parameters.get(experimentName);
                p.deleteParameter(parameterName);
            }
        }
        this.parameterNames.remove(parameterName);
    }

    public String getParametersValue(String experimentName, String parameterName) {
        if (parameters.containsKey(experimentName)) {
            Parameters p = parameters.get(experimentName);
            return p.getParameter(parameterName);
        } else {
            return null;
        }
    }

    public Vector<String> getParametersName() {
        return parameterNames;
    }

    public String getDatasetName() {
        return this.datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public void AddRow(PeakListRow peakListRow) {
        this.peakList.add(peakListRow);
    }

    public void AddNameExperiment(String nameExperiment) {
        this.nameExperiments.addElement(nameExperiment);
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

    public int getNumberCols() {
        return this.nameExperiments.size();
    }

    public Vector<String> getNameExperiments() {
        return this.nameExperiments;
    }

    public void setNameExperiments(Vector<String> experimentNames) {
        this.nameExperiments = experimentNames;
    }

    public DatasetType getType() {
        return type;
    }

    public void setType(DatasetType type) {
        this.type = type;
    }

    @Override
    public SimpleLCMSDataset clone() {
        SimpleLCMSDataset newDataset = new SimpleLCMSDataset(this.datasetName);
        for (String experimentName : this.nameExperiments) {
            newDataset.AddNameExperiment(experimentName);
        }
        for (PeakListRow peakListRow : this.peakList) {
            newDataset.AddRow(peakListRow.clone());
        }
        newDataset.setType(this.type);
        return newDataset;
    }

    public void removeRow(PeakListRow row) {
        try {
            this.peakList.remove(row);
        } catch (Exception e) {
            System.out.println("No row found");
        }
    }

    public void AddNameExperiment(String nameExperiment, int position) {
        this.nameExperiments.insertElementAt(nameExperiment, position);
    }

    public String toString() {
        return this.getDatasetName();
    }

    public String getInfo() {
        return infoDataset;
    }

    public void setInfo(String info) {
        this.infoDataset = info;
    }

    class Parameters {

        Hashtable<String, String> parameters;

        public Parameters() {
            parameters = new Hashtable<String, String>();
        }

        public void addParameter(String parameterName, String parameterValue) {
            if (parameterName != null && parameterValue != null) {
                parameters.put(parameterName, parameterValue);
            }
        }

        public void deleteParameter(String parameterName) {
            if (parameters.containsKey(parameterName)) {
                parameters.remove(parameterName);
            }
        }

        public String getParameter(String parameterName) {
            if (parameters.containsKey(parameterName)) {
                return parameters.get(parameterName);
            } else {
                return null;
            }
        }
    }
}