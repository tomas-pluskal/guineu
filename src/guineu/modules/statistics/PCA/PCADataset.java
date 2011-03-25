/*
 * Copyright 2007-2011 VTT Biotechnology
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

import Jama.Matrix;
import dr.PCA;
import dr.PrincipleComponent;
import guineu.data.PeakListRow;
import guineu.desktop.Desktop;
import guineu.main.GuineuCore;
import guineu.taskcontrol.TaskStatus;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.jfree.data.xy.AbstractXYDataset;

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
        private String[] selectedSamples;
        private PeakListRow[] selectedRows;
        private int[] groupsForSelectedRawDataFiles;
        private Object[] parameterValuesForGroups;
        int numberOfGroups;
        private String datasetTitle;
        private int xAxisPC;
        private int yAxisPC;
        private TaskStatus status = TaskStatus.WAITING;
        private String errorMessage;
        private float progress = 0.0f;
        private List<PrincipleComponent> mainComponents;
        private double totalVariation = 0;

        public PCADataset(ProjectionPlotParameters parameters) {

                this.parameters = parameters;

                this.xAxisPC = parameters.getParameter(ProjectionPlotParameters.xAxisComponent).getValue();
                this.yAxisPC = parameters.getParameter(ProjectionPlotParameters.yAxisComponent).getValue();

                selectedSamples = parameters.getParameter(ProjectionPlotParameters.dataFiles).getValue();
                selectedRows = parameters.getParameter(ProjectionPlotParameters.rows).getValue();

                datasetTitle = "Principal component analysis";

                // Determine groups for selected raw data files
                groupsForSelectedRawDataFiles = new int[selectedSamples.length];

                if (parameters.getParameter(ProjectionPlotParameters.coloringType).getValue() == ColoringType.NOCOLORING) {
                        // All files to a single group
                        for (int ind = 0; ind < selectedSamples.length; ind++) {
                                groupsForSelectedRawDataFiles[ind] = 0;
                        }

                        numberOfGroups = 1;
                }

                if (parameters.getParameter(ProjectionPlotParameters.coloringType).getValue() == ColoringType.COLORBYFILE) {
                        // Each file to own group
                        for (int ind = 0; ind < selectedSamples.length; ind++) {
                                groupsForSelectedRawDataFiles[ind] = ind;
                        }

                        numberOfGroups = selectedSamples.length;
                }

                if (parameters.getParameter(ProjectionPlotParameters.coloringType).getValue() == ColoringType.COLORBYPARAMETER) {
                        // Group files with same parameter value to same group
           /* Vector<Object> availableParameterValues = new Vector<Object>();
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
                        parameterValuesForGroups = availableParameterValues.toArray();*/

                        numberOfGroups = parameterValuesForGroups.length;
                }
        }

        @Override
        public String toString() {
                return datasetTitle;
        }

        public String getXLabel() {
                NumberFormat formatter = new DecimalFormat("#0.00");

                String variation = formatter.format((this.mainComponents.get(xAxisPC - 1).eigenValue / this.totalVariation) * 100);
                if (xAxisPC == 1) {
                        return "1st PC (" + variation + "%)";
                }
                if (xAxisPC == 2) {
                        return "2nd PC (" + variation + "%)";
                }
                if (xAxisPC == 3) {
                        return "3rd PC (" + variation + "%)";
                }
                return "" + xAxisPC + "th PC (" + variation + "%)";
        }

        public String getYLabel() {
                NumberFormat formatter = new DecimalFormat("#0.00");
                String variation = formatter.format((this.mainComponents.get(yAxisPC - 1).eigenValue / this.totalVariation) * 100);
                if (yAxisPC == 1) {
                        return "1st PC (" + variation + "%)";
                }
                if (yAxisPC == 2) {
                        return "2nd PC (" + variation + "%)";
                }
                if (yAxisPC == 3) {
                        return "3rd PC (" + variation + "%)";
                }
                return "" + yAxisPC + "th PC (" + variation + "%)";
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
                return selectedSamples[item];
        }

        public int getGroupNumber(int item) {
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

                double[][] rawData = new double[selectedSamples.length][selectedRows.length];
                for (int rowIndex = 0; rowIndex < selectedRows.length; rowIndex++) {
                        PeakListRow peakListRow = selectedRows[rowIndex];
                        for (int fileIndex = 0; fileIndex < selectedSamples.length; fileIndex++) {
                                String rawDataFile = selectedSamples[fileIndex];
                                Object p = peakListRow.getPeak(rawDataFile);
                                try {
                                        rawData[fileIndex][rowIndex] = (Double) p;
                                } catch (Exception e) {
                                        e.printStackTrace();
                                }

                        }
                }
                this.progress = 0.25f;

                int numComponents = xAxisPC;
                if (yAxisPC > numComponents) {
                        numComponents = yAxisPC;
                }


                PCA pca = new PCA(selectedSamples.length, selectedRows.length);
                Matrix X = new Matrix(rawData, selectedSamples.length, selectedRows.length);
                X = pca.center(X);
                X = pca.scale(X);
                pca.nipals(X);
                mainComponents = pca.getPCs();
                Collections.sort(mainComponents);
                if (status == TaskStatus.CANCELED) {
                        return;
                }
                this.progress = 0.75f;

                for (PrincipleComponent components : mainComponents) {
                        this.totalVariation += components.eigenValue;
                }

                if (mainComponents.size() > yAxisPC - 1) {
                        component1Coords = mainComponents.get(xAxisPC - 1).eigenVector;
                        component2Coords = mainComponents.get(yAxisPC - 1).eigenVector;

                        Desktop desktop = GuineuCore.getDesktop();
                        ProjectionPlotWindow newFrame = new ProjectionPlotWindow(desktop, this,
                                parameters, this.datasetTitle);
                        desktop.addInternalFrame(newFrame);
                }
                this.progress = 1.0f;
                status = TaskStatus.FINISHED;
                logger.info("Finished computing projection plot.");

        }

        public void cancel() {
                status = TaskStatus.CANCELED;
        }

        public String getErrorMessage() {
                return errorMessage;
        }

        public TaskStatus getStatus() {
                return status;
        }

        public String getTaskDescription() {
                if (parameters == null) {
                        return "PCA projection";
                }
                return "PCA projection " + this.datasetTitle;
        }

        public double getFinishedPercentage() {
                return progress;
        }

        public Object[] getCreatedObjects() {
                return null;
        }
}
