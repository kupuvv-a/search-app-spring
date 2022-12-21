package search.engine.dao.message;

import lombok.Data;
import search.engine.model.Page;

@Data
public class SaveResponse {
    private String url;
    private Page page;
}
