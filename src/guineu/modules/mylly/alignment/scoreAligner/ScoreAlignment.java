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
package guineu.modules.mylly.alignment.scoreAligner;

import guineu.desktop.Desktop;
import guineu.main.GuineuCore;
import guineu.modules.GuineuModuleCategory;
import guineu.taskcontrol.Task;
import guineu.taskcontrol.TaskEvent;
import guineu.taskcontrol.TaskStatus;
import guineu.util.dialogs.ExitCode;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import guineu.data.Dataset;
import guineu.data.impl.datasets.SimpleGCGCDataset;
import guineu.modules.GuineuProcessingModule;
import guineu.modules.mylly.datastruct.GCGCData;
import guineu.modules.mylly.datastruct.GCGCDatum;
import guineu.parameters.ParameterSet;
import guineu.util.GUIUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author scsandra
 */
public class ScoreAlignment implements GuineuProcessingModule {

        private Logger logger = Logger.getLogger(this.getClass().getName());
        private Desktop desktop;
        private ScoreAlignmentParameters parameters;
        final String helpID = GUIUtils.generateHelpID(this);

     /*   public ScoreAlignment() {
                parameters = new ScoreAlignmentParameters();
                this.desktop = GuineuCore.getDesktop();
                desktop.addMenuItem(GuineuMenu.MYLLY, "Score Alignment..",
                        "Alignment algorithm based on RT and spectra similarity", KeyEvent.VK_S, this, null, "icons/alignment.png");

        }*/

        public void taskStarted(Task task) {
                logger.info("Running Score Alignment");
        }

        public void taskFinished(Task task) {
                if (task.getStatus() == TaskStatus.FINISHED) {
                        logger.info("Finished Score Alignment on " + ((ScoreAlignmentTask) task).getTaskDescription());
                }

                if (task.getStatus() == TaskStatus.ERROR) {

                        String msg = "Error while Score Alignment on .. " + ((ScoreAlignmentTask) task).getErrorMessage();
                        logger.severe(msg);
                        desktop.displayErrorMessage(msg);

                }
        }

        public void actionPerformed(ActionEvent e) {
                try {
                        ExitCode exitCode = parameters.showSetupDialog();
                        if (exitCode == ExitCode.OK) {
                                runModule();
                        }
                } catch (Exception exception) {
                }
        }


        public ParameterSet getParameterSet() {
                return this.parameters;
        }

        public String toString() {
                return "Score Alignment";
        }

        public Task[] runModule() {
                // prepare a new group of tasks
                Task tasks[] = new ScoreAlignmentTask[1];
                Dataset[] datasets = desktop.getSelectedDataFiles();
                List<GCGCData> newDatasets = new ArrayList<GCGCData>();

                for (int i = 0; i < datasets.length; i++) {
                        GCGCDatum[][] datum = ((SimpleGCGCDataset) datasets[i]).toArray();
                        List<GCGCDatum> datumList = Arrays.asList(datum[0]);
                        newDatasets.add(new GCGCData(datumList, datumList.get(0).getColumnName()));
                }

                tasks[0] = new ScoreAlignmentTask(newDatasets, parameters);

                GuineuCore.getTaskController().addTasks(tasks);

                return tasks;
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
