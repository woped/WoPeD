/**
 * 
 * Copyright 2007-2010 by Nils Reiter.
 * 
 * This FrameNet API is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3.
 *
 * This FrameNet API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this FrameNet API.  If not, see www.gnu.org/licenses/gpl.html.
 * 
 */
package de.saar.coli.salsa.reiter.framenet;

/**
 * This interface specifies that objects implementing it have
 * an associated cDate (creation date) in their XML file.
 * 
 * @author Nils Reiter
 *
 */
public interface IHasCDate {
	
	/** 
	 * Returns the cDate as string. Future versions may provide additional
	 * methods returning a {@link java.lang.Date Date} object.
	 * @return the cDate.
	 */
	public String getCDate();
}
