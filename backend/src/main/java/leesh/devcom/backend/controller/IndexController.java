package leesh.devcom.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RequiredArgsConstructor
@RestController
public class IndexController {

    @GetMapping(value = "/", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> index() {
        RepresentationModel<?> links = new RepresentationModel<>();

        links.add(
                linkTo(methodOn(IndexController.class).index()).withSelfRel(),
                Link.of("http://localhost:18080/docs/index.html").withRel("profile"),
                linkTo(methodOn(AuthController.class).login(null, null)).withRel("login")
        );

        return ResponseEntity.ok(links);
    }
}