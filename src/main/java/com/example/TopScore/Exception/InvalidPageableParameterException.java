package com.example.TopScore.Exception;

import org.springframework.data.domain.Pageable;

public class InvalidPageableParameterException extends RuntimeException {
    public InvalidPageableParameterException(Pageable page)
    {
        super("The page, size or sort argument are not valid, page: [" + page.getPageNumber() + "], size : [" + page.getPageSize() + "], sort: [" + page.getSort() + "]");
    }
}
