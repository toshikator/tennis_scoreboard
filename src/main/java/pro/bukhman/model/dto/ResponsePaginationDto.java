package pro.bukhman.model.dto;

import java.util.List;

public record ResponsePaginationDto<T>(
        List<T> items,
        PaginationDto pagination
) {
}