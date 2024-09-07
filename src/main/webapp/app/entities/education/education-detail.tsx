import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './education.reducer';

export const EducationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const educationEntity = useAppSelector(state => state.hr.education.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="educationDetailsHeading">
          <Translate contentKey="hrApp.education.detail.title">Education</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{educationEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="hrApp.education.name">Name</Translate>
            </span>
          </dt>
          <dd>{educationEntity.name}</dd>
          <dt>
            <span id="faculty">
              <Translate contentKey="hrApp.education.faculty">Faculty</Translate>
            </span>
          </dt>
          <dd>{educationEntity.faculty}</dd>
          <dt>
            <span id="level">
              <Translate contentKey="hrApp.education.level">Level</Translate>
            </span>
          </dt>
          <dd>{educationEntity.level}</dd>
          <dt>
            <span id="degree">
              <Translate contentKey="hrApp.education.degree">Degree</Translate>
            </span>
          </dt>
          <dd>{educationEntity.degree}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="hrApp.education.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {educationEntity.startDate ? <TextFormat value={educationEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="hrApp.education.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>{educationEntity.endDate ? <TextFormat value={educationEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="activities">
              <Translate contentKey="hrApp.education.activities">Activities</Translate>
            </span>
          </dt>
          <dd>{educationEntity.activities}</dd>
          <dt>
            <span id="clubs">
              <Translate contentKey="hrApp.education.clubs">Clubs</Translate>
            </span>
          </dt>
          <dd>{educationEntity.clubs}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hrApp.education.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {educationEntity.createdAt ? <TextFormat value={educationEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hrApp.education.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {educationEntity.updatedAt ? <TextFormat value={educationEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="hrApp.education.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>
            {educationEntity.deletedAt ? <TextFormat value={educationEntity.deletedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="hrApp.education.userProfile">User Profile</Translate>
          </dt>
          <dd>{educationEntity.userProfile ? educationEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/education" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/education/${educationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EducationDetail;
