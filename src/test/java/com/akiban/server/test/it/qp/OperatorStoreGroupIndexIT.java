/**
 * Copyright (C) 2011 Akiban Technologies Inc.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */
package com.akiban.server.test.it.qp;

import com.akiban.ais.model.Column;
import com.akiban.ais.model.GroupIndex;
import com.akiban.ais.model.IndexRowComposition;
import com.akiban.qp.persistitadapter.TestOperatorStore;
import com.akiban.qp.physicaloperator.UndefBindings;
import com.akiban.qp.row.Row;
import com.akiban.server.RowData;
import com.akiban.server.api.dml.scan.NewRow;
import com.akiban.server.service.Service;
import com.akiban.server.service.config.Property;
import com.akiban.server.store.Store;
import com.akiban.server.test.it.ITBase;
import com.akiban.util.Strings;
import com.persistit.exception.PersistitException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public final class OperatorStoreGroupIndexIT extends ITBase {

    @Test
    public void basic() {
        writeRows(
                createNewRow(c, 1L, "alpha"),
                createNewRow(o, 10L, 1L, "01-01-2001"),
                createNewRow(o, 11L, 1L, "02-02-2002"),
                createNewRow(i, 100L, 11L, 1111),
                createNewRow(i, 101L, 11L, 2222),
                createNewRow(i, 102L, 11L, 3333),
                createNewRow(o, 12L, 1L, "03-03-2003"),
                createNewRow(a, 20L, 1L, "Harrington"),
                createNewRow(a, 21L, 1L, "Causeway"),
                createNewRow(c, 2L, "beta")
        );
        String actionString = "testing! ";

        testMaintainedRows(
                actionString,
                createNewRow(c, 1L, "alpha"),
                // sku_name
                actionString + "sku_name [1111, alpha, 1, 11, 100]" + SKU_NAME_COLS,
                actionString + "sku_name [2222, alpha, 1, 11, 101]" + SKU_NAME_COLS,
                actionString + "sku_name [3333, alpha, 1, 11, 102]" + SKU_NAME_COLS,
                // street_aid_cid
                actionString + "street_aid_cid [Harrington, 20, 1, 1]" + STREET_AID_CID_COLS,
                actionString + "street_aid_cid [Causeway, 21, 1, 1]" + STREET_AID_CID_COLS
        );
    }

    @Test
    public void oiIndexOrphanedO() {
        writeRows(
                createNewRow(o, 11L, 1L, "02-02-2002"),
                createNewRow(i, 100L, 11L, 1111),
                createNewRow(i, 101L, 11L, 2222),
                createNewRow(i, 102L, 11L, 3333)
        );
        String actionString = "orphan test: ";

        testMaintainedRows(
                actionString,
                createNewRow(o, 11L, 1L, "02-02-2002"),
                // date sku
                actionString + "date_sku [02-02-2002, 1111, 1, 11, 100]" + DATE_SKU_COLS,
                actionString + "date_sku [02-02-2002, 2222, 1, 11, 101]" + DATE_SKU_COLS,
                actionString + "date_sku [02-02-2002, 3333, 1, 11, 102]" + DATE_SKU_COLS
        );
    }

    // Before and After

    @Before
    public void createTables() {
        testOperatorStore = (TestOperatorStore) store();
        c = createTable(SCHEMA, "customers",
                "cid int key",
                "name varchar(32)"
        );
        o = createTable(SCHEMA, "orders",
                "oid int key",
                "c_id int",
                "odate varchar(64)",
                "CONSTRAINT __akiban_o FOREIGN KEY __akiban_o(c_id) REFERENCES customers(cid)"
        );
        i = createTable(SCHEMA, "items",
                "iid int key",
                "o_id int",
                "sku int",
                "CONSTRAINT __akiban_i FOREIGN KEY __akiban_i(o_id) REFERENCES orders(oid)"
        );
        a = createTable(SCHEMA, "addresses",
                "aid int key",
                "c_id int",
                "street varchar(64)",
                "CONSTRAINT __akiban_o FOREIGN KEY __akiban_o(c_id) REFERENCES customers(cid)"
        );
        final String groupName = getUserTable(SCHEMA,"customers").getGroup().getName();
        createGroupIndex(groupName, "date_sku", "orders.odate, items.sku");
        createGroupIndex(groupName, "sku_name", "items.sku, customers.name");
        createGroupIndex(
                groupName,
                "street_aid_cid", "addresses.street, addresses.aid, customers.cid"
        );
    }

    @After
    public void after() {
        c = null;
        o = null;
        i = null;
        a = null;
        testOperatorStore = null;
    }

    // ApiTestBase interface

    @Override
    protected TestServiceServiceFactory createServiceFactory(Collection<Property> startupConfigProperties) {
        return new OSServiceFactory(startupConfigProperties);
    }

    // private methods

    void testMaintainedRows(String action, NewRow targetRow, String... expectedActions) {
        RowData rowData = targetRow.toRowData();
        StringsGIHandler handler = new StringsGIHandler();
        try {
            opStore().testMaintainGroupIndexes(session(), rowData, handler, action);
        } catch (PersistitException e) {
            throw new RuntimeException(e);
        }
        List<String> actual = Arrays.asList(expectedActions);
        List<String> expected = handler.strings();
        if (!expected.equals(actual)) {
            assertEquals("updates for " + targetRow, Strings.join(actual), Strings.join(expected));
            // and just in case...
            assertEquals("updates for " + targetRow, actual, expected);
        }
    }

    private TestOperatorStore opStore() {
        return testOperatorStore;
    }

    // object state

    private Integer c;
    private Integer o;
    private Integer i;
    private Integer a;
    private TestOperatorStore testOperatorStore;

    // const

    private final static String SCHEMA = "sch";
    private static final String DATE_SKU_COLS = " cols[orders.odate, items.sku, orders.c_id, items.o_id, items.iid]";
    private static final String SKU_NAME_COLS = " cols[items.sku, customers.name, orders.c_id, items.o_id, items.iid]";
    private static final String STREET_AID_CID_COLS = " cols[addresses.street, addresses.aid, customers.cid, addresses.c_id]";

    // nested classes

    private static class OSServiceFactory extends TestServiceServiceFactory {
        private OSServiceFactory(Collection<Property> startupConfigProperties) {
            super(startupConfigProperties);
        }

        @Override
        public Service<Store> storeService() {
            return new TestOperatorStore();
        }
    }

    private static class StringsGIHandler implements TestOperatorStore.GroupIndexHandler<String, RuntimeException> {

        // GroupIndexHandler interface

        @Override
        public void handleRow(String action, GroupIndex groupIndex, Row row) {
            String giName = groupIndex.getIndexName().getName();
            List<Object> fields = new ArrayList<Object>();
            List<Column> columns = new ArrayList<Column>();
            IndexRowComposition irc = groupIndex.indexRowComposition();
            for (int i=0; i < irc.getLength(); ++i ) {
                final int rowIndex;
                if (irc.isInHKey(i)) {
                    rowIndex = irc.getHKeyPosition(i);
                }
                else if (irc.isInRowData(i)) {
                    rowIndex = irc.getFieldPosition(i);
                } else {
                    throw new AssertionError(i);
                }
                fields.add(row.field(rowIndex, UndefBindings.only()));
                columns.add(groupIndex.getColumnForFlattenedRow(rowIndex));
            }
            strings.add(action + giName + ' ' + fields + " cols" + columns);
        }

        // StringsGIHandler interface

        public List<String> strings() {
            return strings;
        }

        // object state

        private final List<String> strings = new ArrayList<String>();
    }
}
