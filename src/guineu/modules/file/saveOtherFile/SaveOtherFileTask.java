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
package guineu.modules.file.saveOtherFile;

import guineu.data.Dataset;
import guineu.database.intro.InDataBase;
import guineu.database.intro.impl.InOracle;
import guineu.parameters.ParameterSet;
import guineu.parameters.SimpleParameterSet;
import guineu.taskcontrol.AbstractTask;
import guineu.taskcontrol.TaskStatus;

/**
 *
 * @author scsandra
 */
public class SaveOtherFileTask extends AbstractTask {

        private Dataset dataset;
        private String path;
        private InDataBase db;
        private ParameterSet parameters;

        public SaveOtherFileTask(Dataset dataset, ParameterSet parameters, String path) {
                this.dataset = dataset;
                this.path = path;
                this.parameters = parameters;
                db = new InOracle();
        }

        public String getTaskDescription() {
                return "Saving Dataset... ";
        }

        public double getFinishedPercentage() {
                return db.getProgress();
        }

        public void cancel() {
                setStatus(TaskStatus.CANCELED);
        }

        public void run() {
                try {
                        setStatus(TaskStatus.PROCESSING);
                        if (parameters.getParameter(SaveOtherParameters.type).getValue().matches(".*Excel.*")) {
                                db.WriteExcelFile(dataset, path, (SimpleParameterSet) parameters);
                        } else {
                                db.WriteCommaSeparatedFile(dataset, path, (SimpleParameterSet) parameters);
                        }

                        setStatus(TaskStatus.FINISHED);
                } catch (Exception e) {
                        setStatus(TaskStatus.ERROR);
                }
        }
}
