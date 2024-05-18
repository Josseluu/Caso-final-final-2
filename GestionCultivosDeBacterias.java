import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class GestionCultivosDeBacterias extends JFrame {

    private Experimento experimentoActual;
    private DefaultListModel<String> modeloListaPoblaciones;
    private JList<String> listaPoblaciones;
    private JTextArea areaInformacion;

    public GestionCultivosDeBacterias() {
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Gestión de Cultivos de Bacterias");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel panelPrincipal = new JPanel(new BorderLayout());
        add(panelPrincipal);


        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuArchivo = new JMenu("Archivo");
        menuBar.add(menuArchivo);


        JMenuItem itemAbrir = new JMenuItem("Abrir Experimento");
        itemAbrir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirExperimento();
            }
        });
        menuArchivo.add(itemAbrir);

        JMenuItem itemNuevo = new JMenuItem("Nuevo Experimento");
        itemNuevo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nuevoExperimento();
            }
        });
        menuArchivo.add(itemNuevo);


        JMenuItem itemGuardar = new JMenuItem("Guardar");
        itemGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarExperimento(false);
            }
        });
        menuArchivo.add(itemGuardar);


        JMenuItem itemGuardarComo = new JMenuItem("Guardar como");
        itemGuardarComo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarExperimento(true);
            }
        });
        menuArchivo.add(itemGuardarComo);


        modeloListaPoblaciones = new DefaultListModel<>();
        listaPoblaciones = new JList<>(modeloListaPoblaciones);
        listaPoblaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollLista = new JScrollPane(listaPoblaciones);

        areaInformacion = new JTextArea();
        areaInformacion.setEditable(false);
        JScrollPane scrollArea = new JScrollPane(areaInformacion);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollLista, scrollArea);
        panelPrincipal.add(splitPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);


        JButton btnAgregarPoblacion = new JButton("Agregar Población");
        btnAgregarPoblacion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarPoblacion();
            }
        });
        panelBotones.add(btnAgregarPoblacion);

        // Borrar una población de bacterias del experimento actual
        JButton btnBorrarPoblacion = new JButton("Borrar Población");
        btnBorrarPoblacion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borrarPoblacion();
            }
        });
        panelBotones.add(btnBorrarPoblacion);

        // Ver información detallada de una población de bacterias del experimento actual
        JButton btnVerInfoPoblacion = new JButton("Ver Información");
        btnVerInfoPoblacion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verInformacionPoblacion();
            }
        });
        panelBotones.add(btnVerInfoPoblacion);

        // Realizar y visualizar la simulación correspondiente con una de las poblaciones de bacterias del experimento
        JButton btnSimular = new JButton("Simular");
        btnSimular.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                realizarSimulacion();
            }
        });
        panelBotones.add(btnSimular);
    }

    private void abrirExperimento() {
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                experimentoActual = (Experimento) ois.readObject();
                actualizarListaPoblaciones();
                areaInformacion.setText("");
                JOptionPane.showMessageDialog(this, "Experimento cargado con éxito.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al abrir el experimento.");
            }
        }
    }

    private void nuevoExperimento() {
        String nombre = JOptionPane.showInputDialog(this, "Introduce el nombre del experimento:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            experimentoActual = new Experimento(nombre, new Date());
            modeloListaPoblaciones.clear();
            areaInformacion.setText("");
            JOptionPane.showMessageDialog(this, "Nuevo experimento creado.");
        } else {
            JOptionPane.showMessageDialog(this, "Nombre del experimento no válido.");
        }
    }

    private void guardarExperimento(boolean guardarComo) {
        if (experimentoActual == null) {
            JOptionPane.showMessageDialog(this, "No hay ningún experimento para guardar.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        if (guardarComo || experimentoActual.getArchivo() == null) {
            int seleccion = fileChooser.showSaveDialog(this);
            if (seleccion != JFileChooser.APPROVE_OPTION) {
                return;
            }
            experimentoActual.setArchivo(fileChooser.getSelectedFile());
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(experimentoActual.getArchivo()))) {
            oos.writeObject(experimentoActual);
            JOptionPane.showMessageDialog(this, "Experimento guardado con éxito.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar el experimento.");
        }
    }

    private void agregarPoblacion() {
        if (experimentoActual == null) {
            JOptionPane.showMessageDialog(this, "Primero crea un experimento.");
            return;
        }

        String nombre = JOptionPane.showInputDialog(this, "Introduce el nombre de la población:");
        int numeroDeBacterias = Integer.parseInt(JOptionPane.showInputDialog(this, "Introduce el número de bacterias:"));
        int duracionDias = Integer.parseInt(JOptionPane.showInputDialog(this, "Introduce la duración del experimento (días):"));
        String[] patrones = {"Constante", "Incremento Lineal", "Decremento Lineal", "Alterno"};
        String patronComida = (String) JOptionPane.showInputDialog(this, "Selecciona el patrón de comida:", "Patrón de Comida", JOptionPane.QUESTION_MESSAGE, null, patrones, patrones[0]);

        if (nombre != null && !nombre.trim().isEmpty()) {
            PoblacionDeBacterias nuevaPoblacion = new PoblacionDeBacterias(nombre, numeroDeBacterias, duracionDias, patronComida, new Date());
            experimentoActual.agregarPoblacion(nuevaPoblacion);
            actualizarListaPoblaciones();
            JOptionPane.showMessageDialog(this, "Nueva población de bacterias agregada.");
        } else {
            JOptionPane.showMessageDialog(this, "Nombre de la población no válido.");
        }
    }

    private void borrarPoblacion() {
        if (experimentoActual == null) {
            JOptionPane.showMessageDialog(this, "Primero crea un experimento.");
            return;
        }

        String nombre = listaPoblaciones.getSelectedValue();
        if (nombre != null) {
            experimentoActual.eliminarPoblacion(nombre);
            actualizarListaPoblaciones();
            areaInformacion.setText("");
            JOptionPane.showMessageDialog(this, "Población de bacterias eliminada.");
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una población para eliminar.");
        }
    }

    private void verInformacionPoblacion() {
        if (experimentoActual == null) {
            JOptionPane.showMessageDialog(this, "Primero crea un experimento.");
            return;
        }

        String nombre = listaPoblaciones.getSelectedValue();
        if (nombre != null) {
            PoblacionDeBacterias poblacion = experimentoActual.obtenerPoblacion(nombre);
            areaInformacion.setText(poblacion.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una población para ver su información.");
        }
    }

    private void realizarSimulacion() {
        if (experimentoActual == null) {
            JOptionPane.showMessageDialog(this, "Primero crea un experimento.");
            return;
        }

        String nombre = listaPoblaciones.getSelectedValue();
        if (nombre != null) {
            PoblacionDeBacterias poblacion = experimentoActual.obtenerPoblacion(nombre);
            if (poblacion != null) {
                poblacion.realizarSimulacion();
                mostrarResultadosSimulacion(poblacion);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una población para realizar la simulación.");
        }
    }

    private void mostrarResultadosSimulacion(PoblacionDeBacterias poblacion) {
        JFrame frameResultados = new JFrame("Resultados de la Simulación");
        frameResultados.setSize(500, 500);
        frameResultados.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(20, 20));
        int[][][] resultados = poblacion.getBacteriasPorDia();

        for (int dia = 0; dia < resultados.length; dia++) {
            for (int x = 0; x < 20; x++) {
                for (int y = 0; y < 20; y++) {
                    int bacterias = resultados[dia][x][y];
                    JLabel label = new JLabel();
                    label.setOpaque(true);
                    label.setBackground(colorSegunCantidadBacterias(bacterias));
                    panel.add(label);
                }
            }
        }

        frameResultados.add(panel);
        frameResultados.setVisible(true);
    }

    private Color colorSegunCantidadBacterias(int cantidad) {
        if (cantidad >= 20) return Color.RED;
        if (cantidad >= 15) return Color.MAGENTA;
        if (cantidad >= 10) return Color.ORANGE;
        if (cantidad >= 5) return Color.YELLOW;
        if (cantidad >= 1) return Color.GREEN;
        return Color.WHITE;
    }

    private void actualizarListaPoblaciones() {
        modeloListaPoblaciones.clear();
        for (PoblacionDeBacterias poblacion : experimentoActual.getPoblaciones()) {
            modeloListaPoblaciones.addElement(poblacion.getNombre());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GestionCultivosDeBacterias().setVisible(true);
            }
        });
    }
}

