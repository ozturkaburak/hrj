import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserLanguage from './user-language';
import UserLanguageDetail from './user-language-detail';
import UserLanguageUpdate from './user-language-update';
import UserLanguageDeleteDialog from './user-language-delete-dialog';

const UserLanguageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserLanguage />} />
    <Route path="new" element={<UserLanguageUpdate />} />
    <Route path=":id">
      <Route index element={<UserLanguageDetail />} />
      <Route path="edit" element={<UserLanguageUpdate />} />
      <Route path="delete" element={<UserLanguageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserLanguageRoutes;
