import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './language.reducer';

export const LanguageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const languageEntity = useAppSelector(state => state.hr.language.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="languageDetailsHeading">
          <Translate contentKey="hrApp.language.detail.title">Language</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{languageEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="hrApp.language.name">Name</Translate>
            </span>
          </dt>
          <dd>{languageEntity.name}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hrApp.language.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{languageEntity.createdAt ? <TextFormat value={languageEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hrApp.language.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{languageEntity.updatedAt ? <TextFormat value={languageEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="hrApp.language.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>{languageEntity.deletedAt ? <TextFormat value={languageEntity.deletedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/language" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/language/${languageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LanguageDetail;