class Experimento implements Serializable {
    private String nombre;
    private Date fechaInicio;
    private ArrayList<PoblacionDeBacterias> poblaciones;
    private transient File archivo;

    public Experimento(String nombre, Date fechaInicio) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.poblaciones = new ArrayList<>();
    }

    public void agregarPoblacion(PoblacionDeBacterias poblacion) {
        poblaciones.add(poblacion);
    }

    public void eliminarPoblacion(String nombre) {
        poblaciones.removeIf(p -> p.getNombre().equals(nombre));
    }

    public PoblacionDeBacterias obtenerPoblacion(String nombre) {
        return poblaciones.stream().filter(p -> p.getNombre().equals(nombre)).findFirst().orElse(null);
    }

    public ArrayList<PoblacionDeBacterias> getPoblaciones() {
        return poblaciones;
    }

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }

    public File getArchivo() {
        return archivo;
    }
}

class PoblacionDeBacterias implements Serializable {
    private String nombre;
    private int numeroInicialDeBacterias;
    private int duracionDias;
    private String patronComida;
    private Date fechaCreacion;
    private int[][][] bacteriasPorDia;

    public PoblacionDeBacterias(String nombre, int numeroInicialDeBacterias, int duracionDias, String patronComida, Date fechaCreacion) {
        this.nombre = nombre;
        this.numeroInicialDeBacterias = numeroInicialDeBacterias;
        this.duracionDias = duracionDias;
        this.patronComida = patronComida;
        this.fechaCreacion = fechaCreacion;
        this.bacteriasPorDia = new int[duracionDias][20][20]; // Suponiendo un plato de 20x20
        inicializarPlato();
    }

