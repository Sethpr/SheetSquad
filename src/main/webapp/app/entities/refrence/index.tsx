import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Refrence from './refrence';
import RefrenceDetail from './refrence-detail';
import RefrenceUpdate from './refrence-update';
import RefrenceDeleteDialog from './refrence-delete-dialog';

const RefrenceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Refrence />} />
    <Route path="new" element={<RefrenceUpdate />} />
    <Route path=":id">
      <Route index element={<RefrenceDetail />} />
      <Route path="edit" element={<RefrenceUpdate />} />
      <Route path="delete" element={<RefrenceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RefrenceRoutes;
