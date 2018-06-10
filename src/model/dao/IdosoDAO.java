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
import model.bean.Idoso;
import model.bean.historico;
import model.bean.responsavel;


/**
 *
 * @author matheuszeus
 */
public class IdosoDAO {
    responsavel responsavel = new responsavel();
    public void create(Idoso i){
            Connection con = ConnectionFactory.getConnection();
            PreparedStatement stmt = null;
            

        try {
            stmt = con.prepareStatement("INSERT INTO idoso ( nome_idoso,  idade_idoso, rg_idoso, medicamento_idoso, situacao_idoso)VALUES(?, ?, ?, ?, ?)");
            
            
            stmt.setString(1, i.getNome());
            stmt.setString(2, i.getIdade());
            stmt.setString(3, i.getRg());
            stmt.setString(4, i.getMedicamentos());
            stmt.setString(5, i.getSituacao());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Cadastrado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar:" + ex);
            Logger.getLogger(IdosoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

        }
    public String readForName(String nomeidoso){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

       
        try {
            stmt = con.prepareStatement("SELECT nome_idoso FROM idoso WHERE nome_idoso LIKE ?");
            stmt.setString(1, nomeidoso +"%");
            rs = stmt.executeQuery();

           

        } catch (SQLException ex) {
            Logger.getLogger(IdosoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return nomeidoso;

    }
    public List<Idoso> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Idoso> idosos = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM idoso");
            rs = stmt.executeQuery();

            while (rs.next()) {

                Idoso i = new Idoso();
                i.setId(rs.getInt("id_idoso"));
                i.setNome(rs.getString("nome_idoso"));
                i.setIdade(rs.getString("idade_idoso"));
                i.setRg(rs.getString("rg_idoso"));
                i.setMedicamentos(rs.getString("medicamento_idoso"));
                i.setSituacao(rs.getString("situacao_idoso"));
                idosos.add(i);

            }

        } catch (SQLException ex) {
            Logger.getLogger(IdosoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return idosos;
    }
    
    public void update(Idoso i) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE idoso SET nome_idoso = ?, idade_idoso = ?, rg_idoso = ?, medicamento_idoso = ?, situacao_idoso = ?  WHERE id_idoso = ?");
            stmt.setString(1, i.getNome());
            stmt.setString(2, i.getIdade());
            stmt.setString(3, i.getRg());
            stmt.setString(4, i.getMedicamentos());
            stmt.setString(5, i.getSituacao());
            stmt.setInt(6, i.getId());
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Atualizado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar:" + ex);
            Logger.getLogger(IdosoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }
    
     public void delete(Idoso i) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE FROM idoso WHERE id_idoso = ?");
            
            stmt.setInt(1, i.getId());

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
