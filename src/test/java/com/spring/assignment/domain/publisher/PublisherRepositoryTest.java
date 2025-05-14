package com.spring.assignment.domain.publisher;

import com.spring.assignment.domain.book.entity.Book;
import com.spring.assignment.domain.publisher.entity.Publisher;
import com.spring.assignment.domain.publisher.repository.PublisherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PublisherRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    public void testSavePublisher() {
        // Create a publisher
        Publisher publisher = Publisher.builder()
                .name("한빛미디어")
                .establishedDate(LocalDate.of(1990, 5, 15))
                .address("서울시 마포구")
                .books(new ArrayList<>())
                .build();
        
        // Save publisher
        Publisher savedPublisher = publisherRepository.save(publisher);
        
        // Verify publisher saved
        assertThat(savedPublisher).isNotNull();
        assertThat(savedPublisher.getId()).isNotNull();
        assertThat(savedPublisher.getName()).isEqualTo("한빛미디어");
        assertThat(savedPublisher.getEstablishedDate()).isEqualTo(LocalDate.of(1990, 5, 15));
    }

    @Test
    public void testFindByName() {
        // Create a publisher
        Publisher publisher = Publisher.builder()
                .name("위키북스")
                .establishedDate(LocalDate.of(1995, 3, 10))
                .address("서울시 강남구")
                .books(new ArrayList<>())
                .build();
        
        // Save publisher
        entityManager.persist(publisher);
        entityManager.flush();
        
        // Find by name
        Publisher foundPublisher = publisherRepository.findByName("위키북스");
        
        // Verify publisher found
        assertThat(foundPublisher).isNotNull();
        assertThat(foundPublisher.getName()).isEqualTo("위키북스");
        assertThat(foundPublisher.getAddress()).isEqualTo("서울시 강남구");
    }

    @Test
    public void testCountBooksByPublisherId() {
        // Create a publisher
        Publisher publisher = Publisher.builder()
                .name("길벗")
                .establishedDate(LocalDate.of(1992, 8, 20))
                .address("서울시 서초구")
                .books(new ArrayList<>())
                .build();
        
        // Save publisher
        publisher = entityManager.persist(publisher);
        
        // Create some books for this publisher
        Book book1 = Book.builder()
                .title("자바 프로그래밍")
                .author("홍길동")
                .isbn("9788956748511")
                .publishDate(LocalDate.of(2022, 3, 15))
                .price(25000)
                .publisher(publisher)
                .build();
        
        Book book2 = Book.builder()
                .title("스프링 부트 실전 가이드")
                .author("김철수")
                .isbn("9788956748528")
                .publishDate(LocalDate.of(2022, 5, 20))
                .price(30000)
                .publisher(publisher)
                .build();
        
        // Save books
        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.flush();
        
        // Count books from publisher
        Long bookCount = publisherRepository.countBooksByPublisherId(publisher.getId());
        
        // Verify count
        assertThat(bookCount).isEqualTo(2L);
    }
}