package com.maple.lucene.controller;

import com.maple.lucene.bean.BlogTitle;
import com.maple.lucene.consts.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiTerms;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
@Slf4j
public class TestController {

    private void printTerms(IndexReader reader) {
        try {
            Terms terms = MultiTerms.getTerms(reader, "title");
            TermsEnum iterator = terms.iterator();
            BytesRef term;
            while ((term = iterator.next()) != null) {
                log.info("分词：{}, term freq：{}, doc freq: {}", term.utf8ToString(), iterator.totalTermFreq(), iterator.docFreq());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 简单搜索
     */
    @RequestMapping("/searchText")
    public List<BlogTitle> searchText(String text) throws IOException, ParseException {
        try (Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(Constants.INDEX_DIR));
             IndexReader reader = DirectoryReader.open(directory);) {
            IndexSearcher searcher = new IndexSearcher(reader);
            printTerms(reader);

            TermQuery query = new TermQuery(new Term("title", text));


            // 获取前十条记录
            TopDocs topDocs = searcher.search(query, 10);
            // 获取总条数
            log.info("本次搜索共找到" + topDocs.totalHits + "条数据");
            // 获取得分文档对象（ScoreDoc）数组.SocreDoc中包含：文档的编号、文档的得分
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            List<BlogTitle> list = new ArrayList<>();
            for (ScoreDoc scoreDoc : scoreDocs) {
                // 取出文档编号
                int docId = scoreDoc.doc;
                // 根据编号去找文档
                Document doc = reader.document(docId);
                BlogTitle content = toEntity(doc);
                list.add(content);
            }
            return list;
        }
    }


    private BlogTitle toEntity(Document doc) {
        BlogTitle entity = new BlogTitle();
        String id = doc.get("id");
        entity.setId(Long.valueOf(id));
        entity.setTitle(doc.get("title"));
        entity.setDescription(doc.get("description"));
        return entity;
    }
}
