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
package guineu.modules.identification.linearnormalization;

import guineu.parameters.SimpleParameterSet;
import guineu.parameters.UserParameter;
import guineu.parameters.parametersType.ComboParameter;

/**
 * @author Taken from MZmine2
 * http://mzmine.sourceforge.net/
 */
public class LinearNormalizerParameters extends SimpleParameterSet {

        public static final String NormalizationTypeAverageIntensity = "Average intensity";
        public static final String NormalizationTypeAverageSquaredIntensity = "Average squared intensity";
        public static final String NormalizationTypeMaximumPeakHeight = "Maximum peak intensity";
        public static final String NormalizationTypeStandards = "Selected standards";
        public static final String[] normalizationTypePossibleValues = {
                NormalizationTypeStandards,
                NormalizationTypeAverageIntensity,
                NormalizationTypeAverageSquaredIntensity,
                NormalizationTypeMaximumPeakHeight};
        public static final ComboParameter<String> normalizationType = new ComboParameter<String>(
                "Normalization type",
                "Normalize intensities by...",
                normalizationTypePossibleValues);

        public LinearNormalizerParameters() {
                super(new UserParameter[]{normalizationType});
        }
}
