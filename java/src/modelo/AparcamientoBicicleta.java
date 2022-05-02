package modelo;

import exception.XMLException;

import java.util.InputMismatchException;
import java.util.Objects;

public final class AparcamientoBicicleta implements Comparable<AparcamientoBicicleta>{
	private Integer id;
	private String title;
	private String tipo;
	private Integer plazas;
	private Integer anclajes;
	private String lastUpdated;
	private String icon;

	/**
	 * Constructor AparcamientoBicicleta
	 * <hr/>
	 * Construye un objeto AparcamientoBicicleta con la informacion del array pasado por parametro.
	 *
	 * @param info Array con la informacion del aparcamiento.
	 * @throws XMLException Error si el array no contiene la cantidad exacta de posiciones necesarias para crear el objeto.
	 */
	public AparcamientoBicicleta(String[] info) throws XMLException {
		if(info.length != 7){
			throw new XMLException("Ha habido un problema al crear los objetos AparcamientoBicicleta. Codigo Error: 0027xAPBC");
		}
		init(info);
	}

	/**
	 * metodo Init
	 * <hr/>
	 * Rellena cada variable del objeto con la posicion correspondiente del array.
	 *
	 * @param info array de 7 posiciones
	 * @throws XMLException Error si los datos pasados son incorrectos o no validos.
	 */
	private void init(String[] info) throws XMLException {
		try{
			setId(info[0]);
			setTitle(info[1]);
			setTipo(info[2]);
			setPlazas(info[3]);
			setAnclajes(info[4]);
			setLastUpdated(info[5]);
			setIcon(info[6]);
		}catch(InputMismatchException e){
			throw new XMLException("Ha habido un problema al crear los objetos AparcamientoBicicleta. Codigo Error: 0050xAPIN");
		}
	}

	//SETTERS --------------------------------------------------------------------------------

	private void setId(String id) throws InputMismatchException{
		if(id == null || id.trim().length() == 0){
			this.id = null;
		}else{
			this.id = Integer.parseInt(id);
		}
	}

	private void setTitle(String title) {
		this.title = title;
	}

	private void setTipo(String tipo){
		this.tipo = tipo;
	}

	private void setPlazas(String plazas) throws InputMismatchException {
		if(plazas == null || plazas.trim().length() == 0){
			this.plazas = null;
		}else{
			this.plazas = Integer.parseInt(plazas);
		}
	}

	private void setAnclajes(String anclajes) throws InputMismatchException {
		if(anclajes == null || anclajes.trim().length() == 0){
			this.anclajes = null;
		}else{
			this.anclajes = Integer.parseInt(anclajes);
		}
	}

	private void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	private void setIcon(String icon) {
		this.icon = icon;
	}

	//GETTERS --------------------------------------------------------------------------------

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getTipo() {
		return tipo;
	}

	public Integer getPlazas() {
		return plazas;
	}

	public Integer getAnclajes() {
		return anclajes;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public String getIcon() {
		return icon;
	}

	//OVERRIDE --------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "AparcamientoBicicleta{" +
				"id=" + id +
				", title='" + title + '\'' +
				", tipo='" + tipo + '\'' +
				", plazas=" + plazas +
				", anclajes=" + anclajes +
				", lastUpdated='" + lastUpdated + '\'' +
				", icon='" + icon + '\'' +
				'}';
	}

	@Override
	public int compareTo(AparcamientoBicicleta o) {
		int resultado = plazas.compareTo(o.plazas);
		if(resultado == 0){
			if(title == null || o.title == null){
				return 0;
			}
			return title.compareTo(o.title);
		}
		return resultado;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AparcamientoBicicleta)) return false;

		AparcamientoBicicleta that = (AparcamientoBicicleta) o;

		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
