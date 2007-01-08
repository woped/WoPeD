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
package org.woped.core.model;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.woped.core.config.ConfigurationManager;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * Created on: 20.01.2005 Last Change on: 20.01.2005
 */

@SuppressWarnings("serial")
public class CreationMap extends HashMap<String, Object>
{

    public static final String ELEMENT_ID            = "ELEMENT_ID";
    public static final String ELEMENT_TYPE          = "ELEMENT_TYPE";
    public static final String ELEMENT_SIZE          = "ELEMENT_SIZE";
    public static final String ELEMENT_POSITION      = "ELEMENT_POSITION";
    public static final String ELEMENT_NAME          = "ELEMENT_NAME";
    public static final String ELEMENT_NAME_SIZE     = "ELEMENT_NAME_SIZE";
    public static final String ELEMENT_NAME_POSITION = "ELEMENT_NAME_POSITION";
    public static final String EDIT_ON_CREATION      = "EDIT_ON_CREATION";
    public static final String OPERATOR_TYPE         = "OPERATOR_TYPE";
    public static final String TRIGGER_TYPE          = "TRIGGER_TYPE";
    public static final String TRIGGER_POSITION      = "TRIGGER_POSITION";
    public static final String TOKENS                = "TOKENS";
    public static final String UNKNOWN_TOOLSPEC      = "UNKNOWN_TOOLSPEC";
    public static final String ARC_ID                = "ARC_ID";
    public static final String ARC_TARGET_ID         = "ARC_TARGET_ID";
    public static final String ARC_SOURCE_ID         = "ARC_SOURCE_ID";
    public static final String ARC_POINTS            = "ARC_POINTS";
    public static final String ARC_ROUTE             = "ARC_ROUTE";
    public static final String RESOURCE_POSITION     = "RESOURCE_POSITION";
    public static final String RESOURCE_ROLE         = "RESOURCE_ROLE";
    public static final String RESOURCE_ORGUNIT      = "RESOURCE_ORGUNIT";
    public static final String IMAGEICON             = "IMAGEICON";
    public static final String STATE_TYPE            = "STATETYPE";
    public static final String READ_ONLY             = "READ_ONLY";
    public static final String TRANSITION_TIME		 = "TRANSITION_TIME";
    public static final String TRANSITION_TIMEUNIT	 = "TRANSITION_TIMEUNIT";
    public static final String UPPER_ELEMENT         = "UPPER_ELEMENT";

    public static CreationMap createMap()
    {
        return new CreationMap();
    }

    public boolean isValid()
    {
        if (getType() != -1) return true;
        else return false;
    }

    public void setId(String id)
    {
        put(ELEMENT_ID, id);
    }

    public String getId()
    {
        if (containsKey(ELEMENT_ID))
        {
            return (String) get(ELEMENT_ID);
        } else
        {
            return null;
        }
    }

    public void setType(int type)
    {
        put(ELEMENT_TYPE, new Integer(type));
    }

    public int getType()
    {
        if (containsKey(ELEMENT_TYPE))
        {
            return ((Integer) get(ELEMENT_TYPE)).intValue();
        } else
        {
            return -1;
        }
    }

    public void setSize(IntPair intPair)
    {
        put(ELEMENT_SIZE, intPair);
    }

    public IntPair getSize()
    {
        if (containsKey(ELEMENT_SIZE))
        {
            return (IntPair) get(ELEMENT_SIZE);
        } else
        {
            return null;
        }
    }

    public void setPosition(IntPair intPair)
    {
        put(ELEMENT_POSITION, intPair);
    }

    public void setPosition(int x, int y)
    {
        put(ELEMENT_POSITION, new IntPair(x, y));
    }

    public IntPair getPosition()
    {
        if (containsKey(ELEMENT_POSITION))
        {
            return (IntPair) get(ELEMENT_POSITION);
        } else
        {
            return null;
        }
    }

    public void setName(String name)
    {
        put(ELEMENT_NAME, name);
    }

    public String getName()
    {
        if (containsKey(ELEMENT_NAME))
        {
            return (String) get(ELEMENT_NAME);
        } else
        {
            return null;
        }
    }

