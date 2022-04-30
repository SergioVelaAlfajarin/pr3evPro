package main;

import exception.XMLException;
import gestor.GestorXML;
import modelo.AparcamientoBicicleta;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class Main {

	private static final String RUTA_XML = "ficheros/aparcamiento-bicicleta.xml";
	private static final String NUEVA_RUTA_XML = "ficheros/aparcamiento.xml";

	public static void main(String[] args) {
		GestorXML gestor = new GestorXML();

		try{
			gestor.setInformacionArchivoOrigen(RUTA_XML);
			gestor.setInformacionArchivoDestino(NUEVA_RUTA_XML);

			{
				ArrayList<Element> listaElements = nodeListToArray(gestor.getNodosRaiz());
				ArrayList<AparcamientoBicicleta> listaAparcamientos = new ArrayList<>();

				for(Element el: listaElements){
					listaAparcamientos.add(gestor.transformElementToAparcamiento(el));
				}


			}

			System.out.println(gestor.getRutaOrigen());
			System.out.println(gestor.getRutaDestino());

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