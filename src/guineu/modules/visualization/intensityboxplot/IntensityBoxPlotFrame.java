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
/**
 * @author Taken from MZmine2
 * http://mzmine.sourceforge.net/
 */
package guineu.modules.visualization.intensityboxplot;

import guineu.data.Dataset;
import guineu.data.PeakListRow;
import guineu.main.GuineuCore;
import guineu.parameters.ParameterSet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

/**
 * 
 */
public class IntensityBoxPlotFrame extends JInternalFrame {

        static final Font legendFont = new Font("SansSerif", Font.PLAIN, 10);
        static final Font titleFont = new Font("SansSerif", Font.PLAIN, 11);
        private Logger logger = Logger.getLogger(this.getClass().getName());
        private BoxAndWhiskerCategoryDataset dataset;
        private JFreeChart chart;
        private String selectedFiles[];
        private PeakListRow selectedRows[];
        private Object xAxisValueSource;

        public IntensityBoxPlotFrame(ParameterSet parameters) {
                super("", true, true, true, true);


                String title = "Intensity box plot [" + GuineuCore.getDesktop().getSelectedDataFiles()[0].getDatasetName() + "]";
                String xAxisLabel = parameters.getParameter(IntensityBoxPlotParameters.xAxisValueSource).getValue().toString();
                this.xAxisValueSource = parameters.getParameter(
                        IntensityBoxPlotParameters.xAxisValueSource).getValue();
                // create dataset
                this.selectedFiles = parameters.getParameter(
                        IntensityBoxPlotParameters.dataFiles).getValue();

                this.selectedRows = parameters.getParameter(
                        IntensityBoxPlotParameters.selectedRows).getValue();

                this.dataset = this.createSampleDataset();
                // create new JFreeChart
                logger.finest("Creating new chart instance");
                //      chart = ChartFactory.createLineChart(title, xAxisLabel, "Intensity",
                //               dataset, PlotOrientation.VERTICAL, true, true, false);

                //  CategoryPlot plot = (CategoryPlot) chart.getPlot();

                // set renderer
                BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
                renderer.setFillBox(true);
                // set tooltip generator               
                renderer.setBaseToolTipGenerator(new BoxAndWhiskerToolTipGenerator());

                //  plot.setRenderer(renderer);
                //  plot.setBackgroundPaint(Color.white);
                //   CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
                //  xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
                // set y axis properties

                final CategoryAxis xAxis = new CategoryAxis("Type");
                final NumberAxis yAxis = new NumberAxis("Value");
                NumberFormat yAxisFormat = new DecimalFormat("0.0E0");
                yAxis.setNumberFormatOverride(yAxisFormat);
                final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

                final JFreeChart chart = new JFreeChart(
                        "Intensity Box plot",
                        new Font("SansSerif", Font.BOLD, 14),
                        plot,
                        true);
                chart.setBackgroundPaint(Color.white);

                // create chart JPanel
                ChartPanel chartPanel = new ChartPanel(chart);
                add(chartPanel, BorderLayout.CENTER);

//                IntensityBoxPlotToolBar toolBar = new IntensityBoxPlotToolBar(this);
                //   add(toolBar, BorderLayout.EAST);

                // disable maximum size (we don't want scaling)
                chartPanel.setMaximumDrawWidth(Integer.MAX_VALUE);
                chartPanel.setMaximumDrawHeight(Integer.MAX_VALUE);

                // set title properties
                TextTitle chartTitle = chart.getTitle();
                chartTitle.setMargin(5, 0, 0, 0);
                chartTitle.setFont(titleFont);

                LegendTitle legend = chart.getLegend();
                legend.setItemFont(legendFont);
                legend.setBorder(0, 0, 0, 0);



                // set shape provider
                IntensityBoxPlotDrawingSupplier shapeSupplier = new IntensityBoxPlotDrawingSupplier();

                plot.setDrawingSupplier(shapeSupplier);



                setTitle(title);
                setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
                setBackground(Color.white);
                pack();

        }

        JFreeChart getChart() {
                return chart;
        }

        private BoxAndWhiskerCategoryDataset createSampleDataset() {

                final DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();

                final List list = new ArrayList();
                if (!xAxisValueSource.equals("Sample")) {
                        Dataset data = GuineuCore.getDesktop().getSelectedDataFiles()[0];
                        List<String> parameterValues = data.getParameterAvailableValues((String) xAxisValueSource);
                        for (int i = 0; i < parameterValues.size(); i++) {
                                for (String sampleName : this.selectedFiles) {
                                        if (parameterValues.get(i).equals(data.getParametersValue(sampleName, (String) xAxisValueSource))) {
                                                for (int k = 0; k < this.selectedRows.length; k++) {
                                                        Double value = (Double) this.selectedRows[k].getPeak(sampleName);
                                                        list.add(value);

                                                }
                                        }
                                }

                                dataset.add(BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(list), "Series ", parameterValues.get(i));
                        }
                } else {
                        for (int i = 0; i < this.selectedFiles.length; i++) {
                                for (int k = 0; k < this.selectedRows.length; k++) {
                                        Double value = (Double) this.selectedRows[k].getPeak(this.selectedFiles[i]);
                                        list.add(value);
                                }
                                dataset.add(BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(list), "Series ", this.selectedFiles[i]);
                        }
                }

                return dataset;
        }
}
