/*
 * Copyright 2007-2008 VTT Biotechnology
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
package guineu.desktop;

import guineu.data.Dataset;
import guineu.main.GuineuModule;
import guineu.modules.mylly.gcgcaligner.datastruct.GCGCData;
import java.awt.Color;
import java.awt.event.ActionListener;

import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;

/**
 * This interface represents the application GUI
 * 
 */
public interface Desktop extends GuineuModule {

    /**
     * Returns a reference to main application window
     * 
     * @return Main window frame
     */
    public JFrame getMainFrame();

    /**
     * Creates a new menu item in the main application menu.
     * 
     * @param parentMenu GuineuMenu where to create the new item
     * @param text Item text
     * @param toolTip Item's tooltip
     * @param mnemonic Item's keyboard shortcut
     * @param listener ActionListener to receive the new menu item's events
     * @param actionCommand Action command for action listener or null
     * @return Newly created JMenuItem
     */
    public JMenuItem addMenuItem(GuineuMenu parentMenu, String text,
            String toolTip, int mnemonic, ActionListener listener, String actionCommand);

    /**
     * Adds a separator to a given Guineu menu
     * 
     * @param parentMenu Menu where to add a separator
     */
    public void addMenuSeparator(GuineuMenu parentMenu);

    /**
     * Adds a new internal frame (JInternalFrame) to the desktop pane
     * 
     * @param frame Internal frame to add
     */
    public void addInternalFrame(JInternalFrame frame);

    /**
     * Returns all internal frames in the desktop pane
     * 
     * @return Array of all internal frames
     */
    public JInternalFrame[] getInternalFrames();

    /**
     * Returns the currently selected frame or null if no frame is selected
     * 
     * @return Selected frame
     */
    public JInternalFrame getSelectedFrame();

    /**
     * Displays a given text on the application status bar in black color
     * 
     * @param text Text to show
     */
    public void setStatusBarText(String text);

    /**
     * Displays a given text on the application status bar in a given color
     * 
     * @param text Text to show
     * @param textColor Text color
     */
    public void setStatusBarText(String text, Color textColor);

    /**
     * Displays a message box with a given text
     * 
     * @param msg Text to show
     */
    public void displayMessage(String msg);

    /**
     * Displays an error message box with a given text
     * 
     * @param msg Text to show
     */
    public void displayErrorMessage(String msg);

    /**
     * Returns array of currently selected raw data files in GUI
     * 
     * @return Array of selected raw data files
     */
    public Dataset[] getSelectedDataFiles();

	public List<GCGCData> getSelectedGCGCDataFiles();

    public void AddNewFile(Dataset dataset);

	public void AddNewFile(GCGCData dataToAlign);

    public void removeData(Dataset file);
}
