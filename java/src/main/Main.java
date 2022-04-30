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
	private static final String NODOS_PRINCIPALES = "aparcamiento-bicicleta";

	public static void main(String[] args) {
		LectorXML lector = new LectorXML();
		EscritorXML escritor = new EscritorXML();

		try{
			lector.leerArchivoXML(RUTA_XML);
			NodeList nodes = lector.getNodosPrincipales(NODOS_PRINCIPALES);
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

	private static ArrayList<Element> nodeListToArray(NodeList listaHijos) {
		ArrayList<Element> lista = new ArrayList<>();
		for (int i = 0; i < listaHijos.getLength(); i++) {
			lista.add((Element) listaHijos.item(i));
		}
		return lista;
	}


}