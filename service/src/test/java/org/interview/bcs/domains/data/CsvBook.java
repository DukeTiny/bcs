package org.interview.bcs.domains.data;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: wanghu
 * @since: 2025/5/16 22:57
 */
@Setter
@Getter
public class CsvBook {

    private String bookId;

    private String title;

    private String author;

    private String category;

    private int publicationYear;

}
