package clases;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;

public class BuscaBrayans extends JFrame {

	private static final long serialVersionUID = 1L;
	private JMenuBar barraMenu;
	private JMenu menuDificultad, mas;
	private JMenuItem miFacil, miMedio, miDificil, miPersonalizar, acercaDe;
	private CampoDeBrayans campoFacil, campoMedio, campoDificil, campoActual, campoPersonalizado;
	private EventoCambiarDificultad cambiarDif;
	private JButton bReiniciar;
	private JPanel panelDeJuego, panelMonitor, panelDeCampo;
	private JLabel marcadorDeMinas, labelTiempo;
	private Timer temporizador;
	private int segundos;
	private ImageIcon brayan25, brayan31, brayan51;
	private int filasPersolalizadas, columnasPersonalizadas; 

	public BuscaBrayans(int filas, int col, int cm) {

		iniciarComponentes();
		setSegundos(0);
		temporizador = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				segundos = (segundos + 1) % 1000;
				String s = "";
				if (segundos < 100) {
					s += "0";
					if (segundos < 10) {
						s += "0" + segundos;
					} else {
						s += "" + segundos;
					}
				} else {
					s = "" + segundos;
				}
				labelTiempo.setText(s);
			}
		});
		this.setIconImage(brayan51.getImage());
		this.setTitle("Busca Brayans");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(516, 598));
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}

	private void iniciarComponentes() {
		cambiarDif = new EventoCambiarDificultad();

		brayan25 = new ImageIcon("src/imgs/brayan_25x25.png");
		brayan31 = new ImageIcon("src/imgs/brayan_31x31.png");
		brayan51 = new ImageIcon("src/imgs/brayan_51x51.png");

		campoFacil = new CampoDeBrayans(10, 10, 10, this, brayan51);
		campoMedio = new CampoDeBrayans(16, 16, 40, this, brayan31);
		campoDificil = new CampoDeBrayans(20, 20, 80, this, brayan25);
		campoActual = campoMedio;

		panelDeJuego = new JPanel(new BorderLayout());

		panelDeCampo = new JPanel(new BorderLayout());
		panelDeCampo.add(campoActual, BorderLayout.CENTER);

		panelMonitor = new JPanel(new FlowLayout(FlowLayout.CENTER));
		marcadorDeMinas = new JLabel("00/" + campoActual.getCantidadDeBrayans());

		labelTiempo = new JLabel("000");
		panelMonitor.add(labelTiempo);
		panelMonitor.add(new JSeparator());
		panelMonitor.add(new JSeparator());
		panelMonitor.add(new JSeparator());
		panelMonitor.add(new JSeparator());
		bReiniciar = new JButton("Reiniciar");

		bReiniciar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				reiniciar();
			}
		});

		panelMonitor.add(bReiniciar);
		panelMonitor.add(new JSeparator());
		panelMonitor.add(new JSeparator());
		panelMonitor.add(new JSeparator());
		panelMonitor.add(new JSeparator());
		panelMonitor.add(marcadorDeMinas);

		panelDeJuego.add(panelDeCampo, BorderLayout.CENTER);
		panelDeJuego.add(panelMonitor, BorderLayout.NORTH);

		this.add(panelDeJuego);

		barraMenu = new JMenuBar();
		menuDificultad = new JMenu("Dificuldad");

		miFacil = new JMenuItem("Facil");
		miFacil.addActionListener(cambiarDif);
		miMedio = new JMenuItem("Medio");
		miMedio.addActionListener(cambiarDif);
		miDificil = new JMenuItem("Dificil");
		miDificil.addActionListener(cambiarDif);
		miPersonalizar = new JMenuItem("Personalizar");
		miPersonalizar.addActionListener(cambiarDif);

		menuDificultad.add(miFacil);
		menuDificultad.add(miMedio);
		menuDificultad.add(miDificil);
		menuDificultad.add(miPersonalizar);
		barraMenu.add(menuDificultad);

		mas = new JMenu("Qu� es este bot�n?");
		acercaDe = new JMenuItem("Acerca de");
		acercaDe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(BuscaBrayans.this,
						"Un busca Brayan, realizado por Braian Ledantes\n(la idea de buscar Brayans fue de Jose)",
						"Hola gato", JOptionPane.PLAIN_MESSAGE);
			}
		});
		mas.add(acercaDe);
		barraMenu.add(mas);
		this.getContentPane().add(barraMenu, BorderLayout.NORTH);

	}

	// ------------------------------------------------------------------------------------
	public void iniciar() {
		temporizador.restart();
		actualizarContadorDeBrayans();
	}

	public void perder() {
		campoActual.mostrarBrayansYBloquearRanchos();
		temporizador.stop();
	}

	public void ganar() {
		temporizador.stop();
		segundos = 0;
		JOptionPane.showMessageDialog(this, "Encontraste todos los Brayans, \nte ganaste 5 pe pal guiso\nATR amego",
				"Ganaste", JOptionPane.PLAIN_MESSAGE);
	}

	public void reiniciar() {
		campoActual.reiniciar();

		reiniciarTemporizador();
		actualizarContadorDeBrayans();
		panelMonitor.paintComponents(panelMonitor.getGraphics());
		temporizador.stop();
	}

	public void actualizarContadorDeBrayans() {
		String can;
		if (campoActual.getRanchosMarcados() < 10) {
			can = "0" + campoActual.getRanchosMarcados();
		} else {
			can = "" + campoActual.getRanchosMarcados();
		}
		can += "/" + campoActual.getCantidadDeBrayans();
		marcadorDeMinas.setText(can);
	}

	public void reiniciarTemporizador() {
		temporizador.stop();
		segundos = 0;
		labelTiempo.setText("000");
	}

	// ------------------------------------------------------------------------------------
	public int getSegundos() {
		return segundos;
	}

	public void setSegundos(int segundos) {
		this.segundos = segundos;
	}

	// ------------------------------------------------------------------------------------
	public static void main(String[] args) {
		System.out.println("Arranca el Busca Brayans ATR");
		BuscaBrayans buscaminas = new BuscaBrayans(20, 20, 150);
		buscaminas.setVisible(true);
	}

	// ------------------------------------------------------------------------------------
	class EventoCambiarDificultad implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			campoActual.reiniciar();
			panelDeCampo.removeAll();
			reiniciarTemporizador();
			if (e.getSource() == miFacil) {
				campoActual = campoFacil;
				panelDeCampo.add(campoActual, BorderLayout.CENTER);
			}
			if (e.getSource() == miMedio) {
				campoActual = campoMedio;
				panelDeCampo.add(campoActual, BorderLayout.CENTER);
			}
			if (e.getSource() == miDificil) {
				campoActual = campoDificil;
				panelDeCampo.add(campoActual, BorderLayout.CENTER);
			}
			if (e.getSource() == miPersonalizar) {
				VentanaDeCampoPersonalizado perso = new VentanaDeCampoPersonalizado();
				if (perso.getOpcionSeleccionada() == 1) {
					System.out.println("filas: " + filasPersolalizadas + "\ncolumnas: " + columnasPersonalizadas);
					
				}
				
			}
			actualizarContadorDeBrayans();
			paintComponents(getGraphics());
		}
	}
	// -------------------------------------------------------------------------------------

	class VentanaDeCampoPersonalizado extends JFrame {

		private JButton btnAceptar, btnCancelar;
		private JLabel labelFilas, labelColumnas, labelMinas;
		private JSpinner filas, columnas, minas;
		public static final int ACEPTAR_OPCION = 1, CANCELAR_OPCION = 0;
		private int opcionSeleccionada = -1;
		int cantFilas, cantCol;

		public VentanaDeCampoPersonalizado() {
			this.setTitle("Personalizar Dificultad");
			this.setSize(300, 300);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setLocationRelativeTo(BuscaBrayans.this);
			this.setResizable(false);

			btnAceptar = new JButton("Aceptar");
			btnCancelar = new JButton("Cancelar");
			labelFilas = new JLabel("cantidad de filas");
			labelColumnas = new JLabel("cantidad de columnas");
			labelMinas = new JLabel("cantidad de minas");

			filas = new JSpinner(new SpinnerNumberModel(20, 20, 30, 2));

			columnas = new JSpinner(new SpinnerNumberModel(20, 20, 30, 2));
			minas = new JSpinner(new SpinnerNumberModel(20, 1, 300, 1));
			JPanel panel = new JPanel(new GridLayout(4, 2));
			this.setContentPane(panel);

			btnAceptar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					filasPersolalizadas = (int) filas.getValue();
					columnasPersonalizadas = (int) columnas.getValue();
					campoPersonalizado = new CampoDeBrayans(filasPersolalizadas, columnasPersonalizadas, (int)minas.getValue(), BuscaBrayans.this, null);
					campoActual = campoPersonalizado;
					panelDeCampo.add(campoActual, BorderLayout.CENTER);
					BuscaBrayans.this.paintComponents(BuscaBrayans.this.getGraphics());
					dispose();
				}
			});
			
			panel.add(labelFilas);
			panel.add(filas);
			panel.add(labelColumnas);
			panel.add(columnas);
			panel.add(labelMinas);
			panel.add(minas);
			panel.add(btnAceptar);
			panel.add(btnCancelar);

			this.setVisible(true);
		}

		public int getOpcionSeleccionada() {
			return opcionSeleccionada;
		}
	}
}
