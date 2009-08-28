/*
Copyright 2006-2007 VTT Biotechnology

This file is part of MYLLY.

MYLLY is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

MYLLY is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with MYLLY; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package guineu.data.impl;


import guineu.modules.mylly.alignment.scoreAligner.functions.*;
import guineu.modules.mylly.alignment.scoreAligner.ScoreAlignmentParameters;
import guineu.modules.mylly.alignment.scoreAligner.functions.AlignmentSorterFactory.SORT_MODE;
import guineu.modules.mylly.gcgcaligner.datastruct.GCGCDatum;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Collections;
import guineu.data.Dataset;
import guineu.data.PeakListRow;
import java.util.Vector;
/**
 * @author jmjarkko
 */
public class SimpleGCGCDataset implements Dataset{

	private static long nextId = 1;
	private List<SimplePeakListRowGCGC> alignment;
	private Vector<String> names;
	private AlignmentSorterFactory.SORT_MODE lastSortMode;
	private ScoreAlignmentParameters params;
	private Aligner aligner;
	private final long id;
	private String name;
	private DatasetType type;

	private static synchronized long getNextId() {
		return nextId++;
	}

	public SimpleGCGCDataset(String[] names, ScoreAlignmentParameters parameters, Aligner aligner) {
		this.names = new Vector<String>();
		for(String experimentName : names){
			this.names.addElement(experimentName);
		}
		alignment = new ArrayList<SimplePeakListRowGCGC>();
		lastSortMode = SORT_MODE.none;
		this.params = parameters;
		this.aligner = aligner;
		id = getNextId();
		name =  "Alignment " + id;
	}

	public SimpleGCGCDataset(SimpleGCGCDataset other) {
		this((String[])other.names.toArray(new String[0]), other.params, other.aligner);
		alignment.addAll(other.alignment);
		this.lastSortMode = other.lastSortMode;

	}

	public SimpleGCGCDataset(String datasetName) {
		this.names = new Vector<String>();
		this.names.addElement(datasetName);
		alignment = new ArrayList<SimplePeakListRowGCGC>();
		id = 0;
		this.name = datasetName;
	}

	public void setGCGCDataConcentration() {
		if ((Boolean) params.getParameterValue(ScoreAlignmentParameters.useConcentration)) {
			for (SimplePeakListRowGCGC row : alignment) {
				for (GCGCDatum data : row) {
					if (data.getConcentration() > 0) {
						data.setUseConcentration(true);
					} else {
						data.setUseConcentration(false);
					}
				}
			}
		} else {
			for (SimplePeakListRowGCGC row : alignment) {
				for (GCGCDatum data : row) {
					data.setUseConcentration(false);
				}
			}
		}
	}

	public void sort(SORT_MODE mode) {
		if (lastSortMode != mode && mode != AlignmentSorterFactory.SORT_MODE.none) {
			Collections.sort(alignment, AlignmentSorterFactory.getComparator(mode));
			lastSortMode = mode;
		}
	}

	/**
	 * @return a list containing the AlignmentRows in this Alignment
	 */
	public List<SimplePeakListRowGCGC> getAlignment() {
		return new ArrayList<SimplePeakListRowGCGC>(alignment);
	}

	public GCGCDatum[][] toArray() {
		GCGCDatum tempArray[][] = new GCGCDatum[rowCount()][];
		GCGCDatum returnedArray[][] = new GCGCDatum[colCount()][rowCount()];
		for (int i = 0; i < rowCount(); i++) {
			tempArray[i] = alignment.get(i).getRow();
		}
		for (int i = 0; i < rowCount(); i++) {
			for (int j = 0; j < colCount(); j++) {
				returnedArray[j][i] = tempArray[i][j];
			}
		}

		return returnedArray;
	}

	public List<SimplePeakListRowGCGC> getSortedAlignment(SORT_MODE mode) {
		sort(mode);
		return getAlignment();
	}

	public ScoreAlignmentParameters getParameters() {
		return params;
	}

	public Aligner getAligner() {
		return aligner;
	}

	public void SaveToFile(File f, String separator) throws IOException {
		SaveToFile(f, separator, SORT_MODE.quantMass);
	}

