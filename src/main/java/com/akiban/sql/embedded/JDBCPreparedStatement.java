/**
 * END USER LICENSE AGREEMENT (“EULA”)
 *
 * READ THIS AGREEMENT CAREFULLY (date: 9/13/2011):
 * http://www.akiban.com/licensing/20110913
 *
 * BY INSTALLING OR USING ALL OR ANY PORTION OF THE SOFTWARE, YOU ARE ACCEPTING
 * ALL OF THE TERMS AND CONDITIONS OF THIS AGREEMENT. YOU AGREE THAT THIS
 * AGREEMENT IS ENFORCEABLE LIKE ANY WRITTEN AGREEMENT SIGNED BY YOU.
 *
 * IF YOU HAVE PAID A LICENSE FEE FOR USE OF THE SOFTWARE AND DO NOT AGREE TO
 * THESE TERMS, YOU MAY RETURN THE SOFTWARE FOR A FULL REFUND PROVIDED YOU (A) DO
 * NOT USE THE SOFTWARE AND (B) RETURN THE SOFTWARE WITHIN THIRTY (30) DAYS OF
 * YOUR INITIAL PURCHASE.
 *
 * IF YOU WISH TO USE THE SOFTWARE AS AN EMPLOYEE, CONTRACTOR, OR AGENT OF A
 * CORPORATION, PARTNERSHIP OR SIMILAR ENTITY, THEN YOU MUST BE AUTHORIZED TO SIGN
 * FOR AND BIND THE ENTITY IN ORDER TO ACCEPT THE TERMS OF THIS AGREEMENT. THE
 * LICENSES GRANTED UNDER THIS AGREEMENT ARE EXPRESSLY CONDITIONED UPON ACCEPTANCE
 * BY SUCH AUTHORIZED PERSONNEL.
 *
 * IF YOU HAVE ENTERED INTO A SEPARATE WRITTEN LICENSE AGREEMENT WITH AKIBAN FOR
 * USE OF THE SOFTWARE, THE TERMS AND CONDITIONS OF SUCH OTHER AGREEMENT SHALL
 * PREVAIL OVER ANY CONFLICTING TERMS OR CONDITIONS IN THIS AGREEMENT.
 */

package com.akiban.sql.embedded;

import com.akiban.server.types.AkType;
import com.akiban.server.types.ValueSource;
import com.akiban.server.types3.TInstance;
import com.akiban.server.types3.pvalue.PValueSource;
import com.akiban.sql.server.ServerJavaValues;
import com.akiban.sql.server.ServerQueryContext;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.io.*;
import java.util.*;

public class JDBCPreparedStatement extends JDBCStatement implements PreparedStatement
{
    protected ExecutableStatement executableStatement;
    protected EmbeddedQueryContext context;
    protected final Values values = new Values();

    protected JDBCPreparedStatement(JDBCConnection connection, 
                                    ExecutableStatement executableStatement) {
        super(connection);
        this.executableStatement = executableStatement;
        context = new EmbeddedQueryContext(this);
    }

    protected class Values extends ServerJavaValues {
        @Override
        protected int size() {
            return executableStatement.getParameterMetaData().getParameters().size();
        }

        @Override
        protected ServerQueryContext getContext() {
            return context;
        }

        @Override
        protected ValueSource getValue(int index) {
            return context.getValue(index);
        }

        @Override
        protected void setValue(int index, ValueSource source, AkType akType) {
            context.setValue(index, source, akType);
        }

        @Override
        protected PValueSource getPValue(int index) {
            return context.getPValue(index);
        }

        @Override
        protected void setPValue(int index, PValueSource source) {
            context.setPValue(index, source);
        }

        @Override
        protected AkType getAkType(int index) {
            return executableStatement.getParameterMetaData().getParameter(index + 1).getAkType();
        }

        @Override
        protected TInstance getTInstance(int index) {
            return executableStatement.getParameterMetaData().getParameter(index + 1).getTInstance();
        }

        @Override
        protected ResultSet toResultSet(int index, Object resultSet) {
            throw new UnsupportedOperationException();
        }
    }

    /* PreparedStatement */

