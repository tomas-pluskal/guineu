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
package guineu.modules.file.openMassLynxFiles;

import guineu.data.Dataset;
import guineu.data.impl.datasets.SimpleBasicDataset;
import guineu.taskcontrol.AbstractTask;
import guineu.taskcontrol.TaskStatus;
import guineu.util.GUIUtils;

/**
 *
 * @author scsandra
 */
public class OpenFileTask extends AbstractTask {

        private String fileDir;
        
        
        private double progress;

        public OpenFileTask(String fileDir) {
                if (fileDir != null) {
                        this.fileDir = fileDir;
                }
        }

        public String getTaskDescription() {
                return "Opening File... ";
        }

        public double getFinishedPercentage() {
                return progress;
        }
       
        public void cancel() {
                setStatus(TaskStatus.CANCELED);
        }

        public void run() {
                try {
                        this.openFile();
                } catch (Exception e) {
                        setStatus(TaskStatus.ERROR);
                        errorMessage = e.toString();
                        return;
                }
        }

        public void openFile() {
                setStatus(TaskStatus.PROCESSING);
                try {
                        if (getStatus() == TaskStatus.PROCESSING) {
                                LCMSParserMassLynx parser = new LCMSParserMassLynx(fileDir);
                                progress = parser.getProgress();
                                Dataset dataset = (SimpleBasicDataset) parser.getDataset();
                                progress = parser.getProgress();
                                GUIUtils.showNewTable(dataset, true);
                        }
                } catch (Exception ex) {
                }

                progress = 1f;
                setStatus(TaskStatus.FINISHED);
        }
       
}
