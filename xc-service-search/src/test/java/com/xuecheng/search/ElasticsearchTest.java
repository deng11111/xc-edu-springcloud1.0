package com.xuecheng.search;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by mrt on 2018/4/24.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchTest {

    @Autowired
    RestHighLevelClient client;

    @Autowired
    RestClient restClient;

//    @Before
//    public void setup() {
//        client = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost("localhost", 9200, "http")));
//        restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();
//    }

    @After
    public void after() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //创建索引库
    @Test
    public void testCreateIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("xuecheng_index");
        request.settings(Settings.builder()
                        .put("index.number_of_shards", 1)
                        .put("index.number_of_replicas", 0)
        );
        //创建索引时创建文档类型映射
        request.mapping("xc_course", "{\n" +
                        "   \"properties\" : {\n" +
                        "      \"description\" : {\n" +
                        "         \"analyzer\" : \"ik_max_word\",\n" +
                        "         \"type\" : \"text\"\n" +
                        "      },\n" +
                        "      \"name\" : {\n" +
                        "         \"analyzer\" : \"ik_max_word\",\n" +
                        "         \"type\" : \"text\"\n" +
                        "      },\n" +
                        "      \"price\" : {\n" +
                        "         \"scaling_factor\" : 100,\n" +
                        "         \"type\" : \"scaled_float\"\n" +
                        "      },\n" +
                        "      \"studymodel\" : {\n" +
                        "         \"index\" : true,\n" +
                        "         \"type\" : \"keyword\"\n" +
                        "      },\n" +
                        "      \"timestamp\" : {\n" +
                        "         \"format\" : \"yyyy-MM-dd HH:mm:ss||epoch_millis\",\n" +
                        "         \"type\" : \"date\"\n" +
                        "      }\n" +
                        "   }\n" +
                        "}\n",
                XContentType.JSON);
        CreateIndexResponse createIndexResponse = client.indices().create(request);
        boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
        System.out.println(shardsAcknowledged);

    }
    //删除索引库
    @Test
    public void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("xuecheng_index");
        DeleteIndexResponse deleteIndexResponse = client.indices().delete(request);
        boolean acknowledged = deleteIndexResponse.isAcknowledged();
        System.out.println(acknowledged);

    }

    //添加文档
    @Test
    public void addDoc() throws IOException {
        /**
         {
         "name":"spring cloud实战",
         "discription":"本课程主要从四个章节进行讲解： 1.微服务架构入门 2.spring cloud 基础入门 3.实战Spring Boot 4.注册中心eureka。",
         "studymodel":"201001",
         "timestamp":"2018-1-1 00:00:00",
         "price":5.6
         }
         */
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "spring cloud实战");
        jsonMap.put("discription", "本课程主要从四个章节进行讲解： 1.微服务架构入门 2.spring cloud 基础入门 3.实战Spring Boot 4.注册中心eureka。");
        jsonMap.put("studymodel", "201001");
        jsonMap.put("timestamp", dateFormat.format(new Date()));
        jsonMap.put("price", 5.6f);
        //IndexRequest indexRequest = new IndexRequest("xuecheng_index", "xc_course") //不指定ID则由ES自动生成
        IndexRequest indexRequest = new IndexRequest("xuecheng_index", "xc_course", "4028e581617f945f01617f9dabc40000")
                .source(jsonMap); // <1>
        //end::index-request-map
        IndexResponse indexResponse = client.index(indexRequest);
        assertEquals(indexResponse.getResult(), DocWriteResponse.Result.CREATED);
    }
    //更新文档
    @Test
    public void updateDoc() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("xuecheng_index", "xc_course", "4028e581617f945f01617f9dabc40000");
        Map<String, String> map = new HashMap<>();
        map.put("name", "spring cloud实战");
        updateRequest.doc(map);
        UpdateResponse update = client.update(updateRequest);
        assertSame(update.getResult(), DocWriteResponse.Result.UPDATED);
