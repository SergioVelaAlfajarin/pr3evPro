package main;

import exception.XMLException;
import gestor.EscritorXML;
import gestor.LectorXML;
import modelo.AparcamientoBicicleta;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class Main {

	private static final String RUTA_XML = "ficheros/aparcamiento-bicicleta.xml";
	private static final String RUTA_XML_NUEVA = "ficheros/aparcamiento.xml";
	private static final String RUTA_XML_INVALIDOS = "ficheros/llevar_bicis.dat";

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
			lector.leerArchivoXML(RUTA_XML);
			NodeList nodes = lector.getNodosPrincipales();
			ArrayList<Element> listaElements = nodeListToArray(nodes);
			for(Element el: listaElements){
				AparcamientoBicicleta apb = lector.transformElementToAparcamiento(el);
				escritor.addAparcamientoBicicleta(apb);
			}
			escritor.escribeAparcamientosEnArchivo(RUTA_XML_NUEVA,RUTA_XML_INVALIDOS);
		}catch(XMLException e){
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