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
package guineu.modules.filter.commonmolecules;

import guineu.data.Dataset;
import guineu.data.PeakListRow;
import guineu.data.impl.datasets.SimpleBasicDataset;
import guineu.data.impl.peaklists.SimplePeakListRowLCMS;
import guineu.data.impl.peaklists.SimplePeakListRowOther;
import guineu.taskcontrol.AbstractTask;
import guineu.taskcontrol.TaskStatus;
import guineu.util.GUIUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
class CommonMoleculesTask extends AbstractTask {

        private Dataset[] peakLists;
        private String[][] databaseValues;
        private int finishedLines = 0;
        private String dataBaseFile;
        List<lipid> commonNames;

        CommonMoleculesTask(Dataset[] peakList) {
                this.peakLists = peakList;
                commonNames = new ArrayList<lipid>();
        }

        public void cancel() {
                setStatus(TaskStatus.CANCELED);
        }

        public double getFinishedPercentage() {
                if (databaseValues == null) {
                        return 0;
                }
                return ((double) finishedLines) / databaseValues.length;
        }

        public String getTaskDescription() {
                return "Peak identification of " + peakLists + " using database " + dataBaseFile;
        }

        /**
         * @see java.lang.Runnable#run()
         */
        public void run() {

                setStatus(TaskStatus.PROCESSING);

                try {

                        for (Dataset dataset : peakLists) {
                                for (PeakListRow row : dataset.getRows()) {
                                        SimplePeakListRowLCMS Row = (SimplePeakListRowLCMS) row;
                                        lipid mol = new lipid(Row.getName(), Row.getMZ(), Row.getRT());
                                        lipid mol2 = isRepeat(mol);
                                        if (mol2 != null) {
                                                mol2.addDatasetName(dataset.getDatasetName());
                                                mol2.sumApears();
                                        } else {
                                                mol.sumApears();
                                                mol.addDatasetName(dataset.getDatasetName());
                                                commonNames.add(mol);
                                        }
                                }
                        }

                        SimpleBasicDataset dataset = new SimpleBasicDataset("Common peaks");
                        dataset.addColumnName("m/z");
                        dataset.addColumnName("rt");
                        dataset.addColumnName("Molecule Name");
                        dataset.addColumnName("Number of datasets");
                        dataset.addColumnName("Dataset names");

                        for (lipid mol : commonNames) {
                                SimplePeakListRowOther row = new SimplePeakListRowOther();
                                row.setPeak("m/z", String.valueOf(mol.mz));
                                row.setPeak("rt", String.valueOf(mol.rt));
                                row.setPeak("Molecule Name", mol.Name);
                                row.setPeak("Number of datasets", String.valueOf(mol.apears));
                                row.setPeak("Dataset names", String.valueOf(mol.DatasetNames));

                                dataset.addRow(row);
                        }
                        GUIUtils.showNewTable(dataset, true);

                } catch (Exception e) {
                        setStatus(TaskStatus.ERROR);
                        errorMessage = e.toString();
                        return;
                }


                setStatus(TaskStatus.FINISHED);

        }

        private lipid isRepeat(lipid mol) {
                for (lipid mol2 : commonNames) {
                        double mzdiff = Math.abs(mol2.mz - mol.mz);
                        double rtdiff = Math.abs(mol2.rt - mol.rt);
                        if (mol2.Name.equals(mol.Name) && mzdiff < 0.05 && rtdiff < 4) {
                                return mol2;
                        }
                }
                return null;
        }

        class lipid {

                String Name;
                double mz;
                double rt;
                int apears;
                String DatasetNames = "";

                public lipid(String name, double mz, double rt) {
                        this.Name = name;
                        this.mz = mz;
                        this.rt = rt;
                        apears = 0;
                }

                public void sumApears() {
                        apears++;
                }

                public void addDatasetName(String name) {
                        DatasetNames += " / " + name;
                }
        }
}
