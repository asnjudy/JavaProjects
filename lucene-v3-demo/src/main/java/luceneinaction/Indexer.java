package luceneinaction;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

/**
 * Hello world!
 */
public class Indexer {

    private IndexWriter writer;

    public Indexer(String indexDir) throws IOException {
        Directory dir = FSDirectory.open(new File(indexDir));

        //实例化 lucene 索引写入器
        writer = new IndexWriter(dir,
                new StandardAnalyzer(Version.LUCENE_30),
                true,
                IndexWriter.MaxFieldLength.UNLIMITED);
    }

    public static void main(String[] args) throws IOException {

        String indexDir = "data\\index";
        String dataDir = "F:\\桌面\\Lucene\\lucene-3.5.0\\docs";

        long start = System.currentTimeMillis();
        Indexer indexer = new Indexer(indexDir);
        int numIndexed = -1;

        try {
            numIndexed = indexer.index(dataDir);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            indexer.close();
        }

        long end = System.currentTimeMillis();
        System.out.println("Indexing " + numIndexed + " files took " + (end - start) + " milliseconds");


    }

    public void close() throws IOException {
        writer.close();
    }

    public int index(String dataDir) throws Exception {
        File[] files = new File(dataDir).listFiles();

        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith(".html");
            }
        };

        for (File f : files) {
            if (!f.isDirectory() && !f.isHidden() && f.exists() && f.canRead()) {

                if (fileFilter.accept(f)) {
                    //对文件建立索引
                    System.out.println("Indexing " + f.getCanonicalPath());
                    Document doc = getDocument(f);
                    writer.addDocument(doc); //向lucene索引中添加文档
                }
            }
        }
        return writer.numDocs(); //返回被索引文件数
    }

    protected Document getDocument(File f) throws Exception {
        Document doc = new Document();
        doc.add(new Field("contents", new FileReader(f))); //索引文件内容
        doc.add(new Field("filename", f.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED)); //索引文件名
        doc.add(new Field("fullpath", f.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED)); //索引文件完整路径
        return doc;
    }
}