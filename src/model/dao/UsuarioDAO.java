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
import model.bean.usuario;

/**
 *
 * @author matheus
 */
public class UsuarioDAO {

    private String checknome = null;
    private String checkcargo = null;

    public void create(usuario u) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("INSERT INTO usuario (nome_usuario, matricula_usuario, senha_usuario, cargo_usuario, chave_gerente)VALUES(?, ?, ?, ?, ?)");
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getMatricula());
            stmt.setString(3, u.getSenha());
            stmt.setString(4, u.getCargo());
            stmt.setString(5, u.getChave());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Cadastrado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar:" + ex);
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }

    public boolean checkLogin(String matricula, String senha) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean check = false;

        try {
            stmt = con.prepareStatement("SELECT * FROM usuario WHERE matricula_usuario = ?");
            stmt.setString(1, matricula);
            
            rs = stmt.executeQuery();

            if (rs.next()) {
                check = true;

            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return check;

    }

    public String checkUsuario(String matricula) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement("SELECT nome_usuario, cargo_usuario FROM usuario WHERE matricula_usuario = ?");
            stmt.setString(1, matricula);

            rs = stmt.executeQuery();
            while (rs.next()) {
                usuario us = new usuario();
                us.setNome(rs.getString("nome_usuario"));
                us.setCargo(rs.getString("cargo_usuario"));
                checknome = us.getNome();
                checkcargo = us.getCargo();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return "  " + checkcargo + "  " + checknome;

    }

    public String getcargo() {
        return checkcargo;
    }

    public boolean checkGerente() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean gerente = false;
        try {
            stmt = con.prepareStatement("SELECT * FROM usuario WHERE cargo_usuario LIKE ? ");
            stmt.setString(1, "%Gerente");
            rs = stmt.executeQuery();

            if (rs.next()) {
                gerente = true;
            } else {
                gerente = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gerente;
    }
    
    public boolean checkChave(String chave){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean validar = false;
        
        try {
            stmt = con.prepareStatement("SELECT chave_gerente FROM usuario WHERE chave_gerente = ?");
            stmt.setString(1, chave);
            rs = stmt.executeQuery();
            if(rs.next()){
                validar = true;
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return validar;
    }
    
    public List<usuario> readRecep() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<usuario> usuarios = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM usuario WHERE cargo_usuario = ?");
            stmt.setString(1, "Recepcionista");
            rs = stmt.executeQuery();

            while (rs.next()) {

                usuario u = new usuario();
                u.setId(rs.getInt("idusuario"));
                u.setNome(rs.getString("nome_usuario"));
                u.setMatricula(rs.getString("matricula_usuario"));
                u.setCargo(rs.getString("cargo_usuario"));
                usuarios.add(u);

            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return usuarios;
    }
    
    public List<usuario> readAdmin() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<usuario> usuarios = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM usuario WHERE cargo_usuario = ?");
            stmt.setString(1, "Administrador");
            rs = stmt.executeQuery();

            while (rs.next()) {

                usuario u = new usuario();
                u.setId(rs.getInt("idusuario"));
                u.setNome(rs.getString("nome_usuario"));
                u.setMatricula(rs.getString("matricula_usuario"));
                u.setCargo(rs.getString("cargo_usuario"));
                usuarios.add(u);

            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return usuarios;
    }
    
    public void update(usuario u) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE usuario SET nome_usuario = ?, matricula_usuario = ?, cargo_usuario = ?  WHERE idusuario = ?");
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getMatricula());
            stmt.setString(3, u.getCargo());
            stmt.setInt(4, u.getId());
           
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Atualizado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar:" + ex);
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }
    
    public void delete(usuario u) {

        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE FROM usuario WHERE idusuario = ?");
            
            stmt.setInt(1, u.getId());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Excluído com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir:" + ex);
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }

    }
    
   public List<usuario> readForNomeRecep(String nome) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<usuario> usuarios = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM usuario WHERE nome_usuario LIKE ? AND cargo_usuario = ?");
            stmt.setString(1, "%"+nome+"%");
            stmt.setString(2, "Recepcionista");
            rs = stmt.executeQuery();

            while (rs.next()) {

                usuario u = new usuario();

                u.setId(rs.getInt("idusuario"));
                u.setNome(rs.getString("nome_usuario"));
                u.setCargo(rs.getString("cargo_usuario"));
                usuarios.add(u);

            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return usuarios;

    }
   
   public List<usuario> readForNomeAdmin(String nome) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<usuario> usuarios = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM usuario WHERE nome_usuario LIKE ? AND cargo_usuario = ?");
            stmt.setString(1, "%"+nome+"%");
            stmt.setString(2, "Administrador");
            rs = stmt.executeQuery();

            while (rs.next()) {

                usuario u = new usuario();

                u.setId(rs.getInt("idusuario"));
                u.setNome(rs.getString("nome_usuario"));
                u.setMatricula(rs.getString("matricula_usuario"));
                u.setCargo(rs.getString("cargo_usuario"));
                usuarios.add(u);

            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return usuarios;

    }

}
