package core.basesyntax.strategy;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

import core.basesyntax.db.Storage;
import core.basesyntax.model.Fruit;
import core.basesyntax.service.impl.StorageServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DecrementHandlerTest {
    private DecrementHandler decrementHandler;
    private Fruit banana;

    @BeforeEach
    void setUp() {
        StorageServiceImpl storageService = new StorageServiceImpl();
        decrementHandler = new DecrementHandler(storageService);
        banana = new Fruit("banana");
    }

    @AfterEach
    void tearDown() {
        Storage.clear();
    }

    @Test
    void handle_validDecrementing_ok() {
        Storage.updateFruitQuantity(banana, 50);
        decrementHandler.handle(banana, 30);
        assertEquals(20, Storage.getFruitQuantity(banana));
    }

    @Test
    void handle_insufficientStock_notOk() {
        Storage.updateFruitQuantity(banana, 10);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> decrementHandler.handle(banana, 20))
                .withMessage("Not enough banana in storage to remove");
    }
}
