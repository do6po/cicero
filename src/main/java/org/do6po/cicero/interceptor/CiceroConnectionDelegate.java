package org.do6po.cicero.interceptor;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.ShardingKey;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class CiceroConnectionDelegate implements Connection {

  private final Connection delegate;
  private final QueryCounter queryCounter;

  public CiceroConnectionDelegate(Connection delegate, QueryCounter queryCounter) {
    this.delegate = delegate;
    this.queryCounter = queryCounter;
  }

  @Override
  public Statement createStatement() throws SQLException {
    return delegate.createStatement();
  }

  @Override
  public PreparedStatement prepareStatement(String s) throws SQLException {
    queryCounter.tryIncrement();

    return delegate.prepareStatement(s);
  }

  @Override
  public CallableStatement prepareCall(String s) throws SQLException {
    queryCounter.tryIncrement();

    return delegate.prepareCall(s);
  }

  @Override
  public String nativeSQL(String s) throws SQLException {
    return delegate.nativeSQL(s);
  }

  @Override
  public void setAutoCommit(boolean b) throws SQLException {
    delegate.setAutoCommit(b);
  }

  @Override
  public boolean getAutoCommit() throws SQLException {
    return delegate.getAutoCommit();
  }

  @Override
  public void commit() throws SQLException {
    delegate.commit();
  }

  @Override
  public void rollback() throws SQLException {
    delegate.rollback();
  }

  @Override
  public void close() throws SQLException {
    delegate.close();
  }

  @Override
  public boolean isClosed() throws SQLException {
    return delegate.isClosed();
  }

  @Override
  public DatabaseMetaData getMetaData() throws SQLException {
    return delegate.getMetaData();
  }

  @Override
  public void setReadOnly(boolean b) throws SQLException {
    delegate.setReadOnly(b);
  }

  @Override
  public boolean isReadOnly() throws SQLException {
    return delegate.isReadOnly();
  }

  @Override
  public void setCatalog(String s) throws SQLException {
    delegate.setCatalog(s);
  }

  @Override
  public String getCatalog() throws SQLException {
    return delegate.getCatalog();
  }

  @Override
  public void setTransactionIsolation(int i) throws SQLException {
    delegate.setTransactionIsolation(i);
  }

  @Override
  public int getTransactionIsolation() throws SQLException {
    return delegate.getTransactionIsolation();
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    return delegate.getWarnings();
  }

  @Override
  public void clearWarnings() throws SQLException {
    delegate.clearWarnings();
  }

  @Override
  public Statement createStatement(int i, int i1) throws SQLException {
    return delegate.createStatement(i, i1);
  }

  @Override
  public PreparedStatement prepareStatement(String s, int i, int i1) throws SQLException {
    return delegate.prepareStatement(s, i, i1);
  }

  @Override
  public CallableStatement prepareCall(String s, int i, int i1) throws SQLException {
    return delegate.prepareCall(s, i, i1);
  }

  @Override
  public Map<String, Class<?>> getTypeMap() throws SQLException {
    return delegate.getTypeMap();
  }

  @Override
  public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    delegate.setTypeMap(map);
  }

  @Override
  public void setHoldability(int i) throws SQLException {
    delegate.setHoldability(i);
  }

  @Override
  public int getHoldability() throws SQLException {
    return delegate.getHoldability();
  }

  @Override
  public Savepoint setSavepoint() throws SQLException {
    return delegate.setSavepoint();
  }

  @Override
  public Savepoint setSavepoint(String s) throws SQLException {
    return delegate.setSavepoint(s);
  }

  @Override
  public void rollback(Savepoint savepoint) throws SQLException {
    delegate.rollback(savepoint);
  }

  @Override
  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    delegate.releaseSavepoint(savepoint);
  }

  @Override
  public Statement createStatement(int i, int i1, int i2) throws SQLException {
    return delegate.createStatement(i, i1, i2);
  }

  @Override
  public PreparedStatement prepareStatement(String s, int i, int i1, int i2) throws SQLException {
    return delegate.prepareStatement(s, i, i1, i2);
  }

  @Override
  public CallableStatement prepareCall(String s, int i, int i1, int i2) throws SQLException {
    return delegate.prepareCall(s, i, i1, i2);
  }

  @Override
  public PreparedStatement prepareStatement(String s, int i) throws SQLException {
    return delegate.prepareStatement(s, i);
  }

  @Override
  public PreparedStatement prepareStatement(String s, int[] ints) throws SQLException {
    return delegate.prepareStatement(s, ints);
  }

  @Override
  public PreparedStatement prepareStatement(String s, String[] strings) throws SQLException {
    return delegate.prepareStatement(s, strings);
  }

  @Override
  public Clob createClob() throws SQLException {
    return delegate.createClob();
  }

  @Override
  public Blob createBlob() throws SQLException {
    return delegate.createBlob();
  }

  @Override
  public NClob createNClob() throws SQLException {
    return delegate.createNClob();
  }

  @Override
  public SQLXML createSQLXML() throws SQLException {
    return delegate.createSQLXML();
  }

  @Override
  public boolean isValid(int i) throws SQLException {
    return delegate.isValid(i);
  }

  @Override
  public void setClientInfo(String s, String s1) throws SQLClientInfoException {
    delegate.setClientInfo(s, s1);
  }

  @Override
  public void setClientInfo(Properties properties) throws SQLClientInfoException {
    delegate.setClientInfo(properties);
  }

  @Override
  public String getClientInfo(String s) throws SQLException {
    return delegate.getClientInfo(s);
  }

  @Override
  public Properties getClientInfo() throws SQLException {
    return delegate.getClientInfo();
  }

  @Override
  public Array createArrayOf(String s, Object[] objects) throws SQLException {
    return delegate.createArrayOf(s, objects);
  }

  @Override
  public Struct createStruct(String s, Object[] objects) throws SQLException {
    return delegate.createStruct(s, objects);
  }

  @Override
  public void setSchema(String s) throws SQLException {
    delegate.setSchema(s);
  }

  @Override
  public String getSchema() throws SQLException {
    return delegate.getSchema();
  }

  @Override
  public void abort(Executor executor) throws SQLException {
    delegate.abort(executor);
  }

  @Override
  public void setNetworkTimeout(Executor executor, int i) throws SQLException {
    delegate.setNetworkTimeout(executor, i);
  }

  @Override
  public int getNetworkTimeout() throws SQLException {
    return delegate.getNetworkTimeout();
  }

  @Override
  public void beginRequest() throws SQLException {
    delegate.beginRequest();
  }

  @Override
  public void endRequest() throws SQLException {
    delegate.endRequest();
  }

  @Override
  public boolean setShardingKeyIfValid(
      ShardingKey shardingKey, ShardingKey superShardingKey, int timeout) throws SQLException {
    return delegate.setShardingKeyIfValid(shardingKey, superShardingKey, timeout);
  }

  @Override
  public boolean setShardingKeyIfValid(ShardingKey shardingKey, int timeout) throws SQLException {
    return delegate.setShardingKeyIfValid(shardingKey, timeout);
  }

  @Override
  public void setShardingKey(ShardingKey shardingKey, ShardingKey superShardingKey)
      throws SQLException {
    delegate.setShardingKey(shardingKey, superShardingKey);
  }

  @Override
  public void setShardingKey(ShardingKey shardingKey) throws SQLException {
    delegate.setShardingKey(shardingKey);
  }

  @Override
  public <T> T unwrap(Class<T> aClass) throws SQLException {
    return delegate.unwrap(aClass);
  }

  @Override
  public boolean isWrapperFor(Class<?> aClass) throws SQLException {
    return delegate.isWrapperFor(aClass);
  }
}
