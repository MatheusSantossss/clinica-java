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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import model.bean.responsavel;


/**
 *
 * @author matheuszeus
 */
public class ResponsavelDAO {
    public void create(responsavel r){
            Connection con = ConnectionFactory.getConnection();
            PreparedStatement stmt = null;
            
        try {
            stmt = con.prepareStatement("INSERT INTO responsavel (nome_responsavel, idade_responsavel, rg_responsavel, telefone_responsavel)VALUES(?, ?, ?, ?)");
            
            stmt.setString(1, r.getNome());
            stmt.setString(2, r.getIdade());
            stmt.setString(3, r.getRg());
            stmt.setString(4, r.getTelefone());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Cadastrado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar:" + ex);
            Logger.getLogger(ResponsavelDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

        }

    public List<responsavel> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<responsavel> responsaveis = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM responsavel");
            rs = stmt.executeQuery();

            while (rs.next()) {

                responsavel r = new responsavel();
                r.setId(rs.getInt("idiresponsavel"));
                r.setNome(rs.getString("nome_responsavel"));
                r.setIdade(rs.getString("idade_responsavel"));
                r.setRg(rs.getString("rg_responsavel"));
                r.setTelefone(rs.getString("telefone_responsavel"));
                responsaveis.add(r);

            }

        } catch (SQLException ex) {
            Logger.getLogger(ResponsavelDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return responsaveis;
    }
    
    
    public void update(responsavel r) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE responsavel SET nome_responsavel = ?, idade_responsavel = ?, rg_responsavel = ?, telefone_responsavel = ?  WHERE idiresponsavel = ?");
            stmt.setString(1, r.getNome());
            stmt.setString(2, r.getIdade());
            stmt.setString(3, r.getRg());
            stmt.setString(4, r.getTelefone());
            stmt.setInt(5, r.getId());
           
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Atualizado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar:" + ex);
            Logger.getLogger(ResponsavelDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }
    
    public void delete(responsavel r) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE FROM responsavel WHERE idiresponsavel = ?");
            
            stmt.setInt(1, r.getId());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Excluído com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir:" + ex);
            Logger.getLogger(ResponsavelDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }



}

