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
package guineu.modules.file.saveOtherFile;

import guineu.data.Dataset;
import guineu.desktop.Desktop;
import guineu.main.GuineuCore;
import guineu.main.GuineuModule;
import guineu.parameters.ParameterSet;
import guineu.parameters.SimpleParameterSet;
import guineu.taskcontrol.Task;
import guineu.taskcontrol.TaskStatus;

import guineu.taskcontrol.TaskListener;
import guineu.util.dialogs.ExitCode;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author scsandra
 */
public class SaveOtherFile implements GuineuModule, TaskListener {

        private Logger logger = Logger.getLogger(this.getClass().getName());
        private Desktop desktop;
        private Dataset[] Datasets;
        private SaveOtherParameters parameters;

        public SaveOtherFile(Dataset[] Datasets, ParameterSet parameters) {
                this.Datasets = Datasets;
                this.parameters = (SaveOtherParameters) parameters;
        }

        public SaveOtherFile() {
                parameters = new SaveOtherParameters();
        }

        public void initModule() {
                ExitCode exitCode = this.parameters.showSetupDialog();
                if (exitCode != ExitCode.OK) {
                        return;
                }
                runModule();
        }

        public void taskStarted(Task task) {
                logger.info("Running Save Dataset into File");
        }

        public void taskFinished(Task task) {
                if (task.getStatus() == TaskStatus.FINISHED) {
                        logger.info("Finished Save Data set" + ((SaveOtherFileTask) task).getTaskDescription());
                }

                if (task.getStatus() == TaskStatus.ERROR) {

                        String msg = "Error while save Data set on .. " + ((SaveOtherFileTask) task).getErrorMessage();
                        logger.severe(msg);
                        desktop.displayErrorMessage(msg);

                }
        }

        public ParameterSet getParameterSet() {
                return parameters;
        }

        @Override
        public String toString() {
                return "Save Other Data set";
        }

        public Task[] runModule() {
                try {
                        // prepare a new group of tasks
                        String path = parameters.getParameter(SaveOtherParameters.Otherfilename).getValue().getCanonicalPath();
                        Task[] tasks = new SaveOtherFileTask[Datasets.length];
                        for (int i = 0; i < Datasets.length; i++) {
                                String newpath = path;
                                if (i > 0) {
                                        newpath = path.substring(0, path.length() - 4) + String.valueOf(i) + path.substring(path.length() - 4);
                                }
                                tasks[i] = new SaveOtherFileTask(Datasets[i], parameters, newpath);
                        }
                        GuineuCore.getTaskController().addTasks(tasks);
                        return tasks;
                } catch (IOException ex) {
                        Logger.getLogger(SaveOtherFile.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                }

        }
}
