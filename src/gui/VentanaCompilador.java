package gui;

import compilador.Lexico;
import compilador.parser;
import java_cup.runtime.ComplexSymbolFactory;
import pruebaast.ast.GeneradorAssembler;
import pruebaast.ast.NodoPrograma;
import pruebaast.ast.ResultadoAssembler;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VentanaCompilador extends JFrame {

    private static final long serialVersionUID = 1L;

    // ── Colores y fuentes ──────────────────────────────────────────────────────
    private static final Color BG_DARK = new Color(18, 20, 28);
    private static final Color BG_PANEL = new Color(26, 29, 42);
    private static final Color BG_INPUT = new Color(34, 38, 55);
    private static final Color ACCENT = new Color(99, 179, 237);
    private static final Color ACCENT2 = new Color(72, 199, 142);
    private static final Color TEXT_MAIN = new Color(226, 232, 240);
    private static final Color TEXT_DIM = new Color(113, 128, 150);
    private static final Color BORDER_COLOR = new Color(45, 55, 72);
    private static final Color BTN_HOVER = new Color(66, 153, 225);
    private static final Color ERR_COLOR = new Color(252, 129, 129);

    private static final Font FONT_MONO = new Font("Courier New", Font.PLAIN, 13);
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_BTN = new Font("Segoe UI", Font.BOLD, 13);

    // ── Componentes ────────────────────────────────────────────────────────────
    private JTextField txtRuta;
    private JButton btnSeleccionar;
    private JButton btnEjecutar;
    private JButton btnCompilar;
    private JButton btnLimpiar;
    private String rutaTasm = null;
    private JTextArea taConsola;
    private JTextArea taArbol;
    private JTextArea taCodigo;
    private JLabel lblEstado;

    // ── Constructor ────────────────────────────────────────────────────────────
    public VentanaCompilador() {
        setTitle("Compiladores 2026  —  Generador de AST");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 0));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        redirigirSysOut();
        setVisible(true);
    }

    // ── Main ───────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(VentanaCompilador::new);
    }

    // ── Header ─────────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(16, 0));
        header.setBackground(BG_PANEL);
        header.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(14, 20, 14, 20)));

        // Título izquierda
        JLabel title = new JLabel("⟨/⟩  Compiladores 2026");
        title.setFont(FONT_TITLE);
        title.setForeground(ACCENT);
        header.add(title, BorderLayout.WEST);

        // Panel de selección de archivo
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filePanel.setOpaque(false);

        JLabel lblArchivo = new JLabel("Archivo fuente:");
        lblArchivo.setFont(FONT_LABEL);
        lblArchivo.setForeground(TEXT_DIM);

        txtRuta = new JTextField(30);
        txtRuta.setEditable(false);
        txtRuta.setBackground(BG_INPUT);
        txtRuta.setForeground(TEXT_MAIN);
        txtRuta.setCaretColor(ACCENT);
        txtRuta.setFont(FONT_LABEL);
        txtRuta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        btnSeleccionar = createButton("📂 Seleccionar", ACCENT, BG_INPUT);
        btnEjecutar = createButton("▶  Ejecutar", ACCENT2, new Color(30, 80, 50));
        btnCompilar = createButton("⚙  Compilar .exe", new Color(255, 193, 70), new Color(80, 60, 10));
        btnLimpiar = createButton("✕ Limpiar", TEXT_DIM, BG_INPUT);

        btnSeleccionar.addActionListener(e -> seleccionarArchivo());
        btnEjecutar.addActionListener(e -> ejecutar());
        btnCompilar.addActionListener(e -> compilarExe());
        btnLimpiar.addActionListener(e -> limpiar());

        filePanel.add(lblArchivo);
        filePanel.add(txtRuta);
        filePanel.add(btnSeleccionar);
        filePanel.add(btnEjecutar);
        filePanel.add(btnCompilar);
        filePanel.add(btnLimpiar);

        header.add(filePanel, BorderLayout.CENTER);
        return header;
    }

    // ── Panel central ──────────────────────────────────────────────────────────
    private JSplitPane buildCenter() {
        // Izquierda: código fuente + consola
        JSplitPane leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                buildPanel("📄 Código Fuente", taCodigo = buildTextArea(FONT_MONO, TEXT_MAIN)),
                buildPanel("🖥  Consola / Traza", taConsola = buildTextArea(FONT_MONO, new Color(180, 210, 180))));
        leftSplit.setDividerLocation(250);
        leftSplit.setDividerSize(5);
        leftSplit.setBackground(BG_DARK);
        leftSplit.setBorder(null);

        // Derecha: árbol AST
        JPanel astPanel = buildPanel("🌳 Árbol AST (formato Graphviz DOT)",
                taArbol = buildTextArea(FONT_MONO, new Color(210, 240, 210)));

        JButton btnGraphviz = createButton("🔗 Abrir en Graphviz Online", ACCENT, BG_INPUT);
        btnGraphviz.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnGraphviz.addActionListener(e -> {
            try {
                String dotCode = taArbol.getText();
                if (dotCode == null || dotCode.trim().isEmpty()) {
                    return;
                }
                String encodedDot = java.net.URLEncoder
                        .encode(dotCode, StandardCharsets.UTF_8)
                        .replace("+", "%20");

                String url = "https://dreampuf.github.io/GraphvizOnline/#" + encodedDot;
                java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JPanel astWrapper = new JPanel(new BorderLayout(0, 4));
        astWrapper.setBackground(BG_DARK);
        astWrapper.add(astPanel, BorderLayout.CENTER);
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnBar.setOpaque(false);
        btnBar.add(btnGraphviz);
        astWrapper.add(btnBar, BorderLayout.SOUTH);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplit, astWrapper);
        mainSplit.setDividerLocation(550);
        mainSplit.setDividerSize(5);
        mainSplit.setBackground(BG_DARK);
        mainSplit.setBorder(new EmptyBorder(8, 8, 0, 8));
        return mainSplit;
    }

    // ── Footer ─────────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_PANEL);
        footer.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, BORDER_COLOR),
                new EmptyBorder(6, 16, 6, 16)));
        lblEstado = new JLabel("Listo. Seleccioná un archivo .txt y presioná Ejecutar.");
        lblEstado.setFont(FONT_LABEL);
        lblEstado.setForeground(TEXT_DIM);
        footer.add(lblEstado, BorderLayout.WEST);

        JLabel info = new JLabel("JFlex + CUP  |  AST Grapher");
        info.setFont(FONT_LABEL);
        info.setForeground(TEXT_DIM);
        footer.add(info, BorderLayout.EAST);
        return footer;
    }

    // ── Helpers de UI ─────────────────────────────────────────────────────────
    private JPanel buildPanel(String titulo, JTextArea ta) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(6, 0, 6, 0));

        JLabel lbl = new JLabel("  " + titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(TEXT_DIM);
        lbl.setBorder(new EmptyBorder(0, 0, 4, 0));

        JScrollPane scroll = new JScrollPane(ta);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scroll.getViewport().setBackground(BG_INPUT);
        scroll.setBackground(BG_INPUT);

        p.add(lbl, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JTextArea buildTextArea(Font font, Color fg) {
        JTextArea ta = new JTextArea();
        ta.setFont(font);
        ta.setBackground(BG_INPUT);
        ta.setForeground(fg);
        ta.setCaretColor(ACCENT);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(new EmptyBorder(8, 10, 8, 10));
        ta.setEditable(false);
        return ta;
    }

    private JButton createButton(String texto, Color fg, Color bg) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_BTN);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fg.darker(), 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(fg.darker());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    // ── Redirigir System.out ───────────────────────────────────────────────────
    private void redirigirSysOut() {
        PrintStream ps = new PrintStream(new OutputStream() {
            private final StringBuilder linea = new StringBuilder();

            public void write(int b) {
                char c = (char) b;
                linea.append(c);
                if (c == '\n') {
                    final String txt = linea.toString();
                    SwingUtilities.invokeLater(() -> taConsola.append(txt));
                    linea.setLength(0);
                }
            }
        });
        System.setOut(ps);
        System.setErr(ps);
    }

    // ── Acciones ───────────────────────────────────────────────────────────────
    private void seleccionarArchivo() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccionar programa fuente");
        fc.setFileFilter(new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt"));
        fc.setCurrentDirectory(new File(System.getProperty("user.home")));

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            txtRuta.setText(archivo.getAbsolutePath());
            // Mostrar el código fuente
            try {
                String contenido = new String(Files.readAllBytes(archivo.toPath()));
                taCodigo.setEditable(true);
                taCodigo.setText(contenido);
                taCodigo.setEditable(false);
                taCodigo.setCaretPosition(0);
                setEstado("Archivo cargado: " + archivo.getName(), TEXT_DIM);
            } catch (IOException ex) {
                setEstado("Error al leer el archivo: " + ex.getMessage(), ERR_COLOR);
            }
        }
    }

    private void ejecutar() {
        String ruta = txtRuta.getText().trim();
        if (ruta.isEmpty()) {
            setEstado("⚠  Primero seleccioná un archivo fuente.", ERR_COLOR);
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccioná un archivo .txt primero.",
                    "Sin archivo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Limpiar salidas anteriores
        taConsola.setText("");
        taArbol.setText("");

        setEstado("⟳  Compilando...", ACCENT);
        btnEjecutar.setEnabled(false);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            String dotOutput = "";
            boolean exito = false;

            @Override
            protected Void doInBackground() {
                try {
                    System.out.println("══════════════════════════════════════");
                    System.out.println("  Compiladores 2026 — Inicio");
                    System.out.println("  Archivo: " + ruta);
                    System.out.println("══════════════════════════════════════\n");

                    FileReader fr = new FileReader(ruta);
                    ComplexSymbolFactory sf = new ComplexSymbolFactory();
                    Lexico lexico = new Lexico(fr, sf);
                    parser p = new parser(lexico, sf);

                    System.out.println("[ Análisis léxico y sintáctico ]\n");
                    NodoPrograma arbol = (NodoPrograma) p.parse().value;

                    if (arbol != null) {
                        System.out.println("\n[ AST generado correctamente ✓ ]");
                        dotOutput = arbol.graficar();
                        exito = true;
                    } else {
                        System.out.println("\n[ Error: el árbol es nulo ]");
                    }

                    GeneradorAssembler.reset(); // Por si compilan varias veces seguidas
                    ResultadoAssembler finalAsm = arbol.generarAssembler();

                    // Escribir el string 'finalAsm.getCodigo()' en un archivo "Final.asm"
                    Files.write(Paths.get("Final.asm"), finalAsm.getCodigo().getBytes());

                } catch (Exception ex) {
                    System.out.println("\n[ ERROR ]: " + ex.getMessage());
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                btnEjecutar.setEnabled(true);
                if (exito) {
                    taArbol.setText(dotOutput);
                    taArbol.setCaretPosition(0);
                    setEstado("✓  Compilación exitosa. Árbol AST generado.", ACCENT2);
                } else {
                    setEstado("✗  Se encontraron errores. Revisá la consola.", ERR_COLOR);
                }
            }
        };
        worker.execute();
    }

    private void compilarExe() {
        File finalAsm = new File("Final.asm");
        if (!finalAsm.exists()) {
            setEstado("Primero compilá el fuente para generar Final.asm.", ERR_COLOR);
            return;
        }

        if (rutaTasm == null) {
            try {
                new ProcessBuilder("tasm").start().destroy();
            } catch (IOException ex) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Seleccionar tasm.exe");
                fc.setFileFilter(new FileNameExtensionFilter("Ejecutables (*.exe)", "exe"));
                if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
                rutaTasm = fc.getSelectedFile().getAbsolutePath();
            }
        }

        String tasmPath = rutaTasm != null ? rutaTasm : "tasm";
        String dirTasm = rutaTasm != null ? new File(rutaTasm).getParent() : null;
        String tlinkPath = dirTasm != null ? dirTasm + File.separator + "tlink.exe" : "tlink";

        btnCompilar.setEnabled(false);
        setEstado("Ensamblando...", ACCENT);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            boolean exito = false;

            @Override
            protected Void doInBackground() {
                try {
                    ProcessBuilder pbTasm = new ProcessBuilder(tasmPath, "Final.asm");
                    pbTasm.redirectErrorStream(true);
                    Process pTasm = pbTasm.start();
                    System.out.println(new String(pTasm.getInputStream().readAllBytes()));
                    pTasm.waitFor();

                    if (pTasm.exitValue() != 0) return null;

                    ProcessBuilder pbTlink = new ProcessBuilder(tlinkPath, "Final.obj");
                    pbTlink.redirectErrorStream(true);
                    Process pTlink = pbTlink.start();
                    System.out.println(new String(pTlink.getInputStream().readAllBytes()));
                    pTlink.waitFor();

                    if (pTlink.exitValue() != 0) return null;

                    exito = true;
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                btnCompilar.setEnabled(true);
                if (exito) {
                    setEstado("✓  Final.exe generado.", ACCENT2);
                } else {
                    setEstado("✗  Error al compilar el ejecutable.", ERR_COLOR);
                }
            }
        };
        worker.execute();
    }

    private void limpiar() {
        txtRuta.setText("");
        taCodigo.setText("");
        taConsola.setText("");
        taArbol.setText("");
        setEstado("Listo.", TEXT_DIM);
    }

    private void setEstado(String msg, Color color) {
        lblEstado.setText(msg);
        lblEstado.setForeground(color);
    }
}
