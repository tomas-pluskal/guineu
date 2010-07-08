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
package guineu.modules.mylly.filter.pubChem;

import guineu.data.DatasetType;
import guineu.data.impl.SimpleGCGCDataset;
import guineu.taskcontrol.Task;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import guineu.data.Dataset;
import guineu.taskcontrol.TaskStatus;
import guineu.util.GUIUtils;

/**
 *
 * @author scsandra
 */
public class PubChemFilterTask implements Task {

        private TaskStatus status = TaskStatus.WAITING;
        private String errorMessage;
        private Dataset dataset;
        private PubChemParameters parameters;

        public PubChemFilterTask(Dataset dataset, PubChemParameters parameters) {
                this.dataset = dataset;
                this.parameters = parameters;
        }

        public String getTaskDescription() {
                return "Filtering files with PubChem ID Filter... ";
        }

        public double getFinishedPercentage() {
                return 1f;
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

        public void run() {
                status = TaskStatus.PROCESSING;
                try {
                        String name = (String) parameters.getParameterValue(PubChemParameters.fileNames);
                        PubChem filter = new PubChem();
                        filter.createCorrector(new File(name));
                        SimpleGCGCDataset alignment = filter.actualMap((SimpleGCGCDataset) dataset);
                        alignment.setDatasetName(alignment.getDatasetName() + (String) parameters.getParameterValue(PubChemParameters.suffix));
                        alignment.setType(DatasetType.GCGCTOF);
                        GUIUtils.showNewTable(alignment);
                        status = TaskStatus.FINISHED;
                } catch (Exception ex) {
                        Logger.getLogger(PubChemFilterTask.class.getName()).log(Level.SEVERE, null, ex);
                        status = TaskStatus.ERROR;
                }
        }
}
