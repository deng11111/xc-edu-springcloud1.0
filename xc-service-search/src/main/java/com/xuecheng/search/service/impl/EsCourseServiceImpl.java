package com.xuecheng.search.service.impl;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.search.service.EsCourseService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2018/2/18.
 */
@Service
public class EsCourseServiceImpl implements EsCourseService {

    @Value("${xuecheng.elasticsearch.course.index}")
    private String es_index;
    @Value("${xuecheng.elasticsearch.course.type}")
    private String es_type;
    @Value("${xuecheng.elasticsearch.course.source_field}")
    private String source_field= "{\"children\":[{\"children\":[{\"grade\":\"3\",\"id\":\"4028e58161bd269f0161bd2778750001\",\"pname\":\"第一节 vue基础\"},{\"grade\":\"3\",\"id\":\"4028e58161bd269f0161bd27d7c50002\",\"pname\":\"第二节 属性和事件、模板、交互、案例\"}],\"grade\":\"2\",\"id\":\"4028e58161bd269f0161bd270a340000\",\"pname\":\"Vuejs 第一讲\"},{\"children\":[{\"grade\":\"3\",\"id\":\"4028e58161bd269f0161bd2877740005\",\"pname\":\"第一节 计算属性的使用、vue实例的简单方法\"},{\"grade\":\"3\",\"id\":\"4028e58161bd269f0161bd293df90006\",\"pname\":\"第二节 自定义过滤器、自定义指令\"}],\"grade\":\"2\",\"id\":\"4028e58161bd269f0161bd281bde0003\",\"pname\":\"Vuejs 第二讲\"},{\"children\":[],\"grade\":\"2\",\"id\":\"4028e58161bd269f0161bd284bad0004\",\"pname\":\"Vuejs 第三讲\"}],\"grade\":\"1\",\"id\":\"4028e58161bd22e60161bd2366fb0000\",\"pname\":\"Javascript之VueJS\"}";

   @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public String list(int page,int size,CourseSearchParam courseSearchParam) throws IOException {
        SearchRequest searchRequest = new SearchRequest(es_index);
        searchRequest.types(es_type);
        // tag::search-request-highlighting
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //source源字段过虑
        String[] source_fields = source_field.split(",");
        searchSourceBuilder.fetchSource(source_fields, new String[]{});
        //关键字
        if(StringUtils.isNotEmpty(courseSearchParam.getKeyword())){
            //匹配关键字
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(), "name", "description");
            //设置匹配占比
            multiMatchQueryBuilder.minimumShouldMatch("70%");
            //提升另个字段的Boost值
            multiMatchQueryBuilder.field("name",10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }

        //过虑
        if(StringUtils.isNotEmpty(courseSearchParam.getMt())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getMt()));
        }
        if(StringUtils.isNotEmpty(courseSearchParam.getSt())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("st",courseSearchParam.getSt()));
        }
        if(StringUtils.isNotEmpty(courseSearchParam.getGrade())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade",courseSearchParam.getGrade()));
        }
        //分页
        if(page<=0){
            page = 1;
        }
        if(size<=0){
            size = 20;
        }
        int start = (page-1)*size;
        searchSourceBuilder.from(start);
        searchSourceBuilder.size(size);
        //布尔查询
        searchSourceBuilder.query(boolQueryBuilder);
        //请求搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        return searchResponse.toString();
    }

    @Override
    public Map<String,CourseInfo> getbase(String ids) throws IOException {
        SearchRequest searchRequest = new SearchRequest(es_index);
        searchRequest.types(es_type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String[] split = ids.split(",");
        List<String> idList = Arrays.asList(split);
        searchSourceBuilder.query(QueryBuilders.termsQuery("id", idList));
        //source源字段过虑
        searchSourceBuilder.fetchSource(new String[]{"name", "grade", "charge","pic"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        Map<String,CourseInfo> map = new HashMap<>();
        for (SearchHit hit : searchHits) {
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String grade = (String) sourceAsMap.get("grade");
            String charge = (String) sourceAsMap.get("charge");
            String pic = (String) sourceAsMap.get("pic");
            CourseInfo courseInfo  =new CourseInfo();
            courseInfo.setName(name);
            courseInfo.setPic(pic);
            courseInfo.setGrade(grade);
            map.put(id,courseInfo);
        }

        return map;
    }

    @Override
    public Map<String, CoursePub> getall(String id) {
        //设置索引库
        SearchRequest searchRequest = new SearchRequest(es_index);
        //设置类型
        searchRequest.types(es_type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //查询条件，根据课程id查询
        searchSourceBuilder.query(QueryBuilders.termsQuery("id", id));
        //取消source源字段过虑，查询所有字段
//        searchSourceBuilder.fetchSource(new String[]{"name", "grade", "charge","pic"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            //执行搜索
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取搜索结果
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        Map<String,CoursePub> map = new HashMap<>();
        for (SearchHit hit : searchHits) {
            String courseId = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String grade = (String) sourceAsMap.get("grade");
            String charge = (String) sourceAsMap.get("charge");
            String pic = (String) sourceAsMap.get("pic");
            String description = (String) sourceAsMap.get("description");
            String teachplan = (String) sourceAsMap.get("teachplan");
            CoursePub coursePub = new CoursePub();
            coursePub.setName(name);
            coursePub.setPic(pic);
            coursePub.setGrade(grade);
            coursePub.setTeachplan(teachplan);
            coursePub.setDescription(description);
            map.put(courseId,coursePub);
        }

        return map;
    }
}
