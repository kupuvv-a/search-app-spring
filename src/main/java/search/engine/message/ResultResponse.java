package search.engine.message;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.Builder;

@Builder
public record ResultResponse(@NotNull String result, @Nullable String error) {
}
