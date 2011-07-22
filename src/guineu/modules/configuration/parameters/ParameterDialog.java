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

import com.csvreader.CsvReader;
import guineu.data.Dataset;
import guineu.modules.identification.normalizationtissue.readFileDialog;
import guineu.util.components.HelpButton;
import guineu.util.dialogs.ExitCode;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

/**
 * Dialog to configure the parameters of the samples
 *
 * @author scsandra
 */
public class ParameterDialog extends JDialog implements ActionListener {

        private JButton btnHelp;
        private ExitCode exitCode = ExitCode.UNKNOWN;
        private ParameterDataModel model;
        private Clipboard system;
        private StringSelection stsel;
        private String rowstring, value;
        private Vector<register> registers;
        private Dataset dataset;
        int indexRegister = 0;

        /** Creates new form ParameterDialog */
        public ParameterDialog(String title, String helpID, Dataset dataset) {
                registers = new Vector<register>();
                this.dataset = dataset;

                initComponents();

                this.cancelButton.addActionListener(this);
                this.addParameterButton.addActionListener(this);
                this.saveButton.addActionListener(this);
                this.deleteParameterButton.addActionListener(this);

                // The first column is a list of samples taken from the data set
                if (dataset != null) {
                        model = new ParameterDataModel(dataset, table);
                        this.table.setModel(model);
                }

                // Table properties
                setTableProperties();

                // Help button
                btnHelp = new HelpButton(helpID);
                btnPanel.add(btnHelp);


        }

