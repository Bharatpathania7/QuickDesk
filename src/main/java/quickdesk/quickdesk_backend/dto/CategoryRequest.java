package quickdesk.quickdesk_backend.dto;

import lombok.Data;

@Data
public class CategoryRequest {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private  String name;
}
