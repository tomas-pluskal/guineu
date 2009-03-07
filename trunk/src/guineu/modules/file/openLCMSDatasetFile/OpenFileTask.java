/*
Copyright 2007-2008 VTT Biotechnology
This file is part of GUINEU.
 */
package guineu.modules.file.openLCMSDatasetFile;

import guineu.data.datamodels.DatasetDataModel;
import guineu.data.parser.impl.LCMSParserCSV;
import guineu.data.parser.impl.LCMSParserXLS;
import guineu.data.impl.SimpleDataset;
import guineu.data.parser.Parser;
import guineu.desktop.Desktop;
import guineu.taskcontrol.Task;
import guineu.util.Tables.DataTable;
import guineu.util.Tables.DataTableModel;
import guineu.util.Tables.impl.PushableTable;
import guineu.util.internalframe.DataInternalFrame;
import java.awt.Dimension;
import java.io.IOException;

/**
 *
 * @author scsandra
 */
public class OpenFileTask implements Task {

    private String fileDir;
    private TaskStatus status = TaskStatus.WAITING;
    private String errorMessage;
    private Desktop desktop;
    private double progress;

    public OpenFileTask(String fileDir, Desktop desktop) {
        if (fileDir != null) {
            this.fileDir = fileDir;
        }
        this.desktop = desktop;
    }

    public String getTaskDescription() {
        return "Opening File... ";
    }

    public double getFinishedPercentage() {
        return progress;
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
        try {
            this.openFile();
        } catch (Exception e) {
            status = TaskStatus.ERROR;
            errorMessage = e.toString();
            return;
        }
    }

    public void openFile() {
        status = TaskStatus.PROCESSING;
        Parser parser;
        if (fileDir.matches(".*xls")) {
            try {
                parser = new LCMSParserXLS(fileDir, null);
                String[] sheetsNames = ((LCMSParserXLS)parser).getSheetNames(fileDir);
                for (String Name : sheetsNames) {
                    try {
                         parser = new LCMSParserXLS(fileDir, Name);
                        this.open(parser);
                    } catch (Exception exception) {
                        // exception.printStackTrace();
                    }
                }
            } catch (IOException ex) {
            }
        } else if (fileDir.matches(".*csv")) {
            try {
                if (status == TaskStatus.PROCESSING) {
                    parser = new LCMSParserCSV(fileDir);
                    this.open(parser);
                }
            } catch (Exception ex) {
            }
        }
        progress = 1f;
        status = TaskStatus.FINISHED;
    }

    public void open(Parser parser) {
        try {
            if (status == TaskStatus.PROCESSING) {
                SimpleDataset dataset = (SimpleDataset) parser.getDataset();
                // progress += parser.getProgress() / sheetsNames.length;
                desktop.AddNewFile(dataset);

                //creates internal frame with the table
                DataTableModel model = new DatasetDataModel(dataset);
                DataTable table = new PushableTable(model);
                table.formatNumbers(dataset.getType());
                DataInternalFrame frame = new DataInternalFrame(dataset.getDatasetName(), table.getTable(), new Dimension(800, 800));

                desktop.addInternalFrame(frame);
                frame.setVisible(true);
            }
        } catch (Exception exception) {
            // exception.printStackTrace();
        }
    }
}
