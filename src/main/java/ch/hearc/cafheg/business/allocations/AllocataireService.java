package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.VersementMapper;

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
}
