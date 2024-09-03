import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Upload from './upload';
import UploadDetail from './upload-detail';
import UploadUpdate from './upload-update';
import UploadDeleteDialog from './upload-delete-dialog';

const UploadRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Upload />} />
    <Route path="new" element={<UploadUpdate />} />
    <Route path=":id">
      <Route index element={<UploadDetail />} />
      <Route path="edit" element={<UploadUpdate />} />
      <Route path="delete" element={<UploadDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UploadRoutes;
