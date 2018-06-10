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
import model.bean.visitante;

/**
 *
 * @author matheuszeus
 */
public class Visitante_por_idosoDAO {
    public void create(String rgIdoso, int idVisit){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try {
            stmt = con.prepareStatement("INSERT INTO idoso_has_visitante (idoso_rg_idoso, visitante_idvisitante) VALUES ((SELECT rg_idoso FROM idoso WHERE nome_idoso LIKE ?), (SELECT idvisitante FROM visitante WHERE idvisitante = ?))");
            stmt.setString(1, "%"+rgIdoso+"%");
            stmt.setInt(2,  idVisit);
            ResultSet rs = stmt.executeQuery();
            
            visitante v = new visitante();
            v.setId(rs.getInt("idvisitante"));
        } catch (SQLException ex) {
            Logger.getLogger(Visitante_por_idosoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
