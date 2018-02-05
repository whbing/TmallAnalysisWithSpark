package cn.whbing.spark.Tmall;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import cn.whbing.spark.Tmall.pojo.Product;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.*;  //静态导包

import scala.Function1;
import scala.Tuple2;

public class TopN {

	public static void main(String[] args) throws AnalysisException {
		SparkSession spark = SparkSession
				.builder()		
				.master("local")
				.appName("Tmall data analysis with spark SQL")
				.getOrCreate();
		
		// Encoders are created for Java beans
		Encoder<Product> propductEncoder = Encoders.bean(Product.class);

		//local模式下也是可以使用这个path的
		//String path = "hdfs://master-1a:9000/whbing/data/result-1-38.json";
		String path = "D:\\javaTools\\EclipseWork1\\taobaospider\\result\\r21-lingshi.json";
		//String path = "hdfs://master-1a:9000/whbing/data/r12-shouji.json";
		
		Dataset<Product> productDS = spark.read().json(path).as(propductEncoder);
		
		//将dataframe注册为临时视图
		productDS.createOrReplaceTempView("tmalldata");
		//String sqlText = "SELECT class1,avg(CAST(price AS DOUBLE)) AS avg_price,sum(CAST(sales AS DOUBLE)) as sum_sales,count(class1) AS class1_num FROM tmalldata GROUP BY class1 ORDER BY sum_sales DESC";
		String sqlText = "SELECT class2 ,class1,avg(CAST(price AS DOUBLE)) AS avg_price,sum(CAST(sales AS DOUBLE)) as sum_sales,count(class2) AS class2_num FROM tmalldata GROUP BY class2,class1 ORDER BY sum_sales DESC";
		Dataset<Row> sqlDF = spark.sql(sqlText);
		sqlDF.show(100);	
		//productDS.agg(avg(productDS.col("sales")), max("sales")).show();
		
	}
	
	

}
