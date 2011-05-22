package es.ucm.plg.interprete.memoria;

import java.util.Comparator;

public class HuecosDireccionComparator implements Comparator {

	public int compare(Object o1, Object o2) { 
        Hueco hueco1 = (Hueco)o1; 
        Hueco hueco2 = (Hueco)o2; 
        if(hueco1.getDir() > hueco2.getDir())
			return 1;
		else
			if(hueco1.getDir() == hueco2.getDir())
				return 0;
			else
				return -1;
    } 
	
}
