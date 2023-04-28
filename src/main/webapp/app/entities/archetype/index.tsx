import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Archetype from './archetype';
import ArchetypeDetail from './archetype-detail';
import ArchetypeUpdate from './archetype-update';
import ArchetypeDeleteDialog from './archetype-delete-dialog';

const ArchetypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Archetype />} />
    <Route path="new" element={<ArchetypeUpdate />} />
    <Route path=":id">
      <Route index element={<ArchetypeDetail />} />
      <Route path="edit" element={<ArchetypeUpdate />} />
      <Route path="delete" element={<ArchetypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ArchetypeRoutes;
