package com.company.ourfinances.view.utils.patterns.impl

import com.company.ourfinances.model.entity.CategoryExpenseEntity
import com.company.ourfinances.view.utils.patterns.EntityNameResolver
import com.company.ourfinances.viewmodel.FinanceActivityViewModel

class CategoryTypeResolver(private val viewModel: FinanceActivityViewModel) :
    EntityNameResolver<CategoryExpenseEntity> {
    override fun getEntityNameById(id: Long?): String {
        return viewModel.getCategoryById(id!!).name
    }
}