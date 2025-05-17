package org.interview.bcs.domains.book.service.impl;

import org.interview.bcs.api.base.BaseResp;
import org.interview.bcs.api.base.PageVo;
import org.interview.bcs.api.constant.BookStatusEnum;
import org.interview.bcs.api.domains.author.req.GetAuthorReq;
import org.interview.bcs.api.domains.author.resp.GetAuthorResp;
import org.interview.bcs.api.domains.author.vo.AuthorVo;
import org.interview.bcs.api.domains.book.req.DeleteBookReq;
import org.interview.bcs.api.domains.book.req.GetBookReq;
import org.interview.bcs.api.domains.book.req.UpsertBookReq;
import org.interview.bcs.api.domains.book.resp.GetBookResp;
import org.interview.bcs.api.domains.book.resp.UpsertBookResp;
import org.interview.bcs.api.domains.book.vo.BookVo;
import org.interview.bcs.api.domains.category.req.GetCategoryReq;
import org.interview.bcs.api.domains.category.resp.GetCategoryResp;
import org.interview.bcs.common.BeanCopy;
import org.interview.bcs.common.BizException;
import org.interview.bcs.common.PageUtils;
import org.interview.bcs.common.ValidationUtils;
import org.interview.bcs.domains.author.service.AuthorService;
import org.interview.bcs.domains.book.service.BookService;
import org.interview.bcs.domains.book.entites.Book;
import org.interview.bcs.domains.book.mappers.BookMapper;
import org.interview.bcs.domains.category.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: wanghu
 * @since: 2025/5/15 10:00
 */
@Service
public class BookServiceImpl implements BookService {

    @Resource
    private BookMapper bookMapper;

    @Resource
    private AuthorService authorService;

    @Resource
    private CategoryService categoryService;

    @Override
    public GetBookResp getBooks( GetBookReq req ) {
        // 切面
        ValidationUtils.validate( req );

        GetBookResp resp = new GetBookResp();

        GetAuthorReq getAuthorReq = GetAuthorReq.builder().authorName( req.getAuthorName() ).build();

        // 这里可以封装成远程调用，方便后续服务拆分
        GetAuthorResp authorsResp = authorService.getAuthors( getAuthorReq );
        if ( CollectionUtils.isEmpty( authorsResp.getAuthors() ) ) {
            return resp;
        }

        List<Long> authorIds = authorsResp.getAuthors().stream().map( AuthorVo::getAuthorId ).collect( Collectors.toList() );
        Integer status = req.getBookStatus() == null ? null : req.getBookStatus().getCode();

        // 时间问题，没有支持更多的参数查询
        PageVo<BookVo> page = PageUtils.query( req,
                r -> bookMapper.select( req.getIsbn(), req.getTitle(), authorIds, status ),
                this::bookToBookVo
        );

        resp.convert( page, resp::setBooks );

        return resp;
    }

    @Override
    public UpsertBookResp upsertBook( UpsertBookReq req ) {
        ValidationUtils.validate( req );

        // 业务验证
        bizValidate( req );
        Long bookId = req.getBookId();

        Book book = new Book();
        BeanCopy.copy( req, book );
        book.setStatus( req.getBookStatus().getCode() );
        book.setDescription( StringUtils.hasText( req.getDescription() ) ? req.getDescription() : "" );

        if ( Objects.isNull( bookId ) ) {
            bookMapper.insertSelective( book );
            bookId = book.getBookId();
        }
        else {
            bookMapper.updateByPrimaryKeySelective( book );
        }

        return UpsertBookResp.builder().bookId( bookId ).build();
    }

    @Override
    public BaseResp deleteBook( DeleteBookReq req ) {
        // 切面
        ValidationUtils.validate( req );
        Long bookId = req.getBookId();
        Book book = new Book();
        book.setBookId( bookId );
        book.setDisable( true );

        int effectRows = bookMapper.updateByPrimaryKeySelective( book );
        if( effectRows < 1 ){
            throw new BizException( "Delete book failed,book not found." );
        }

        return BaseResp.success();
    }

    private void bizValidate( UpsertBookReq req ) {

        final Long bookId = req.getBookId();
        if ( Objects.nonNull( bookId ) ) {
            Book book = bookMapper.selectByPrimaryKey( bookId );
            if ( Objects.isNull( book ) ) {
                throw new BizException( "Book not found." );
            }
        }
        final String isbn = req.getIsbn();
        if ( StringUtils.hasText( isbn ) ) {
            Book book = bookMapper.selectByIsbn( isbn );
            if ( Objects.nonNull( book ) && !book.getBookId().equals( bookId ) ) {
                throw new BizException( "isbn already exists." );
            }
        }

        final Long authorId = req.getAuthorId();
        if ( Objects.nonNull( authorId ) ) {
            // 这里可以封装成远程调用，方便后续服务拆分
            GetAuthorResp authorResp = authorService.getAuthors( GetAuthorReq.builder().authorId( authorId ).build() );
            if ( CollectionUtils.isEmpty( authorResp.getAuthors() ) ) {
                throw new BizException( "Author not found!" );
            }
        }

        final Long categoryId = req.getCategoryId();
        if ( Objects.nonNull( categoryId ) ) {
            // 这里可以封装成远程调用，方便后续服务拆分
            GetCategoryResp categoryResp = categoryService.getCategories( GetCategoryReq.builder().categoryId( categoryId ).build() );
            if ( CollectionUtils.isEmpty( categoryResp.getCategories() ) ) {
                throw new BizException( "Category not found!" );
            }
        }
        // 时间原因，出版社管理未开发
        Long publisherId = req.getPublisherId();
    }


    private BookVo bookToBookVo( Book book ) {
        BookVo bookVo = new BookVo();
        BeanCopy.copy( book, bookVo );
        bookVo.setBookStatus( BookStatusEnum.of( book.getStatus() ) );
        bookVo.setCategoryId( book.getCategoryId() );
        return bookVo;
    }

}
