package com.spring.assignment.domain.publisher.repository;

import com.spring.assignment.domain.publisher.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Publisher findByName(String name);

    @Query("select count(*) from Publisher p where p.id = :publisher")
    Long countBooksByPublisherId(Long publisherId);
}
