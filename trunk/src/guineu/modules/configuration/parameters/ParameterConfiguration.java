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
package guineu.modules.configuration.parameters;

import guineu.data.Dataset;
import guineu.desktop.Desktop;
import guineu.desktop.GuineuMenu;
import guineu.main.GuineuCore;
import guineu.main.GuineuModule;
import guineu.parameters.ParameterSet;
import guineu.taskcontrol.Task;
import guineu.taskcontrol.TaskListener;
import guineu.taskcontrol.TaskStatus;
import guineu.util.GUIUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

/**
 *
 * @author scsandra
 */
public class ParameterConfiguration implements GuineuModule, TaskListener, ActionListener {

        private Logger logger = Logger.getLogger(this.getClass().getName());
        private Desktop desktop;
        final String helpID = GUIUtils.generateHelpID(this);

        public ParameterConfiguration() {
                this.desktop = GuineuCore.getDesktop();
                desktop.addMenuItem(GuineuMenu.CONFIGURATION, "Parameters Configuration..",
                        "Parameters configuration", KeyEvent.VK_P, this, null, null);
        }

        public void taskStarted(Task task) {
                logger.info("Parameters configuration");
        }

        public void taskFinished(Task task) {
                if (task.getStatus() == TaskStatus.FINISHED) {
                        logger.info("Finished Parameters configuration ");
                }

                if (task.getStatus() == TaskStatus.ERROR) {

                        String msg = "Error while Parameters configuration  .. ";
                        logger.severe(msg);
                        desktop.displayErrorMessage(msg);

                }
        }

        public void actionPerformed(ActionEvent e) {
                Dataset[] dataset = desktop.getSelectedDataFiles();
                if (dataset.length > 0) {
                        ParameterDialog dialog = new ParameterDialog("Data parameters configuration", helpID, dataset[0]);
                        dialog.setVisible(true);
                }
        }
       

        public String toString() {
                return "Parameters configuration";
        }

        public ParameterSet getParameterSet() {
                return null;
        }
}
