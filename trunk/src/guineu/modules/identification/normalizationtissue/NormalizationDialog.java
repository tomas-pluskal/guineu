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
package guineu.modules.identification.normalizationtissue;

import guineu.main.GuineuCore;
import guineu.util.dialogs.ExitCode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author  scsandra
 */
public class NormalizationDialog extends javax.swing.JDialog implements ActionListener {

    private Vector<StandardUmol> standards;
    ExitCode exit = ExitCode.UNKNOWN;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    /** Creates new form NormalizationDialog */
    public NormalizationDialog(Vector<StandardUmol> standards, Hashtable<String, Double> weights) {
        super(GuineuCore.getDesktop().getMainFrame(),
                "Please fill the standards...", true);

        this.standards = standards;
        initComponents();

        StandardsDataModel model = new StandardsDataModel(this.standards);
        UnknownsDataModel unknownModel = new UnknownsDataModel(this.standards);

        this.jTable1.setModel(model);
        this.jTable2.setModel(unknownModel);
        this.jButtonClose.addActionListener(this);
        this.jButtonOk.addActionListener(this);
        this.jButtonReset.addActionListener(this);
        this.setSize(305, 410);
        logger.finest("Displaying Normalization Serum dialog");
    }

   
    public void fillStandards() {
        try {
           ((StandardsDataModel)this.jTable1.getModel()).fillStandards();
           ((UnknownsDataModel)this.jTable2.getModel()).fillStandards();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error, You have not introduced a correct value.", "Error", JOptionPane.ERROR_MESSAGE);
           
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jButtonOk = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jButtonReset = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();

        setMinimumSize(new java.awt.Dimension(587, 551));

        jPanel5.setPreferredSize(new java.awt.Dimension(500, 500));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("umol / l.blood sample  --- umol / g sample"));
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 250));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable1.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel1);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("")), "Unknown Compounds"));
        jPanel6.setPreferredSize(new java.awt.Dimension(500, 250));
        jPanel6.setRequestFocusEnabled(false);
        jPanel6.setLayout(new java.awt.BorderLayout());

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable2.setCellSelectionEnabled(true);
        jScrollPane2.setViewportView(jTable2);

        jPanel6.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel6);

        jButtonOk.setText("  Ok ");
        jPanel4.add(jButtonOk);

        jButtonClose.setText("Close");
        jPanel4.add(jButtonClose);

        jButtonReset.setText("Reset");
        jButtonReset.setMaximumSize(new java.awt.Dimension(60, 27));
        jButtonReset.setMinimumSize(new java.awt.Dimension(60, 27));
        jButtonReset.setPreferredSize(new java.awt.Dimension(60, 27));
        jPanel4.add(jButtonReset);

        jPanel2.setPreferredSize(new java.awt.Dimension(500, 300));

        jScrollPane3.setPreferredSize(new java.awt.Dimension(480, 380));

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Experiment Name", "Weight (mg)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable.setCellSelectionEnabled(true);
        jTable.setPreferredSize(new java.awt.Dimension(480, 380));
        jScrollPane3.setViewportView(jTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 887, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public ExitCode getExitCode() {
        return exit;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClose;
    public javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonReset;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.jButtonOk) {
            exit = ExitCode.OK;
            fillStandards();
            dispose();
        } else if (e.getSource() == this.jButtonClose) {
            exit = ExitCode.CANCEL;
            dispose();
        } else if (e.getSource() == this.jButtonReset) {
            this.reset();
        }


    }

    public void reset() {
        try {
            ((StandardsDataModel)this.jTable1.getModel()).resetStandards();
            ((UnknownsDataModel)this.jTable2.getModel()).resetStandards();
        } catch (Exception e) {
        }
    }


    public void readWeithgs() {
        FileReader fr = null;

        try {
            fr = new FileReader(new File("list.csv"));
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = (br.readLine())) != null) {
                if (!line.isEmpty()) {
                    String[] w = line.split(",");
                    for (int i = 0; i < this.jTable.getRowCount(); i++) {
                        w[0] = w[0].replaceAll(" \"", "").toLowerCase();
                        System.out.println(this.jTable.getValueAt(i, 0).toString().toLowerCase() + " - " + w[0] + " - " + w[1]);
                        if (this.jTable.getValueAt(i, 0).toString()/*.replace("_", "")*/.toLowerCase().matches(w[0] + ".*")) {

                            this.jTable.setValueAt(Double.valueOf(w[1]), i, 1);
                            break;
                        }
                    }
                }
            }
            fr.close();
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(NormalizationDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