//        RestStatus status = indexResponse.status();
//        assertSame(status, RestStatus.OK);
    }
    //删除文档
    @Test
    public void deleteDoc() throws IOException {
        DeleteRequest request = new DeleteRequest("xuecheng_index", "xc_course", "4028e581617f945f01617f9dabc40000");
        // tag::delete-execute
        DeleteResponse deleteResponse = client.delete(request);

        // end::delete-execute
        assertSame(deleteResponse.getResult(), DocWriteResponse.Result.DELETED);
    }

    //根据条件删除文档
    @Test
    public void deleteDocByQuery() throws IOException {
        String queryString="{\n" +
                "    \"query\":{\n" +
                "        \"term\":{\n" +
                "            \"studymodel\":\"201001\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        String endPoint = "/xuecheng_index/xc_course/_delete_by_query";
        HttpEntity entity = new NStringEntity(queryString, ContentType.APPLICATION_JSON);
        try {
            restClient.performRequest("POST", endPoint, Collections.<String, String>emptyMap(),
                    entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //删除所有文档
    @Test
    public void deleteDocAll() throws IOException {
        String queryString="{\n" +
                "    \"query\": {\n" +
                "        \"match_all\": {}\n" +
                "    }\n" +
                "}";
        String endPoint = "/xuecheng_index/xc_course/_delete_by_query";
        HttpEntity entity = new NStringEntity(queryString, ContentType.APPLICATION_JSON);
        try {
            Response post = restClient.performRequest("POST", endPoint, Collections.<String, String>emptyMap(),
                    entity);
            InputStream content = post.getEntity().getContent();
            String s = IOUtils.toString(content);
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查询文档
    @Test
    public void getDoc() throws IOException {
        GetRequest getRequest = new GetRequest(
                "xuecheng_index",
                "xc_course",
                "4028e581617f945f01617f9dabc40000");
        GetResponse getResponse = client.get(getRequest);
        assertTrue(getResponse.isExists());
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        System.out.println(sourceAsMap);
    }

    //搜索type下的全部记录
    @Test
    public void testSearchAll() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xuecheng_index");
        searchRequest.types("xc_course");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //source源字段过虑
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }

    //根据关键字搜索，单项匹配
    @Test
    public void testSearchByKeyword() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xuecheng_index");
        searchRequest.types("xc_course");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //source源字段过虑
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"}, new String[]{});
        //匹配关键字
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("description", "spring 前台页面");
        //设置匹配占比
        matchQueryBuilder.minimumShouldMatch("50%");

        searchSourceBuilder.query(matchQueryBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }

    //根据关键字搜索，多项匹配
    @Test
    public void testSearchByKeyword2() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xuecheng_index");
        searchRequest.types("xc_course");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //source源字段过虑
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"}, new String[]{});
        //匹配关键字
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring 前台页面", "name", "description");
        //设置匹配占比
        multiMatchQueryBuilder.minimumShouldMatch("50%");
        //提升另个字段的Boost值
        multiMatchQueryBuilder.field("name",2);
        //设置匹配类型（默认为best_fields）
        multiMatchQueryBuilder.type("best_fields");
        searchSourceBuilder.query(multiMatchQueryBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }

    //精确匹配
    @Test
    public void testSearchByTermQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String ids = "4028e581617f945f01617f9dabc40000,297e7c7c62b888f00162b8a7dec20000";
        String[] split = ids.split(",");
        List<String> idList = Arrays.asList(split);
        searchSourceBuilder.query(QueryBuilders.termsQuery("id", idList));
//        searchSourceBuilder.query(QueryBuilders.termQuery("name","spring"));
        //分页查询，设置起始下标
        searchSourceBuilder.from(0);
        //每页显示个数
        searchSourceBuilder.size(10);
        //排序
        searchSourceBuilder.sort(new FieldSortBuilder("studymodel").order(SortOrder.ASC));
        searchSourceBuilder.sort(new FieldSortBuilder("price").order(SortOrder.DESC));
        //source源字段过虑
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }

    //布尔查询
    @Test
    public void testSearchByBoolQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xuecheng_index");
        searchRequest.types("xc_course");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //分页查询，设置起始下标
        searchSourceBuilder.from(0);
        //每页显示个数
        searchSourceBuilder.size(10);
        //排序
        searchSourceBuilder.sort(new FieldSortBuilder("studymodel").order(SortOrder.ASC));
        searchSourceBuilder.sort(new FieldSortBuilder("price").order(SortOrder.DESC));
        //布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.termQuery("studymodel", "201001"));
        boolQueryBuilder.should(QueryBuilders.termQuery("studymodel", "201002"));
        //过虑
        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel","201001"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("timestamp").gte("2018-1-1 9:9:8").lte("2018-1-1 9:9:8"));
        //source源字段过虑
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"}, new String[]{});
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }
}
