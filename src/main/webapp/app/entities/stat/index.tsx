import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Stat from './stat';
import StatDetail from './stat-detail';
import StatUpdate from './stat-update';
import StatDeleteDialog from './stat-delete-dialog';

const StatRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Stat />} />
    <Route path="new" element={<StatUpdate />} />
    <Route path=":id">
      <Route index element={<StatDetail />} />
      <Route path="edit" element={<StatUpdate />} />
      <Route path="delete" element={<StatDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StatRoutes;
