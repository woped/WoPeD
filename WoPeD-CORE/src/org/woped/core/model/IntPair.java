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
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.core.model;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;

/*
 * The model should not be based on classes in the java.awt package. Some EJB-Server don't like it.
 * This is the the compensation for Point, Dimension, ...
 */
/**
 * 
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 */
@SuppressWarnings("serial")
public class IntPair implements Serializable
{

    private int x1, x2;

    public IntPair(Dimension d)
    {
        this.x1 = d.width;
        this.x2 = d.height;
    }

    public IntPair(Point p)
    {
        this.x1 = p.x;
        this.x2 = p.y;
    }

    public IntPair(int x1, int x2)
    {
        this.x1 = x1;
        this.x2 = x2;
    }

    public int getX1()
    {
        return x1;
    }

    public void setX1(int x1)
    {
        this.x1 = x1;
    }

    public int getX2()
    {
        return x2;
    }

    public void setX2(int x2)
    {
        this.x2 = x2;
    }

}
