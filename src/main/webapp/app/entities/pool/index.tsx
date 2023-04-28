import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Pool from './pool';
import PoolDetail from './pool-detail';
import PoolUpdate from './pool-update';
import PoolDeleteDialog from './pool-delete-dialog';

const PoolRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Pool />} />
    <Route path="new" element={<PoolUpdate />} />
    <Route path=":id">
      <Route index element={<PoolDetail />} />
      <Route path="edit" element={<PoolUpdate />} />
      <Route path="delete" element={<PoolDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PoolRoutes;
