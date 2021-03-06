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
package guineu.modules.filter.Alignment.SerumHuNormalization;

import guineu.data.Dataset;
import guineu.main.GuineuCore;
import guineu.modules.GuineuModuleCategory;
import guineu.modules.GuineuProcessingModule;
import guineu.parameters.ParameterSet;
import guineu.taskcontrol.Task;
import guineu.util.dialogs.ExitCode;

public class SerumHuNormalizationModule implements GuineuProcessingModule {

        public static final String MODULE_NAME = "Control samples Normalization";
        private SerumHuNormalizationParameters parameters;

        public String toString() {
                return MODULE_NAME;
        }

        public ParameterSet getParameterSet() {
                return parameters;
        }

        public Task[] runModule(ParameterSet parameters) {
                Dataset[] peakLists = GuineuCore.getDesktop().getSelectedDataFiles();
                parameters = new SerumHuNormalizationParameters();
                ExitCode exitCode = parameters.showSetupDialog();
                if (exitCode == ExitCode.OK) {
                        // check peak lists
                        if ((peakLists == null) || (peakLists.length == 0)) {
                                GuineuCore.getDesktop().displayErrorMessage("Please select peak lists for Human Serum Normalization");
                                return null;
                        }

                        // prepare a new group with just one task
                        Task task = new SerumHuNormalizationTask(peakLists, parameters);

                        GuineuCore.getTaskController().addTask(task);
                        return new Task[]{task};
                }
                return null;

        }

        public GuineuModuleCategory getModuleCategory() {
                return GuineuModuleCategory.NORMALIZATION;
        }

        public String getIcon() {
                return null;
        }

        public boolean setSeparator() {
                return false;
        }
}
