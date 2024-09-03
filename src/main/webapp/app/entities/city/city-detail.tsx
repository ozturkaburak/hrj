import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './city.reducer';

export const CityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cityEntity = useAppSelector(state => state.hr.city.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cityDetailsHeading">
          <Translate contentKey="hrApp.city.detail.title">City</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cityEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="hrApp.city.name">Name</Translate>
            </span>
          </dt>
          <dd>{cityEntity.name}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hrApp.city.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{cityEntity.createdAt ? <TextFormat value={cityEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hrApp.city.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{cityEntity.updatedAt ? <TextFormat value={cityEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="hrApp.city.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>{cityEntity.deletedAt ? <TextFormat value={cityEntity.deletedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="hrApp.city.country">Country</Translate>
          </dt>
          <dd>{cityEntity.country ? cityEntity.country.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/city" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/city/${cityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CityDetail;
