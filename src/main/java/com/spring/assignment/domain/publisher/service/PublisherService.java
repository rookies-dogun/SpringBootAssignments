package com.spring.assignment.domain.publisher.service;

import com.spring.assignment.domain.book.dto.BookDTO;
import com.spring.assignment.domain.book.entity.Book;
import com.spring.assignment.domain.publisher.entity.Publisher;
import com.spring.assignment.domain.publisher.repository.PublisherRepository;
import com.spring.assignment.exception.BusinessException;
import com.spring.assignment.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;


    public List<BookDTO.Response> getAllBookByPublisherId(Long id){
        Publisher publisher = publisherRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "id", id));
        return publisher.getBooks().stream().map(BookDTO.Response::fromEntity).toList();

    }

}
