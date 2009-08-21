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
package guineu.modules.mylly.gcgcaligner.process;

import guineu.modules.mylly.gcgcaligner.filter.MapFunction;
import guineu.modules.mylly.gcgcaligner.gui.StatusReporter;
import guineu.modules.mylly.gcgcaligner.gui.parameters.ParameterInputException;
import java.awt.Frame;



/**
 * All classes should implements both zero-argument constructor
 * and clone() in such a way that they can be cloned at will and
 * afterwards configured individually using askParameters().
 * @author jmjarkko
 *
 * @param <T>
 * @param <Y>
 */
public interface ProcessAndSettings<T,Y> extends MapFunction<T,Y>, Cloneable, StatusReporter
{
	/**
	 * How to handle errors?
	 * @param parentWindow
	 * @throws ParameterInputException when it encounters problems reading parameters.
	 */
	public void askParameters(Frame parentWindow) throws ParameterInputException;
	
	/**
	 * 
	 * @return
	 */
	public ProcessAndSettings<T,Y> clone();
	
	/**
	 * Doesn askParameters do anything important?
	 * @return
	 */
	public boolean isConfigurable();
	

	
	
}
