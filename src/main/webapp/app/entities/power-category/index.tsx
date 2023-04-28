import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PowerCategory from './power-category';
import PowerCategoryDetail from './power-category-detail';
import PowerCategoryUpdate from './power-category-update';
import PowerCategoryDeleteDialog from './power-category-delete-dialog';

const PowerCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PowerCategory />} />
    <Route path="new" element={<PowerCategoryUpdate />} />
    <Route path=":id">
      <Route index element={<PowerCategoryDetail />} />
      <Route path="edit" element={<PowerCategoryUpdate />} />
      <Route path="delete" element={<PowerCategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PowerCategoryRoutes;
