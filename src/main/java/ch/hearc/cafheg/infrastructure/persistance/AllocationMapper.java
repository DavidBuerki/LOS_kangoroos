package ch.hearc.cafheg.infrastructure.persistance;

import ch.hearc.cafheg.business.allocations.Allocation;
import ch.hearc.cafheg.business.allocations.Canton;
import ch.hearc.cafheg.business.common.Montant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AllocationMapper extends Mapper {

  private static final Logger logger = LoggerFactory.getLogger(AllocationMapper.class);

  private static final String QUERY_FIND_ALL = "SELECT * FROM ALLOCATIONS";

  public List<Allocation> findAll() {
    logger.info("Recherche de toutes les allocations");

    Connection connection = activeJDBCConnection();
    try {
      logger.debug("SQL: {}", QUERY_FIND_ALL);
      PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_ALL);
      List<Allocation> allocations = new ArrayList<>();
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          logger.trace("Processing resultSet");
          allocations.add(new Allocation(
                  new Montant(resultSet.getBigDecimal("MONTANT")),
                  Canton.fromValue(resultSet.getString("CANTON")),
                  resultSet.getDate("DATE_DEBUT").toLocalDate(),
                  resultSet.getDate("DATE_FIN") != null ? resultSet.getDate("DATE_FIN").toLocalDate() : null
          ));
        }
      }
      logger.info("Allocations found: {}", allocations.size());
      return allocations;
    } catch (SQLException e) {
      logger.error("SQLException in findAll", e);
      throw new RuntimeException(e);
    }
  }
}
