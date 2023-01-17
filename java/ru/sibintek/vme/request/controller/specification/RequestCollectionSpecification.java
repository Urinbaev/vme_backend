package ru.sibintek.vme.request.controller.specification;

import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;
import ru.sibintek.vme.request.domain.RequestBaseEntity;

@And({
        @Spec(params = "date", path = "date", spec = Equal.class),
        @Spec(params = "type", path = "type", spec = LikeIgnoreCase.class),
        @Spec(params = "typeOfWork", path = "typeOfWork", spec = LikeIgnoreCase.class),
        @Spec(params = "typeMachine", path = "typeMachine", spec = LikeIgnoreCase.class)
})
public interface RequestCollectionSpecification extends Specification<RequestBaseEntity> {
}
