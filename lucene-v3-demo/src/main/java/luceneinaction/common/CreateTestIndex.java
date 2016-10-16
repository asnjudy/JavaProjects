package luceneinaction.common;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by asnju on 2016/9/15.
 */
public class CreateTestIndex {


    public static Document getDocument(String rootDir, File file) throws IOException, ParseException {

        Properties props = new Properties();
        props.load(new FileInputStream(file));

        Document doc = new Document();

        String category = file.getParent().substring(rootDir.length());
        category = category.replace(File.separatorChar, '/');

        String isbn = props.getProperty("isbn");
        String title = props.getProperty("title");
        String author = props.getProperty("author");
        String url = props.getProperty("url");
        String subject = props.getProperty("subject");
        String pubmonth = props.getProperty("pubmonth");

        System.out.println(title + "\n" + author + "\n" + subject + "\n" + pubmonth + "\n" + category + "\n---------");

        doc.add(new Field("isbn", isbn, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("category", category, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("title",
                title,
                Field.Store.YES,
                Field.Index.ANALYZED,
                Field.TermVector.WITH_POSITIONS_OFFSETS));

        doc.add(new Field("title2",
                title.toLowerCase(),
                Field.Store.YES,
                Field.Index.ANALYZED,
                Field.TermVector.WITH_POSITIONS_OFFSETS
        ));

        String[] authors = author.split(",");
        for (String a : authors) {
            doc.add(new Field("author",
                    a,
                    Field.Store.YES,
                    Field.Index.NOT_ANALYZED,
                    Field.TermVector.WITH_POSITIONS_OFFSETS));
        }

        doc.add(new Field("url",
                url,
                Field.Store.YES,
                Field.Index.NOT_ANALYZED_NO_NORMS));

        doc.add(new Field("subject",
                subject,
                Field.Store.YES,
                Field.Index.ANALYZED,
                Field.TermVector.WITH_POSITIONS_OFFSETS));

        doc.add(new NumericField("pubmonth", Field.Store.YES, true)
                .setIntValue(Integer.parseInt(pubmonth)));

        Date date;

        date = DateTools.stringToDate(pubmonth);
        doc.add(new NumericField("pubmonthAsDay")
                .setIntValue((int) date.getTime() / (1000 * 60 * 60 * 24)));

        for (String text : new String[]{title, subject, author, category}) {
            doc.add(new Field("contents",
                    text,
                    Field.Store.NO,
                    Field.Index.ANALYZED,
                    Field.TermVector.WITH_POSITIONS_OFFSETS));
        }

        return doc;
    }

    private static String aggregate(String[] strings) {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < strings.length; i++) {
            buffer.append(strings[i]);
            buffer.append(" ");
        }
        return buffer.toString();
    }


    /**
     * 加载文件
     *
     * @param result
     * @param dir
     */
    private static void findFiles(List<File> result, File dir) {
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".properties")) {
                result.add(file);
            } else if (file.isDirectory()) {
                findFiles(result, file);
            }
        }
    }


/*
    private static class MyStandardAnalyzer extends StandardAnalyzer {

        public MyStandardAnalyzer(Version matchVersion) {
            super(matchVersion);
        }
        public int getPositionIncrementGap(String field) {
            if (field.equals("contents"))
                return 100;
            else
                return 0;
        }
    }*/


    public static void main(String[] args) throws IOException, ParseException {

        String dataDir = "data";
        String indexDir = "index/books";

        List<File> results = new ArrayList<File>();
        findFiles(results, new File(dataDir));
        System.out.println(results.size() + " books to index");

        Directory directory = FSDirectory.open(new File(indexDir));
        IndexWriter indexWriter = new IndexWriter(directory, new StandardAnalyzer(Version.LUCENE_30), true, IndexWriter.MaxFieldLength.UNLIMITED);

        for (File file : results) {
            Document document = getDocument(dataDir, file);
            indexWriter.addDocument(document);
        }

        indexWriter.close();
        directory.close();

    }

}