        /** This method is called from within the constructor to
         * initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {
                java.awt.GridBagConstraints gridBagConstraints;

                jPanel3 = new javax.swing.JPanel();
                jLabel2 = new javax.swing.JLabel();
                jScrollPane1 = new javax.swing.JScrollPane();
                table = new javax.swing.JTable();
                btnPanel = new javax.swing.JPanel();
                deleteParameterButton = new javax.swing.JButton();
                loadFileButton = new javax.swing.JButton();
                jPanel1 = new javax.swing.JPanel();
                saveButton = new javax.swing.JButton();
                cancelButton = new javax.swing.JButton();
                jPanel2 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                parameterNameTF = new javax.swing.JTextField();
                addParameterButton = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                setMinimumSize(new java.awt.Dimension(700, 500));

                jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.PAGE_AXIS));

                jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel2.setText("Parameters list:");
                jPanel3.add(jLabel2);

                jScrollPane1.setPreferredSize(new java.awt.Dimension(800, 502));

                table.setAutoCreateRowSorter(true);
                table.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {
                                {null, null, null, null},
                                {null, null, null, null},
                                {null, null, null, null},
                                {null, null, null, null}
                        },
                        new String [] {
                                "Title 1", "Title 2", "Title 3", "Title 4"
                        }
                ));
                table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
                table.setFillsViewportHeight(true);
                jScrollPane1.setViewportView(table);

                jPanel3.add(jScrollPane1);

                btnPanel.setPreferredSize(new java.awt.Dimension(699, 100));
                btnPanel.setLayout(new java.awt.GridBagLayout());

                deleteParameterButton.setText("Delete selected parameter");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 1;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                btnPanel.add(deleteParameterButton, gridBagConstraints);

                loadFileButton.setText("Load parameter file");
                loadFileButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                loadFileButtonActionPerformed(evt);
                        }
                });
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 2;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                btnPanel.add(loadFileButton, gridBagConstraints);

                saveButton.setText("Save");
                jPanel1.add(saveButton);

                cancelButton.setText("Cancel");
                jPanel1.add(cancelButton);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 1;
                gridBagConstraints.gridy = 2;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                btnPanel.add(jPanel1, gridBagConstraints);

                jLabel1.setText("Parameter name: ");
                jPanel2.add(jLabel1);

                parameterNameTF.setMinimumSize(new java.awt.Dimension(100, 28));
                parameterNameTF.setPreferredSize(new java.awt.Dimension(100, 28));
                jPanel2.add(parameterNameTF);

                addParameterButton.setText("Add parameter");
                jPanel2.add(addParameterButton);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                btnPanel.add(jPanel2, gridBagConstraints);

                jPanel3.add(btnPanel);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void loadFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadFileButtonActionPerformed
                openSelectionFile();
        }//GEN-LAST:event_loadFileButtonActionPerformed
        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton addParameterButton;
        private javax.swing.JPanel btnPanel;
        private javax.swing.JButton cancelButton;
        private javax.swing.JButton deleteParameterButton;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JButton loadFileButton;
        private javax.swing.JTextField parameterNameTF;
        private javax.swing.JButton saveButton;
        private javax.swing.JTable table;
        // End of variables declaration//GEN-END:variables

        public void actionPerformed(ActionEvent e) {

                Object src = e.getSource();
                this.setPreferredSize(new Dimension(700, 500));
                // Adding the new parameters to the dataset configuration and closing the dialog.
                if (src == saveButton) {
                        exitCode = ExitCode.OK;
                        model.addParameters(dataset);
                        dispose();
                }

                // Closing the dialog.
                if (src == cancelButton) {
                        exitCode = ExitCode.CANCEL;
                        dispose();
                }

                // Adds a new column to the table with the string writen in the textField as a column name.
                if (src == addParameterButton) {
                        if (!tableContains(parameterNameTF.getText())) {
                                model.addColumn(parameterNameTF.getText());
                                table.createDefaultColumnsFromModel();

                                // Size of the firts column which corresponds to the sample list column
                                table.getColumnModel().getColumn(0).setMinWidth(300);
                        }
                }

                // Deletes the first selected column.
                if (src == deleteParameterButton) {
                        model.addParameters(dataset);
                        dataset.deleteParameter(table.getColumnName(table.getSelectedColumn()));
                        model = new ParameterDataModel(dataset, table);
                        table.setModel(model);

                        // Size of the firts column which corresponds to the sample list column
                        table.getColumnModel().getColumn(0).setMinWidth(300);

                        table.validate();
                }



                //table behavior

                // Copy
                if (e.getActionCommand().compareTo("Copy") == 0) {
                        StringBuilder sbf = new StringBuilder();
                        // Checks to ensure we have selected only a contiguous block of cells
                        int numcols = table.getSelectedColumnCount();
                        int numrows = table.getSelectedRowCount();
                        int[] rowsselected = table.getSelectedRows();
                        int[] colsselected = table.getSelectedColumns();
                        if (!((numrows - 1 == rowsselected[rowsselected.length - 1] - rowsselected[0]
                                && numrows == rowsselected.length)
                                && (numcols - 1 == colsselected[colsselected.length - 1] - colsselected[0]
                                && numcols == colsselected.length))) {
                                JOptionPane.showMessageDialog(null, "Invalid Copy Selection",
                                        "Invalid Copy Selection",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                        }
                        for (int i = 0; i < numrows; i++) {
                                for (int j = 0; j < numcols; j++) {
                                        String str = (String) table.getValueAt(rowsselected[i], colsselected[j]);
                                        // If the cell is empty it will add "ç" character as a mark
                                        if (str.isEmpty()) {
                                                sbf.append("ç");
                                        } else {
                                                sbf.append(str);
                                        }
                                        if (j < numcols - 1) {
                                                sbf.append("\t");
                                        }
                                }
                                sbf.append("\n");
                        }
                        stsel = new StringSelection(sbf.toString());
                        system = Toolkit.getDefaultToolkit().getSystemClipboard();
                        system.setContents(stsel, stsel);
                        table.validate();
                }

                // Paste
                if (e.getActionCommand().compareTo("Paste") == 0) {

                        int startRow = (table.getSelectedRows())[0];
                        int startCol = (table.getSelectedColumns())[0];
                        register newRegister = null;
                        String rtrstring;
                        try {
                                rtrstring = (String) (system.getContents(this).getTransferData(DataFlavor.stringFlavor));
                                StringTokenizer rst1 = new StringTokenizer(rtrstring, "\n");
                                rowstring = rst1.nextToken();
                                StringTokenizer st2 = new StringTokenizer(rowstring, "\t");
                                newRegister = new register(startRow, rst1.countTokens() + 1, startCol, st2.countTokens());
                                newRegister.getValues();
                        } catch (Exception ex) {
                        }

                        try {

                                String trstring = (String) (system.getContents(this).getTransferData(DataFlavor.stringFlavor));
                                trstring = trstring.replaceAll("(?sm)\t\t", "\t \t");
                                trstring = trstring.replaceAll("(?sm)\t\n", "\t \n");
                                StringTokenizer st1 = new StringTokenizer(trstring, "\n");
                                for (int i = 0; st1.hasMoreTokens(); i++) {
                                        rowstring = st1.nextToken();
                                        StringTokenizer st2 = new StringTokenizer(rowstring, "\t");
                                        for (int j = 0; st2.hasMoreTokens(); j++) {
                                                value = st2.nextToken();
                                                // If the value is "ç" means that the cell was empty. Look "copy" code.
                                                if (value.equals("ç")) {
                                                        value = "";
                                                }
                                                if (startRow + i < table.getRowCount()
                                                        && startCol + j < table.getColumnCount()) {
                                                        ((ParameterDataModel) table.getModel()).setValueAt(value, startRow + i, startCol + j, false);
                                                }
                                        }
                                }
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }

                        newRegister.getNewValues();
                        this.registers.addElement(newRegister);
                        this.indexRegister = this.registers.size() - 1;
                        table.validate();
                }

                // Deletes content of the selected cells.
                if (e.getActionCommand().compareTo("Delete") == 0) {
                        register newRegister = new register(table.getSelectedColumns(), table.getSelectedRows());
                        newRegister.getValues();

                        int[] selectedRow = table.getSelectedRows();
                        int[] selectedCol = table.getSelectedColumns();

                        try {
                                for (int i = 0; i < selectedRow.length; i++) {
                                        for (int j = 0; j < selectedCol.length; j++) {
                                                ((ParameterDataModel) table.getModel()).setValueAt("", selectedRow[i], selectedCol[j], false);
                                        }
                                }
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }

                        newRegister.getNewValues();
                        this.registers.addElement(newRegister);
                        this.indexRegister = this.registers.size() - 1;
                        table.validate();
                }

                // Undo
                if (e.getActionCommand().compareTo("Undo") == 0) {
                        this.registers.elementAt(indexRegister).undo();
                        if (indexRegister > 0) {
                                indexRegister--;
                        }
                        table.validate();
                }

                // Redo
                if (e.getActionCommand().compareTo("Redo") == 0) {
                        this.registers.elementAt(indexRegister).redo();
                        if (indexRegister < this.registers.size() - 1) {
                                indexRegister++;
                        }
                        table.validate();
                }
                System.gc();
        }

        /**
         * Method for reading exit code
         */
        public ExitCode getExitCode() {
                return exitCode;
        }

