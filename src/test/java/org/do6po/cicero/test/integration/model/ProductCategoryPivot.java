package org.do6po.cicero.test.integration.model;

import org.do6po.cicero.annotation.ModelProjection;

@ModelProjection(
    table = "product_category",
    columns = {
      "product_id",
      "category_id",
    })
public class ProductCategoryPivot {}
