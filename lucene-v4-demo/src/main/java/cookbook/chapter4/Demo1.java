package cookbook.chapter4;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class Demo1 {

	private StandardAnalyzer analyzer = new StandardAnalyzer();
	private Directory directory = new RAMDirectory();
	private Document document = new Document();

	// 构建索引
	public void index() throws IOException {

		IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LATEST, analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, writerConfig);

		TextField textField = new TextField("content", "", Field.Store.YES);

		String[] contents = { "Humpty Dumpty sat on a wall,", "Humpty Dumpty had a great fall.",
				"All the king's horses and all the king's men", "Couldn't put Humpty together again." };

		for (String content : contents) {
			textField.setStringValue(content);
			document.removeField("content");
			document.add(textField);
			indexWriter.addDocument(document);
		}

		indexWriter.commit();
		indexWriter.close();
	}

	// 通过索引 检索文档
	public void search() throws IOException, ParseException {

		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		QueryParser queryParser = new QueryParser("content", analyzer);
		Query query = queryParser.parse("humpty dumpty");

		TopDocs topDocs = indexSearcher.search(query, 2);
		System.out.println("Total hits: " + topDocs.totalHits);

		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			document = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " + document.getField("content").stringValue());
		}
	}
	
	
	public static void main(String[] args) throws IOException, ParseException {
		Demo1 demo1 = new Demo1();
		demo1.index();
		demo1.search();
	}
}
