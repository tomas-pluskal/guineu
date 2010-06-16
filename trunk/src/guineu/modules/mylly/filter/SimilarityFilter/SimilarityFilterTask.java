/*
 * Copyright 2007-2010 VTT Biotechnology
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
package guineu.modules.mylly.filter.SimilarityFilter;

import guineu.data.datamodels.DatasetGCGCDataModel;
import guineu.data.impl.DatasetType;
import guineu.main.GuineuCore;
import guineu.data.impl.SimpleGCGCDataset;
import guineu.taskcontrol.Task;
import guineu.taskcontrol.TaskStatus;
import guineu.util.Tables.DataTable;
import guineu.util.Tables.DataTableModel;
import guineu.util.Tables.impl.PushableTable;
import guineu.util.internalframe.DataInternalFrame;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author scsandra
 */
public class SimilarityFilterTask implements Task {

	private TaskStatus status = TaskStatus.WAITING;
	private String errorMessage;
	private SimpleGCGCDataset dataset;
	private SimilarityParameters parameters;

	public SimilarityFilterTask(SimpleGCGCDataset dataset, SimilarityParameters parameters) {
		this.dataset = dataset;
		System.out.println(dataset.toString());
		this.parameters = parameters;
	}

	public String getTaskDescription() {
		return "Filtering files with Similarity Filter... ";
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
			double minValue = (Double)parameters.getParameterValue(SimilarityParameters.minSimilarity);
			String typeSimilarity = (String)parameters.getParameterValue(SimilarityParameters.type);
			String mode = Similarity.MEAN_SIMILARITY;
			if(typeSimilarity.matches("maximum similarity")){
				mode = Similarity.MAX_SIMILARITY;
			}

			String typeAction = (String)parameters.getParameterValue(SimilarityParameters.action);
			String action = Similarity.REMOVE;
			if(typeAction.matches("Rename")){
				action = Similarity.RENAME;
			}
			Similarity filter = new Similarity(minValue, action, mode);
			SimpleGCGCDataset newAlignment = filter.actualMap(dataset);
			newAlignment.setDatasetName(newAlignment.toString() + (String) parameters.getParameterValue(SimilarityParameters.suffix));
			newAlignment.setType(DatasetType.GCGCTOF);
			DataTableModel model = new DatasetGCGCDataModel(newAlignment);
            DataTable table = new PushableTable(model);
			table.formatNumbers(newAlignment.getType());
            DataInternalFrame frame = new DataInternalFrame(newAlignment.getDatasetName(), table.getTable(), new Dimension(800, 800));

            GuineuCore.getDesktop().addInternalFrame(frame);
			GuineuCore.getDesktop().AddNewFile(newAlignment);
			status = TaskStatus.FINISHED;
		} catch (Exception ex) {
			Logger.getLogger(SimilarityFilterTask.class.getName()).log(Level.SEVERE, null, ex);
			status = TaskStatus.ERROR;
		}
	}

	
}