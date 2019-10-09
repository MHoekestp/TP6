package testingwithhsqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class DAO {
	private final DataSource myDataSource;
	
	public DAO(DataSource dataSource) {
		myDataSource = dataSource;
	}

	/**
	 * Renvoie le nom d'un client à partir de son ID
	 * @param id la clé du client à chercher
	 * @return le nom du client (LastName) ou null si pas trouvé
	 * @throws SQLException 
	 */
	public String nameOfCustomer(int id) throws SQLException {
		String result = null;
		
		String sql = "SELECT LastName FROM Customer WHERE ID = ?";
		try (Connection myConnection = myDataSource.getConnection(); 
		     PreparedStatement statement = myConnection.prepareStatement(sql)) {
			statement.setInt(1, id); // On fixe le 1° paramètre de la requête
			try ( ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					// est-ce qu'il y a un résultat ? (pas besoin de "while", 
                                        // il y a au plus un enregistrement)
					// On récupère les champs de l'enregistrement courant
					result = resultSet.getString("LastName");
				}
			}
		}
		// dernière ligne : on renvoie le résultat
		return result;
	}
        public void insertProduct(Product produit) throws DAOException,Exception{
            String sql = "INSERT INTO PRODUCT VALUES (?,?,?)";
                if(produit.id<0){
                    throw new IllegalArgumentException("Product must be positive");
                }
		try (Connection connection = myDataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql)) {
			// Définir la valeur des paramètres
			stmt.setInt(1, produit.id);
                        stmt.setString(2, produit.name);
                        stmt.setFloat(3,produit.price);

			stmt.executeUpdate();

		} catch (SQLException ex) {
			Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
			throw new DAOException(ex.getMessage());
		}
	
        }
        public Product getProduct(int productId) throws DAOException{
            Product result;
            
            String sql = "SELECT * PRODUCT WHERE ID = ?";
                try (Connection connection = myDataSource.getConnection();
                        PreparedStatement stmt = connection.prepareStatement(sql)){
                    stmt.setInt(1, productId);
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    result = new Product(rs.getInt("ID"),rs.getString("Name"),rs.getFloat("Price"));
                    
                    
                } catch (SQLException ex) {
			Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
			throw new DAOException(ex.getMessage());
		}
		return result;    
        }
}
