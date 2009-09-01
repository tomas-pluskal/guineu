/*
 * Copyright 2007-2008 VTT Biotechnology
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
package guineu.modules.mylly.filter.NameFilter.pre;

import guineu.data.impl.DatasetType;
import guineu.data.impl.SimpleGCGCDataset;
import guineu.data.impl.SimplePeakListRowGCGC;
import guineu.main.GuineuCore;
import guineu.modules.mylly.filter.NameFilter.NameFilterModule;
import guineu.modules.mylly.filter.NameFilter.NameFilterParameters;
import guineu.modules.mylly.gcgcaligner.datastruct.GCGCData;
import guineu.modules.mylly.gcgcaligner.datastruct.GCGCDatum;
import guineu.taskcontrol.Task;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author scsandra
 */
public class NamePreFilterTask implements Task {

	private TaskStatus status = TaskStatus.WAITING;
	private String errorMessage;
	private List<GCGCData> datasets;
	private NameFilterParameters parameters;	

	public NamePreFilterTask(List<GCGCData> datasets, NameFilterParameters parameters) {
		this.datasets = datasets;
		this.parameters = parameters;
	}

	public String getTaskDescription() {
		return "Filtering files with Name Preprocessor Filter... ";
	}

	public double getFinishedPercentage() {
		return 1f;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void cancel() {
		status = TaskStatus.CANCELED;
	}

	public void run() {
		status = TaskStatus.PROCESSING;
		try {
			NameFilterModule filter = new NameFilterModule();
			String names = (String) parameters.getParameterValue(NameFilterParameters.fileNames);
			filter.generateNewFilter(this.askParameters(names));
			List<GCGCData> newDatasets = filter.actualMap(datasets);

			for (GCGCData dates : newDatasets) {
				dates.setName(dates.getName() + (String) parameters.getParameterValue(NameFilterParameters.suffix));
				SimpleGCGCDataset newTableOther = this.writeDataset(dates.toList(), dates.getName());
				GuineuCore.getDesktop().AddNewFile(newTableOther);
			}

			status = TaskStatus.FINISHED;
		} catch (Exception ex) {
			Logger.getLogger(NamePreFilterTask.class.getName()).log(Level.SEVERE, null, ex);
			status = TaskStatus.ERROR;
		}
	}

	private SimpleGCGCDataset writeDataset(List<GCGCDatum> data, String name) {

		SimpleGCGCDataset dataset = new SimpleGCGCDataset(name);
		dataset.AddNameExperiment(name);
		dataset.setType(DatasetType.GCGCTOF);

		for (GCGCDatum mol : data) {
			SimplePeakListRowGCGC row = new SimplePeakListRowGCGC((int) mol.getId(), mol.getRT1(), mol.getRT2(), mol.getRTI(),
					mol.getSimilarity(), 0, 0, 0, mol.getQuantMass(), 0, mol.getName(),
					null, mol.getSpectrum().toString(), null, mol.getCAS());


			GCGCDatum[] peaks = new GCGCDatum[1];
			mol.setColumnName(name);
			peaks[0] = mol;
			row.setDatum(peaks);
			dataset.addAlignmentRow(row);
		}

		return dataset;
	}

	public List<String> askParameters(String names) {
		File f = new File(names);
		if (f != null) {
			try {
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String line = null;
				List<String> namesList = new ArrayList<String>();
				while ((line = (br.readLine())) != null) {
					namesList.add(line);
				}
				return namesList;
			} catch (FileNotFoundException e) {
				return null;
			//	GCGCAlign.getMainWindow().displayErrorDialog("File " + f + " was not found");
			} catch (IOException e) {
				return null;
			//	GCGCAlign.getMainWindow().displayErrorDialog(e);
			}
		}
		return null;
	}
}
