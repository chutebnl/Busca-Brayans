package clases;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Rancho extends JButton implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CampoDeBrayans campo;
	private BuscaBrayans buscabrayans;
	private boolean hayBrayans;
	private boolean visitado, marcadoSospechoso, activado;
	private ImageIcon imagenSospechoso;
	private ArrayList<Rancho> vecinos;
	private static boolean primero = true;

	public Rancho(CampoDeBrayans camp, boolean minado, BuscaBrayans bus) {
		super();
		this.buscabrayans = bus;
		this.hayBrayans = minado;
		this.campo = camp;
		this.activado = true;
		this.visitado = false;
		this.marcadoSospechoso = false;
		this.vecinos = new ArrayList<Rancho>();

		this.setBackground(Color.WHITE);
		this.visitado = false;

		this.setMargin(new Insets(0, 0, 0, 0));

		this.addMouseListener(this);
	}

	// ------------------------------------------------------------------------------

	public boolean hayBrayan() {
		return hayBrayans;
	}

	public void setHayBrayans(boolean minado) {
		this.hayBrayans = minado;
	}

	public boolean isVisitado() {
		return visitado;
	}

	public void setVisitado(boolean visitado) {
		this.visitado = visitado;
	}

	public boolean isMarcadoSospechoso() {
		return marcadoSospechoso;
	}

	public void setMarcadoSospechoso(boolean marcadoSospechoso) {
		this.marcadoSospechoso = marcadoSospechoso;
		if (marcadoSospechoso) {
			this.setIcon(imagenSospechoso);
			this.setBackground(Color.ORANGE);
			this.campo.aumentarRanchosMarcados();
			//this.visitado = true;
		} else {
			this.setIcon(null);
			this.setBackground(Color.WHITE);
			this.campo.disminuirRanchosMarcados();
			//this.visitado = false;
		}
		this.buscabrayans.actualizarContadorDeBrayans();
	}

	public boolean isActivado() {
		return activado;
	}

	public void setActivado(boolean activado) {
		this.activado = activado;
	}

	// -------------------------------------------------------------------------------

	public void reestablecerValores() {
		visitado = false;
		hayBrayans = false;
		setBackground(Color.WHITE);
		this.setIcon(null);
		// setEnabled(true);
		activado = true;
		marcadoSospechoso = false;
		vecinos = new ArrayList<Rancho>();
		setText("");
		imagenSospechoso = null;
		primero = true;
	}

	public void establecerVecinos(Rancho[][] campos, int x, int y) {
		int maxX, maxY;
		maxX = campos.length - 1;
		maxY = campos[0].length - 1;

		if (x - 1 >= 0)
			vecinos.add(campos[x - 1][y]);
		if (y + 1 <= maxY && x - 1 >= 0)
			vecinos.add(campos[x - 1][y + 1]);
		if (y + 1 <= maxY)
			vecinos.add(campos[x][y + 1]);
		if (x + 1 <= maxX && y + 1 <= maxY)
			vecinos.add(campos[x + 1][y + 1]);
		if (x + 1 <= maxX)
			vecinos.add(campos[x + 1][y]);
		if (x + 1 <= maxX && y - 1 >= 0)
			vecinos.add(campos[x + 1][y - 1]);
		if (y - 1 >= 0)
			vecinos.add(campos[x][y - 1]);
		if (x - 1 >= 0 && y - 1 >= 0)
			vecinos.add(campos[x - 1][y - 1]);
	}

	public void visitarRanchos() {
		int cant = 0;
		if (!this.isVisitado() && !this.isMarcadoSospechoso()) {
			this.visitado = true;
			this.setBackground(Color.GRAY);
			campo.seActivoRancho();
			for (Rancho vecino : vecinos) {
				if (vecino.hayBrayan()) {
					cant += 1;
				} else {
					if (this.getCantidadDeMinasAlrededor() == 0) {
						vecino.visitarRanchos();
					}
				}
			}
		}

		if (cant > 0) {
			this.setText("" + cant);
		}
	}

	private int getCantidadDeMinasAlrededor() {
		int canti = 0;
		for (Rancho vecino : vecinos) {
			canti += (vecino.hayBrayan()) ? 1 : 0;
		}
		return canti;
	}

	private void limpiarAlrededores() {
		if (this.isVisitado()) {
			int r = 0;

			for (Rancho rancho : vecinos) {
				if (rancho != null && ((!rancho.hayBrayan() && rancho.isMarcadoSospechoso())
						|| (rancho.hayBrayan() && !rancho.isMarcadoSospechoso()))) {
					r++;
				} else {
					if (!rancho.hayBrayan()) {
						rancho.visitarRanchos();
					}
				}
			}

			if (r > 0) {
				buscabrayans.perder();
			}
		}
	}

	// -------------------------------------------------------------------------

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (this.activado) {
			if (e.getModifiers() == 16) {
				if (primero) {
					buscabrayans.iniciar();
					primero = false;
				}
				Rancho even = (Rancho) e.getSource();
				if (this.hayBrayan()) {
					buscabrayans.perder();
				} else {
					even.visitarRanchos();
				}
			} else if (e.getModifiers() == 8) {
				this.limpiarAlrededores();
			} else if (e.getModifiers() == 4) {
				if (!this.isVisitado()) {
					if (!this.isMarcadoSospechoso()) {
						this.setMarcadoSospechoso(true);
					} else {
						this.setMarcadoSospechoso(false);
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
