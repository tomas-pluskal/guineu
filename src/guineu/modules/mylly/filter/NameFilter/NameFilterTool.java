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
package guineu.modules.mylly.filter.NameFilter;

import guineu.modules.mylly.datastruct.GCGCDatum;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NameFilterTool implements FilterFunction<GCGCDatum> {

    private Set<String> namesToFilter;
    private String name;

    public NameFilterTool(Collection<String> names) {
        this(names, "Filter by peak names");
    }

    public NameFilterTool(Collection<String> names, String filterName) {
        this.namesToFilter = new HashSet<String>(names.size());
        for (String curStr : names) {
            String parts[] = curStr.split("\\(CAS\\)");
            if (parts.length > 0) {
                namesToFilter.add(parts[0].trim());
            } else {
                namesToFilter.add(curStr.trim());
            }
        }
        this.name = filterName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean include(GCGCDatum obj) {
        boolean notFound = true;
        for (String str : namesToFilter) {
            if (obj.getName().equals(str)) {
                notFound = false;
                break;
            }
        }
        return notFound;
    }

    public boolean include(String obj) {
        boolean notFound = true;
        for (String str : namesToFilter) {
            if (obj.equals(str)) {
                notFound = false;
                break;
            }
        }
        return notFound;
    }

    @Override
    public GCGCDatum map(GCGCDatum obj) {
        if (include(obj)) {
            return obj;
        }
        return null;
    }

    @Override
    public boolean exclude(GCGCDatum obj) {
        return !include(obj);
    }

    public Set<String> filteredNames() {
        return new HashSet<String>(namesToFilter);
    }
}
