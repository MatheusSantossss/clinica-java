/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import com.mysql.jdbc.Statement;
import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.bean.visitante;

/**
 *
 * @author matheuszeus
 */
public class VisitanteDAO {
    public void create(visitante v){
            Connection con = ConnectionFactory.getConnection();
            PreparedStatement stmt = null;
            

        try {
            stmt = con.prepareStatement("INSERT INTO visitante ( nome_visitante,  telefone_visitante, rg_visitante) VALUES(?, ?, ?)");
            
            
            stmt.setString(1, v.getNome());
            stmt.setString(2, v.getTelefone());
            stmt.setString(3, v.getRg());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Cadastrado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar:" + ex);
            Logger.getLogger(VisitanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

        }
    public List<visitante> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<visitante> visitantes = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM visitante");
            rs = stmt.executeQuery();

            while (rs.next()) {

                visitante v = new visitante();
                v.setId(rs.getInt("idvisitante"));
                v.setNome(rs.getString("nome_visitante"));
                v.setTelefone(rs.getString("telefone_visitante"));
                v.setRg(rs.getString("rg_visitante"));
                
                visitantes.add(v);

            }

        } catch (SQLException ex) {
            Logger.getLogger(VisitanteDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return visitantes;
    }
    
    public void update(visitante v) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE visitante SET nome_visitante = ?, telefone_visitante = ?, rg_visitante = ?  WHERE idvisitante = ?");
            stmt.setString(1, v.getNome());
            stmt.setString(2, v.getTelefone());
            stmt.setString(3, v.getRg());
            stmt.setInt(4, v.getId());
           
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Atualizado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar:" + ex);
            Logger.getLogger(IdosoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }
    
    public void delete(visitante v) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE FROM visitante WHERE idvisitante = ?");
            
            stmt.setInt(1, v.getId());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Excluído com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir:" + ex);
            Logger.getLogger(IdosoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }
    
    
}
