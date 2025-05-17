package org.interview.bcs.domains.author;

import lombok.extern.slf4j.Slf4j;
import org.interview.bcs.BaseTest;
import org.interview.bcs.domains.author.entities.Author;
import org.interview.bcs.domains.author.mappers.AuthorMapper;
import org.interview.bcs.domains.data.Initializer;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author: wanghu
 * @since: 2025/5/17 09:30
 */
@Slf4j
public class AuthorMapperTest extends BaseTest {

    @Resource
    private AuthorMapper authorMapper;

    @Test
    void testGetAuthorsByName() {

        final List<Author> authors = Initializer.AUTHORS;

        authors.forEach( author -> {
            List<Author> authorsByName = authorMapper.getAuthorsByName( author.getName() )
                    .stream()
                    .filter( e -> e.getName().equals( author.getName() ) )
                    .collect( Collectors.toList() );

            assertThat( authorsByName.size() ).isEqualTo( 1 );
            assertThat( authorsByName.get( 0 ).getAuthorId() ).isEqualTo( author.getAuthorId() );
        } );

    }


}
