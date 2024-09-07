import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './company.reducer';

export const CompanyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const companyEntity = useAppSelector(state => state.hr.company.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="companyDetailsHeading">
          <Translate contentKey="hrApp.company.detail.title">Company</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{companyEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="hrApp.company.name">Name</Translate>
            </span>
          </dt>
          <dd>{companyEntity.name}</dd>
          <dt>
            <span id="logo">
              <Translate contentKey="hrApp.company.logo">Logo</Translate>
            </span>
          </dt>
          <dd>{companyEntity.logo}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hrApp.company.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{companyEntity.createdAt ? <TextFormat value={companyEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hrApp.company.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{companyEntity.updatedAt ? <TextFormat value={companyEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="hrApp.company.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>{companyEntity.deletedAt ? <TextFormat value={companyEntity.deletedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="hrApp.company.active">Active</Translate>
            </span>
          </dt>
          <dd>{companyEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="hrApp.company.city">City</Translate>
          </dt>
          <dd>{companyEntity.city ? companyEntity.city.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/company" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/company/${companyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CompanyDetail;
