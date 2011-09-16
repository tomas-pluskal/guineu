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
package guineu.modules.database.saveDatasetDB;

import guineu.data.Dataset;
import guineu.desktop.Desktop;
import guineu.main.GuineuCore;
import guineu.modules.GuineuModuleCategory;
import guineu.modules.GuineuProcessingModule;
import guineu.parameters.ParameterSet;
import guineu.taskcontrol.Task;
import guineu.taskcontrol.TaskEvent;
import guineu.taskcontrol.TaskStatus;
import guineu.util.dialogs.ExitCode;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

/**
 *
 * @author scsandra
 */
public class SaveFileDB implements GuineuProcessingModule {

        private Logger logger = Logger.getLogger(this.getClass().getName());
        private Desktop desktop;
        private Dataset Dataset;
        private SaveFileParameters parameters;

    /*    public SaveFileDB() {
                this.desktop = GuineuCore.getDesktop();
                desktop.addMenuItem(GuineuMenu.DATABASE, "Save Dataset..",
                        "Save Dataset into the internal database", KeyEvent.VK_I, this, null, null);
                parameters = new SaveFileParameters();
        }*/

        public void taskStarted(Task task) {
                logger.info("Running Save Dataset into Database");
        }

        public void taskFinished(Task task) {
                if (task.getStatus() == TaskStatus.FINISHED) {
                        logger.info("Finished Save Dataset" + ((SaveFileDBTask) task).getTaskDescription());
                }

                if (task.getStatus() == TaskStatus.ERROR) {

                        String msg = "Error while save Dataset on .. " + ((SaveFileDBTask) task).getErrorMessage();
                        logger.severe(msg);
                        desktop.displayErrorMessage(msg);

                }
        }

        public void setupParameters(ParameterSet currentParameters) {
                ExitCode exitCode = currentParameters.showSetupDialog();
                if (exitCode == ExitCode.OK) {
                        runModule();
                }
        }

        public ParameterSet getParameterSet() {
                return parameters;
        }

        @Override
        public String toString() {
                return "Save Dataset";
        }

        public Task[] runModule() {

                // prepare a new group of tasks
                Task tasks[] = new SaveFileDBTask[1];

                tasks[0] = new SaveFileDBTask(Dataset, parameters);

                GuineuCore.getTaskController().addTasks(tasks);

                return tasks;

        }

        public void actionPerformed(ActionEvent e) {

                try {
                        Dataset = desktop.getSelectedDataFiles()[0];

                        if (Dataset.getDatasetName() != null) {
                                //parameters..setParameterValue(SaveFileParameters.name, Dataset.getDatasetName());
                                setupParameters(parameters);
                        }
                } catch (Exception exception) {
                }
        }

        public void statusChanged(TaskEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
        }

        public Task[] runModule(ParameterSet parameters) {
                throw new UnsupportedOperationException("Not supported yet.");
        }

        public GuineuModuleCategory getModuleCategory() {
                throw new UnsupportedOperationException("Not supported yet.");
        }
}

