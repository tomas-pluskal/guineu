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

package guineu.modules.filter.Alignment.RANSACGCGC;

import java.util.Comparator;

public class GCGCRTs implements Comparator<GCGCRTs> {

	public double RT;
	public double RT2;
	int map;

	public GCGCRTs() {
	}

	public GCGCRTs(double RT, double RT2) {
		this.RT = RT + (0.001 / Math.random());
		this.RT2 = RT2 + (0.001 / Math.random());
	}

	public int compare(GCGCRTs arg0, GCGCRTs arg1) {
		if (arg0.RT < arg1.RT) {
			return -1;
		} else {
			return 1;
		}

	}
}
