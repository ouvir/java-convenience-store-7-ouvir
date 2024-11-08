package store.dto;

import java.util.ArrayList;
import java.util.List;

public class InventoryDTO {
    private List<ProductDTO> inventory = new ArrayList<>();

    public InventoryDTO(List<ProductDTO> inventory) {
        this.inventory = inventory;
    }

    public List<ProductDTO> getInventory() {
        return inventory;
    }
}
