package com.spring.assignment.domain.book;

import com.spring.assignment.domain.book.entity.Book;
import com.spring.assignment.domain.book.repository.BookRepository;
import com.spring.assignment.domain.publisher.entity.Publisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookPublisherRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testSaveBook() {
        // Create a publisher
        Publisher publisher = Publisher.builder()
                .name("에이콘출판사")
                .establishedDate(LocalDate.of(1998, 6, 12))
                .address("서울시 종로구")
                .books(new ArrayList<>())
                .build();
        publisher = entityManager.persist(publisher);

        // Create a book
        Book book = Book.builder()
                .title("클린 코드")
                .author("로버트 마틴")
                .isbn("9788966260959")
                .publishDate(LocalDate.of(2013, 12, 24))
                .price(33000)
                .publisher(publisher)
                .build();

        // Save book
        Book savedBook = bookRepository.save(book);

        // Verify book saved
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("클린 코드");
        assertThat(savedBook.getAuthor()).isEqualTo("로버트 마틴");
        assertThat(savedBook.getIsbn()).isEqualTo("9788966260959");
        assertThat(savedBook.getPrice()).isEqualTo(33000);
        assertThat(savedBook.getPublisher().getName()).isEqualTo("에이콘출판사");
    }

    @Test
    public void testFindByIsbn() {
        // Create a publisher
        Publisher publisher = Publisher.builder()
                .name("제이펍")
                .establishedDate(LocalDate.of(2000, 4, 18))
                .address("경기도 파주시")
                .books(new ArrayList<>())
                .build();
        publisher = entityManager.persist(publisher);

        // Create a book
        Book book = Book.builder()
                .title("모던 자바스크립트 Deep Dive")
                .author("이웅모")
                .isbn("9791165212308")
                .publishDate(LocalDate.of(2020, 9, 25))
                .price(45000)
                .publisher(publisher)
                .build();

        entityManager.persist(book);
        entityManager.flush();

        // Find by ISBN
        Book foundBook = bookRepository.findByIsbn("9791165212308").orElse(new Book());

        // Verify book found
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getTitle()).isEqualTo("모던 자바스크립트 Deep Dive");
        assertThat(foundBook.getAuthor()).isEqualTo("이웅모");
    }

    @Test
    public void testFindByAuthor() {
        // Create a publisher
        Publisher publisher = Publisher.builder()
                .name("인사이트")
                .establishedDate(LocalDate.of(2003, 10, 5))
                .address("서울시 강남구")
                .books(new ArrayList<>())
                .build();
        publisher = entityManager.persist(publisher);

        // Create books by the same author
        Book book1 = Book.builder()
                .title("리팩터링")
                .author("마틴 파울러")
                .isbn("9788966262748")
                .publishDate(LocalDate.of(2018, 11, 5))
                .price(35000)
                .publisher(publisher)
                .build();

        Book book2 = Book.builder()
                .title("엔터프라이즈 애플리케이션 아키텍처 패턴")
                .author("마틴 파울러")
                .isbn("9791165213398")
                .publishDate(LocalDate.of(2021, 3, 15))
                .price(38000)
                .publisher(publisher)
                .build();

        Book book3 = Book.builder()
                .title("객체지향 프로그래밍")
                .author("그래디 부치")
                .isbn("9788966262755")
                .publishDate(LocalDate.of(2019, 5, 10))
                .price(32000)
                .publisher(publisher)
                .build();

        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.persist(book3);
        entityManager.flush();

        // Find books by author
        List<Book> books = bookRepository.findByAuthor("마틴 파울러");

        // Verify correct books found
        assertThat(books).hasSize(2);
        assertThat(books).extracting("title").containsOnly("리팩터링", "엔터프라이즈 애플리케이션 아키텍처 패턴");
    }

    @Test
    public void testFindByPublisher() {
        // Create two publishers
        Publisher publisher1 = Publisher.builder()
                .name("Manning")
                .establishedDate(LocalDate.of(1990, 2, 8))
                .address("미국")
                .books(new ArrayList<>())
                .build();

        Publisher publisher2 = Publisher.builder()
                .name("O'Reilly")
                .establishedDate(LocalDate.of(1985, 12, 15))
                .address("미국")
                .books(new ArrayList<>())
                .build();

        publisher1 = entityManager.persist(publisher1);
        publisher2 = entityManager.persist(publisher2);

        // Create books for each publisher
        Book book1 = Book.builder()
                .title("스프링 인 액션")
                .author("크레이그 월즈")
                .isbn("9791189909994")
                .publishDate(LocalDate.of(2020, 1, 30))
                .price(38000)
                .publisher(publisher1)
                .build();

        Book book2 = Book.builder()
                .title("자바 퍼시스턴스 위드 JPA")
                .author("크리스찬 바우어")
                .isbn("9791189909987")
                .publishDate(LocalDate.of(2019, 11, 20))
                .price(42000)
                .publisher(publisher1)
                .build();

        Book book3 = Book.builder()
                .title("러닝 파이썬")
                .author("마크 러츠")
                .isbn("9791162242742")
                .publishDate(LocalDate.of(2019, 6, 5))
                .price(35000)
                .publisher(publisher2)
                .build();

        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.persist(book3);
        entityManager.flush();

        // Find books by publisher
        List<Book> manningBooks = bookRepository.findByPublisher(publisher1);

        // Verify correct books found
        assertThat(manningBooks).hasSize(2);
        assertThat(manningBooks).extracting("title").containsOnly("스프링 인 액션", "자바 퍼시스턴스 위드 JPA");
    }

    @Test
    public void testUpdateBook() {
        // Create a publisher
        Publisher publisher = Publisher.builder()
                .name("프리렉")
                .establishedDate(LocalDate.of(2005, 7, 25))
                .address("서울시 마포구")
                .books(new ArrayList<>())
                .build();
        publisher = entityManager.persist(publisher);

        // Create a book
        Book book = Book.builder()
                .title("코틀린 프로그래밍")
                .author("황영덕")
                .isbn("9791162242735")
                .publishDate(LocalDate.of(2021, 8, 10))
                .price(28000)
                .publisher(publisher)
                .build();

        book = entityManager.persist(book);
        entityManager.flush();

        // Update book
        book.setPrice(30000);
        book.setTitle("코틀린 프로그래밍 마스터하기");
        bookRepository.save(book);

        // Find the book and verify updates
        Book updatedBook = bookRepository.findById(book.getId()).orElse(null);
        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getPrice()).isEqualTo(30000);
        assertThat(updatedBook.getTitle()).isEqualTo("코틀린 프로그래밍 마스터하기");
    }

    @Test
    public void testDeleteBook() {
        // Create a publisher
        Publisher publisher = Publisher.builder()
                .name("스타트업")
                .establishedDate(LocalDate.of(2015, 3, 15))
                .address("서울시 강남구")
                .books(new ArrayList<>())
                .build();
        publisher = entityManager.persist(publisher);

        // Create a book
        Book book = Book.builder()
                .title("디자인 패턴")
                .author("에릭 감마")
                .isbn("9791162242728")
                .publishDate(LocalDate.of(2020, 2, 5))
                .price(35000)
                .publisher(publisher)
                .build();

        book = entityManager.persist(book);
        entityManager.flush();

        // Delete book
        bookRepository.delete(book);

        // Verify book deleted
        Book deletedBook = bookRepository.findById(book.getId()).orElse(null);
        assertThat(deletedBook).isNull();
    }
}