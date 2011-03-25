/*
 * Copyright 2007-2011 VTT Biotechnology
 * 
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
 * Guineu; if not, write to the Free Software Foundation, Inc., 51 Franklin
 * St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package guineu.parameters.parametersType;

import guineu.parameters.UserParameter;
import org.w3c.dom.Element;

/**
 * Simple Parameter implementation
 * 
 * 
 */
/**
 * @author Taken from MZmine2
 * http://mzmine.sourceforge.net/
 */
public class PercentParameter implements
		UserParameter<Double, PercentComponent> {

	private String name, description;
	private Double value;

	public PercentParameter(String name, String description) {
		this.name = name;
		this.description = description;
	}

	
	@Override
	public String getName() {
		return name;
	}

	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public PercentComponent createEditingComponent() {
		return new PercentComponent();
	}

	@Override
	public void setValueFromComponent(PercentComponent component) {
		Double componentValue = component.getValue();
		if (componentValue == null)
			return;
		if ((componentValue < 0) || (componentValue > 100))
			throw new IllegalArgumentException("Invalid percentage value");
		this.value = componentValue;
	}

	@Override
	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public PercentParameter clone() {
		PercentParameter copy = new PercentParameter(name, description);
		copy.setValue(this.getValue());
		return copy;
	}

	@Override
	public void setValueToComponent(PercentComponent component, Double newValue) {
		if (newValue == null) return;
		component.setValue(newValue);
	}

	@Override
	public Double getValue() {
		return value;
	}

	@Override
	public void loadValueFromXML(Element xmlElement) {
		String numString = xmlElement.getTextContent();
		if (numString.length() == 0)
			return;
		this.value = Double.parseDouble(numString);
	}

	@Override
	public void saveValueToXML(Element xmlElement) {
		if (value == null)
			return;
		xmlElement.setTextContent(value.toString());
	}

}
