package store.dto;

import java.util.List;

public class InventoryDTO {
    private List<ProductDTO> inventory;

    public InventoryDTO(final List<ProductDTO> inventory) {
        this.inventory = inventory;
    }

    public List<ProductDTO> getInventory() {
        return inventory;
    }
}
