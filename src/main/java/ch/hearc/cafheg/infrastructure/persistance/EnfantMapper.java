package ch.hearc.cafheg.infrastructure.persistance;

import ch.hearc.cafheg.business.versements.Enfant;
import ch.hearc.cafheg.business.allocations.NoAVS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnfantMapper extends Mapper {

  private static final Logger logger = LoggerFactory.getLogger(EnfantMapper.class);
  private final String QUERY_FIND_ENFANT_BY_ID = "SELECT NO_AVS, NOM, PRENOM FROM ENFANTS WHERE NUMERO=?";

  public Enfant findById(long id) {
    logger.info("Recherche d'un enfant par son id: {}", id);
    Connection connection = activeJDBCConnection();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_ENFANT_BY_ID);
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        logger.trace("Mapping result set to Enfant object");
        return new Enfant(new NoAVS(resultSet.getString("NO_AVS")),
                resultSet.getString("NOM"), resultSet.getString("PRENOM"));
      } else {
        logger.warn("No Enfant found with id: {}", id);
        return null;  // Or throw a specific exception depending on your design
      }
    } catch (SQLException e) {
      logger.error("SQLException in findById", e);
      throw new RuntimeException(e);
    }
  }
}
