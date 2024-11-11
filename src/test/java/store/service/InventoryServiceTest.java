package store.service;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest extends NsTest {
    private static final InventoryService inventoryService = new InventoryService();

    @Test
    @DisplayName("products.md 파일 읽어 오기")
    void readProductsFile() {
        List<Product> products = inventoryService.readProductsFile();
        assertNotNull(products);
    }

    @Override
    protected void runMain() {

    }
}