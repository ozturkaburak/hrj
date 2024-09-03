import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Certificate from './certificate';
import CertificateDetail from './certificate-detail';
import CertificateUpdate from './certificate-update';
import CertificateDeleteDialog from './certificate-delete-dialog';

const CertificateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Certificate />} />
    <Route path="new" element={<CertificateUpdate />} />
    <Route path=":id">
      <Route index element={<CertificateDetail />} />
      <Route path="edit" element={<CertificateUpdate />} />
      <Route path="delete" element={<CertificateDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CertificateRoutes;
