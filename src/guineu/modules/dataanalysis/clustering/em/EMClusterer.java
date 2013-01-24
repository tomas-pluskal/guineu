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

package guineu.modules.dataanalysis.clustering.em;

import guineu.modules.dataanalysis.clustering.ClusteringAlgorithm;
import guineu.modules.dataanalysis.clustering.VisualizationType;
import guineu.parameters.ParameterSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.clusterers.Clusterer;
import weka.clusterers.EM;
import weka.core.Instance;
import weka.core.Instances;

public class EMClusterer implements ClusteringAlgorithm {

        private ParameterSet parameters;
        private int numberOfGroups = 0;

        public EMClusterer() {
                parameters = new EMClustererParameters();
        }

        public List<Integer> getClusterGroups(Instances dataset) {
                List<Integer> clusters = new ArrayList<Integer>();
                String[] options = new String[2];
                Clusterer clusterer = new EM();

                int numberOfIterations = parameters.getParameter(EMClustererParameters.numberOfIterations).getValue();
                options[0] = "-I";
                options[1] = String.valueOf(numberOfIterations);

                try {
                        ((EM) clusterer).setOptions(options);
                        clusterer.buildClusterer(dataset);
                        Enumeration e = dataset.enumerateInstances();
                        while (e.hasMoreElements()) {
                                clusters.add(clusterer.clusterInstance((Instance) e.nextElement()));
                        }
                        this.numberOfGroups = clusterer.numberOfClusters();
                } catch (Exception ex) {
                        Logger.getLogger(EMClusterer.class.getName()).log(Level.SEVERE, null, ex);
                }
                return clusters;
        }

        @Override
        public String toString() {
                return "Density Based Clusterer";
        }

      
        public ParameterSet getParameterSet() {
                return parameters;
        }

        public String getHierarchicalCluster(Instances dataset) {
                return null;
        }

        public VisualizationType getVisualizationType() {
                return parameters.getParameter(EMClustererParameters.visualization).getValue();
        }

        public int getNumberOfGroups() {
                return this.numberOfGroups;
        }
}