    public void setNameSize(IntPair intPair)
    {
        put(ELEMENT_NAME_SIZE, intPair);
    }

    public IntPair getNameSize()
    {
        if (containsKey(ELEMENT_NAME_SIZE))
        {
            return (IntPair) get(ELEMENT_NAME_SIZE);
        } else
        {
            return null;
        }
    }

    public void setNamePosition(int x, int y)
    {
        put(ELEMENT_NAME_POSITION, new IntPair(x, y));
    }

    public void setNamePosition(IntPair intPair)
    {
        put(ELEMENT_NAME_POSITION, intPair);
    }

    public IntPair getNamePosition()
    {
        if (containsKey(ELEMENT_NAME_POSITION))
        {
            return (IntPair) get(ELEMENT_NAME_POSITION);
        } else
        {
            return null;
        }
    }

    public void setOperatorType(int operatorType)
    {
        put(OPERATOR_TYPE, new Integer(operatorType));
    }

    public int getOperatorType()
    {
        if (containsKey(OPERATOR_TYPE))
        {
            return ((Integer) get(OPERATOR_TYPE)).intValue();
        } else
        {
            return -1;
        }
    }

    public void setEditOnCreation(boolean edit)
    {
        put(EDIT_ON_CREATION, new Boolean(edit));
    }

    public boolean isEditOnCreation()
    {
        if (containsKey(EDIT_ON_CREATION))
        {
            return ((Boolean) get(EDIT_ON_CREATION)).booleanValue();
        } else
        {
            return ConfigurationManager.getConfiguration().isEditingOnCreation();
        }
    }

    public void setTriggerType(int type)
    {
        put(TRIGGER_TYPE, new Integer(type));
    }

    public int getTriggerType()
    {
        if (containsKey(TRIGGER_TYPE))
        {
            return ((Integer) get(TRIGGER_TYPE)).intValue();
        } else
        {
            return -1;
        }
    }

    public void setTriggerPosition(IntPair intPair)
    {
        put(TRIGGER_POSITION, intPair);
    }

    public void setTriggerPosition(int x, int y)
    {
        put(TRIGGER_POSITION, new IntPair(x, y));
    }

    public IntPair getTriggerPosition()
    {
        if (containsKey(TRIGGER_POSITION))
        {
            return (IntPair) get(TRIGGER_POSITION);
        } else
        {
            return null;
        }
    }

    public void setTokens(int tokens)
    {
        put(TOKENS, new Integer(tokens));
    }

    public int getTokens()
    {
        if (containsKey(TOKENS))
        {
            return ((Integer) get(TOKENS)).intValue();
        } else
        {
            return -1;
        }
    }

    public Vector getUnknownToolSpec()
    {
        if (get(UNKNOWN_TOOLSPEC) == null)
        {
            setUnknownToolSpec(new Vector());
        }
        return (Vector) get(UNKNOWN_TOOLSPEC);
    }

    public void setUnknownToolSpec(Vector toolSpec)
    {
        put(UNKNOWN_TOOLSPEC, toolSpec);
    }

    @SuppressWarnings("unchecked")
	public void addUnknownToolSpec(Object toolSpec)
    {
        if (get(UNKNOWN_TOOLSPEC) == null) setUnknownToolSpec(new Vector<Object>());
        
        ((Vector<Object>) get(UNKNOWN_TOOLSPEC)).add(toolSpec);
    }

    public String getArcId()
    {
        if (containsKey(ARC_ID))
        {
            return (String) get(ARC_ID);
        } else
        {
            return null;
        }
    }

    public void setArcId(String id)
    {
        put(ARC_ID, id);
    }

    public String getArcTargetId()
    {
        if (containsKey(ARC_TARGET_ID))
        {
            return (String) get(ARC_TARGET_ID);
        } else
        {
            return null;
        }
    }

    public void setArcTargetId(String id)
    {
        put(ARC_TARGET_ID, id);
    }

    public String getArcSourceId()
    {
        if (containsKey(ARC_SOURCE_ID))
        {
            return (String) get(ARC_SOURCE_ID);
        } else
        {
            return null;
        }
    }

