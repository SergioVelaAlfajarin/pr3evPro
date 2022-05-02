package main;

import exception.LogException;
import exception.XMLException;
import gestor.EscritorXML;
import gestor.LectorXML;
import gestor.Logger;
import modelo.AparcamientoBicicleta;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class Main {
	private static final String RUTA_XML = "ficheros/aparcamiento-bicicleta.xml";
	private static final String RUTA_XML_NUEVA = "ficheros/aparcamiento.xml";
	private static final String RUTA_XML_INVALIDOS = "ficheros/llevar_bicis.dat";
	private static final String RUTA_LOG = "ficheros/log.txt";

	/**
	 * Metodo main
	 * <hr/>
	 * Crea un lector y un escritor.
	 * El lector se encargara de obtener la informacion del xml, y de analizarla. <br/>
	 * El escritor contendra un array de objetos Aparcamientos, para luego escribirlo. <br/>
	 */
	public static void main(String[] args) {
		LectorXML lector = new LectorXML();
		EscritorXML escritor = new EscritorXML();

		try{
			Logger.init(RUTA_LOG); //unica linea que puede lanzar LogException
			lector.leerArchivoXML(RUTA_XML);

			//lee nodos y los agrega a la coleccion
			NodeList nodes = lector.getNodosPrincipales();
			Logger.print("Transformando NodeList en Array de elements...");
			ArrayList<Element> listaElements = nodeListToArray(nodes);
			Logger.print("Lista de nodos transformada correctamente a array.");
			Logger.print("Iterando y transformando en Objeto...");
			for(Element el: listaElements){
				AparcamientoBicicleta apb = lector.transformElementToAparcamiento(el);
				escritor.addAparcamientoBicicleta(apb);
				Logger.print("AparcamientoBicleta transformado y ananido correctamente al array de EscritorXML. Id:" + apb.getId());
			}
			Logger.print("Array de elements Transformado correctamente.");

			//escribe los aparcamientos de la coleccion
			escritor.escribeAparcamientosEnArchivo(RUTA_XML_NUEVA,RUTA_XML_INVALIDOS);
			Logger.print("Aparcamientos Escritos. Ejecucion terminada.");
			System.out.println("Ejecucion terminada. Ver detalles en " + RUTA_LOG);
		}catch(XMLException e){
			Logger.print(e.getMessage());
			Logger.close();
			System.out.println("Ha ocurrido un problema. Por favor compruebe el archivo " +
					"'ficheros/log.txt'");
		}catch(LogException e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Metodo nodeListToArray
	 * <hr/>
	 * Transforma una nodeList en un array de Elements.
	 * Hecho para que pueda ser iterable.
	 * @param listaHijos Lista de nodos obtenidos desde el xml.
	 * @return ArrayList de Elements relleno de los nodos pasados por parametro.
	 */
	private static ArrayList<Element> nodeListToArray(NodeList listaHijos) {
		ArrayList<Element> lista = new ArrayList<>();
		for (int i = 0; i < listaHijos.getLength(); i++) {
			lista.add((Element) listaHijos.item(i));
		}
		return lista;
	}


}