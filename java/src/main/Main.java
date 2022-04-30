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
	private static final String NUEVA_RUTA_XML = "ficheros/aparcamiento.xml";

	public static void main(String[] args) {
		LectorXML lector = new LectorXML();
		EscritorXML escritor = new EscritorXML();

		try{
			lector.setInformacionArchivoOrigen(RUTA_XML);
			escritor.setInformacionArchivoDestino(NUEVA_RUTA_XML);

			{
				ArrayList<Element> listaElements = nodeListToArray(lector.getNodosRaiz());

				for(Element el: listaElements){
					AparcamientoBicicleta apb = lector.transformElementToAparcamiento(el);
					escritor.addAparcamientoBicicleta(apb);
				}

				System.out.println(escritor.getListaAparcamientos());
			}
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