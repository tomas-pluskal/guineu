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
package guineu.modules.filter.Alignment.centering.median;

import guineu.data.Dataset;
import guineu.data.PeakListRow;
import guineu.taskcontrol.Task;
import guineu.taskcontrol.TaskStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

class MedianCenteringTask implements Task {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Dataset peakLists[];
    private TaskStatus status = TaskStatus.WAITING;
    private String errorMessage;
    // Processed rows counter
    private int processedRows,  totalRows;

    public MedianCenteringTask(Dataset[] peakLists) {

        this.peakLists = peakLists;
    }

   
    public String getTaskDescription() {
        return "Median centering";
    }

   
    public double getFinishedPercentage() {
        if (totalRows == 0) {
            return 0f;
        }
        return (double) processedRows / (double) totalRows;
    }

    
    public TaskStatus getStatus() {
        return status;
    }

    
    public String getErrorMessage() {
        return errorMessage;
    }

    
    public void cancel() {
        status = TaskStatus.CANCELED;
    }

    /**
     * @see Runnable#run()
     */
    public void run() {
        status = TaskStatus.PROCESSING;
        logger.info("Running Median centering");

        for (Dataset data : this.peakLists) {
            normalize(data);
        }
        logger.info(
                "Finished Median centering");
        status = TaskStatus.FINISHED;

    }

    private void normalize(Dataset data) {
        for (String nameExperiment : data.getAllColumnNames()) {
            List<Double> median = new ArrayList<Double>();
            for (PeakListRow row : data.getRows()) {
                Object value = row.getPeak(nameExperiment);
                if (value != null && value instanceof Double) {
                    median.add((Double) value);
                }
            }
            Collections.sort(median);

            for (PeakListRow row : data.getRows()) {
                Object value = row.getPeak(nameExperiment);
                if (value != null && value instanceof Double) {
                    row.setPeak(nameExperiment, Math.abs((Double) value - median.get(median.size() / 2)));
                }
            }
        }
    }
}
