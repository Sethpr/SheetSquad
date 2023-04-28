import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Power from './power';
import PowerDetail from './power-detail';
import PowerUpdate from './power-update';
import PowerDeleteDialog from './power-delete-dialog';

const PowerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Power />} />
    <Route path="new" element={<PowerUpdate />} />
    <Route path=":id">
      <Route index element={<PowerDetail />} />
      <Route path="edit" element={<PowerUpdate />} />
      <Route path="delete" element={<PowerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PowerRoutes;
