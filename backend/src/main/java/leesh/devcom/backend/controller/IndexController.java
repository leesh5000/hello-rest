package leesh.devcom.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestController
public class IndexController {
    @GetMapping("/")
    public ResponseEntity<?> index() {
        RepresentationModel<?> links = new RepresentationModel<>();
        links.add(linkTo(IndexController.class).withSelfRel());
        links.add(Link.of("/index.html").withRel("profile"));
        return ResponseEntity.ok(links);
    }
}