

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.AllocataireService;
import ch.hearc.cafheg.infrastructure.persistance.*;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class MyTestsIT {

    private AllocataireMapper allocataireMapper = new AllocataireMapper();
    private VersementMapper versementMapper = new VersementMapper();
    private AllocataireService allocataireService = new AllocataireService(allocataireMapper, versementMapper);

    private IDatabaseTester databaseTester;

    @BeforeEach
    public void setUp() throws Exception {
        // Initialisation de la base de données
        Database database = new Database();
        Migrations migrations = new Migrations(database);
        database.start();
        migrations.start();
        // Configuration de DBUnit
        databaseTester = new JdbcDatabaseTester("org.h2.Driver", "jdbc:h2:mem:sample", "", "");
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(getClass().getClassLoader().getResourceAsStream("allocataire-dataset.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(), dataSet);
    }

    @Test
    public void deleteAllocataireIfNoVersements_GivenAllocataireId_shouldBeNull() throws Exception {
        Database.inTransaction(() -> {
            assertNotNull(allocataireService.findAllocataireById(502));

            allocataireService.deleteAllocataireIfNoVersements(502);

            assertNull(allocataireService.findAllocataireById(502));
            return null;
        });
    }

    @Test
    public void updateAllocataire_ShouldUpdateSuccessfully() throws Exception {
        Database.inTransaction(() -> {
        var originalAllocataire = allocataireService.findAllocataireById(501);
        assertThat(originalAllocataire).isNotNull();

        // Modify the allocataire
        Allocataire updatedAllocataire = new Allocataire();
        updatedAllocataire.setNom("Davide");
        updatedAllocataire.setPrenom("Bertrand");

        String result = allocataireService.updateAllocataire(updatedAllocataire,501);
        assertThat(result).isEqualTo("Allocataire modifié");

        // Verify the changes
        var modifiedAllocataire = allocataireService.findAllocataireById(501);
        assertThat(modifiedAllocataire).isNotNull();
        assertThat(modifiedAllocataire.getNom()).isEqualTo("Davide");
        assertThat(modifiedAllocataire.getPrenom()).isEqualTo("Bertrand");
        return null;
        });
    }



}
