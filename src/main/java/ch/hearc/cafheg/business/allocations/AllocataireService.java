package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;

import java.util.Objects;

public class AllocataireService {

    private AllocataireMapper allocataireMapper;
    private VersementMapper versementMapper;

    public AllocataireService(AllocataireMapper allocataireMapper, VersementMapper versementMapper) {
        this.allocataireMapper = allocataireMapper;
        this.versementMapper = versementMapper;
    }

    public AllocataireMapper getAllocataireMapper() {
        return allocataireMapper;
    }

    public void setAllocataireMapper(AllocataireMapper allocataireMapper) {
        this.allocataireMapper = allocataireMapper;
    }

    public VersementMapper getVersementMapper() {
        return versementMapper;
    }

    public void setVersementMapper(VersementMapper versementMapper) {
        this.versementMapper = versementMapper;
    }

    public String deleteAllocataireIfNoVersements(Long id) {
        if(!this.versementMapper.findVersementByParentId(id)) {
            this.allocataireMapper.delete(id);
            return "Allocataire supprimé";
        }else
            return "Impossible de supprimer l'allocataire, il a des versements";
    }
    public Allocataire findAllocataireById(Long id) {
        return this.allocataireMapper.findById(id);
}

    public String updateAllocataire(Allocataire allocataire, Long id) {
        // Trouver l'allocataire actuel par son ID
        Allocataire currentAllocataire = this.allocataireMapper.findById(id);

        // Vérifier si l'allocataire existe
        if (currentAllocataire == null) {
            return "Allocataire non trouvé";
        } else {
            // Comparer les noms et prénoms en utilisant equals() au lieu de ==
            boolean isNomSame = allocataire.getNom().equals(currentAllocataire.getNom());
            boolean isPrenomSame = allocataire.getPrenom().equals(currentAllocataire.getPrenom());

            if (isNomSame && isPrenomSame) {
                return "Aucune modification apportée";
            } else {
                // Mise à jour du nom et du prénom si nécessaire
                this.allocataireMapper.updateNomPrenom(id, allocataire.getNom(), allocataire.getPrenom());
                return "Allocataire modifié";
            }
        }
    }
}
