/*
 * 
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.ba-karlsruhe.de
 *
 */
/*
 *	File:		BrowserHistory.java
 *	Description:	Store HTML history for Helpbrowser
 *
 *	@author Thomas Freytag
 */

package org.woped.gui.help;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * @author <a href="mailto:freytag@ba-karlsruhe.de">Thomas Freytag </a> <br>
 *         History implementation (using two Stacks) for the MiniBrowser. The
 *         History class remembers two stacks of recently visited pages (forward
 *         and backward direction) in addition to the currently displayed page.
 */
public class BrowserHistory
{
    /**
     * Stack of visited pages in backward direction
     */
    protected Stack  history = new Stack();

    /**
     * Stack of visited pages in forward direction
     */
    protected Stack  future  = new Stack();

    /**
     * URL of currently displayed page (<tt>null</tt> if none)
     */
    protected String current;

    /**
     * Add a page to this history object. The new page will become the current
     * page (but successive duplicate entries are ignored).
     */
    public void add(String url)
    {
        if (current != null && !current.equals(url)) history.push(current);

        future.removeAllElements(); // drop forward stack
        current = url;
    }

    /**
     * Return the current page in the history (or <tt>null</tt> if there is
     * not yet a current page).
     */
    public String getCurrent()
    {
        return current;
    }

    /**
     * Return whether the backward history is non-empty.
     */
    public boolean canBack()
    {
        return !history.empty();
    }

    /**
     * Move one step back in the history and return the corresponding page
     * (which will become the new current page).
     * 
     * @throws EmptyStackException
     *             if the backward history stack is empty
     */
    public String back()
    {
        String result = (String) history.pop();

        future.push(current);
        return current = result;
    }

    /**
     * Return whether the forward history is non-empty.
     */
    public boolean canForward()
    {
        return !future.empty();
    }

    /**
     * Move one step forward in the history and return the corresponding page
     * (which will become the new current page).
     * 
     * @throws EmptyStackException
     *             if the forward history stack is empty
     */
    public String forward()
    {
        String result = (String) future.pop();

        history.push(current);
        return current = result;
    }
}