    private void inicializarPlato() {
        int bacteriasInicialesPorCelda = numeroInicialDeBacterias / 16;
        for (int i = 8; i < 12; i++) {
            for (int j = 8; j < 12; j++) {
                bacteriasPorDia[0][i][j] = bacteriasInicialesPorCelda;
            }
        }
    }

    public void realizarSimulacion() {
        Random random = new Random();
        for (int dia = 1; dia < duracionDias; dia++) {
            for (int x = 0; x < 20; x++) {
                for (int y = 0; y < 20; y++) {
                    int bacterias = bacteriasPorDia[dia - 1][x][y];
                    for (int b = 0; b < bacterias; b++) {
                        int numeroAleatorio = random.nextInt(100);
                        if (numeroAleatorio < 20) {
                            bacteriasPorDia[dia][x][y]--;
                        } else if (numeroAleatorio >= 60) {
                            moverBacteria(dia, x, y, numeroAleatorio);
                        }
                    }
                }
            }
        }
    }

    private void moverBacteria(int dia, int x, int y, int numeroAleatorio) {
        int nuevaX = x;
        int nuevaY = y;

        if (numeroAleatorio < 70) {
            nuevaX = x - 1;
        } else if (numeroAleatorio < 80) {
            nuevaX = x + 1;
        } else if (numeroAleatorio < 90) {
            nuevaY = y - 1;
        } else {
            nuevaY = y + 1;
        }

        if (nuevaX >= 0 && nuevaX < 20 && nuevaY >= 0 && nuevaY < 20) {
            bacteriasPorDia[dia][nuevaX][nuevaY]++;
        }
    }

    public int[][][] getBacteriasPorDia() {
        return bacteriasPorDia;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "PoblacionDeBacterias{" +
                "nombre='" + nombre + '\'' +
                ", numeroInicialDeBacterias=" + numeroInicialDeBacterias +
                ", duracionDias=" + duracionDias +
                ", patronComida='" + patronComida + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