	public boolean containsMainPeaks() {
		boolean contains = false;
		for (SimplePeakListRowGCGC row : alignment) {
			if (!row.getDistValue().isNull()) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	public void SaveToFile(File f, String separator, SORT_MODE mode) throws IOException {
		sort(mode);
		boolean containsMainPeaks = containsMainPeaks();

		NumberFormat formatter = new DecimalFormat("####.####");
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
		out.write("Aligner" + separator + aligner.getName() + "\n");
		out.write("\n\n");
		out.write("RT1");
		out.write(separator);
		out.write("RT2");
		out.write(separator);
		out.write("RI");
		out.write(separator);
		out.write("Quant Mass");
		out.write(separator);
		out.write("Num Found");
		out.write(separator);
		if (containsMainPeaks) {
			out.write("Difference to ideal peak");
			out.write(separator);
		}
		out.write("CAS");
		out.write(separator);
		out.write("Name");
		out.write(separator);
		out.write("All names");
		out.write(separator);
		out.write("Max similarity");
		out.write(separator);
		out.write("Mean similarity");
		out.write(separator);
		out.write("Similarity std dev");
		for (String st : names) {
			out.write(separator);
			out.write(st + " Area");
		}
		out.write(separator);
		out.write("Spectrum");
		out.write('\n');
		for (SimplePeakListRowGCGC row : alignment) {
			StringBuilder sb = new StringBuilder();
			sb.append(formatter.format(row.getRT1())).append(separator);
			sb.append(formatter.format(row.getRT2())).append(separator);
			sb.append(formatter.format(row.getRTI())).append(separator);
			sb.append(formatter.format(row.getMass())).append(separator);
			sb.append(row.nonNullPeakCount()).append(separator);
			if (containsMainPeaks) {
				if (!row.getDistValue().isNull()) {
					sb.append(formatter.format(row.getDistValue().distance()));
				} else {
				}
				sb.append(separator);
			}
			String CAS = row.getCAS();
			sb.append(CAS).append(separator);
			//Now write the names
			String name = row.getName();
			sb.append(name).append(separator);
			if (row.getNames().length > 0) {
				sb.append('"');
				for (int i = 0; i < row.getNames().length; i++) {
					sb.append(row.getNames()[i]);
					if (i != row.getNames().length - 1) {
						sb.append(" || ");
					}
				}
				sb.append('"');
			} else {
				sb.append(row.getName());
			}
			sb.append(separator);

			sb.append(formatter.format(row.getMaxSimilarity())).append(separator);
			sb.append(formatter.format(row.getMeanSimilarity())).append(separator);
			sb.append(formatter.format(row.getSimilaritySTDDev())).append(separator);
			for (GCGCDatum d : row) {
				if (d != null) {
					if (d.getConcentration() > 0 && (Boolean) params.getParameterValue(ScoreAlignmentParameters.useConcentration)) {
						sb.append(formatter.format(d.getConcentration()));

					} else {
						sb.append(formatter.format(d.getArea()));
					}
				} else {
					sb.append("NA");
				}
				sb.append(separator);
			}
			if (row.getSpectrumString() != null) {
				sb.append(row.getSpectrumString().toString());
			}
			out.write(sb.toString());
			out.write('\n');
		}
		out.close();
	}

	public List<SimplePeakListRowGCGC> getQuantMassAlignments() {
		List<SimplePeakListRowGCGC> QuantMassList = new ArrayList<SimplePeakListRowGCGC>();
		for (int i = 0; i < alignment.size(); i++) {
			SimplePeakListRowGCGC alignmentRow = alignment.get(i);
			if (alignmentRow.getMass() > -1) {
				QuantMassList.add(alignmentRow);
			}
		}
		return QuantMassList;
	}

	public GCGCDatum getPeak(int rowIx, int colIx) {
		if (rowIx < 0 || rowIx >= rowCount() || colIx < 0 || colIx >= colCount()) {
			throw new IndexOutOfBoundsException("indices out of bounds: rowIx = " +
					rowIx + " valid range [0," + rowCount() + "]" +
					" colIx = " + colIx + " valid range [0," + colCount() +
					"]");
		}
		return alignment.get(rowIx).getRow()[colIx];
	}

	public int colCount() {
		return names.size();
	}

	public int rowCount() {
		return alignment.size();
	}

	public String[] getColumnNames() {
		return (String[])names.toArray(new String[0]).clone();
	}

	public boolean addAlignmentRow(SimplePeakListRowGCGC row) {
		return alignment.add(row);
	}

	public void addAll(Collection<? extends SimplePeakListRowGCGC> rows) {
		for (SimplePeakListRowGCGC r : rows) {
			addAlignmentRow(r);
		}
	}

	@Override
	public int hashCode() {
		return (int) ((id % 0xFFFFFFFFL) + Integer.MIN_VALUE);
	}

	public boolean equals(Object o) {
		if (o instanceof SimpleGCGCDataset) {
			return ((SimpleGCGCDataset) o).id == id;
		}
		return false;
	}

	public String toString() {
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getDatasetName() {
		return name;
	}

	public Vector<String> getNameExperiments() {	
		return names;
	}
	public int getNumberCols() {
		return names.size();
	}

	public int getNumberRows() {
		return this.rowCount();
	}

	public void setDatasetName(String name) {
		this.name = name;
	}

	public DatasetType getType() {
		return this.type;
	}

	public void setType(DatasetType type) {
		this.type = type;
	}

	public PeakListRow getRow(int row) {
		return this.alignment.get(row);
	}

	public void removeRow(PeakListRow row) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void AddNameExperiment(String nameExperiment) {
		this.names.add(nameExperiment);
	}

	public void AddNameExperiment(String nameExperiment, int position) {
		 this.names.insertElementAt(nameExperiment, position);
	}

	public Vector<PeakListRow> getRows() {
		return (Vector<PeakListRow>) this.alignment.iterator();
	}
}