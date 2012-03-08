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

package com.akiban.sql.server;

import com.akiban.sql.optimizer.rule.CostEstimator;

import com.akiban.ais.model.Index;
import com.akiban.ais.model.Table;
import com.akiban.server.rowdata.RowDef;
import com.akiban.server.store.statistics.IndexStatistics;
import com.akiban.server.store.statistics.IndexStatisticsService;
import java.util.Properties;

public class ServerCostEstimator extends CostEstimator
{
    private ServerSession session;
    private IndexStatisticsService indexStatistics;
    private boolean scaleIndexStatistics;

    public ServerCostEstimator(ServerSession session,
                               ServerServiceRequirements reqs,
                               Properties compilerProperties) {
        this.session = session;
        indexStatistics = reqs.indexStatistics();
        scaleIndexStatistics = compilerProperties.getProperty("scaleIndexStatistics", "true").equals("true");
    }

    @Override
    public IndexStatistics getIndexStatistics(Index index) {
        return indexStatistics.getIndexStatistics(session.getSession(), index);
    }

    @Override
    public long getTableRowCount(Table table) {
        return table.rowDef().getTableStatus().getApproximateRowCount();
    }

    @Override
    protected boolean scaleIndexStatistics() {
        return scaleIndexStatistics;
    }

}
