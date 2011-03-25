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
package guineu.modules.identification.identificationNeg;

import guineu.data.Dataset;
import guineu.desktop.Desktop;
import guineu.desktop.GuineuMenu;
import guineu.main.GuineuCore;
import guineu.main.GuineuModule;
import guineu.parameters.ParameterSet;
import guineu.taskcontrol.Task;
import guineu.taskcontrol.TaskStatus;

import guineu.taskcontrol.TaskListener;
import guineu.util.internalframe.DataInternalFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;

/**
 *
 * @author scsandra
 */
public class Identification implements GuineuModule, TaskListener, ActionListener {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Desktop desktop;

    public Identification() {

        this.desktop = GuineuCore.getDesktop();
        desktop.addMenuItem(GuineuMenu.IDENTIFICATION, "Identification Negative Ion Mode..",
                "Identification of ESI negative datasets using internal database", KeyEvent.VK_N, this, null, null);

    }

    public void taskStarted(Task task) {
        logger.info("Running Identification negative ion mode");
    }

    public void taskFinished(Task task) {
        if (task.getStatus() == TaskStatus.FINISHED) {
            logger.info("Finished Identification on " + ((IdentificationTask) task).getTaskDescription());
        }

        if (task.getStatus() == TaskStatus.ERROR) {

            String msg = "Error while Identification on .. " + ((IdentificationTask) task).getErrorMessage();
            logger.severe(msg);
            desktop.displayErrorMessage(msg);

        }
    }

    public void actionPerformed(ActionEvent e) {
        runModule();
    }

    public ParameterSet getParameterSet() {
        return null;
    }    

    public String toString() {
        return "Identification";
    }

    public Task[] runModule() {

        // prepare a new group of tasks
        Dataset[] datasets = desktop.getSelectedDataFiles();
        JInternalFrame[] IF = desktop.getInternalFrames();
        if (IF != null) {
            for (JInternalFrame IFn : IF) {
                if (IFn != null && datasets != null && IFn.getTitle().contains(datasets[0].getDatasetName()) && ((DataInternalFrame) IFn).getTable() != null) {
                    Task tasks[] = new IdentificationTask[1];
                    tasks[0] = new IdentificationTask(((DataInternalFrame) IFn).getTable(), datasets[0]);

                    GuineuCore.getTaskController().addTasks(tasks);

                    return tasks;
                } else {
                    return null;
                }
            }
        }
        return null;

    }
}
