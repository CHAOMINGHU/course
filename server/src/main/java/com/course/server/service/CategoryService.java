package com.course.server.service;

import com.course.server.domain.Category;
import com.course.server.domain.CategoryExample;
import com.course.server.dto.CategoryDto;
import com.course.server.dto.PageDto;
import com.course.server.mapper.CategoryMapper;
import com.course.server.util.CopyUtil;
import com.course.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;



@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    public void list(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(),pageDto.getSize()); //对遇到第一个 sql 语句 进行分页
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        pageDto.setTotal(pageInfo.getTotal());
        List<CategoryDto> categoryDtoList = CopyUtil.copyList(categoryList,CategoryDto.class);
        pageDto.setList(categoryDtoList);
    }

    public List<CategoryDto> all(){
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        List<CategoryDto> categoryDtoList = CopyUtil.copyList(categoryList,CategoryDto.class);
        return categoryDtoList;
    }

    /**
     * 列表查询
     */
    public void save(CategoryDto categoryDto) {
//        categoryDto.setId(UuidUtil.getShortUuid());
//        Category category = new Category();
//        BeanUtils.copyProperties(categoryDto,category);
//        categoryMapper.insert(category);

        Category category = CopyUtil.copy(categoryDto,Category.class);
        if(StringUtils.isEmpty(categoryDto.getId())){
            this.insert(category);
        }else{
            this.update(category);
        }

    }

    private void insert(Category category){
        category.setId(UuidUtil.getShortUuid());
        categoryMapper.insert(category);
    }


    private void update(Category category){

        categoryMapper.updateByPrimaryKey(category);
    }


    @Transactional
    public void delete(String id){
        deleteChildren(id);
        categoryMapper.deleteByPrimaryKey(id);
    }


    /**
     * 删除子分类
     * @param id
     */
    public void deleteChildren(String id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        if("00000000".equals(category.getParent())){
            CategoryExample example = new CategoryExample();
            example.createCriteria().andParentEqualTo(category.getParent());
            categoryMapper.deleteByExample(example);
        }

    }


}
