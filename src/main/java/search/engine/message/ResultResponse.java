package search.engine.message;


import lombok.Builder;

import javax.annotation.Nullable;

@Builder
public record ResultResponse(String result, @Nullable String description) {
}
