package org.woped.core.model.petrinet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author waschtl
 */

@SuppressWarnings("serial")
public class ResourceClassModel implements Serializable
{
    private String          name       						  = null;
    private int             type      					      = -1;
    
    private ArrayList<ResourceClassModel> superModelList  	  = null;
    public static final int TYPE_ROLE    					  = 0;
    public static final int TYPE_ORGUNIT 					  = 1;
  

    public ResourceClassModel(String name, int type)
    {
        this.name = name;
        this.type = type;
    }

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }
    
    
    public Iterator<ResourceClassModel> getSuperModels(){
    	if(superModelList != null){
    		 Iterator<ResourceClassModel> superModels =  superModelList.iterator();
    		 return superModels;
    	}else{
    		return null;
    	}
    }
    
    public void addSuperModel(ResourceClassModel superModel){
    	if(superModelList == null){
    		superModelList = new ArrayList<ResourceClassModel>();
    	}
    	superModelList.add(superModel);
    }
 
    
    public void removeSuperModel(ResourceClassModel model){
    	for (int i=0;i<superModelList.size();i++){		
    		if(superModelList.get(i).toString().equals(model.toString())){
    			superModelList.remove(i); 
    		}
    	}
    	if(superModelList.size()==0){
    		superModelList=null;
    	}
    }
    
    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Returns the type.
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return getName();
    }
}