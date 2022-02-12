package leesh.devcom.backend.dto.assembler;

import leesh.devcom.backend.controller.AuthController;
import leesh.devcom.backend.dto.LoginResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class LoginResponseAssembler implements RepresentationModelAssembler<LoginResponse, EntityModel<LoginResponse>> {

    @Override
    public EntityModel<LoginResponse> toModel(LoginResponse entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(AuthController.class).login(null, null)).withSelfRel(),
                linkTo(methodOn(AuthController.class).logout("")).withRel("logout"),
                Link.of("http://localhost:18080/docs/index.html#login").withRel("profile")
        );
    }
}
