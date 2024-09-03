import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserAssignment from './user-assignment';
import UserAssignmentDetail from './user-assignment-detail';
import UserAssignmentUpdate from './user-assignment-update';
import UserAssignmentDeleteDialog from './user-assignment-delete-dialog';

const UserAssignmentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserAssignment />} />
    <Route path="new" element={<UserAssignmentUpdate />} />
    <Route path=":id">
      <Route index element={<UserAssignmentDetail />} />
      <Route path="edit" element={<UserAssignmentUpdate />} />
      <Route path="delete" element={<UserAssignmentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserAssignmentRoutes;
