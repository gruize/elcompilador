/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.ucm.plg.interfaz;

import java.io.File;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import es.ucm.plg.interprete.Interprete;

/**
 *
 * @author GRUPO 3: Gonzalo Ortiz Jaureguizar, Alicia Perez Jimenez, Laura Reyero Sainz, Hector Sanjuan Redondo, Ruben Tarancon Garijo
 */
public class EjecucionThread extends Thread {

    Reader reader;
    Writer writer;
    boolean debug;


    public EjecucionThread(Reader reader, Writer writer,boolean debug) {
        this.reader = reader;
        this.writer = writer;
        this.debug=debug;
    }



    @Override
    public void run() {
         try {
            Interprete interprete = new Interprete(debug, reader, new PrintWriter(System.out,true));
            File f2 = new File("c:/temp/codigoP.bc");
            interprete.leerPrograma(f2);
            interprete.ejecutarPrograma();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
