import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './certificate.reducer';

export const CertificateDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const certificateEntity = useAppSelector(state => state.hr.certificate.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="certificateDetailsHeading">
          <Translate contentKey="hrApp.certificate.detail.title">Certificate</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{certificateEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="hrApp.certificate.name">Name</Translate>
            </span>
          </dt>
          <dd>{certificateEntity.name}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="hrApp.certificate.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {certificateEntity.startDate ? <TextFormat value={certificateEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="hrApp.certificate.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {certificateEntity.endDate ? <TextFormat value={certificateEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="hrApp.certificate.description">Description</Translate>
            </span>
          </dt>
          <dd>{certificateEntity.description}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hrApp.certificate.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {certificateEntity.createdAt ? <TextFormat value={certificateEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hrApp.certificate.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {certificateEntity.updatedAt ? <TextFormat value={certificateEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="hrApp.certificate.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>
            {certificateEntity.deletedAt ? <TextFormat value={certificateEntity.deletedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="hrApp.certificate.userProfile">User Profile</Translate>
          </dt>
          <dd>{certificateEntity.userProfile ? certificateEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/certificate" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/certificate/${certificateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CertificateDetail;
