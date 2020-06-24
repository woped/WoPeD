package org.woped.quantana.utilities;

import java.awt.Color;

public class ColorFactory {
	
	private static final int CF_MAX_COLORS	= 42;
	
	private static final int[] CF_01	= {0, 51, 0};
	private static final int[] CF_02	= {0, 51, 102};
	private static final int[] CF_03	= {0, 0, 102};
	private static final int[] CF_04	= {102, 0, 102};
	private static final int[] CF_05	= {165, 0, 33};
	private static final int[] CF_06	= {102, 51, 0};
	private static final int[] CF_07	= {0, 128, 0};
	private static final int[] CF_08	= {0, 102, 153};
	private static final int[] CF_09	= {51, 51, 255};
	private static final int[] CF_10	= {204, 0, 204};
	private static final int[] CF_11	= {204, 0, 0};
	private static final int[] CF_12	= {204, 153, 0};
	private static final int[] CF_13	= {51, 204, 51};
	private static final int[] CF_14	= {51, 204, 204};
	private static final int[] CF_15	= {51, 102, 255};
	private static final int[] CF_16	= {255, 0, 255};
	private static final int[] CF_17	= {255, 80, 80};
	private static final int[] CF_18	= {255, 255, 0};
	private static final int[] CF_19	= {102, 255, 102};
	private static final int[] CF_20	= {0, 255, 255};
	private static final int[] CF_21	= {102, 153, 255};
	private static final int[] CF_22	= {255, 102, 255};
	private static final int[] CF_23	= {255, 124, 128};
	private static final int[] CF_24	= {255, 255, 102};
	private static final int[] CF_25	= {153, 255, 153};
	private static final int[] CF_26	= {102, 255, 255};
	private static final int[] CF_27	= {153, 204, 255};
	private static final int[] CF_28	= {255, 153, 255};
	private static final int[] CF_29	= {255, 153, 153};
	private static final int[] CF_30	= {255, 255, 153};
	private static final int[] CF_31	= {51, 153, 102};
	private static final int[] CF_32	= {0, 51, 153};
	private static final int[] CF_33	= {102, 0, 204};
	private static final int[] CF_34	= {153, 51, 102};
	private static final int[] CF_35	= {153, 51, 0};
	private static final int[] CF_36	= {102, 102, 51};
	private static final int[] CF_37	= {0, 255, 153};
	private static final int[] CF_38	= {0, 153, 255};
	private static final int[] CF_39	= {153, 102, 255};
	private static final int[] CF_40	= {255, 51, 153};
	private static final int[] CF_41	= {255, 153, 51};
	private static final int[] CF_42	= {153, 255, 51};
	
	private static final Color[] CF_COLORS = {
		getColor(CF_01), getColor(CF_02), getColor(CF_03), 
		getColor(CF_04), getColor(CF_05), getColor(CF_06), 
		getColor(CF_07), getColor(CF_08), getColor(CF_09), 
		getColor(CF_10), getColor(CF_11), getColor(CF_12), 
		getColor(CF_13), getColor(CF_14), getColor(CF_15), 
		getColor(CF_16), getColor(CF_17), getColor(CF_18), 
		getColor(CF_19), getColor(CF_20), getColor(CF_21), 
		getColor(CF_22), getColor(CF_23), getColor(CF_24), 
		getColor(CF_25), getColor(CF_26), getColor(CF_27), 
		getColor(CF_28), getColor(CF_29), getColor(CF_30), 
		getColor(CF_31), getColor(CF_32), getColor(CF_33), 
		getColor(CF_34), getColor(CF_35), getColor(CF_36), 
		getColor(CF_37), getColor(CF_38), getColor(CF_39), 
		getColor(CF_40), getColor(CF_41), getColor(CF_42)
	};
	
	private int count = 0;
	
	public Color nextColor(){
		++count;
		if (count > CF_MAX_COLORS) count = 1;
		
		return CF_COLORS[count - 1];
	}
	
	private static Color getColor(int[] rgb){
		return new Color(rgb[0], rgb[1], rgb[2]);
	}
}
