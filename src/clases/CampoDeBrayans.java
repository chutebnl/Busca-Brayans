package clases;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CampoDeBrayans extends JPanel {
	/**
	 *un comentario de prueba para github 18/07/2019 
	 */
	private static final long serialVersionUID = 1L;
	private int filas, columnas, cantidadDeBrayans, ranchosMarcados, ranchosNoMinadas, ranchosActivados;
	private Rancho[][] rancho;
	private BuscaBrayans buscabrayans;
	private ImageIcon imagenDelBrayan;
	public static final boolean EMPEZO = true;

	public CampoDeBrayans(int f, int c, int m, BuscaBrayans b, ImageIcon img) {
		filas = f;
		columnas = c;
		cantidadDeBrayans = m;
		buscabrayans = b;
		this.imagenDelBrayan = img;

		ranchosMarcados = 0;
		ranchosNoMinadas = (f * c) - m;
		ranchosActivados = 0;

		rancho = new Rancho[filas][columnas];
		setLayout(new GridLayout(filas, columnas));

		cargarCampo();
	}

	// ------------------------------------------------------------------------------------------------------------

	public int getCantidadDeBrayans() {
		return cantidadDeBrayans;
	}

	public void setCantidadDeBrayans(int cantidadDeBrayans) {
		this.cantidadDeBrayans = cantidadDeBrayans;
	}

	public int getRanchosMarcados() {
		return ranchosMarcados;
	}

	public void setRanchosMarcados(int ranchosMarcadas) {
		this.ranchosMarcados = ranchosMarcadas;
	}

	public Rancho[][] getCampo() {
		return rancho;
	}

	public void setCampo(Rancho[][] campo) {
		this.rancho = campo;
	}

	public int getFilas() {
		return filas;
	}

	public void setFilas(int filas) {
		this.filas = filas;
	}

	public int getColumnas() {
		return columnas;
	}

	public void setColumnas(int columnas) {
		this.columnas = columnas;
	}

	public int getMinas() {
		return cantidadDeBrayans;
	}

	public void setMinas(int minas) {
		this.cantidadDeBrayans = minas;
	}

	// ------------------------------------------------------------------------------------------------------------

	public void aumentarRanchosMarcados() {
		ranchosMarcados++;
	}

	public void disminuirRanchosMarcados() {
		ranchosMarcados--;
	}

	public void seActivoRancho() {
		ranchosActivados++;
		if (ranchosActivados == ranchosNoMinadas) {
			buscabrayans.ganar();
		}
	}

	private void cargarCampo() {
		if (rancho != null) {
			for (int i = 0; i < rancho.length; i++) {
				for (int j = 0; j < rancho[0].length; j++) {
					rancho[i][j] = new Rancho(this, false, buscabrayans);
					this.add(rancho[i][j]);
				}
			}
			establecerVecinos();
			cargarBrayans();
		}
	}

	public void cargarBrayans() {
		int contador = 1;
		int posX, posY;
		while (contador <= cantidadDeBrayans) {
			posX = (int) (Math.random() * filas);
			posY = (int) (Math.random() * columnas);
			if (!rancho[posX][posY].hayBrayan()) {
				rancho[posX][posY].setHayBrayans(true);
				contador++;
			}
		}
	}

	private void establecerVecinos() {
		for (int i = 0; i < rancho.length; i++) {
			for (int j = 0; j < rancho[0].length; j++) {
				rancho[i][j].establecerVecinos(rancho, i, j);
			}
		}
	}

	public void reiniciar() {
		for (int i = 0; i < rancho.length; i++) {
			for (int j = 0; j < rancho[0].length; j++) {
				rancho[i][j].reestablecerValores();
			}
		}
		cargarBrayans();
		establecerVecinos();
		ranchosMarcados = 0;
		ranchosActivados = 0;
	}

	public void mostrarBrayansYBloquearRanchos() {
		for (int i = 0; i < rancho.length; i++) {
			for (int j = 0; j < rancho[0].length; j++) {
				rancho[i][j].setActivado(false);
				if (rancho[i][j].hayBrayan()) {
					// rancho[i][j].setVisitado(true);
					rancho[i][j].setIcon(imagenDelBrayan);
					rancho[i][j].setBackground(Color.RED);
					if (rancho[i][j].isMarcadoSospechoso()) {
						rancho[i][j].setBackground(Color.GREEN);
					}
				}
			}
		}
	}
}
