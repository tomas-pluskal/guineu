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
package guineu.modules.identification.AdductSearch;

import guineu.parameters.SimpleParameterSet;
import guineu.parameters.UserParameter;
import guineu.parameters.parametersType.MZToleranceParameter;
import guineu.parameters.parametersType.MultiChoiceParameter;
import guineu.parameters.parametersType.NumberParameter;
import guineu.parameters.parametersType.RTToleranceParameter;
import java.text.NumberFormat;

public class AdductSearchParameters extends SimpleParameterSet {

        public static final NumberFormat percentFormat = NumberFormat.getPercentInstance();
        public static final RTToleranceParameter rtTolerance = new RTToleranceParameter(
                "Time tolerance",
                "Maximum allowed difference of time to set a relationship between peaks");
        public static final MultiChoiceParameter<AdductType> adducts = new MultiChoiceParameter<AdductType>(
                "Adducts",
                "List of adducts, each one refers a specific distance in m/z axis between related peaks",
                AdductType.values());
        public static final NumberParameter customAdductValue = new NumberParameter(
                "Custom adduct value",
                "Mass value (m/z difference) for custom adduct");
        public static final MZToleranceParameter mzTolerance = new MZToleranceParameter(
                "m/z tolerance",
                "Tolerance value of the m/z difference between peaks");
        public static final NumberParameter maxAdductHeight = new NumberParameter(
                "Max adduct peak height",
                "Maximum height of the recognized adductpeak, relative to the main peak",
                percentFormat, new Double(0.20));

        public AdductSearchParameters() {
                super(new UserParameter[]{rtTolerance, adducts, customAdductValue,
                                mzTolerance, maxAdductHeight});
        }
}
