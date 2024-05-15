package ch.hearc.cafheg.business.allocations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AllocationServiceTest {

  private AllocationService allocationService;

  private AllocataireMapper allocataireMapper;
  private AllocationMapper allocationMapper;

  @BeforeEach
  void setUp() {
    allocataireMapper = Mockito.mock(AllocataireMapper.class);
    allocationMapper = Mockito.mock(AllocationMapper.class);

    allocationService = new AllocationService(allocataireMapper, allocationMapper);
  }

  @Test
  void findAllAllocataires_GivenEmptyAllocataires_ShouldBeEmpty() {
    Mockito.when(allocataireMapper.findAll("Geiser")).thenReturn(Collections.emptyList());
    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
    assertThat(all).isEmpty();
  }

  @Test
  void findAllAllocataires_Given2Geiser_ShouldBe2() {
    Mockito.when(allocataireMapper.findAll("Geiser"))
        .thenReturn(Arrays.asList(new Allocataire(new NoAVS("1000-2000"), "Geiser", "Arnaud"),
            new Allocataire(new NoAVS("1000-2001"), "Geiser", "Aurélie")));
    List<Allocataire> all = allocationService.findAllAllocataires("Geiser");
    assertAll(() -> assertThat(all.size()).isEqualTo(2),
        () -> assertThat(all.get(0).getNoAVS()).isEqualTo(new NoAVS("1000-2000")),
        () -> assertThat(all.get(0).getNom()).isEqualTo("Geiser"),
        () -> assertThat(all.get(0).getPrenom()).isEqualTo("Arnaud"),
        () -> assertThat(all.get(1).getNoAVS()).isEqualTo(new NoAVS("1000-2001")),
        () -> assertThat(all.get(1).getNom()).isEqualTo("Geiser"),
        () -> assertThat(all.get(1).getPrenom()).isEqualTo("Aurélie"));
  }

  @Test
  void findAllocationsActuelles() {
    Mockito.when(allocationMapper.findAll())
        .thenReturn(Arrays.asList(new Allocation(new Montant(new BigDecimal(1000)), Canton.NE,
            LocalDate.now(), null), new Allocation(new Montant(new BigDecimal(2000)), Canton.FR,
            LocalDate.now(), null)));
    List<Allocation> all = allocationService.findAllocationsActuelles();
    assertAll(() -> assertThat(all.size()).isEqualTo(2),
        () -> assertThat(all.get(0).getMontant()).isEqualTo(new Montant(new BigDecimal(1000))),
        () -> assertThat(all.get(0).getCanton()).isEqualTo(Canton.NE),
        () -> assertThat(all.get(0).getDebut()).isEqualTo(LocalDate.now()),
        () -> assertThat(all.get(0).getFin()).isNull(),
        () -> assertThat(all.get(1).getMontant()).isEqualTo(new Montant(new BigDecimal(2000))),
        () -> assertThat(all.get(1).getCanton()).isEqualTo(Canton.FR),
        () -> assertThat(all.get(1).getDebut()).isEqualTo(LocalDate.now()),
        () -> assertThat(all.get(1).getFin()).isNull());
  }

    @Test
    public void getParentDroitAllocation_Given_NoActiviteLucrative_shouldBe_Parent1ETParent2() {
      ParentAllocationParams params = new ParentAllocationParams(
              "ville", // enfantResidence
              false, // parent1ActiviteLucrative
              null, // parent1Residence
              false, // parent2ActiviteLucrative
              null, // parent2Residence
              false, // parentsEnsemble
              BigDecimal.ZERO, // parent1Salaire
              BigDecimal.ZERO // parent2Salaire
      );
      String resultat = allocationService.getParentDroitAllocation(params);


      // Vérification du résultat
      assertEquals("PARENT_1_Et_PARENT_2", resultat); // On s'attend à ce que le parent 1 soit sélectionné par défaut

      // Vous pouvez ajouter d'autres assertions si nécessaire pour vérifier d'autres aspects du résultat.
    }

  @Test
  public void getParentDroitAllocation_Given_DeuxParentsActiviteLucrativeEtParentSalairePlusEleve_ShouldBe_PARENT2() {
    // Préparation des données d'entrée
    ParentAllocationParams params = new ParentAllocationParams(
            "ville",
            true,
            null,
            true,
            null,
            false,
            new BigDecimal("2000"),
            new BigDecimal("1500")
    );

    String resultat = allocationService.getParentDroitAllocation(params);


    // Vérification du résultat
    assertEquals("Parent2", resultat); // On s'attend à ce que le parent 1 soit sélectionné car il a le salaire le plus élevé

    // Vous pouvez ajouter d'autres assertions si nécessaire pour vérifier d'autres aspects du résultat.
  }

  @Test
  public void getParentDroitAllocation_Given_UnSeulParentActiviteLucrative_ShouldBe_Parent2() {
    ParentAllocationParams params = new ParentAllocationParams(
            "ville",
            true,
            null,
            false,
            null,
            false,
            new BigDecimal("2000"),
            BigDecimal.ZERO
    );

    String resultat = allocationService.getParentDroitAllocation(params);


    // Vérification du résultat
    assertEquals("Parent2", resultat); // On s'attend à ce que le parent 1 soit sélectionné car il est le seul à avoir une activité lucrative

  }
  @Test
  public void getParentDroitAllocation_Given_Les2ParentsMemeSalaire_ShouldBe_Parent1ETParent2() {
    ParentAllocationParams params = new ParentAllocationParams(
            "ville", // enfantResidence
            true,    // parent1ActiviteLucrative
            null,    // parent1Residence
            true,    // parent2ActiviteLucrative
            null,    // parent2Residence
            false,   // parentsEnsemble
            new BigDecimal("2000"), // parent1Salaire
            new BigDecimal("2000")  // parent2Salaire
    );

    String resultat = allocationService.getParentDroitAllocation(params);

    // Vérification du résultat
    assertEquals("Parent1"+"Parent2", resultat); // On s'attend à ce que le parent 1 soit sélectionné car il est le seul à avoir une activité lucrative

  }





}