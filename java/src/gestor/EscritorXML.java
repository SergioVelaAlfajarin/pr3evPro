package gestor;

import exception.XMLException;
import modelo.AparcamientoBicicleta;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class EscritorXML {
	private ArrayList<AparcamientoBicicleta> listaAparcamientos = new ArrayList<>();


	public void addAparcamientoBicicleta(AparcamientoBicicleta apb) throws XMLException {
		if(apb == null){
			throw new XMLException("No puedes agregar aparcamientos nulos.");
		}
		listaAparcamientos.add(apb);
	}
	
	public String getListaAparcamientos(){
		if(listaAparcamientos.size() != 0){
			sortListaAparcamientos();
			StringBuilder sb = new StringBuilder();
			for(AparcamientoBicicleta ap: listaAparcamientos){
				sb.append(ap).append("\n");
			}
			return sb.toString();
		}
		return "";
	}

	public void sortListaAparcamientos(){
		Collections.sort(listaAparcamientos);
	}

    /*
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    //Creamos un objeto del constructor de documentos
    DocumentBuilder db = null;
    try {
        //instanciamos el constructor de documentos
        db = dbf.newDocumentBuilder();
        //creamos un generador de DOM
        DOMImplementation generador = db.getDOMImplementation();
        //generamos el documento con la etiqueta raiz
        Document miDoc = generador.createDocument(null, "alumnos", null);
        miDoc.setXmlVersion("1.0");
        miDoc.setXmlStandalone(true);
        //extraigo la raiz
        Element raiz = miDoc.getDocumentElement();
        //Creamos todos los elementos necesarios
        Element etAlumno = miDoc.createElement("alumno");
        Element etNombre = miDoc.createElement("nombre");
        Element etApellido = miDoc.createElement("apellido");
        //Creamos el atributo
        Attr atCod = miDoc.createAttribute("cod");
        //Añadir texto al atributo
        atCod.setTextContent("001");
        //Creamos los nodos de texto
        Text valorNombre = miDoc.createTextNode("Juan");
        Text valorApellido = miDoc.createTextNode("Pérez");
        //organizamos las etiquetas
        etNombre.appendChild(valorNombre);
        etApellido.appendChild(valorApellido);
        etAlumno.setAttributeNode(atCod);
        etAlumno.appendChild(etNombre);
        etAlumno.appendChild(etApellido);
        //añadimos el alumno a la etiqueta raiz
        raiz.appendChild(etAlumno);
        //Creamos el archivo fuente que queremos guardar
        Source fuente = new DOMSource (miDoc);
        //indicamos donde queremos guardar el archivo
        Result resultado = new StreamResult(fichero);
        Transformer transform = TransformerFactory.newInstance().newTransformer();
        transform.setOutputProperty(OutputKeys.INDENT, "yes");
        transform.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transform.transform(fuente, resultado);


    } catch (ParserConfigurationException ex) {
        System.out.println("Imposible generar el documento");
    } catch (TransformerConfigurationException ex) {
        System.out.println("Error con el documento");
    } catch (TransformerException ex) {
        System.out.println("Error guardando el fichero");
    }
    */

	public void escribeAparcamientosEnArchivo(String destino, String invalidos) throws XMLException {
		File archivoDestino = setRutaDestino(destino);
		File archivoInvalidos = setRutaInvalidos(invalidos); // no borrar archivo al escribir

		for(AparcamientoBicicleta apb: listaAparcamientos){
			if(apb.getAnclajes() == 0){
				// -> a invalidos
			}else{
				// -> a normales
			}
		}
	}

	private File setRutaInvalidos(String ruta) throws XMLException {
		if(ruta == null || ruta.length() == 0){
			throw new XMLException("La ruta no es valida.");
		}
		return setFicheroInvalidos(ruta);
	}

	private File setFicheroInvalidos(String ruta) throws XMLException {
		File f = new File(ruta);
		if(f.isDirectory()){
			throw new XMLException("La ruta Destino no es valida.");
		}
		return f;
	}

	private File setRutaDestino(String rutaDestino) throws XMLException {
		if(rutaDestino == null || rutaDestino.length() == 0){
			throw new XMLException("La ruta no es valida.");
		}
		return setFicheroDestino(rutaDestino);
	}

	private File setFicheroDestino(String rutaDestino) throws XMLException {
		File f = new File(rutaDestino);
		if(f.isDirectory()){
			throw new XMLException("La ruta Destino no es valida.");
		}
		return f;
	}
}
