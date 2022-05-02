package gestor;

import java.io.File;

import exception.XMLException;
import modelo.AparcamientoBicicleta;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public final class LectorXML {
	private File ficheroOrigen;
	private Element raiz;

	/**
	 * Metodo leerArchivoXML
	 * <hr/>
	 * Lee el archivo xml, y lo guarda en fichero origen. Si este es valido,
	 * Saca tambien la raiz del documento.
	 *
	 * @param ruta Ruta donde se encuentra el archivo XML.
	 * @throws XMLException si la ruta no es valida.
	 */
	public void leerArchivoXML(String ruta) throws XMLException {
		setRutaOrigen(ruta);
		Logger.print("Fichero abierto correctamente.");
		setRaiz();
		Logger.print("Raiz obtenida correctamente.");
	}

	/**
	 * metodo setRutaOrigen
	 * <hr/>
	 * Encargado de comprobar si la ruta es valida.
	 * Si lo es se creara un fichero con la ruta indicada.
	 *
	 * @param rutaOrigen Ruta del archivo.
	 * @throws XMLException Si la ruta es nula o una cadena vacia.
	 */
	private void setRutaOrigen(String rutaOrigen) throws XMLException{
		if(rutaOrigen == null || rutaOrigen.length() == 0){
			throw new XMLException("La ruta no es valida.");
		}
		setFicheroOrigen(rutaOrigen);
	}

	/**
	 * Metodo setFicheroOrigen
	 * <hr/>
	 * Crea un objeto File con la ruta comprobada en el medoto setRutaOrigen.
	 *
	 * @param rutaOrigen ruta del fichero XML
	 * @throws XMLException si el fichero abierto en la ruta indicada no existe o es un directorio.
	 */
	private void setFicheroOrigen(String rutaOrigen)throws XMLException {
		File f = new File(rutaOrigen);
		if(!f.exists() || f.isDirectory()){
			throw new XMLException("La ruta es un directorio o no existe.");
		}
		ficheroOrigen = f;
	}

	/**
	 * metodo setRaiz
	 * <hr/>
	 * Obtiene la raiz del documento xml, si la ruta y el archivo abierto son validos.
	 *
	 * @throws XMLException si la raiz no ha podido ser obtenida.
	 */
	private void setRaiz() throws XMLException {
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			raiz = db.parse(ficheroOrigen).getDocumentElement();
		} catch (IOException ex) {
			throw new XMLException("Error al leer el archivo XML");
		} catch (ParserConfigurationException ex) {
			throw new XMLException("Error al leer el archivo XML");
		} catch (SAXException ex) {
			throw new XMLException("Error al leer el archivo XML");
		}
	}

	/**
	 * Metodo getNodosPrincipales
	 * <hr/>
	 * Obtiene todos los elementos con la tag principal del documento.
	 * Tambien, comprobara si este es valido.
	 *
	 * @return nodeList con los tags obtenidos.
	 * @throws XMLException si ha habido un problema con la comprobacion del xml.
	 */
	public NodeList getNodosPrincipales() throws XMLException {
		NodeList nl = raiz.getElementsByTagName("aparcamiento-bicicleta");
		compruebaLongitudValida(nl);
		Logger.print("Cantidad de nodos validada correctamente");
		return nl;
	}

	/**
	 * Metodo compruebaLongitudValida
	 * <hr/>
	 * este metodo obtendra la tag "rows" del documento xml. Esta tag es
	 * encargada de indicar cuantos tag de aparcamientos hay en el documento. <br/>
	 * Si la cantidad indicada en este tag y la longitud de la nodelist son diferentes,
	 * hay un problema con el documento o con el programa.
	 *
	 * @param nl NodeList de las tags Principales
	 * @throws XMLException si la longitud no es la misma en ambos casos.
	 */
	private void compruebaLongitudValida(NodeList nl) throws XMLException {
		Node cuenta = raiz.getElementsByTagName("rows").item(0);
		try{
			Integer rows = Integer.valueOf(cuenta.getTextContent());
			Integer longitud = nl.getLength();

			Logger.print("Nodos extraidos. Cantidad: " + longitud);
			Logger.print("Comprobando validez. Requerido: " + rows);

			if(!rows.equals(longitud)){
				throw new XMLException("El archivo XML es invalido.");
			}
		}catch(NumberFormatException e){
			throw new XMLException("Ha ocurrido un problema al analizar el documento.");
		}
	}

	/**
	 * metodo transformElementToAparcamiento
	 * <hr/>
	 * Crea dos arrays.
	 * <ul>
	 *     <li>Uno con la lista de Tags que deberia contener cada Tag principal.</li>
	 *     <li>Otro con lo que sera la informacion obtenida de cada tag principal.</li>
	 * </ul>
	 * Rellena el segundo array, recorriendo el primero y obteniendo las tags en ese orden. <br/>
	 * La tag obtenida se guardara en un nodelist, el cual si la longitud es 0, se guardara null en el segundo array,
	 * y si es mas de 1 se guardara la informacion unicamente del primero.
	 *
	 * @param elmnt Elemento que corresponde a una tag principal del archivo xml leido.
	 * @return objeto creado con la inforacion obtenida.
	 */
	public AparcamientoBicicleta transformElementToAparcamiento(Element elmnt) throws XMLException {
		String[] listaTagNames = new String[]{"id", "title", "tipo", "plazas", "anclajes", "lastUpdated","icon"};
		String[] informacionAparcamiento = new String[listaTagNames.length];
		for(int i=0; i<listaTagNames.length; i++){
			NodeList nodes = elmnt.getElementsByTagName(listaTagNames[i]);
			if(nodes.getLength() == 0){
				informacionAparcamiento[i] = null;
			}else{
				informacionAparcamiento[i] = nodes.item(0).getTextContent();
				if(i == 0){
					Logger.print("Transformando aparcamiento con id: " + informacionAparcamiento[i]);
				}
			}
		}
		AparcamientoBicicleta apb = new AparcamientoBicicleta(informacionAparcamiento);
		Logger.print("Transformado correctamente.");
		return apb;
	}
}
