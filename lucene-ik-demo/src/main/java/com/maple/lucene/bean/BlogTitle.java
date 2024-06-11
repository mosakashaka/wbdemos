package com.maple.lucene.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * blog标题
 * </p>
 *
 * @author 笑小枫 <https://www.xiaoxiaofeng.com/>
 * @since 2023-01-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//@TableName("blog_title")
public class BlogTitle implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String description;
}
