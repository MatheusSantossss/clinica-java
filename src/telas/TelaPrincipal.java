/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.CardLayout;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import model.bean.Idoso;
import model.bean.historico;
import model.bean.responsavel;
import model.bean.usuario;
import model.bean.visitante;
import model.dao.HistoricoDAO;
import model.dao.IdosoDAO;
import model.dao.ResponsavelDAO;
import model.dao.UsuarioDAO;
import model.dao.VisitanteDAO;


/**
 *
 * @author matheus
 */
public class TelaPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form TelaPrincipal
     */
    public TelaPrincipal() {
        initComponents();
        TelaLoginModal tl = new TelaLoginModal(this, true);
        tl.setVisible(true);

        DefaultTableModel modelo = (DefaultTableModel) tbVisitAndamento.getModel();
        tbVisitAndamento.setRowSorter(new TableRowSorter(modelo));
        readJTable();
        readRecepTB();
        readAdminTB();
    }

    public static JMenuItem getMnIAdmin() {
        return mnIAdmin;
        
    }

    public void setMnIAdmin(JMenuItem mnIAdmin) {
        this.mnIAdmin = mnIAdmin;
    }
    
    // Método para preencher a tabela do histórico utilizando os dados do banco
    public void readJTable() {
        DefaultTableModel modelo = (DefaultTableModel) tbHistorico.getModel();
        modelo.setNumRows(0);
        HistoricoDAO hdao = new HistoricoDAO();

        for (historico h : hdao.read()) {
            
            modelo.addRow(new Object[]{
                h.getNome(),
                h.getRg(),
                h.getPlaca(),
                h.getHora_entrada(),
                h.getMotivo(),
                h.getHora_saida()

            });

        }
    }
    
    // Método para preenchimento da tabela de recepcionistas utilizandos os dados do banco
    public void readRecepTB() {
        DefaultTableModel modelo = (DefaultTableModel) tbRecep.getModel();
        modelo.setNumRows(0);
        UsuarioDAO usrdao = new UsuarioDAO();

        for (usuario u : usrdao.readRecep()) {

            modelo.addRow(new Object[]{
                u.getId(),
                u.getNome(),
                u.getMatricula(),
                u.getCargo()

            });

        }
    }
    
    //Método para preenchimento da tabela recepcionistas  pesquisando por nomes
    public void readRecepNome(String nome) {
        DefaultTableModel modelo = (DefaultTableModel) tbRecep.getModel();
        modelo.setNumRows(0);
        UsuarioDAO udao = new UsuarioDAO();

        for (usuario u : udao.readForNomeRecep(nome)) {

            modelo.addRow(new Object[]{
                u.getId(),
                u.getNome(),
                u.getMatricula(),
                u.getCargo()

            });

        }
    }

    // Método para gerar PDF com os dados do banco
    private static void gerarPDF() throws SQLException {

        HistoricoDAO hisdao = new HistoricoDAO();
        Document doc = new Document();
        String arquivoPdf = "historico.pdf";

        try {
            PdfWriter.getInstance(doc, new FileOutputStream(arquivoPdf));
            doc.open();

            Paragraph p = new Paragraph("Histórico de visitas");

            p.setAlignment(1);
            doc.add(p);
            p = new Paragraph("    ");
            doc.add(p);
            PdfPTable table = new PdfPTable(6);

            PdfPCell cel1 = new PdfPCell(new Paragraph("Nome"));
            PdfPCell cel2 = new PdfPCell(new Paragraph("RG"));
            PdfPCell cel3 = new PdfPCell(new Paragraph("Placa do veiculo"));
            PdfPCell cel4 = new PdfPCell(new Paragraph("Hora de Entrada"));
            PdfPCell cel5 = new PdfPCell(new Paragraph("Motivo"));
            PdfPCell cel6 = new PdfPCell(new Paragraph("Hora de Saída"));

            table.addCell(cel1);
            table.addCell(cel2);
            table.addCell(cel3);
            table.addCell(cel4);
            table.addCell(cel5);
            table.addCell(cel6);

            for (historico h : hisdao.read()) {
                 cel1 = new PdfPCell(new Paragraph(h.getNome()+""));
                 cel2 = new PdfPCell(new Paragraph(h.getRg()+""));
                 cel3 = new PdfPCell(new Paragraph(h.getPlaca()+""));
                 cel4 = new PdfPCell(new Paragraph(h.getHora_entrada()+""));
                 cel5 = new PdfPCell(new Paragraph(h.getMotivo()+""));
                 cel6 = new PdfPCell(new Paragraph(h.getHora_saida()+""));

                table.addCell(cel1);
                table.addCell(cel2);
                table.addCell(cel3);
                table.addCell(cel4);
                table.addCell(cel5);
                table.addCell(cel6);
            }
            
            doc.add(table);
            doc.close();
            Desktop.getDesktop().open(new File(arquivoPdf));
        } catch (Exception ex) {

        }
    }

    // Método para preencher a tabela de administradores
    public void readAdminTB() {
        DefaultTableModel modelo = (DefaultTableModel) tbAdmin.getModel();
        modelo.setNumRows(0);
        UsuarioDAO usrdao = new UsuarioDAO();

        for (usuario u : usrdao.readAdmin()) {

            modelo.addRow(new Object[]{
                u.getId(),
                u.getNome(),
                u.getMatricula(),
                u.getCargo()

            });

        }
    }
    // Método para pesquisar um administrador pelo nome
    public void readAdminNome(String nome) {
        DefaultTableModel modelo = (DefaultTableModel) tbAdmin.getModel();
        modelo.setNumRows(0);
        UsuarioDAO udao = new UsuarioDAO();

        udao.readForNomeAdmin(nome).forEach((u) -> {
            modelo.addRow(new Object[]{
                u.getId(),
                u.getNome(),
                u.getMatricula(),
                u.getCargo()

            });
        });
    }

    

    public static JMenu getMnClientes() {
        return mnClientes;
    }

    public static JMenu getMnControle() {
        return mnControle;
    }

    public static JMenu getMnUsuarios() {
        return mnUsuarios;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField6 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        pnlPrincipal = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbHistorico = new javax.swing.JTable();
        pnlControle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pnlDadosControle = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNomeControle = new javax.swing.JTextField();
        txtPlacaControle = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cbMotivoControle = new javax.swing.JComboBox<>();
        txtHoraEntrada = new javax.swing.JFormattedTextField();
        txtRGControle = new javax.swing.JFormattedTextField();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbVisitAndamento = new javax.swing.JTable();
        jButton9 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        pnlIdosos = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtNomeResp = new javax.swing.JTextField();
        spnIdadeResp = new javax.swing.JSpinner();
        txtRGResp = new javax.swing.JFormattedTextField();
        txtTelResp = new javax.swing.JFormattedTextField();
        txtCelResp = new javax.swing.JFormattedTextField();
        txtNomeIdoso = new javax.swing.JTextField();
        spnIdadeIdoso = new javax.swing.JSpinner();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtSitIdoso = new javax.swing.JTextArea();
        txtMedIdoso = new javax.swing.JTextField();
        txtRGIdoso = new javax.swing.JFormattedTextField();
        jButton4 = new javax.swing.JButton();
        pnlVisitantes = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtVisitNome = new javax.swing.JTextField();
        txtIdosoAss = new javax.swing.JTextField();
        txtTelVisit = new javax.swing.JFormattedTextField();
        txtRGVisit = new javax.swing.JFormattedTextField();
        jLabel28 = new javax.swing.JLabel();
        txtPesqIdoso = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        pnlPesquisar = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        pnlRecepcionistas = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbRecep = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtPesqRecep = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        txtNomeAtRecep = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        txtMatriAtUsr = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        cbCargoAtUsr = new javax.swing.JComboBox<>();
        pnlAdministradores = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtPesqAdmin = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbAdmin = new javax.swing.JTable();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        txtNomeAtAdmin = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtMatriAtAdmin = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        cbCargoAtAdmin = new javax.swing.JComboBox<>();
        pnlCadastroUsr = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        txtNomeUsr = new javax.swing.JTextField();
        txtMatriUsr = new javax.swing.JTextField();
        txtSenhaUsr = new javax.swing.JPasswordField();
        cbUsr = new javax.swing.JComboBox<>();
        txtKeyGerente = new javax.swing.JPasswordField();
        jButton7 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnPrincipal = new javax.swing.JMenu();
        mnControle = new javax.swing.JMenu();
        mnClientes = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        mnUsuarios = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        mnIAdmin = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();

        jTextField6.setText("jTextField6");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Clínica");

        jPanel1.setLayout(new java.awt.CardLayout());

        pnlPrincipal.setBackground(new java.awt.Color(34, 49, 63));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pdf-icon-01.png"))); // NOI18N
        jButton1.setText("Gerar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Histórico"));

        tbHistorico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "RG", "Placa do veiculo", "Hora de Entrada", "Motivo", "Hora de Saída"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbHistorico);
        if (tbHistorico.getColumnModel().getColumnCount() > 0) {
            tbHistorico.getColumnModel().getColumn(0).setResizable(false);
            tbHistorico.getColumnModel().getColumn(1).setResizable(false);
            tbHistorico.getColumnModel().getColumn(2).setResizable(false);
            tbHistorico.getColumnModel().getColumn(3).setResizable(false);
            tbHistorico.getColumnModel().getColumn(4).setResizable(false);
            tbHistorico.getColumnModel().getColumn(5).setResizable(false);
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1245, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlPrincipalLayout = new javax.swing.GroupLayout(pnlPrincipal);
        pnlPrincipal.setLayout(pnlPrincipalLayout);
        pnlPrincipalLayout.setHorizontalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        pnlPrincipalLayout.setVerticalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(42, 42, 42))
        );

        jPanel1.add(pnlPrincipal, "principal");

        pnlControle.setBackground(new java.awt.Color(34, 49, 63));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Controle");

        pnlDadosControle.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados"));

        jLabel3.setText("Nome: ");

        jLabel4.setText("RG: ");

        jLabel7.setText("*Placa do veículo:");

        jLabel8.setText("Hora de Entrada:");

        jLabel9.setText("Motivo:");

        cbMotivoControle.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Visita", "Serviços", "Doações" }));

        try {
            txtHoraEntrada.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtRGControle.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout pnlDadosControleLayout = new javax.swing.GroupLayout(pnlDadosControle);
        pnlDadosControle.setLayout(pnlDadosControleLayout);
        pnlDadosControleLayout.setHorizontalGroup(
            pnlDadosControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDadosControleLayout.createSequentialGroup()
                .addGroup(pnlDadosControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDadosControleLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlDadosControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDadosControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPlacaControle, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHoraEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlDadosControleLayout.createSequentialGroup()
                        .addGroup(pnlDadosControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlDadosControleLayout.createSequentialGroup()
                                .addGap(86, 86, 86)
                                .addGroup(pnlDadosControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)))
                            .addGroup(pnlDadosControleLayout.createSequentialGroup()
                                .addGap(84, 84, 84)
                                .addComponent(jLabel9)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDadosControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNomeControle, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbMotivoControle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlDadosControleLayout.createSequentialGroup()
                                .addComponent(txtRGControle)
                                .addGap(411, 411, 411)))))
                .addContainerGap(118, Short.MAX_VALUE))
        );
        pnlDadosControleLayout.setVerticalGroup(
            pnlDadosControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDadosControleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDadosControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNomeControle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDadosControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtRGControle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDadosControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPlacaControle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDadosControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(txtHoraEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDadosControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(cbMotivoControle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jButton2.setText("Registrar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Visitas em Andamento"));

        tbVisitAndamento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome do Visitante", "RG", "Placa do veículo", "Horário de Entrada", "Motivo", "Horário de Saída"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbVisitAndamento);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
        );

        jButton9.setText("Salvar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton12.setText("Encerrar Processo");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlControleLayout = new javax.swing.GroupLayout(pnlControle);
        pnlControle.setLayout(pnlControleLayout);
        pnlControleLayout.setHorizontalGroup(
            pnlControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlControleLayout.createSequentialGroup()
                .addGroup(pnlControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlControleLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton12)
                        .addGap(18, 18, 18)
                        .addComponent(jButton9))
                    .addGroup(pnlControleLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(pnlControleLayout.createSequentialGroup()
                        .addContainerGap(185, Short.MAX_VALUE)
                        .addGroup(pnlControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlDadosControle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(292, Short.MAX_VALUE))
        );
        pnlControleLayout.setVerticalGroup(
            pnlControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlControleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(pnlDadosControle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(66, 66, 66)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9)
                    .addComponent(jButton12))
                .addContainerGap(70, Short.MAX_VALUE))
        );

        jPanel1.add(pnlControle, "controle");

        pnlIdosos.setBackground(new java.awt.Color(34, 49, 63));
        pnlIdosos.setName(""); // NOI18N

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Idosos");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Cadastro de Idosos"));

        jLabel11.setFont(new java.awt.Font("Dialog", 3, 14)); // NOI18N
        jLabel11.setText("Responsável");

        jLabel12.setText("Nome:");

        jLabel13.setText("Idade:");

        jLabel14.setText("RG:");

        jLabel15.setText("Telefone:");

        jLabel16.setText("Celular:");

        jLabel17.setFont(new java.awt.Font("Dialog", 3, 14)); // NOI18N
        jLabel17.setText("Idoso");

        jLabel18.setText("Nome:");

        jLabel19.setText("Idade:");

        jLabel20.setText("RG:");

        jLabel21.setText("Medicamentos:");

        jLabel22.setText("Situação:");

        spnIdadeResp.setModel(new javax.swing.SpinnerNumberModel(18, 18, 120, 1));

        try {
            txtRGResp.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtTelResp.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtCelResp.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) #####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        spnIdadeIdoso.setModel(new javax.swing.SpinnerNumberModel(60, 60, 120, 1));

        txtSitIdoso.setColumns(20);
        txtSitIdoso.setRows(5);
        jScrollPane3.setViewportView(txtSitIdoso);

        try {
            txtRGIdoso.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(463, 463, 463)
                        .addComponent(jLabel17))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtRGResp, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spnIdadeResp, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel16))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtNomeResp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtCelResp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                                        .addComponent(txtTelResp, javax.swing.GroupLayout.Alignment.LEADING)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel21)
                                .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtNomeIdoso)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                        .addComponent(spnIdadeIdoso, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMedIdoso))
                    .addComponent(txtRGIdoso, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel18)
                    .addComponent(txtNomeResp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNomeIdoso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel19)
                    .addComponent(spnIdadeResp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnIdadeIdoso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel20)
                    .addComponent(txtRGResp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRGIdoso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel21)
                    .addComponent(txtTelResp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMedIdoso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtCelResp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(99, Short.MAX_VALUE))
        );

        jButton4.setText("Salvar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlIdososLayout = new javax.swing.GroupLayout(pnlIdosos);
        pnlIdosos.setLayout(pnlIdososLayout);
        pnlIdososLayout.setHorizontalGroup(
            pnlIdososLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlIdososLayout.createSequentialGroup()
                .addContainerGap(81, Short.MAX_VALUE)
                .addGroup(pnlIdososLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlIdososLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1058, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlIdososLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton4)))
                .addContainerGap(83, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlIdososLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlIdososLayout.setVerticalGroup(
            pnlIdososLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlIdososLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addContainerGap(93, Short.MAX_VALUE))
        );

        jPanel1.add(pnlIdosos, "idosos");

        pnlVisitantes.setBackground(new java.awt.Color(34, 49, 63));

        jLabel23.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Visitantes");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Cadastrar Visitantes"));

        jLabel24.setText("Nome:");

        jLabel25.setText("RG:");

        jLabel26.setText("Telefone:");

        jLabel27.setText("Idoso associado:");

        try {
            txtTelVisit.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtRGVisit.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel27))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel25)
                            .addComponent(jLabel26)
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIdosoAss, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
                            .addComponent(txtVisitNome)
                            .addComponent(txtTelVisit, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtRGVisit, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(363, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtIdosoAss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtVisitNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtTelVisit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txtRGVisit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(82, Short.MAX_VALUE))
        );

        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Pesquisar Idoso:");

        jButton5.setText("Pesquisar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Cadastrar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlVisitantesLayout = new javax.swing.GroupLayout(pnlVisitantes);
        pnlVisitantes.setLayout(pnlVisitantesLayout);
        pnlVisitantesLayout.setHorizontalGroup(
            pnlVisitantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVisitantesLayout.createSequentialGroup()
                .addGroup(pnlVisitantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlVisitantesLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5))
                    .addGroup(pnlVisitantesLayout.createSequentialGroup()
                        .addContainerGap(78, Short.MAX_VALUE)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 460, Short.MAX_VALUE)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesqIdoso, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(91, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVisitantesLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addGap(200, 200, 200))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVisitantesLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlVisitantesLayout.setVerticalGroup(
            pnlVisitantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVisitantesLayout.createSequentialGroup()
                .addGroup(pnlVisitantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlVisitantesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlVisitantesLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(pnlVisitantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPesqIdoso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28))))
                .addGap(26, 26, 26)
                .addComponent(jButton5)
                .addGap(75, 75, 75)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6)
                .addContainerGap(250, Short.MAX_VALUE))
        );

        jPanel1.add(pnlVisitantes, "visitantes");

        pnlPesquisar.setBackground(new java.awt.Color(34, 49, 63));
        pnlPesquisar.setName("pesquisar"); // NOI18N

        jLabel29.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Pesquisar");

        jButton19.setText("Pesquisar por idoso");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setText("Pesquisar por responsável");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton21.setText("Pesquisar por visitante");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPesquisarLayout = new javax.swing.GroupLayout(pnlPesquisar);
        pnlPesquisar.setLayout(pnlPesquisarLayout);
        pnlPesquisarLayout.setHorizontalGroup(
            pnlPesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPesquisarLayout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addGroup(pnlPesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(1063, Short.MAX_VALUE))
        );
        pnlPesquisarLayout.setVerticalGroup(
            pnlPesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPesquisarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addComponent(jButton19)
                .addGap(18, 18, 18)
                .addComponent(jButton20)
                .addGap(18, 18, 18)
                .addComponent(jButton21)
                .addContainerGap(459, Short.MAX_VALUE))
        );

        jPanel1.add(pnlPesquisar, "pesquisar");

        pnlRecepcionistas.setBackground(new java.awt.Color(34, 49, 63));

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Recepcionistas");

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Recepcionistas Cadastrados"));

        tbRecep.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Matricula", "Cargo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbRecep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbRecepMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbRecep);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
        );

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Pesquisar por recepcionista: ");

        jButton13.setText("Pesquisar");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setText("Atualizar");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setText("Excluir");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Nome: ");

        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Matricula: ");

        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Cargo: ");

        cbCargoAtUsr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Recepcionista", "Administrador" }));

        javax.swing.GroupLayout pnlRecepcionistasLayout = new javax.swing.GroupLayout(pnlRecepcionistas);
        pnlRecepcionistas.setLayout(pnlRecepcionistasLayout);
        pnlRecepcionistasLayout.setHorizontalGroup(
            pnlRecepcionistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRecepcionistasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRecepcionistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlRecepcionistasLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(12, 12, 12)
                        .addComponent(txtPesqRecep, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRecepcionistasLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnlRecepcionistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton13)
                            .addGroup(pnlRecepcionistasLayout.createSequentialGroup()
                                .addComponent(jLabel41)
                                .addGap(6, 6, 6)
                                .addComponent(cbCargoAtUsr, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(408, 408, 408)))))
                .addGap(63, 63, 63))
            .addGroup(pnlRecepcionistasLayout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addGroup(pnlRecepcionistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRecepcionistasLayout.createSequentialGroup()
                        .addComponent(jButton14)
                        .addGap(18, 18, 18)
                        .addComponent(jButton15))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlRecepcionistasLayout.createSequentialGroup()
                        .addGroup(pnlRecepcionistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel39)
                            .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlRecepcionistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNomeAtRecep, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                            .addComponent(txtMatriAtUsr))))
                .addContainerGap(188, Short.MAX_VALUE))
        );
        pnlRecepcionistasLayout.setVerticalGroup(
            pnlRecepcionistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRecepcionistasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRecepcionistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtPesqRecep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton13)
                .addGap(1, 1, 1)
                .addGroup(pnlRecepcionistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(txtNomeAtRecep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlRecepcionistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(txtMatriAtUsr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41)
                    .addComponent(cbCargoAtUsr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(pnlRecepcionistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton14)
                    .addComponent(jButton15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel1.add(pnlRecepcionistas, "recepcionistas");

        pnlAdministradores.setBackground(new java.awt.Color(34, 49, 63));

        jLabel32.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Administradores");

        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Pesquisar por administrador: ");

        jButton16.setText("Pesquisar");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Administradores Cadastrados"));

        tbAdmin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Matricula", "Cargo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbAdminMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tbAdmin);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1143, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE))
        );

        jButton17.setText("Atualizar");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setText("Excluir");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Nome: ");

        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Matricula: ");

        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Cargo: ");

        cbCargoAtAdmin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Recepcionista" }));

        javax.swing.GroupLayout pnlAdministradoresLayout = new javax.swing.GroupLayout(pnlAdministradores);
        pnlAdministradores.setLayout(pnlAdministradoresLayout);
        pnlAdministradoresLayout.setHorizontalGroup(
            pnlAdministradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdministradoresLayout.createSequentialGroup()
                .addGroup(pnlAdministradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAdministradoresLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel32)
                        .addGroup(pnlAdministradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAdministradoresLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton16))
                            .addGroup(pnlAdministradoresLayout.createSequentialGroup()
                                .addGap(518, 518, 518)
                                .addComponent(jLabel33)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPesqAdmin))))
                    .addGroup(pnlAdministradoresLayout.createSequentialGroup()
                        .addGroup(pnlAdministradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAdministradoresLayout.createSequentialGroup()
                                .addGap(94, 94, 94)
                                .addGroup(pnlAdministradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnlAdministradoresLayout.createSequentialGroup()
                                        .addComponent(jButton17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton18))
                                    .addGroup(pnlAdministradoresLayout.createSequentialGroup()
                                        .addGroup(pnlAdministradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel45)
                                            .addGroup(pnlAdministradoresLayout.createSequentialGroup()
                                                .addComponent(jLabel42)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtNomeAtAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(105, 105, 105)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbCargoAtAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(pnlAdministradoresLayout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(jLabel43)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMatriAtAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 92, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlAdministradoresLayout.setVerticalGroup(
            pnlAdministradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdministradoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAdministradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)
                    .addComponent(txtPesqAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton16)
                .addGap(2, 2, 2)
                .addGroup(pnlAdministradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(txtNomeAtAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlAdministradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(txtMatriAtAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45)
                    .addComponent(cbCargoAtAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(pnlAdministradoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton17)
                    .addComponent(jButton18))
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(pnlAdministradores, "administradores");

        pnlCadastroUsr.setBackground(new java.awt.Color(34, 49, 63));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Cadastro de Usuários")));

        jLabel30.setText("Nome: ");

        jLabel34.setText("Matrícula:");

        jLabel35.setText("Senha: ");

        jLabel36.setText("Cargo: ");

        jLabel37.setText("Chave: ");

        cbUsr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Recepcionista", "Administrador" }));

        jButton7.setText("Salvar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel34)
                                .addComponent(jLabel35))
                            .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMatriUsr)
                            .addComponent(txtSenhaUsr, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                            .addComponent(cbUsr, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtKeyGerente, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNomeUsr, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(220, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton7)
                .addGap(66, 66, 66))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtNomeUsr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(txtMatriUsr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(txtSenhaUsr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbUsr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addGap(89, 89, 89)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(txtKeyGerente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 272, Short.MAX_VALUE)
                .addComponent(jButton7)
                .addGap(57, 57, 57))
        );

        javax.swing.GroupLayout pnlCadastroUsrLayout = new javax.swing.GroupLayout(pnlCadastroUsr);
        pnlCadastroUsr.setLayout(pnlCadastroUsrLayout);
        pnlCadastroUsrLayout.setHorizontalGroup(
            pnlCadastroUsrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCadastroUsrLayout.createSequentialGroup()
                .addContainerGap(275, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(301, Short.MAX_VALUE))
        );
        pnlCadastroUsrLayout.setVerticalGroup(
            pnlCadastroUsrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCadastroUsrLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel1.add(pnlCadastroUsr, "cadastro_usuario");

        jMenuBar1.setForeground(new java.awt.Color(238, 238, 238));

        mnPrincipal.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mnPrincipal.setForeground(new java.awt.Color(0, 0, 0));
        mnPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/house.png"))); // NOI18N
        mnPrincipal.setText("Página Principal");
        mnPrincipal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mnPrincipalMousePressed(evt);
            }
        });
        jMenuBar1.add(mnPrincipal);

        mnControle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mnControle.setForeground(new java.awt.Color(0, 0, 0));
        mnControle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/cog.png"))); // NOI18N
        mnControle.setText("Controle");
        mnControle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mnControleMousePressed(evt);
            }
        });
        jMenuBar1.add(mnControle);

        mnClientes.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mnClientes.setForeground(new java.awt.Color(0, 0, 0));
        mnClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/group.png"))); // NOI18N
        mnClientes.setText("Clientes");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/user.png"))); // NOI18N
        jMenuItem1.setText("Idosos");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        mnClientes.add(jMenuItem1);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/user_green.png"))); // NOI18N
        jMenuItem3.setText("Visitantes");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        mnClientes.add(jMenuItem3);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/zoom.png"))); // NOI18N
        jMenuItem2.setText("Pesquisar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        mnClientes.add(jMenuItem2);

        jMenuBar1.add(mnClientes);

        mnUsuarios.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mnUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/group_edit.png"))); // NOI18N
        mnUsuarios.setText("Usuários");

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/user_orange.png"))); // NOI18N
        jMenuItem4.setText("Recepcionistas");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        mnUsuarios.add(jMenuItem4);

        mnIAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/user_gray.png"))); // NOI18N
        mnIAdmin.setText("Administradores");
        mnIAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnIAdminActionPerformed(evt);
            }
        });
        mnUsuarios.add(mnIAdmin);

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/add.png"))); // NOI18N
        jMenuItem5.setText("Cadastrar Usuário");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        mnUsuarios.add(jMenuItem5);

        jMenuBar1.add(mnUsuarios);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Código para preencher a tabela de visitas em andamento

        DefaultTableModel dtmVisitas = (DefaultTableModel) tbVisitAndamento.getModel();
        Object[] dados = {txtNomeControle.getText(), txtRGControle.getText(), txtPlacaControle.getText(), txtHoraEntrada.getText(), cbMotivoControle.getSelectedItem()};
        if (txtNomeControle.getText().equals("") || txtRGControle.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Preencha os campos para registro.");
        } else {
            dtmVisitas.addRow(dados);
            txtNomeControle.setText("");
            txtRGControle.setText("");
            txtPlacaControle.setText("");
            txtHoraEntrada.setText("");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Para chamar outros paineis conforme o clique no menu 
        CardLayout c1 = (CardLayout) jPanel1.getLayout();
        c1.show(jPanel1, "idosos");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void mnPrincipalMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnPrincipalMousePressed
        // TODO add your handling code here:
        CardLayout c1 = (CardLayout) jPanel1.getLayout();
        c1.show(jPanel1, "principal");
    }//GEN-LAST:event_mnPrincipalMousePressed

    private void mnControleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnControleMousePressed
        // TODO add your handling code here:
        CardLayout c1 = (CardLayout) jPanel1.getLayout();
        c1.show(jPanel1, "controle");
    }//GEN-LAST:event_mnControleMousePressed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        CardLayout c1 = (CardLayout) jPanel1.getLayout();
        c1.show(jPanel1, "visitantes");
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        CardLayout c1 = (CardLayout) jPanel1.getLayout();
        c1.show(jPanel1, "pesquisar");
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // Código para salvar os dados de visita em andamento na tabela histórico do banco 
        if (tbVisitAndamento.getSelectedRow() != -1) {
            int x = tbVisitAndamento.getSelectedRow();
            while (x != -1) {

                historico h = new historico();
                HistoricoDAO dao = new HistoricoDAO();

                h.setNome(String.valueOf(tbVisitAndamento.getValueAt(x, 0)));
                h.setRg(String.valueOf(tbVisitAndamento.getValueAt(x, 1)));
                h.setPlaca(String.valueOf(tbVisitAndamento.getValueAt(x, 2)));
                h.setHora_entrada(String.valueOf(tbVisitAndamento.getValueAt(x, 3)));
                h.setMotivo(String.valueOf(tbVisitAndamento.getValueAt(x, 4)));
                h.setHora_saida(String.valueOf(tbVisitAndamento.getValueAt(x, 5)));
                dao.create(h);
                readJTable();
                break;
            }

        } else {
            JOptionPane.showMessageDialog(null, "Selecione um registro para salvar");
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // Código para encerrar um processo de visitas em andamento
        DefaultTableModel dtm = (DefaultTableModel) tbVisitAndamento.getModel();
        if (tbVisitAndamento.getSelectedRow() >= 0) {
            dtm.removeRow(tbVisitAndamento.getSelectedRow());
            tbVisitAndamento.setModel(dtm);
        } else {
            JOptionPane.showMessageDialog(null, "Por favor selecione um processo.");
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        CardLayout c1 = (CardLayout) jPanel1.getLayout();
        c1.show(jPanel1, "recepcionistas");

    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void mnIAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnIAdminActionPerformed
        // TODO add your handling code here:
        CardLayout c1 = (CardLayout) jPanel1.getLayout();
        c1.show(jPanel1, "administradores");
    }//GEN-LAST:event_mnIAdminActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Código para cadastrar um idoso com seu respectivo responsável, chama método definido nas classes ResponsavelDAO e IdosoDAO 

        if (txtNomeResp.getText().equals("") || txtNomeIdoso.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Preencha os campos para cadastro!");
        } else {
            responsavel r = new responsavel();
            ResponsavelDAO rdao = new ResponsavelDAO();
            r.setNome(txtNomeResp.getText());
            r.setIdade(spnIdadeResp.getValue().toString());
            r.setRg(txtRGResp.getText());
            r.setTelefone(txtTelResp.getText());
            rdao.create(r);

            Idoso i = new Idoso();
            IdosoDAO idao = new IdosoDAO();

            i.setNome(txtNomeIdoso.getText());
            i.setIdade(spnIdadeIdoso.getValue().toString());
            i.setRg(txtRGIdoso.getText());
            i.setMedicamentos(txtMedIdoso.getText());
            i.setSituacao(txtSitIdoso.getText());
            idao.create(i);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Chama o método que pesquisa um idoso pelo nome definido nas primeiras linhas deste código
        Idoso i = new Idoso();
        IdosoDAO idao = new IdosoDAO();
        i.setNome(txtPesqIdoso.getText());
        txtIdosoAss.setText(idao.readForName(i.getNome()));

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Para cadastrar um visitante, chama o método create da classe VisitanteDAO
        Idoso i = new Idoso();
        visitante v = new visitante();
        VisitanteDAO visitdao = new VisitanteDAO();

        v.setNome(txtVisitNome.getText());
        v.setRg(txtRGVisit.getText());
        v.setTelefone(txtTelVisit.getText());
        visitdao.create(v);

        i.setNome(txtIdosoAss.getText());


    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // Chama a tela que permite a pesquisa, atualização e exclusão de um responsável
        TelaPesquisarResponsavel telapesres = new TelaPesquisarResponsavel();
        pnlPesquisar.add(telapesres);
        telapesres.setVisible(true);

    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        TelaPesquisarVisitante telavisit = new TelaPesquisarVisitante();
        pnlPesquisar.add(telavisit);
        telavisit.setVisible(true);

    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
        TelaPesquisarIdosos telapesqid = new TelaPesquisarIdosos();
        pnlPesquisar.add(telapesqid);
        telapesqid.setVisible(true);
    }//GEN-LAST:event_jButton19ActionPerformed

    public static JComboBox<String> getCbUsr() {
        return cbUsr;
    }

    public void setCbUsr(JComboBox<String> cbUsr) {
        this.cbUsr = cbUsr;
    }

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        usuario u = new usuario();
        UsuarioDAO dao = new UsuarioDAO();

        if (txtNomeUsr.getText().equals("") || txtMatriUsr.getText().equals("") || new String(txtSenhaUsr.getPassword()).equals("")) {
            JOptionPane.showMessageDialog(null, "Preencha os campos para cadastro!");
        } else if (dao.checkChave(new String(txtKeyGerente.getPassword()))) {

            u.setNome(txtNomeUsr.getText());
            u.setMatricula(txtMatriUsr.getText());
            u.setSenha(new String(txtSenhaUsr.getPassword()));
            u.setCargo(cbUsr.getSelectedItem().toString());
            dao.create(u);
            readRecepTB();
            readAdminTB();
        } else {
            JOptionPane.showMessageDialog(null, "Chave do gerente incorreta!");
        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed

        CardLayout c1 = (CardLayout) jPanel1.getLayout();
        c1.show(jPanel1, "cadastro_usuario");

    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void tbRecepMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRecepMouseClicked
        if (tbRecep.getSelectedRow() != -1) {
            txtNomeAtRecep.setText(tbRecep.getValueAt(tbRecep.getSelectedRow(), 1).toString());
            txtMatriAtUsr.setText(tbRecep.getValueAt(tbRecep.getSelectedRow(), 2).toString());
            

        }
    }//GEN-LAST:event_tbRecepMouseClicked

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // Código para atualizar um usuário
        if (tbRecep.getSelectedRow() != -1) {
            usuario u = new usuario();
            UsuarioDAO udao = new UsuarioDAO();

            u.setNome(txtNomeAtRecep.getText());
            u.setMatricula(txtMatriAtUsr.getText());
            
            u.setCargo(cbCargoAtUsr.getSelectedItem().toString());

            u.setId((int) tbRecep.getValueAt(tbRecep.getSelectedRow(), 0));

            udao.update(u);
            readRecepTB();
            readAdminTB();
            txtNomeAtRecep.setText("");
            txtMatriAtUsr.setText("");
        

        } else {
            JOptionPane.showMessageDialog(null, "Selecione um usuário para atualizar.");
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // Deleta  um recepcionista
        if (tbRecep.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um usuário para excluir.");
        } else {
            usuario u = new usuario();
            UsuarioDAO udao = new UsuarioDAO();
            u.setId((int) tbRecep.getValueAt(tbRecep.getSelectedRow(), 0));
            udao.delete(u);
            readRecepTB();

            txtNomeAtRecep.setText("");
            txtMatriAtUsr.setText("");
           

        }

    }//GEN-LAST:event_jButton15ActionPerformed

    private void tbAdminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbAdminMouseClicked

        if (tbAdmin.getSelectedRow() != -1) {
            txtNomeAtAdmin.setText(tbAdmin.getValueAt(tbAdmin.getSelectedRow(), 1).toString());
            txtMatriAtAdmin.setText(tbAdmin.getValueAt(tbAdmin.getSelectedRow(), 2).toString());
           

        }


    }//GEN-LAST:event_tbAdminMouseClicked

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // Atualiza um administrador
        if (tbAdmin.getSelectedRow() != -1) {
            usuario u = new usuario();
            UsuarioDAO udao = new UsuarioDAO();

            u.setNome(txtNomeAtAdmin.getText());
            u.setMatricula(txtMatriAtAdmin.getText());
            
            u.setCargo(cbCargoAtAdmin.getSelectedItem().toString());

            u.setId((int) tbAdmin.getValueAt(tbAdmin.getSelectedRow(), 0));

            udao.update(u);
            readAdminTB();
            readRecepTB();

            txtNomeAtRecep.setText("");
            txtMatriAtUsr.setText("");
            

            readJTable();
        } else {
            JOptionPane.showMessageDialog(null, "Selecione um usuário para atualizar.");
        }

    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // Deleta um aministrador
        if (tbAdmin.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um usuário para excluir.");
        } else {
            usuario u = new usuario();
            UsuarioDAO udao = new UsuarioDAO();
            u.setId((int) tbAdmin.getValueAt(tbAdmin.getSelectedRow(), 0));
            udao.delete(u);
            readAdminTB();

            txtNomeAtRecep.setText("");
            txtMatriAtUsr.setText("");
           

        }

    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        
        readRecepNome(txtPesqRecep.getText());

    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        readAdminNome(txtPesqAdmin.getText());
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Chama o método responsável por gerar o arquivo PDF
        try {
           
            gerarPDF();
        } catch (SQLException ex) {
            Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new TelaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbCargoAtAdmin;
    private javax.swing.JComboBox<String> cbCargoAtUsr;
    private javax.swing.JComboBox<Object> cbMotivoControle;
    private static javax.swing.JComboBox<String> cbUsr;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTextField jTextField6;
    private static javax.swing.JMenu mnClientes;
    private static javax.swing.JMenu mnControle;
    private static javax.swing.JMenuItem mnIAdmin;
    private javax.swing.JMenu mnPrincipal;
    private static javax.swing.JMenu mnUsuarios;
    private javax.swing.JPanel pnlAdministradores;
    private javax.swing.JPanel pnlCadastroUsr;
    private javax.swing.JPanel pnlControle;
    private javax.swing.JPanel pnlDadosControle;
    private javax.swing.JPanel pnlIdosos;
    private javax.swing.JPanel pnlPesquisar;
    private javax.swing.JPanel pnlPrincipal;
    private javax.swing.JPanel pnlRecepcionistas;
    private javax.swing.JPanel pnlVisitantes;
    private javax.swing.JSpinner spnIdadeIdoso;
    private javax.swing.JSpinner spnIdadeResp;
    private javax.swing.JTable tbAdmin;
    private javax.swing.JTable tbHistorico;
    private javax.swing.JTable tbRecep;
    private javax.swing.JTable tbVisitAndamento;
    private javax.swing.JFormattedTextField txtCelResp;
    private javax.swing.JFormattedTextField txtHoraEntrada;
    private javax.swing.JTextField txtIdosoAss;
    private javax.swing.JPasswordField txtKeyGerente;
    private javax.swing.JTextField txtMatriAtAdmin;
    private javax.swing.JTextField txtMatriAtUsr;
    private javax.swing.JTextField txtMatriUsr;
    private javax.swing.JTextField txtMedIdoso;
    private javax.swing.JTextField txtNomeAtAdmin;
    private javax.swing.JTextField txtNomeAtRecep;
    private javax.swing.JTextField txtNomeControle;
    private javax.swing.JTextField txtNomeIdoso;
    private javax.swing.JTextField txtNomeResp;
    private javax.swing.JTextField txtNomeUsr;
    private javax.swing.JTextField txtPesqAdmin;
    private javax.swing.JTextField txtPesqIdoso;
    private javax.swing.JTextField txtPesqRecep;
    private javax.swing.JTextField txtPlacaControle;
    private javax.swing.JFormattedTextField txtRGControle;
    private javax.swing.JFormattedTextField txtRGIdoso;
    private javax.swing.JFormattedTextField txtRGResp;
    private javax.swing.JFormattedTextField txtRGVisit;
    private javax.swing.JPasswordField txtSenhaUsr;
    private javax.swing.JTextArea txtSitIdoso;
    private javax.swing.JFormattedTextField txtTelResp;
    private javax.swing.JFormattedTextField txtTelVisit;
    private javax.swing.JTextField txtVisitNome;
    // End of variables declaration//GEN-END:variables
}
