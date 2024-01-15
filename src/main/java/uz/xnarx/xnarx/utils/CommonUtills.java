package uz.xnarx.xnarx.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import uz.xnarx.xnarx.exception.BadRequestException;

public class CommonUtills {
    public static void validatePageNumberAndSize( int page, int size){
        if (page<0){
            throw new BadRequestException("Sahifa soni noldan kam bo'ish mumkin emas.");
        }

        if (size>ApplicationConstants.MAX_PAGE_SIZE){
            throw new BadRequestException("Sahifa soni " +ApplicationConstants.MAX_PAGE_SIZE + " dan ko'p bo'lishi mumkin emas.");
        }
    }
    public static Pageable getPageableByCreatedAtDesc(int page, int size) {
        validatePageNumberAndSize(page, size);
        return PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    }

    public static Pageable getPageableByCreatedAtAsc(int page, int size) {
        validatePageNumberAndSize(page, size);
        return PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
    }

    public static Pageable simplePageable(int page, int size) {
        validatePageNumberAndSize(page, size);
        return PageRequest.of(page, size);
    }
}
