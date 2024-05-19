package org.do6po.cicero.test.integration.model;

import java.util.List;
import lombok.Getter;
import org.do6po.cicero.annotation.ModelProjection;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.relation.ManyToMany;
import org.do6po.cicero.relation.ManyToOne;
import org.do6po.cicero.relation.MorphMany;
import org.do6po.cicero.relation.Relation;
import org.do6po.cicero.test.integration.model.ProductM_.Columns;
import org.do6po.cicero.test.integration.model.builder.BrandQB;
import org.do6po.cicero.test.integration.model.builder.CategoryQB;
import org.do6po.cicero.test.integration.model.builder.MediaQB;
import org.do6po.cicero.test.integration.model.builder.ProductQB;

@ModelProjection(
    table = "products",
    columns = {
      "id",
      "brand_id",
      "article",
      "name",
      "description",
      "created_at",
      "updated_at",
    })
@Getter
public class ProductM extends BaseModel<ProductM, ProductQB> {
  protected final String table = ProductM_.table;

  public String getId() {
    return getAttribute(Columns.id);
  }

  public Relation<ProductM, MediaM, MediaQB, List<MediaM>> media() {
    return new MorphMany<>(this, "id", MediaM.class, "reference_type", "reference_id");
  }

  public List<MediaM> getMedia() {
    return getRelation(ProductM::media);
  }

  public Relation<ProductM, CategoryM, CategoryQB, List<CategoryM>> categories() {
    return new ManyToMany<>(
        this,
        Columns.id,
        ProductCategoryPivot_.table,
        ProductCategoryPivot_.Columns.productId,
        ProductCategoryPivot_.Columns.categoryId,
        CategoryM_.Columns.id,
        CategoryM.class);
  }

  public List<CategoryM> getCategories() {
    return getRelation(ProductM::categories);
  }

  public Relation<ProductM, BrandM, BrandQB, BrandM> brand() {
    return new ManyToOne<>(this, Columns.brandId, BrandM.class, BrandM_.Columns.id);
  }

  public BrandM getBrand() {
    return getRelation(ProductM::brand);
  }
}
