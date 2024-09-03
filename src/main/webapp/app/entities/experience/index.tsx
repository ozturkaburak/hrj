import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Experience from './experience';
import ExperienceDetail from './experience-detail';
import ExperienceUpdate from './experience-update';
import ExperienceDeleteDialog from './experience-delete-dialog';

const ExperienceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Experience />} />
    <Route path="new" element={<ExperienceUpdate />} />
    <Route path=":id">
      <Route index element={<ExperienceDetail />} />
      <Route path="edit" element={<ExperienceUpdate />} />
      <Route path="delete" element={<ExperienceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ExperienceRoutes;
