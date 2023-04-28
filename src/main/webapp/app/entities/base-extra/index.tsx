import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BaseExtra from './base-extra';
import BaseExtraDetail from './base-extra-detail';
import BaseExtraUpdate from './base-extra-update';
import BaseExtraDeleteDialog from './base-extra-delete-dialog';

const BaseExtraRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BaseExtra />} />
    <Route path="new" element={<BaseExtraUpdate />} />
    <Route path=":id">
      <Route index element={<BaseExtraDetail />} />
      <Route path="edit" element={<BaseExtraUpdate />} />
      <Route path="delete" element={<BaseExtraDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BaseExtraRoutes;
