package leesh.devcom.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IndexControllerTest {

    @Autowired
    IndexController indexController;

    @Test
    void indexTest() {
        ResponseEntity<?> index = indexController.index();
    }

}