    @Override
    public ResultSet executeQuery() throws SQLException {
        return executeQueryInternal(executableStatement, context);
    }

    @Override
    public int executeUpdate() throws SQLException {
        return executeUpdateInternal(executableStatement, context);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        try {
            values.setNull(parameterIndex - 1);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        try {
            values.setBoolean(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        try {
            values.setByte(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        try {
            values.setShort(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        try {
            values.setInt(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        try {
            values.setLong(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        try {
            values.setFloat(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        try {
            values.setDouble(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        try {
            values.setBigDecimal(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        try {
            values.setString(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        try {
            values.setBytes(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        try {
            values.setDate(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        try {
            values.setTime(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        try {
            values.setTimestamp(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        try {
            values.setAsciiStream(parameterIndex - 1, x, length);
        }
        catch (IOException ex) {
            throw new JDBCException(ex);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        try {
            values.setUnicodeStream(parameterIndex - 1, x, length);
        }
        catch (IOException ex) {
            throw new JDBCException(ex);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        try {
            values.setBinaryStream(parameterIndex - 1, x, length);
        }
        catch (IOException ex) {
            throw new JDBCException(ex);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void clearParameters() throws SQLException {
        context.clear();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        setObject(parameterIndex, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        try {
            values.setObject(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public boolean execute() throws SQLException {
        return executeInternal(executableStatement, context);
    }

    @Override
    public void addBatch() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        try {
            values.setCharacterStream(parameterIndex - 1, reader, length);
        }
        catch (IOException ex) {
            throw new JDBCException(ex);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        try {
            values.setRef(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        try {
            values.setBlob(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        try {
            values.setClob(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        try {
            values.setArray(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return executableStatement.getResultSetMetaData();
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        try {
            values.setDate(parameterIndex - 1, x, cal);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        try {
            values.setTime(parameterIndex - 1, x, cal);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        try {
            values.setTimestamp(parameterIndex - 1, x, cal);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setNull (int parameterIndex, int sqlType, String typeName) throws SQLException {
        setNull(parameterIndex, sqlType);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        try {
            values.setURL(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return executableStatement.getParameterMetaData();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        try {
            values.setRowId(parameterIndex - 1, x);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        try {
            values.setNString(parameterIndex - 1, value);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        try {
            values.setNCharacterStream(parameterIndex - 1, value, length);
        }
        catch (IOException ex) {
            throw new JDBCException(ex);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        try {
            values.setNClob(parameterIndex - 1, value);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        try {
            values.setClob(parameterIndex - 1, reader, length);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        try {
            values.setBlob(parameterIndex - 1, inputStream, length);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        try {
            values.setNClob(parameterIndex - 1, reader, length);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        try {
            values.setSQLXML(parameterIndex - 1, xmlObject);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        setObject(parameterIndex, x, targetSqlType);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        try {
            values.setAsciiStream(parameterIndex - 1, x, length);
        }
        catch (IOException ex) {
            throw new JDBCException(ex);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        try {
            values.setBinaryStream(parameterIndex - 1, x, length);
        }
        catch (IOException ex) {
            throw new JDBCException(ex);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        try {
            values.setCharacterStream(parameterIndex - 1, reader, length);
        }
        catch (IOException ex) {
            throw new JDBCException(ex);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        try {
            values.setAsciiStream(parameterIndex - 1, x);
        }
        catch (IOException ex) {
            throw new JDBCException(ex);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        try {
            values.setBinaryStream(parameterIndex - 1, x);
        }
        catch (IOException ex) {
            throw new JDBCException(ex);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        try {
            values.setCharacterStream(parameterIndex - 1, reader);
        }
        catch (IOException ex) {
            throw new JDBCException(ex);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        try {
            values.setNCharacterStream(parameterIndex - 1, value);
        }
        catch (IOException ex) {
            throw new JDBCException(ex);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        try {
            values.setClob(parameterIndex - 1, reader);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        try {
            values.setBlob(parameterIndex - 1, inputStream);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        try {
            values.setNClob(parameterIndex - 1, reader);
        }
        catch (RuntimeException ex) {
            throw JDBCException.throwUnwrapped(ex);
        }
    }
}