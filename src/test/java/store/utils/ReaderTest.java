package store.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {

    @Test
    @DisplayName("products.md 파일 읽어오기")
    void testReadFile() {
        List<List<String>> productsInfos = Reader.readFile("products.md");
        assert productsInfos != null;
        for (List<String> productsInfo : productsInfos) {
            assertEquals(productsInfo.size(), 4);
        }
    }

    @Test
    @DisplayName("promotions.md 파일 읽어오기")
    void testReadPromotionsMd() {
        List<List<String>> promotionsInfos = Reader.readFile("promotions.md");
        assert promotionsInfos != null;
        for (List<String> promotionInfo : promotionsInfos) {
            assertEquals(promotionInfo.size(), 5);
        }
    }

}