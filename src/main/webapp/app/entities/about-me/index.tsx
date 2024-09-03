import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AboutMe from './about-me';
import AboutMeDetail from './about-me-detail';
import AboutMeUpdate from './about-me-update';
import AboutMeDeleteDialog from './about-me-delete-dialog';

const AboutMeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AboutMe />} />
    <Route path="new" element={<AboutMeUpdate />} />
    <Route path=":id">
      <Route index element={<AboutMeDetail />} />
      <Route path="edit" element={<AboutMeUpdate />} />
      <Route path="delete" element={<AboutMeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AboutMeRoutes;
