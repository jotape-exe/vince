package com.company.ourfinances.view.utils.patterns.impl

import com.company.ourfinances.model.entity.PaymentTypeEntity
import com.company.ourfinances.view.utils.patterns.EntityNameResolver
import com.company.ourfinances.viewmodel.FinanceActivityViewModel

class PaymentTypeResolver(private val viewModel: FinanceActivityViewModel) :
    EntityNameResolver<PaymentTypeEntity> {
    override fun getEntityNameById(id: Long?): String {
        return viewModel.getTypePaymentById(id!!).name
    }
}