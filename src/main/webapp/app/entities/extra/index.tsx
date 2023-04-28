import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Extra from './extra';
import ExtraDetail from './extra-detail';
import ExtraUpdate from './extra-update';
import ExtraDeleteDialog from './extra-delete-dialog';

const ExtraRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Extra />} />
    <Route path="new" element={<ExtraUpdate />} />
    <Route path=":id">
      <Route index element={<ExtraDetail />} />
      <Route path="edit" element={<ExtraUpdate />} />
      <Route path="delete" element={<ExtraDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ExtraRoutes;
