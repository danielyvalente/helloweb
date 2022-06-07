/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package local.dmvs.helloweb.dao;

import connection.MySQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import static java.util.logging.Level.INFO;
import java.util.logging.Logger;
import local.dmvs.helloweb.bean.Produto;

/**
 *
 * @author devsys-a
 */
public class ProdutoDAO {
    
    private static final String SQL_INSERT = "INSERT INTO produto(codBarras, "
            + "descricao, qtd, valorCompra, valorVenda) "
            + "VALUES (?, ?, ?, ?, ?)";
    
    private static final String SQL_SELECT_ALL = "SELECT * FROM produto";
    private static final String SQL_SELECT_ID = "SELECT * FROM produto "
            + "WHERE id = ?";
    
    
    private static final String SQL_UPDATE = "UPDATE produto SET codBarras = ?,"
            + "descricao = ?, qtd = ?, valorCompra = ?, valorVenda = ? "
            + "WHERE id = ?";
    
    private static final String SQL_DELETE = "DELETE FROM produto WHERE id = ?";
    
    /**
     * Insere um usuario na base de dados Produto
     * @param p
     */
    public void create(Produto p) {
        // Inicia uma conexão com a base de dados utilizando nossa classe MySQLConnection.
        Connection conn = MySQLConnection.getConnection(); 
        // declara o PreparedStatement que irá receber a instrução a ser executada no banco
        PreparedStatement stmt = null;  
        
        try { 
            //Prepara a instrução dada na constante SQL_INSERT. 
            stmt = conn.prepareStatement(SQL_INSERT); 
            // substitui os “?” pelos atributos do produto recebido no método.
            stmt.setString(1, p.getCodBarras());
            stmt.setString(2, p.getDescricao());
            stmt.setDouble(3, p.getQtd());
            stmt.setDouble(4, p.getValorCompra());
            stmt.setDouble(5, p.getValorVenda());
            
            // Executa a query  e retorna um inteiro. O inteiro representa a quantidade de registros incluídos.
            int auxRetorno = stmt.executeUpdate();
            
            // gera um log INFO para informação.
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.INFO, null, 
                    "Inclusao: " + auxRetorno);
          
        } catch (SQLException sQLException) {
            // gera um log SEVERE caso tenha algum erro.
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null,
                    sQLException);
        } finally {
            //Encerra a conexão com o banco e o statement
            MySQLConnection.closeConnection(conn, stmt);
        }
        
    }
    
    /**
     * Retorna todos os registros da tabela produto
     * @return 
     */
    public List<Produto> getResults(){
        Connection conn = MySQLConnection.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Produto p = null;
        List<Produto> listaProdutos = null;
        
        try {
            //Prepara a string de SELECT e executa a query.
            stmt = conn.prepareStatement(SQL_SELECT_ALL);
            rs = stmt.executeQuery(); //Executa a query e retorna um ResultSet.
            
            //Carrega os dados do ResultSet rs, converte em Produto e
            // adiciona na lista de retorno.
            listaProdutos = new ArrayList<>();
            
            // Varre o ResultSet (rs) utilizando o método rs.next().
            while (rs.next()){
                p = new Produto();
                // captura os campos do registro posicionado pelo rs.next(), 
                // monta um objeto produto temporário e o inclui na lista.
                p.setId(rs.getInt("id"));
                p.setCodBarras(rs.getString("codBarras"));
                p.setDescricao(rs.getString("descricao"));
                p.setQtd(rs.getDouble("qtd"));
                p.setValorCompra(rs.getDouble("valorCompra"));
                p.setValorVenda(rs.getDouble("valorVenda"));
                p.setDataCadastro(rs.getString("dataCadastro"));
                listaProdutos.add(p);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaProdutos;
    }
    
    /**
     * Retorna um produto da tabela produto.
     * @param id
     * @return 
     */
    //A diferença deste código para o anterior é que ele retorna apenas um produto. 
    public Produto getResultById(int id){
        Connection conn = MySQLConnection.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Produto p = null;
        
        try{
            stmt = conn.prepareStatement(SQL_SELECT_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                p = new Produto();
                p.setId(rs.getInt("id"));
                p.setCodBarras(rs.getString("codBarras"));
                p.setDescricao(rs.getString("descricao"));
                p.setQtd(rs.getDouble("qtd"));
                p.setValorCompra(rs.getDouble("valorCompra"));
                p.setValorVenda(rs.getDouble("valorVenda"));
                p.setDataCadastro(rs.getString("dataCadastro"));
            }
            
        } catch (SQLException sQLException) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null,
                    sQLException);
        } finally {
            // Encerra a conexão com o banco e o statement.
            MySQLConnection.closeConnection(conn, stmt, rs);
        }
        
        return p;
    }
    
    /**
     * Atualiza um registro n atabela produto.
     * @param p Produto a ser atualizado.
     */
    // Recebe como parâmetro um produto para ser alterado. Usa como referência o atributo ID.
    // Segue o mesmo padrão do método create.
    public void update(Produto p) {
        Connection conn = MySQLConnection.getConnection();
        PreparedStatement stmt = null;
        
        try {
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, p.getCodBarras());
            stmt.setString(2, p.getDescricao());
            stmt.setDouble(3, p.getQtd());
            stmt.setDouble(4, p.getValorCompra());
            stmt.setDouble(5, p.getValorVenda());
            stmt.setInt(6, p.getId());
            
            //Executa a query
            int auxRetorno = stmt.executeUpdate();
            
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.INFO, null,
                    "Update: " + auxRetorno);
            
        } catch (SQLException sQLException) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null,
                    sQLException);
            
        } finally {
            // Encerra a conexão com o banco e o statement.
            MySQLConnection.closeConnection(conn, stmt);
        }
        
    }
    
    /**
     * Exclui um produto com base do ID fornecido.
     * @param id  IDentificação do produto.
     */
    public void delete(int id) {
        Connection conn = MySQLConnection.getConnection();
        PreparedStatement stmt = null;
        
        try {
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, id);
            
            // Executa a query
            int auxRetorno = stmt.executeUpdate();
            
            Logger.getLogger(ProdutoDAO.class.getName()).log(INFO, null,
                    "Delete: " + auxRetorno);
            
            } catch (SQLException sQLException) {
                Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null,
                    sQLException);
            
            } finally {
            // Encerra a conexãp com o banco e o statement.
            MySQLConnection.closeConnection(conn, stmt);
        }
    }
}
