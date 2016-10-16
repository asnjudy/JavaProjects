package cookbook.chapter3;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;



public class CreateNumericField {
	
	
	public static void main(String[] args) throws IOException {
		
		IntField intField = new IntField("int_value", 100, Field.Store.YES);
		LongField longField = new LongField("long_value", 100L, Field.Store.YES);
		FloatField floatField = new FloatField("float_value", 100.0F, Field.Store.YES);
		DoubleField doubleField = new DoubleField("double_value", 100.0D, Field.Store.YES);
		
		FieldType sortedIntField = new FieldType();
		sortedIntField.setNumericType(FieldType.NumericType.INT);
		sortedIntField.setNumericPrecisionStep(Integer.MAX_VALUE);
		sortedIntField.setStored(false);
		sortedIntField.setIndexed(true);
		
		IntField intFieldSorted = new IntField("int_value_sort", 100, sortedIntField);
		
		Document document = new Document();
		document.add(intField);
		document.add(longField);
		document.add(floatField);
		document.add(doubleField);
		document.add(intFieldSorted);
		
		//IndexWriter indexWriter = LuceneTool.getIndexWriter();
		//indexWriter.addDocument(document);
		//indexWriter.commit();
		
		//IndexSearcher indexSearcher = LuceneTool.getIndexSearcher();
		

	}
}
