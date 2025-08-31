package lk.mgmt.coursemgmtbackend.modules.terms.mapper;

import lk.mgmt.coursemgmtbackend.modules.terms.dto.TermDto;
import lk.mgmt.coursemgmtbackend.modules.terms.entity.TermEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TermMapper {
    TermDto toDto(TermEntity e);
}
