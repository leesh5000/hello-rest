package leesh.devcom.backend;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public interface RestDocsConfig {

    static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                modifyUris() // (1)
                        .scheme("http")
                        .host("localhost")
                        .port(18080),
//                        .removePort(),
                prettyPrint()); // (2)
    }

    static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(
                modifyUris()
                        .scheme("http")
                        .host("localhost")
                        .port(18080),
                prettyPrint()); // (3)
    }
}