        /**
         * Table properties
         */
        public void setTableProperties() {
                // Selection mode

                table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                table.setColumnSelectionAllowed(true);


                // Size of the firts column which corresponds to the sample list column

                table.getColumnModel().getColumn(0).setMinWidth(300);

                //key actions for copy, paste, undo and redo

                registerKey(KeyEvent.VK_C, ActionEvent.CTRL_MASK, "Copy");
                registerKey(KeyEvent.VK_V, ActionEvent.CTRL_MASK, "Paste");
                registerKey(KeyEvent.VK_DELETE, 0, "Delete");
                registerKey(KeyEvent.VK_Z, ActionEvent.CTRL_MASK, "Undo");
                registerKey(KeyEvent.VK_Y, ActionEvent.CTRL_MASK, "Redo");

                system = Toolkit.getDefaultToolkit().getSystemClipboard();
        }

        /**
         * Adds a concrete action to a combination of keys.
         *
         * @param key Key responsible of the action
         * @param mask Mask of the key
         * @param name Name of the action
         */
        private void registerKey(int key, int mask, String name) {
                KeyStroke action = KeyStroke.getKeyStroke(key, mask, false);
                table.registerKeyboardAction(this, name, action, JComponent.WHEN_FOCUSED);
        }

        /**
         * Checks whether the table already contains a column with the same name
         * as the param "text".
         *
         * @param text String
         * @return True is there is already a column with the same name. 
         */
        private boolean tableContains(String text) {
                for (int i = 0; i < table.getColumnCount(); i++) {
                        if (table.getColumnName(i).equals(text)) {
                                return true;
                        }
                }
                return false;
        }

