package org.woped.quantana.dashboard.storage;



public class ArchiveFilter {

	
	public int[] Filter( int anzahlWerte, int maxWerte) {
		
		int[] arrIndexToArchive = null;
		
		//check, if array of original values is greater than the desired size
		if(anzahlWerte <= 0 || maxWerte <= 0 ){
			
			return null;
			
		}else if (maxWerte >= anzahlWerte){
			
			arrIndexToArchive = new int[anzahlWerte];
			
			for(int i = 0; i< anzahlWerte ; i++){
				arrIndexToArchive[i] = i;
			}
			 
		}
		else{
		
			arrIndexToArchive = new int[maxWerte];
			
			int hopsize = (anzahlWerte-1) / (maxWerte-1);
			int rest = (anzahlWerte-1) % (maxWerte-1);
			int lasthop =  hopsize + rest;
			
			int currIndex = 0;
					
			for(int i = 0; i< maxWerte ; i++){
				if(i == (maxWerte-1)){
					arrIndexToArchive[currIndex] = i*hopsize + rest;
				}else{
					arrIndexToArchive[currIndex] = i * hopsize;
					currIndex++;
				}
				
			}
		}
	
		return arrIndexToArchive;
	}
	
	
	

}