    public void setArcSourceId(String id)
    {
        put(ARC_SOURCE_ID, id);
    }

    public void setArcRoute(boolean route)
    {
        put(ARC_ROUTE, new Boolean(route));
    }

    public boolean isArcRoute()
    {
        if (containsKey(ARC_ROUTE))
        {
            return ((Boolean) get(ARC_ROUTE)).booleanValue();
        } else
        {
            return true;
        }
    }

	public List getArcPoints()
    {
        if (containsKey(ARC_POINTS))
        {
            return ((List) get(ARC_POINTS));
        } else
        {
            return new Vector();
        }
    }

	@SuppressWarnings("unchecked")
    public void addArcPoint(IntPair intPair)
    {
        getArcPoints().add(intPair);
    }

    public void setArcPoints(List points)
    {
        put(ARC_POINTS, points);
    }

    // TRAnSITION_RESOURCES!!
    // TODO: documentation Sebastian!!
    public void setResourcePosition(IntPair intPair)
    {
        put(RESOURCE_POSITION, intPair);
    }

    public void setResourcePosition(int x, int y)
    {
        put(RESOURCE_POSITION, new IntPair(x, y));
    }

    public IntPair getResourcePosition()
    {
        if (containsKey(RESOURCE_POSITION))
        {
            return (IntPair) get(RESOURCE_POSITION);
        } else
        {
            return null;
        }
    }

    public void setResourceRole(String roleName)
    {
        put(RESOURCE_ROLE, roleName);
    }

    public String getResourceRole()
    {
        if (containsKey(RESOURCE_ROLE))
        {
            return (String) get(RESOURCE_ROLE);
        } else
        {
            return null;
        }
    }

    public void setResourceOrgUnit(String orgUnitName)
    {
        put(RESOURCE_ORGUNIT, orgUnitName);
    }

    public String getResourceOrgUnit()
    {
        if (containsKey(RESOURCE_ORGUNIT))
        {
            return (String) get(RESOURCE_ORGUNIT);
        } else
        {
            return null;
        }
    }

    public ImageIcon getImageIcon()
    {
        if (containsKey(IMAGEICON))
        {
            return (ImageIcon) get(IMAGEICON);
        } else
        {
            return null;
        }
    }

    public void setImageIcon(ImageIcon img)
    {
        put(IMAGEICON, img);
    }

    public void setStateType(int type)
    {
        put(STATE_TYPE, new Integer(type));
    }

    public int getStateType()
    {
        if (containsKey(STATE_TYPE))
        {
            return ((Integer) get(STATE_TYPE)).intValue();
        } else
        {
            return -1;
        }
    }
    
    public void setReadOnly(boolean readOnly)
    {
    	put(READ_ONLY, readOnly);;
    }

	public Boolean getReadOnly() {
		 if (containsKey(READ_ONLY))
	     {
	        return (Boolean) get(READ_ONLY);
	     } 
		 else
	     {
	        return false;
	     }
	}
	
    public void setTransitionTime(int time)
    {
    	put(TRANSITION_TIME, time);
    }

	public int getTransitionTime() {
		 if (containsKey(TRANSITION_TIME))
	     {
	        return ((Integer) get(TRANSITION_TIME)).intValue();
	     } 
		 else
	     {
	        return -1;
	     }
	}
	
    public void setTransitionTimeUnit(int time)
    {
    	put(TRANSITION_TIMEUNIT, time);
    }

	public int getTransitionTimeUnit() {
		 if (containsKey(TRANSITION_TIMEUNIT))
	     {
	        return ((Integer) get(TRANSITION_TIMEUNIT)).intValue();
	     } 
		 else
	     {
	        return -1;
	     }
	}

	public void setUpperElement(AbstractElementModel upperElement)
	{
		put(UPPER_ELEMENT, upperElement);
		
	}
	
	public AbstractElementModel getRealElement()
	{
		 if (containsKey(UPPER_ELEMENT))
	     {
	        return (AbstractElementModel) get(UPPER_ELEMENT);
	     } 
		 else
	     {
	        return null;
	     }
	}
}