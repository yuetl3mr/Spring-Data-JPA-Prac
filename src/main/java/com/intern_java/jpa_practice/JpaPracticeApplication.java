package com.intern_java.jpa_practice;

import com.intern_java.jpa_practice.model.Author;
import com.intern_java.jpa_practice.model.Book;
import com.intern_java.jpa_practice.repository.AuthorRepository;
import com.intern_java.jpa_practice.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaPracticeApplication implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaPracticeApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        System.out.println("----- BẮT ĐẦU DEMO AUTHOR-BOOK (CHẠY TỪ APPLICATION) -----");

        // ----- 1. CREATE -----
        System.out.println("\n----- CREATE -----");
        Author author1 = new Author("Nguyễn Nhật Ánh", "nna@example.com", "Việt Nam");
        Author author2 = new Author("Tô Hoài", "tohoai@example.com", "Việt Nam");

        Book book1 = new Book("Cho tôi xin một vé đi tuổi thơ", "ISBN-001", 2008);
        Book book2 = new Book("Mắt biếc", "ISBN-002", 1990);
        Book book3 = new Book("Dế Mèn phiêu lưu ký", "ISBN-003", 1941);

        author1.addBook(book1);
        author1.addBook(book2);
        author2.addBook(book3);

        authorRepository.save(author1);
        authorRepository.save(author2);

        System.out.println("Đã lưu " + authorRepository.count() + " tác giả và " + bookRepository.count() + " sách.");

        // ----- 2. READ -----
        System.out.println("\n----- READ -----");
        System.out.println("Danh sách tác giả và sách của họ:");
        authorRepository.findAll().forEach(author -> {
            System.out.println("- Tác giả: " + author.getName() + " (" + author.getEmail() + ")");
            author.getBooks().forEach(book -> {
                System.out.println("  -> " + book.getTitle() + " (ISBN: " + book.getIsbn() + ", Năm: " + book.getPublishYear() + ")");
            });
        });

        // ----- 3. UPDATE -----
        System.out.println("\n----- UPDATE -----");
        Author authorToUpdate = authorRepository.findById(author1.getId()).orElse(null);
        if (authorToUpdate != null) {
            System.out.println("Email cũ: " + authorToUpdate.getEmail());
            authorToUpdate.setEmail("nguyennhatanh.official@example.com");
            authorRepository.save(authorToUpdate);
            System.out.println("Email mới: " + authorToUpdate.getEmail());

            Book bookToUpdate = authorToUpdate.getBooks().get(0);
            System.out.println("Tên sách cũ: " + bookToUpdate.getTitle());
            bookToUpdate.setTitle("Cho tôi xin một vé đi tuổi thơ (Tái bản)");
            bookRepository.save(bookToUpdate);
            System.out.println("Tên sách mới: " + bookToUpdate.getTitle());
        }

        // ----- 4. DELETE -----
        System.out.println("\n----- DELETE -----");
        Author authorToDelete = authorRepository.findById(author2.getId()).orElse(null);
        if (authorToDelete != null) {
            System.out.println("Sắp xóa tác giả: " + authorToDelete.getName()
                    + " (có " + authorToDelete.getBooks().size() + " sách)");
            authorRepository.delete(authorToDelete);
            System.out.println("Đã xóa tác giả " + authorToDelete.getName());
            System.out.println("Số sách còn lại trong DB: " + bookRepository.count() + " (orphanRemoval đã xóa sách của tác giả)");
        }

        System.out.println("\n----- KẾT QUẢ CUỐI CÙNG -----");
        System.out.println("Tổng số tác giả: " + authorRepository.count());
        System.out.println("Tổng số sách: " + bookRepository.count());

        System.out.println("\nDanh sách còn lại:");
        authorRepository.findAll().forEach(author -> {
            System.out.println("- " + author.getName() + " có " + author.getBooks().size() + " sách");
        });
    }
}
