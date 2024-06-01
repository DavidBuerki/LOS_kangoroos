package ch.hearc.cafheg.infrastructure.persistance;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.NoAVS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllocataireMapper extends Mapper {

    private static final Logger logger = LoggerFactory.getLogger(AllocataireMapper.class);

    private static final String QUERY_FIND_ALL = "SELECT NOM, PRENOM, NO_AVS FROM ALLOCATAIRES";
    private static final String QUERY_FIND_WHERE_NOM_LIKE = "SELECT NOM, PRENOM, NO_AVS FROM ALLOCATAIRES WHERE NOM LIKE ?";
    private static final String QUERY_FIND_WHERE_NUMERO = "SELECT NO_AVS, NOM, PRENOM FROM ALLOCATAIRES WHERE NUMERO=?";
    private static final String QUERY_DELETE = "DELETE FROM ALLOCATAIRES WHERE NUMERO=?";
    private static final String QUERY_UPDATE_NOM_PRENOM = "UPDATE ALLOCATAIRES SET NOM=?, PRENOM=? WHERE NUMERO=?";

    public List<Allocataire> findAll(String likeNom) {
        logger.info("Executing findAll with filter: {}", likeNom);
        Connection connection = activeJDBCConnection();
        try {
            PreparedStatement preparedStatement;
            if (likeNom == null) {
                logger.debug("Preparing SQL: {}", QUERY_FIND_ALL);
                preparedStatement = connection.prepareStatement(QUERY_FIND_ALL);
            } else {
                logger.debug("Preparing SQL: {}", QUERY_FIND_WHERE_NOM_LIKE);
                preparedStatement = connection.prepareStatement(QUERY_FIND_WHERE_NOM_LIKE);
                preparedStatement.setString(1, likeNom + "%");
            }
            List<Allocataire> allocataires = new ArrayList<>();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    logger.trace("Mapping Allocataire");
                    allocataires.add(new Allocataire(new NoAVS(resultSet.getString("NO_AVS")),
                            resultSet.getString("PRENOM"), resultSet.getString("NOM")));
                }
            }
            logger.info("Allocataires found: {}", allocataires.size());
            return allocataires;
        } catch (SQLException e) {
            logger.error("SQLException in findAll", e);
            throw new RuntimeException(e);
        }
    }

    public Allocataire findById(long id) {
        logger.info("Executing findById for id: {}", id);
        Connection connection = activeJDBCConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_WHERE_NUMERO);
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    logger.trace("Allocataire mapping");
                    return new Allocataire(new NoAVS(resultSet.getString("NO_AVS")),
                            resultSet.getString("NOM"), resultSet.getString("PRENOM"));
                }
            }
            logger.warn("No Allocataire found for ID: {}", id);
            return null; // Or throw exception depending on your application's needs
        } catch (SQLException e) {
            logger.error("SQLException in findById", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(long id) {
        logger.info("Executing delete for id: {}", id);
        Connection connection = activeJDBCConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_DELETE);
            preparedStatement.setLong(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            logger.debug("Delete operation affected rows: {}", affectedRows);
        } catch (SQLException e) {
            logger.error("SQLException in delete", e);
            throw new RuntimeException(e);
        }
    }

    public void updateNomPrenom(long id, String nom, String prenom) {
        logger.info("Executing updateNomPrenom for id: {}", id);
        Connection connection = activeJDBCConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_UPDATE_NOM_PRENOM);
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setLong(3, id);
            int affectedRows = preparedStatement.executeUpdate();
            logger.debug("Update operation affected rows: {}", affectedRows);
        } catch (SQLException e) {
            logger.error("SQLException in updateNomPrenom", e);
            throw new RuntimeException(e);
        }
    }
}
