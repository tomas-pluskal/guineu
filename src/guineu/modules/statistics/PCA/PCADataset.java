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
package guineu.modules.statistics.PCA;

import guineu.data.Dataset;
import guineu.data.PeakListRow;
import guineu.desktop.Desktop;
import guineu.main.GuineuCore;
import guineu.taskcontrol.TaskStatus;
import guineu.util.WekaUtils;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

//import javastat.multivariate.PCA;
import jmprojection.PCA;
import jmprojection.Preprocess;
import jmprojection.ProjectionStatus;

import org.jfree.data.xy.AbstractXYDataset;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.PrincipalComponents;

/**
 * @author Taken from MZmine2
 * http://mzmine.sourceforge.net/
 *
 */
public class PCADataset extends AbstractXYDataset implements
        ProjectionPlotDataset {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private double[] component1Coords;
    private double[] component2Coords;
    private ProjectionPlotParameters parameters;
    private Vector<String> selectedSamples;
    private List<PeakListRow> selectedRows;
    private int[] groupsForSelectedRawDataFiles;
    private Object[] parameterValuesForGroups;
    int numberOfGroups;
    private String datasetTitle;
    private int xAxisPC;
    private int yAxisPC;
    private TaskStatus status = TaskStatus.WAITING;
    private String errorMessage;
    private ProjectionStatus projectionStatus;
    private Dataset dataset;

    public PCADataset(ProjectionPlotParameters parameters) {

        this.parameters = parameters;

        this.xAxisPC = (Integer) parameters.getParameterValue(ProjectionPlotParameters.xAxisComponent);
        this.yAxisPC = (Integer) parameters.getParameterValue(ProjectionPlotParameters.yAxisComponent);

        selectedSamples = parameters.getSelectedSamples();
        selectedRows = parameters.getSelectedRows();
        dataset = parameters.getSourcePeakList();

        datasetTitle = "Principal component analysis";

        // Determine groups for selected raw data files
        groupsForSelectedRawDataFiles = new int[selectedSamples.size()];

        if (parameters.getParameterValue(ProjectionPlotParameters.coloringType) == ProjectionPlotParameters.ColoringTypeSingleColor) {
            // All files to a single group
            for (int ind = 0; ind < selectedSamples.size(); ind++) {
                groupsForSelectedRawDataFiles[ind] = 0;
            }

            numberOfGroups = 1;
        }

        if (parameters.getParameterValue(ProjectionPlotParameters.coloringType) == ProjectionPlotParameters.ColoringTypeByFile) {
            // Each file to own group
            for (int ind = 0; ind < selectedSamples.size(); ind++) {
                groupsForSelectedRawDataFiles[ind] = ind;
            }

            numberOfGroups = selectedSamples.size();
        }

        if (parameters.getParameterValue(ProjectionPlotParameters.coloringType) == ProjectionPlotParameters.ColoringTypeByParameterValue) {
            // Group files with same parameter value to same group
            Vector<Object> availableParameterValues = new Vector<Object>();
            for (String rawDataFile : selectedSamples) {
                String paramValue = parameters.getParamValue(rawDataFile);
                if (!availableParameterValues.contains(paramValue)) {
                    availableParameterValues.add(paramValue);
                }
            }

            for (int ind = 0; ind < selectedSamples.size(); ind++) {
                String paramValue = parameters.getParamValue(selectedSamples.elementAt(ind));
                groupsForSelectedRawDataFiles[ind] = availableParameterValues.indexOf(paramValue);
            }
            parameterValuesForGroups = availableParameterValues.toArray();

            numberOfGroups = parameterValuesForGroups.length;
        }
    }

    public String toString() {
        return datasetTitle;
    }

    public String getXLabel() {
        if (xAxisPC == 1) {
            return "1st PC";
        }
        if (xAxisPC == 2) {
            return "2nd PC";
        }
        if (xAxisPC == 3) {
            return "3rd PC";
        }
        return "" + xAxisPC + "th PC";
    }

    public String getYLabel() {
        if (yAxisPC == 1) {
            return "1st PC";
        }
        if (yAxisPC == 2) {
            return "2nd PC";
        }
        if (yAxisPC == 3) {
            return "3rd PC";
        }
        return "" + yAxisPC + "th PC";
    }

    @Override
    public int getSeriesCount() {
        return 1;
    }

    @Override
    public Comparable getSeriesKey(int series) {
        return 1;
    }

    public int getItemCount(int series) {
        return component1Coords.length;
    }

    public Number getX(int series, int item) {
        return component1Coords[item];
    }

    public Number getY(int series, int item) {
        return component2Coords[item];
    }

    public String getRawDataFile(int item) {
        //return "hola";
           return selectedSamples.elementAt(item);
    }

    public int getGroupNumber(int item) {
       // return 0;
            return groupsForSelectedRawDataFiles[item];
    }

    public Object getGroupParameterValue(int groupNumber) {
        if (parameterValuesForGroups == null) {
            return null;
        }
        if ((parameterValuesForGroups.length - 1) < groupNumber) {
            return null;
        }
        return parameterValuesForGroups[groupNumber];
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }

    public void run() {

        status = TaskStatus.PROCESSING;

        logger.info("Computing projection plot");

        double[][] rawData = new double[selectedSamples.size()][selectedRows.size()];
        for (int rowIndex = 0; rowIndex < selectedRows.size(); rowIndex++) {
            PeakListRow peakListRow = selectedRows.get(rowIndex);
            for (int fileIndex = 0; fileIndex < selectedSamples.size(); fileIndex++) {
                String rawDataFile = selectedSamples.elementAt(fileIndex);
                Object p = peakListRow.getPeak(rawDataFile);
                try {
                    rawData[fileIndex][rowIndex] = (Double) p;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        int numComponents = xAxisPC;
        if (yAxisPC > numComponents) {
            numComponents = yAxisPC;
        }

        /* System.out.println(dataset.getDatasetName());
        Instances data = WekaUtils.getWekaDataset(dataset);

        PrincipalComponents PCA = new PrincipalComponents();
        try {
        PCA.setInputFormat(data);
        PCA.setVarianceCovered(0.95);
        PCA.setCenterData(true);
        } catch (Exception ex) {
        Logger.getLogger(PCADataset.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
        Instances transformedData = Filter.useFilter(data, PCA);


        double[][] result = new double[transformedData.numInstances()][transformedData.numAttributes()];
        for (int rowIndex = 0; rowIndex < transformedData.numInstances(); rowIndex++) {
        Instance instance = data.instance(rowIndex);
        //PCA.input(instance);
        for (int fileIndex = 0; fileIndex < selectedSamples.size(); fileIndex++) {
        double[] row = instance.toDoubleArray();
        result[rowIndex] = row;
        }
        }

        if (status == TaskStatus.CANCELED) {
        return;
        }

        if (result.length > yAxisPC - 1) {
        component1Coords = result[yAxisPC - 1];
        component2Coords = result[xAxisPC - 1];


        Desktop desktop = GuineuCore.getDesktop();
        ProjectionPlotWindow newFrame = new ProjectionPlotWindow(desktop, this,
        parameters);
        desktop.addInternalFrame(newFrame);
        }

        } catch (Exception ex) {
        Logger.getLogger(PCADataset.class.getName()).log(Level.SEVERE, null, ex);
        }
         */


        //scaleToUnityVariance(rawData);
        //PCA pca = new PCA(0.95, "regression", rawData);

        //  double[][] result = (double[][]) pca.principalComponents;

        scaleToUnityVariance(rawData);
        PCA pcaProj = new PCA(rawData, numComponents);
        projectionStatus = pcaProj.getProjectionStatus();

        double[][] result = pcaProj.getState();

        if (status == TaskStatus.CANCELED) {
            return;
        }

        if (result.length > yAxisPC - 1) {
            component1Coords = result[yAxisPC - 1];
            component2Coords = result[xAxisPC - 1];


            Desktop desktop = GuineuCore.getDesktop();
            ProjectionPlotWindow newFrame = new ProjectionPlotWindow(desktop, this,
                    parameters);
            desktop.addInternalFrame(newFrame);
        }

        status = TaskStatus.FINISHED;
        logger.info("Finished computing projection plot.");

    }

    public void scaleToUnityVariance(double[][] data) {
        try {
            int cols;
            int rows;

            double mean;
            double s;
            double delta;
            double temp;

            rows = data.length;
            cols = data[0].length;

            for (int i = 0; i < cols; i++) {
                mean = 0.0;
                s = 0.0;
                delta = 0.0;

                for (int j = 0; j < rows; j++) {
                    temp = data[j][i];
                    delta = temp - mean;
                    mean += delta / (j + 1);
                    s += delta * (temp - mean);
                }
                s = Math.sqrt(s / (rows - 1));
                for (int j = 0; j < rows; j++) {

                    data[j][i] -= mean;
                    data[j][i] /= s;
                }                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        if (projectionStatus != null) {
            projectionStatus.cancel();
        }
        status = TaskStatus.CANCELED;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getTaskDescription() {
        if ((parameters == null) || (parameters.getSourcePeakList() == null)) {
            return "PCA projection";
        }
        return "PCA projection " + parameters.getSourcePeakList();
    }

    public double getFinishedPercentage() {
        if (projectionStatus == null) {
            return 0;
        }
        return projectionStatus.getFinishedPercentage();
    }

    public Object[] getCreatedObjects() {
        return null;
    }
}
