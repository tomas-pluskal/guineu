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
package guineu.modules.R;

import guineu.data.Dataset;
import guineu.main.GuineuCore;
import guineu.modules.GuineuModuleCategory;
import guineu.modules.GuineuProcessingModule;
import guineu.parameters.ParameterSet;
import guineu.taskcontrol.Task;
import java.awt.event.ActionEvent;

/**
 *
 * @author scsandra
 */
public class R implements GuineuProcessingModule {

        public static final String MODULE_NAME = "R";

        public ParameterSet getParameterSet() {
                return null;
        }

        @Override
        public String toString() {
                return MODULE_NAME;
        }

        public void actionPerformed(ActionEvent arg0) {
        }

        public Task[] runModule(ParameterSet parameters) {
                Dataset dataset = GuineuCore.getDesktop().getSelectedDataFiles()[0];
                if (dataset != null) {
                        RConsole console = new RConsole(GuineuCore.getDesktop().getMainFrame(), false, dataset);
                        console.setVisible(true);
                }
                return null;
        }

        public GuineuModuleCategory getModuleCategory() {
                return GuineuModuleCategory.R;
        }

        public String getIcon() {
                return null;
        }

        public boolean setSeparator() {
                return false;
        }
}
