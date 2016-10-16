package mysqltolucene;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.UUID;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.document.Field;

public class QueryDataFromDb {

	private Directory directory = null;
	private static boolean insertFlag = true;

	public static Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://127.0.0.1:3306/test";
		String username = "root";
		String password = "123";
		Connection connection = DriverManager.getConnection(url, username, password);
		return connection;
	}

	/**
	 * 插入数据
	 * @throws SQLException
	 */
	public void insertData() throws SQLException {
		Connection connection = getConnection();
		Statement stmt = connection.createStatement();

		Random random = new Random();

		for (int i = 0; i < 10 && insertFlag; i++) {
			StringBuffer sql = new StringBuffer("insert student (name, math) values ");

			for (int k = 0; k < 100000; k++) {
				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				sql.append("('" + uuid + "'," + random.nextInt(100) + "),");
			}
			String insert = sql.toString().substring(0, sql.length() - 1);
			stmt.execute(insert);
		}
		stmt.close();
		connection.close();
	}

	/**
	 * 建立索引
	 */
	public void index() {
		
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
		writerConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
		writerConfig.setMaxBufferedDocs(100);
		
		IndexWriter indexWriter = null;
		Connection connection = null;
		try {
			directory = FSDirectory.open(new File("lucene-index-data"));	
			indexWriter = new IndexWriter(directory, writerConfig);
			
			//insertFlag = false;
			insertData();
			
			connection = getConnection();
			Statement stmt = connection.createStatement();
			long count = 1;
			
			for (int i = 1; i < 10; i++) {
				String query = "select * from student limit "+ i * 100000+","+ 100000;
		        ResultSet result = stmt.executeQuery(query);
		        while (result.next()) {
		          Document document = new Document();
		          document.add(new StringField("id", result.getString("id"), Field.Store.YES));
		          document.add(new StringField("name", result.getString("name"), Field.Store.YES));
		          document.add(new StringField("math", result.getString("math"), Field.Store.YES));
		          indexWriter.addDocument(document);
		          count ++;
		        }
			}
			System.out.println("Total records: " + count);	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (indexWriter != null) {
				try {
					indexWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public IndexSearcher getSearcher() throws IOException {
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
	
	/**
	 * 执行查询
	 * @param field
	 * @param name
	 * @param num
	 * @throws IOException
	 */
	public void searchByTerm(String field, String name, int num) throws IOException {
		/**
		 * util.searchByTerm("math", "90", 2);
		 * 
		 * new Term(field, name) -> new Term("math", name)
		 * 
		 */
		
		IndexSearcher searcher = getSearcher();
		
		Query query = new TermQuery(new Term(field, name));
		
		TopDocs topDocs = searcher.search(query, num);
		System.out.println("search results count: " + topDocs.totalHits);
		
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document document = searcher.doc(scoreDoc.doc);
			System.out.println("id: " + document.get("id"));
			System.out.println("name: " + document.get("name"));
			System.out.println("math: " + document.get("math"));
		}
	}
	
	public static void main(String[] args) throws IOException, SQLException {
		
		QueryDataFromDb util = new QueryDataFromDb();
		util.index();
		
		int i = 0;
		long start = System.currentTimeMillis();
		
		System.out.println("查询前90分 and 前2名 的学生信息");
		util.searchByTerm("math", "90", 2);
		System.out.println(i + " spend time: " + (System.currentTimeMillis() - start) + "ms");
		
	}
}
