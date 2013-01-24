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
package guineu.modules.dataanalysis.anova;

import guineu.data.Dataset;
import guineu.main.GuineuCore;
import guineu.modules.GuineuModuleCategory;
import guineu.modules.GuineuProcessingModule;
import guineu.parameters.ParameterSet;
import guineu.taskcontrol.Task;
import guineu.util.dialogs.ExitCode;

/**
 *
 * @author scsandra
 */
public class AnovaTestModule implements GuineuProcessingModule {

        public static final String MODULE_NAME = "Anova Test";
        private AnovaParameters parameters;

        public ParameterSet getParameterSet() {
                return this.parameters;
        }

        public String toString() {
                return MODULE_NAME;
        }

        public Task[] runModule(ParameterSet parameters) {
                Dataset[] dataFiles = GuineuCore.getDesktop().getSelectedDataFiles();
                if (dataFiles != null && dataFiles[0].getParametersName().size() > 0) {
                        String[] parameterList = GuineuCore.getDesktop().getSelectedDataFiles()[0].getParametersName().toArray(new String[0]);
                        parameters = new AnovaParameters(parameterList);
                        ExitCode exitCode = parameters.showSetupDialog();
                        if (exitCode == ExitCode.OK) {
                                Dataset[] DataFiles = GuineuCore.getDesktop().getSelectedDataFiles();
                                // prepare a new group of tasks
                                Task tasks[] = new AnovaTestTask[1];
                                tasks[0] = new AnovaTestTask(DataFiles[0], (AnovaParameters) parameters);
                                GuineuCore.getTaskController().addTasks(tasks);

                                return tasks;
                        }
                }
                return null;
        }

        public GuineuModuleCategory getModuleCategory() {
                return GuineuModuleCategory.DATAANALYSIS;
        }

        public String getIcon() {
                return "icons/anova.png";
        }

        public boolean setSeparator() {
                return false;
        }
}
