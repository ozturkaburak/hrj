import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Education from './education';
import EducationDetail from './education-detail';
import EducationUpdate from './education-update';
import EducationDeleteDialog from './education-delete-dialog';

const EducationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Education />} />
    <Route path="new" element={<EducationUpdate />} />
    <Route path=":id">
      <Route index element={<EducationDetail />} />
      <Route path="edit" element={<EducationUpdate />} />
      <Route path="delete" element={<EducationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EducationRoutes;
