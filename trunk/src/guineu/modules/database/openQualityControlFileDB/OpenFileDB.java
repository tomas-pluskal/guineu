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
package guineu.modules.database.openQualityControlFileDB;

import guineu.data.Dataset;
import guineu.desktop.Desktop;
import guineu.desktop.GuineuMenu;
import guineu.main.GuineuCore;
import guineu.main.GuineuModule;
import guineu.parameters.ParameterSet;
import guineu.taskcontrol.Task;
import guineu.taskcontrol.TaskListener;
import guineu.taskcontrol.TaskStatus;
import guineu.util.dialogs.ExitCode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

/**
 *
 * @author scsandra
 */
public class OpenFileDB implements GuineuModule, TaskListener, ActionListener {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Desktop desktop;
    DatasetOpenDialog dialog;
    boolean combine = false;
    Dataset[] datasets;

    public void OpenFileDB() {
        this.desktop = GuineuCore.getDesktop();
        desktop.addMenuItem(GuineuMenu.REPORT, "Open database..",
                "Visualize the content of the internal database", KeyEvent.VK_O, this, null, null);
        desktop.addMenuSeparator(GuineuMenu.REPORT);
    }

    public void taskStarted(Task task) {
        logger.info("Running Open Database");
    }

    public void taskFinished(Task task) {
        if (task.getStatus() == TaskStatus.FINISHED) {
            logger.info("Finished open database on " + ((OpenFileDBTask) task).getTaskDescription());
        }

        if (task.getStatus() == TaskStatus.ERROR) {

            String msg = "Error while open database on .. " + ((OpenFileDBTask) task).getErrorMessage();
            logger.severe(msg);
            desktop.displayErrorMessage(msg);

        }
    }

    public void actionPerformed(ActionEvent e) {
        ExitCode exitCode = setupParameters();
        if (exitCode != ExitCode.OK) {
            return;
        }

        runModule();


    }

    public ExitCode setupParameters() {
        dialog = new DatasetOpenDialog();
        dialog.setVisible(true);
        datasets = dialog.getDatasets().toArray(new Dataset[0]);
        return dialog.getExitCode();
    }

    public ParameterSet getParameterSet() {
        return null;
    }

    public void setParameters(ParameterSet parameterValues) {
    }

    @Override
    public String toString() {
        return "Open Database";
    }

    public Task[] runModule() {
        Task tasks[] = null;

        // prepare a new group of tasks
        tasks = new OpenFileDBTask[datasets.length];
        for (int i = 0; i < datasets.length; i++) {
            tasks[i] = new OpenFileDBTask(datasets[i]);
        }

        GuineuCore.getTaskController().addTasks(tasks);
        return tasks;

    }
}
