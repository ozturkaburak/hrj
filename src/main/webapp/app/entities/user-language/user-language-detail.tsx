import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-language.reducer';

export const UserLanguageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userLanguageEntity = useAppSelector(state => state.hr.userLanguage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userLanguageDetailsHeading">
          <Translate contentKey="hrApp.userLanguage.detail.title">UserLanguage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userLanguageEntity.id}</dd>
          <dt>
            <span id="level">
              <Translate contentKey="hrApp.userLanguage.level">Level</Translate>
            </span>
          </dt>
          <dd>{userLanguageEntity.level}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hrApp.userLanguage.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {userLanguageEntity.createdAt ? <TextFormat value={userLanguageEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hrApp.userLanguage.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {userLanguageEntity.updatedAt ? <TextFormat value={userLanguageEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="hrApp.userLanguage.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>
            {userLanguageEntity.deletedAt ? <TextFormat value={userLanguageEntity.deletedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="hrApp.userLanguage.user">User</Translate>
          </dt>
          <dd>{userLanguageEntity.user ? userLanguageEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="hrApp.userLanguage.language">Language</Translate>
          </dt>
          <dd>{userLanguageEntity.language ? userLanguageEntity.language.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-language" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-language/${userLanguageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserLanguageDetail;
