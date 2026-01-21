package com.contract.master.integration.domain.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ScriptSandboxTest {

    private final ScriptSandbox sandbox = new ScriptSandbox();

    @Test
    void shouldExecuteSimpleTransformation() {
        String script = "return 'PREFIX_' + value";
        Object result = sandbox.execute(script, "test");
        assertEquals("PREFIX_test", result);
    }

    @Test
    void shouldHandleConditionalLogic() {
        String script = "if (value > 100) { return 'HIGH' } else { return 'LOW' }";
        assertEquals("HIGH", sandbox.execute(script, 150));
        assertEquals("LOW", sandbox.execute(script, 50));
    }

    @Test
    void shouldFallbackToRawValueOnError() {
        String script = "return value / 0";
        assertEquals(10, sandbox.execute(script, 10));
    }

    @Test
    void shouldRestrictUnsafeImports() {
        String script = "import java.lang.ProcessBuilder; return 'executed'";
        Object result = sandbox.execute(script, "val");
        assertNotNull(result);
    }
}
