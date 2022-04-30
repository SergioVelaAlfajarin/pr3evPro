package modelo;

import exception.XMLException;

import java.util.InputMismatchException;

public final class AparcamientoBicicleta implements Comparable{
	private Integer id;
	private String title;
	private String tipo;
	private Integer plazas;
	private Integer anclajes;
	private String lastUpdated;
	private String icon;

	public AparcamientoBicicleta(String[] info) throws XMLException {
		if(info.length != 7){
			throw new XMLException("Ha habido un problema al crear los objetos AparcamientoBicicleta.");
		}
		init(info);
	}


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
			throw new XMLException("Ha habido un problema al crear los objetos AparcamientoBicicleta.");
		}
	}

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
	public int compareTo(Object o) {
		//numero de bicis (plazas)
		//si es igual
		//por calle
		AparcamientoBicicleta apb = (AparcamientoBicicleta)o;

		int resultado = plazas.compareTo(apb.plazas);

		if(resultado == 0){
			return title.compareTo(apb.title);
		}

		return resultado;
	}
}
