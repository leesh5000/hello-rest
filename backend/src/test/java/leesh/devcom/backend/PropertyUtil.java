package leesh.devcom.backend;

import leesh.devcom.backend.common.GlobalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class PropertyUtil {

    @Autowired
    GlobalProperties globalProperties;

    public final String address = this.globalProperties.getServer().getAddress();

}
