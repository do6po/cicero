package org.do6po.cicero.iterator;

import java.sql.SQLException;
import org.do6po.cicero.collection.ModelList;
import org.do6po.cicero.model.BaseModel;
import org.do6po.cicero.query.ModelQueryBuilder;

public class ModelChunkIterator<M extends BaseModel<M, B>, B extends ModelQueryBuilder<M, B>>
    extends PlantChunkIterator<M, B> {

  @SuppressWarnings("unchecked")
  public ModelChunkIterator(ModelQueryBuilder<M, B> builder, int size) throws SQLException {
    super((B) builder, size);
  }

  @Override
  public ModelList<M, B> next() {
    ModelList<M, B> chunk = new ModelList<>(super.next());
    chunk.load(builder.getWith());
    return chunk;
  }
}
