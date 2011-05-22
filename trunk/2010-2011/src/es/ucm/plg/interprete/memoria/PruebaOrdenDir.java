package es.ucm.plg.interprete.memoria;

import java.util.Collections;
import java.util.Vector;

public class PruebaOrdenDir {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Memoria memoria = new Memoria(10);		
		Vector<Hueco> huecos = new Vector<Hueco>();
		huecos.add(new Hueco(9, 4));
		huecos.add(new Hueco(12, 1));
		huecos.add(new Hueco(4, 2));
		huecos.add(new Hueco(13, 1));
		huecos.add(new Hueco(7, 2));
		huecos.add(new Hueco(6, 1));
		memoria.setHuecos(huecos);
		Collections.sort(memoria.getHuecos());
		System.out.println("Tamaño----------------------------------");
		for(int i = 0; i < memoria.getHuecos().size(); i++)
			System.out.println("Direccion: " + memoria.getHuecos().get(i).getDir() + " - Tam: " + memoria.getHuecos().get(i).getTam());
		Collections.sort(memoria.getHuecos(), new HuecosDireccionComparator());
		System.out.println("Direccion----------------------------------");
		for(int i = 0; i < memoria.getHuecos().size(); i++)
			System.out.println("Direccion: " + memoria.getHuecos().get(i).getDir() + " - Tam: " + memoria.getHuecos().get(i).getTam());
		memoria.unifica();
		System.out.println("Unifica----------------------------------");
		for(int i = 0; i < memoria.getHuecos().size(); i++)
			System.out.println("Direccion: " + memoria.getHuecos().get(i).getDir() + " - Tam: " + memoria.getHuecos().get(i).getTam());
	}

}
