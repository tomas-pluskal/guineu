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
package guineu.modules.dataanalysis.correlations;

import guineu.data.Dataset;
import guineu.data.PeakListRow;
import guineu.data.impl.datasets.SimpleBasicDataset;
import guineu.data.impl.peaklists.SimplePeakListRowOther;
import guineu.taskcontrol.AbstractTask;
import guineu.taskcontrol.TaskStatus;
import guineu.util.GUIUtils;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;

/**
 *
 * @author scsandra
 */
public class CorrelationTask extends AbstractTask {

        private Dataset[] dataset;
        private int progress = 0;

        public CorrelationTask(Dataset[] dataset) {
                this.dataset = dataset;
        }

        public String getTaskDescription() {
                return "Performing correlation matrix... ";
        }

        public double getFinishedPercentage() {
                return (float) progress;
        }

        public void cancel() {
                setStatus(TaskStatus.CANCELED);
        }

        public void run() {
                setStatus(TaskStatus.PROCESSING);
                try {
                        Dataset data1 = dataset[0];
                        Dataset data2 = dataset[1];
                        Dataset newDataset = new SimpleBasicDataset("Correlation matrix");
                        newDataset.addColumnName("ID");
                        for (PeakListRow row : data1.getRows()) {
                                String name = row.getID() + " - " + row.getName();
                                newDataset.addColumnName(name);
                        }

                        for (int i = 0; i < data2.getNumberRows(); i++) {
                                PeakListRow row = data2.getRow(i);
                                PeakListRow newRow = new SimplePeakListRowOther();
                                newRow.setPeak("ID", row.getID() + " - " + row.getName());
                                newDataset.addRow(newRow);
                        }

                        for (PeakListRow row : data1.getRows()) {
                                String name = row.getID() + " - " + row.getName();
                                for (int i = 0; i < data2.getNumberRows(); i++) {
                                        double r = getCorrelation(row, data2.getRow(i), data1.getAllColumnNames());
                                        newDataset.getRow(i).setPeak(name, String.valueOf(r));
                                }
                        }
                        GUIUtils.showNewTable(newDataset, true);
                        setStatus(TaskStatus.FINISHED);
                } catch (Exception ex) {
                        Logger.getLogger(CorrelationTask.class.getName()).log(Level.SEVERE, null, ex);
                        setStatus(TaskStatus.ERROR);
                }
        }

        private double getCorrelation(PeakListRow row, PeakListRow row2, Vector<String> sampleNames) {
                double[] xArray = new double[sampleNames.size()];
                double[] yArray = new double[sampleNames.size()];
                for (int i = 0; i < sampleNames.size(); i++) {
                        try {
                                xArray[i] = (Double) row.getPeak(sampleNames.elementAt(i));
                                yArray[i] = (Double) row2.getPeak(sampleNames.elementAt(i));
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }
                PearsonsCorrelation correlation = new PearsonsCorrelation();
                return correlation.correlation(xArray, yArray);
        }
}
