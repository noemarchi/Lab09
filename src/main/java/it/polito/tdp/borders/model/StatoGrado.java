package it.polito.tdp.borders.model;

public class StatoGrado 
{
	private Country stato;
	private int grado;
	
	public StatoGrado(Country stato, int grado) {
		super();
		this.stato = stato;
		this.grado = grado;
	}

	public Country getStato() {
		return stato;
	}

	public void setStato(Country stato) {
		this.stato = stato;
	}

	public int getGrado() {
		return grado;
	}

	public void setGrado(int grado) {
		this.grado = grado;
	}

	@Override
	public String toString() 
	{
		return String.format("%s, numero stati continanti=%s", stato.toString().toUpperCase(), grado);
	}
	
	
}
