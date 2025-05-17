package org.interview.bcs.domains.data;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.interview.bcs.api.constant.BookStatusEnum;
import org.interview.bcs.api.constant.Constants;
import org.interview.bcs.domains.author.entities.Author;
import org.interview.bcs.domains.author.mappers.AuthorMapper;
import org.interview.bcs.domains.book.entites.Book;
import org.interview.bcs.domains.book.mappers.BookMapper;
import org.interview.bcs.domains.category.entites.Category;
import org.interview.bcs.domains.category.mappers.CategoryMapper;
import org.interview.bcs.domains.data.mappers.ExecMapper;
import org.junit.jupiter.api.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: wanghu
 * @since: 2025/5/16 22:53
 */
@Slf4j
@Component
@Order( Integer.MIN_VALUE )
public class Initializer {

    /**
     * 数据是在测试时候给予 csv 文件生成的
     */
    public static final List<Author> AUTHORS = new ArrayList<>();

    /**
     * 数据是在测试时候给予 csv 文件生成的
     */
    public static final List<Category> CATEGORIES = new ArrayList<>();

    /**
     * 数据是在测试时候给予 csv 文件生成的
     */
    public static final List<Book> BOOKS = new ArrayList<>();

    private static final Random RANDOM = new SecureRandom();

    @Resource
    private ExecMapper execMapper;

    @Resource
    private AuthorMapper authorMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private BookMapper bookMapper;

    private static final String TRUNCATE_AUTHOR = "TRUNCATE TABLE author";
    private static final String TRUNCATE_CATEGORY = "TRUNCATE TABLE category";
    private static final String TRUNCATE_BOOK = "TRUNCATE TABLE book";

    @PostConstruct
    public void init() throws Exception {
        cleanTable();

        List<CsvBook> books = readFromCsv();

        genAuthor( books );
        genCategory( books );
        genBook( books, AUTHORS, CATEGORIES );
        log.info( "gen data end." );
    }

    private void genBook( List<CsvBook> books, List<Author> authors, List<Category> categories ) {
        Map<String, Author> authorMap = authors.stream().collect( Collectors.toMap( Author::getName, e -> e ) );
        Map<String, Category> categoryMap = categories.stream().collect( Collectors.toMap( Category::getName, e -> e ) );

        for ( CsvBook book : books ) {
            Book b = new Book();
            b.setIsbn( book.getBookId() );
            b.setTitle(book.getTitle());
            b.setAuthorId( authorMap.get( book.getAuthor() ).getAuthorId() );
            b.setPublisherId(11L);
            b.setPublicationYear(book.getPublicationYear());
            b.setCategoryId( categoryMap.get( book.getCategory() ).getCategoryId() );
            b.setLanguage( authorMap.get( book.getAuthor() ).getNationality() );
            b.setPageCount( 100);
            b.setCoverImage("");
            b.setStatus( BookStatusEnum.AVAILABLE.getCode() );
            b.setDescription("default description");
            bookMapper.insertSelective( b );
            BOOKS.add( b );
        }
    }

    private void genCategory( List<CsvBook> books ) {
        Set<String> categoryNames = books.stream().map( CsvBook::getCategory ).collect( Collectors.toSet() );
        CATEGORIES.clear();
        for ( String name : categoryNames ) {
            Category category = new Category();
            category.setName( name );
            category.setParentId( Constants.ROOT_CATEGORY_ID );
            category.setDescription( name );
            categoryMapper.insertSelective( category );
            CATEGORIES.add( category );
        }

        Long categoryId = CATEGORIES.get( RANDOM.nextInt( CATEGORIES.size() ) ).getCategoryId();
        Category onlyChild = new Category();
        onlyChild.setName( "onlyChild" );
        onlyChild.setParentId( categoryId );
        onlyChild.setDescription( "onlyChild" );
        categoryMapper.insertSelective( onlyChild );
        CATEGORIES.add( onlyChild );
    }

    private void genAuthor( List<CsvBook> books ) {
        AUTHORS.clear();
        for ( CsvBook book : books ) {
            Author author = new Author();
            author.setName( book.getAuthor() );
            author.setNationality( RANDOM.nextBoolean() ? "cn" : "en" );
            AUTHORS.add( author );
            authorMapper.insertSelective( author );
        }
    }

    private void cleanTable() {
        execMapper.execute( TRUNCATE_AUTHOR );
        execMapper.execute( TRUNCATE_CATEGORY );
        execMapper.execute( TRUNCATE_BOOK );
    }

    private List<CsvBook> readFromCsv() throws Exception {
        InputStream in = Initializer.class.getClassLoader().getResourceAsStream( "init_data.csv" );
        CSVReader reader = new CSVReader( new InputStreamReader( in ) );

        int row = 0;
        String[] nextLine;

        List<CsvBook> books = new ArrayList<>();
        while ( ( nextLine = reader.readNext() ) != null ) {
            if ( row != 0 ) {
                CsvBook csvBook = new CsvBook();
                csvBook.setBookId( nextLine[0] );
                csvBook.setTitle( nextLine[1] );
                csvBook.setAuthor( nextLine[2] );
                csvBook.setPublicationYear( Integer.valueOf( nextLine[3] ) );
                csvBook.setCategory( nextLine[4] );
                books.add( csvBook );
            }
            row++;
        }
        return books;
    }

    @PreDestroy
    public void clean() {
        cleanTable();
    }

}
