package com.Rakhi1999.Ecommerce_Shop.service.interf;

import com.Rakhi1999.Ecommerce_Shop.dto.CategoryDto;
import com.Rakhi1999.Ecommerce_Shop.dto.Response;

public interface CategoryService {

    Response createCategory(CategoryDto categoryRequest);
    Response updateCategory(Long categoryId, CategoryDto categoryRequest);
    Response getAllCategories();
    Response getCategoryById(Long categoryId);
    Response deleteCategory(Long categoryId);
}
