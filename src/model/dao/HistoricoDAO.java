/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.bean.historico;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author matheus
 */
public class HistoricoDAO {
    
        public void create(historico h){
            Connection con = ConnectionFactory.getConnection();
            PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("INSERT INTO historico (nome, rg, placa, hora_entrada, motivo, hora_saida)VALUES(?, ?, ?, ?, ?, ?)");
            stmt.setString(1, h.getNome());
            stmt.setString(2, h.getRg());
            stmt.setString(3, h.getPlaca());
            stmt.setString(4, h.getHora_entrada());
            stmt.setString(5, h.getMotivo());
            stmt.setString(6, h.getHora_saida());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Salvo com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar:" + ex);
            Logger.getLogger(HistoricoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

        }
        
        public List<historico> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<historico> historicos = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM historico");
            rs = stmt.executeQuery();

            while (rs.next()) {

                historico h = new historico();

                h.setNome(rs.getString("nome"));
                h.setRg(rs.getString("rg"));
                h.setPlaca(rs.getString("placa"));
                h.setHora_entrada(rs.getString("hora_entrada"));
                h.setMotivo(rs.getString("motivo"));
                h.setHora_saida(rs.getString("hora_saida"));
                historicos.add(h);

            }

        } catch (SQLException ex) {
            Logger.getLogger(HistoricoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return historicos;

    }
}    



