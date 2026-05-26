package pro.bukhman.model.dto;

public record PaginationDto(
        int offset,
        int limit,
        long total
) {
}