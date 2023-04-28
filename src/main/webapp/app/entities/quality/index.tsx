import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Quality from './quality';
import QualityDetail from './quality-detail';
import QualityUpdate from './quality-update';
import QualityDeleteDialog from './quality-delete-dialog';

const QualityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Quality />} />
    <Route path="new" element={<QualityUpdate />} />
    <Route path=":id">
      <Route index element={<QualityDetail />} />
      <Route path="edit" element={<QualityUpdate />} />
      <Route path="delete" element={<QualityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default QualityRoutes;
