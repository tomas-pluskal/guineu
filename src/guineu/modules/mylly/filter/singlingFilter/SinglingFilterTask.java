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
package guineu.modules.mylly.filter.singlingFilter;

import guineu.data.PeakListRow;
import guineu.data.impl.DatasetType;
import guineu.data.impl.SimpleDataset;
import guineu.data.impl.SimplePeakListRowGCGC;
import guineu.main.GuineuCore;
import guineu.modules.mylly.alignment.scoreAligner.ScoreAlignmentParameters;
import guineu.modules.mylly.alignment.scoreAligner.functions.Alignment;
import guineu.modules.mylly.alignment.scoreAligner.functions.AlignmentRow;
import guineu.modules.mylly.gcgcaligner.datastruct.GCGCDatum;
import guineu.taskcontrol.Task;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author scsandra
 */
public class SinglingFilterTask implements Task {

	private TaskStatus status = TaskStatus.WAITING;
	private String errorMessage;
	private Alignment dataset;
	private SinglingParameters parameters;
	private int ID = 1;

	public SinglingFilterTask(Alignment dataset, SinglingParameters parameters) {
		this.dataset = dataset;
		this.parameters = parameters;
	}

	public String getTaskDescription() {
		return "Filtering files with Leave Only Uniques Filter... ";
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

			double minSimilarity = (Double) parameters.getParameterValue(SinglingParameters.similarity);
			boolean unknownPeaks = (Boolean) parameters.getParameterValue(SinglingParameters.unknownPeaks);

			Singling filter = new Singling(minSimilarity, unknownPeaks);
			Alignment newAlignment = filter.actualMap(dataset);
			newAlignment.setName(newAlignment.toString() + (String) parameters.getParameterValue(SinglingParameters.suffix));
			SimpleDataset newTableOther = this.writeDataset(newAlignment);
			GuineuCore.getDesktop().AddNewFile(newTableOther);
			GuineuCore.getDesktop().AddNewFile(newAlignment);


			status = TaskStatus.FINISHED;
		} catch (Exception ex) {
			Logger.getLogger(SinglingFilterTask.class.getName()).log(Level.SEVERE, null, ex);
			status = TaskStatus.ERROR;
		}
	}

	private SimpleDataset writeDataset(Alignment alignment) {
		SimpleDataset datasetOther = new SimpleDataset(alignment.toString());
		datasetOther.setType(DatasetType.GCGCTOF);
		ScoreAlignmentParameters alignmentParameters = alignment.getParameters();
		boolean concentration = (Boolean) alignmentParameters.getParameterValue(ScoreAlignmentParameters.useConcentration);
		String[] columnsNames = alignment.getColumnNames();
		for (String columnName : columnsNames) {
			datasetOther.AddNameExperiment(columnName);
		}

		for (AlignmentRow gcgcRow : alignment.getAlignment()) {
			datasetOther.AddRow(writeRow(gcgcRow, columnsNames, concentration));
		}
		return datasetOther;
	}

	private PeakListRow writeRow(AlignmentRow gcgcRow, String[] columnsNames, boolean concentration) {
		String allNames = "";
		for (String name : gcgcRow.getNames()) {
			allNames += name + " || ";
		}

		PeakListRow row = new SimplePeakListRowGCGC(ID++, gcgcRow.getMeanRT1(), gcgcRow.getMeanRT2(),
				gcgcRow.getMeanRTI(), gcgcRow.getMaxSimilarity(), gcgcRow.getMeanSimilarity(),
				gcgcRow.getSimilarityStdDev(), ((double) gcgcRow.nonNullPeakCount()),
				gcgcRow.getQuantMass(), gcgcRow.getDistValue().distance(), gcgcRow.getName(),
				allNames, gcgcRow.getSpectrum().toString(), null);
		int cont = 0;
		for (GCGCDatum data : gcgcRow) {
			if (data != null) {
				if (data.getConcentration() > 0 && concentration) {
					row.setPeak(columnsNames[cont++], data.getConcentration());

				} else {
					row.setPeak(columnsNames[cont++], data.getArea());
				}
			} else {
				row.setPeak(columnsNames[cont++], "NA");
			}
		}
		return row;
	}
	
}
