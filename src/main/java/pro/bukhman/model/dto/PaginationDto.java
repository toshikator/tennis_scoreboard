package pro.bukhman.model.dto;

public record PaginationDto(
        int page,
        int limit,
        long total
) {
}