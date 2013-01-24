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
package guineu.desktop.preferences;

import guineu.data.GCGCColumnName;
import guineu.parameters.Parameter;
import guineu.parameters.SimpleParameterSet;
import guineu.parameters.parametersType.MultiChoiceParameter;

/**
 *
 * @author scsandra
 */
public class ColumnsGCGCParameters extends SimpleParameterSet {


public static final MultiChoiceParameter<GCGCColumnName> GCGCdata = new MultiChoiceParameter<GCGCColumnName>(
			"Select columns for GCxGC-MS", "Select columns for GCxGC-MS", GCGCColumnName.values());

        public ColumnsGCGCParameters() {
                super(new Parameter[]{GCGCdata});
        }
}
