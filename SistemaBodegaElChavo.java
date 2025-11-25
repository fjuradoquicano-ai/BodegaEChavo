package bodegaelchavo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class SistemaBodegaElChavo extends JFrame {

    //INTERFAZ 
    interface IRegistrable {
        void registrar();
        void mostrar();
    }

    // CLASE ABSTRACTA
    static abstract class Persona implements IRegistrable {
        protected String nombre;
        protected String telefono;
        protected String direccion;

        public abstract void mostrarRol();
    }

    //EMPLEADO (HEREDA DE PERSONA)
    static class Empleado extends Persona {

        private int dni;
        private String cargo;

        @Override
        public void registrar() {
            Scanner sc = new Scanner(System.in);
            System.out.println(" REGISTRO DE EMPLEADO (CONSOLA) ");
            System.out.print("Nombre: ");
            nombre = sc.nextLine();
            System.out.print("DNI: ");
            dni = sc.nextInt();
            sc.nextLine();
            System.out.print("Cargo: ");
            cargo = sc.nextLine();
            System.out.print("Teléfono: ");
            telefono = sc.nextLine();
            System.out.print("Dirección: ");
            direccion = sc.nextLine();
        }

        @Override
        public void mostrar() {
            System.out.println("Empleado: " + nombre);
            System.out.println("DNI: " + dni);
            System.out.println("Cargo: " + cargo);
            System.out.println("Teléfono: " + telefono);
            System.out.println("Dirección: " + direccion);
        }

        @Override
        public void mostrarRol() {
            System.out.println("Rol: Empleado de la Bodega");
        }
    } // fin Empleado

    // CATEGORIA
    static class Categoria {
        private String nombre;
        private String descripcion;

        public void registrarCategoriaConsola() {
            Scanner sc = new Scanner(System.in);
            System.out.println("REGISTRO DE CATEGORÍA (CONSOLA) ");
            System.out.print("Nombre categoría: ");
            nombre = sc.nextLine();
            System.out.print("Descripción: ");
            descripcion = sc.nextLine();
        }

        public String getNombre() {
            return nombre;
        }
    } // fin Categoria

       //  PRODUCTO 
    static class Producto {
        private String nombre;
        private String descripcion;   
        private double precio;
        private int stock;
        private Categoria categoria;
        public void registrarProductoConsola() {
            // OJO: aquí NO hay marca
            Scanner obj_scanner= new Scanner(System.in);
            categoria = new Categoria();
            categoria.registrarCategoriaConsola();
            System.out.print("Nombre del producto: ");
            nombre = obj_scanner.nextLine();   // Ej: "Papel higiénico Paracas"
            System.out.print("Descripción (ej. 1L, triple hoja, pack x 6): ");
            descripcion = obj_scanner.nextLine();
            // Precio: aceptamos punto O coma
            System.out.print("Precio: ");
            String precioTexto = obj_scanner.nextLine();
            precioTexto = precioTexto.replace(',', '.'); // 3,5 -> 3.5
            try {
                precio = Double.parseDouble(precioTexto);
            } catch (NumberFormatException e) {
                System.out.println("Precio inválido, se pondrá 0.");
                precio = 0;
            }
            System.out.print("Stock (unidades que tendrás en la bodega): ");
            String stockTexto = obj_scanner.nextLine();
            try {
                stock = Integer.parseInt(stockTexto);
            } catch (NumberFormatException e) {
                System.out.println("Stock inválido, se pondrá 0.");
                stock = 0;
            }
        }
        public String getNombre() { return nombre; }
        public double getPrecio() { return precio; }

        // (si luego quieres mostrar la descripción, puedes agregar un getter)
        public String getDescripcion() { return descripcion; }
    } // fin Producto


    //  DETALLE COMPRA (COMPOSICIÓN)
    static class DetalleCompra {
        private Producto producto;
        private int cantidad;

        public DetalleCompra(Producto p, int cant) {
            this.producto = p;
            this.cantidad = cant;
        }

        public double subtotal() {
            return producto.getPrecio() * cantidad;
        }

        public void mostrar() {
            System.out.println(producto.getNombre() + " x" + cantidad + " = S/ " + subtotal());
        }
    } // fin DetalleCompra  

    // COMPRA (simple y corregida)
static class Compra implements IRegistrable {

    private int id;
    private String proveedor;
    private String fecha;
    private java.util.List<DetalleCompra> detalles = new java.util.ArrayList<>();
    private double total;

    @Override
    public void registrar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("REGISTRO DE COMPRA");
        System.out.print("ID Compra: ");
        id = sc.nextInt();
        sc.nextLine();

        System.out.print("Proveedor: ");
        proveedor = sc.nextLine();

        System.out.print("Fecha: ");
        fecha = sc.nextLine();

        System.out.print("Cantidad de productos: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            System.out.println("--- Producto #" + (i + 1) + " ---");
            Producto p = new Producto();
            p.registrarProductoConsola();

            System.out.print("Cantidad: ");
            int cant = sc.nextInt();
            sc.nextLine();

            detalles.add(new DetalleCompra(p, cant));
            total += p.getPrecio() * cant;
        }
    }

    @Override
    public void mostrar() {
        System.out.println("\nDETALLE COMPRA");
        System.out.println("ID: " + id);
        System.out.println("Proveedor: " + proveedor);
        System.out.println("Fecha: " + fecha);
        System.out.println("Total: S/ " + total);

        for (DetalleCompra d : detalles) {
            d.mostrar();
        }
    }
}
    //  VENTA + ARCHIVO (FILES) 
    static class Venta implements IRegistrable {

        private int id;
        private String cliente;
        private double total;

        @Override
        public void registrar() {
            Scanner sc = new Scanner(System.in);
            System.out.println("REGISTRO DE VENTA (CONSOLA) ");
            System.out.print("ID Venta: ");
            id = sc.nextInt();
            sc.nextLine();
            System.out.print("Cliente: ");
            cliente = sc.nextLine();
            System.out.print("Total: ");
            total = sc.nextDouble();
        }

        @Override
        public void mostrar() {
            System.out.println("Venta ID: " + id + " Cliente: " + cliente + " Total: S/ " + total);
        }

        public String aLineaArchivo() {
            return id + ";" + cliente + ";" + total;
        }
    } // fin Venta

    static class ArchivoVentas {
        private static final String ARCHIVO = "ventas.txt";

        public static void guardar(Venta v) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true));
                bw.write(v.aLineaArchivo());
                bw.newLine();
                bw.close();
                System.out.println("Venta guardada en archivo.");
            } catch (IOException e) {
                System.out.println("Error al guardar venta: " + e.getMessage());
            }
        }

        public static void mostrarVentas() {
            File f = new File(ARCHIVO);
            if (!f.exists()) {
                System.out.println("No hay ventas guardadas.");
                return;
            }
            System.out.println(" VENTAS EN ARCHIVO ");
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String linea;
                while ((linea = br.readLine()) != null) {
                    System.out.println(linea);
                }
                br.close();
            } catch (IOException e) {
                System.out.println("Error al leer archivo: " + e.getMessage());
            }
        }
    } // fin ArchivoVentas

    // CAJERO
    static class Cajero {
        private double totalVentas;
        private double totalGastos;

        public void registrarVenta(double monto) {
            totalVentas += monto;
        }

        public void registrarGasto(double monto) {
            totalGastos += monto;
        }

        public void mostrarBalance() {
            double ganancia = totalVentas - totalGastos;
            System.out.println(" BALANCE DEL DÍA ");
            System.out.println("Ventas: S/ " + totalVentas);
            System.out.println("Gastos: S/ " + totalGastos);
            System.out.println("Ganancia: S/ " + ganancia);
        }
    } // fin Cajero

    // GUI (JFRAME + JDESKTOPPANE)
    private JDesktopPane desktop;

    public SistemaBodegaElChavo() {
        setTitle("Bodega El Chavo - Sistema Completo (Todo en uno)");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        desktop = new JDesktopPane();
        setContentPane(desktop);

        crearMenu();
    }
    private void crearMenu() {
        JMenuBar barra = new JMenuBar();

        JMenu menuMantenimiento = new JMenu("Mantenimiento");

        JMenuItem mEmpleado = new JMenuItem("Empleado (consola)");
        JMenuItem mCompra = new JMenuItem("Compra (consola)");
        JMenuItem mVenta = new JMenuItem("Venta (archivo)");
        JMenuItem mVerArchivo = new JMenuItem("Ver ventas archivo");
        JMenuItem mFrmCategoria = new JMenuItem("Frm Categoría (GUI)");
        JMenuItem mFrmVenta = new JMenuItem("Frm Venta (GUI)");

        // Acciones consola (POO)
        mEmpleado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Empleado emp = new Empleado();
                emp.registrar();
                emp.mostrar();
                emp.mostrarRol();
            }
        });
        mCompra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Compra c = new Compra();
                c.registrar();
                c.mostrar();
            }
        });
      mVenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Venta v = new Venta();
                v.registrar();
                v.mostrar();
                ArchivoVentas.guardar(v);
            }
        });

        mVerArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArchivoVentas.mostrarVentas();
            }
        });

        // GUI interna
        mFrmCategoria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInternal(new FrmCategoriaInternal());
            }
        });

        mFrmVenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInternal(new FrmVentaInternal());
            }
        });

        menuMantenimiento.add(mEmpleado);
        menuMantenimiento.add(mCompra);
        menuMantenimiento.add(mVenta);
        menuMantenimiento.add(mVerArchivo);
        menuMantenimiento.addSeparator();
        menuMantenimiento.add(mFrmCategoria);
        menuMantenimiento.add(mFrmVenta);

        barra.add(menuMantenimiento);
        setJMenuBar(barra);
    }

    private void abrirInternal(JInternalFrame frame) {
        desktop.add(frame);
        frame.setVisible(true);
    }

    // JINTERNALFRAME CATEGORIA (GUI) 
    static class FrmCategoriaInternal extends JInternalFrame {
        private JTextField txtNombre;
        private JTextArea txtDescripcion;

        public FrmCategoriaInternal() {
            super("Registro de Categoría (GUI)", true, true, true, true);
            setSize(400, 250);
            setLayout(new BorderLayout());

            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            panel.add(new JLabel("Nombre:"));
            txtNombre = new JTextField();
            panel.add(txtNombre);

            panel.add(new JLabel("Descripción:"));
            txtDescripcion = new JTextArea(3, 20);
            panel.add(new JScrollPane(txtDescripcion));

            JButton btnGuardar = new JButton("Guardar");
            btnGuardar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    guardar();
                }
            });

            add(panel, BorderLayout.CENTER);
            add(btnGuardar, BorderLayout.SOUTH);
        }

        private void guardar() {
            String nombre = txtNombre.getText();
            String desc = txtDescripcion.getText();
            JOptionPane.showMessageDialog(this,
                    "Categoría registrada:\n" + nombre + "\n" + desc,
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    } // fin FrmCategoriaInternal

    //  JINTERNALFRAME VENTA (GUI)
    static class FrmVentaInternal extends JInternalFrame {
        private JTextField txtId, txtCliente, txtTotal;

        public FrmVentaInternal() {
            super("Registro de Venta (GUI + archivo)", true, true, true, true);
            setSize(400, 200);
            setLayout(new GridLayout(4, 2, 5, 5));

            txtId = new JTextField();
            txtCliente = new JTextField();
            txtTotal = new JTextField();

            add(new JLabel("ID Venta:"));
            add(txtId);
            add(new JLabel("Cliente:"));
            add(txtCliente);
            add(new JLabel("Total:"));
            add(txtTotal);

            JButton btnGuardar = new JButton("Guardar");
            btnGuardar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    guardarVentaGui();
                }
            });
            add(new JLabel());
            add(btnGuardar);
        }
        private void guardarVentaGui() {
            try {
                int id = Integer.parseInt(txtId.getText());
                String cliente = txtCliente.getText();
                double total = Double.parseDouble(txtTotal.getText());

                BufferedWriter bw = new BufferedWriter(new FileWriter("ventas.txt", true));
                bw.write(id + ";" + cliente + ";" + total);
                bw.newLine();
                bw.close();
                JOptionPane.showMessageDialog(this, "Venta guardada en archivo.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } // fin FrmVentaInternal
    // MAIN 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SistemaBodegaElChavo frm = new SistemaBodegaElChavo();
                frm.setVisible(true);
            }
        });
    }
} // fin SistemaBodegaElChavo
