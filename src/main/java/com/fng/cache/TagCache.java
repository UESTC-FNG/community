package com.fng.cache;

import com.fng.dto.TagDTO;
import com.fng.exception.CustomizeErrorCode;
import com.fng.exception.CustomizeException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS=new ArrayList<>();
        TagDTO program=new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("js","php","css","html","java","node","python","javascript","C","html5","C#","swift","ruby","rust"));
        tagDTOS.add(program);

        TagDTO framework=new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("laravel","Spring","SpringBoot","express","flask","tornado","struts","koa"));
        tagDTOS.add(framework);

        TagDTO server=new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("linux","nginx","docker","apache","ubantu","centos","tomcat","负载均衡","unix","缓存"));
        tagDTOS.add(server);

        TagDTO sqlAndCache=new TagDTO();
        sqlAndCache.setCategoryName("数据库和缓存");
        sqlAndCache.setTags(Arrays.asList("sql","oracle","nosql","sqlserver","sqlite","mysql","h2","mybatis","redis","postgresql"));
        tagDTOS.add(sqlAndCache);

        TagDTO utils=new TagDTO();
        utils.setCategoryName("开发工具");
        utils.setTags(Arrays.asList("git","github","vim","xcode","idea","eclipse","maven","ide","atom","svn"));
        tagDTOS.add(utils);
        return tagDTOS;
    }

    public static boolean isValid(String tags){
        String[] inputTag = StringUtils.split(tags, ",|，");
        List<TagDTO> tagDTOS = TagCache.get();
        List<String> tagList=new ArrayList<>();
        for (TagDTO tagDTO :tagDTOS ){
            for (String tag:tagDTO.getTags()){
                tagList.add(tag);
            }
        }
        for (String tag:inputTag){
            if (!tagList.contains(tag)){
               return false;
            }
        }
        return true;
    }
}
