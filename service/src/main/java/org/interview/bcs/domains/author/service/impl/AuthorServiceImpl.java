package org.interview.bcs.domains.author.service.impl;

import org.interview.bcs.api.base.BaseResp;
import org.interview.bcs.api.base.PageVo;
import org.interview.bcs.api.domains.author.req.DeleteAuthorReq;
import org.interview.bcs.api.domains.author.req.GetAuthorReq;
import org.interview.bcs.api.domains.author.req.UpsertAuthorReq;
import org.interview.bcs.api.domains.author.resp.GetAuthorResp;
import org.interview.bcs.api.domains.author.resp.UpsertAuthorResp;
import org.interview.bcs.api.domains.author.vo.AuthorVo;
import org.interview.bcs.common.*;
import org.interview.bcs.domains.author.entities.Author;
import org.interview.bcs.domains.author.mappers.AuthorMapper;
import org.interview.bcs.domains.author.service.AuthorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author: wanghu
 * @since: 2025/5/15 14:43
 */
@Service
public class AuthorServiceImpl implements AuthorService {

    @Resource
    private AuthorMapper authorMapper;

    @Override
    public GetAuthorResp getAuthors( GetAuthorReq req ) {

        // 可以通过切面来完成
        ValidationUtils.validate( req );

        Long authorId = req.getAuthorId();
        GetAuthorResp resp = new GetAuthorResp();

        if ( Objects.nonNull( authorId ) ) {
            Author author = authorMapper.selectByPrimaryKey( req.getAuthorId() );
            resp.setAuthors( new ArrayList<>() );
            if ( Objects.nonNull( author ) ) {
                resp.setTotals( 1 );
                resp.setPages( 1 );
                resp.getAuthors().add( authorToAuthorVo( author ) );
            }
            return resp;
        }

        PageVo<AuthorVo> pageVo = PageUtils.query(
                req, r -> authorMapper.getAuthorsByName( r.getAuthorName() ),
                this::authorToAuthorVo
        );
        resp.convert( pageVo, resp::setAuthors );
        return resp;
    }

    @Override
    @Transactional
    public UpsertAuthorResp upsertAuthor( UpsertAuthorReq req ) {
        // 可以通过切面来完成
        ValidationUtils.validate( req );


        // 业务验证
        bizValidate( req );

        Long authorId = req.getAuthorId();

        Author authorPo = new Author();
        BeanCopy.copy( req, authorPo );
        authorPo.setName( req.getAuthorName() );
        if ( Objects.nonNull( authorId ) ) {
            authorMapper.updateByPrimaryKeySelective( authorPo );
        }
        else {
            authorMapper.insertSelective( authorPo );
            authorId = authorPo.getAuthorId();
        }

        return UpsertAuthorResp.builder().authorId( authorId ).build();
    }

    private void bizValidate( UpsertAuthorReq req ) {
        final Long authorId = req.getAuthorId();
        if ( Objects.nonNull( authorId ) ) {
            Author author = authorMapper.selectByPrimaryKey( authorId );
            if ( Objects.isNull( author ) ) {
                throw new BizException( String.format( "Author[%d] not found!", authorId ) );
            }
        }
    }

    @Override
    @Transactional
    public BaseResp deleteAuthor( DeleteAuthorReq req ) {
        // 可以通过切面来完成
        ValidationUtils.validate( req );

        Author author = new Author();
        author.setAuthorId( req.getAuthorId() );
        author.setDisable( true );
        int effectRows = authorMapper.updateByPrimaryKeySelective( author );

        if ( effectRows < 1 ) {
            throw new BizException( String.format( "Author[%d] not found!", req.getAuthorId() ) );
        }

        return BaseResp.success();
    }

    private AuthorVo authorToAuthorVo( Author author ) {
        AuthorVo authorVo = new AuthorVo();
        BeanCopy.copy( author, authorVo );
        authorVo.setAuthorName( author.getName() );
        return authorVo;
    }

}