        private void openSelectionFile() {
                readFileDialog dialog = new readFileDialog(null);
                dialog.setVisible(true);
                String filePath = "";
                try {
                        filePath = dialog.getCurrentDirectory();
                } catch (Exception e) {
                }
                try {
                        CsvReader reader = new CsvReader(new FileReader(filePath));
                        try {
                                reader.readHeaders();

                                String[] header = reader.getHeaders();
                                for (int i = 1; i < header.length; i++) {
                                        model.addColumn(header[i]);
                                }
                                ((ParameterDataModel) model).fireTableStructureChanged();
                                table.getColumnModel().getColumn(0).setMinWidth(300);
                                while (reader.readRecord()) {
                                        String[] values = reader.getValues();
                                        int index = -1;
                                        for (int e = 0; e < model.getRowCount(); e++) {
                                                String sampleName = model.getValueAt(e, 0);
                                                if (sampleName.equals(values[0])) {
                                                        index = e;
                                                }
                                        }
                                        if (index > -1) {
                                                for (int i = 1; i < values.length; i++) {
                                                        model.setValueAt(values[i], index, i);
                                                }
                                        }
                                }
                                ((ParameterDataModel) model).addParameters(dataset);
                                ((ParameterDataModel) model).fireTableDataChanged();                               

                        } catch (IOException ex) {
                                Logger.getLogger(ParameterDialog.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        reader.close();
                } catch (FileNotFoundException ex) {
                }
        }

        /**
         * Class use to store the historial of copy and paste actions.
         */
        class register {

                int[] columnIndex;
                int[] rowIndex;
                Object[] values;
                Object[] newValues;

                public register(int[] columnIndex, int[] rowIndex) {
                        this.columnIndex = columnIndex;
                        this.rowIndex = rowIndex;
                        values = new Object[columnIndex.length * rowIndex.length];
                        newValues = new Object[columnIndex.length * rowIndex.length];
                }

                private register(int startRow, int rowCount, int startCol, int columnCount) {
                        rowIndex = new int[rowCount];
                        columnIndex = new int[columnCount];
                        for (int i = 0; i < rowCount; i++) {
                                rowIndex[i] = startRow + i;
                        }
                        for (int i = 0; i < columnCount; i++) {
                                columnIndex[i] = startCol + i;
                        }
                        values = new Object[columnIndex.length * rowIndex.length];
                        newValues = new Object[columnIndex.length * rowIndex.length];
                }

                public void getValues() {
                        int cont = 0;
                        for (int row : rowIndex) {
                                for (int column : columnIndex) {
                                        try {
                                                values[cont++] = table.getValueAt(row, column);
                                        } catch (Exception e) {
                                        }
                                }
                        }
                }

                public void getNewValues() {
                        int cont = 0;
                        for (int row : rowIndex) {
                                for (int column : columnIndex) {
                                        try {
                                                newValues[cont++] = table.getValueAt(row, column);
                                        } catch (Exception e) {
                                        }
                                }
                        }
                }

                public void undo() {
                        int cont = 0;
                        for (int row : rowIndex) {
                                for (int column : columnIndex) {
                                        ((ParameterDataModel) table.getModel()).setValueAt(values[cont++], row, column, false);
                                }
                        }
                }

                public void redo() {
                        int cont = 0;
                        for (int row : rowIndex) {
                                for (int column : columnIndex) {
                                        ((ParameterDataModel) table.getModel()).setValueAt(newValues[cont++], row, column, false);
                                }
                        }
                }
        }
}
